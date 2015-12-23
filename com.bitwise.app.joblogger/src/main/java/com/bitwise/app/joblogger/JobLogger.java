package com.bitwise.app.joblogger;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.joblogger.logger.AbstractJobLogger;
import com.bitwise.app.joblogger.logger.ConsoleLogger;
import com.bitwise.app.joblogger.logger.FileLogger;
import com.bitwise.app.joblogger.utils.JobLoggerUtils;

/**
 * 
 * The job logger/loggers manager class
 * 
 * @author Bitwise
 *
 */
public class JobLogger {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(JobLogger.class);
	
	private List<AbstractJobLogger> loggers;
		
	private String projectName;
	private String jobName;
	
	public JobLogger(String projectName, String jobName ){
		this.projectName = projectName;
		this.jobName = jobName;
		registerLoggers();
		logger.debug("Registered all loggers");
	}
	
	
	private void registerLoggers(){
		loggers = new ArrayList<>();
		loggers.add(new FileLogger(projectName, jobName));
		logger.debug("Registred file logger");
		loggers.add(new ConsoleLogger(projectName, jobName));
		logger.debug("Registered Console logger");
	}
	
	
	/**
	 * 
	 * log system information
	 * 
	 */
	public void logSystemInformation(){
		for(AbstractJobLogger jobLogger: loggers){
			jobLogger.logWithNoTimeStamp("System Properties: ");
			
			logSystemProperties(jobLogger);			
			logRuntimeInformation(jobLogger);
			
			jobLogger.logWithNoTimeStamp("--------------------------------------------\n");
			
			logger.debug("Logged System information on {}", jobLogger.getClass().getName());
		}
		
		
	}


	/**
	 * 
	 * Logs runtime properties
	 * 
	 * @param jobLogger
	 */
	private void logRuntimeInformation(AbstractJobLogger jobLogger) {
		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory();
		jobLogger.logWithNoTimeStamp("Max Memory: " + Long.toString(maxMemory / 1024));
		long allocatedMemory = runtime.totalMemory();
		jobLogger.logWithNoTimeStamp("Allocated Memory:  " +
				Long.toString(allocatedMemory / 1024));
		long freeMemory = runtime.freeMemory();
		jobLogger.logWithNoTimeStamp("Free Memory: " + Long.toString(freeMemory / 1024));
		jobLogger.logWithNoTimeStamp("Total free memory: " + Long
				.toString((freeMemory + (maxMemory - allocatedMemory)) / 1024));
		long used = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		jobLogger.logWithNoTimeStamp("Used Memory : "  + Long.toString(used));
	}

	
	/**
	 * Log system properties
	 * 
	 * @param jobLogger
	 */
	private void logSystemProperties(AbstractJobLogger jobLogger) {
		jobLogger.logWithNoTimeStamp("Operating System : " +  System.getProperty("os.name"));
		jobLogger.logWithNoTimeStamp("JVM : " + System.getProperty("java.vm.name"));
		jobLogger.logWithNoTimeStamp("Java specification version : " +
				System.getProperty("java.specification.version"));
		jobLogger.logWithNoTimeStamp("Java Version : " + System.getProperty("java.version"));
		jobLogger.logWithNoTimeStamp("Osgi OS : " +  System.getProperty("osgi.os"));
		jobLogger.logWithNoTimeStamp("Operating System Version : " +
				System.getProperty("os.version"));
		jobLogger.logWithNoTimeStamp("Operating System Architecture : " +
				System.getProperty("os.arch"));
	}
		
	/**
	 * 
	 * log message
	 * 
	 * @param message
	 */
	public void logMessage(String message){
		for(AbstractJobLogger jobLogger: loggers){
			jobLogger.log(message);
			logger.debug("Logged message {} on {}", message, jobLogger.getClass().getName());
		}
	}
	
	
	/**
	 * 
	 * Log job start information
	 * 
	 */
	public void logJobStartInfo(){
		for(AbstractJobLogger jobLogger: loggers){
			jobLogger.logWithNoTimeStamp("====================================================================");
			jobLogger.logWithNoTimeStamp("Job Start Timestamp: " + JobLoggerUtils.getTimeStamp());
			jobLogger.logWithNoTimeStamp("Job name: " + jobLogger.getFullJobName());
			jobLogger.logWithNoTimeStamp("====================================================================");
			
			logger.debug("Logged job start info on {}",jobLogger.getClass().getName());
		}		
	}
	
	/**
	 * 
	 * Log job end information
	 * 
	 */
	public void logJobEndInfo(){
		for(AbstractJobLogger jobLogger: loggers){
			jobLogger.logWithNoTimeStamp("====================================================================");
			jobLogger.logWithNoTimeStamp("Job End Timestamp: " + JobLoggerUtils.getTimeStamp());
			jobLogger.logWithNoTimeStamp("Job name: " + jobLogger.getFullJobName());
			jobLogger.logWithNoTimeStamp("====================================================================");
			jobLogger.logWithNoTimeStamp("\n\n");
			
			logger.debug("Logged job end info on {}",jobLogger.getClass().getName());
		}
	}
	
	
	/**
	 * Release used resources
	 * 
	 */
	public void close(){
		for(AbstractJobLogger jobLogger: loggers){
			jobLogger.close();
			logger.debug("Closed logger - {}",jobLogger.getClass().getName());
		}
	}
}
