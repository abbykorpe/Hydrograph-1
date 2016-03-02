package com.bitwise.app.graph.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
		JobManager.INSTANCE.killJob(getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName());
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
