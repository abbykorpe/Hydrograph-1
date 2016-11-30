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
import hydrograph.ui.graph.execution.tracking.replay.ViewExecutionHistoryUtility;
import hydrograph.ui.graph.execution.tracking.utils.TrackingDisplayUtils;
import hydrograph.ui.graph.handler.JobHandler;
import hydrograph.ui.graph.handler.StopJobHandler;
import hydrograph.ui.graph.utility.JobScpAndProcessUtility;
import hydrograph.ui.graph.utility.ViewDataUtils;
import hydrograph.ui.joblogger.JobLogger;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.slf4j.Logger;


/**
 * The Class DebugRemoteJobLauncher run the job on remote server in debug mode. 
 */
public class DebugRemoteJobLauncher extends AbstractJobLauncher{

	/** The logger. */
	private static Logger logger = LogFactory.INSTANCE.getLogger(DebugRemoteJobLauncher.class);
	
	/** The Constant BUILD_SUCCESSFUL. */
	private static final String BUILD_SUCCESSFUL = "BUILD SUCCESSFUL";
	
	/** The Constant JOB_KILLED_SUCCESSFULLY. */
	private static final String JOB_KILLED_SUCCESSFULLY = "JOB KILLED SUCCESSFULLY";
	
	/** The Constant JOB_COMPLETED_SUCCESSFULLY. */
	private static final String JOB_COMPLETED_SUCCESSFULLY = "JOB COMPLETED SUCCESSFULLY";
	private static final String JOB_FAILED="JOB FAILED";

	/**
	 * Run the job on remote server in debug mode.
	 * 
	 * @param xmlPath
	 * @param debugXmlPath
	 * @param paramFile
	 * @param job
	 * @param gefCanvas
	 * @param externalSchemaFiles list required to move external schema files on remote server
	 * @param subJobList list required to move sub job xml to remote server.
	 * 
	 * 
	 */
	@Override
	public void launchJobInDebug(String xmlPath, String debugXmlPath,
			 String paramFile,String userFunctionsPropertyFile, Job job,
			DefaultGEFCanvas gefCanvas,List<String> externalSchemaFiles,List<String> subJobList) {

		Session session=null;
		if(isExecutionTrackingOn()){
			HydrographServerConnection hydrographServerConnection = new HydrographServerConnection();
			session = hydrographServerConnection.connectToServer(job, job.getUniqueJobId(), 
					TrackingDisplayUtils.INSTANCE.getWebSocketRemoteUrl(job));
			if(hydrographServerConnection.getSelection() == 1){
				return;
			}
		}
		String projectName = xmlPath.split("/", 2)[0];
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		job.setJobProjectDirectory(project.getLocation().toOSString());

		String gradleCommand;

		job.setJobStatus(JobStatus.RUNNING);
		JobLogger joblogger;
		
		gradleCommand = JobScpAndProcessUtility.INSTANCE.getCreateDirectoryCommand(job,paramFile,xmlPath,projectName,externalSchemaFiles,subJobList);
		enableLockedResources(gefCanvas);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}
		
		/*
		 * Created list having relative and absolute # separated path, 
		 * the path is split in gradle script,Using absolute path we move the schema file to relative path directory of remote server   
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
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}
		}
		
		/*
		 * Created list having relative and absolute # separated path, 
		 * the path is split in gradle script using, absolute path we move the schema file to relative path (directory created using create directory command on remote server)   
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
				ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
				TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
				return;
			}
			if (JobStatus.KILLED.equals(job.getJobStatus())) {
				ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
				TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
				return;
			}
		}
		// ---------------------------- code to copy jar file
		gradleCommand = JobScpAndProcessUtility.INSTANCE.getLibararyScpCommand(job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, true, true);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}
		// ----------------------------- Code to copy job xml
		gradleCommand = JobScpAndProcessUtility.INSTANCE.getJobXMLScpCommand(xmlPath, debugXmlPath, job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}
		// ----------------------------- Code to copy jar files of project's lib folder 
		gradleCommand = JobScpAndProcessUtility.INSTANCE.getScpCommandForMovingLibFolderJarFiles(job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			return;
		}

		
		// ----------------------------- Code to copy user-functions property file from resource folder 
		gradleCommand = JobScpAndProcessUtility.INSTANCE.getScpCommandForMovingUserFunctionsPropertyFile(job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			return;
		}

		// ----------------------------- Code to copy parameter file
		gradleCommand = JobScpAndProcessUtility.INSTANCE.getParameterFileScpCommand(paramFile, job);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}

		// ----------------------------- Execute job
		gradleCommand = JobScpAndProcessUtility.INSTANCE.getExecututeJobCommand(xmlPath, debugXmlPath, paramFile,userFunctionsPropertyFile, job);
		job.setJobStatus(JobStatus.SSHEXEC);
		joblogger = executeCommand(job, project, gradleCommand, gefCanvas, false, false);
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			releaseResources(job, gefCanvas, joblogger);
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}
		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
			((JobHandler) RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(true);
			ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
			TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
			return;
		}

		if(job.getJobStatus().equalsIgnoreCase(JobStatus.RUNNING) || job.getJobStatus().equalsIgnoreCase(JobStatus.SSHEXEC)
				|| job.getJobStatus().equalsIgnoreCase(JobStatus.PENDING)){
			job.setJobStatus(JobStatus.SUCCESS);
		}
		
		releaseResources(job, gefCanvas, joblogger);
		ViewDataUtils.getInstance().addViewDataJobDetails(job.getConsoleName(), job);
		ViewExecutionHistoryUtility.INSTANCE.addTrackingJobs(job.getConsoleName(), job);
		TrackingDisplayUtils.INSTANCE.closeWebSocketConnection(session);
	}
	
	/**
	 * Release resources.
	 *
	 * @param job the job
	 * @param gefCanvas the gef canvas
	 * @param joblogger the joblogger
	 */
	private void releaseResources(Job job, DefaultGEFCanvas gefCanvas, JobLogger joblogger) {
		enableLockedResources(gefCanvas);
		refreshProject(gefCanvas);
		joblogger.logJobEndInfo(job.getUniqueJobId(), ViewExecutionHistoryUtility.getInstance().getLogPath());
		joblogger.close();
		JobManager.INSTANCE.removeJob(job.getRemoteJobProcessID());
		if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
			JobManager.INSTANCE.enableRunJob(true);
		}
	}

	/**
	 * Execute command.
	 *
	 * @param job the job
	 * @param project the project
	 * @param gradleCommand the gradle command
	 * @param gefCanvas the gef canvas
	 * @param logSystemInfo the log system info
	 * @param logJobStartInfo the log job start info
	 * @return the job logger
	 */
	private JobLogger executeCommand(Job job, IProject project, String gradleCommand, DefaultGEFCanvas gefCanvas,
			boolean logSystemInfo, boolean logJobStartInfo) {
		ProcessBuilder processBuilder = JobScpAndProcessUtility.INSTANCE.getProcess(project, gradleCommand);
		try {
			Process process = processBuilder.start();

			job.setLocalJobProcess(process);
			JobLogger joblogger = initJobLogger(gefCanvas, logSystemInfo, logJobStartInfo, job.getUniqueJobId());

			JobManager.INSTANCE.addJob(job);
			logProcessLogsAsynchronously(joblogger, process, job, gefCanvas);
			return joblogger;
		} catch (IOException e) {
			logger.debug("Unable to execute the job", e);
		}
		return null;
	}
			
	/**
	 * Log process logs asynchronously.
	 *
	 * @param joblogger the joblogger
	 * @param process the process
	 * @param job the job
	 * @param gefCanvas the gef canvas
	 */
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
						((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(true);
					} catch (NumberFormatException e) {
						logger.warn("Exception while setting Remote job processId- " + line.split("#")[1].trim(), e);
					}
				}

				if (line.contains(Messages.GRADLE_TASK_FAILED) || line.contains(JOB_FAILED)) {
					job.setJobStatus(JobStatus.FAILED);
				}

				if (JobStatus.KILLED.equals(job.getJobStatus())) {
					((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
					((JobHandler) RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(true);
					JobManager.INSTANCE.killJob(job.getConsoleName(), gefCanvas);
					joblogger.logMessage("Killing job with job remote process id: " + job.getRemoteJobProcessID());
					break;
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

		if (JobStatus.KILLED.equals(job.getJobStatus())) {
			joblogger.logMessage(JOB_KILLED_SUCCESSFULLY);
			releaseResources(job, gefCanvas, joblogger);
			JobManager.INSTANCE.removeJob(job.getLocalJobID());
		}

		if (!JobStatus.KILLED.equals(job.getJobStatus()) && !JobStatus.FAILED.equals(job.getJobStatus())
				&& !JobStatus.RUNNING.equals(job.getJobStatus())) {
			joblogger.logMessage(JOB_COMPLETED_SUCCESSFULLY);
			job.setJobStatus(JobStatus.SUCCESS);
			JobManager.INSTANCE.enableRunJob(true);
		}
			
		if (JobStatus.FAILED.equals(job.getJobStatus())) {
			joblogger.logMessage(JOB_FAILED);
		}
	}
	
	
	@Override
	public void launchJob(String xmlPath, String paramFile,String userFunctionsPropertyFile, Job job,
			DefaultGEFCanvas gefCanvas,List<String> externalSchemaFiles,List<String> subJobList) {
		
	}

	@Override
	public void killJob(Job jobToKill) {
		JobScpAndProcessUtility.INSTANCE.killRemoteJobProcess(jobToKill);
	}

}