package com.bitwise.app.graph.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.slf4j.Logger;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.common.util.OSValidator;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.graph.handler.RunJobHandler;
import com.bitwise.app.graph.handler.StopJobHandler;
import com.bitwise.app.joblogger.JobLogger;
import com.bitwise.app.logging.factory.LogFactory;

/**
 * 
 * This class provides functionality to launch remote job
 * 
 * @author Bitwise
 * 
 */
public class RemoteJobLauncher extends AbstractJobLauncher {
	private static Logger logger = LogFactory.INSTANCE.getLogger(RemoteJobLauncher.class);

	@Override
	public void launchJob(String xmlPath, String paramFile, Job job, DefaultGEFCanvas gefCanvas) {
		String projectName = xmlPath.split("/", 2)[0];
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		job.setJobProjectDirectory(project.getLocation().toOSString());

		String gradleCommand;

		job.setJobStatus(JobStatus.RUNNING);
		JobLogger joblogger;
		// ---------------------------- code to copy jar file
		gradleCommand = getLibararyScpCommand(job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, true, true);
		if (job.getJobStatus().equals(JobStatus.FAILED)) {
			releaseResources(job, gefCanvas, joblogger);
			return;
		}
		if (job.getJobStatus().equals(JobStatus.KILLED)) {
			return;
		}
		// ----------------------------- Code to copy job xml
		gradleCommand = getJobXMLScpCommand(xmlPath, job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (job.getJobStatus().equals(JobStatus.FAILED)) {
			releaseResources(job, gefCanvas, joblogger);
			return;
		}
		if (job.getJobStatus().equals(JobStatus.KILLED)) {
			return;
		}

		// ----------------------------- Code to copy parameter file
		gradleCommand = getParameterFileScpCommand(paramFile, job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (job.getJobStatus().equals(JobStatus.FAILED)) {
			releaseResources(job, gefCanvas, joblogger);
			return;
		}
		if (job.getJobStatus().equals(JobStatus.KILLED)) {
			return;
		}

		// ----------------------------- Execute job
		gradleCommand = getExecututeJobCommand(xmlPath, paramFile, job);
		job.setJobStatus(JobStatus.SSHEXEC);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (job.getJobStatus().equals(JobStatus.FAILED)) {
			releaseResources(job, gefCanvas, joblogger);
			return;
		}
		if (job.getJobStatus().equals(JobStatus.KILLED)) {
			((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
			((RunJobHandler) RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(false);
			return;
		}

		job.setJobStatus(JobStatus.SUCCESS);
		releaseResources(job, gefCanvas, joblogger);

	}

	private void releaseResources(Job job, DefaultGEFCanvas gefCanvas, JobLogger joblogger) {
		enableLockedResources(gefCanvas);
		refreshProject(gefCanvas);
		joblogger.logJobEndInfo();
		joblogger.close();
		JobManager.INSTANCE.removeJob(job.getLocalJobID());
		if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
			JobManager.INSTANCE.enableRunJob(true);
		}
	}

	private JobLogger executeCommand(Job job, IProject project, String gradleCommand, DefaultGEFCanvas gefCanvas,
			boolean logSystemInfo, boolean logJobStartInfo) {
		ProcessBuilder processBuilder = getProcess(project, gradleCommand);
		try {
			Process process = processBuilder.start();

			job.setLocalJobProcess(process);
			JobLogger joblogger = initJobLogger(gefCanvas, logSystemInfo, logJobStartInfo);

			JobManager.INSTANCE.addJob(job);
			logProcessLogsAsynchronously(joblogger, process, job, gefCanvas);
			return joblogger;
		} catch (IOException e) {
			logger.debug("Unable to execute the job", e);
		}
		return null;
	}

	private ProcessBuilder getProcess(IProject project, String gradleCommand) {
		String[] runCommand = new String[3];
		if (OSValidator.isWindows()) {
			String[] command = { Messages.CMD, "/c", gradleCommand };
			runCommand = command;

		} else if (OSValidator.isMac()) {
			String[] command = { Messages.SHELL, "-c", gradleCommand };
			runCommand = command;
		}

		ProcessBuilder processBuilder = new ProcessBuilder(runCommand);
		processBuilder.directory(new File(project.getLocation().toOSString()));
		processBuilder.redirectErrorStream(true);
		return processBuilder;

	}

	private String getLibararyScpCommand(Job job) {
		return GradleCommandConstants.GCMD_SCP_JAR + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword();
	}

	private String getJobXMLScpCommand(String xmlPath, Job job) {
		return GradleCommandConstants.GCMD_SCP_JOB_XML + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_JOB_XML + xmlPath.split("/", 2)[1];
	}

	private String getParameterFileScpCommand(String paramFile, Job job) {
		return GradleCommandConstants.GCMD_SCP_PARM_FILE + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_PARAM_FILE + paramFile;
	}

	private String getExecututeJobCommand(String xmlPath, String paramFile, Job job) {
		return GradleCommandConstants.GCMD_EXECUTE_REMOTE_JOB + GradleCommandConstants.GPARAM_HOST + job.getHost()
				+ GradleCommandConstants.GPARAM_USERNAME + job.getUsername() + GradleCommandConstants.GPARAM_PASSWORD
				+ job.getPassword() + GradleCommandConstants.GPARAM_PARAM_FILE + paramFile
				+ GradleCommandConstants.GPARAM_JOB_XML + xmlPath.split("/", 2)[1];
	}

	private void logProcessLogsAsynchronously(final JobLogger joblogger, final Process process, final Job job,
			DefaultGEFCanvas gefCanvas) {
		InputStream stream = process.getInputStream();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream));
			String line = null;

			while ((line = reader.readLine()) != null) {

				if (line.contains(Messages.CURRENT_JOB_ID)) {
					job.setRemoteJobProcessID((line.split("#")[1]).trim());
					((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(true);
				}

				if (line.contains(Messages.GRADLE_TASK_FAILED)) {
					job.setJobStatus(JobStatus.FAILED);
				}

				if (job.getRemoteJobProcessID() != null) {
					if (job.getJobStatus().equals(JobStatus.KILLED)) {
						((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
						((RunJobHandler) RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(false);
						JobManager.INSTANCE.killJob(job.getConsoleName(),gefCanvas);
						joblogger.logMessage("Killing job with job remote process id: " + job.getRemoteJobProcessID());
						break;
					}
				}

				joblogger.logMessage(line);
			}
		} catch (Exception e) {
			if (JobManager.INSTANCE.getRunningJobsMap().containsKey(job.getLocalJobID()))
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
		
		
		if (job.getRemoteJobProcessID() == null) {
			if (job.getJobStatus().equals(JobStatus.KILLED)) {
				joblogger.logMessage("JOB KILLED SUCCESSFULLY");
				releaseResources(job, gefCanvas, joblogger);
				JobManager.INSTANCE.removeJob(job.getLocalJobID());
			}
		}
	}

}
