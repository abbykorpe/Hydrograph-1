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

 
package hydrograph.ui.graph.job;

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.common.util.MultiParameterFileUIUtils;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.datastructures.parametergrid.ParameterFile;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.debug.service.ViewDataServiceInitiator;
import hydrograph.ui.graph.handler.DebugHandler;
import hydrograph.ui.graph.handler.RunJobHandler;
import hydrograph.ui.graph.handler.StopJobHandler;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.joblogger.JobLogger;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.parametergrid.dialog.MultiParameterFileDialog;
import hydrograph.ui.propertywindow.runconfig.RunConfigDialog;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;


/**
 * 
 * Job manager maintains list of executing job. This class is responsible for executing and killing given job
 * 
 * @author Bitwise
 * 
 */
public class JobManager {

	private static Logger logger = LogFactory.INSTANCE.getLogger(JobManager.class);
	private Map<String, Job> runningJobsMap;
	public static JobManager INSTANCE = new JobManager();
	private boolean localMode;


	public boolean isLocalMode() {
		return localMode;
	}

	public void setLocalMode(boolean localMode) {
		this.localMode = localMode;
	}

	private String activeCanvas;
	
	private JobManager() {
		runningJobsMap = new LinkedHashMap<>();
	}
	
	/**
	 * 
	 * Returns active editor as {@link DefaultGEFCanvas}
	 * 
	 * @return {@link DefaultGEFCanvas}
	 */
	private DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}

	/**
	 * 
	 * Register job with Job Manager
	 * 
	 * @param job
	 *            - {@link Job}
	 */
	void addJob(Job job) {
		runningJobsMap.put(job.getLocalJobID(), job);
		logger.debug("Added job " + job.getCanvasName() + " to job map");
	}

	/**
	 * Deregister job with Job Manager
	 * 
	 * @param canvasId
	 */
	void removeJob(String canvasId) {
		runningJobsMap.remove(canvasId);
		logger.debug("Removed job " + canvasId + " from jobmap");
	}

	/**
	 * Toggles state of Run and Stop button if enabled is true Run button will enable and stop button will disable if
	 * enable is false Run button will disbale and stop will enable
	 * 
	 * @param enabled
	 */
	public void enableRunJob(boolean enabled) {
		((RunJobHandler)RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(enabled);
		((StopJobHandler)RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(!enabled);
		((DebugHandler)RunStopButtonCommunicator.RunDebugJob.getHandler()).setDebugJobEnabled(enabled);
	}
	
	/**
	 * execute job
	 * 
	 * @param job
	 *            - {@link Job} to execute
	 */
	public void executeJob(final Job job, String uniqueJobId) {
		enableRunJob(false);
		final DefaultGEFCanvas gefCanvas = CanvasUtils.getComponentCanvas();

		if (!saveJobBeforeExecute(gefCanvas)) {
			return;
		}

		RunConfigDialog runConfigDialog = getRunConfiguration();

		if (!runConfigDialog.proceedToRunGraph()) {
			enableRunJob(true);
			return;
		}
		
		final MultiParameterFileDialog parameterGrid = getParameterFileDialog();
		if (parameterGrid.canRunGraph() == false) {
			logger.debug("Not running graph");
			enableRunJob(true);
			return;
		}
		logger.debug("property File :" + parameterGrid.getParameterFilesForExecution());

		final String xmlPath = getJobXMLPath();
		if (xmlPath == null) {
			WidgetUtility.errorMessage(Messages.OPEN_GRAPH_TO_RUN);
			return;
		}

		String clusterPassword = getClusterPassword(runConfigDialog);

		job.setUsername(runConfigDialog.getUsername());
		job.setPassword(clusterPassword);
		job.setHost(runConfigDialog.getHost());
		job.setRemoteMode(runConfigDialog.isRemoteMode());

		gefCanvas.disableRunningJobResource();
		
			launchJob(job, gefCanvas, parameterGrid, xmlPath);
	}

	public void executeJobInDebug(final Job job, String uniqueJobId, boolean isRemote, String userName) {
		enableRunJob(false);
		final DefaultGEFCanvas gefCanvas = CanvasUtils.getComponentCanvas();

		if (!saveJobBeforeExecute(gefCanvas)) {
			return;
		}
		
		final MultiParameterFileDialog parameterGrid = getParameterFileDialog();
		if (parameterGrid.canRunGraph() == false) {
			logger.debug("Not running graph");
			enableRunJob(true);
			return;
		}
		logger.debug("property File :" + parameterGrid.getParameterFilesForExecution());

		final String xmlPath = getJobXMLPath();
		String debugXmlPath = getJobDebugXMLPath();
		if (xmlPath == null) {
			WidgetUtility.errorMessage(Messages.OPEN_GRAPH_TO_RUN);
			return;
		}
		
		job.setUsername(userName);
		job.setRemoteMode(isRemote);
		job.setHost(job.getIpAddress());

		gefCanvas.disableRunningJobResource();

		launchJobWithDebugParameter(job, gefCanvas, parameterGrid, xmlPath, debugXmlPath, job.getBasePath(), uniqueJobId);
	}
		
	private void launchJob(final Job job, final DefaultGEFCanvas gefCanvas, final MultiParameterFileDialog parameterGrid,
			final String xmlPath) {
		if (job.isRemoteMode()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					AbstractJobLauncher jobLauncher = new RemoteJobLauncher();
					jobLauncher.launchJob(xmlPath, parameterGrid.getParameterFilesForExecution(), job, gefCanvas);
				}

			}).start();
		} else {
			setLocalMode(true);
			new Thread(new Runnable() {

				@Override
				public void run() {
					AbstractJobLauncher jobLauncher = new LocalJobLauncher();
					jobLauncher.launchJob(xmlPath, parameterGrid.getParameterFilesForExecution(), job, gefCanvas);
				}

			}).start();
		}
	}

	private void launchJobWithDebugParameter(final Job job, final DefaultGEFCanvas gefCanvas, final MultiParameterFileDialog parameterGrid,
			final String xmlPath, final String debugXmlPath, final String basePath, final String uniqueJobId) {
		if (job.isRemoteMode()) {
			setLocalMode(false);
			new Thread(new Runnable() {
				@Override
				public void run() {
					AbstractJobLauncher jobLauncher = new DebugRemoteJobLauncher();
					jobLauncher.launchJobInDebug(xmlPath, debugXmlPath, basePath, parameterGrid.getParameterFilesForExecution(), job, gefCanvas, uniqueJobId);
				}

			}).start();
		} else {
			setLocalMode(true);
			ViewDataServiceInitiator.startService();
			new Thread(new Runnable() {

				@Override
				public void run() {
					AbstractJobLauncher jobLauncher = new DebugLocalJobLauncher();
					jobLauncher.launchJobInDebug(xmlPath, debugXmlPath, basePath, parameterGrid.getParameterFilesForExecution(), job, gefCanvas, uniqueJobId);
				}

			}).start();
		}
	}
	
	private String getClusterPassword(RunConfigDialog runConfigDialog) {
		String clusterPassword = runConfigDialog.getClusterPassword() != null ? runConfigDialog.getClusterPassword()
				: "";
		return clusterPassword;
	}

	private String getJobXMLPath() {
		IEditorPart iEditorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		String xmlPath = iEditorPart.getEditorInput().getToolTipText()
				.replace(Messages.JOBEXTENSION, Messages.XMLEXTENSION);
		return xmlPath;
	}

	private String getJobDebugXMLPath() {
		IEditorPart iEditorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		String debugXmlPath = iEditorPart.getEditorInput().getToolTipText().replace(Messages.JOBEXTENSION, "_debug.xml");
		 
		return debugXmlPath;
	}
	
	private MultiParameterFileDialog getParameterFileDialog(){
		
	    String activeProjectLocation=MultiParameterFileUIUtils.getActiveProjectLocation();
	 
		FileInputStream fin;
		List<ParameterFile> filepathList = new LinkedList<>();
		
		updateParameterFileListWithJobSpecificFile(filepathList);
		
		try {
			fin = new FileInputStream(activeProjectLocation + "\\project.metadata");
			ObjectInputStream ois = new ObjectInputStream(fin);
			filepathList.addAll((LinkedList<ParameterFile>)ois.readObject());
		} catch (FileNotFoundException fileNotfoundException) {
			logger.debug("Unable to read file" , fileNotfoundException);
		} catch (IOException ioException) {
			logger.debug("Unable to read file" , ioException);
		} catch (ClassNotFoundException classNotFoundException) {
			logger.debug("Unable to read file" , classNotFoundException);
		}
	    
		MultiParameterFileDialog parameterFileDialog = new MultiParameterFileDialog(Display.getDefault().getActiveShell(), activeProjectLocation);
		parameterFileDialog.setParameterFiles(filepathList);
		parameterFileDialog.open();
		
		return parameterFileDialog;
	}
	
	private void updateParameterFileListWithJobSpecificFile(List<ParameterFile> parameterFileList) {
		
		String parameterFile = getComponentCanvas().getParameterFile();
		if(OSValidator.isWindows()){
			parameterFile.replace("/", "\\");
		} 
		parameterFileList.add(new ParameterFile(getComponentCanvas().getJobName().replace("job", "properties"),
				parameterFile, true, true));
	}

	private RunConfigDialog getRunConfiguration() {
		RunConfigDialog runConfigDialog = new RunConfigDialog(Display.getDefault().getActiveShell(), false);
		runConfigDialog.open();
		return runConfigDialog;
	}

	private boolean saveJobBeforeExecute(final DefaultGEFCanvas gefCanvas) {
		if (gefCanvas.getParameterFile() == null || CanvasUtils.isDirtyEditor()) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().doSave(null);
				enableRunJob(true);
				if (gefCanvas.getParameterFile() == null || CanvasUtils.isDirtyEditor()) {
					return false;
				} else {
					return true;
				}
			} catch (Exception e) {
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
	 * @param gefCanvas 
	 */
	public void killJob(String jobId, DefaultGEFCanvas gefCanvas) {
		Job jobToKill = runningJobsMap.get(jobId);

		jobToKill.setJobStatus(JobStatus.KILLED);

		if (jobToKill.getRemoteJobProcessID() != null) {
			killRemoteProcess(jobToKill,gefCanvas);
		}

	}
	
	/**
	 * Kill the job for given jobId
	 * 
	 * @param jobId
	 * @param gefCanvas 
	 */
	public void killJob(String jobId) {	
		Job jobToKill = runningJobsMap.get(jobId);
		((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
		if(jobToKill.isRemoteMode()){
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() , SWT.ICON_WARNING | SWT.YES | SWT.NO);
			messageBox.setText(Messages.KILL_JOB_MESSAGEBOX_TITLE);
			messageBox.setMessage(Messages.KILL_JOB_MESSAGE);
			if(messageBox.open() == SWT.YES){
				jobToKill.setJobStatus(JobStatus.KILLED);
			}else{
				if(runningJobsMap.get(jobId) != null)
					((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(true);
			}
		}		
	}

	private JobLogger initJobLogger(DefaultGEFCanvas gefCanvas) {
		final JobLogger joblogger = new JobLogger(gefCanvas.getActiveProject(), gefCanvas.getJobName());
		return joblogger;
	}

	public Job getRunningJob(String consoleName) {
		return runningJobsMap.get(consoleName);
	}

	private void killRemoteProcess(Job job, DefaultGEFCanvas gefCanvas) {

		String gradleCommand = getKillJobCommand(job);
		String[] runCommand = new String[3];
		if (OSValidator.isWindows()) {
			String[] command = { Messages.CMD, "/c", gradleCommand };
			runCommand = command;

		} else if (OSValidator.isMac()) {
			String[] command = { Messages.SHELL, "-c", gradleCommand };
			runCommand = command;
		}

		ProcessBuilder processBuilder = new ProcessBuilder(runCommand);
		processBuilder.directory(new File(job.getJobProjectDirectory()));
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();
			logKillProcessLogsAsyncronously(process, job, gefCanvas);
		} catch (IOException e) {
			logger.debug("Unable to kill the job", e);
		}
	}

	private void releaseResources(Job job, DefaultGEFCanvas gefCanvas, JobLogger joblogger) {
		enableLockedResources(gefCanvas);
		refreshProject(gefCanvas);
		if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
			JobManager.INSTANCE.enableRunJob(true);
		}
		JobManager.INSTANCE.removeJob(job.getCanvasName());

		joblogger.logJobEndInfo();
		joblogger.close();
		JobManager.INSTANCE.removeJob(job.getLocalJobID());
	}

	/**
	 * Enables locked resources..like job canvas
	 * 
	 * @param {@link DefaultGEFCanvas}
	 */
	protected void enableLockedResources(final DefaultGEFCanvas gefCanvas) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				gefCanvas.enableRunningJobResource();
			}
		});
	}

	/**
	 * 
	 * Refresh project directory corresponding to given {@link DefaultGEFCanvas}
	 * 
	 * @param gefCanvas
	 */
	protected void refreshProject(DefaultGEFCanvas gefCanvas) {
		IEditorPart iEditorPart = ((IEditorPart) gefCanvas);
		String projectName = ((IFileEditorInput) iEditorPart.getEditorInput()).getFile().getProject().getName();
		IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		try {
			iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			logger.error("Error while refreshing the project", e);
		}
	}

	private void logKillProcessLogsAsyncronously(final Process process, final Job job, final DefaultGEFCanvas gefCanvas) {
		final JobLogger joblogger = initJobLogger(gefCanvas);
		new Thread(new Runnable() {
			InputStream stream = process.getInputStream();

			public void run() {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new InputStreamReader(stream));
					String line = null;

					while ((line = reader.readLine()) != null) {
						joblogger.logMessage(line);
					}
				} catch (Exception e) {
					logger.info("Error occured while reading run job log", e);
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							logger.error("Ignore the exception", e);
						}
					}
				}

				releaseResources(job, gefCanvas, joblogger);
			}

		}).start();
	}

	private String getKillJobCommand(Job job) {
		return GradleCommandConstants.GCMD_KILL_REMOTE_JOB + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_REMOTE_PROCESSID + job.getRemoteJobProcessID();
	}

	/**
	 * isJobRunning() returns true of job is executing for given console
	 * 
	 * @param consoleName
	 * @return
	 */
	public boolean isJobRunning(String consoleName) {
		return runningJobsMap.containsKey(consoleName);
	}

	/**
	 * 
	 * Set active console id
	 * 
	 * @param activeCanvas
	 */
	public void setActiveCanvasId(String activeCanvas) {
		this.activeCanvas = activeCanvas;
	}

	/**
	 * 
	 * Returns active canvas id
	 * 
	 * @return - String (active canvas id)
	 */
	public String getActiveCanvas() {
		return activeCanvas;
	}

	public Map<String, Job> getRunningJobsMap() {
		return runningJobsMap;
	}
	
}
