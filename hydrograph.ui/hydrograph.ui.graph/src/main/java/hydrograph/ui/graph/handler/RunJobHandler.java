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
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;
import hydrograph.ui.graph.utility.CanvasUtils;
import hydrograph.ui.propertywindow.runconfig.RunConfigDialog;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

/**
 * Handler use to run the job using gradle command.
 * 
 * @author Bitwise
 * @version 1.0
 * @since 2015-10-27
 */
public class RunJobHandler{


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
	public Object execute(RunConfigDialog runConfigDialog) {
		
		closeOpenedDataViewerWindows();
		
		((ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()).getViewer().deselectAll();
		String consoleName = getComponentCanvas().getActiveProject() + "." + getComponentCanvas().getJobName();
		String canvasName = consoleName;
		String localJobID = consoleName;

		if (validateGraphProperties()){
			if(isConfirmedByUser()){
				JobManager.INSTANCE.executeJob(getJob(localJobID, consoleName, canvasName), null,runConfigDialog);
			}
				
		}else{
		
			JobManager.INSTANCE.executeJob(getJob(localJobID, consoleName, canvasName), null,runConfigDialog);
		}
		CanvasUtils.INSTANCE.getComponentCanvas().restoreMenuToolContextItemsState();		
		return null;
	}
	
	private boolean validateGraphProperties() {
		Map<String, String> graphPropertiesMap = null;
		boolean retValue = false;
		ELTGraphicalEditor editor = (ELTGraphicalEditor) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();

		if (null != editor) {

			graphPropertiesMap = (Map<String, String>) editor.getContainer()
					.getGraphRuntimeProperties();

			for (String key : graphPropertiesMap.keySet()) {

				if (StringUtils.isBlank(graphPropertiesMap.get(key))) {

					retValue = true;

					break;
				}

			}

		}

		return retValue;
	}

	private boolean isConfirmedByUser() {
		MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_QUESTION | SWT.YES
				| SWT.NO);
		messageBox.setMessage(Messages.CONFIRM_FOR_GRAPH_PROPS_RUN_JOB);
		messageBox.setText(Messages.CONFIRM_FOR_GRAPH_PROPS_RUN_JOB_TITLE);
		int response = messageBox.open();
		if (response == SWT.YES) {
			return true;
		} else {			
			return false; 
		}
	}

}
