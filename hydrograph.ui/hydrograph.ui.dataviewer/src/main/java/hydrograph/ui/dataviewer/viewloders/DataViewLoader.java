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

package hydrograph.ui.dataviewer.viewloders;

import hydrograph.ui.dataviewer.adapters.DataViewerAdapter;
import hydrograph.ui.dataviewer.constants.Views;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.datastructures.RowField;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;

public class DataViewLoader {

	private static final String RECORD = "Record: ";
	private static final String RECORD_SEPARATOR = "----------------------------------\n";
	private StyledText unformattedViewTextarea;
	private StyledText formattedViewTextarea;
	private TableViewer horizontalViewTableViewer;
	private TableViewer gridViewTableViewer;

	private List<RowData> gridViewData;
	private List<RowData> formattedViewData;
	private List<RowData> unformattedViewData;

	private DataViewerAdapter dataViewerAdapter;
	private CTabFolder tabFolder;

	public DataViewLoader(StyledText unformattedViewTextarea, StyledText formattedViewTextarea,
			TableViewer horizontalViewTableViewer, TableViewer gridViewTableViewer, List<RowData> gridViewData,
			List<RowData> formattedViewData, List<RowData> unformattedViewData, DataViewerAdapter dataViewerAdapter, CTabFolder tabFolder) {
		this.unformattedViewTextarea = unformattedViewTextarea;
		this.formattedViewTextarea = formattedViewTextarea;
		this.horizontalViewTableViewer = horizontalViewTableViewer;
		this.gridViewTableViewer = gridViewTableViewer;
		this.gridViewData = gridViewData;
		this.formattedViewData = formattedViewData;
		this.unformattedViewData = unformattedViewData;
		this.dataViewerAdapter = dataViewerAdapter;
		this.tabFolder = tabFolder;
	}

	/**
	 * 
	 * Set unformattedViewTextarea
	 * 
	 * @param unformattedViewTextarea
	 */
	public void setUnformattedViewTextarea(StyledText unformattedViewTextarea) {
		this.unformattedViewTextarea = unformattedViewTextarea;
	}

	/**
	 * Set gridViewTableViewer
	 * 
	 * @param gridViewTableViewer
	 */
	public void setGridViewTableViewer(TableViewer gridViewTableViewer) {
		this.gridViewTableViewer = gridViewTableViewer;
	}

	/**
	 * 
	 * Set formattedViewTextarea
	 * 
	 * @param formattedViewTextarea
	 */
	public void setFormattedViewTextarea(StyledText formattedViewTextarea) {
		this.formattedViewTextarea = formattedViewTextarea;
	}

	/**
	 * 
	 * Update data view list
	 * 
	 */
	public void updateDataViewLists() {
		gridViewData.clear();
		formattedViewData.clear();
		gridViewData.addAll(dataViewerAdapter.getFileData());
		formattedViewData.addAll(gridViewData);
		unformattedViewData = dataViewerAdapter.getFileData();
	}
	
	/**
	 * 
	 * Synchronizes other views data with grid view data.
	 * This method generally called when user sorts data in grid view.
	 * 
	 */
	public void syncOtherViewsDataWithGridViewData(){
		formattedViewData.clear();
		formattedViewData.addAll(gridViewData);
	}
	
	private int getMaxLengthColumn() {
		int lenght = 0;
		for (String columnName : dataViewerAdapter.getColumnList()) {
			if (columnName.length() > lenght) {
				lenght = columnName.length();
			}
		}
		return lenght;
	}

	/**
	 * 
	 * reload visible view with data
	 * 
	 */
	public void reloadloadViews() {
		CTabItem tabItem = tabFolder.getSelection();
		if (tabItem.getData("VIEW_NAME").equals(Views.GRID_VIEW_NAME)) {
			gridViewTableViewer.refresh();
		} else if (tabItem.getData("VIEW_NAME").equals(Views.HORIZONTAL_VIEW_NAME)) {
			//TODO - add horizontal view reload code 
		} else if (tabItem.getData("VIEW_NAME").equals(Views.FORMATTED_VIEW_NAME)) {
			reloadFormattedView();
		} else if (tabItem.getData("VIEW_NAME").equals(Views.UNFORMATTED_VIEW_NAME)) {
			reloadUnformattedView();
		}
	}

	private void reloadFormattedView() {
		formattedViewTextarea.setText("");
		StringBuilder stringBuilder = new StringBuilder();
		int maxLenghtColumn = getMaxLengthColumn();

		maxLenghtColumn += 5;
		String format = "\t\t%-" + maxLenghtColumn + "s: %s\n";

		for (RowData rowData : formattedViewData) {
			stringBuilder.append(RECORD + rowData.getRowNumber() + "\n\n");

			stringBuilder.append("{\n");
			int columnIndex = 0;
			for (String columnName : dataViewerAdapter.getColumnList()) {
				RowField columnData = rowData.getRowFields().get(columnIndex);
				String tempString = String.format(format, columnName, columnData.getValue());
				stringBuilder.append(tempString);
				columnIndex++;
			}
			stringBuilder.append("}\n");
			stringBuilder.append(RECORD_SEPARATOR);
		}
		formattedViewTextarea.setText(stringBuilder.toString());
	}

	private void reloadUnformattedView() {

		unformattedViewTextarea.setText("");
		StringBuilder stringBuilder = new StringBuilder();

		addHeaderLineToUnformattedViewTextArea(stringBuilder);

		for (RowData rowData : unformattedViewData) {
			String row = "";
			for (RowField columnData : rowData.getRowFields()) {
				row = row + columnData.getValue() + ",";
			}
			stringBuilder.append(row.substring(0, row.length() - 1) + "\n");

		}
		unformattedViewTextarea.setText(stringBuilder.toString());
	}

	private void addHeaderLineToUnformattedViewTextArea(StringBuilder stringBuilder) {
		String header = "";
		for (String columnName : dataViewerAdapter.getColumnList()) {
			header = header + columnName + ",";
		}
		stringBuilder.append(header.substring(0, header.length() - 1) + "\n");
		unformattedViewTextarea.setText(stringBuilder.toString());
	}
}
