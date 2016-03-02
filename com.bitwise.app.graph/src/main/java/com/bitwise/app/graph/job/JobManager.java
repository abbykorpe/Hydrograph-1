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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.common.util.OSValidator;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.graph.handler.RunJobHandler;
import com.bitwise.app.graph.handler.StopJobHandler;
import com.bitwise.app.graph.utility.CanvasUtils;
import com.bitwise.app.joblogger.JobLogger;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.parametergrid.dialog.ParameterGridDialog;
import com.bitwise.app.propertywindow.runconfig.RunConfigDialog;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * 
 * Job manager maintains list of executing job. This class is responsible for executing and killing given job
 * 
 * @author Bitwise
 * 
 */
public class JobManager {

	private static Logger logger = LogFactory.INSTANCE.getLogger(JobManager.class);
	Map<String, Job> jobMap;
	public static JobManager INSTANCE = new JobManager();

	private String activeCanvas;

	private JobManager() {
		jobMap = new LinkedHashMap<>();
	}

	/**
	 * 
	 * Register job with Job Manager
	 * 
	 * @param job
	 *            - {@link Job}
	 */
	void addJob(Job job) {
		jobMap.put(job.getLocalJobID(), job);
		logger.debug("Added job " + job.getCanvasName() + " to job map");
	}

	/**
	 * Deregister job with Job Manager
	 * 
	 * @param canvasId
	 */
	void removeJob(String canvasId) {
		jobMap.remove(canvasId);
		logger.debug("Removed job " + canvasId + " from jobmap");
	}

	/**
	 * Toggles state of Run and Stop button if enabled is true Run button will enable and stop button will disable if
	 * enable is false Run button will disbale and stop will enable
	 * 
	 * @param enabled
	 */
	void enableRunJob(boolean enabled) {
		((RunJobHandler) RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(enabled);
		((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(!enabled);
	}

	/**
	 * execute job
	 * 
	 * @param job
	 *            - {@link Job} to execute
	 */
	public void executeJob(final Job job) {
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

		final ParameterGridDialog parameterGrid = getParameters();
		if (parameterGrid.canRunGraph() == false) {
			logger.debug("Not running graph");
			enableRunJob(true);
			return;
		}
		logger.debug("property File :" + parameterGrid.getParameterFile());

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

	private void launchJob(final Job job, final DefaultGEFCanvas gefCanvas, final ParameterGridDialog parameterGrid,
			final String xmlPath) {
		if (job.isRemoteMode()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					AbstractJobLauncher jobLauncher = new RemoteJobLauncher();
					jobLauncher.launchJob(xmlPath, parameterGrid.getParameterFile(), job, gefCanvas);
				}

			}).start();
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					AbstractJobLauncher jobLauncher = new LocalJobLauncher();
					jobLauncher.launchJob(xmlPath, parameterGrid.getParameterFile(), job, gefCanvas);
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
	 */
	public void killJob(String jobId) {
		Job jobToKill = jobMap.get(jobId);

		jobToKill.setJobStatus(JobStatus.KILLED);

		MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
		messageBox.setText(Messages.KILL_JOB_MESSAGEBOX_TITLE);

		if (jobToKill.isRemoteMode()) {
			messageBox.setMessage(Messages.REMOTE_KILL_REQUEST_ACCEPTED);
			((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
		} else {
			messageBox.setMessage(Messages.LOCAL_KILL_REQUEST_ACCEPTED);
		}

		messageBox.open();

		if (jobToKill.getRemoteJobProcessID() != null) {
			killRemoteProcess(jobToKill);
		}

	}

	JobLogger initJobLogger(DefaultGEFCanvas gefCanvas) {
		final JobLogger joblogger = new JobLogger(gefCanvas.getActiveProject(), gefCanvas.getJobName());
		return joblogger;
	}

	public Job getJob(String consoleName) {
		return jobMap.get(consoleName);
	}

	private void killRemoteProcess(Job job) {

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
			logKillProcessLogsAsyncronously(process, job, CanvasUtils.getComponentCanvas());
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
	 * Enables locked resouces..like job canvas
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
		final JobLogger joblogger = initJobLogger(CanvasUtils.getComponentCanvas());
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
		return jobMap.containsKey(consoleName);
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
	String getActiveCanvas() {
		return activeCanvas;
	}
}
