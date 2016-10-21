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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;

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
import hydrograph.ui.logging.factory.LogFactory;


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
	public void updateEditorWithCompStatus(ExecutionStatus executionStatus, ELTGraphicalEditor editor,boolean isReplay) {
		if (executionStatus != null) {
			
			
			
			/**
			 * Push the tracking log in tracking console
			 */
			if(!isReplay){
					pushExecutionStatusToExecutionTrackingConsole(executionStatus);
					ExecutionTrackingFileLogger.INSTANCE.log(executionStatus.getJobId(), executionStatus, JobManager.INSTANCE.isLocalMode());
				}	

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
							if((component.getProperties().get(Constants.TYPE).equals(Constants.INPUT)||component.getProperties().get(Constants.TYPE).equals(Constants.OPERATION))){
								Map<String, SubjobDetails> componentNameAndLink = new HashMap();
								StringBuilder subjobPrefix = new StringBuilder("");
								populateSubjobRecordCount(componentNameAndLink, component,subjobPrefix,true);
								applyRecordCountOnSubjobComponent(component, componentNameAndLink, executionStatus);
							} 
							int successCount = 0;
							updateStatusCountForSubjobComponent(executionStatus, component, successCount);

						}else
						{
							updateStatusCountForComponent(executionStatus,component);
						}
					}
				}
				ExecutionTrackingConsoleUtils.INSTANCE.readFile(executionStatus, null, JobManager.INSTANCE.isLocalMode());
			}
	}
	
	private void updateStatusCountForComponent(
			ExecutionStatus executionStatus, Component component) {

		for( ComponentStatus componentStatus: executionStatus.getComponentStatus()){
			if(componentStatus.getComponentId().substring(componentStatus.getComponentId().lastIndexOf(".")+1).equals(component.getComponentId())){
				logger.info("Updating normal component {} status {}",component.getComponentId(), componentStatus.getCurrentStatus());
				component.updateStatus(componentStatus.getCurrentStatus());
				for(Link link: component.getSourceConnections()){
					if(componentStatus.getComponentId().substring(componentStatus.getComponentId().lastIndexOf(".")+1).equals(link.getSource().getComponentId())){
						link.updateRecordCount(componentStatus.getProcessedRecordCount().get(link.getSourceTerminal()).toString());
					}
				}
			}
		}
	}

	private void updateStatusCountForSubjobComponent(ExecutionStatus executionStatus,Component component, int successCount) {
		boolean running = false;
		boolean pending = false;

		for( ComponentStatus componentStatus: executionStatus.getComponentStatus()){

			if(!pending){
					boolean isPending =applyPendingStatus(component, componentStatus, pending);
				if(isPending){
					component.updateStatus(ComponentExecutionStatus.PENDING.value());
				}
			}
			if(!running){
				boolean isRunning =applyRunningStatus(component, componentStatus, running);
				if(isRunning){
					component.updateStatus(ComponentExecutionStatus.RUNNING.value());
				}
			} 

			boolean isFail =applyFailStatus(component, componentStatus);
				if(isFail){
					component.updateStatus(ComponentExecutionStatus.FAILED.value());
					break;
				}
		}
		if(component.getStatus().value().equalsIgnoreCase(ComponentExecutionStatus.PENDING.value()) || component.getStatus().value().equalsIgnoreCase(ComponentExecutionStatus.RUNNING.value()) ){
			boolean isSuccess=applySuccessStatus(component, executionStatus, successCount);
	 		if(isSuccess)
	 			component.updateStatus(ComponentExecutionStatus.SUCCESSFUL.value());
		}
	}
	
	private void applyRecordCountOnSubjobComponent( Component component,Map<String, SubjobDetails> componentNameAndLink, ExecutionStatus executionStatus){
		if (!componentNameAndLink.isEmpty()) {
			for (Map.Entry<String, SubjobDetails> entry : componentNameAndLink.entrySet()) {
				for (ComponentStatus componentStatus : executionStatus.getComponentStatus()) {
					if (componentStatus.getComponentId().contains(entry.getKey())) {
						List<String> portList = new ArrayList(componentStatus.getProcessedRecordCount().keySet());
						for (String port : portList) {
							if ((((SubjobDetails) entry.getValue()).getSourceTerminal()).equals(port)) {
								for (Link link : component.getSourceConnections()) {
									if (link.getSourceTerminal().toString()
											.equals(((SubjobDetails) entry.getValue()).getTargetTerminal())) {
										link.updateRecordCount(componentStatus.getProcessedRecordCount()
												.get(((SubjobDetails) entry.getValue()).getSourceTerminal())
												.toString());
										break;
									}
								}
							}
						}
					} else {
						continue;
					}
				}
			}
			System.out.println("componentNameAndLink :"+componentNameAndLink);
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
	private void populateSubjobRecordCount(Map<String, SubjobDetails> componentNameAndLink, Component component,StringBuilder subjobPrefix,boolean isParent) {
		Component outputSubjobComponent=(Component) component.getProperties().get(Messages.OUTPUT_SUBJOB_COMPONENT);
		if(outputSubjobComponent!=null){
			for(Link link:outputSubjobComponent.getTargetConnections()){
				Component componentPrevToOutput = link.getSource();
				if(Constants.SUBJOB_COMPONENT.equals(componentPrevToOutput.getComponentName())){
					subjobPrefix.append(component.getComponentId()+".");
					populateSubjobRecordCount(componentNameAndLink, componentPrevToOutput,subjobPrefix,false);
				}else{
					String portNumber = link.getTargetTerminal().replace(Messages.IN_PORT_TYPE, Messages.OUT_PORT_TYPE);

					SubjobDetails subjobDetails = new SubjobDetails(link.getSourceTerminal(), portNumber);
					if(CLONE_COMPONENT_TYPE.equalsIgnoreCase(componentPrevToOutput.getComponentName())){
						componentNameAndLink.put(subjobPrefix+component.getComponentId()+"."+componentPrevToOutput.getComponentId()+"."+portNumber, subjobDetails);
					}else{
						if(isParent)
							componentNameAndLink.put(component.getComponentId()+"."+componentPrevToOutput.getComponentId(), subjobDetails);
						else
							componentNameAndLink.put(subjobPrefix+component.getComponentId()+"."+componentPrevToOutput.getComponentId(), subjobDetails);
					}
				}
			}
		}
	}
	
	private boolean applyPendingStatus(Component component, ComponentStatus componentStatus, boolean pendingStatusApplied) {
		Container container=(Container)component.getProperties().get("Container");
		for (Component innerSubComponent : container.getChildren()) {
			if(Constants.SUBJOB_COMPONENT.equals(innerSubComponent.getComponentName())){
				applyPendingStatus(innerSubComponent, componentStatus, pendingStatusApplied);
			}else{
				String compName = component.getComponentId()+"."+innerSubComponent.getComponentId();
				if(componentStatus.getComponentId().contains(compName) && componentStatus.getCurrentStatus().equals(ComponentExecutionStatus.PENDING.value())){
					pendingStatusApplied = true;
					return pendingStatusApplied;
				}
			}
			}
		return pendingStatusApplied;
	}

	private boolean applyRunningStatus(Component component, ComponentStatus componentStatus, boolean runningStatusApplied) {
		Container container = (Container) component.getProperties().get(Constants.SUBJOB_CONTAINER);
		for (Component innerSubComponent : container.getChildren()) {
			if (Constants.SUBJOB_COMPONENT.equals(innerSubComponent.getComponentName())) {
				applyRunningStatus(innerSubComponent, componentStatus, runningStatusApplied);
			} else {
				String compName = component.getComponentId() + "."+ innerSubComponent.getComponentId();
				if (componentStatus.getComponentId().contains(compName)&& componentStatus.getCurrentStatus().equals(ComponentExecutionStatus.RUNNING.value())) {
					runningStatusApplied = true;
					return runningStatusApplied;
				}
			}
		}
		return runningStatusApplied;
	}
	private boolean applyFailStatus(Component component, ComponentStatus componentStatus) {
		Container container = (Container) component.getProperties().get(Constants.SUBJOB_CONTAINER);
		for (Component innerSubComponent : container.getChildren()) {
			if (Constants.SUBJOB_COMPONENT.equals(innerSubComponent.getComponentName())) {
				applyFailStatus(innerSubComponent, componentStatus);
			} else {
				String compName = component.getComponentId() + "."+ innerSubComponent.getComponentId();
				if (componentStatus.getComponentId().contains(compName)&& componentStatus.getCurrentStatus().equals(ComponentExecutionStatus.FAILED.value())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean applySuccessStatus(Component component, ExecutionStatus executionStatus,int successcount) {
		Container container = (Container) component.getProperties().get(Constants.SUBJOB_CONTAINER);
		for (ComponentStatus componentStatus : executionStatus.getComponentStatus()) {
			for (Component innerSubComponent : container.getChildren()) {
				if (Constants.SUBJOB_COMPONENT.equals(innerSubComponent.getComponentName())) {
					applySuccessStatus(innerSubComponent, executionStatus,successcount);
				} else {
					String compName = component.getComponentId() + "."+ innerSubComponent.getComponentId();
					if (componentStatus.getComponentId().contains(compName)){
						if(!componentStatus.getCurrentStatus().equals(ComponentExecutionStatus.SUCCESSFUL.value())){
							return false;
						}
					}
				}
			}
		}
		
		return true;
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
				ExecutionStatus[] status = ExecutionTrackingConsoleUtils.INSTANCE.readFile(executionStatus, null, JobManager.INSTANCE.isLocalMode());
				for(int i=0;i<status.length;i++){		
					console.setStatus(ExecutionTrackingConsoleUtils.INSTANCE.getExecutionStatusInString(status[i]));		
				}	
			}
		});
	}

	
}