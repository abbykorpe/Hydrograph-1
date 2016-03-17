package com.bitwise.app.graph.handler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.debugconverter.DebugConverter;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.job.Job;
import com.bitwise.app.graph.job.JobManager;
import com.bitwise.app.graph.job.RunStopButtonCommunicator;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.runconfig.RunConfigDialog;


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

	private void createDebugXml() throws IOException, NoSuchAlgorithmException{
		String currentJobPath=null;
		
		
		ELTGraphicalEditor eltGraphicalEditor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(eltGraphicalEditor.getEditorInput() instanceof GraphicalEditor)
		{}
		else
			currentJobIPath=new Path(eltGraphicalEditor.getTitleToolTip());
			DebugConverter converter = new DebugConverter();
			
		try {
			uniqueJobID= eltGraphicalEditor.generateUniqueJobId();
			currentJobPath = currentJobIPath.lastSegment().replace(Constants.JOB_EXTENSION, Constants.DEBUG_EXTENSION);
			currentJobName = currentJobIPath.lastSegment().replace(Constants.JOB_EXTENSION, "");
			currentJobIPath = currentJobIPath.removeLastSegments(1).append(currentJobPath);
			
			converter.marshall(converter.getParam(), ResourcesPlugin.getWorkspace().getRoot().getFile(currentJobIPath));
			
			
		} catch (JAXBException | IOException e) {
			logger.error(e.getMessage(), e);
		} catch (CoreException e) {
			logger.error(e.getMessage(), e);
		}
	}


	
	@Override
	public Object execute(ExecutionEvent event){
		 

		if(getComponentCanvas().getParameterFile() == null || isDirtyEditor()){
			try{
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().doSave(null);
				JobManager.INSTANCE.enableDebugJob(true);
				if(getComponentCanvas().getParameterFile() == null || isDirtyEditor()){
					return null;
				}
			}catch(Exception e){
				logger.debug("Unable to save graph ", e);
					JobManager.INSTANCE.enableDebugJob(true);
			}
		}
		
		try {
			createDebugXml();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}
		
		RunConfigDialog runConfigDialog = new RunConfigDialog(Display.getDefault().getActiveShell(), true);
		runConfigDialog.open();
		if (!runConfigDialog.proceedToRunGraph()) {
			JobManager.INSTANCE.enableDebugJob(true);
	
		}
		String clusterPassword = runConfigDialog.getClusterPassword()!=null ? runConfigDialog.getClusterPassword():"";
		basePath = runConfigDialog.getBasePath();
		String host = runConfigDialog.getHost();
		String userId = runConfigDialog.getUserId();
		if(!runConfigDialog.proceedToRunGraph()){
			setBaseEnabled(true);
			JobManager.INSTANCE.enableDebugJob(true);
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
		

		job.setDebugMode(true);
		job.setPassword(clusterPassword);
		job.setRemoteMode(runConfigDialog.isRemoteMode());
		 
		addDebugJob(currentJobName, job);
		
		
	 
		JobManager.INSTANCE.executeJobInDebug(job, uniqueJobID, runConfigDialog.isRemoteMode(), runConfigDialog.getUsername());
		
		return null;
	}
 
	public String getJobId() {
		return uniqueJobID;
	}

	public String getBasePath() {
		return basePath;
	}
}