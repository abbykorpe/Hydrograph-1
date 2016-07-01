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
import hydrograph.ui.dataviewer.find.FindViewDataDialog;
import hydrograph.ui.dataviewer.window.DebugDataViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class FindAction extends Action{
	public boolean isOpened;
	private static final String LABEL="Find";
	private DebugDataViewer debugDataViewer;
	public FindAction(DebugDataViewer debugDataViewer) {
		super(LABEL);
		this.debugDataViewer = debugDataViewer;
		
		if (OSValidator.isWindows())
			setAccelerator(SWT.CTRL + 'f');
		if (OSValidator.isMac())
			setAccelerator(SWT.COMMAND + 'f');
	}
	@Override
	public void run() {
		if(!isOpened){
			FindViewDataDialog findViewDataDialog = new FindViewDataDialog(Display.getDefault().getActiveShell(), debugDataViewer.getTableViewer(),
					debugDataViewer.getFormattedViewTextarea(), debugDataViewer.getUnformattedViewTextarea(), debugDataViewer.getCurrentView());
			findViewDataDialog.open(this);
		}
	}
}
