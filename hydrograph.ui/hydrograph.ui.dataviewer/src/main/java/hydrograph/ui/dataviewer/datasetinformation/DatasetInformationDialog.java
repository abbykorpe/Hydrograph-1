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
package hydrograph.ui.dataviewer.datasetinformation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.help.ViewContextComputer;

import hydrograph.ui.common.schema.Field;
import hydrograph.ui.common.schema.Fields;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.dataviewer.utilities.ViewDataSchemaHelper;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.utility.GridWidgetCommonBuilder;

public class DatasetInformationDialog extends Dialog {
	
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_6;
	private Text text_7;
	
	private Table table;
	DatasetInformationDetail datasetInformationDetail = new DatasetInformationDetail();
	ViewDataSchemaHelper viewDataSchemaHelper;
	private List<GridRow> gridRowList=new ArrayList<>();
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	
	public DatasetInformationDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL | SWT.RESIZE);
		
	}
	
	/**
	 * Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Dataset Information");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmGeneral = new TabItem(tabFolder, SWT.NONE);
		tbtmGeneral.setText("General");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmGeneral.setControl(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFilePathOfRemoteRun = new Label(composite_2, SWT.NONE);
		lblFilePathOfRemoteRun.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilePathOfRemoteRun.setText(" View Data File Path on Remote Run");
		
		
		text = new Text(composite_2, SWT.BORDER );
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setEditable(false);
		text.setText(datasetInformationDetail.getRemoteFilePath());
		
		
		Label lblFilePathOfLocalRun = new Label(composite_2, SWT.NONE);
		lblFilePathOfLocalRun.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilePathOfLocalRun.setText(" View Data File Path on Local Run");
		
		text_1 = new Text(composite_2, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_1.setEditable(false);
		text_1.setText(datasetInformationDetail.getLocalFilePath());
		
		Label lblFilePathOfChunks = new Label(composite_2, SWT.NONE);
		lblFilePathOfChunks.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilePathOfChunks.setText("View Data File Path of Chunks Downloaded");
		
		text_2 = new Text(composite_2, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_2.setEditable(false);
		text_2.setText(datasetInformationDetail.getChunkFilePath());
		
		Label lblSizeOfData = new Label(composite_2, SWT.NONE);
		lblSizeOfData.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSizeOfData.setText("View Data File Size(MB)");
		
		text_3 = new Text(composite_2, SWT.BORDER);
		text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		text_3.setEditable(false);
		text_3.setText(datasetInformationDetail.getSizeOfData());
		
		Label lblNoOfRecords = new Label(composite_2, SWT.NONE);
		lblNoOfRecords.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNoOfRecords.setText("No of Records in View Data File");
		
		
		text_5 = new Text(composite_2, SWT.BORDER);
		text_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		text_5.setEditable(false);
		text_5.setText(datasetInformationDetail.getNoOfRecords());
		
		
		Label lblPageSize = new Label(composite_2, SWT.NONE);
		lblPageSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPageSize.setText("View Data File Page Size");
		
		text_4 = new Text(composite_2, SWT.BORDER);
		text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		text_4.setEditable(false);
		text_4.setText(datasetInformationDetail.getPageSize());
		
		Label lblDelimiter = new Label(composite_2, SWT.NONE);
		lblDelimiter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDelimiter.setText("Delimiter");
		
		text_6 = new Text(composite_2, SWT.BORDER);
		text_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		text_6.setEditable(false);
		text_6.setText(datasetInformationDetail.getDelimeter());
		
		Label lblQuote = new Label(composite_2, SWT.NONE);
		lblQuote.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQuote.setText("Quote");
		
		text_7 = new Text(composite_2, SWT.BORDER);
		text_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		text_7.setEditable(false);
		text_7.setText(datasetInformationDetail.getQuote());
		
		TabItem tbtmSchema = new TabItem(tabFolder, SWT.NONE);
		tbtmSchema.setText("Schema");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmSchema.setControl(composite_3);
		composite_3.setLayout(new GridLayout(1, false));
		
		TableViewer tableViewer = new TableViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem = tableViewerColumn.getColumn();
		tblclmnItem.setWidth(100);
		tblclmnItem.setText("Field Name");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider());
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_1 = tableViewerColumn_1.getColumn();
		tblclmnItem_1.setWidth(100);
		tblclmnItem_1.setText("Data Type");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_2 = tableViewerColumn_2.getColumn();
		tblclmnItem_2.setWidth(100);
		tblclmnItem_2.setText("Date Format");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_3 = tableViewerColumn_3.getColumn();
		tblclmnItem_3.setWidth(100);
		tblclmnItem_3.setText("Precision");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_4 = tableViewerColumn_4.getColumn();
		tblclmnItem_4.setWidth(100);
		tblclmnItem_4.setText("Scale");
		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_5 = tableViewerColumn_5.getColumn();
		tblclmnItem_5.setWidth(100);
		tblclmnItem_5.setText("Scale Type");
		
		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_6 = tableViewerColumn_6.getColumn();
		tblclmnItem_6.setWidth(100);
		tblclmnItem_6.setText("Description");
		
		loadSchemaData();
		tableViewer.setContentProvider(new DatasetContentProvider());
		tableViewer.setLabelProvider(new DatasetLabelProvider());
		tableViewer.setInput(gridRowList);
		tableViewer.refresh();
		
		return container;
	}

	public void loadSchemaData() {
		String schemaFilePath="C:\\Users\\ashikah\\Desktop\\input.xml";
		Fields fields = ViewDataSchemaHelper.INSTANCE.getFieldsFromSchema(schemaFilePath);
		for(Field field : fields.getField()){
			GridRow gridRow=new GridRow();
			gridRow.setFieldName(field.getName());
			gridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(field.getType().value()));
			gridRow.setDataTypeValue(GridWidgetCommonBuilder.getDataTypeValue()[GridWidgetCommonBuilder.getDataTypeByValue(field.getType().value())]);
			
			if(field.getFormat()!=null){
				gridRow.setDateFormat(field.getFormat());
			}else{
				gridRow.setDateFormat("");
			}
			if(field.getPrecision()!= null){
				gridRow.setPrecision(Integer.toString(field.getPrecision()));
			}
			else{
				gridRow.setPrecision("ABC");
			}
			if(gridRow.getScale() != null){
				gridRow.setScale(Integer.toString(field.getScale()));
			}
			else{
				gridRow.setScale("");
			}
			
			if(field.getDescription()!=null)
				gridRow.setDescription(field.getDescription());
			else{
				gridRow.setDescription("");
			}
			if(field.getScaleType()!=null){
				gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(field.getScaleType().value()));	
				gridRow.setScaleTypeValue(GridWidgetCommonBuilder.getScaleTypeValue()[GridWidgetCommonBuilder.getScaleTypeByValue(field.getScaleType().value())]);
			}else{
				gridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(Messages.SCALE_TYPE_NONE));
				gridRow.setScaleTypeValue(GridWidgetCommonBuilder.getScaleTypeValue()[Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]);
			}
			
			gridRowList.add(gridRow);
		}
	}
	

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(736, 419);
	}

	public void setData(DatasetInformationDetail datasetInformationDetail) {
		this.datasetInformationDetail=datasetInformationDetail;
	}
}
