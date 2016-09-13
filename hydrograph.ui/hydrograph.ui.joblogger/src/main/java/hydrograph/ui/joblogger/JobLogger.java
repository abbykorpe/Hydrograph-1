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

 
package hydrograph.ui.joblogger;


import hydrograph.ui.joblogger.logger.AbstractJobLogger;
import hydrograph.ui.joblogger.logger.ConsoleLogger;
import hydrograph.ui.joblogger.logger.FileLogger;
import hydrograph.ui.joblogger.utils.JobLoggerUtils;
import hydrograph.ui.logging.factory.LogFactory;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;



/**
 * The Class JobLogger.
 * <p>
 * Manages Job logging. Job logs will be displayed in console as well as written in the log file. For each job run, it
 * will log system information of the client machine.
 * 
 * @author Bitwise
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
			if(StringUtils.isNotBlank(message)){
				message = StringUtils.trim(message);
				jobLogger.log(message);
			}
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
