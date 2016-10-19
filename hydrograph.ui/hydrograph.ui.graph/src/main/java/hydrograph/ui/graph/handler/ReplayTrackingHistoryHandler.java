package hydrograph.ui.graph.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.dataviewer.constants.MessageBoxText;
import hydrograph.ui.graph.Activator;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.execution.tracking.preferences.ExecutionPreferenceConstants;
import hydrograph.ui.graph.execution.tracking.replay.ReplayExecutionTrackingDialog;
import hydrograph.ui.graph.execution.tracking.replay.ReplayExecutionTrackingUtility;
import hydrograph.ui.graph.execution.tracking.utils.TrackingDisplayUtils;
import hydrograph.ui.graph.execution.tracking.utils.TrackingStatusUpdateUtils;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.utility.MessageBox;

public class ReplayTrackingHistoryHandler extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String consoleName = getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName();
		
		Map<String, List<Job>> jobDetails1 = ReplayExecutionTrackingUtility.INSTANCE.getTrackingJobs();
		List<Job> tmpList = jobDetails1.get(consoleName);
		
		if(tmpList==null){
			MessageBox.INSTANCE.showMessage(MessageBoxText.INFO, Messages.FORGOT_TO_EXECUTE_DEBUG_JOB);
			return "";
		}
		
		ReplayExecutionTrackingDialog dialog = new ReplayExecutionTrackingDialog(Display.getDefault().getActiveShell(), tmpList);
		dialog.open();
		
		
		try {
			ExecutionStatus executionStatus = readJsonLogFile(dialog.getSelectedUniqueJobId(), true, getLogPath());
			replayExecutionTracking(executionStatus);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	public void replayExecutionTracking(ExecutionStatus executionStatus){
		IWorkbenchPage page = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage();
		IEditorReference[] refs = page.getEditorReferences();

		ReplayExecutionTrackingUtility.INSTANCE.addTrackingStatus(executionStatus.getJobId(), executionStatus);
		for (IEditorReference ref : refs){
			IEditorPart editor = ref.getEditor(false);
			if(editor instanceof ELTGraphicalEditor){
				TrackingStatusUpdateUtils.INSTANCE.updateEditorWithCompStatus(executionStatus, (ELTGraphicalEditor)editor,true); 
			}
		}
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

	private ExecutionStatus readJsonLogFile(String uniqueJobId, boolean isLocalMode, String filePath) throws FileNotFoundException{
		ExecutionStatus[] executionStatus;
		String jobId = "";
		String path = null;
		
		if(isLocalMode){
			jobId = "L_" + uniqueJobId;
		}else{
			jobId = "R_" + uniqueJobId;
		}
		path = getLogPath() + jobId + ".track.log";
		
		JsonParser jsonParser = new JsonParser();
		
		Gson gson = new Gson();
		String jsonArray = jsonParser.parse(new FileReader(new File(path))).toString();
		executionStatus = gson.fromJson(jsonArray, ExecutionStatus[].class);
		for(int i=0; i<executionStatus.length;i++){
			System.out.println(executionStatus[i]);
		}
		System.out.println("<<<<<LAST_Status>>>>>>"+executionStatus[executionStatus.length-1]);
		return executionStatus[executionStatus.length-1];
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
}
