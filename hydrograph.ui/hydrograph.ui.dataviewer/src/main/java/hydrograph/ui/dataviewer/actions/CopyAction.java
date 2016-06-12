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
import hydrograph.ui.dataviewer.datastructures.RowField;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.window.DebugDataViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;

/**
 * 
 * Copy action
 * 
 * @author Bitwise
 *
 */
public class CopyAction extends Action {
	
	private static final String LABEL="Copy";
	private DebugDataViewer debugDataViewer;
	
	public CopyAction(DebugDataViewer debugDataViewer) {
		super(LABEL);
		this.debugDataViewer = debugDataViewer;
		if (OSValidator.isWindows())
			setAccelerator(SWT.CTRL + 'c');
		if (OSValidator.isMac())
			setAccelerator(SWT.COMMAND + 'c');
	}
	@Override
	public void run() {
		if (debugDataViewer.getUnformattedViewTextarea()!=null && debugDataViewer.getUnformattedViewTextarea().isVisible())
			debugDataViewer.getUnformattedViewTextarea().copy();
		else if (debugDataViewer.getFormattedViewTextarea()!=null && debugDataViewer.getFormattedViewTextarea().isVisible())
			debugDataViewer.getFormattedViewTextarea().copy();
		else
			copySelectedAsTabDelimited();
	}
	
	// This method is used to copy all selected rows as tab delimited
	private void copySelectedAsTabDelimited() {
		StringBuffer stringBuffer = new StringBuffer();
		for (Item item : debugDataViewer.getTableViewer().getTable().getSelection()) {
			RowData rowData = (RowData) item.getData();
			for (RowField columnData : rowData.getFields()) {
				stringBuffer.append(columnData.getValue() + "\t");
			}
			stringBuffer.append("\n");
		}
		Clipboard cb = new Clipboard(Display.getCurrent());
		TextTransfer textTransfer = TextTransfer.getInstance();
		String textData = stringBuffer.toString();
		cb.setContents(new Object[] { textData }, new Transfer[] { textTransfer });
		cb.dispose();
	}
	
}
