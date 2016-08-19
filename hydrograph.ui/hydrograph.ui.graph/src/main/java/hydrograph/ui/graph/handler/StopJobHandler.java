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

import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


/**
 * 
 * The class is responsible to kill running job
 * 
 * @author Bitwise
 * 
 */
public class StopJobHandler extends AbstractHandler {

	public StopJobHandler() {
		setBaseEnabled(false);
		RunStopButtonCommunicator.StopJob.setHandler(this);
	}


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		((StopJobHandler) RunStopButtonCommunicator.StopJob.getHandler()).setStopJobEnabled(false);
		JobManager.INSTANCE.killJob(JobManager.INSTANCE.getActiveCanvas());
		
		return null;
	}

	/**
	 * 
	 * Enable/Disable stop button
	 * 
	 * @param enable
	 */
	public void setStopJobEnabled(boolean enable) {
		setBaseEnabled(enable);
	}
}
