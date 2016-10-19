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

package hydrograph.ui.graph.execution.tracking.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.json.JSONException;
import org.slf4j.Logger;

import com.google.gson.Gson;

import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.graph.Activator;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.execution.tracking.preferences.ExecutionPreferenceConstants;
import hydrograph.ui.graph.execution.tracking.utils.TrackingDisplayUtils;
import hydrograph.ui.logging.factory.LogFactory;


class LastExecutionStatus{
	
	private int statusNumber;
	private ExecutionStatus executionStatus;
	
	
	public LastExecutionStatus(int statusNumber, ExecutionStatus executionStatus) {
		super();
		this.statusNumber = statusNumber;
		this.executionStatus = executionStatus;
	}

	public int getStatusNumber() {
		return statusNumber;
	}

	public ExecutionStatus getExecutionStatus() {
		return executionStatus;
	}
	
	@Override
	public String toString() {
		return "LastExecutionStatus [statusNumber=" + statusNumber + ", executionStatus=" + executionStatus + "]";
	}	
}

/**
 * The Class ExecutionTrackingFileLogger use to show as well as save execution tracking log
 */
public class ExecutionTrackingFileLogger {
	
	/** The logger. */
	private Logger logger = LogFactory.INSTANCE.getLogger(ExecutionTrackingFileLogger.class);
	
	private static final String EXECUTION_STATUS_RECORD_SEPARATOR = " | ";
	private static final String TIMESTAMP_FORMAT = "MM/dd/yyyy HH:mm:ss";
	private static final String SUBMISSION_TIME = "Submission time: ";
	private static final String JOB_ID = "Job ID: ";
	
	/** The Constant ExecutionTrackingLogFileExtention. */
	private static final String EXECUTION_TRACKING_LOG_FILE_EXTENTION = ".track.log";
	private static final String EXECUTION_TRACKING_LOCAL_MODE = "L_";
	private static final String EXECUTION_TRACKING_REMOTE_MODE = "R_";

	private List<ExecutionStatus> executionStatusList = new ArrayList<>();
	/** The Constant INSTANCE. */
	public static final ExecutionTrackingFileLogger INSTANCE = new ExecutionTrackingFileLogger();
	
	/** The job tracking log directory. */
	private String jobTrackingLogDirectory;;

	//private Map<String,LastExecutionTrackingStatus> lastExecutionStatusMap;
	
	/**
	 * Instantiates a new execution tracking file logger.
	 */
	private ExecutionTrackingFileLogger(){

		jobTrackingLogDirectory = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, ExecutionPreferenceConstants.TRACKING_LOG_PATH, 
				TrackingDisplayUtils.INSTANCE.getInstallationPath(), null);			
		//lastExecutionStatusMap = new HashMap<>();
		createJobTrackingLogDirectory();
	}

	
	private void initializeTrackingLogPath(){
 		jobTrackingLogDirectory = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, ExecutionPreferenceConstants.TRACKING_LOG_PATH, 
 				TrackingDisplayUtils.INSTANCE.getInstallationPath(), null);
 		if(OSValidator.isWindows()){
 			jobTrackingLogDirectory = jobTrackingLogDirectory + "\\";
 		}else if(OSValidator.isMac()){
 			jobTrackingLogDirectory = jobTrackingLogDirectory + "//";
 		}
 	}
	
	/**
	 * Creates the job tracking log directory.
	 */
	private void createJobTrackingLogDirectory() {
		initializeTrackingLogPath();
		File file = new File(jobTrackingLogDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/*public void removeLastExecutionStatus(String jobID){
		if(lastExecutionStatusMap.get(jobID)!=null){
			lastExecutionStatusMap.remove(jobID);
		}
	}*/
	
	/**
	 * Write the log
	 *
	 * @param uniqJobId the uniq job id
	 * @param executionStatus the execution status
	 */
	public void log(String uniqJobId,ExecutionStatus executionStatus, boolean isLocalMode){
		
		String header=null;
		/*if(lastExecutionStatusMap.get(uniqJobId)==null){
			lastExecutionStatusMap.put(uniqJobId, new LastExecutionTrackingStatus(0, executionStatus));
			header = getHeader(executionStatus);
		}else{
			header = null;
			if(executionStatus.equals(lastExecutionStatusMap.get(uniqJobId).getExecutionStatus())){
				return;
			}			
			lastExecutionStatusMap.put(uniqJobId, new LastExecutionTrackingStatus(lastExecutionStatusMap.get(uniqJobId).getStatusNumber() + 1, executionStatus));
		}*/
		
		executionStatusList.add(executionStatus);
		getExecutionStatusLogger(uniqJobId, isLocalMode, executionStatusList);
		/*Logger executionTrackingLogger = getExecutionStatusLogger(uniqJobId, isLocalMode, executionStatusList);
		
		if(!StringUtils.isBlank(header)){
			executionTrackingLogger.debug(header);
		}
		executionTrackingLogger.debug(executionStatusString);*/
		/*String executionStatusString = getExecutionStatusInString(executionStatus);
		if(StringUtils.isBlank(executionStatusString)){
			return;
		}*/
	}


	private String getHeader(ExecutionStatus executionStatus) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(JOB_ID);
		stringBuilder.append(executionStatus.getJobId() + EXECUTION_STATUS_RECORD_SEPARATOR);
		stringBuilder.append(SUBMISSION_TIME);
		
		String timeStamp = getTimeStamp();
		stringBuilder.append(timeStamp + "\n");
		return stringBuilder.toString();
	}


	private String getTimeStamp() {
		String timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
		return timeStamp;
	}


	/**
	 * Gets the execution status logger.
	 *
	 * @param uniqJobId the uniq job id
	 * @return the execution status logger
	 */
	private void getExecutionStatusLogger(String uniqJobId, boolean isLocalMode, List<ExecutionStatus> executionStatusList) {	
		if(isLocalMode){
			uniqJobId = EXECUTION_TRACKING_LOCAL_MODE + uniqJobId;
		}else{
			uniqJobId = EXECUTION_TRACKING_REMOTE_MODE + uniqJobId;
		}
		if(OSValidator.isWindows()){
			jobTrackingLogDirectory = jobTrackingLogDirectory + "\\";
		}else if(OSValidator.isMac()){
			jobTrackingLogDirectory = jobTrackingLogDirectory + "//";
		}
		
		//Logger logger = ExecutionTrackingLogger.INSTANCE.getLogger(uniqJobId, jobTrackingLogDirectory + uniqJobId + EXECUTION_TRACKING_LOG_FILE_EXTENTION);
		try {
			createJsonFormatFile(jobTrackingLogDirectory + uniqJobId + EXECUTION_TRACKING_LOG_FILE_EXTENTION, executionStatusList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//return logger;
	}

	/**
	 * Gets the execution status in string.
	 *
	 * @param executionStatus the execution status
	 * @return the execution status in string
	 *//*
	private String getExecutionStatusInString(ExecutionStatus executionStatus) {
		StringBuilder stringBuilder = new StringBuilder();
		if(executionStatus==null){
			return null;
		}

		for(ComponentStatus componentStatus : executionStatus.getComponentStatus()){
			
			Map<String, Long> processCounts = componentStatus.getProcessedRecordCount();
			
			for(String portID: processCounts.keySet()){
				stringBuilder.append(lastExecutionStatusMap.get(executionStatus.getJobId()).getStatusNumber() + EXECUTION_STATUS_RECORD_SEPARATOR);
				stringBuilder.append(getTimeStamp() + EXECUTION_STATUS_RECORD_SEPARATOR);
				stringBuilder.append(componentStatus.getComponentId() + EXECUTION_STATUS_RECORD_SEPARATOR);
				stringBuilder.append(portID + EXECUTION_STATUS_RECORD_SEPARATOR);
				stringBuilder.append(componentStatus.getCurrentStatus() + EXECUTION_STATUS_RECORD_SEPARATOR);
				stringBuilder.append(processCounts.get(portID) + "\n");
			}
		}
				
		return stringBuilder.toString();
	}*/

	private void createJsonFormatFile(String path, List<ExecutionStatus> executionStatusList) throws JSONException {
		try {
			FileWriter fileWriter = new FileWriter(path);
			Gson gson = new Gson();
			String jsonArray=gson.toJson(executionStatusList);
			fileWriter.write(jsonArray);
			fileWriter.flush();
			fileWriter.close();
			
		} catch (IOException exception) {
			logger.error("Failed to create json file", exception);
		}
	}
	
	
	/**
	 * Dispose logger.
	 */
	public void disposeLogger(){
		//TODO - to be implemented
	}
}
