package com.bitwise.app.graph.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;
import com.bitwise.app.graph.job.Job;
import com.bitwise.app.graph.job.JobManager;
import com.bitwise.app.graph.job.RunStopButtonCommunicator;

/**
 * Handler use to run the job using gradle command.
 * 
 * @author Bitwise
 * @version 1.0
 * @since 2015-10-27
 */
public class RunJobHandler extends AbstractHandler {
	
	public RunJobHandler(){
		RunStopButtonCommunicator.runJobHandler = this;
	}

	/**
	 * Enable disable run button
	 * 
	 * @param enable
	 */
	public void setRunJobEnable(boolean enable){
		setBaseEnabled(enable);
	}
	
	private Job getJob(String consoleName,String canvasName){
		return new Job(null, null, null, consoleName, canvasName);
	}
	
	private DefaultGEFCanvas getComponentCanvas() {		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}
	
	/*
	 * 
	 * Execute command to run the job.
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) {
		
		String consoleName= getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName();
		String canvasName = consoleName;
		JobManager.INSTANCE.executeJob(getJob(consoleName, canvasName));
		
		return null;
	}

	
	
	
	
	
	
}
