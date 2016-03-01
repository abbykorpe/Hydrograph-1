package com.bitwise.app.graph.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.graph.job.JobManager;
import com.bitwise.app.graph.job.RunStopButtonCommunicator;

/**
 * 
 * The class is responsible to kill running job
 * 
 * @author bitwise
 *
 */
public class StopJobHandler extends AbstractHandler {
	
	public StopJobHandler(){
		setBaseEnabled(false);
		RunStopButtonCommunicator.StopJob.setHandler(this);
	}
	
	private DefaultGEFCanvas getComponentCanvas() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
		else
			return null;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//RunStopButtonCommunicator.runJobHandler.setRunJobEnable(true);
		JobManager.INSTANCE.killJob(getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName());
		//JobManager.INSTANCE.setJobStatus(getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName(),"KILLED");
		//setBaseEnabled(false);
		
		/*MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK );
		messageBox.setText("Kill job");
		messageBox.setMessage("Kill request accpeted.\nPlease make a note that - Run button will available only when current operation will complete");
		messageBox.open();*/
		
		//((RunJobHandler)RunStopButtonCommunicator.RunJob.getHandler()).setRunJobEnabled(true);
		return null;
	}
	
	/**
	 * 
	 * Enable/Disable stop button
	 * 
	 * @param enable
	 */
	public void setStopJobEnabled(boolean enable){
		setBaseEnabled(enable);
	}
}
