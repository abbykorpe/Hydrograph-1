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

package hydrograph.ui.graph.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.dataviewer.constants.MessageBoxText;
import hydrograph.ui.graph.Activator;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.execution.tracking.preferences.ExecutionPreferenceConstants;
import hydrograph.ui.graph.execution.tracking.replay.ReplayComponentDialog;
import hydrograph.ui.graph.execution.tracking.replay.ViewExecutionHistoryDialog;
import hydrograph.ui.graph.execution.tracking.replay.ViewExecutionHistoryUtility;
import hydrograph.ui.graph.execution.tracking.utils.TrackingDisplayUtils;
import hydrograph.ui.graph.execution.tracking.utils.TrackingStatusUpdateUtils;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.utility.MessageBox;
import hydrograph.ui.logging.factory.LogFactory;
/**
 * 
 * Open tracking dialog window where user can view previous execution tracking view history.
 * @author Bitwise
 *
 */
public class ViewExecutionHistoryHandler extends AbstractHandler{

	/** The logger. */
	private static Logger logger = LogFactory.INSTANCE.getLogger(ViewExecutionHistoryHandler.class);
	
	/** The Constant ExecutionTrackingLogFileExtention. */
	private static final String EXECUTION_TRACKING_LOG_FILE_EXTENTION = ".track.log";
	private static final String EXECUTION_TRACKING_LOCAL_MODE = "L_";
	private static final String EXECUTION_TRACKING_REMOTE_MODE = "R_";
	
	private List<String> compNameList;
	private List<String> missedCompList;

	
	/**
	 * Show view execution history for selected jobId.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String consoleName = getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName();
		
		Map<String, List<Job>> jobDetails1 = ViewExecutionHistoryUtility.INSTANCE.getTrackingJobs();
		List<Job> tmpList = jobDetails1.get(consoleName);
		
		if(tmpList==null){
			MessageBox.INSTANCE.showMessage(MessageBoxText.INFO, Messages.RUN_THE_JOB);
			return "";
		}
		
		ViewExecutionHistoryDialog dialog = new ViewExecutionHistoryDialog(Display.getDefault().getActiveShell(),this ,tmpList);
		dialog.open();
		
		if(!(missedCompList.size() > 0) && !(compNameList.size() > 0)){
			return true;
		}
		ReplayComponentDialog componentDialog = new ReplayComponentDialog(Display.getDefault().getActiveShell(), compNameList, missedCompList);
		componentDialog.open();
		
		
		compNameList.clear();
		missedCompList.clear();
		
		return null;
	}
	
	/**
	 * Apply status and count on editor according to jobid.
	 * @param executionStatus
	 * @return boolean (the status if replay was successful(true) or not(false))
	 */
	public boolean replayExecutionTracking(ExecutionStatus executionStatus){
		IWorkbenchPage page = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage();
		IEditorReference[] refs = page.getEditorReferences();

		ViewExecutionHistoryUtility.INSTANCE.addTrackingStatus(executionStatus.getJobId(), executionStatus);
		ViewExecutionHistoryUtility.INSTANCE.getUnusedCompsOnCanvas().clear();
		
		ELTGraphicalEditor eltGraphicalEditor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if(!(eltGraphicalEditor.getEditorInput() instanceof GraphicalEditor)){
			
			if(!executionStatus.getJobId().startsWith(eltGraphicalEditor.getContainer().getUniqueJobId())){
				getMessageDialog(Messages.INVALID_LOG_FILE +eltGraphicalEditor.getContainer().getUniqueJobId());
				return false;
			}else{
				TrackingStatusUpdateUtils.INSTANCE.updateEditorWithCompStatus(executionStatus, eltGraphicalEditor, true);
				compNameList = new ArrayList<>();
				missedCompList = ViewExecutionHistoryUtility.INSTANCE.getReplayMissedComponents(executionStatus);
				
				ViewExecutionHistoryUtility.INSTANCE.getExtraComponentList(executionStatus);
				ViewExecutionHistoryUtility.INSTANCE.getUnusedCompsOnCanvas().forEach((compId, compName)->{
					compNameList.add(compName);
				});
				
				return true;
			} 
		}
		return false;
		
	}
	
	/**
	 * Gets the component canvas.
	 *
	 * @return the component canvas
	 */
	private DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}

/**
 * Return last execution tracking status from tracking log file.
 * @param uniqueJobId
 * @param isLocalMode
 * @param filePath
 * @return
 * @throws FileNotFoundException
 */
	public ExecutionStatus readJsonLogFile(String uniqueJobId, boolean isLocalMode, String filePath) throws FileNotFoundException{
		ExecutionStatus[] executionStatus;
		String jobId = "";
		String path = null;
		
		if(isLocalMode){
			jobId = EXECUTION_TRACKING_LOCAL_MODE + uniqueJobId;
		}else{
			jobId = EXECUTION_TRACKING_REMOTE_MODE + uniqueJobId;
		}
		path = getLogPath() + jobId + EXECUTION_TRACKING_LOG_FILE_EXTENTION;
		
		JsonParser jsonParser = new JsonParser();
		
		Gson gson = new Gson();
		String jsonArray = jsonParser.parse(new FileReader(new File(path))).toString();
		executionStatus = gson.fromJson(jsonArray, ExecutionStatus[].class);
		return executionStatus[executionStatus.length-1];
	}
	
	/**
	 * Return last execution tracking status from browsed tracking log file.
	 * @param filePath
	 * @return ExecutionStatus
	 * @throws FileNotFoundException
	 */
		public ExecutionStatus readBrowsedJsonLogFile(String filePath) throws FileNotFoundException{
			ExecutionStatus[] executionStatus;
			JsonParser jsonParser = new JsonParser();
			Gson gson = new Gson();
			String jsonArray = jsonParser.parse(new FileReader(new File(filePath))).toString();
			executionStatus = gson.fromJson(jsonArray, ExecutionStatus[].class);
			return executionStatus[executionStatus.length-1];
		}
	
	
	/**
	 * Gets the log path.
	 *
	 * @return the log path
	 */
	public String getLogPath(){
		String jobTrackingLogDirectory = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, ExecutionPreferenceConstants.TRACKING_LOG_PATH, 
				TrackingDisplayUtils.INSTANCE.getInstallationPath(), null);
		return jobTrackingLogDirectory = jobTrackingLogDirectory + "/";
	}
	
	
	/**
	 * @param message Display the error message 
	 * 
	 */
	public void getMessageDialog(String message){
		MessageBox.INSTANCE.showMessage(MessageBoxText.INFO, message);
		return;
	}
	
	
	private String getUniqueJobId(){
		String jobId = "";
		ELTGraphicalEditor eltGraphicalEditor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(!(eltGraphicalEditor.getEditorInput() instanceof GraphicalEditor)){
			jobId = eltGraphicalEditor.getContainer().getUniqueJobId();
			return jobId;
		}
		return jobId;
	}
}
