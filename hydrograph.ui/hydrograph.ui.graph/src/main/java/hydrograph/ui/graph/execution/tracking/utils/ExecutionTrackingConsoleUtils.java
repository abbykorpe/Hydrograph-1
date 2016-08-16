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
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

public class ExecutionTrackingConsoleUtils {
	private static Logger logger = LogFactory.INSTANCE.getLogger(ExecutionTrackingConsoleUtils.class);

	public static ExecutionTrackingConsoleUtils INSTANCE = new ExecutionTrackingConsoleUtils();
	
	private ExecutionTrackingConsoleUtils(){}
	
	public void openExecutionTrackingConsole(){
		String localJobId = getLocalJobId();
		if(StringUtils.isBlank(localJobId)){
			return;
		}

		if(!isConsoleAlreadyOpen(localJobId)){
			if(!JobManager.INSTANCE.isJobRunning(localJobId)){
				openExecutionTrackingConsoleWindow(localJobId);
				//MessageBox.INSTANCE.showMessage(MessageBox.INFO, "Can not show Execution Tracking Console.\nThe job " + localJobId + " is not in execution");
				//return;
			}
		}
		
		openExecutionTrackingConsoleWindow(localJobId);
	}
	
	public void openExecutionTrackingConsole(String localJobId){
		
		if(StringUtils.isBlank(localJobId)){
			return;
		}

		if(!isConsoleAlreadyOpen(localJobId)){
			if(!JobManager.INSTANCE.isJobRunning(localJobId)){
				openExecutionTrackingConsoleWindow(localJobId);
				//MessageBox.INSTANCE.showMessage(MessageBox.INFO, "Can not show Execution Tracking Console.\nThe job " + localJobId + " is not in execution");
				//return;
			}
		}
		
		openExecutionTrackingConsoleWindow(localJobId);
	}
	
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
		console.setStatus(null, readFile(null, getUniqueJobId()));
	}

	private String getLocalJobId() {
		DefaultGEFCanvas canvas = CanvasUtils.INSTANCE.getComponentCanvas();
		
		if(canvas==null){
			MessageBox.INSTANCE.showMessage(MessageBox.INFO, Messages.NO_ACTIVE_GRAPHICAL_EDITOR);
			return null;
		}
		
		String jobId = canvas.getActiveProject() + "." + canvas.getJobName();
		return jobId;
	}
	
	public StringBuilder readFile(ExecutionStatus executionStatus, String uniqueJobId){
		String jobId = "";
		if(executionStatus != null){
			jobId = executionStatus.getJobId();	
		}else{
			jobId = uniqueJobId;
		}
		try {
			String path = getLogPath() + jobId + ".log";
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
			StringBuilder builder = new StringBuilder();
			String line = bufferedReader.readLine();
			while(line != null){
				builder.append(line);
				builder.append(System.lineSeparator());
			    line = bufferedReader.readLine();
			}
			String s = builder.toString();
			System.out.println(""+s);
				return builder;
			} catch (FileNotFoundException exception) {
				logger.error("File not found", exception.getMessage());
			} catch (IOException exception) {
				logger.error("IO Exception", exception.getMessage());
			}
			return null;
	}

	private String getLogPath(){
		IEclipsePreferences eclipsePreferences = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		String defaultJobTrackingLogDirectory=Platform.getInstallLocation().getURL().getPath() + "config/logger/JobTrackingLog";
		String jobTrackingLogDirectory = eclipsePreferences.get(ExecutionPreferenceConstants.TRACKING_LOG_PATH, defaultJobTrackingLogDirectory);
		return jobTrackingLogDirectory = jobTrackingLogDirectory + "/";
	}

	private String getUniqueJobId(){
		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		String uniqueJobId = editor.getJobId();
		
		return uniqueJobId;
		
	}
}
