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

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;
import hydrograph.ui.graph.utility.CanvasUtils;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.PlatformUI;

/**
 * Handler use to run the job using gradle command.
 * 
 * @author Bitwise
 * @version 1.0
 * @since 2015-10-27
 */
public class RunJobHandler extends AbstractHandler {

	public RunJobHandler() {
		RunStopButtonCommunicator.RunJob.setHandler(this);
	}

	/**
	 * Enable disable run button
	 * 
	 * @param enable
	 */
	public void setRunJobEnabled(boolean enable) {
		setBaseEnabled(enable);
	}

	private Job getJob(String localJobID, String consoleName, String canvasName) {
		return new Job(localJobID, consoleName, canvasName, null, null, null, null);
	}

	private DefaultGEFCanvas getComponentCanvas() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		else
			return null;
	}

	private void closeOpenedDataViewerWindows() {

		for (DebugDataViewer debugDataViewer : JobManager.INSTANCE.getDataViewerMap().values()) {
			debugDataViewer.close();
		}
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
		
		closeOpenedDataViewerWindows();
		
		((ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()).getViewer().deselectAll();
		String consoleName = getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName();
		String canvasName = consoleName;
		String localJobID = consoleName;
		JobManager.INSTANCE.executeJob(getJob(localJobID, consoleName, canvasName), null);
		CanvasUtils.getComponentCanvas().restoreMenuToolContextItemsState();		
		return null;
	}

}
