package com.bitwise.app.graph.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.common.util.OSValidator;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.graph.handler.RunJobHandler;
import com.bitwise.app.graph.handler.StopJobHandler;
import com.bitwise.app.joblogger.JobLogger;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.parametergrid.dialog.ParameterGridDialog;
import com.bitwise.app.propertywindow.runconfig.RunConfigDialog;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * 
 * Job manager maintains list of executing job.
 * This class is responsible for executing and killing given job
 * 
 * @author Bitwise
 *
 */
public class JobManager {

	private static Logger logger = LogFactory.INSTANCE.getLogger(JobManager.class);

	private static Map<String,Job> jobMap;	
	public static JobManager INSTANCE = new JobManager();
	private IEditorPart iEditorPart;

	private String activeCanvas;

	private JobManager(){
		jobMap = new LinkedHashMap<>();
	}
	
	private void addJob(Job job){
		jobMap.put(job.getCanvasName(), job);
		logger.debug("Added job " + job.getCanvasName() + " to job map");
	}

	private void removeJob(String canvasId){
		jobMap.remove(canvasId);
		logger.debug("Removed job " + canvasId + " from jobmap");
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

	
	private void enableRunJob(boolean enabled){
		((RunJobHandler)RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(enabled);
		((StopJobHandler)RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(!enabled);
	}
	
	/**
	 * execute job
	 * 
	 * @param job - job to execute
	 */
	public void executeJob(Job job){
		enableRunJob(false);
		
		final DefaultGEFCanvas gefCanvas = getComponentCanvas();
		
		iEditorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if(!saveJobBeforeExecute(gefCanvas)){
			return;
		}

		RunConfigDialog runConfigDialog = getRunConfiguration();
		if(!runConfigDialog.proceedToRunGraph()){	
			enableRunJob(true);
			return;
		}

		ParameterGridDialog parameterGrid = getParameters();
		if(parameterGrid.canRunGraph() == false){
			logger.debug("Not running graph");
			enableRunJob(true);
			return;
		}
		logger.debug("property File :"+parameterGrid.getParameterFile());

		String xmlPath = getJobXMLPath();		
		if (xmlPath == null) {				
			WidgetUtility.errorMessage("Please open a graph to run.");
			return;
		}

		String clusterPassword = getClusterPassword(runConfigDialog);
		
		gefCanvas.disableRunningJobResource();
		Process process=null;
		try {
			process = executeJob(xmlPath,parameterGrid.getParameterFile(),clusterPassword);
		} catch (IOException e) {
			logger.error("Error in Run Job",e);
		}
		
		job.setLocalProcessId(process);		
		final JobLogger joblogger = initJobLogger(gefCanvas);
		job.setConsoleName(gefCanvas.getActiveProject() + "." + gefCanvas.getJobName());
		job.setCanvasName(gefCanvas.getActiveProject() + "." + gefCanvas.getJobName());
		
		addJob(job);
		logProcessLogsAsyncronously(gefCanvas, joblogger, process,job.getCanvasName());
	}
	

	private void logProcessLogsAsyncronously(final DefaultGEFCanvas gefCanvas,
			final JobLogger joblogger, Process process, final String jobCanvasId) {
		final InputStream stream = process.getInputStream();
		new Thread(new Runnable() {
			public void run() {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(
							new InputStreamReader(stream));
					String line = null;
					while ((line = reader.readLine()) != null) {
						joblogger.logMessage(line);
					}
				} catch (Exception e) {
					logger.info("Error occured while reading run job log",e);
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							logger.error("Ignore the exception", e);
						}
					}
				}							
				joblogger.logJobEndInfo();
				joblogger.close();
				removeJob(jobCanvasId);
				
				if(jobCanvasId.equals(activeCanvas)){
					enableRunJob(true);
				}
				
				enableLockedResources(gefCanvas);

				refreshProject();
			}


		}).start();
	}

	private void enableLockedResources(
			final DefaultGEFCanvas gefCanvas) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				gefCanvas.enableRunningJobResource();

			}
		});
	}		
	private void refreshProject() {
		
		String projectName=((IFileEditorInput)iEditorPart.getEditorInput()).getFile().getProject().getName();
		IProject iProject=ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		try {
			iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			logger.error("Error while refreshing the project",e);
		}
	}

	private String getClusterPassword(RunConfigDialog runConfigDialog) {
		String clusterPassword = runConfigDialog.getClusterPassword()!=null ? runConfigDialog.getClusterPassword():"";
		return clusterPassword;
	}

	private String getJobXMLPath() {
		IEditorPart iEditorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		String xmlPath = iEditorPart.getEditorInput().getToolTipText().replace(Messages.JOBEXTENSION, Messages.XMLEXTENSION);
		return xmlPath;
	}

	private JobLogger initJobLogger(DefaultGEFCanvas gefCanvas) {
		final JobLogger joblogger = new JobLogger(gefCanvas.getActiveProject(), gefCanvas.getJobName());
		joblogger.logJobStartInfo();
		joblogger.logSystemInformation();
		return joblogger;
	}

	private ParameterGridDialog getParameters() {
		ParameterGridDialog parameterGrid = new ParameterGridDialog(Display.getDefault().getActiveShell());
		parameterGrid.setVisibleParameterGridNote(false);
		parameterGrid.open();
		return parameterGrid;
	}

	private RunConfigDialog getRunConfiguration() {
		RunConfigDialog runConfigDialog = new RunConfigDialog(Display.getDefault().getActiveShell());
		runConfigDialog.open();
		return runConfigDialog;
	}

	private boolean saveJobBeforeExecute(final DefaultGEFCanvas gefCanvas) {
		if(gefCanvas.getParameterFile() == null || isDirtyEditor()){
			try{
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().doSave(null);
				enableRunJob(true);
				if(gefCanvas.getParameterFile() == null || isDirtyEditor()){
					return false;
				}else{
					return true;
				}				
			}catch(Exception e){
				logger.debug("Unable to save graph ", e);
				enableRunJob(true);
				return false;
			}			
		}

		return true;
	}

	/**
	 * Kill the job for given jobId
	 * 
	 * @param jobId
	 */
	public void killJob(String jobId){

	}


	/**
	 * Execute run job.
	 *
	 * @param xmlPath the xml path that contain xml file name to run.
	 * @return the process
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Process executeJob(String xmlPath,String paramFile,String clusterPassword) throws IOException{
		String projectName = xmlPath.split("/", 2)[0];
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		String[] runCommand=new String[3];
		if (OSValidator.isWindows()) {
			logger.info("This is windows.");
			String[] command = {Messages.CMD,"/c",Messages.GRADLE_RUN + " " + Messages.XMLPATH + "=\""+ xmlPath.split("/", 2)[1] + "\" "+ Messages.PARAM_FILE+"=\""+paramFile+"\" "+ Messages.CLUSTER_PASSWORD+"=\""+clusterPassword+"\""};
			runCommand=command;
		} else if (OSValidator.isMac()) {
			logger.debug("This is Mac.");
			String[] command = {Messages.SHELL,"-c",Messages.GRADLE_RUN + " " + Messages.XMLPATH + "=\""+ xmlPath.split("/", 2)[1] + "\" "+ Messages.PARAM_FILE+"=\""+paramFile+"\" "+ Messages.CLUSTER_PASSWORD+"=\""+clusterPassword+"\""};
			runCommand=command;
		} else if (OSValidator.isUnix()) {
			logger.debug("This is Unix or Linux");
		} else if (OSValidator.isSolaris()) {
			logger.debug("This is Solaris");
		} else {
			logger.debug("Your OS is not supported!!");
		}

		ProcessBuilder pb = new ProcessBuilder(runCommand);
		pb.directory(new File(project.getLocation().toOSString()));
		pb.redirectErrorStream(true);
		Process process = pb.start();
		return process;
	}

	/**
	 * isJobRunning() returns true of job is executing for given console
	 * 
	 * @param consoleName
	 * @return
	 */
	public boolean isJobRunning(String consoleName) {		
		return jobMap.containsKey(consoleName);
	}

	/**
	 * 
	 * Set active console id
	 * 
	 * @param activeCanvas
	 */
	public void setActiveCanvasId(String activeCanvas){
		this.activeCanvas = activeCanvas;
	}
}
