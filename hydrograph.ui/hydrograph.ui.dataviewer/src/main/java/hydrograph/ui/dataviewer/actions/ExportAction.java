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

import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.datastructures.ColumnData;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.preferances.ViewDataPreferences;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.opencsv.CSVWriter;

public class ExportAction extends Action {
	private DebugDataViewer debugDataViewer;
	private Boolean header;
	private String delimiter;
	private String quoteCharactor;
	private static final String EXPORT_FILE="Export File";
	private static final String PLUGIN_NAME="hydrograph.ui.dataviewer";
    private static final String EXPORT_DATA_DEFAULT_PATH="exportDataDefaultpath";
    private static final String DEFAULT="default";
    private static final String DEFAILT_FILE_NAME="export_data.csv";
    private static final String INFORMATION="Information";
	
    private static final String LABEL="Export";
    
    public ExportAction(DebugDataViewer debugDataViewer) {
    	super(LABEL);
    	this.debugDataViewer = debugDataViewer;
	}
    
	
	@Override
	public void run() {
		ViewDataPreferences viewDataPreferences = debugDataViewer.getViewDataPreferences();
		delimiter = viewDataPreferences.getDelimiter();
		quoteCharactor = viewDataPreferences.getQuoteCharactor();
		header = viewDataPreferences.getIncludeHeaders();
		TableViewer tableViewer = debugDataViewer.getTableViewer();
		List<RowData> eachRowData = getListOfRowData(tableViewer);
		List<String[]> exportedfileDataList = new ArrayList<String[]>();
		TableColumn[] columns = tableViewer.getTable().getColumns();
		if (header != null) {
			addHeadersInList(tableViewer, exportedfileDataList, columns);
		}
		addRowsDataInList(tableViewer, eachRowData, exportedfileDataList);
		FileDialog fileDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
		String filePath = getPathOfFileDialog(fileDialog);
		writeDataInFile(exportedfileDataList, filePath);
	}

	private void showInformationMessage(String filePath) {
		MessageBox messageBox = new MessageBox(new Shell(), SWT.OK| SWT.ICON_INFORMATION);
		messageBox.setText(INFORMATION);
		messageBox.setMessage("Data exported to " + filePath + " successfully.");
		int response=messageBox.open();
		if(response==SWT.OK)
		{
		}
	}

	private void writeDataInFile(List<String[]> fileDataList, String filePath) {
		CSVWriter writer;
		try {
			FileWriter fileWriter=new FileWriter(filePath);
			writer = new CSVWriter(fileWriter, delimiter.toCharArray()[0],quoteCharactor.toCharArray()[0]);
			writer.writeAll(fileDataList, false);
			writer.close();
			showInformationMessage(filePath);
		} catch (IOException e1) {
			MessageBox messageBox = new MessageBox(new Shell(), SWT.OK| SWT.ICON_ERROR);
			messageBox.setText("Error");
			messageBox.setMessage("File already is in use.Please close it to replace it.");
			int response=messageBox.open();
			if(response==SWT.OK)
			{
			}
		}
	}

	private String getPathOfFileDialog(FileDialog fileDialog) {
		fileDialog.setText(EXPORT_FILE);
		String exportDataDefaultpath = readExportDataDefaultPathFromFile();
		if (!exportDataDefaultpath.isEmpty()) {
			fileDialog.setFilterPath(exportDataDefaultpath);
		}
		fileDialog.setFileName(DEFAILT_FILE_NAME);
		fileDialog.setOverwrite(true);
		String filePath = fileDialog.open();
		return filePath;
	}

	private String readExportDataDefaultPathFromFile() {
		IScopeContext context = new InstanceScope();
		IEclipsePreferences eclipsePreferences = context.getNode(PLUGIN_NAME);
		String exportDataDefaultpath = eclipsePreferences.get(EXPORT_DATA_DEFAULT_PATH, DEFAULT);
		exportDataDefaultpath = exportDataDefaultpath.equalsIgnoreCase(DEFAULT) ? " " : exportDataDefaultpath;
		return exportDataDefaultpath;
	}

	private void addRowsDataInList(TableViewer tableViewer, List<RowData> eachRowData, List<String[]> fileDataList) {
		for (RowData rowData : eachRowData) {
			List<ColumnData> columnDatas = rowData.getColumns();
			String[] eachrowdata = new String[tableViewer.getTable().getColumnCount()];
			for (int j = 0; j < columnDatas.size(); j++) {
				eachrowdata[j] = columnDatas.get(j).getValue();
			}
			fileDataList.add(eachrowdata);
		}
	}

	private void addHeadersInList(TableViewer tableViewer, List<String[]> fileDataList, TableColumn[] columns) {
		if (header) {
			String[] tablecolumns = new String[tableViewer.getTable().getColumnCount()];
			for (int k = 0; k < columns.length-1; k++) {
				tablecolumns[k] = columns[k+1].getText();
			}
			fileDataList.add(tablecolumns);
		}
	}

	private List<RowData> getListOfRowData(TableViewer tableViewer) {
		TableItem[] items = tableViewer.getTable().getItems();
		int i = 1;
		List<RowData> eachRowData = new ArrayList<RowData>();
		for (int index = 0; index < items.length; index++) {
			List<ColumnData> columnData = new ArrayList<ColumnData>();
			for (int j = 1; j < tableViewer.getTable().getColumnCount(); j++) {
				columnData.add(new ColumnData(items[index].getText(j), null));
			}
			RowData rowData = new RowData(columnData, i);
			eachRowData.add(rowData);
			i++;
		}
		return eachRowData;
	}
	
}
