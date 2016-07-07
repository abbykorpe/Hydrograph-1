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
import org.eclipse.swt.custom.ScrolledComposite;

/**
 * Data set Information Window
 * @author Bitwise
 */
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
	private Composite genralTabDatacomposite;
	
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
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tbtmGeneral.setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
	
		
		genralTabDatacomposite = new Composite(scrolledComposite, SWT.NONE);
		genralTabDatacomposite.setLayout(new GridLayout(2, false));
		
		createLabel(genralTabDatacomposite,DatasetInformationConstants.VIEW_DATA_FILE);
		
		setLabelValue(genralTabDatacomposite,datasetInformationDetail.getViewDataFilePath());
		
		createLabel(genralTabDatacomposite,DatasetInformationConstants.EDGE_NODE);
		
		setLabelValue(genralTabDatacomposite,datasetInformationDetail.getEdgeNode());
	
		if(jobDetails.isRemote()){
			createLabel(genralTabDatacomposite,DatasetInformationConstants.USERNAME);
		
			setLabelValue(genralTabDatacomposite,datasetInformationDetail.getUserName());
		}
		
		createLabel(genralTabDatacomposite,DatasetInformationConstants.LOCALCHUNKDATA);
		
		setLabelValue(genralTabDatacomposite,datasetInformationDetail.getChunkFilePath());
		
		createLabel(genralTabDatacomposite,DatasetInformationConstants.FILESIZE);
		
		setLabelValue(genralTabDatacomposite,datasetInformationDetail.getSizeOfData());
		
		createLabel(genralTabDatacomposite,DatasetInformationConstants.NOOFRECORDS);
		
		setLabelValue(genralTabDatacomposite,datasetInformationDetail.getNoOfRecords());
		
		createLabel(genralTabDatacomposite,DatasetInformationConstants.PAGESIZE);
		
		setLabelValue(genralTabDatacomposite,datasetInformationDetail.getPageSize());
		
		createLabel(genralTabDatacomposite,DatasetInformationConstants.DELIMETER);
		
		setLabelValue(genralTabDatacomposite,datasetInformationDetail.getDelimeter());
		
		createLabel(genralTabDatacomposite,DatasetInformationConstants.QUOTE);
		
		setLabelValue(genralTabDatacomposite,datasetInformationDetail.getQuote());
		
		createLabel(genralTabDatacomposite,DatasetInformationConstants.LOCAL_FILTER_CONDITION);
		setLabelValue(genralTabDatacomposite,datasetInformationDetail.getLocalFilter());
		 		
		createLabel(genralTabDatacomposite,DatasetInformationConstants.REMOTE_FILTER_CONDITION);
		setLabelValue(genralTabDatacomposite,datasetInformationDetail.getRemoteFilter());
		
		TabItem tbtmSchema = new TabItem(tabFolder, SWT.NONE);
		tbtmSchema.setText(DatasetInformationConstants.SCHEMA);
		
		ScrolledComposite scrolledComposite1 = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tbtmSchema.setControl(scrolledComposite1);
		scrolledComposite1.setExpandHorizontal(true);
		scrolledComposite1.setExpandVertical(true);
		
		Composite composite_3 = new Composite(scrolledComposite1, SWT.NONE);
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
		
		scrolledComposite.setContent(genralTabDatacomposite);
		scrolledComposite.setMinSize(genralTabDatacomposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		scrolledComposite1.setContent(composite_3);
		scrolledComposite1.setMinSize(composite_3.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
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
		labelValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		labelValue.setText(value);
	}

	/**
	 * Creates the label for dataset information window
	 * @param composite_2
	 */
	public void createLabel(Composite composite_2, String windowLabelName) {
		
		Label lblName = new Label(composite_2, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
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
