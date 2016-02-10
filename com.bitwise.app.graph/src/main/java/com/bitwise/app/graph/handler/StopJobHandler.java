package com.bitwise.app.graph.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bitwise.app.graph.job.RunStopButtonCommunicator;

public class StopJobHandler extends AbstractHandler {
	
	public StopJobHandler(){
		setBaseEnabled(false);
		RunStopButtonCommunicator.stopJobHandler = this;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//RunStopButtonCommunicator.runJobHandler.setRunJobEnable(true);
		return null;
	}
	
	public void setStopJobEnable(boolean enable){
		setBaseEnabled(enable);
	}
}
