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

package hydrograph.ui.graph.action.debug;

import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.dataviewer.constants.MessageBoxText;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.LinkEditPart;
import hydrograph.ui.graph.controller.PortEditPart;
import hydrograph.ui.graph.debugconverter.DebugHelper;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.handler.DebugHandler;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.utility.MessageBox;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

/**
 * The Class WatchRecordAction used to view data at watch points after job execution
 * 
 * @author Bitwise
 * 
 */
public class WatchRecordAction extends SelectionAction {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(WatchRecordAction.class);
	private boolean isWatcher;
	private WatchRecordInner watchRecordInner = new WatchRecordInner();

	private static Map<String, DebugDataViewer> dataViewerMap;

	public WatchRecordAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}
	
	@Override
	protected void init() {
		super.init();
		setText(Messages.WATCH_RECORD_TEXT);
		setId(Constants.WATCH_RECORD_ID);
		setEnabled(true);
		dataViewerMap = new LinkedHashMap<>();
		JobManager.INSTANCE.setDataViewerMap(dataViewerMap);
	}

	private void createWatchCommand() throws CoreException {
		List<Object> selectedObjects = getSelectedObjects();
		for (Object obj : selectedObjects) {
			if (obj instanceof LinkEditPart) {
				Link link = (Link) ((LinkEditPart) obj).getModel();
				String componentId = link.getSource().getComponentLabel().getLabelContents();
				Component component = link.getSource();
				if (StringUtils.equalsIgnoreCase(component.getComponentName(), Constants.SUBJOB_COMPONENT)) {
					String str = DebugHelper.INSTANCE.getSubgraphComponent(component);
					String[] str1 = StringUtils.split(str, ".");
					String componentID = str1[0];
					String socketId = str1[1];
					watchRecordInner
							.setComponentId(link.getSource().getComponentLabel().getLabelContents() + "." + componentID);
					watchRecordInner.setSocketId(socketId);
				} else {
					watchRecordInner.setComponentId(componentId);
					String socketId = link.getSourceTerminal();
					watchRecordInner.setSocketId(socketId);
				}

				isWatcher = checkWatcher(link.getSource(), link.getSourceTerminal());
			}
		}
	}

	private boolean checkWatcher(Component selectedComponent, String portName) {
		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		IPath path = new Path(editor.getTitleToolTip());
		String currentJob = path.lastSegment().replace(Constants.JOB_EXTENSION, "");
		watchRecordInner.setCurrentJob(currentJob);
		GraphicalViewer graphicalViewer = (GraphicalViewer) ((GraphicalEditor) editor).getAdapter(GraphicalViewer.class);
		String uniqueJobId = editor.getJobId();
		watchRecordInner.setUniqueJobId(uniqueJobId);

		for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); iterator.hasNext();) {
			EditPart editPart = (EditPart) iterator.next();
			if (editPart instanceof ComponentEditPart) {
				Component comp = ((ComponentEditPart) editPart).getCastedModel();
				if (comp.equals(selectedComponent)) {
					List<PortEditPart> portEditParts = editPart.getChildren();
					for (AbstractGraphicalEditPart part : portEditParts) {
						if (part instanceof PortEditPart) {
							String port_Name = ((PortEditPart) part).getCastedModel().getTerminal();
							if (port_Name.equals(portName)) {
								return ((PortEditPart) part).getPortFigure().isWatched();
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	protected boolean calculateEnabled() {
		int count = 0;
		List<Object> selectedObject = getSelectedObjects();

		try {
			createWatchCommand();
		} catch (CoreException exception) {
			logger.error(exception.getMessage(), exception);
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

	@Override
	public void run() {			
		//Create watch command
		try {
			createWatchCommand();
		} catch (CoreException e) {
			logger.error("Unable to create watch command",e);
			MessageBox.INSTANCE.showMessage(MessageBox.ERROR, Messages.UNABLE_TO_CREATE_WATCH_RECORD);
			return;
		}
		
		// Check if job is executed in debug mode
		Job job = DebugHandler.getJob(watchRecordInner.getCurrentJob());		
		if(job==null){
			MessageBox.INSTANCE.showMessage(MessageBoxText.INFO, Messages.FORGOT_TO_EXECUTE_DEBUG_JOB);
			return;
		}
		
		
		//Create data viewer window name, if window exist reopen same window
		String dataViewerWindowName = job.getLocalJobID().replace(".", "_") + "_" + watchRecordInner.getComponentId() + "_"
				+ watchRecordInner.getSocketId();
		if (dataViewerMap.keySet().contains(dataViewerWindowName)) {
			dataViewerMap.get(dataViewerWindowName).getShell().setActive();
			return;
		}
		
		// Check if watcher exist
		if (!isWatcher) {
			MessageBox.INSTANCE.showMessage(MessageBox.INFO, Messages.MESSAGES_BEFORE_CLOSE_WINDOW);
			return;
		}
				
		final JobDetails jobDetails = getJobDetails(job);

		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		editor.getContainer().getJobDetailsInGraph().add(jobDetails);

		
		final String dataViewerWindowTitle = dataViewerWindowName;	

		//Open data viewer window
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				DebugDataViewer window = new DebugDataViewer(jobDetails,dataViewerWindowTitle);
				dataViewerMap.put(dataViewerWindowTitle, window);
				window.setBlockOnOpen(true);
				window.setDataViewerMap(dataViewerMap);
				window.open();
			}
		});
	}

	private JobDetails getJobDetails(Job job) {
		final JobDetails jobDetails = new JobDetails(
				job.getHost(), 
				job.getPortNumber(), 
				job.getUserId(), 
				job.getPassword(), 
				job.getBasePath(),
				watchRecordInner.getUniqueJobId(), 
				watchRecordInner.getComponentId(), 
				watchRecordInner.getSocketId(),
				job.isRemoteMode());
		return jobDetails;
	}
}

class WatchRecordInner {
	private String componentId;
	private String socketId;
	private String currentJob;
	private String uniqueJobId;

	public WatchRecordInner() {		
	}

	public String getUniqueJobId() {
		return uniqueJobId;
	}

	public void setUniqueJobId(String uniqueJobId) {
		this.uniqueJobId = uniqueJobId;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getSocketId() {
		return socketId;
	}

	public void setSocketId(String socketId) {
		this.socketId = socketId;
	}

	public String getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(String currentJob) {
		this.currentJob = currentJob;
	}

}
