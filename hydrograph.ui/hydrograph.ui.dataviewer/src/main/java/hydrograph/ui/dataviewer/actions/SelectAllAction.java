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

package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.dataviewer.window.DebugDataViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
/**
 * 
 * 
 * Select all action 
 * 
 * @author Bitwise
 *
 */
public class SelectAllAction extends Action{
	
	private static final String LABEL="Select All";
	private DebugDataViewer debugDataViewer;
	
	public SelectAllAction(DebugDataViewer debugDataViewer) {
    	super(LABEL);
    	this.debugDataViewer = debugDataViewer;
		if (OSValidator.isWindows())
			setAccelerator(SWT.CTRL + 'a');
		if (OSValidator.isMac())
			setAccelerator(SWT.COMMAND + 'a');
	}
	
	@Override
	public void run() {
		if (debugDataViewer.getUnformattedViewTextarea()!=null && debugDataViewer.getUnformattedViewTextarea().isVisible())
			debugDataViewer.getUnformattedViewTextarea().selectAll();
		else if (debugDataViewer.getFormattedViewTextarea()!=null && debugDataViewer.getFormattedViewTextarea().isVisible())
			debugDataViewer.getFormattedViewTextarea().selectAll();
		else
			selectAll();
	}

	
	// This method is used to mark all rows of advance data viewer as selected.
	public void selectAll() {
		Table table = debugDataViewer.getTableViewer().getTable();
		table.setSelection(0, table.getItemCount() - 1);
	}
	
}
