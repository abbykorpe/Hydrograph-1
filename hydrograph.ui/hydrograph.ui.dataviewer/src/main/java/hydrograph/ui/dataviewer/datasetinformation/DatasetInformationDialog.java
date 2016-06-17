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

import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.common.schema.Field;
import hydrograph.ui.common.schema.Fields;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.dataviewer.constants.DatasetInformationConstants;
import hydrograph.ui.dataviewer.filemanager.DataViewerFileManager;
import hydrograph.ui.dataviewer.utilities.ViewDataSchemaHelper;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.utility.GridWidgetCommonBuilder;

public class DatasetInformationDialog extends Dialog {
	
	
	private Table table;
	DatasetInformationDetail datasetInformationDetail;
	DebugDataViewer debugDataViewer;
	ViewDataSchemaHelper viewDataSchemaHelper;
	private JobDetails jobDetails;
	private String debugFileLocation;
	private String debugFileName;
	private Fields dataViewerFileSchema;
	private String SCHEMA_FILE_EXTENTION=".xml";
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
		newShell.setText("Dataset Information - " + debugDataViewer.getDataViewerWindowTitle());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		container.getShell().setMinimumSize(700, 300);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmGeneral = new TabItem(tabFolder, SWT.NONE);
		tbtmGeneral.setText(DatasetInformationConstants.GENERAL);
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmGeneral.setControl(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFilePath = new Label(composite_2, SWT.NONE);
		lblFilePath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilePath.setText(DatasetInformationConstants.VIEW_DATA_FILE);
		lblFilePath.setAlignment(SWT.RIGHT);
		
		Label labelPath = new Label(composite_2, SWT.NONE |SWT.READ_ONLY);
		labelPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		labelPath.setText(datasetInformationDetail.getViewDataFilePath());
		
		
		Label lblEdgeNode = new Label(composite_2, SWT.NONE);
		lblEdgeNode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEdgeNode.setText(DatasetInformationConstants.EDGE_NODE);
		lblEdgeNode.setAlignment(SWT.RIGHT);
		
		Label labelNode = new Label(composite_2, SWT.NONE |SWT.READ_ONLY);
		labelNode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		labelNode.setText(datasetInformationDetail.getEdgeNode());
		
		if(jobDetails.isRemote()){
		Label lblUserName = new Label(composite_2, SWT.NONE);
		lblUserName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUserName.setText(DatasetInformationConstants.USERNAME);
		lblUserName.setAlignment(SWT.RIGHT);
		
		Label lblUserName1 = new Label(composite_2, SWT.NONE |SWT.READ_ONLY);
		lblUserName1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblUserName1.setText(datasetInformationDetail.getUserName());
		}
		
		Label lblFilePathOfChunks = new Label(composite_2, SWT.NONE);
		lblFilePathOfChunks.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilePathOfChunks.setText(DatasetInformationConstants.LOCALCHUNKDATA);
		lblFilePathOfChunks.setAlignment(SWT.RIGHT);
		
		Label labelChunk = new Label(composite_2, SWT.NONE |SWT.READ_ONLY);
		labelChunk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		labelChunk.setText(datasetInformationDetail.getChunkFilePath());
		
		Label lblData = new Label(composite_2, SWT.NONE);
		lblData.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblData.setText(DatasetInformationConstants.FILESIZE);
		lblData.setAlignment(SWT.RIGHT);
		
		Label labelSizeOfData = new Label(composite_2, SWT.NONE |SWT.READ_ONLY);
		labelSizeOfData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		labelSizeOfData.setText(datasetInformationDetail.getSizeOfData());
		
		Label lblRecords = new Label(composite_2, SWT.NONE);
		lblRecords.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRecords.setText(DatasetInformationConstants.NOOFRECORDS);
		lblRecords.setAlignment(SWT.RIGHT);
		
		
		Label labelNoOfRecords = new Label(composite_2, SWT.NONE |SWT.READ_ONLY);
		labelNoOfRecords.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		labelNoOfRecords.setText(datasetInformationDetail.getNoOfRecords());
		
		
		Label lblPage = new Label(composite_2, SWT.NONE);
		lblPage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPage.setText(DatasetInformationConstants.PAGESIZE);
		lblPage.setAlignment(SWT.RIGHT);
		
		Label labelPageSize = new Label(composite_2, SWT.NONE |SWT.READ_ONLY);
		labelPageSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		labelPageSize.setText(datasetInformationDetail.getPageSize());
		
		Label lblDelimiter = new Label(composite_2, SWT.NONE);
		lblDelimiter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDelimiter.setText(DatasetInformationConstants.DELIMETER);
		lblDelimiter.setAlignment(SWT.RIGHT);
		
		Label labelDelimeter1 = new Label(composite_2, SWT.NONE |SWT.READ_ONLY);
		labelDelimeter1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		labelDelimeter1.setText(datasetInformationDetail.getDelimeter());
		
		Label lblQuote = new Label(composite_2, SWT.NONE);
		lblQuote.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQuote.setText(DatasetInformationConstants.QUOTE);
		lblQuote.setAlignment(SWT.RIGHT);
		
		Label labelQuote1 = new Label(composite_2, SWT.NONE |SWT.READ_ONLY);
		labelQuote1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		labelQuote1.setText(datasetInformationDetail.getQuote());
	
		
		TabItem tbtmSchema = new TabItem(tabFolder, SWT.NONE);
		tbtmSchema.setText(DatasetInformationConstants.SCHEMA);
		
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
		tblclmnItem.setText(DatasetInformationConstants.FEILDNAME);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider());
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_1 = tableViewerColumn_1.getColumn();
		tblclmnItem_1.setWidth(100);
		tblclmnItem_1.setText(DatasetInformationConstants.DATATYPE);
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_2 = tableViewerColumn_2.getColumn();
		tblclmnItem_2.setWidth(100);
		tblclmnItem_2.setText(DatasetInformationConstants.DATEFORMAT);
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_3 = tableViewerColumn_3.getColumn();
		tblclmnItem_3.setWidth(100);
		tblclmnItem_3.setText(DatasetInformationConstants.PRECISION);
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_4 = tableViewerColumn_4.getColumn();
		tblclmnItem_4.setWidth(100);
		tblclmnItem_4.setText(DatasetInformationConstants.SCALE);
		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_5 = tableViewerColumn_5.getColumn();
		tblclmnItem_5.setWidth(100);
		tblclmnItem_5.setText(DatasetInformationConstants.SCALETYPE);
		
		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem_6 = tableViewerColumn_6.getColumn();
		tblclmnItem_6.setWidth(100);
		tblclmnItem_6.setText(DatasetInformationConstants.DESCRIPTION);
		
		loadSchemaData();
		tableViewer.setContentProvider(new DatasetContentProvider());
		tableViewer.setLabelProvider(new DatasetLabelProvider());
		tableViewer.setInput(gridRowList);
		tableViewer.refresh();
		
		return container;
	}

	public void loadSchemaData() {
		
			jobDetails = debugDataViewer.getJobDetails();
			debugFileName = debugDataViewer.getDebugFileName();
	 		debugFileLocation = debugDataViewer.getDebugFileLocation();
	 		
		dataViewerFileSchema = ViewDataSchemaHelper.INSTANCE.getFieldsFromSchema(debugFileLocation + debugFileName +SCHEMA_FILE_EXTENTION);
		for(Field field : dataViewerFileSchema.getField()){
			GridRow gridRow=new GridRow();
			
			gridRow.setFieldName(field.getName());
			gridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(field.getType().value()));
			gridRow.setDataTypeValue(field.getType().value());
			
			if(field.getFormat()!=null){
				gridRow.setDateFormat(field.getFormat());
			}else{
				gridRow.setDateFormat("");
			}
			if(field.getPrecision()!= null){
				gridRow.setPrecision(String.valueOf(field.getPrecision()));
			}
			else{
				gridRow.setPrecision("");
			}
			if(field.getScale()!= null){
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
		return new Point(683, 323);
	}

	public void setData(DatasetInformationDetail datasetInformationDetail,DebugDataViewer debugDataViewer,JobDetails jobDetails) {
		this.debugDataViewer = debugDataViewer;
		this.datasetInformationDetail=datasetInformationDetail;
		this.jobDetails = jobDetails;
	}
}
