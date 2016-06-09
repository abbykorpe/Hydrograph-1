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

import hydrograph.ui.dataviewer.adapters.CSVAdapter;
import hydrograph.ui.dataviewer.constants.Views;
import hydrograph.ui.dataviewer.datastructures.ColumnData;
import hydrograph.ui.dataviewer.datastructures.RowData;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;

public class DataViewLoader {

	private StyledText unformattedViewTextarea;
	private StyledText formattedViewTextarea;
	private TableViewer horizontalViewTableViewer;
	private TableViewer gridViewTableViewer;

	private List<RowData> gridViewData;
	private List<RowData> formattedViewData;
	private List<RowData> unformattedViewData;

	private CSVAdapter csvAdapter;
	private CTabFolder tabFolder;

	public DataViewLoader(StyledText unformattedViewTextarea, StyledText formattedViewTextarea,
			TableViewer horizontalViewTableViewer, TableViewer gridViewTableViewer, List<RowData> gridViewData,
			List<RowData> formattedViewData, List<RowData> unformattedViewData, CSVAdapter csvAdapter, CTabFolder tabFolder) {
		super();
		this.unformattedViewTextarea = unformattedViewTextarea;
		this.formattedViewTextarea = formattedViewTextarea;
		this.horizontalViewTableViewer = horizontalViewTableViewer;
		this.gridViewTableViewer = gridViewTableViewer;
		this.gridViewData = gridViewData;
		this.formattedViewData = formattedViewData;
		this.unformattedViewData = unformattedViewData;
		this.csvAdapter = csvAdapter;
		this.tabFolder = tabFolder;
	}

	public void setUnformattedViewTextarea(StyledText unformattedViewTextarea) {
		this.unformattedViewTextarea = unformattedViewTextarea;
	}

	public void setHorizontalViewTableViewer(TableViewer horizontalViewTableViewer) {
		this.horizontalViewTableViewer = horizontalViewTableViewer;
	}

	public void setGridViewTableViewer(TableViewer gridViewTableViewer) {
		this.gridViewTableViewer = gridViewTableViewer;
	}

	public void setFormattedViewTextarea(StyledText formattedViewTextarea) {
		this.formattedViewTextarea = formattedViewTextarea;
	}

	public void updateDataViewLists() {
		gridViewData.clear();
		gridViewData.addAll(csvAdapter.getTableData());
		formattedViewData = csvAdapter.getTableData();
		unformattedViewData = csvAdapter.getTableData();
	}

	private int getMaxLengthColumn() {

		int lenght = 0;

		for (String columnName : csvAdapter.getColumnList()) {
			if (columnName.length() > lenght) {
				lenght = columnName.length();
			}
		}

		return lenght;
	}

	public void reloadloadViews() {
		CTabItem tabItem = tabFolder.getSelection();
		if (tabItem.getData("VIEW_NAME").equals(Views.GRID_VIEW_NAME)) {
			gridViewTableViewer.refresh();
		} else if (tabItem.getData("VIEW_NAME").equals(Views.HORIZONTAL_VIEW_NAME)) {

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
			stringBuilder.append("Record: " + rowData.getRowNumber() + "\n\n");

			stringBuilder.append("{\n");
			int columnIndex = 0;
			for (String columnName : csvAdapter.getColumnList()) {
				ColumnData columnData = rowData.getColumns().get(columnIndex);
				String tempString = String.format(format, columnName, columnData.getValue());
				stringBuilder.append(tempString);
				columnIndex++;
			}
			stringBuilder.append("}\n");
			stringBuilder.append("----------------------------------\n");
		}
		formattedViewTextarea.setText(stringBuilder.toString());
	}

	private void reloadUnformattedView() {

		unformattedViewTextarea.setText("");
		StringBuilder stringBuilder = new StringBuilder();

		String header = "";
		for (String columnName : csvAdapter.getColumnList()) {
			header = header + columnName + ",";
		}
		stringBuilder.append(header.substring(0, header.length() - 1) + "\n");
		unformattedViewTextarea.setText(stringBuilder.toString());

		for (RowData rowData : unformattedViewData) {
			String row = "";
			for (ColumnData columnData : rowData.getColumns()) {
				row = row + columnData.getValue() + ",";
			}
			stringBuilder.append(row.substring(0, row.length() - 1) + "\n");

		}
		unformattedViewTextarea.setText(stringBuilder.toString());
	}
}
