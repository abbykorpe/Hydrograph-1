/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hydrograph.ui.graph.handler;

import hydrograph.ui.graph.job.RunStopButtonCommunicator;
import hydrograph.ui.propertywindow.runconfig.RunConfigDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

/**
 * JobHandler Handles both debug and normal run.
 *
 * @author Bitwise
 */
public class JobHandler extends AbstractHandler {

	/**
	 * Instantiates a new job handler.
	 */
	public JobHandler() {
		RunStopButtonCommunicator.RunJob.setHandler(this);
	} 
	
	/**
	 * Enable disable run button.
	 *
	 * @param enable the new run job enabled
	 */
	public void setRunJobEnabled(boolean enable) {
		setBaseEnabled(enable);
	}

	/**
	 * Enable disable debug button.
	 *
	 * @param enable the new debug job enabled
	 */
	public void setDebugJobEnabled(boolean enable){
		setBaseEnabled(enable);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		RunConfigDialog runConfigDialog = getRunConfiguration();
		if(runConfigDialog.isDebug()){
			new DebugHandler().execute(runConfigDialog);
		}
		else{
			new RunJobHandler().execute(runConfigDialog);
		}
		return null;
	}

	/**
	 * Gets the run configuration.
	 *
	 * @return the run configuration
	 */
	private RunConfigDialog getRunConfiguration() {
		RunConfigDialog runConfigDialog = new RunConfigDialog(Display.getDefault().getActiveShell());
		runConfigDialog.open();
		return runConfigDialog;
	}

}
