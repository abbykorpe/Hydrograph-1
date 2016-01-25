package com.bitwise.app.joblogger.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;

import com.bitwise.app.joblogger.utils.LogFactory;

/**
 * 
 * Class to create file logger
 * 
 * @author Bitwise
 *
 */
public class FileLogger extends AbstractJobLogger{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FileLogger.class);
	
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
			logger.debug("Exception while logging joblog to file " + e.getMessage());			
		}
	}
	
	
	/**
	 * 
	 * Initialize file logger stream
	 * 
	 */
	private void initLogFileStream() {
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		
		File file = new File(getFullJobName() + "_" +  dateFormat.format(date) + ".log") ;
		logger.debug("Created logfile- " + getFullJobName() + "_" +  dateFormat.format(date) + ".log");
		try {
			logFileStream = new BufferedWriter(new FileWriter(file));
			logger.debug("Created job log file stream");
		} catch (IOException e) {
			logger.debug("IOException while creating job log file stream" + e.getMessage());
		}
	}

	@Override
	public void close() {
		try {
			logFileStream.close();
			logger.debug("Closed job log file stream");
		} catch (IOException e) {
			logger.debug("IOException while closing job log file stream" + e.getMessage());
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
			logger.debug("Exception while logging joblog to file " + e.getMessage());			
		}		
	}
}
