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

import hydrograph.ui.common.util.ConvertHexValues;
import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.datastructures.ColumnData;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.preferances.ViewDataPreferences;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import org.slf4j.Logger;

import com.opencsv.CSVWriter;

/**
 * This class is responsible for exporting watcher data
 * 
 * @author Bitwise
 *
 */
public class ExportAction extends Action {
	private DebugDataViewer debugDataViewer;
	private Boolean isIncludeHeadersChecked;
	private String delimiter;
	private String quoteCharactor;
	private static final String EXPORT_FILE = "Export File";
	private static final String PLUGIN_NAME = "hydrograph.ui.dataviewer";
	private static final String EXPORT_DATA_DEFAULT_PATH = "exportDataDefaultpath";
	private static final String DEFAULT = "default";
	private static final String DEFAILT_FILE_NAME = "export_data.csv";
	private static final String INFORMATION = "Information";
	private static final String ERROR = "Error";
	private String ERROR_MESSAGE = "File is open. Please close it to replace it.";
	private Logger logger = LogFactory.INSTANCE.getLogger(ExportAction.class);
	private static final String LABEL = "Export Data";

	public ExportAction(DebugDataViewer debugDataViewer) {
		super(LABEL);
		this.debugDataViewer = debugDataViewer;
	}

	@Override
	public void run() {
		ViewDataPreferences viewDataPreferences = debugDataViewer.getViewDataPreferences();
		delimiter = viewDataPreferences.getDelimiter();
		quoteCharactor = viewDataPreferences.getQuoteCharactor();
		isIncludeHeadersChecked = viewDataPreferences.getIncludeHeaders();
		TableViewer tableViewer = debugDataViewer.getTableViewer();
		List<RowData> eachRowData = getListOfRowData(tableViewer);
		List<String[]> exportedfileDataList = new ArrayList<String[]>();
		TableColumn[] columns = tableViewer.getTable().getColumns();
		if (isIncludeHeadersChecked != null) {
			addHeadersInList(tableViewer, exportedfileDataList, columns);
		}
		addRowsDataInList(tableViewer, eachRowData, exportedfileDataList);
		FileDialog fileDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
		String filePath = getPathOfFileDialog(fileDialog);
		try {
			writeDataInFile(exportedfileDataList, filePath);
		} catch (IOException e) {
			logger.error("Error occur while writing data in csv File" + e.getMessage());
		}
	}

	private void showMessage(String message, String messageBoxTitle, int icon) {
		MessageBox messageBox = new MessageBox(new Shell(), SWT.OK | icon);
		messageBox.setText(messageBoxTitle);
		messageBox.setMessage(message);
		int response = messageBox.open();
		if (response == SWT.OK) {
		}
	}

	private void writeDataInFile(List<String[]> fileDataList, String filePath) throws IOException {
		CSVWriter writer = null;
		FileWriter fileWriter = null;
		try {
			if (filePath != null) {
				if (StringUtils.length(ConvertHexValues.parseHex(delimiter)) == 1
						&& StringUtils.length(ConvertHexValues.parseHex(quoteCharactor)) == 1) {
					fileWriter = new FileWriter(filePath);
					writer = new CSVWriter(fileWriter, ConvertHexValues.parseHex(delimiter).toCharArray()[0],
							ConvertHexValues.parseHex(quoteCharactor).toCharArray()[0]);
					writer.writeAll(fileDataList, false);
					showMessage("Data exported to " + filePath + " successfully.", INFORMATION, SWT.ICON_INFORMATION);
				}
			}
		} catch (IOException e1) {
			showMessage(ERROR_MESSAGE, ERROR, SWT.ICON_ERROR);
		} finally {
			if (fileWriter != null) {
				fileWriter.close();
			}
			if (writer != null) {
				writer.close();
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

	private void addRowsDataInList(TableViewer tableViewer, List<RowData> rowDataList, List<String[]> fileDataList) {
		for (RowData rowData : rowDataList) {
			List<ColumnData> columnDataList = rowData.getColumns();
			String[] eachRowData = new String[tableViewer.getTable().getColumnCount() - 1];
			for (int j = 0; j < columnDataList.size(); j++) {
				eachRowData[j] = columnDataList.get(j).getValue();
			}
			fileDataList.add(eachRowData);
		}
	}

	private void addHeadersInList(TableViewer tableViewer, List<String[]> fileDataList, TableColumn[] columns) {
		if (isIncludeHeadersChecked) {
			String[] tablecolumns = new String[tableViewer.getTable().getColumnCount() - 1];
			for (int k = 0; k < columns.length - 1; k++) {
				tablecolumns[k] = columns[k + 1].getText();
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
