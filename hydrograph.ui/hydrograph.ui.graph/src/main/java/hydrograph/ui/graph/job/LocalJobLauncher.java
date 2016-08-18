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
import hydrograph.ui.graph.execution.tracking.connection.HydrographServerConnection;
import hydrograph.ui.graph.handler.JobHandler;
import hydrograph.ui.graph.utility.JobScpAndProcessUtility;
import hydrograph.ui.joblogger.JobLogger;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.slf4j.Logger;


/**
 * This class provides functionality to launch local job
 * 
 * @author Bitwise
 * 
 */
public class LocalJobLauncher extends AbstractJobLauncher {

	private static Logger logger = LogFactory.INSTANCE.getLogger(LocalJobLauncher.class);
	private static final String BUILD_SUCCESSFUL="BUILD SUCCESSFUL";
	private static final String BUILD_FAILED="BUILD FAILED";
	private static final String JOB_COMPLETED_SUCCESSFULLY="JOB COMPLETED SUCCESSFULLY";
	private static final String JOB_KILLED_SUCCESSFULLY = "JOB KILLED SUCCESSFULLY";
	private static final String JOB_FAILED="JOB FAILED";

	@Override
	public void launchJob(String xmlPath, String paramFile, Job job, DefaultGEFCanvas gefCanvas,List<String> externalSchemaFiles,List<String> subJobList) {
		Session session=null;

		if(isExecutionTracking()){
			HydrographServerConnection hydrographServerConnection = new HydrographServerConnection();
			session = hydrographServerConnection.connectToLocalServer(job, job.getUniqueJobId(), 
					webSocketLocalHost);
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
		((JobHandler) RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(false);
		gradleCommand = getExecututeJobCommand(xmlPath, paramFile, job);
		executeCommand(job, project, gradleCommand, gefCanvas);

		job.setJobStatus(JobStatus.SUCCESS);

		if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
			JobManager.INSTANCE.enableRunJob(true);
		}
		enableLockedResources(gefCanvas);
		refreshProject(gefCanvas);
		JobManager.INSTANCE.removeJob(job.getCanvasName());
		closeWebSocketConnection(session);
	}

	private void executeCommand(Job job, IProject project, String gradleCommand, DefaultGEFCanvas gefCanvas) {
		ProcessBuilder processBuilder = JobScpAndProcessUtility.INSTANCE.getProcess(project, gradleCommand);
		try {
			Process process = processBuilder.start();
			job.setLocalJobProcess(process);
			JobLogger joblogger = initJobLogger(gefCanvas,true,true);

			JobManager.INSTANCE.addJob(job);
			logProcessLogsAsynchronously(joblogger, process, job);

		} catch (IOException e) {
			logger.debug("Unable to execute the job", e);
		}
	}

	private String getExecututeJobCommand(String xmlPath, String paramFile, Job job) {
		
		String exeCommond = GradleCommandConstants.GCMD_EXECUTE_LOCAL_JOB + GradleCommandConstants.DAEMON_ENABLE + GradleCommandConstants.GPARAM_PARAM_FILE + "\""+ paramFile+"\""+ GradleCommandConstants.GPARAM_JOB_XML +   "\""+ xmlPath.split("/", 2)[1] +"\"" +
		GradleCommandConstants.GPARAM_LOCAL_JOB + GradleCommandConstants.GPARAM_UNIQUE_JOB_ID + job.getUniqueJobId() + 
		GradleCommandConstants.GPARAM_IS_EXECUTION_TRACKING + job.isExecutionTrack();
		logger.info("Gradle Command: {}", exeCommond);
		
		return exeCommond;
	}

	private void logProcessLogsAsynchronously(final JobLogger joblogger, final Process process, final Job job) {

		InputStream stream = process.getInputStream();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream));
			String line = null;

			while ((line = reader.readLine()) != null) {
				if (line.contains("Gradle build daemon has been stopped.")) {
					job.setJobStatus(JobStatus.KILLED);
					joblogger.logMessage(JOB_KILLED_SUCCESSFULLY);
					break;
				}else if(line.contains(JOB_FAILED)){
					job.setJobStatus(JobStatus.FAILED);
				}
				
				if(!line.contains(BUILD_SUCCESSFUL) && !line.contains(BUILD_FAILED)){
					joblogger.logMessage(line);
				}else{
					if (job.getJobStatus().equalsIgnoreCase(JobStatus.KILLED)){
							joblogger.logMessage(JOB_KILLED_SUCCESSFULLY);
						} else if(job.getJobStatus().equalsIgnoreCase(JobStatus.FAILED)){
							joblogger.logMessage(JOB_FAILED);
						}else{
							joblogger.logMessage(JOB_COMPLETED_SUCCESSFULLY);
						}
				}
			}
		} catch (IOException e) {
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
		joblogger.logJobEndInfo();
		joblogger.close();
		JobManager.INSTANCE.removeJob(job.getLocalJobID());
	}

	@Override
	public void launchJobInDebug(String xmlPath, String debugXmlPath,
			 String paramFile, Job job,
			DefaultGEFCanvas gefCanvas,List<String> externalSchemaFiles,List<String> subJobList) {
		
	}

	@Override
	public void killJob(Job jobToKill) {
		JobScpAndProcessUtility.INSTANCE.killLocalJobProcess(jobToKill);
	}
	
	private void closeWebSocketConnection(final Session session ){
		
		final Timer timer = new Timer();
		
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (session != null  && session.isOpen()) {
					try {
						CloseReason closeReason = new CloseReason(CloseCodes.NORMAL_CLOSURE,"Closed");
						session.close(closeReason);
						logger.info("Session closed");
					} catch (IOException e) {
						e.printStackTrace();
					}
					timer.cancel();
				}
			}
		};
		timer.schedule(timerTask, 10000);
		

	}
}
