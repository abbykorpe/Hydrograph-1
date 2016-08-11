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

import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;
import hydrograph.ui.propertywindow.runconfig.RunConfigDialog;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

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
		if(validateGraphProperties()){
		if(confirmationFromUser()){
			executeJob();
		}
		}
		else {
			executeJob();
		}
		return null;
	
	}
	
	public void executeJob()
	{
		RunConfigDialog runConfigDialog = getRunConfiguration();
		if(runConfigDialog.isDebug()){
			new DebugHandler().execute(runConfigDialog);
		}
		else{
			new RunJobHandler().execute(runConfigDialog);
		}
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
	
private boolean confirmationFromUser() {
		
		MessageDialog messageDialog = new MessageDialog(Display.getCurrent().getActiveShell(),Messages.CONFIRM_FOR_GRAPH_PROPS_RUN_JOB_TITLE, null,
				Messages.CONFIRM_FOR_GRAPH_PROPS_RUN_JOB, MessageDialog.QUESTION, new String[] { "Yes",
		  "No" }, 0);
		int response = messageDialog.open();
		 if(response == 0){
	        	return true;
	        } else {
	        	return false;
	        }
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

}
