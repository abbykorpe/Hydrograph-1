package com.bitwise.app.graph.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.common.util.OSValidator;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.graph.utility.CanvasUtils;
import com.bitwise.app.joblogger.JobLogger;
import com.bitwise.app.logging.factory.LogFactory;

public class RemoteJobLauncher extends AbstractJobLauncher{
	private static Logger logger = LogFactory.INSTANCE.getLogger(RemoteJobLauncher.class);
	
	@Override
	public void launchJob(String xmlPath, String paramFile, Job job,DefaultGEFCanvas gefCanvas) {
		String projectName = xmlPath.split("/", 2)[0];
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);		
		job.setJobProjectDirectory(project.getLocation().toOSString());
		
		String gradleCommand;
		
		job.setJobStatus("RUNNING");
		//---------------------------- code to copy jar file
		gradleCommand = getLibararyScpCommand(job);
		executeCommand(job, project, gradleCommand,gefCanvas);
		if(job.getJobStatus().equals("FAIELD") || job.getJobStatus().equals("KILLED")){
			enableLockedResources(gefCanvas);
			refreshProject(gefCanvas);
			if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
				JobManager.INSTANCE.enableRunJob(true);
			}
			JobManager.INSTANCE.removeJob(job.getCanvasName());
			return;
		}
		//----------------------------- Code to copy job xml
		gradleCommand = getJobXMLScpCommand(xmlPath, job);
		executeCommand(job, project, gradleCommand,gefCanvas);
		if(job.getJobStatus().equals("FAIELD") || job.getJobStatus().equals("KILLED")){
			enableLockedResources(gefCanvas);
			refreshProject(gefCanvas);
			if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
				JobManager.INSTANCE.enableRunJob(true);
			}
			JobManager.INSTANCE.removeJob(job.getCanvasName());
			return;
		}
		
		//----------------------------- Code to copy parameter file
		gradleCommand = getParameterFileScpCommand(paramFile, job);
		executeCommand(job, project, gradleCommand,gefCanvas);
		if(job.getJobStatus().equals("FAIELD") || job.getJobStatus().equals("KILLED")){
			enableLockedResources(gefCanvas);
			refreshProject(gefCanvas);
			if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
				JobManager.INSTANCE.enableRunJob(true);
			}
			JobManager.INSTANCE.removeJob(job.getCanvasName());
			return;
		}
		
		//----------------------------- Execute job
		gradleCommand = getExecututeJobCommand(xmlPath, paramFile, job);
		executeCommand(job, project, gradleCommand,gefCanvas);
		if(job.getJobStatus().equals("FAIELD") || job.getJobStatus().equals("KILLED")){
			enableLockedResources(gefCanvas);
			refreshProject(gefCanvas);
			if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
				JobManager.INSTANCE.enableRunJob(true);
			}
			JobManager.INSTANCE.removeJob(job.getCanvasName());
			return;
		}
		
		job.setJobStatus("SUCCESS");
		
		if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
			JobManager.INSTANCE.enableRunJob(true);
		}
		enableLockedResources(gefCanvas);
		refreshProject(gefCanvas);
		JobManager.INSTANCE.removeJob(job.getCanvasName());
	}

	private void executeCommand(Job job, IProject project, String gradleCommand,DefaultGEFCanvas gefCanvas) {
		ProcessBuilder processBuilder = getProcess(project, gradleCommand);
		try {
			Process process = processBuilder.start();
			
			job.setLocalJobProcess(process);
			JobLogger joblogger = JobManager.INSTANCE.initJobLogger(gefCanvas);
			
			JobManager.INSTANCE.addJob(job);
			logProcessLogsAsyncronously(joblogger, process, job);
			
		} catch (IOException e) {
			logger.debug("Unable to execute the job" , e);
		}
	}
	
	private ProcessBuilder getProcess(IProject project,String gradleCommand){
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
		return "gradle scpJarFiles -Phost=" + job.getHost() + " -Pusername=" + job.getUsername()
				+ " -Ppassword=" + job.getPassword();
	}

	private String getJobXMLScpCommand(String xmlPath, Job job) {
		String gradleCommand;
		gradleCommand = "gradle scpJobXML -Phost=" + job.getHost() + " -Pusername=" + job.getUsername()
				+ " -Ppassword=" + job.getPassword() + " -PjobXML=" + xmlPath.split("/", 2)[1];
		return gradleCommand;
	}

	private String getParameterFileScpCommand(String paramFile, Job job) {
		String gradleCommand;
		gradleCommand = "gradle scpParameterFile -Phost=" + job.getHost() + " -Pusername=" + job.getUsername()
				+ " -Ppassword=" + job.getPassword() + " -PparameterFile=" + paramFile;
		return gradleCommand;
	}

	private String getExecututeJobCommand(String xmlPath, String paramFile, Job job) {
		String gradleCommand = "gradle executeRemoteJob -Phost=" + job.getHost() + " -Pusername=" + job.getUsername()
				+ " -Ppassword=" + job.getPassword() +  " -PparameterFile=" + paramFile  + " -PjobXML=" + xmlPath.split("/", 2)[1];
		return gradleCommand;
	}
	
	private void logProcessLogsAsyncronously(final JobLogger joblogger,
			final Process process, final Job job) {

		//new Thread(new Runnable() {
			InputStream stream = process.getInputStream();

			//public void run() {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new InputStreamReader(stream));
					String line = null;
					
					while ((line = reader.readLine()) != null) {

						if (line.contains("Current JobID#")) {
							job.setRemoteJobProcessID((line.split("#")[1]).trim());
						}
						
						if (line.contains("#Gradle failed to execute task#")) {
							job.setJobStatus("Failed");
						}
						
						joblogger.logMessage(line);
					}
				} catch (Exception e) {
					if(JobManager.INSTANCE.jobMap.containsKey(job.getLocalJobID()))
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

				/*if (job.getCanvasName().equals(JobManager.INSTANCE.getActiveCanvas())) {
					JobManager.INSTANCE.enableRunJob(true);
				}*/

			//}

		//}).start();
	}

	
	
	
}
