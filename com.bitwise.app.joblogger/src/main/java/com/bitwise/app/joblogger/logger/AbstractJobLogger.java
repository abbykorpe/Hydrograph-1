package com.bitwise.app.joblogger.logger;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.joblogger.utils.JobLoggerUtils;

/**
 * 
 * Abstract job logger. Each job logger must extend this class
 * 
 * @author Bitwise
 *
 */
public abstract class AbstractJobLogger {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(AbstractJobLogger.class);
	
	private String fullJobName;
	
	/**
	 * 
	 * @param projectName - name of active project
	 * @param jobName - name of current job
	 */
	public AbstractJobLogger(String projectName,String jobName){
		fullJobName = projectName + "." + jobName;
		
		logger.debug("Build fullJobName - " + fullJobName);
	}
	
	/**
	 *  
	 * @return fullJobName
	 */
	public String getFullJobName(){
		return fullJobName;
	}
	
	/**
	 * 
	 * get log stamp as "Timestamp [FullJobName] -"
	 * 
	 * @return
	 */
	protected String getLogStamp(){
		String logStamp;
		logStamp=JobLoggerUtils.getTimeStamp().toString() + " [" + fullJobName + "] - "; 
		return logStamp;
	}
	
	/**
	 * 
	 * log message
	 * 
	 * @param message - message to log
	 */
	public abstract void log(String message);
	
	/**
	 * 
	 * log message without logstamp
	 * 
	 * @param message - message to log
	 */
	public abstract void logWithNoTimeStamp(String message);
	
	/**
	 * Release resources used in logger. e.g. - Close filestream/constole stream etc..
	 * 
	 */
	public abstract void close();
}
