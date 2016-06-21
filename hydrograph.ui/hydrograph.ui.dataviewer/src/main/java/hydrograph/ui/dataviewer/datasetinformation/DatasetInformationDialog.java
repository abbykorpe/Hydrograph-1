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
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import hydrograph.ui.common.datastructures.dataviewer.JobDetails;
import hydrograph.ui.common.schema.Field;
import hydrograph.ui.common.schema.Fields;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.dataviewer.constants.DatasetInformationConstants;
import hydrograph.ui.dataviewer.utilities.ViewDataSchemaHelper;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.utility.GridWidgetCommonBuilder;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

public class DatasetInformationDialog extends Dialog {
	
	
	private Table table;
	private DatasetInformationDetail datasetInformationDetail;
	private DebugDataViewer debugDataViewer;
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
		
		
		final Composite container = (Composite) super.createDialogArea(parent);
		
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
		
		createLabel(composite_2,DatasetInformationConstants.VIEW_DATA_FILE);
		
		setLabelValue(composite_2,datasetInformationDetail.getViewDataFilePath());
		
		createLabel(composite_2,DatasetInformationConstants.EDGE_NODE);
		
		setLabelValue(composite_2,datasetInformationDetail.getEdgeNode());
	
		if(jobDetails.isRemote()){
			createLabel(composite_2,DatasetInformationConstants.USERNAME);
		
			setLabelValue(composite_2,datasetInformationDetail.getUserName());
		}
		
		createLabel(composite_2,DatasetInformationConstants.LOCALCHUNKDATA);
		
		setLabelValue(composite_2,datasetInformationDetail.getChunkFilePath());
		
		createLabel(composite_2,DatasetInformationConstants.FILESIZE);
		
		setLabelValue(composite_2,datasetInformationDetail.getSizeOfData());
		
		createLabel(composite_2,DatasetInformationConstants.NOOFRECORDS);
		
		setLabelValue(composite_2,datasetInformationDetail.getNoOfRecords());
		
		createLabel(composite_2,DatasetInformationConstants.PAGESIZE);
		
		setLabelValue(composite_2,datasetInformationDetail.getPageSize());
		
		createLabel(composite_2,DatasetInformationConstants.DELIMETER);
		
		setLabelValue(composite_2,datasetInformationDetail.getDelimeter());
		
		createLabel(composite_2,DatasetInformationConstants.QUOTE);
		
		setLabelValue(composite_2,datasetInformationDetail.getQuote());
		
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
		
		
		createTableViewerColumns(tableViewer,DatasetInformationConstants.FEILDNAME);
		
		createTableViewerColumns(tableViewer,DatasetInformationConstants.DATATYPE);
		
		createTableViewerColumns(tableViewer,DatasetInformationConstants.DATEFORMAT);
		
		createTableViewerColumns(tableViewer,DatasetInformationConstants.PRECISION);
	
		createTableViewerColumns(tableViewer,DatasetInformationConstants.SCALE);
		
		createTableViewerColumns(tableViewer,DatasetInformationConstants.SCALETYPE);
		
		final TableViewerColumn tableViewerColumn=createTableViewerColumns(tableViewer,DatasetInformationConstants.DESCRIPTION);
		

		container.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				tableViewerColumn.getColumn().setWidth(container.getSize().x-642);
			}
		});
		
		loadSchemaData();
		tableViewer.setContentProvider(new DatasetContentProvider());
		tableViewer.setLabelProvider(new DatasetLabelProvider());
		tableViewer.setInput(gridRowList);
		tableViewer.refresh();
		
		return container;
	}

	/**
	 * Creates columns for the Schema Grid
	 * @param tableViewer
	 */
	public TableViewerColumn createTableViewerColumns(TableViewer tableViewer, String columnName) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnItem = tableViewerColumn.getColumn();
		tblclmnItem.setWidth(100);
		tblclmnItem.setText(columnName);
		return tableViewerColumn;
	}

	/**
	 * Set the values of the dataset information window for the respective  labels
	 * @param composite_2
	 */
	public void setLabelValue(Composite composite_2, String value) {
		Label labelValue= new Label(composite_2, SWT.NONE |SWT.READ_ONLY);
		labelValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		labelValue.setText(value);
	}

	/**
	 * Creates the label for dataset information window
	 * @param composite_2
	 */
	public void createLabel(Composite composite_2, String windowLabelName) {
		Label lblName = new Label(composite_2, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText(windowLabelName);
		lblName.setAlignment(SWT.RIGHT);
	}

	/**
	 * Set the values of schema file in schema grid
	 */
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
			
			if(StringUtils.isNotEmpty(field.getFormat())){
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
			
			if(StringUtils.isNotEmpty(field.getDescription()))
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
		return new Point(721, 323);
	}

	/**
	 * Set the objects for the dataset information window
	 * @param datasetInformationDetail,debugDataViewer,jobDetails
	 */
	public void setData(DatasetInformationDetail datasetInformationDetail,DebugDataViewer debugDataViewer,JobDetails jobDetails) {
		this.debugDataViewer = debugDataViewer;
		this.datasetInformationDetail=datasetInformationDetail;
		this.jobDetails = jobDetails;
	}
}
