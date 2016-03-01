package com.bitwise.app.graph.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
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
	private IEditorPart iEditorPart;

	private String activeCanvas;
	JobLogger joblogger;

	private JobManager() {
		jobMap = new LinkedHashMap<>();
	}

	void addJob(Job job) {
		jobMap.put(job.getLocalJobID(), job);
		logger.debug("Added job " + job.getCanvasName() + " to job map");
	}

	void removeJob(String canvasId) {
		jobMap.remove(canvasId);
		logger.debug("Removed job " + canvasId + " from jobmap");
	}

	

	void enableRunJob(boolean enabled) {
		((RunJobHandler) RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(enabled);
		((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(!enabled);
	}

	/**
	 * execute job
	 * 
	 * @param job
	 *            - job to execute
	 */
	public void executeJob(final Job job) {
		enableRunJob(false);

		final DefaultGEFCanvas gefCanvas = CanvasUtils.getComponentCanvas();

		iEditorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

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
			WidgetUtility.errorMessage("Please open a graph to run.");
			return;
		}

		String clusterPassword = getClusterPassword(runConfigDialog);
		
		job.setUsername(runConfigDialog.getUsername());
		job.setPassword(clusterPassword);
		job.setHost(runConfigDialog.getHost());
		job.setRemoteMode(runConfigDialog.isRemoteMode());
		
		gefCanvas.disableRunningJobResource();
		
		
		
		
		if(job.isRemoteMode()){
			new Thread(new Runnable() {
				@Override
				public void run() {
					AbstractJobLauncher jobLauncher = new RemoteJobLauncher();
					jobLauncher.launchJob(xmlPath, parameterGrid.getParameterFile(),job,gefCanvas);
				}
				
			}).start();
		}else{
			new Thread(new Runnable() {

				@Override
				public void run() {
					AbstractJobLauncher jobLauncher = new LocalJobLauncher();
					jobLauncher.launchJob(xmlPath, parameterGrid.getParameterFile(),job,gefCanvas);
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

	JobLogger initJobLogger(DefaultGEFCanvas gefCanvas) {
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
		
		//killLocalJobProcess(jobToKill);
		jobToKill.setJobStatus("KILLED");
		
		if(jobToKill.isRemoteMode()){
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK );
			messageBox.setText("Kill job");
			messageBox.setMessage("Kill request accpeted.\nPlease make a note that - Run button will available only when current operation will complete");
			messageBox.open();
			((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
			
		}else{
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK );
			messageBox.setText("Kill job");
			messageBox.setMessage("Only the remote job can be killed");
			messageBox.open();			
		}
		
		if(jobToKill.getRemoteJobProcessID() != null){
			killRemoteProcess(jobToKill);
		}
		
		
		//removeJob(jobId);
	}

	private void killLocalJobProcess(Job jobToKill) {
		try {
			jobToKill.getLocalJobProcess().getErrorStream().close();
			jobToKill.getLocalJobProcess().getInputStream().close();
			jobToKill.getLocalJobProcess().getOutputStream().close();
		} catch (IOException e) {
			logger.debug("Unable to close process streams ",e);
		}
		jobToKill.getLocalJobProcess().destroy();
		logger.debug("Job " + jobToKill.getLocalJobID() + " killed");
		logger.debug("Killed remote job: " + jobToKill.getRemoteJobProcessID());
	}
	
	private void killRemoteProcess(Job job){
		String[] runCommand = new String[3];
		if (OSValidator.isWindows()) {
			logger.info("This is windows.");
			String[] command = {
					Messages.CMD,
					"/c",
					Messages.KILL_JOB + " " + Messages.HOST + "=\"" + job.getHost() + "\" " + Messages.USERNAME + "=\""
							+ job.getUsername() + "\" " + Messages.CLUSTER_PASSWORD + "=\"" + job.getPassword() + "\" " + 
							Messages.PROCESS_ID + "=\"" + job.getRemoteJobProcessID()};
			runCommand = command;

		} else if (OSValidator.isMac()) {
			logger.debug("This is Mac.");
			String[] command = {
					Messages.SHELL,
					"-c",
					Messages.KILL_JOB + " " + Messages.HOST + "=\"" + job.getHost() + "\" " + Messages.USERNAME + "=\""
							+ job.getUsername() + "\" " + Messages.CLUSTER_PASSWORD + "=\"" + job.getPassword() + "\"" +
							Messages.PROCESS_ID + "=\"" + job.getRemoteJobProcessID()};
			runCommand = command;
		} else if (OSValidator.isUnix()) {
			logger.debug("This is Unix or Linux");
		} else if (OSValidator.isSolaris()) {
			logger.debug("This is Solaris");
		} else {
			logger.debug("Your OS is not supported!!");
		}

		ProcessBuilder pb = new ProcessBuilder(runCommand);
		pb.directory(new File(job.getJobProjectDirectory()));
		try {
			Process process = pb.start();
			//logKillProcessLogsAsyncronously(process);
		} catch (IOException e) {
			logger.debug("Unable start to start kill job process ", e);
		}
		
	}
	
	
	private void logKillProcessLogsAsyncronously(final Process process) {

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
				//joblogger.logJobEndInfo();
				//joblogger.close();				
			}

		}).start();
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

	String getActiveCanvas() {
		return activeCanvas;
	}

	public void setJobStatus(String jobEditor, String status) {
		
		
	}
}
