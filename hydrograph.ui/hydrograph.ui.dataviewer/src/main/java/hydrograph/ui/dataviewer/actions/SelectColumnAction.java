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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;

import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.dataviewer.actions.SelectColumnActionDialog;

import org.eclipse.swt.widgets.Display;

/**
 * 
 * SelectColumnAction maintain users preferences 
 * 
 * @author Bitwise
 *
 */
public class SelectColumnAction extends Action {
	DebugDataViewer debugDataViewer;
	private List<String> allColumns;
	private List<String> selectedColumns;
	private static final String menuItem = "Select Columns";

	public SelectColumnAction(DebugDataViewer debugDataViewer) {
		super(menuItem);
		this.debugDataViewer = debugDataViewer;
		allColumns=new ArrayList<String>();
		selectedColumns= new ArrayList<String>();
	} 

	@Override
	public void run() {
		if(allColumns.size()==0 && selectedColumns.size()==0)
		allColumns.addAll(debugDataViewer.getColumnList());
		SelectColumnActionDialog sc = new SelectColumnActionDialog(Display.getDefault().getActiveShell(), this);
		if (sc.open() != 1) {
			for (int index = debugDataViewer.getDataViewerAdapter()
					.getColumnList().size(); index >= 0; index--) {
				debugDataViewer.getTableViewer().getTable().getColumns()[index]
						.dispose();
			}
			debugDataViewer.getTableViewer().refresh();
			debugDataViewer.getDataViewerAdapter().setColumnList(this.getSelectedColumns());
			TableViewer t = debugDataViewer.getTableViewer();
			debugDataViewer.createGridViewTableColumns(t);
			debugDataViewer.getDataViewLoader().reloadloadViews();
		}
		super.run();
	}
	
	/**
	 * Get list of Available Columns
	 */
	public List<String> getAllColumns() {
		return allColumns;
	}
	
	/**
	 * set list of Available Columns
	 */
	public void setAllColumns(List<String> allColumns) {
		this.allColumns = allColumns;
	}
	
	/**
	 * Get list of Selected Columns
	 */
	public List<String> getSelectedColumns() {
		return selectedColumns;
	}
	
	/**
	 * Set list of Available Columns
	 */
	public void setSelectedColumns(List<String> selectedColumns) {
		this.selectedColumns = selectedColumns;
	}
}
