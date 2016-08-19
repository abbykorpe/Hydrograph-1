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
package hydrograph.server.execution.tracking.client.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import hydrograph.server.execution.tracking.server.status.datastructures.ComponentStatus;
import hydrograph.server.execution.tracking.server.status.datastructures.ExecutionStatus;

/**
 * Use to manage logging
 * The Class ExecutionTrackingFileLogger.
 */
public class ExecutionTrackingFileLogger {
	
	/** The Constant logger. */
	private final static Logger logger = Logger.getLogger(ExecutionTrackingFileLogger.class);
	
	/** The Constant ExecutionTrackingLogFileExtention. */
	private static final String ExecutionTrackingLogFileExtention = ".log";
	
	/** The Constant INSTANCE. */
	public static final ExecutionTrackingFileLogger INSTANCE = new ExecutionTrackingFileLogger();
	
	/** The job tracking log directory. */
	private String jobTrackingLogDirectory;
		
	/** The execution tracking loggers. */
	private Map<String,BufferedWriter> executionTrackingLoggers;
	
	/**
	 * Instantiates a new execution tracking file logger.
	 */
	private ExecutionTrackingFileLogger(){
		jobTrackingLogDirectory = "/tmp/JobTrackingLog/";
				
		executionTrackingLoggers = new HashMap();
		
		createJobTrackingLogDirectory();
	}

	/**
	 * Creates the job tracking log directory.
	 */
	private void createJobTrackingLogDirectory() {
		File file = new File(jobTrackingLogDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
	 * Log.
	 *
	 * @param uniqJobId the uniq job id
	 * @param executionStatus the execution status
	 */
	public void log(String uniqJobId,ExecutionStatus executionStatus){
		String executionStatusString = getExecutionStatusInString(executionStatus);
		if(StringUtils.isBlank(executionStatusString)){
			return;
		}
		
		BufferedWriter executionTrackingLogger = getExecutionStatusLogger(uniqJobId);
		
		try {
			executionTrackingLogger.write(executionStatusString);
			executionTrackingLogger.flush();
		} catch (IOException e) {
			logger.info("Unable to write to execution tracking log file" + e.getMessage());
		}
	}

	/**
	 * Gets the execution status logger.
	 *
	 * @param uniqJobId the uniq job id
	 * @return the execution status logger
	 */
	private BufferedWriter getExecutionStatusLogger(String uniqJobId) {
		BufferedWriter bufferedWriter = executionTrackingLoggers.get(uniqJobId);		
		if(bufferedWriter==null){
			try{
				 FileWriter fileWriter = new FileWriter(jobTrackingLogDirectory + uniqJobId + ExecutionTrackingLogFileExtention, true);
				 bufferedWriter = new BufferedWriter(fileWriter);
				 executionTrackingLoggers.put(uniqJobId, bufferedWriter);
			}catch (Exception e) {
				logger.info("Unable to create Execution Tracking Logger " + e.getMessage());
			}
		}
		return bufferedWriter;
	}

	/**
	 * Gets the execution status in string.
	 *
	 * @param executionStatus the execution status
	 * @return the execution status in string
	 */
	private String getExecutionStatusInString(ExecutionStatus executionStatus) {
		StringBuilder stringBuilder = new StringBuilder();
		if(executionStatus==null){
			return null;
		}
		stringBuilder.append("Job ID " + executionStatus.getJobId() + "\n");
		stringBuilder.append("Job Type: " + executionStatus.getType() + "\n");
		stringBuilder.append("Job Status: " + executionStatus.getJobStatus() + "\n");
		
		for(ComponentStatus componentStatus : executionStatus.getComponentStatus()){
			stringBuilder.append("-------------------------------------\n");
			stringBuilder.append("Component ID: " + componentStatus.getComponentId() + "\n");
			stringBuilder.append("Current Status: " + componentStatus.getCurrentStatus() + "\n");
			stringBuilder.append("Processed record count: " + componentStatus.getProcessedRecordCount().toString() + "\n");
		}
		
		stringBuilder.append("============================================================================\n");
		logger.info(stringBuilder.toString());
		return stringBuilder.toString();
	}

	/**
	 * Dispose logger.
	 *
	 * @param jobId the job id
	 */
	public void disposeLogger(String jobId){
		if (executionTrackingLoggers.containsKey(jobId)) {
			BufferedWriter bufferedWriter = executionTrackingLoggers.get(jobId);
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				logger.info("Unable to close execution tracking logger" + e);
			}
		}
	}
}