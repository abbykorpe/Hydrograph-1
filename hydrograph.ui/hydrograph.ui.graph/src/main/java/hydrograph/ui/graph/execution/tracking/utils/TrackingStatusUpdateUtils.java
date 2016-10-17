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

package hydrograph.ui.graph.execution.tracking.utils;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.execution.tracking.connection.HydrographUiClientSocket;
import hydrograph.ui.graph.execution.tracking.datastructure.ComponentStatus;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.execution.tracking.datastructure.SubjobDetails;
import hydrograph.ui.graph.execution.tracking.logger.ExecutionTrackingFileLogger;
import hydrograph.ui.graph.execution.tracking.windows.ExecutionTrackingConsole;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.ComponentExecutionStatus;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;


/**
 * The Class TrackingDisplayUtils.
 */
public class TrackingStatusUpdateUtils {

	
	/** The logger. */
	private Logger logger = LogFactory.INSTANCE.getLogger(HydrographUiClientSocket.class);

	/** The instance. */
	public static TrackingStatusUpdateUtils INSTANCE = new TrackingStatusUpdateUtils();

	private String CLONE_COMPONENT_TYPE ="CloneComponent";
	
	/**
	 * Instantiates a new tracking display utils.
	 */
	private TrackingStatusUpdateUtils() {
	}

	/**
	 * Update component status and processed record 
	 * @param executionStatus
	 * @param editor
	 */
	public void updateEditorWithCompStatus(ExecutionStatus executionStatus, ELTGraphicalEditor editor) {
		Map<String, SubjobDetails> componentNameAndLink = new HashMap();
		if (executionStatus != null) {
			
			
			/**
			 * Push the tracking log in tracking console
			 */
			pushExecutionStatusToExecutionTrackingConsole(executionStatus);
			ExecutionTrackingFileLogger.INSTANCE.log(executionStatus.getJobId(), executionStatus, JobManager.INSTANCE.isLocalMode());

				
				GraphicalViewer graphicalViewer = (GraphicalViewer) ((GraphicalEditor) editor).getAdapter(GraphicalViewer.class);
				
				for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry().values().iterator(); ite.hasNext();) {
					EditPart editPart = (EditPart) ite.next();
					if (editPart instanceof ComponentEditPart) {
						Component component = ((ComponentEditPart) editPart).getCastedModel();
					
						/**
						 * Updating status and record count of subjob
						 * component in main job.
						 */
						if (Constants.SUBJOB_COMPONENT.equals(component.getComponentName())) {
							String keySubjobName = component.getComponentLabel().getLabelContents();
							StringBuilder subjobPrefix = new StringBuilder("");
							populateSubjobRecordCount(componentNameAndLink, component, subjobPrefix);

							int successCount = 0;
							updateStatusCountForSubjobComponent(executionStatus, componentNameAndLink, component, successCount, keySubjobName);

						}else
						{
							updateStatusCountForSubjobComponent(executionStatus,component);
						}
					}
				}
			}
			ExecutionTrackingConsoleUtils.INSTANCE.readFile(executionStatus, null, JobManager.INSTANCE.isLocalMode());
	}
	
	private void updateStatusCountForSubjobComponent(
			ExecutionStatus executionStatus, Component component) {

		for( ComponentStatus componentStatus: executionStatus.getComponentStatus()){
			if(componentStatus.getComponentId().substring(componentStatus.getComponentId().lastIndexOf(".")+1).equals(component.getComponentLabel().getLabelContents())){
				logger.info("Updating normal component {} status {}",component.getComponentLabel().getLabelContents(), componentStatus.getCurrentStatus());
				component.updateStatus(componentStatus.getCurrentStatus());
				for(Link link: component.getSourceConnections()){
					if(componentStatus.getComponentId().substring(componentStatus.getComponentId().lastIndexOf(".")+1).equals(link.getSource().getComponentLabel().getLabelContents())){
						link.updateRecordCount(componentStatus.getProcessedRecordCount().get(link.getSourceTerminal()).toString());
					}
				}
			}
		}
	}

	private void updateStatusCountForSubjobComponent(
			ExecutionStatus executionStatus,
			Map<String, SubjobDetails> componentNameAndLink,
			Component component, int successCount, String keySubjobName) {
		boolean running = false;
		boolean pending = false;
		int componentCount = 0;

		for( ComponentStatus componentStatus: executionStatus.getComponentStatus()){

			if(!pending){
				updateComponentPending(component, componentStatus, pending);
			}

			if(!running){
				updateComponentRunning(component, componentStatus, running);
			} 

			if(componentStatus.getComponentId().contains(keySubjobName+".") && componentStatus.getCurrentStatus().equalsIgnoreCase(ComponentExecutionStatus.FAILED.value())){
				component.updateStatus(ComponentExecutionStatus.FAILED.value());
				break;
			}


			if(!componentNameAndLink.isEmpty()){
				for (Map.Entry<String, SubjobDetails> entry : componentNameAndLink.entrySet())
				{
					if(entry.getKey().contains(componentStatus.getComponentId())){
						System.out.println("map :"+entry.getKey());
						System.out.println("Status :"+componentStatus.getComponentId());
						List<String> portList = new ArrayList(componentStatus.getProcessedRecordCount().keySet());
						System.out.println("portList :"+portList);
						for(String port: portList){
							if((componentStatus.getComponentId()+"."+port).equals(entry.getKey())){
								for(Link link: component.getSourceConnections()){
									if(link.getSourceTerminal().toString().equals(((SubjobDetails)entry.getValue()).getTargetTerminal())){
										link.updateRecordCount(componentStatus.getProcessedRecordCount().get(((SubjobDetails)entry.getValue()).getSourceTerminal()).toString());
									}
								}
								if(componentStatus.getCurrentStatus().equalsIgnoreCase(ComponentExecutionStatus.SUCCESSFUL.value())){
									successCount++;
								}
								break;	
							}
							/*if(componentStatus.getCurrentStatus().equalsIgnoreCase(ComponentExecutionStatus.SUCCESSFUL.value())){
								successCount++;
							}*/
						}
					}else{
						continue;
					}

				}
			}else if(component.getProperties().get(Constants.TYPE).equals(Constants.STANDALONE_SUBJOB)){
				Container container = null;
				String filePath=null;

				filePath=(String) component.getProperties().get(Constants.PATH_PROPERTY_NAME);
				IPath jobFileIPath = new Path(filePath);
				if (ResourcesPlugin.getWorkspace().getRoot().getFile(jobFileIPath).exists()) {
					InputStream inp = null;
					try {
						inp = ResourcesPlugin.getWorkspace().getRoot().getFile(jobFileIPath).getContents();
					} catch (CoreException e) {
						logger.error("Not able to find subjob file", e);
					}
					container = (Container)CanvasUtils.INSTANCE.fromXMLToObject(inp);
					List<Component> subJobComponents = container.getChildren();
					componentCount = subJobComponents.size();
					for(Component subJobComponent : subJobComponents){
						String compName = component.getComponentLabel().getLabelContents()+"."+subJobComponent.getComponentLabel().getLabelContents();
						if(compName.equals(componentStatus.getComponentId()) && componentStatus.getCurrentStatus().equals(ComponentExecutionStatus.SUCCESSFUL.value())){
							successCount++;
						}
					}
				} 
			}
			System.out.println("componentNameAndLink size :"+componentNameAndLink.size());
			System.out.println("Size :"+successCount);
			//For subjobs other than stand alone
			if(successCount == componentNameAndLink.size() && !component.getProperties().get(Constants.TYPE).equals(Constants.STANDALONE_SUBJOB)){
				component.updateStatus(ComponentExecutionStatus.SUCCESSFUL.value());
			} 
			//For stand alone subjob
			else if(successCount == componentCount && component.getProperties().get(Constants.TYPE).equals(Constants.STANDALONE_SUBJOB)){
				component.updateStatus(ComponentExecutionStatus.SUCCESSFUL.value());
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
	
	/**
	 * 
	 * Populate the map for subjob status and record count.
	 * 
	 * @param componentNameAndLink map to hold port vs count
	 * @param component
	 * @param subjobPrefix use to identify inner subjob components
	 */
	private void populateSubjobRecordCount(Map<String, SubjobDetails> componentNameAndLink, Component component,StringBuilder subjobPrefix) {
		
		Component outputSubjobComponent=(Component) component.getProperties().get(Messages.OUTPUT_SUBJOB_COMPONENT);
		
		if(outputSubjobComponent!=null && (component.getProperties().get(Constants.TYPE).equals(Constants.INPUT)||
				component.getProperties().get(Constants.TYPE).equals(Constants.OPERATION))){
			for(Link link:outputSubjobComponent.getTargetConnections()){
				Component componentPrevToOutput = link.getSource();
				if(Constants.SUBJOB_COMPONENT.equals(componentPrevToOutput.getComponentName())){
					subjobPrefix.append(component.getComponentLabel().getLabelContents()+".");
					populateSubjobRecordCount(componentNameAndLink, componentPrevToOutput,subjobPrefix);
				}else{
					String portNumber = link.getTargetTerminal().replace(Messages.IN_PORT_TYPE, Messages.OUT_PORT_TYPE);

					SubjobDetails subjobDetails = new SubjobDetails(link.getSourceTerminal(), portNumber);
					if(CLONE_COMPONENT_TYPE.equalsIgnoreCase(componentPrevToOutput.getComponentName())){
						componentNameAndLink.put(subjobPrefix+component.getComponentLabel().getLabelContents()+"."+componentPrevToOutput.getComponentLabel().getLabelContents()+"."+portNumber, subjobDetails);
					}else{
						componentNameAndLink.put(subjobPrefix+component.getComponentLabel().getLabelContents()+"."+componentPrevToOutput.getComponentLabel().getLabelContents()+"."+subjobDetails.getSourceTerminal(), subjobDetails);
					}
				}
			}
		}else if(component.getProperties().get(Constants.TYPE).equals(Constants.OUTPUT)){
			Component inputSubjobComponent=(Component) component.getProperties().get(Messages.INPUT_SUBJOB_COMPONENT);
			for(Link link:inputSubjobComponent.getSourceConnections()){
				Component componentNextToInput = link.getTarget();
				if(Constants.SUBJOB_COMPONENT.equals(componentNextToInput.getComponentName())){
					subjobPrefix.append(component.getComponentLabel().getLabelContents()+".");
					populateSubjobRecordCount(componentNameAndLink, componentNextToInput,subjobPrefix);
				}else{
					String portNumber = link.getSourceTerminal().replace(Messages.IN_PORT_TYPE, Messages.OUT_PORT_TYPE);

					SubjobDetails subjobDetails = new SubjobDetails(link.getTargetTerminal(), portNumber);
					if(CLONE_COMPONENT_TYPE.equalsIgnoreCase(componentNextToInput.getComponentName())){
						componentNameAndLink.put(subjobPrefix+component.getComponentLabel().getLabelContents()+"."+componentNextToInput.getComponentLabel().getLabelContents()+"."+portNumber, subjobDetails);
					}else{
						componentNameAndLink.put(subjobPrefix+component.getComponentLabel().getLabelContents()+"."+componentNextToInput.getComponentLabel().getLabelContents()+"."+subjobDetails.getTargetTerminal(), subjobDetails);
					}
				}
			}
		}
	}

	private void updateComponentPending(Component component, ComponentStatus componentStatus, boolean pendingStatusApplied) {
		Component inputSubjobComponent=(Component) component.getProperties().get(Messages.INPUT_SUBJOB_COMPONENT);
		if(inputSubjobComponent!=null){
			for(Link link:inputSubjobComponent.getSourceConnections()){
				Component componentNextToInput = link.getTarget();
				//if componentNextToInput is pending
				String compName = component.getComponentLabel().getLabelContents()+"."+componentNextToInput.getComponentLabel().getLabelContents();
				if(compName.equals(componentStatus.getComponentId()) && componentStatus.getCurrentStatus().equals(ComponentExecutionStatus.PENDING.value())){
					component.updateStatus(ComponentExecutionStatus.PENDING.value());
					pendingStatusApplied = true;
					break;
				}
			}
		}else{
			Container container = null;
			String filePath=null;
			filePath=(String) component.getProperties().get(Constants.PATH_PROPERTY_NAME);
			IPath jobFileIPath = new Path(filePath);
			if (ResourcesPlugin.getWorkspace().getRoot().getFile(jobFileIPath).exists()) {
				InputStream inp = null;
				try {
					inp = ResourcesPlugin.getWorkspace().getRoot().getFile(jobFileIPath).getContents();
				} catch (CoreException e) {
					logger.error("Not able to find subjob file", e);
				}
				container = (Container)CanvasUtils.INSTANCE.fromXMLToObject(inp);
				List<Component> subJobComponents = container.getChildren();
				for(Component subJobComponent : subJobComponents){
					String compName = component.getComponentLabel().getLabelContents()+"."+subJobComponent.getComponentLabel().getLabelContents();
					if(compName.equals(componentStatus.getComponentId()) && componentStatus.getCurrentStatus().equals(ComponentExecutionStatus.PENDING.value())){
						component.updateStatus(ComponentExecutionStatus.PENDING.value());
						pendingStatusApplied = true;
						break;
					}
				}
			} 
			
		}
	}

	private void updateComponentRunning(Component component, ComponentStatus componentStatus, boolean runningStatusApplied) {
		Component inputSubjobComponent=(Component) component.getProperties().get(Messages.INPUT_SUBJOB_COMPONENT);
		if(inputSubjobComponent!=null){
			for(Link link:inputSubjobComponent.getSourceConnections()){
				Component componentNextToInput = link.getTarget();
				//if componentNextToInput is running
				String compName = component.getComponentLabel().getLabelContents()+"."+componentNextToInput.getComponentLabel().getLabelContents();
				if(compName.equals(componentStatus.getComponentId()) && componentStatus.getCurrentStatus().equals(ComponentExecutionStatus.RUNNING.value())){
					component.updateStatus(ComponentExecutionStatus.RUNNING.value());
					runningStatusApplied = true;
					break;
				}
			}
		}else{
			Container container = null;
			String filePath=null;
			filePath=(String) component.getProperties().get(Constants.PATH_PROPERTY_NAME);
			IPath jobFileIPath = new Path(filePath);
			if (ResourcesPlugin.getWorkspace().getRoot().getFile(jobFileIPath).exists()) {
				InputStream inp = null;
				try {
					inp = ResourcesPlugin.getWorkspace().getRoot().getFile(jobFileIPath).getContents();
				} catch (CoreException e) {
					logger.error("Not able to find subjob file", e);
				}
				container = (Container)CanvasUtils.INSTANCE.fromXMLToObject(inp);
				List<Component> subJobComponents = container.getChildren();
				for(Component subJobComponent : subJobComponents){
					String compName = component.getComponentLabel().getLabelContents()+"."+subJobComponent.getComponentLabel().getLabelContents();
					if(compName.equals(componentStatus.getComponentId()) && componentStatus.getCurrentStatus().equals(ComponentExecutionStatus.RUNNING.value())){
						component.updateStatus(ComponentExecutionStatus.RUNNING.value());
						runningStatusApplied = true;
						break; 
					}
				}
			} 
			
		}
	}

	/**
	 * Push execution status to execution tracking console.
	 *
	 * @param executionStatus the execution status
	 */
	private void pushExecutionStatusToExecutionTrackingConsole(
			ExecutionStatus executionStatus) {

		String jobId = executionStatus.getJobId();
		ExecutionTrackingConsole console = JobManager.INSTANCE.getExecutionTrackingConsoles().get(jobId);
		if(console!=null){
			updateExecutionTrackingConsole(executionStatus, console);	
		}
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
				console.setStatus(ExecutionTrackingConsoleUtils.INSTANCE.readFile(executionStatus, null, JobManager.INSTANCE.isLocalMode()));
			}
		});
	}

	
}
