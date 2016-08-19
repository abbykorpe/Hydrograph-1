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
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.execution.tracking.connection.HydrographServerConnection;
import hydrograph.ui.graph.handler.JobHandler;
import hydrograph.ui.graph.handler.StopJobHandler;
import hydrograph.ui.graph.utility.JobScpAndProcessUtility;
import hydrograph.ui.joblogger.JobLogger;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.slf4j.Logger;


/**
 * 
 * This class provides functionality to launch remote job
 * 
 * @author Bitwise
 * 
 */
public class RemoteJobLauncher extends AbstractJobLauncher {
	private static Logger logger = LogFactory.INSTANCE.getLogger(RemoteJobLauncher.class);
	private static final String BUILD_SUCCESSFUL = "BUILD SUCCESSFUL";
	private static final String JOB_KILLED_SUCCESSFULLY = "JOB KILLED SUCCESSFULLY";
	private static final String JOB_COMPLETED_SUCCESSFULLY = "JOB COMPLETED SUCCESSFULLY";
	private static final String JOB_FAILED="JOB FAILED";
	public  static boolean isRunning=false;

	@Override
	public void launchJob(String xmlPath, String paramFile, Job job, DefaultGEFCanvas gefCanvas,List<String> externalSchemaFiles,List<String> subJobList) {

		Session session=null;
		
		if(isExecutionTrackingOn()){
			HydrographServerConnection hydrographServerConnection = new HydrographServerConnection();
			session = hydrographServerConnection.connectToServer(job, job.getUniqueJobId(), 
					webSocketRemoteUrl);
		if(hydrographServerConnection.getSelection() == 1){
			closeWebSocketConnection(session);
			return;
		}
		} 

		String projectName = xmlPath.split("/", 2)[0];
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		job.setJobProjectDirectory(project.getLocation().toOSString());

		String gradleCommand;

		job.setJobStatus(JobStatus.RUNNING);
		JobLogger joblogger;

		gradleCommand = JobScpAndProcessUtility.INSTANCE.getCreateDirectoryCommand(job,paramFile,xmlPath,projectName,new ArrayList<String>(externalSchemaFiles),new ArrayList<>(subJobList));

		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			closeWebSocketConnection(session);
			return;
		}


		/*
		 * Created list having relative and absolute # separated path, 
		 * the path is split in gradle script,Using absolute path we move the subjob to remote server   
		 */
		if(!subJobList.isEmpty()){
			List<String> subJobFullPath = new ArrayList<>();
			for (String subJobFile : subJobList) {
				subJobFullPath.add(subJobFile+"#"+JobManager.getAbsolutePathFromFile(new Path(subJobFile)).replace(Constants.JOB_EXTENSION, Constants.XML_EXTENSION));
			}
			gradleCommand = JobScpAndProcessUtility.INSTANCE.getSubjobScpCommand(subJobFullPath,job);

			joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
			if (JobStatus.FAILED.equals(job.getJobStatus())) {
				releaseResources(job, gefCanvas, joblogger);
				closeWebSocketConnection(session);
				return;
			}
			if (JobStatus.KILLED.equals(job.getJobStatus())) {
				closeWebSocketConnection(session);
				return;
			}
		}


		/*
		 * Created list having relative and absolute # separated path, 
		 * the path is split in gradle script, Using absolute path we move external schema file to remote server   
		 */
		if(!externalSchemaFiles.isEmpty()){
			List<String> schemaFilesFullPath = new ArrayList<>();
			for (String schemaFile : externalSchemaFiles) {
				schemaFilesFullPath.add(schemaFile+"#"+JobManager.getAbsolutePathFromFile(new Path(schemaFile)));
			}
			gradleCommand = JobScpAndProcessUtility.INSTANCE.getSchemaScpCommand(schemaFilesFullPath,job);

			joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
			if (JobStatus.FAILED.equals(job.getJobStatus())) {
				releaseResources(job, gefCanvas, joblogger);
				closeWebSocketConnection(session);
				return;
			}
			if (JobStatus.KILLED.equals(job.getJobStatus())) {
				closeWebSocketConnection(session);
				return;
			}
		}

		gradleCommand = JobScpAndProcessUtility.INSTANCE.getLibararyScpCommand(job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, true, true);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			closeWebSocketConnection(session);
			return;
		}
		// ----------------------------- Code to copy job xml
		gradleCommand = JobScpAndProcessUtility.INSTANCE.getJobXMLScpCommand(xmlPath,"", job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			closeWebSocketConnection(session);
			return;
		}

		// ----------------------------- Code to copy parameter file
		gradleCommand = JobScpAndProcessUtility.INSTANCE.getParameterFileScpCommand(paramFile, job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			closeWebSocketConnection(session);
			return;
		}

		// ----------------------------- Execute job
		gradleCommand = JobScpAndProcessUtility.INSTANCE.getExecututeJobCommand(xmlPath,"", paramFile, job);
		job.setJobStatus(JobStatus.SSHEXEC);
		isRunning=true;
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			isRunning=false;
			closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
			((JobHandler) RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(false);
			isRunning=false;
			closeWebSocketConnection(session);
			return;
		}

		job.setJobStatus(JobStatus.SUCCESS);
		releaseResources(job, gefCanvas, joblogger);
		closeWebSocketConnection(session);
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
		ProcessBuilder processBuilder = JobScpAndProcessUtility.INSTANCE.getProcess(project, gradleCommand);
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


	private void logProcessLogsAsynchronously(final JobLogger joblogger, final Process process, final Job job,
			DefaultGEFCanvas gefCanvas) {
		InputStream stream = process.getInputStream();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream));
			String line = null;

			while ((line = reader.readLine()) != null) {

				if (line.contains(Messages.CURRENT_JOB_ID)) {
					try {
						Long.parseLong((line.split("#")[1]).trim());
						job.setRemoteJobProcessID((line.split("#")[1]).trim());
						((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(true);

					} catch (NumberFormatException e) {
						logger.warn("Exception while setting Remote job processId- " +line.split("#")[1].trim(), e);
					}

				}

				if (line.contains(Messages.GRADLE_TASK_FAILED)) {
					job.setJobStatus(JobStatus.FAILED);
				}else if(line.contains(JOB_FAILED)){
					job.setJobStatus(JobStatus.FAILED);
				}

				if (job.getRemoteJobProcessID() != null) {
					if (JobStatus.KILLED.equals(job.getJobStatus())) {
						((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
						((JobHandler) RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(true);
						JobManager.INSTANCE.killJob(job.getConsoleName(), gefCanvas);
						joblogger.logMessage("Killing job with job remote process id: " + job.getRemoteJobProcessID());
						break;
					}
				}

				if (!line.contains(BUILD_SUCCESSFUL)) {
					joblogger.logMessage(line);
				}

			}
		} catch (IOException e) {
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
			if (JobStatus.KILLED.equals(job.getJobStatus())) {
				joblogger.logMessage(JOB_KILLED_SUCCESSFULLY);
				releaseResources(job, gefCanvas, joblogger);
				JobManager.INSTANCE.removeJob(job.getLocalJobID());
			}
		}

		if (job.getRemoteJobProcessID() != null) {
			if (!JobStatus.KILLED.equals(job.getJobStatus()) && !JobStatus.FAILED.equals(job.getJobStatus())
					&& !JobStatus.RUNNING.equals(job.getJobStatus())) {
				joblogger.logMessage(JOB_COMPLETED_SUCCESSFULLY);
			}
		}

		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			joblogger.logMessage(JOB_FAILED);
		}
		
	}

	@Override
	public void launchJobInDebug(String xmlPath, String debugXmlPath,
			String paramFile, Job job,
			DefaultGEFCanvas gefCanvas,List<String> externalSchemaFiles,List<String> subJobList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void killJob(Job jobToKill) {
		/*if (isRunning) {
			Session session = null;
			HydrographServerConnection hydrographServerConnection = new HydrographServerConnection();
			try {
				String remoteUrl = TrackingDisplayUtils.INSTANCE.getWebSocketRemoteUrl();
				session = hydrographServerConnection.connectToKillJob(jobToKill.getUniqueJobId(), remoteUrl);
				Thread.sleep(8000);
			} catch (Throwable e1) {
				((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
				MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText(Messages.KILL_JOB_MESSAGEBOX_TITLE);
				messageBox.setMessage(Messages.KILL_JOB_MESSAGE);
				if (messageBox.open() == SWT.YES) {
					jobToKill.setJobStatus(JobStatus.KILLED);
				}
			} finally {
				if (session != null) {
					try {
						session.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			JobScpAndProcessUtility.INSTANCE.killJobProcess(jobToKill);
		}*/
		JobScpAndProcessUtility.INSTANCE.killRemoteJobProcess(jobToKill);
	}

	/**
	 * 
	 * @param session
	 */
	private void closeWebSocketConnection(Session session){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
		}
		if (session != null  && session.isOpen()) {
			try {
				CloseReason closeReason = new CloseReason(CloseCodes.NORMAL_CLOSURE,"Closed");
				session.close(closeReason);
				logger.info("Session closed");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}
}
