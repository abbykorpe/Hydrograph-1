/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package hydrograph.ui.graph.execution.tracking.connection;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.execution.tracking.datastructure.ComponentStatus;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.execution.tracking.datastructure.SubjobDetails;
import hydrograph.ui.graph.execution.tracking.logger.ExecutionTrackingFileLogger;
import hydrograph.ui.graph.execution.tracking.utils.ExecutionTrackingConsoleUtils;
import hydrograph.ui.graph.execution.tracking.windows.ExecutionTrackingConsole;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.model.CompStatus;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.google.gson.Gson;

/**
 * The Class HydrographUiClientSocket is use to get get execution tracking status and update the job canvas.
 */
@ClientEndpoint
public class HydrographUiClientSocket {

	/** The session. */
	private Session session;
	
	/** The logger. */
	private Logger logger = LogFactory.INSTANCE.getLogger(HydrographUiClientSocket.class);

	/**
	 * On open.
	 *
	 * @param session the session
	 */
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected to server");
		this.session = session;
	}
  
	/**
	 * 
	 * Called by web socket server, message contain execution tracking status that updated on job canvas.
	 *
	 * @param message the message
	 * @param session the session
	 */
	@OnMessage
	public void updateJobTrackingStatus(String message, Session session) { 
		
		
		
		final String status = message; 
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Gson gson = new Gson();
				ExecutionStatus executionStatus=gson.fromJson(status, ExecutionStatus.class);
				IWorkbenchPage page = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage();
				IEditorReference[] refs = page.getEditorReferences();
				
				for (IEditorReference ref : refs){
					IEditorPart editor = ref.getEditor(false);
					if(editor instanceof ELTGraphicalEditor){
						if(((ELTGraphicalEditor)editor).getJobId().equals(executionStatus.getJobId())){
							updateEditorWithCompStatus(executionStatus, (ELTGraphicalEditor)editor);
						}
					}
				}
			}
			/**
			 * Update component status and processed record 
			 * @param executionStatus
			 * @param editor
			 */
			private void updateEditorWithCompStatus(
					ExecutionStatus executionStatus, ELTGraphicalEditor editor) {
				Map<String, SubjobDetails> componentNameAndLink = new HashMap();
				if(executionStatus!=null)
				{
					pushExecutionStatusToExecutionTrackingConsole(executionStatus);
					
					ExecutionTrackingFileLogger.INSTANCE.log(executionStatus.getJobId(), executionStatus);

					if(editor!=null && editor instanceof ELTGraphicalEditor && (editor.getJobId().equals(executionStatus.getJobId())))
					{
						GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
						for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry().values().iterator(); 
								ite.hasNext();)
						{
							EditPart editPart = (EditPart) ite.next();
							if(editPart instanceof ComponentEditPart) 
							{
								Component component = ((ComponentEditPart)editPart).getCastedModel();

								if(Constants.SUBJOB_COMPONENT.equals(component.getComponentName())){
									String keySubjobName = component.getComponentLabel().getLabelContents();
									StringBuilder subjobPrefix = new StringBuilder("");
									populateSubjobRecordCount(componentNameAndLink,
											component,subjobPrefix);
									
									int successCount=0;
									updateStatusCountForSubjobComponent(
											executionStatus,
											componentNameAndLink, component,
											successCount, keySubjobName);

								}else {
									updateStatusCountForNormalComponent(
											executionStatus, component);

								}
							}
						}
					}
				}
				ExecutionTrackingConsoleUtils.INSTANCE.readFile(executionStatus, null);
			}

			private void updateStatusCountForSubjobComponent(
					ExecutionStatus executionStatus,
					Map<String, SubjobDetails> componentNameAndLink,
					Component component, int successCount, String keySubjobName) {
				for( ComponentStatus componentStatus: executionStatus.getComponentStatus()){
					boolean running = false;
					boolean pending = false;

					if(!pending){
						updateComponentPending(component, componentStatus, running);
					}
					
					if(!running){
						updateComponentRunning(component, componentStatus, running);
					} 
					
					if(componentStatus.getComponentId().contains(keySubjobName+".") && componentStatus.getCurrentStatus().equalsIgnoreCase(CompStatus.FAILED.value())){
						component.updateStatus(CompStatus.FAILED.value());
						break;
					}
					

					if(!componentNameAndLink.isEmpty()){
						for (Map.Entry<String, SubjobDetails> entry : componentNameAndLink.entrySet())
						{
							if(componentStatus.getComponentId().equals(entry.getKey())){

								for(Link link: component.getSourceConnections()){ 
									if(link.getSourceTerminal().toString().equals(((SubjobDetails)entry.getValue()).getTargetTerminal())){
										link.updateRecordCount(componentStatus.getProcessedRecordCount().get(((SubjobDetails)entry.getValue()).getSourceTerminal()).toString());
									}
								}
								if(componentStatus.getCurrentStatus().equalsIgnoreCase(CompStatus.SUCCESSFUL.value())){
									successCount++;
								}

								break;	
							}
						}
					}
					
					
					
					if(successCount == componentNameAndLink.size()){
						component.updateStatus(CompStatus.SUCCESSFUL.value());
					}
				}
			}

			private void updateStatusCountForNormalComponent(
					ExecutionStatus executionStatus, Component component) {
				
				
				for( ComponentStatus componentStatus: executionStatus.getComponentStatus()){
					if(componentStatus.getComponentId().equals(component.getComponentLabel().getLabelContents())){
						logger.info("Updating normal component {} status {}",component.getComponentLabel().getLabelContents(), componentStatus.getCurrentStatus());
						component.updateStatus(componentStatus.getCurrentStatus());
						for(Link link: component.getSourceConnections()){
							if(componentStatus.getComponentId().equals(link.getSource().getComponentLabel().getLabelContents())){
								link.updateRecordCount(componentStatus.getProcessedRecordCount().get(link.getSourceTerminal()).toString());
							}
						}
					}
				}
			}
			
			private void populateSubjobRecordCount(
					Map<String, SubjobDetails> componentNameAndLink, Component component,StringBuilder subjobPrefix) {
				Component outputSubjobComponent=(Component) component.getProperties().get(Messages.OUTPUT_SUBJOB_COMPONENT);
				for(Link link:outputSubjobComponent.getTargetConnections()){
					Component componentPrevToOutput = link.getSource();
					if(Constants.SUBJOB_COMPONENT.equals(componentPrevToOutput.getComponentName())){
						subjobPrefix.append(component.getComponentLabel().getLabelContents()+".");
						populateSubjobRecordCount(componentNameAndLink, componentPrevToOutput,subjobPrefix);
					}else{
					String portNumber = link.getTargetTerminal().replace(Messages.IN_PORT_TYPE, Messages.OUT_PORT_TYPE);
					
					SubjobDetails subjobDetails = new SubjobDetails(link.getSourceTerminal(), portNumber);
					
						componentNameAndLink.put(subjobPrefix+component.getComponentLabel().getLabelContents()+"."+componentPrevToOutput.getComponentLabel().getLabelContents(), subjobDetails);
						}
				}
			}
			
			private void updateComponentPending(Component component, ComponentStatus componentStatus, boolean started) {
				Component inputSubjobComponent=(Component) component.getProperties().get(Messages.INPUT_SUBJOB_COMPONENT);
				for(Link link:inputSubjobComponent.getSourceConnections()){
					Component componentNextToInput = link.getTarget();
					//if componentNextToInput is pending
					String compName = component.getComponentLabel().getLabelContents()+"."+componentNextToInput.getComponentLabel().getLabelContents();
					if(compName.equals(componentStatus.getComponentId()) && componentStatus.getCurrentStatus().equals(CompStatus.PENDING.value())){
						component.updateStatus(CompStatus.PENDING.value());
						started = true;
						break;
					}
				}
			}

			private void updateComponentRunning(Component component, ComponentStatus componentStatus, boolean started) {
				Component inputSubjobComponent=(Component) component.getProperties().get(Messages.INPUT_SUBJOB_COMPONENT);
				for(Link link:inputSubjobComponent.getSourceConnections()){
					Component componentNextToInput = link.getTarget();
					//if componentNextToInput is running
					String compName = component.getComponentLabel().getLabelContents()+"."+componentNextToInput.getComponentLabel().getLabelContents();
					if(compName.equals(componentStatus.getComponentId()) && componentStatus.getCurrentStatus().equals(CompStatus.RUNNING.value())){
						component.updateStatus(CompStatus.RUNNING.value());
						started = true;
						break;
					}
				}
			}
			
         });
	}

	/**
	 * On close.
	 *
	 * @param reason the reason
	 * @param session the session
	 */
	@OnClose
	public void onClose(CloseReason reason, Session session) {
		logger.info("Closing a WebSocket due to {}", reason.getReasonPhrase());
	} 

	/**
	 * Send message.
	 *
	 * @param str the str
	 */
	public void sendMessage(String str) {
		try {
			session.getBasicRemote().sendText(str);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Push execution status to execution tracking console.
	 *
	 * @param executionStatus the execution status
	 */
	private void pushExecutionStatusToExecutionTrackingConsole(
			ExecutionStatus executionStatus) {
		
		String jobId = getJobID(executionStatus);		
		ExecutionTrackingConsole console = JobManager.INSTANCE.getExecutionTrackingConsoles().get(jobId);
		if(console!=null){
			updateExecutionTrackingConsole(executionStatus, console);	
		}
	}

	/**
	 * Gets the job id.
	 *
	 * @param executionStatus the execution status
	 * @return the job id
	 */
	private String getJobID(ExecutionStatus executionStatus) {
		String jobId = executionStatus.getJobId();
		jobId = StringUtils.substringBeforeLast(jobId, "_");
		jobId = StringUtils.substringBeforeLast(jobId, "_");
		return jobId;
	}

	/**
	 * Update execution tracking console.
	 *
	 * @param executionStatus the execution status
	 * @param console the console
	 */
	private void updateExecutionTrackingConsole(
			final ExecutionStatus executionStatus,
			final ExecutionTrackingConsole console) {
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				console.clearConsole();
				console.setStatus(executionStatus, ExecutionTrackingConsoleUtils.INSTANCE.readFile(executionStatus, null));
			}
		});
	}
}