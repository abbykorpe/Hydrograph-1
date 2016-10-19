package hydrograph.ui.graph.execution.tracking.replay;

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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.execution.tracking.datastructure.ComponentStatus;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.execution.tracking.datastructure.SubjobDetails;
import hydrograph.ui.graph.execution.tracking.utils.ExecutionTrackingConsoleUtils;
import hydrograph.ui.graph.execution.tracking.windows.ExecutionTrackingConsole;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.ComponentExecutionStatus;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.logging.factory.LogFactory;

public class ReplayExecutionTrackingUtility {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(ReplayExecutionTrackingUtility.class);
	
	private String CLONE_COMPONENT_TYPE ="CloneComponent";
	private Map<String, ExecutionStatus> trackingMap;
	private Map<String, List<Job>> trackingJobMap;
	
	
	public static ReplayExecutionTrackingUtility INSTANCE = new ReplayExecutionTrackingUtility();
	
	private ReplayExecutionTrackingUtility() {
		trackingMap = new HashMap<String, ExecutionStatus>();
		trackingJobMap = new HashMap<String, List<Job>>();
	}
	
	public void addTrackingStatus(String uniqueRunJobId, ExecutionStatus executionStatus){
		if(uniqueRunJobId != null){
			trackingMap.put(uniqueRunJobId, executionStatus);
		}
	}
	
	public void addTrackingJobs(String jobName, Job jobDetails){
		if(trackingJobMap.get(jobName)==null){
			List<Job> jobs = new ArrayList<>();
			jobs.add(jobDetails);
			trackingJobMap.put(jobName, jobs);
		}else{
			trackingJobMap.get(jobName).add(jobDetails);
		}
	}
	
	public Map<String, List<Job>> getTrackingJobs(){
		return trackingJobMap;
		
	}
	
	public Map<String, ExecutionStatus> getTrackingStatus(){
		return trackingMap;
	}
	
	public void replayExecutionTracking(ExecutionStatus executionStatus){
		IWorkbenchPage page = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage();
		IEditorReference[] refs = page.getEditorReferences();

		ReplayExecutionTrackingUtility.INSTANCE.addTrackingStatus(executionStatus.getJobId(), executionStatus);
		for (IEditorReference ref : refs){
			IEditorPart editor = ref.getEditor(false);
			if(editor instanceof ELTGraphicalEditor){
				updateEditorWithCompStatus(executionStatus, (ELTGraphicalEditor) editor, null);
			}
		}
	}
	
	
	/**
	 * Update component status and processed record 
	 * @param executionStatus
	 * @param editor
	 */
	public void updateEditorWithCompStatus(
			ExecutionStatus executionStatus, ELTGraphicalEditor editor, String jsonObject) {
		int count = 0;
		Map<String, SubjobDetails> componentNameAndLink = new HashMap();
		List<String> compList = new ArrayList<>();
		if(executionStatus!=null){
			pushExecutionStatusToExecutionTrackingConsole(executionStatus);
			/*String jobId = executionStatus.getJobId();
			ExecutionTrackingConsole console = JobManager.INSTANCE.getExecutionTrackingConsoles().get(jobId);
			if(console!=null){
				//updateExecutionTrackingConsole(executionStatus, console);	
				updateReplayTrackingConsole(executionStatus, console);
			}*/

			//ExecutionTrackingFileLogger.INSTANCE.log(executionStatus.getJobId().trim(), executionStatus, JobManager.INSTANCE.isLocalMode());

			if(editor!=null && editor instanceof ELTGraphicalEditor){
				GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
				for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry().values().iterator(); 
						ite.hasNext();){
					EditPart editPart = (EditPart) ite.next();
					if(editPart instanceof ComponentEditPart){
						Component component = ((ComponentEditPart)editPart).getCastedModel();
						compList.add(component.getComponentLabel().getLabelContents());
						System.out.println("Comp Name:"+component.getComponentLabel().getLabelContents());
					}
					if(editPart instanceof ComponentEditPart){
						Component component = ((ComponentEditPart)editPart).getCastedModel();
						
						if(Constants.SUBJOB_COMPONENT.equals(component.getComponentName())){
							String keySubjobName = component.getComponentLabel().getLabelContents();
							StringBuilder subjobPrefix = new StringBuilder("");
							componentNameAndLink.clear();
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
		ExecutionTrackingConsoleUtils.INSTANCE.readFile(executionStatus, null, JobManager.INSTANCE.isLocalMode());
	}
	
	private void updateStatusCountForNormalComponent(
			ExecutionStatus executionStatus, Component component) {
		for( ComponentStatus componentStatus: executionStatus.getComponentStatus()){
			if(componentStatus.getComponentId().trim().equals(component.getComponentLabel().getLabelContents())){
				logger.info("Updating normal component {} status {}",component.getComponentLabel().getLabelContents(), componentStatus.getCurrentStatus());
				component.updateStatus(componentStatus.getCurrentStatus().trim());
				for(Link link: component.getSourceConnections()){
					if(componentStatus.getComponentId().trim().equals(link.getSource().getComponentLabel().getLabelContents())){
						link.updateRecordCount(componentStatus.getProcessedRecordCount().get(link.getSourceTerminal()).toString());
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
				//console.setStatus(ExecutionTrackingConsoleUtils.INSTANCE.readFile(executionStatus, null, JobManager.INSTANCE.isLocalMode()));
			}
		});
	}
	
	private void updateReplayTrackingConsole(final ExecutionStatus executionStatus, final ExecutionTrackingConsole console){
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				console.clearConsole();
				console.setStatus(executionStatus.toString());
			}
		});
	}
	
	private void populateSubjobRecordCount(
			Map<String, SubjobDetails> componentNameAndLink, Component component,StringBuilder subjobPrefix) {
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
						List<String> portList = new ArrayList(componentStatus.getProcessedRecordCount().keySet());
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
							if(componentStatus.getCurrentStatus().equalsIgnoreCase(ComponentExecutionStatus.SUCCESSFUL.value())){
								successCount++;
							}
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
}
