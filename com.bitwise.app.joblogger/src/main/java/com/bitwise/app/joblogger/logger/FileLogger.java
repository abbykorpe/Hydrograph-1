package com.bitwise.app.joblogger.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;
import org.slf4j.Logger;


import com.bitwise.app.joblogger.Activator;
import com.bitwise.app.logging.factory.LogFactory;

/**
 * 
 * Class to create file logger
 * 
 * @author Bitwise
 *
 */
public class FileLogger extends AbstractJobLogger{
	private static final String YYYY_M_MDD_H_HMMSS = "yyyyMMdd_HHmmss";
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FileLogger.class);
	public final static String LOGGER_FOLDER_PATH = Platform.getInstallLocation().getURL().getPath()
			+ "config\\logger\\job_logs\\";
	public final String JOB_LOGS_ERROR = "Job_Logs will not be created in your workspace. Delete or Move Job_Logs to another location for smooth creation of logs.";
	
	private BufferedWriter logFileStream;
	
	/**
	 * 
	 * Create file logger
	 * 
	 * @param projectName - name of active project
	 * @param jobName - name of current job
	 */
	public FileLogger(String projectName, String jobName) {
		super(projectName, jobName);
		
		initLogFileStream();
		logger.debug("Initialized file logger");
	}

	
	@Override
	public void log(String message) {
		try{
			logFileStream.write(getLogStamp() + message);
			logFileStream.newLine();
			logFileStream.flush();
			
			logger.debug("Written log to file with timestamp -  log message- {}",message);
		}catch (Exception e) {
			logger.debug("Exception while logging joblog to file ", e);			
		}
	}
	
	
	/**
	 * 
	 * Initialize file logger stream
	 * 
	 */
	private void initLogFileStream() {
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_M_MDD_H_HMMSS) ;
		
	   
		logger.debug("Created logfile- " + getFullJobName() + "_" +  dateFormat.format(date) + ".log");
		
		try {
			File job_logs_folder = new File(LOGGER_FOLDER_PATH);
			if (job_logs_folder.exists()) {
				if (job_logs_folder.isDirectory()) {
					File file = new File(LOGGER_FOLDER_PATH + getFullJobName() + "_" + dateFormat.format(date) + ".log");
					logFileStream = new BufferedWriter(new FileWriter(file, true));
				} else {
					Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, JOB_LOGS_ERROR);
					StatusManager.getManager().handle(status, StatusManager.BLOCK);
				}
			} else {
				job_logs_folder.mkdir();
				File file = new File(LOGGER_FOLDER_PATH + getFullJobName() + "_" + dateFormat.format(date) + ".log");
				logFileStream = new BufferedWriter(new FileWriter(file, true));
			}
			logger.debug("Created job log file stream");
		} catch (IOException e) {
			logger.debug("IOException while creating job log file stream", e);
		}
	}

	@Override
	public void close() {
		try {
			if (logFileStream != null) {
				logFileStream.close();
			}
			logger.debug("Closed job log file stream");
		} catch (IOException e) {
			logger.debug("IOException while closing job log file stream", e);
		}
	}

	@Override
	public void logWithNoTimeStamp(String message) {
		try{
			logFileStream.write(message);
			logFileStream.newLine();
			logFileStream.flush();
			logger.debug("Written log to file with no timestamp -  log message- {}",message);
		}catch (Exception e) {
			logger.debug("Exception while logging joblog to file ", e);			
		}		
	}
}
