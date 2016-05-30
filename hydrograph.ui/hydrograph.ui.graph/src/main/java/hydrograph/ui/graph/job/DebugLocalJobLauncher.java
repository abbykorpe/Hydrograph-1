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
import hydrograph.ui.graph.handler.RunJobHandler;
import hydrograph.ui.graph.handler.StopJobHandler;
import hydrograph.ui.graph.utility.JobScpAndProcessUtility;
import hydrograph.ui.joblogger.JobLogger;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.slf4j.Logger;


public class DebugLocalJobLauncher extends AbstractJobLauncher{

	private static Logger logger = LogFactory.INSTANCE.getLogger(DebugLocalJobLauncher.class);
	private static final String BUILD_SUCCESSFUL="BUILD SUCCESSFUL";
	private static final String BUILD_FAILED="BUILD FAILED";
	private static final String JOB_FAILED="JOB FAILED";
	private static final String JOB_COMPLETED_SUCCESSFULLY="JOB COMPLETED SUCCESSFULLY";
	
	
	@Override
	public void launchJobInDebug(String xmlPath, String debugXmlPath,String paramFile, Job job,	DefaultGEFCanvas gefCanvas,List<String> externalSchemaFiles,List<String> subJobList) {
		String projectName = xmlPath.split("/", 2)[0];
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		job.setJobProjectDirectory(project.getLocation().toOSString());

		String gradleCommand;

		job.setJobStatus(JobStatus.RUNNING);
		((RunJobHandler) RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(false);
		((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
		
		gradleCommand = getExecututeJobCommand(xmlPath, paramFile, debugXmlPath,job);
		executeCommand(job, project, gradleCommand, gefCanvas);

		job.setJobStatus(JobStatus.SUCCESS);

		if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
			JobManager.INSTANCE.enableRunJob(true);
		}
		enableLockedResources(gefCanvas);
		refreshProject(gefCanvas);
		
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
	
	private String getExecututeJobCommand(String xmlPath, String paramFile, String debugXmlPath, Job job) {
		return GradleCommandConstants.GCMD_EXECUTE_DEBUG_LOCAL_JOB + GradleCommandConstants.GPARAM_PARAM_FILE + paramFile + GradleCommandConstants.GPARAM_JOB_XML +  xmlPath.split("/", 2)[1] +
				GradleCommandConstants.GPARAM_LOCAL_JOB + GradleCommandConstants.GPARAM_JOB_DEBUG_XML + debugXmlPath.split("/", 2)[1] + GradleCommandConstants.GPARAM_JOB_BASE_PATH + job.getBasePath() + GradleCommandConstants.GPARAM_UNIQUE_JOB_ID +job.getUniqueJobId();
	}
	
	private void logProcessLogsAsynchronously(final JobLogger joblogger, final Process process, final Job job) {

		InputStream stream = process.getInputStream();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream));
			String line = null;

			while ((line = reader.readLine()) != null) {
				if(!line.contains(BUILD_SUCCESSFUL) && !line.contains(BUILD_FAILED)){
					joblogger.logMessage(line);
				}else{
					if(line.contains(BUILD_FAILED)){
						joblogger.logMessage(JOB_FAILED);
					}else{
						joblogger.logMessage(JOB_COMPLETED_SUCCESSFULLY);
						JobManager.INSTANCE.enableRunJob(true);
						//((DebugHandler) RunStopButtonCommunicator.RunDebugJob.getHandler()).setDebugJobEnabled(true);
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
	}

	@Override
	public void launchJob(String xmlPath, String paramFile, Job job,
			DefaultGEFCanvas gefCanvas,List<String> externalSchemaFiles,List<String> subJobList) {
		// TODO Auto-generated method stub
		
	}
}
