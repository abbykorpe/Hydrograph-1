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
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.debugconverter.DebugConverter;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.graph.utility.JobScpAndProcessUtility;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.runconfig.RunConfigDialog;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;



/**
 * @author Bitwise
 *
 */
public class DebugHandler  extends AbstractHandler {
	private Logger logger = LogFactory.INSTANCE.getLogger(DebugHandler.class);
	private static Map<String,Job> jobMap = new HashMap<>();	
	private IPath currentJobIPath=null;
	private String uniqueJobID =null;
	private String basePath = null;
	private String currentJobName = null;
	 
	
	
	public DebugHandler(){
		RunStopButtonCommunicator.RunDebugJob.setHandler(this);
	}

	/**
	 * Enable disable debug button
	 * 
	 * @param enable
	 */
	public void setDebugJobEnabled(boolean enable){
		setBaseEnabled(enable);
	}
	
	public static Job getJob(String jobName) {
		return jobMap.get(jobName);
	}
	
	 
	public void addDebugJob(String jobId, Job debugJob){
		jobMap.put(jobId, debugJob);
		
	}
	
	private DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}
	
	private boolean isDirtyEditor(){
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().isDirty();
	}

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

	private void closeOpenedDataViewerWindows(){
		
		for(DebugDataViewer debugDataViewer:JobManager.INSTANCE.getDataViewerMap().values()){
			debugDataViewer.close();
		}
	}
	
	@Override
	public Object execute(ExecutionEvent event){
		closeOpenedDataViewerWindows();
		
		
		if(getComponentCanvas().getParameterFile() == null || isDirtyEditor()){
			try{
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().doSave(null);
				JobManager.INSTANCE.enableRunJob(true);
				if(getComponentCanvas().getParameterFile() == null || isDirtyEditor()){
					CanvasUtils.getComponentCanvas().restoreMenuToolContextItemsState();					
					return null;
				}
			}catch(Exception e){
				logger.debug("Unable to save graph ", e);
				CanvasUtils.getComponentCanvas().restoreMenuToolContextItemsState();
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
		
		RunConfigDialog runConfigDialog = new RunConfigDialog(Display.getDefault().getActiveShell(), true);
		runConfigDialog.open();
		if (!runConfigDialog.proceedToRunGraph()) {
			//JobManager.INSTANCE.enableDebugJob(true);
	
		}
		String clusterPassword = runConfigDialog.getClusterPassword()!=null ? runConfigDialog.getClusterPassword():"";
		basePath = runConfigDialog.getBasePath();
		String host = runConfigDialog.getHost();
		String userId = runConfigDialog.getUserId();
		if(!runConfigDialog.proceedToRunGraph()){
			setBaseEnabled(true);
			JobManager.INSTANCE.enableRunJob(true);
			//JobManager.INSTANCE.enableDebugJob(true);
			CanvasUtils.getComponentCanvas().restoreMenuToolContextItemsState();			
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
		
		String portNumber =runConfigDialog.getPortNo();
		
		if(!StringUtils.isEmpty(portNumber)){
			job.setPortNumber(portNumber);
		}else{
			job.setPortNumber("8004");
		}
		job.setDebugMode(true);
		job.setPassword(clusterPassword);
		job.setRemoteMode(runConfigDialog.isRemoteMode());
		 
		addDebugJob(currentJobName, job);
		
		JobManager.INSTANCE.executeJobInDebug(job, runConfigDialog.isRemoteMode(), runConfigDialog.getUsername());
		CanvasUtils.getComponentCanvas().restoreMenuToolContextItemsState();		
		return null;
	}
 
	public String getJobId() {
		return uniqueJobID;
	}

	public String getBasePath() {
		return basePath;
	}

	public static Map<String, Job> getJobMap() {
		return jobMap;
	}

	public static void setJobMap(Map<String, Job> jobMap) {
		DebugHandler.jobMap = jobMap;
	}

}
