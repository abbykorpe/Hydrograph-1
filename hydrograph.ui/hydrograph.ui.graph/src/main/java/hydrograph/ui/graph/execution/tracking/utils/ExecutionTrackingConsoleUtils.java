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

package hydrograph.ui.graph.execution.tracking.utils;

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.graph.Activator;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.execution.tracking.preferences.ExecutionPreferenceConstants;
import hydrograph.ui.graph.execution.tracking.windows.ExecutionTrackingConsole;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.graph.utility.MessageBox;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;


/**
 * The Class ExecutionTrackingConsoleUtils.
 * @author Bitwise
 */
public class ExecutionTrackingConsoleUtils {
	
	/** The logger. */
	private static Logger logger = LogFactory.INSTANCE.getLogger(ExecutionTrackingConsoleUtils.class);

	/** The instance. */
	public static ExecutionTrackingConsoleUtils INSTANCE = new ExecutionTrackingConsoleUtils();
	
	/** The Constant ExecutionTrackingLogFileExtention. */
	private static final String EXECUTION_TRACKING_LOG_FILE_EXTENTION = ".log";
	
	private static final String EXECUTION_TRACKING_LOCAL_MODE = "L_";

	private static final String EXECUTION_TRACKING_REMOTE_MODE = "R_";

	
	/**
	 * Instantiates a new execution tracking console utils.
	 */
	private ExecutionTrackingConsoleUtils(){}
	
	/**
	 * Open execution tracking console.
	 */
	public void openExecutionTrackingConsole(){
		String localJobId = getLocalJobId();
		if(StringUtils.isBlank(localJobId)){
			return;
		}

		if(!isConsoleAlreadyOpen(localJobId)){
			if(!JobManager.INSTANCE.isJobRunning(localJobId)){
				openExecutionTrackingConsoleWindow(localJobId);
			}
		}
		
		openExecutionTrackingConsoleWindow(localJobId);
	}
	
	/**
	 * Open execution tracking console.
	 *
	 * @param localJobId the local job id
	 */
	public void openExecutionTrackingConsole(String localJobId){
		
		if(StringUtils.isBlank(localJobId)){
			return;
		}

		if(!isConsoleAlreadyOpen(localJobId)){
			if(!JobManager.INSTANCE.isJobRunning(localJobId)){
				openExecutionTrackingConsoleWindow(localJobId);
			}
		}
		
		openExecutionTrackingConsoleWindow(localJobId);
	}
	
	/**
	 * Checks if is console already open.
	 *
	 * @param localJobId the local job id
	 * @return true, if is console already open
	 */
	private boolean isConsoleAlreadyOpen(String localJobId){
		ExecutionTrackingConsole console = JobManager.INSTANCE.getExecutionTrackingConsoles().get(localJobId.replace(".", "_"));
		if(console==null){
			return false;
		}else{
			if(console.getShell()==null){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Open execution tracking console window.
	 *
	 * @param localJobId the local job id
	 */
	private void openExecutionTrackingConsoleWindow(String localJobId) {
		ExecutionTrackingConsole console = JobManager.INSTANCE.getExecutionTrackingConsoles().get(localJobId.replace(".", "_"));
		if(console==null){
			console = new ExecutionTrackingConsole(localJobId);
			JobManager.INSTANCE.getExecutionTrackingConsoles().put(localJobId.replace(".", "_"), console);
		}
		if(console.getShell()==null){
			console.open();
		}else{
			Rectangle originalBounds = console.getShell().getBounds();
			console.getShell().setMaximized(true);
			Rectangle originalBoundsClone = new Rectangle(originalBounds.x, originalBounds.y, originalBounds.width, originalBounds.height);
			console.getShell().setBounds(originalBoundsClone);		
			console.getShell().setActive();	
		}
		console.setStatus(null, readFile(null, getUniqueJobId(), JobManager.INSTANCE.isLocalMode()));
	}

	/**
	 * Gets the local job id.
	 *
	 * @return the local job id
	 */
	private String getLocalJobId() {
		DefaultGEFCanvas canvas = CanvasUtils.INSTANCE.getComponentCanvas();
		
		if(canvas==null){
			MessageBox.INSTANCE.showMessage(MessageBox.INFO, Messages.NO_ACTIVE_GRAPHICAL_EDITOR);
			return null;
		}
		
		String jobId = canvas.getActiveProject() + "." + canvas.getJobName();
		return jobId;
	}
	
	/**
	 * Read log file.
	 *
	 * @param executionStatus the execution status
	 * @param uniqueJobId the unique job id
	 * @return the string builder
	 */
	public StringBuilder readFile(ExecutionStatus executionStatus, String uniqueJobId, boolean isLocalMode){
		String jobId = "";
		if(executionStatus != null){
			jobId = executionStatus.getJobId();	
		}else{
			jobId = uniqueJobId;
		}
		if(isLocalMode){
			jobId = EXECUTION_TRACKING_LOCAL_MODE + jobId;
		}else{
			jobId = EXECUTION_TRACKING_REMOTE_MODE + jobId;
		}
		try {
			String path = getLogPath() + jobId + EXECUTION_TRACKING_LOG_FILE_EXTENTION;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
			StringBuilder builder = new StringBuilder();
			String line = bufferedReader.readLine();
			while(line != null){
				builder.append(line);
				builder.append(System.lineSeparator());
			    line = bufferedReader.readLine();
			}
			return builder;
			} catch (FileNotFoundException exception) {
				logger.error("File not found", exception.getMessage());
			} catch (IOException exception) {
				logger.error("IO Exception", exception.getMessage());
			}
			return null;
	}

	/**
	 * Gets the log path.
	 *
	 * @return the log path
	 */
	private String getLogPath(){
		String jobTrackingLogDirectory = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, ExecutionPreferenceConstants.TRACKING_LOG_PATH, 
				TrackingDisplayUtils.INSTANCE.getInstallationPath(), null);
		return jobTrackingLogDirectory = jobTrackingLogDirectory + "/";
	}

	/**
	 * Gets the unique job id.
	 *
	 * @return the unique job id
	 */
	private String getUniqueJobId(){
		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		String uniqueJobId = editor.getJobId();
		
		return uniqueJobId;
		
	}
}
