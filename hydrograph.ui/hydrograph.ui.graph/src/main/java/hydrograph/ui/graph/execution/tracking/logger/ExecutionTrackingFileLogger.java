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
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.slf4j.Logger;


/**
 * The Class ExecutionTrackingFileLogger use to show as well as save execution tracking log
 */
public class ExecutionTrackingFileLogger {
	
	/** The Constant ExecutionTrackingLogFileExtention. */
	private static final String ExecutionTrackingLogFileExtention = ".log";

	/** The Constant logger. */
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ExecutionTrackingFileLogger.class);
	
	/** The Constant INSTANCE. */
	public static final ExecutionTrackingFileLogger INSTANCE = new ExecutionTrackingFileLogger();
	
	/** The default job tracking log directory. */
	private String defaultJobTrackingLogDirectory;
	
	/** The job tracking log directory. */
	private String jobTrackingLogDirectory;;
		
	/** The execution tracking loggers. */
	private Map<String,BufferedWriter> executionTrackingLoggers;
	
	/**
	 * Instantiates a new execution tracking file logger.
	 */
	private ExecutionTrackingFileLogger(){
		IEclipsePreferences eclipsePreferences = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		defaultJobTrackingLogDirectory=Platform.getInstallLocation().getURL().getPath() + getLoggerPath();
		jobTrackingLogDirectory = eclipsePreferences.get(ExecutionPreferenceConstants.TRACKING_LOG_PATH, defaultJobTrackingLogDirectory);
		//jobTrackingLogDirectory = jobTrackingLogDirectory + "\\";
				
		executionTrackingLoggers = new HashMap<>();
		
		createJobTrackingLogDirectory();
	}

	/**
	 * Gets the logger path.
	 *
	 * @return the logger path
	 */
	private String getLoggerPath(){
		String dirPath = null;
		if(OSValidator.isWindows()){
			dirPath = "config//logger//JobTrackingLog";
		}else if(OSValidator.isMac()){
			dirPath = "config//logger//JobTrackingLog";
		}else if(OSValidator.isUnix()){
			dirPath = "config\\logger\\JobTrackingLog";
		}
		return dirPath;
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
	 * Write the log
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
			logger.warn("Unable to write to execution tracking log file",e);
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
		if(OSValidator.isWindows()){
			jobTrackingLogDirectory = jobTrackingLogDirectory + "\\";
		}else if(OSValidator.isMac()){
			jobTrackingLogDirectory = jobTrackingLogDirectory + "//";
		}
		if(bufferedWriter==null){
			try{
				 FileWriter fileWriter = new FileWriter(jobTrackingLogDirectory + uniqJobId + ExecutionTrackingLogFileExtention, true);
				 bufferedWriter = new BufferedWriter(fileWriter);
				 executionTrackingLoggers.put(uniqJobId, bufferedWriter);
			}catch (Exception e) {
				logger.warn("Unable to create Execution Tracking Logger " , e);
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
		for(BufferedWriter bufferedWriter: executionTrackingLoggers.values()){
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				logger.warn("Unable to close execution tracking logger",e);
			}
		}
	}
}