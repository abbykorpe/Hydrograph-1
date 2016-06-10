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

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.ReloadInformation;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.LinkEditPart;
import hydrograph.ui.graph.controller.PortEditPart;
import hydrograph.ui.graph.debug.service.DebugRestClient;
import hydrograph.ui.graph.debugconverter.DebugHelper;
import hydrograph.ui.graph.debugconverter.SchemaHelper;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.handler.DebugHandler;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
 
/**
 * The Class WatchRecordAction used to view data at watch points after job execution
 * @author Bitwise
 *
 */
public class WatchRecordAction extends SelectionAction {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(WatchRecordAction.class);
	private boolean isWatcher;
	private WatchRecordInner watchRecordInner = new WatchRecordInner();
	
	private HashMap<String,DebugDataViewer> dataViewerMap;
	
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
	
	private boolean createWatchCommand(List<Object> selectedObjects) throws CoreException  {
		for(Object obj:selectedObjects){
			if(obj instanceof LinkEditPart)	{
				Link link = (Link)((LinkEditPart)obj).getModel();
				String componentId = link.getSource().getComponentLabel().getLabelContents();
				Component component = link.getSource();
				if(StringUtils.equalsIgnoreCase(component.getComponentName(), Constants.SUBJOB_COMPONENT)){
					String str = DebugHelper.INSTANCE.getSubgraphComponent(component);
					String[] str1 = StringUtils.split(str,".");
					String componentID = str1[0];
					String socketId = str1[1];
					watchRecordInner.setComponentId(link.getSource().getComponentLabel().getLabelContents()+"."+componentID);
					watchRecordInner.setSocketId(socketId);
				}else{
					watchRecordInner.setComponentId(componentId);
					String socketId = link.getSourceTerminal();
					watchRecordInner.setSocketId(socketId);
				}
				
				isWatcher = checkWatcher(link.getSource(), link.getSourceTerminal());
				
				return true;
			}	
		}
		return false;
	}
	
	private boolean checkWatcher(Component selectedComponent, String portName) {
		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IPath path = new Path(editor.getTitleToolTip());
		String currentJob = path.lastSegment().replace(Constants.JOB_EXTENSION, "");
		watchRecordInner.setCurrentJob(currentJob);
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		String uniqueJobId = editor.getJobId();
		watchRecordInner.setUniqueJobId(uniqueJobId);
		 
		for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); iterator.hasNext();) {
			EditPart editPart = (EditPart) iterator.next();
			if(editPart instanceof ComponentEditPart) {
				Component comp = ((ComponentEditPart)editPart).getCastedModel();
				if(comp.equals(selectedComponent)){
					List<PortEditPart> portEditParts = editPart.getChildren();
					for(AbstractGraphicalEditPart part:portEditParts) {	
						if(part instanceof PortEditPart){
							String port_Name = ((PortEditPart)part).getCastedModel().getTerminal();
							if(port_Name.equals(portName)){
								return ((PortEditPart)part).getPortFigure().isWatched();
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	
	private String getLocalADVData() {
		String basePath = null,ipAddress = null,userID = null,password = null,port_no = null;
		Job job = DebugHandler.getJob(watchRecordInner.getCurrentJob());
		if(job!=null){
			basePath = job.getBasePath();
			ipAddress = "localhost";
			userID = job.getUserId();
			password = job.getPassword();
			port_no = DebugHelper.INSTANCE.restServicePort();
		}
		
		
		DebugRestClient debugRestClient = new DebugRestClient();
		try {
			
			 String remoteFilePath = debugRestClient.calltoReadService(ipAddress, port_no,
			 basePath, watchRecordInner.getUniqueJobId(), watchRecordInner.getComponentId(),
			 watchRecordInner.getSocketId(), "userID", "password", Utils.getFileSize());
			 return remoteFilePath;
		} catch (IOException exception) {
			logger.error("Connection failed", exception);
			messageDialog(Messages.REMOTE_MODE_TEXT);
			return null;
		} catch (Exception exception) {
			logger.error("Exception while calling rest service: ", exception);
			messageDialog(Messages.REMOTE_MODE_TEXT);
			return null;
		}
	}
		
	private String getRemoteADVData() {
		String basePath = null,ipAddress = null,userID = null,password = null,port_no = null;
		Job job = DebugHandler.getJob(watchRecordInner.getCurrentJob());
		if(job!=null){
			basePath = job.getBasePath();
			ipAddress = job.getIpAddress();
			userID = job.getUserId();
			password = job.getPassword();
			port_no = job.getPortNumber();
		}
		
		logger.info("Job Id: {}, Component Id: {}, Socket ID: {}, User ID:{}",
				new Object[] { basePath, watchRecordInner.getUniqueJobId(), watchRecordInner.getComponentId(), watchRecordInner.getSocketId() });
		DebugRestClient debugRestClient = new DebugRestClient();
		try {
			
			 String remoteFilePath = debugRestClient.calltoReadService(ipAddress, port_no,
			 basePath, watchRecordInner.getUniqueJobId(), watchRecordInner.getComponentId(),
			 watchRecordInner.getSocketId(), userID, password, Utils.getFileSize());
			 return remoteFilePath;
		} catch (IOException exception) {
			logger.error("Connection failed", exception);
			messageDialog(Messages.REMOTE_MODE_TEXT);
			return null;
		} catch (Exception exception) {
			logger.error("Exception while calling rest service: ", exception);
			messageDialog(Messages.REMOTE_MODE_TEXT);
			return null;
		}
	}

	private boolean isLocalDebugMode(){
		logger.debug("getRunningMode: "+ JobManager.INSTANCE.isLocalMode());
		return JobManager.INSTANCE.isLocalMode();
	}
	
	private void messageDialog(String message){
		MessageBox messageBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.APPLICATION_MODAL | SWT.OK);
		messageBox.setText(Messages.DEBUG_ALERT_MESSAGE); 
		messageBox.setMessage(message);
		messageBox.open();
	}
	
	@Override
	protected boolean calculateEnabled() {
		int count =0;
		List<Object> selectedObject = getSelectedObjects();
		
		try {
			createWatchCommand(selectedObject);
		} catch (CoreException exception) {
			logger.error(exception.getMessage(), exception);
		}
		
		if(!selectedObject.isEmpty() && isWatcher){
			for(Object obj : selectedObject){
				if(obj instanceof LinkEditPart)	{
					count++;
				}
			}
		}
		
		if(count == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public static String getActiveProjectLocation() {
		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActivePart();
		IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput()
				.getAdapter(IFile.class);
		IProject project = file.getProject();
		String activeProjectLocation = project.getLocation().toOSString();
		return activeProjectLocation;
	}
	
	
	private void deleteDownloadedViewDataFile(Job job) {

		try{
			HttpClient httpClient = new HttpClient();

			String host=job.getIpAddress();
			String port;
			if(isLocalDebugMode()){
				host="localhost";
			}
						
			
			PostMethod postMethod = new PostMethod("http://" + host + ":" + job.getPortNumber() + "/deleteLocalDebugFile");
			postMethod.addParameter("jobId", watchRecordInner.getUniqueJobId());
			postMethod.addParameter("componentId", watchRecordInner.getComponentId());
			postMethod.addParameter("socketId", watchRecordInner.getSocketId());

			int response = httpClient.executeMethod(postMethod);

		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		
		final ReloadInformation reloadInformation = new ReloadInformation();
		
		String tempCopyPath = Utils.getDebugPath();
		tempCopyPath=tempCopyPath.trim();
		
		
		if(OSValidator.isWindows()){
			if(tempCopyPath.startsWith("/")){
				tempCopyPath = tempCopyPath.replaceFirst("/", "").replace("/", "\\");
			}			 
		}
		
		try {
			createWatchCommand(getSelectedObjects());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		
		Job job = DebugHandler.getJob(watchRecordInner.getCurrentJob());
		
		if(job==null){
			messageDialog("Looks like you forgot to run the job");
			return;
		}
		
		
		String windowName = job.getLocalJobID().replace(".", "_") + "_" + watchRecordInner.getComponentId() + "_"
				+ watchRecordInner.getSocketId();
		if (dataViewerMap.keySet().contains(windowName)) {
			dataViewerMap.get(windowName).getShell().setActive();
			return;
		}
		
		String watchFile = null;
		String watchFileName = null;
		if (!isWatcher) {
			messageDialog(Messages.MESSAGES_BEFORE_CLOSE_WINDOW);
		} else {
			if (isLocalDebugMode()) {
				watchFile = getLocalADVData();

				watchFileName = watchFile.substring(watchFile.lastIndexOf("/") + 1, watchFile.length()).replace(".csv", "");
				try {
					String destinationFile;
					if(OSValidator.isWindows()){
						destinationFile = (tempCopyPath + "\\" + watchFileName.trim() + ".csv").trim();
					}else{
						destinationFile = (tempCopyPath + "/" + watchFileName.trim() + ".csv").trim();
					}
					
					String sourceFile = watchFile.trim();
					
					File file = new File(destinationFile);
					if(!file.exists()) { 
						Files.copy(Paths.get(sourceFile), Paths.get(destinationFile), StandardCopyOption.REPLACE_EXISTING);
						deleteDownloadedViewDataFile(job);
					}
				} catch (IOException e) {
					messageDialog("Unable to fetach debug file");
					logger.debug("Unable to fetach debug file",e);
					return;
				}

			} else {
				watchFile = getRemoteADVData();
				watchFileName = watchFile.substring(watchFile.lastIndexOf("/") + 1, watchFile.length()).replace(".csv", "");

				String destinationFile;
				if(OSValidator.isWindows()){
					destinationFile = (tempCopyPath + "\\" + watchFileName.trim() + ".csv").trim();
				}else{
					destinationFile = (tempCopyPath + "/" + watchFileName.trim() + ".csv").trim();
				}
				File file = new File(destinationFile);
				if(!file.exists()) { 
					ScpFrom scpFrom = new ScpFrom();
					scpFrom.scpFileFromRemoteServer(job.getIpAddress(), job.getUsername(), job.getPassword(), watchFile.trim(),
							tempCopyPath);					
				}
				deleteDownloadedViewDataFile(job);
			}
		}
		
		
		final String dataViewerFilePath= tempCopyPath.trim();
		final String dataViewerFileh= watchFileName.trim();
		final String dataViewerWindowName = windowName;
		
		
		reloadInformation.setComponentID(watchRecordInner.getComponentId());
		reloadInformation.setComponentSocketID(watchRecordInner.getSocketId());
		reloadInformation.setUniqueJobID(watchRecordInner.getUniqueJobId());
		if (isLocalDebugMode()) {
			reloadInformation.setHost("localhost");
			reloadInformation.setIsLocalJob(true);
		}else{
			reloadInformation.setHost(job.getHost());
			reloadInformation.setIsLocalJob(false);
		}
		reloadInformation.setPassword(job.getPassword());
		reloadInformation.setUsername(job.getUserId());		
		reloadInformation.setBasepath(job.getBasePath());
		reloadInformation.setPort(job.getPortNumber());
		
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(dataViewerFilePath + dataViewerFileh + ".csv"));
			if (br.readLine() == null) {
			    messageDialog("Unable to show record, empty debug file");
			    return;
			}
		} catch (Exception e1) {
			 messageDialog("Unable to read debug file");
			logger.debug("Unable to read debug file",e1);
			return;
		}finally{
			try {
				if(br != null){
					br.close();
				}					
			} catch (IOException e) {
				logger.debug("Unable to close debug file",e);
			}
		}
		
		String path = dataViewerFilePath+dataViewerFileh;
		SchemaHelper.INSTANCE.exportSchemaGridData(getSelectedObjects(), path);
		
		Display.getDefault().asyncExec(new Runnable() {
		      @Override
		      public void run() {
		    	  try {

		  			DebugDataViewer window = new DebugDataViewer(dataViewerFilePath, dataViewerFileh, dataViewerWindowName,reloadInformation);
		  			window.setBlockOnOpen(true);
		  			dataViewerMap.put(dataViewerWindowName, window);
		  			
		  			window.open();
		  		} catch (Exception e) {
		  			logger.debug("Unable to open debug window",e);
		  			messageDialog("Unable to open debug window");
		  		}
		  		dataViewerMap.remove(dataViewerWindowName);
		      }
		    });
		if(dataViewerMap.get(dataViewerWindowName)!=null){
			dataViewerMap.get(dataViewerWindowName).getShell().setActive();
		}
		
	}
}

class WatchRecordInner{
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
