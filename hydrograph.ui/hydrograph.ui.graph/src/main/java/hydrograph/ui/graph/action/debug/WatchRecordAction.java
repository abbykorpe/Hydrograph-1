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
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.communication.debugservice.DebugServiceClient;
import hydrograph.ui.communication.utilities.SCPUtility;
import hydrograph.ui.dataviewer.constants.MessageBoxText;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.LinkEditPart;
import hydrograph.ui.graph.controller.PortEditPart;
import hydrograph.ui.graph.debugconverter.DebugHelper;
import hydrograph.ui.graph.debugconverter.SchemaHelper;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.handler.DebugHandler;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.utility.MessageBox;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
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

	private Map<String, DebugDataViewer> dataViewerMap;

	private static final String DEBUG_DATA_FILE_EXTENTION=".csv";
	
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
				
		// Get csv debug file name and location 
		final JobDetails jobDetails = getJobDetails(job);
		String csvDebugFileAbsolutePath = null;
		String csvDebugFileName = null;
		try {
			csvDebugFileAbsolutePath = DebugServiceClient.INSTANCE.getDebugFile(jobDetails, Utils.INSTANCE.getFileSize()).trim();
			csvDebugFileName = csvDebugFileAbsolutePath.substring(csvDebugFileAbsolutePath.lastIndexOf("/") + 1,
					csvDebugFileAbsolutePath.length()).replace(DEBUG_DATA_FILE_EXTENTION, "").trim();
		} catch (Exception e1) {
			MessageBox.INSTANCE.showMessage(MessageBox.ERROR, Messages.UNABLE_TO_FETCH_DEBUG_FILE);
			logger.error("Unable to fetch debug file", e1);
			return;
		}
				
		//Copy csv debug file to Data viewers temporary file location
		String dataViewerDebugFile = getDataViewerDebugFile(csvDebugFileName);		
		try {
			copyCSVDebugFileToDataViewerStagingArea(jobDetails, csvDebugFileAbsolutePath, dataViewerDebugFile);
		} catch (Exception e3) {
			MessageBox.INSTANCE.showMessage(MessageBox.ERROR, Messages.UNABLE_TO_FETCH_DEBUG_FILE);
			logger.error("Unable to fetch debug file", e3);
			return;
		}
		
		//Delete csv debug file after copy
		final String dataViewerFilePath= getDataViewerDebugFilePath().trim();
		final String dataViewerFile= csvDebugFileName.trim();
		final String dataViewerWindowTitle = dataViewerWindowName;		
		try {
			DebugServiceClient.INSTANCE.deleteDebugFile(jobDetails);
		} catch (Exception e2) {
			logger.warn("Unable to delete debug file",e2);
		}
		
		//Check for empty csv debug file
		if(isEmptyDebugCSVFile(dataViewerFilePath, dataViewerFile)){
			MessageBox.INSTANCE.showMessage(MessageBox.ERROR,Messages.EMPTY_DEBUG_FILE);
			logger.error("Empty debug file");
		    return;
		}
		
		//Export Debug file schema
		String tempSchemaFilePath = dataViewerFilePath+dataViewerFile;
		SchemaHelper.INSTANCE.exportSchemaGridData(getSelectedObjects(), tempSchemaFilePath);
				
		//Open data viewer window
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					DebugDataViewer window = new DebugDataViewer(dataViewerFilePath, dataViewerFile, dataViewerWindowTitle,
							jobDetails);
					window.setBlockOnOpen(true);
					dataViewerMap.put(dataViewerWindowTitle, window);
					window.open();
				} catch (Exception e) {
					logger.debug(Messages.UNABLE_TO_OPEN_DATA_VIEWER, e);
					MessageBox.INSTANCE.showMessage(MessageBox.ERROR, Messages.UNABLE_TO_OPEN_DATA_VIEWER);
				}
				dataViewerMap.remove(dataViewerWindowTitle);
			}
		});
	}

	private boolean isEmptyDebugCSVFile(String dataViewerFilePath, final String dataViewerFileh) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(dataViewerFilePath + dataViewerFileh + DEBUG_DATA_FILE_EXTENTION));
			if (bufferedReader.readLine() == null) {
			    return true;
			}
		} catch (Exception e1) {
			logger.error(Messages.UNABLE_TO_READ_DEBUG_FILE,e1);
			return true;
		}finally{
			try {
				if(bufferedReader != null){
					bufferedReader.close();
				}					
			} catch (IOException e) {
				logger.error(Messages.UNABLE_TO_READ_DEBUG_FILE,e);
			}
		}
		return false;
	}

	private void copyCSVDebugFileToDataViewerStagingArea(JobDetails jobDetails, String csvDebugFileAbsolutePath, String dataViewerDebugFile)
			throws Exception {
		if (!jobDetails.isRemote()) {
			String sourceFile = csvDebugFileAbsolutePath.trim();
			File file = new File(dataViewerDebugFile);
			if (!file.exists()) {
				Files.copy(Paths.get(sourceFile), Paths.get(dataViewerDebugFile), StandardCopyOption.REPLACE_EXISTING);
			}
		} else {
			File file = new File(dataViewerDebugFile);
			if (!file.exists()) {				
				SCPUtility.INSTANCE.scpFileFromRemoteServer(jobDetails.getHost(), jobDetails.getUsername(), jobDetails.getPassword(),
						csvDebugFileAbsolutePath.trim(), getDataViewerDebugFilePath());
			}
		}
	}

	private String getDataViewerDebugFile(String csvDebugFileName) {
		String dataViewerDebugFile = getDataViewerDebugFilePath();
		if (OSValidator.isWindows()) {
			dataViewerDebugFile = (dataViewerDebugFile + "\\" + csvDebugFileName.trim() + DEBUG_DATA_FILE_EXTENTION).trim();
		} else {
			dataViewerDebugFile = (dataViewerDebugFile + "/" + csvDebugFileName.trim() + DEBUG_DATA_FILE_EXTENTION).trim();
		}
		return dataViewerDebugFile;
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

	private String getDataViewerDebugFilePath() {
		String dataViewerDebugFilePath = Utils.INSTANCE.getDataViewerDebugFilePath();
		dataViewerDebugFilePath=dataViewerDebugFilePath.trim();
		
		
		if(OSValidator.isWindows()){
			if(dataViewerDebugFilePath.startsWith("/")){
				dataViewerDebugFilePath = dataViewerDebugFilePath.replaceFirst("/", "").replace("/", "\\");
			}			 
		}
		return dataViewerDebugFilePath;
	}	
}

class WatchRecordInner {
	private String componentId;
	private String socketId;
	private String currentJob;
	private String uniqueJobId;

	public WatchRecordInner() {
		// TODO Auto-generated constructor stub
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
