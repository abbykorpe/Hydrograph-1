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

public class ExecutionTrackingFileLogger {
	private static final String ExecutionTrackingLogFileExtention = ".log";

	private static final Logger logger = LogFactory.INSTANCE.getLogger(ExecutionTrackingFileLogger.class);
	
	public static final ExecutionTrackingFileLogger INSTANCE = new ExecutionTrackingFileLogger();
	private String defaultJobTrackingLogDirectory;
	private String jobTrackingLogDirectory;;
		
	private Map<String,BufferedWriter> executionTrackingLoggers;
	
	private ExecutionTrackingFileLogger(){
		IEclipsePreferences eclipsePreferences = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		defaultJobTrackingLogDirectory=Platform.getInstallLocation().getURL().getPath() + getLoggerPath();
		jobTrackingLogDirectory = eclipsePreferences.get(ExecutionPreferenceConstants.TRACKING_LOG_PATH, defaultJobTrackingLogDirectory);
		jobTrackingLogDirectory = jobTrackingLogDirectory + "\\";
				
		executionTrackingLoggers = new HashMap<>();
		
		createJobTrackingLogDirectory();
	}

	private String getLoggerPath(){
		String dirPath = null;
		if(OSValidator.isWindows()){
			dirPath = "config//logger//JobTrackingLog";
		}else if(OSValidator.isMac()){
			dirPath = "config\\logger\\JobTrackingLog";
		}else if(OSValidator.isUnix()){
			dirPath = "config\\logger\\JobTrackingLog";
		}
		return dirPath;
	}
	
	
	private void createJobTrackingLogDirectory() {
		File file = new File(jobTrackingLogDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
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

	private BufferedWriter getExecutionStatusLogger(String uniqJobId) {
		BufferedWriter bufferedWriter = executionTrackingLoggers.get(uniqJobId);		
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

	private String getExecutionStatusInString(ExecutionStatus executionStatus) {
		StringBuilder stringBuilder = new StringBuilder();
		if(executionStatus==null){
			return null;
		}
		stringBuilder.append("Job ID: " + executionStatus.getJobId() + "\n");
		stringBuilder.append("Job Type: " + executionStatus.getType() + "\n");
		stringBuilder.append("Job Status: " + executionStatus.getJobStatus() + "\n");
		
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
