/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package hydrograph.ui.graph.action.debug;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.dataviewer.constants.MessageBoxText;
import hydrograph.ui.dataviewer.filter.FilterConditions;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.controller.LinkEditPart;
import hydrograph.ui.graph.execution.tracking.datastructure.SubjobDetails;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.utility.MessageBox;
import hydrograph.ui.graph.utility.ViewDataUtils;
import hydrograph.ui.logging.factory.LogFactory;

/**
 * The Class WatchRecordAction used to view data at watch points after job execution
 * 
 * @author Bitwise
 * 
 */
public class ViewDataCurrentJobAction extends SelectionAction{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ViewDataCurrentJobAction.class);
	private WatchRecordInner watchRecordInner = new WatchRecordInner();
	private static Map<String, DebugDataViewer> dataViewerMap;
	private Map<String, FilterConditions> watcherAndConditon =new LinkedHashMap<String, FilterConditions>();
	
	public ViewDataCurrentJobAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();
		setText(Messages.VIEW_DATA_CURRENT_RUN_TEXT);
		setId(Constants.CURRENT_VIEW_DATA_ID);
		setEnabled(true);
		
		dataViewerMap = new HashMap<>();
		JobManager.INSTANCE.setDataViewerMap(dataViewerMap);
	}
	
	
	@Override
	protected boolean calculateEnabled(){
		Map<String, SubjobDetails> componentNameAndLink = new HashMap();
		int count = 0;
		List<String> list = null;
		boolean isWatcher = false;
		List<Object> selectedObject = getSelectedObjects();
		for (Object obj : selectedObject) {
			if (obj instanceof LinkEditPart) {
				Link link = (Link) ((LinkEditPart) obj).getModel();
				String componentId = link.getSource().getComponentId();
				Component component = link.getSource();
				if (StringUtils.equalsIgnoreCase(component.getComponentName(), Constants.SUBJOB_COMPONENT)) {
					String componenet_Id = "";
					String socket_Id = "";
					ViewDataUtils.getInstance().subjobParams(componentNameAndLink, component, new StringBuilder(), link.getSourceTerminal());
					for(Entry<String, SubjobDetails> entry : componentNameAndLink.entrySet()){
						String comp_soc = entry.getKey();
						String[] split = StringUtils.split(comp_soc, "/.");
						componenet_Id = split[0];
						for(int i = 1;i<split.length-1;i++){
							componenet_Id = componenet_Id + "." + split[i];
						}
						socket_Id = split[split.length-1];
					}

					watchRecordInner.setComponentId(componenet_Id);
					watchRecordInner.setSocketId(socket_Id);
				} else {
					watchRecordInner.setComponentId(componentId);
					String socketId = link.getSourceTerminal();
					watchRecordInner.setSocketId(socketId);
				}
				ViewDataUtils dataUtils = ViewDataUtils.getInstance();
				isWatcher = dataUtils.checkWatcher(link.getSource(), link.getSourceTerminal());
			}
		}
		
		if (!selectedObject.isEmpty() && isWatcher) {
			for (Object obj : selectedObject) {
				if (obj instanceof LinkEditPart) {
					count++;
				}
			}
		}

		if (count == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the component canvas.
	 *
	 * @return the component canvas
	 */
	private DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}

	@Override
	public void run() {
		// Check if job is executed in debug mode
				Job job = JobManager.INSTANCE.getPreviouslyExecutedJobs().get(getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName());
				if(job==null){
					MessageBox.INSTANCE.showMessage(MessageBoxText.INFO, Messages.RUN_THE_JOB_IN_DEBUG_MODE);
					return;
				}
				
				//Create data viewer window name, if window exist reopen same window
				String dataViewerWindowName = job.getLocalJobID().replace(".", "_") + "_" + watchRecordInner.getComponentId() + "_"
						+ watchRecordInner.getSocketId();
				if (dataViewerMap.keySet().contains(dataViewerWindowName)) {
					Point originalWindowSize=dataViewerMap.get(dataViewerWindowName).getShell().getSize();
					dataViewerMap.get(dataViewerWindowName).getShell().setActive();
					dataViewerMap.get(dataViewerWindowName).getShell().setMaximized(true);
					dataViewerMap.get(dataViewerWindowName).getShell().setSize(new Point(originalWindowSize.x, originalWindowSize.y));
					return;
				}
				
				final JobDetails jobDetails = getJobDetails(job);

				final String dataViewerWindowTitle = dataViewerWindowName;	
				
				DebugDataViewer window = new DebugDataViewer(jobDetails,dataViewerWindowTitle);
				String watcherId=watchRecordInner.getComponentId()+watchRecordInner.getComponentId();
				dataViewerMap.put(dataViewerWindowTitle, window);
				window.setBlockOnOpen(true);
				window.setDataViewerMap(dataViewerMap);
				if(watcherAndConditon.containsKey(watcherId))
				{
					window.setConditions(watcherAndConditon.get(watcherId));
					if(watcherAndConditon.get(watcherId).isOverWritten()){
						window.setOverWritten(watcherAndConditon.get(watcherId).isOverWritten());
					}
				}
				
				window.open();
				if(window.getConditions()!=null){
					if(!window.getConditions().getRetainLocal()){
						window.getConditions().setLocalCondition("");
						window.getConditions().getLocalConditions().clear();
						window.getConditions().getLocalGroupSelectionMap().clear();
					}
					if(!window.getConditions().getRetainRemote()){
						window.getConditions().setRemoteCondition("");
						window.getConditions().getRemoteConditions().clear();
						window.getConditions().getRemoteGroupSelectionMap().clear();
					}
					watcherAndConditon.put(watcherId,window.getConditions());
				}
			}

			
		private JobDetails getJobDetails(Job job) {
			final JobDetails jobDetails = new JobDetails(
					job.getHost(), 
					job.getPortNumber(), 
					job.getUserId(), 
					job.getPassword(), 
					job.getBasePath(),
					job.getUniqueJobId(), 
					watchRecordInner.getComponentId(), 
					watchRecordInner.getSocketId(),
					job.isRemoteMode(), job.getJobStatus());
			return jobDetails;
		}
	}
