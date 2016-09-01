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

import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.graph.Activator;
import hydrograph.ui.graph.execution.tracking.datastructure.ComponentStatus;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.execution.tracking.preferences.ExecutionPreferenceConstants;
import hydrograph.ui.graph.execution.tracking.utils.TrackingDisplayUtils;
import hydrograph.ui.logging.execution.tracking.ExecutionTrackingLogger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;


/**
 * The Class ExecutionTrackingFileLogger use to show as well as save execution tracking log
 */
public class ExecutionTrackingFileLogger {
	
	/** The Constant ExecutionTrackingLogFileExtention. */
	private static final String EXECUTION_TRACKING_LOG_FILE_EXTENTION = ".log";
	private static final String EXECUTION_TRACKING_LOCAL_MODE = "L_";
	private static final String EXECUTION_TRACKING_REMOTE_MODE = "R_";

	/** The Constant INSTANCE. */
	public static final ExecutionTrackingFileLogger INSTANCE = new ExecutionTrackingFileLogger();
	
	/** The job tracking log directory. */
	private String jobTrackingLogDirectory;;

	private Map<String,ExecutionStatus> lastExecutionStatus;
	
	/**
	 * Instantiates a new execution tracking file logger.
	 */
	private ExecutionTrackingFileLogger(){

		jobTrackingLogDirectory = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, ExecutionPreferenceConstants.TRACKING_LOG_PATH, 
				TrackingDisplayUtils.INSTANCE.getInstallationPath(), null);			
		lastExecutionStatus = new HashMap<>();
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

	public void removeLastExecutionStatus(String jobID){
		if(lastExecutionStatus.get(jobID)!=null){
			lastExecutionStatus.remove(jobID);
		}
	}
	
	/**
	 * Write the log
	 *
	 * @param uniqJobId the uniq job id
	 * @param executionStatus the execution status
	 */
	public void log(String uniqJobId,ExecutionStatus executionStatus, boolean isLocalMode){
		
		if(lastExecutionStatus.get(uniqJobId)!=null && executionStatus.equals(lastExecutionStatus.get(uniqJobId))){
			return;
		}
		lastExecutionStatus.put(uniqJobId, executionStatus);
		
		String executionStatusString = getExecutionStatusInString(executionStatus);
		if(StringUtils.isBlank(executionStatusString)){
			return;
		}
		
		Logger executionTrackingLogger = getExecutionStatusLogger(uniqJobId, isLocalMode);
		executionTrackingLogger.debug(executionStatusString);
	}

	/**
	 * Gets the execution status logger.
	 *
	 * @param uniqJobId the uniq job id
	 * @return the execution status logger
	 */
	private Logger getExecutionStatusLogger(String uniqJobId, boolean isLocalMode) {	
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
		
		Logger logger = ExecutionTrackingLogger.INSTANCE.getLogger(uniqJobId, jobTrackingLogDirectory + uniqJobId + EXECUTION_TRACKING_LOG_FILE_EXTENTION);

		return logger;
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
		stringBuilder.append("Job ID: " + executionStatus.getJobId() + "\n");
		stringBuilder.append("Job Type: " + executionStatus.getType() + "\n");
		
		for(ComponentStatus componentStatus : executionStatus.getComponentStatus()){
			stringBuilder.append("-------------------------------------\n");
			stringBuilder.append("Component ID: " + componentStatus.getComponentId() + "\n");
			//stringBuilder.append("Component Name: " + componentStatus.getComponentName() + "\n");
			stringBuilder.append("Current Status: " + componentStatus.getCurrentStatus() + "\n");
			stringBuilder.append("Processed record count: " + componentStatus.getProcessedRecordCount().toString() + "\n");
		}
		
		stringBuilder.append("============================================================================\n");		
		return stringBuilder.toString();
	}

	/**
	 * Dispose logger.
	 */
	public void disposeLogger(){
		//TODO - to be implement
	}
}
