/********************************************************************************
  * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  * http://www.apache.org/licenses/LICENSE2.0
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  ******************************************************************************/

package hydrograph.ui.graph.handler;

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.communication.debugservice.DebugServiceClient;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.graph.debugconverter.DebugConverter;
import hydrograph.ui.graph.debugconverter.SchemaHelper;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.graph.utility.DataViewerUtility;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.runconfig.RunConfigDialog;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;


/**
 * Handler use to run the job on debug mode.
 * @author Bitwise
 *
 */
public class DebugHandler{
	
	/** Default debug service port */
	private static final String DEFAULT_DEBUG_SERVICE_PORT = "8004";

	/** The logger. */
	private Logger logger = LogFactory.INSTANCE.getLogger(DebugHandler.class);
	
	/** The job map. */
	private static Map<String,Job> jobMap = new HashMap<>();	
	
	/** The current job Ipath. */
	private IPath currentJobIPath=null;
	
	/** The unique job id. */
	private String uniqueJobID =null;
	
	/** The base path. */
	private String basePath = null;
	
	/** The current job name. */
	private String currentJobName = null;
	
	private List<String> dataViewFileIds =  new ArrayList<String>();
	
	/**
	 * Gets the job.
	 *
	 * @param jobName the job name
	 * @return the job
	 */
	public static Job getJob(String jobName) {
		return jobMap.get(jobName);
	}
	
	 
	/**
	 * Adds the debug job.
	 *
	 * @param jobId the job id
	 * @param debugJob the debug job
	 */
	public void addDebugJob(String jobId, Job debugJob){
		jobMap.put(jobId, debugJob);
		
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
	
	/**
	 * Checks if is dirty editor.
	 *
	 * @return true, if is dirty editor
	 */
	private boolean isDirtyEditor(){
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().isDirty();
	}

	/**
	 * Creates the debug xml.
	 *
	 * @throws Exception the exception
	 */
	private void createDebugXml() throws Exception{
		String currentJobPath=null;
		ELTGraphicalEditor eltGraphicalEditor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(!(eltGraphicalEditor.getEditorInput() instanceof GraphicalEditor)){
			currentJobIPath=new Path(eltGraphicalEditor.getTitleToolTip());
		}
		
		DebugConverter converter = new DebugConverter();
			
		try {
			uniqueJobID= eltGraphicalEditor.generateUniqueJobId();
			currentJobPath = currentJobIPath.lastSegment().replace(Constants.JOB_EXTENSION, Constants.DEBUG_EXTENSION);
			currentJobName = currentJobIPath.lastSegment().replace(Constants.JOB_EXTENSION, "");
			currentJobIPath = currentJobIPath.removeLastSegments(1).append(currentJobPath);
			
			converter.marshall(converter.getParam(), ResourcesPlugin.getWorkspace().getRoot().getFile(currentJobIPath));
		} catch (JAXBException | IOException  | CoreException exception) {
			logger.error(exception.getMessage(), exception);
		} 
	}
	
	/*
	 * execute method launch the job in debug mode.
	 */
	public Object execute(RunConfigDialog runConfigDialog){
		DataViewerUtility.INSTANCE.closeDataViewerWindows();
		
		if(getComponentCanvas().getParameterFile() == null || isDirtyEditor()){
			try{
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().doSave(null);
				JobManager.INSTANCE.enableRunJob(true);
				if(getComponentCanvas().getParameterFile() == null || isDirtyEditor()){
					CanvasUtils.INSTANCE.getComponentCanvas().restoreMenuToolContextItemsState();					
					return null;
				}
			}catch(Exception e){
				logger.debug("Unable to save graph ", e);
				CanvasUtils.INSTANCE.getComponentCanvas().restoreMenuToolContextItemsState();
					JobManager.INSTANCE.enableRunJob(true);
			}
		}
		
		try {
			createDebugXml();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		String clusterPassword = runConfigDialog.getClusterPassword()!=null ? runConfigDialog.getClusterPassword():"";
		basePath = runConfigDialog.getBasePath();
		String host = runConfigDialog.getHost();
		String userId = runConfigDialog.getUserId();
		if(!runConfigDialog.proceedToRunGraph()){
			JobManager.INSTANCE.enableRunJob(true);
			CanvasUtils.INSTANCE.getComponentCanvas().restoreMenuToolContextItemsState();			
			return null;
		}
		
		String consoleName= getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName();
		String canvasName = consoleName;
		String localJobID = consoleName;

		Job job = new Job(localJobID, consoleName, canvasName, basePath, host, userId, clusterPassword); 
		job.setBasePath(basePath);
		job.setIpAddress(host);
		job.setUserId(userId);
		job.setHost(runConfigDialog.getHost());
		job.setUsername(runConfigDialog.getUsername());
		job.setRemoteMode(runConfigDialog.isRemoteMode());
		job.setPassword(clusterPassword);
		job.setUniqueJobId(uniqueJobID);
		
		IFile file=ResourcesPlugin.getWorkspace().getRoot().getFile(currentJobIPath);
		job.setDebugFilePath(file.getFullPath().toString());
		
		String portNumber = Utils.INSTANCE.getServicePortNo();
		
		if(!StringUtils.isEmpty(portNumber)){
			job.setPortNumber(portNumber);
		}else{
			job.setPortNumber(DEFAULT_DEBUG_SERVICE_PORT);
		}
		job.setDebugMode(true);
		job.setPassword(clusterPassword);
		job.setRemoteMode(runConfigDialog.isRemoteMode());
		 
		addDebugJob(currentJobName, job);
		if(!dataViewFileIds.isEmpty()){
			deletePreviousRunsDataviewCsvXmlFiles();
			deletePreviousRunsBasePathDebugFiles(host, job.getPortNumber(), uniqueJobID, basePath, userId, clusterPassword);
			dataViewFileIds.clear();
		}
		
		JobManager.INSTANCE.executeJobInDebug(job, runConfigDialog.isRemoteMode(), runConfigDialog.getUsername());
		CanvasUtils.INSTANCE.getComponentCanvas().restoreMenuToolContextItemsState();	
		
		exportSchemaFile();
		
		return null;
	}
	
	private void deletePreviousRunsDataviewCsvXmlFiles(){
		String dataViewerDirectoryPath = Utils.INSTANCE.getDataViewerDebugFilePath();

		IPath path = new Path(dataViewerDirectoryPath);
		boolean deleted = false;
		String dataViewerSchemaFilePathToBeDeleted = "";
		if(path.toFile().isDirectory()){
			String[] fileList = path.toFile().list();
			for (String file: fileList){
				for (String previousFileID: dataViewFileIds){
					if(file.contains(previousFileID)){
						if (OSValidator.isWindows()){
							dataViewerSchemaFilePathToBeDeleted = dataViewerDirectoryPath+ "\\" + file;
						}else{
							dataViewerSchemaFilePathToBeDeleted = dataViewerDirectoryPath+ "/" + file;
						}
						path = new Path(dataViewerSchemaFilePathToBeDeleted);
						if(path.toFile().exists()){
							deleted = path.toFile().delete();
							if(deleted){
								logger.debug("Deleted Data Viewer file {}", dataViewerSchemaFilePathToBeDeleted);
							}else{
								logger.warn("Unable to delete Viewer file {}", dataViewerSchemaFilePathToBeDeleted);
							}
						}
					}
				}
			}
		}
	}
	
	private void deletePreviousRunsBasePathDebugFiles(String host, String port, String uniqJobId, String basePath, 
			String userID, String password){
		if(Utils.INSTANCE.isPurgeViewDataPrefSet()){
			for(String previousFileID: dataViewFileIds){
				try {
					DebugServiceClient.INSTANCE.deleteBasePathFiles(host, port, previousFileID, basePath, userID, password);
				} catch (NumberFormatException e) {
					logger.warn("Unable to delete debug Base path file",e);
				} catch (HttpException e) {
					logger.warn("Unable to delete debug Base path file",e);
				} catch (MalformedURLException e) {
					logger.warn("Unable to delete debug Base path file",e);
				} catch (IOException e) {
					logger.warn("Unable to delete debug Base path file",e);
				}
			}
		}
	}
 
	private void exportSchemaFile(){
		String validPath = null;
		String filePath = null;
		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		String jobId = editor.getJobId();
		dataViewFileIds.add(jobId);
		String path = Utils.INSTANCE.getDataViewerDebugFilePath();
		if(StringUtils.isNotBlank(path)){
			logger.debug("validating file path : {}", path);
			validPath = SchemaHelper.INSTANCE.validatePath(path);
			
			createDirectoryIfNotExist(validPath);
			
			filePath = validPath + jobId;
			SchemaHelper.INSTANCE.exportSchemaFile(filePath);
		}else{
			logger.debug("File path does not exist : {}", path);
		}
	}


	private void createDirectoryIfNotExist(String validPath) {
		File directory = new File(validPath);
		if(!directory.exists()){
			directory.mkdirs();
		}
	}
	
	/**
	 * Gets the job id.
	 *
	 * @return the job id
	 */
	public String getJobId() {
		return uniqueJobID;
	}

	/**
	 * Gets the base path.
	 *
	 * @return the base path
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * Gets the job map.
	 *
	 * @return the job map
	 */
	public static Map<String, Job> getJobMap() {
		return jobMap;
	}

	/**
	 * Sets the job map.
	 *
	 * @param jobMap the job map
	 */
	public static void setJobMap(Map<String, Job> jobMap) {
		DebugHandler.jobMap = jobMap;
	}

	
}
