package com.bitwise.app.graph.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

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
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//RunStopButtonCommunicator.runJobHandler.setRunJobEnable(true);
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
