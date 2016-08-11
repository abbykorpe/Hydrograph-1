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

package hydrograph.ui.propertywindow.widgets.customwidgets.operational;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.ImagePathConstant;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.common.util.ParameterUtil;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.expression.ExpressionEditorData;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.datastructure.property.mapping.InputField;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.utils.SWTResourceManager;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.mapping.tables.inputtable.InputFieldColumnLabelProvider;
import hydrograph.ui.propertywindow.widgets.customwidgets.mapping.tables.inputtable.TableContentProvider;
import hydrograph.ui.propertywindow.widgets.filterproperty.ELTCellModifier;
import hydrograph.ui.propertywindow.widgets.filterproperty.ELTFilterContentProvider;
import hydrograph.ui.propertywindow.widgets.filterproperty.ELTFilterLabelProvider;
import hydrograph.ui.propertywindow.widgets.filterproperty.ErrorLabelProvider;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTSWTWidgets;
import hydrograph.ui.propertywindow.widgets.interfaces.IOperationClassDialog;
import hydrograph.ui.propertywindow.widgets.utility.DragDropUtility;
import hydrograph.ui.propertywindow.widgets.utility.SchemaButtonsSyncUtility;
import hydrograph.ui.propertywindow.widgets.utility.SchemaSyncUtility;
import hydrograph.ui.propertywindow.widgets.utility.TransformMappingFeatureUtility;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;
import java.awt.Dimension;
import java.awt.Toolkit;
import hydrograph.ui.propertywindow.transform.viewdata.TransformViewDataDialog;
import hydrograph.ui.propertywindow.widgets.customwidgets.operational.ExpressionComposite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Bitwise
 *
 */
public class TransformDialog extends Dialog implements IOperationClassDialog {

	private static final String OPERATION_ID_TEXT_BOX2 = "OperationIDTextBox";
	private static final String PLEASE_SELECT_PARAMETER_FIELD_S_ONLY = "Please select Parameter field(s) only";
	private static final String OUTPUT_FIELD = "OutputField";
	private static final String EXPRESSION_TEXT_BOX = "expressionTextBox";
	private static final String OUTPUT_DELETE_BUTTON = "outputDeleteButton";
	private static final String OUTPUT_ADD_BUTTON = "outputAddButton";
	private static final String OPERATION_OUTPUT_FIELD_TABLE_VIEWER = "operationOutputFieldTableViewer";
	private static final String INPUT_DELETE_BUTTON = "inputDeletButton";
	private static final String INPUT_ADD_BUTTON = "inputAddButton";
	private static final String OPERATION_INPUT_FIELD_TABLE_VIEWER = "operationInputFieldTableViewer";
	private static final String OPERATION_ID_TEXT_BOX = "operationIDTextBox";
	private static final String OPERATION_CLASS_TEXT_BOX = "operationClassTextBox";
	private static final String PARAMETER_TEXT_BOX = "parameterTextBox";
	private static final String BTN_NEW_BUTTON = "btnNewButton";
	private static final String IS_PARAM="isParam"; 
	private static final String OUTPUT_TABLE_VIEWER="OutputTableViewer";
	
	
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */

	private Composite container;
	private CellEditor[] editors;
	private ExpandBar expandBar = null;
	private ELTSWTWidgets widget = new ELTSWTWidgets();
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private Text operationClassTextBox;
	private Text operationIDTextBox;
	private boolean isYesButtonPressed;
	private boolean isNoButtonPressed;
	private boolean cancelPressed;
	private boolean okPressed;
	private Table tableViewerTable;
	private Component component;
	private WidgetConfig widgetConfig;
	private Text text;
	private Button isParam;
	private TableViewer operationalInputFieldTableViewer;
	private TableViewer operationalOutputFieldTableViewer;
	private Button operationInputaddButton;
	private Button operationInputDeleteButton;
	private ScrolledComposite scrolledComposite;
	private TableViewer inputFieldTableViewer;
	private TableViewer mappingTableViewer;
	private TransformMapping transformMapping;
	private TableViewer outputFieldViewer;
	private Map<String,List<FilterProperties>> temporaryOutputFieldMap;
	private MappingSheetRow mappingSheetRow;
	private Label errorLabel;
	private boolean isOperationInputFieldDuplicate;
	private TransformDialog transformDialog;
	private List<Label> errorLabelList;
	private TableViewer errorTableViewer;
	private Composite errorComposite;
	private Map<String,List<String>> duplicateOperationInputFieldMap;
	private Map<String,List<String>> duplicateFieldMap;
	private ControlDecoration isFieldNameAlphanumericDecorator;
	private ControlDecoration fieldNameDecorator;
	private SashForm mainSashForm;
	private SashForm middleSashForm;
	private Integer windowButtonWidth = 30;
	private Integer windowButtonHeight = 25;
	private Integer macButtonWidth = 40;
	private Integer macButtonHeight = 30;
	private ExpressionOrOperationClassSelectDialog expressionOrOperationClassSelectDialog;
	private boolean optionToSelect;
	private List<FilterProperties> finalSortedList;
	private Set<Integer> outputFieldIndices = new LinkedHashSet<Integer>();
	private Shell parentShell;
	private Button deleteLabel;
	private Button addLabel;
	private Button viewTransform;
	/**
    * @param parentShell
    * @param component
    * @param widgetConfig
    * @param atMapping
    */
	public TransformDialog(Shell parentShell, Component component, WidgetConfig widgetConfig, TransformMapping atMapping) {

		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL);
		this.transformMapping = atMapping;
		isYesButtonPressed = false;
		isNoButtonPressed = false;
		this.component = component;
		this.widgetConfig = widgetConfig;
		this.parentShell = parentShell;
		this.transformDialog = this;
		temporaryOutputFieldMap=new HashMap<String,List<FilterProperties>>();
		errorLabelList=new ArrayList<>();
		duplicateOperationInputFieldMap=new HashMap<String,List<String>>();
		duplicateFieldMap=new HashMap<String,List<String>>();
		if(Constants.TRANSFORM.equalsIgnoreCase(component.getComponentName()))
		optionToSelect=true;
	}  

	/**
	 * @wbp.parser.constructor
	 */

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(3, false));
		container.getShell().setText(Messages.TRANSFORM_EDITOR);
		propertyDialogButtonBar = new PropertyDialogButtonBar(container);
		mainSashForm = new SashForm(container, SWT.SMOOTH);
		mainSashForm.setSashWidth(1);
		mainSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 0));
		createInputFieldTable(mainSashForm);
        createOperationClassGrid(mainSashForm);
		createOutputFieldTable(mainSashForm);
		mainSashForm.setWeights(new int[] {71, 242, 87});
		return mainSashForm;
	}

	private void createInputFieldTable(Composite container) {

		Composite inputFieldComposite = new Composite(container, SWT.NONE);
		
		inputFieldComposite.setLayout(new GridLayout(2, false));

		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite.widthHint = 250;
		inputFieldComposite.setLayoutData(gd_composite);
		new Label(inputFieldComposite, SWT.NONE);
		inputFieldTableViewer = new TableViewer(inputFieldComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);

		setTableViewer(inputFieldTableViewer, inputFieldComposite, new String[] { Messages.OPERATIONAL_SYSTEM_FIELD },
				new TableContentProvider(), new OperationLabelProvider());
		inputFieldTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		DragDropUtility.INSTANCE.applyDragFromTableViewer(inputFieldTableViewer.getTable());
		inputFieldTableViewer.setLabelProvider(new InputFieldColumnLabelProvider());
		inputFieldTableViewer.setInput(transformMapping.getInputFields());
        addControlListener(inputFieldTableViewer.getTable());
        inputFieldTableViewer.getControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) 
			{
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(null,
						inputFieldTableViewer,outputFieldViewer,null,null);
  			}
		});
	}

	private void createOutputFieldTable(Composite composite) {

		Composite outputComposite = new Composite(composite, SWT.NONE);
		outputComposite.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gridData.widthHint = 250;
		outputComposite.setLayoutData(gridData);

		Composite buttonComposite = new Composite(outputComposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(3, false));
		buttonComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));

		Composite outputFieldComposite = new Composite(outputComposite, SWT.NONE);
		outputFieldComposite.setLayout(new GridLayout(1, false));
		outputFieldComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button btnPull = new Button(buttonComposite, SWT.NONE);
		btnPull.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog dialog = new MessageDialog(new Shell(), Constants.SYNC_CONFIRM, null, Constants.SYNC_CONFIRM_MESSAGE, MessageDialog.QUESTION, new String[] {"OK", "Cancel" }, 0);
				int dialogResult =dialog.open();
				if(dialogResult == 0){
					syncTransformFieldsWithSchema();
				}
			}
		});
		btnPull.setBounds(20, 10, 20, 20);
		Image pullButtonImage = new Image(null,XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.PULL_BUTTON);
		btnPull.setImage(pullButtonImage);
		btnPull.setToolTipText("Pull Schema");
	
	
		Button addLabel = widget.buttonWidget(buttonComposite, SWT.CENTER, new int[] { 60, 3, 20, 15 }, "");
		Image addImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON);
		addLabel.setImage(addImage);
		SchemaButtonsSyncUtility.INSTANCE.buttonSize(addLabel,macButtonWidth,macButtonHeight,windowButtonWidth,windowButtonHeight);
		addLabel.setToolTipText(Messages.ADD_SCHEMA_TOOLTIP);
		addLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				FilterProperties filterproperties = new FilterProperties();
				filterproperties.setPropertyname("");
					transformMapping.getOutputFieldList().add(filterproperties);
					((List<FilterProperties>)outputFieldViewer.getInput()).add(filterproperties);
					outputFieldViewer.refresh();
					int i = ((List<FilterProperties>)outputFieldViewer.getInput()).size() == 0 ? ((List<FilterProperties>)outputFieldViewer.getInput()).size()
							:((List<FilterProperties>)outputFieldViewer.getInput()).size() - 1;
					outputFieldViewer.editElement(outputFieldViewer.getElementAt(i), 0);
			}

		});
		
		
		
		Button deletLabel = widget.buttonWidget(buttonComposite, SWT.CENTER, new int[] { 160, 10, 20, 15 }, "");
		Image deleteImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON);
		deletLabel.setImage(deleteImage);
		SchemaButtonsSyncUtility.INSTANCE.buttonSize(deletLabel,macButtonWidth,macButtonHeight,windowButtonWidth,windowButtonHeight);
		deletLabel.setToolTipText(Messages.DELETE_SCHEMA_TOOLTIP);
		deletLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				Table table = outputFieldViewer.getTable();
				int temp = table.getSelectionIndex();
				int[] indexs = table.getSelectionIndices();
				if (temp == -1) 
				{
				WidgetUtility.errorMessage(Messages.SelectRowToDelete);
                } 
				else 
				{
					boolean isParameter=true;
					List<FilterProperties> fieldsToBeDelete = new ArrayList<FilterProperties >();
					for (int index : indexs) {
						fieldsToBeDelete.add(((List<FilterProperties>)outputFieldViewer.getInput()).get(index));
					}
					for(FilterProperties filterProperties:fieldsToBeDelete)
					{
						if(!ParameterUtil.isParameter(filterProperties.getPropertyname()))
						{
							isParameter=false;
							break;
						}	
					}
					if(!isParameter)
					{
					 WidgetUtility.errorMessage
				   (PLEASE_SELECT_PARAMETER_FIELD_S_ONLY);
					}	
					else
					{	
					table.remove(indexs);
					for(FilterProperties filterProperties:fieldsToBeDelete)
					{		
						transformMapping.getOutputFieldList().remove(filterProperties);
					}
					}
					refreshOutputTable();
            	}
			}

		});
		

		outputFieldViewer = new TableViewer(outputFieldComposite, SWT.BORDER | SWT.FULL_SELECTION|SWT.MULTI);
		setTableViewer(outputFieldViewer, outputFieldComposite, new String[] { Messages.OUTPUT_FIELD },
				new ELTFilterContentProvider(), new OperationLabelProvider());
		outputFieldViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		outputFieldViewer.setCellModifier(new ELTCellModifier(outputFieldViewer,transformDialog));
		outputFieldViewer.setLabelProvider(new ELTFilterLabelProvider());
		refreshOutputTable();
		setIsOperationInputFieldDuplicate();
		showHideValidationMessage();
		outputFieldViewer.getControl().addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseUp(MouseEvent e) {
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(null,
						inputFieldTableViewer,outputFieldViewer,null,null);
			}
		 });
		addControlListener(outputFieldViewer.getTable());
	}
   
	private void addListenerForRowHighlighting(
			ExpressionComposite expressionComposite ) {
		expressionComposite.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				Composite composite=(Composite)e.widget;
				ExpressionComposite expressionComposite=(ExpressionComposite)composite;
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(expressionComposite.getExpressionIdTextBox(),
					inputFieldTableViewer,outputFieldViewer,transformMapping,finalSortedList);
			}
		});
    	expressionComposite.getExpressionIdTextBox().getParent().addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				Composite composite=(Composite)e.widget;
				ExpressionComposite expressionComposite=(ExpressionComposite)composite.getParent();
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(expressionComposite.getExpressionIdTextBox(),
						inputFieldTableViewer,outputFieldViewer,transformMapping,finalSortedList);
				
			}
		});
    	expressionComposite.getTableViewer().getControl().addMouseListener(new MouseAdapter() 
    	{
    		public void mouseUp(MouseEvent e) {
				Composite composite=(Composite)e.widget;
				ExpressionComposite expressionComposite=(ExpressionComposite)composite.getParent().getParent();
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(expressionComposite.getExpressionIdTextBox(),
						inputFieldTableViewer,outputFieldViewer,transformMapping,finalSortedList);
				
			}
		});
    	expressionComposite.getOutputFieldTextBox().getParent().addMouseListener(new MouseAdapter() 
    	{
    		public void mouseUp(MouseEvent e) {
				Composite composite=(Composite)e.widget;
				ExpressionComposite expressionComposite=(ExpressionComposite)composite.getParent();
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(expressionComposite.getExpressionIdTextBox(),
						inputFieldTableViewer,outputFieldViewer,transformMapping,finalSortedList);
				
			}
		});
    	expressionComposite.getAddButton().getParent().addMouseListener(new MouseAdapter() 
    	{
    		public void mouseUp(MouseEvent e) {
				Composite composite=(Composite)e.widget;
				ExpressionComposite expressionComposite=(ExpressionComposite)composite.getParent().getParent();
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(expressionComposite.getExpressionIdTextBox(),
						inputFieldTableViewer,outputFieldViewer,transformMapping,finalSortedList);
				
			}
		});
	}
	
	
	private void addControlListener(Table table) {
		table.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Table table = (Table) e.widget;
				Rectangle area = table.getClientArea();
				int totalAreaWidth = area.width;
				int diff = totalAreaWidth - (table.getColumn(0).getWidth());
				table.getColumn(0).setWidth(diff + table.getColumn(0).getWidth());
			}
		});
	}

	private void createOperationClassGrid(Composite parentComposite) {

		Composite middleComposite = new Composite(parentComposite, SWT.NONE);
		middleComposite.setLayout(new GridLayout(1, false));
		Composite topAddButtonComposite = new Composite(middleComposite, SWT.NONE);
		GridData gd_topAddButtonComposite = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_topAddButtonComposite.heightHint = 40;
		topAddButtonComposite.setLayoutData(gd_topAddButtonComposite);
		
		middleSashForm = new SashForm(middleComposite, SWT.SMOOTH|SWT.VERTICAL|SWT.BORDER);
		middleSashForm.setSashWidth(1);
		middleSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 0));

		scrolledComposite = new ScrolledComposite(middleSashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		scrolledComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scrolledComposite.setLayout(new GridLayout(1, false));

		GridData gd_scrolledComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_scrolledComposite.minimumHeight = 170;
		gd_scrolledComposite.heightHint = 200;
		scrolledComposite.setLayoutData(gd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setVisible(true);

		expandBar = new ExpandBar(scrolledComposite, SWT.NONE);
		expandBar.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		expandBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		expandBar.setVisible(true);
		expandBar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		
		
		addLabel = widget.buttonWidget(topAddButtonComposite, SWT.CENTER, new int[] { 130, 10, 20, 15 }, "");
		Image addImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON);
		addLabel.setImage(addImage);
		SchemaButtonsSyncUtility.INSTANCE.buttonSize(addLabel,43,30,30,25);
		addLabel.setToolTipText(Messages.ADD_OPERATION_CONTROL);
		
		addLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(Constants.TRANSFORM.equalsIgnoreCase(component.getComponentName()))
				{	
				createOperationOrExpressionSelectDialog();	
				if(expressionOrOperationClassSelectDialog.isOkPressed())
				{	
					optionToSelect=expressionOrOperationClassSelectDialog.getOptionToSelect();	
					addOperations();
				}
				}
				else
				addOperations();	
				
			}
		});
		
		if(OSValidator.isMac()){
			deleteLabel = widget.buttonWidget(topAddButtonComposite, SWT.CENTER, new int[] { 165, 11, 20, 15 },"");
		}else{
			deleteLabel = widget.buttonWidget(topAddButtonComposite, SWT.CENTER, new int[] { 165, 10, 20, 15 },"");
		}
		Image deleteImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON);
		SchemaButtonsSyncUtility.INSTANCE.buttonSize(deleteLabel,44,28,30,25);
		deleteLabel.setImage(deleteImage);
		deleteLabel.setToolTipText(Messages.DELETE_OPERATION_CONTROL);
		deleteLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (transformMapping.getMappingSheetRows().isEmpty()) {
					WidgetUtility.errorMessage(Messages.OPERATION_LIST_EMPTY);

				} else {
					OperationClassDeleteDialog operationClassDeleteDialog = new OperationClassDeleteDialog(deleteLabel
							.getShell(), transformMapping, expandBar);
					operationClassDeleteDialog.open();
					refreshOutputTable();
					showHideValidationMessage();
					

				}
				scrolledComposite.setMinSize(expandBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});

		Label lblOperationsControl = new Label(topAddButtonComposite, SWT.NONE);
		if(OSValidator.isMac()){
			lblOperationsControl.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));
			lblOperationsControl.setBounds(0, 15, 129, 25);
		}else {
			lblOperationsControl.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
			lblOperationsControl.setBounds(0, 10, 129, 25);
		}
		lblOperationsControl.setText(Messages.OPERATION_CONTROL);
		
		if(OSValidator.isMac()){
			viewTransform = widget.buttonWidget(topAddButtonComposite, SWT.CENTER, new int[] {205,10,95,15}, "View Transform");
			viewTransform.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		} else{
			viewTransform = widget.buttonWidget(topAddButtonComposite, SWT.CENTER, new int[] {200,10,95,15}, "View Transform");
			viewTransform.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		}
		SchemaButtonsSyncUtility.INSTANCE.buttonSize(viewTransform,105,29,95,25);
		viewTransform.addSelectionListener(new SelectionAdapter() {
			@Override
			public void  widgetSelected(SelectionEvent e) {
				TransformViewDataDialog transformViewDataDialog = new TransformViewDataDialog(parentShell);
				transformViewDataDialog.getMappingSheet(transformMapping.getMappingSheetRows(),transformMapping.getMapAndPassthroughField());
				transformViewDataDialog.open();
			}
			
		});
		
		if (!transformMapping.getMappingSheetRows().isEmpty()) {
			for (MappingSheetRow mappingSheetRow : transformMapping.getMappingSheetRows()) {
				 optionToSelect=mappingSheetRow.isExpression();
				addExpandItem(scrolledComposite, mappingSheetRow, mappingSheetRow.getOperationID());
				setDuplicateOperationInputFieldMap(mappingSheetRow);
				 if(Constants.NORMALIZE.equalsIgnoreCase(component.getComponentName())){
						addLabel.setEnabled(false);
						deleteLabel.setEnabled(false);
					}	
			}

		}else if(Constants.NORMALIZE.equalsIgnoreCase(component.getComponentName())){
			addOperations();
			addLabel.setEnabled(false);
			deleteLabel.setEnabled(false);
		}
		createMapAndPassthroughTable(middleSashForm);
		middleSashForm.setWeights(new int[] {56, 54, 23});

	}
    
	private void createOperationOrExpressionSelectDialog() {
		expressionOrOperationClassSelectDialog=new ExpressionOrOperationClassSelectDialog(this.getShell(),optionToSelect);
		expressionOrOperationClassSelectDialog.open();
	}
	
	
	private void createMapAndPassthroughTable(Composite middleComposite) {
		Composite mappingTableComposite = new Composite(middleComposite, SWT.NONE);
		mappingTableComposite.setLayout(new GridLayout(2, false));
		GridData gd_mappingTableComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 10, 1);
		gd_mappingTableComposite.minimumHeight=180;
		
		mappingTableComposite.setLayoutData(gd_mappingTableComposite);

		Composite labelComposite = new Composite(mappingTableComposite, SWT.NONE);
		labelComposite.setLayout(new GridLayout(1, false));
		GridData gd_mappingTableComposite3 = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		labelComposite.setLayoutData(gd_mappingTableComposite3);

		Composite buttonComposite = new Composite(mappingTableComposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(2, true));
		GridData gd_mappingTableComposite2 = new GridData(SWT.END, SWT.END, false, false, 1, 1);
		buttonComposite.setLayoutData(gd_mappingTableComposite2);

		Composite tableComposite = new Composite(mappingTableComposite, SWT.NONE);
		tableComposite.setLayout(new GridLayout(1, false));
		GridData gd_mappingTableComposite1 = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		tableComposite.setLayoutData(gd_mappingTableComposite1);
		
		transformMapping.getMapAndPassthroughField();

		mappingTableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		setTableViewer(mappingTableViewer, tableComposite, new String[] { Messages.SOURCE, Messages.TARGET },
				new ELTFilterContentProvider(), new OperationLabelProvider());
		mappingTableViewer.setLabelProvider(new PropertyLabelProvider());
		mappingTableViewer.setCellModifier(new PropertyGridCellModifier(this, mappingTableViewer));
		mappingTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		mappingTableViewer.setInput(transformMapping.getMapAndPassthroughField());
        mappingTableViewer.getControl().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(null,
						inputFieldTableViewer,outputFieldViewer,null,null);
			}
		});
		CellEditor[] editor=mappingTableViewer.getCellEditors();
		
		for(int i=0;i<=1;i++)
		{	
		fieldNameDecorator = WidgetUtility.addDecorator(editor[i].getControl(),Messages.FIELDNAME_SHOULD_NOT_BE_BLANK);
		isFieldNameAlphanumericDecorator=WidgetUtility.addDecorator(editor[i].getControl(),Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);	
		editor[i].setValidator(new TransformCellEditorFieldValidator(fieldNameDecorator,isFieldNameAlphanumericDecorator));
		if(i==0)
		{	
		isFieldNameAlphanumericDecorator.setMarginWidth(8);
		fieldNameDecorator.setMarginWidth(8);
		}
		}
		
		mappingTableViewer.getTable().addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Table table = (Table) e.widget;
				int columnCount = table.getColumnCount();

				Rectangle area = table.getClientArea();
				int totalAreaWidth = area.width;
				table.getColumn(0).setWidth(area.width / 2);
				int lineWidth = table.getGridLineWidth();
				int totalGridLineWidth = (2 - 1) * lineWidth;
				int totalColumnWidth = 0;
				for (TableColumn column : table.getColumns()) {
					totalColumnWidth = totalColumnWidth + column.getWidth();
				}
				int diff = totalAreaWidth - (totalColumnWidth + totalGridLineWidth);

				TableColumn lastCol = table.getColumns()[columnCount - 1];
				lastCol.setWidth(diff + lastCol.getWidth());

			}
		});

		DragDropUtility.INSTANCE.applyDrop(mappingTableViewer,
				new DragDropTransformOpImp(this, transformMapping,null, false,false,
						mappingTableViewer));

		Label lblNewLabel = new Label(labelComposite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblNewLabel.setText(Messages.MAP_FIELD);
	

		Button mapFieldAddLabel = widget.buttonWidget(buttonComposite, SWT.CENTER, new int[] { 635, 10, 20, 15 }, "");
		Image addImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON);
		mapFieldAddLabel.setImage(addImage);
		mapFieldAddLabel.setToolTipText(Messages.ADD_SCHEMA_TOOLTIP);
		mapFieldAddLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				NameValueProperty nameValueProperty = new NameValueProperty();
				nameValueProperty.setPropertyName("");
				nameValueProperty.setPropertyValue("");
				if (!transformMapping.getMapAndPassthroughField().contains(nameValueProperty)) {
					transformMapping.getMapAndPassthroughField().add(nameValueProperty);
					mappingTableViewer.refresh();
					int currentSize = transformMapping.getMapAndPassthroughField().size();
					int i = currentSize == 0 ? currentSize : currentSize - 1;
					mappingTableViewer.editElement(mappingTableViewer.getElementAt(i), 0);
					component.setLatestChangesInSchema(false);
				}

			}
		});

		Button mapFieldDeletLabel = widget.buttonWidget(buttonComposite, SWT.CENTER, new int[] { 665, 10, 20, 15 }, "");
		Image deleteImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON);
		mapFieldDeletLabel.setImage(deleteImage);
		mapFieldDeletLabel.setToolTipText(Messages.DELETE_SCHEMA_TOOLTIP);

		mapFieldDeletLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				Table table = mappingTableViewer.getTable();
				int temp = table.getSelectionIndex();
				int[] indexs = table.getSelectionIndices();
				if (temp == -1) {
					WidgetUtility.errorMessage(Messages.SelectRowToDelete);
				} 
				else 
				{
					table.remove(indexs);
					List<NameValueProperty> tempList = new ArrayList<NameValueProperty >();
					for (int index : indexs) {
						tempList.add(transformMapping.getMapAndPassthroughField().get(index));
					}
					
					for(NameValueProperty nameValueProperty:tempList)
					{	
						int index=-1;	
						for(int j=0;j<transformMapping.getOutputFieldList().size();j++)
						{
							
							if(transformMapping.getOutputFieldList().get(j)==nameValueProperty.getFilterProperty())
							{
								index=j;
								break;
							}	
						}
						if(index!=-1)
						transformMapping.getOutputFieldList().remove(index);
						transformMapping.getMapAndPassthroughField().remove(nameValueProperty);
					}
					refreshOutputTable();
					showHideValidationMessage();
				}
			}
		});

		mappingTableViewer.getTable().getColumn(0).setWidth(362);
		mappingTableViewer.getTable().getColumn(1).setWidth(362);

		errorComposite = new Composite(middleComposite, SWT.NONE);
		errorComposite.setLayout(new GridLayout(1, false));
		GridData errorCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		errorCompositeData.minimumHeight=60;
		
		
		errorComposite.setLayoutData(errorCompositeData);

		errorTableViewer = new TableViewer(errorComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		setTableViewer(errorTableViewer, errorComposite, new String[] { "Error Log" }, new ELTFilterContentProvider(),
				new OperationLabelProvider());
		errorTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		errorTableViewer.setLabelProvider(new ErrorLabelProvider());
		errorTableViewer.setInput(errorLabelList);
		errorTableViewer.getControl().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(null,
						inputFieldTableViewer,outputFieldViewer,null,null);
			}
		});
        addControlListener(errorTableViewer.getTable());
	}

	private void addExpandItem(final ScrolledComposite scrollBarComposite, MappingSheetRow mappingSheetRow, String operationID) 
	{
    	ExpandItem expandItem = new ExpandItem(expandBar, SWT.V_SCROLL);
		expandItem.setExpanded(true);
		expandItem.setHeight(230);
		expandBar.addListener(SWT.MouseUp, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				scrollBarComposite.setMinSize(expandBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		if (operationID != null)
			expandItem.setText(operationID);
    if(mappingSheetRow.isExpression())
    {	
        ExpressionComposite expressionComposite=new ExpressionComposite(expandBar,SWT.NONE,mappingSheetRow,getComponent()); 
    	expandItem.setControl(expressionComposite);
    	expressionComposite.getExpressionIdTextBox().setText(expandItem.getText());
    	expressionComposite.getExpressionIdTextBox().setData(expandItem);
    	expandItem.setData(expressionComposite.getExpressionIdTextBox());
    	expandItem.setData(IS_PARAM,expressionComposite.getIsParamButton());
    	expandItem.setData(EXPRESSION_TEXT_BOX,expressionComposite.getExressionTextBox());
    	expandItem.setData(OUTPUT_FIELD,expressionComposite.getOutputFieldTextBox());
    	addListenerForRowHighlighting(expressionComposite);
    	addModifyListenerToOperationClassAndExpressionTextBox(expressionComposite.getExressionTextBox());
    	attachModifyListenerToIdTextBox(mappingSheetRow, expressionComposite.getExpressionIdTextBox());
    	attachFocusListenerToIdTextBox(expressionComposite.getExpressionIdTextBox());
    	addIsParamSelectionListener(expressionComposite.getIsParamButton(), mappingSheetRow);
    	operationalInputFieldTableViewer=expressionComposite.getTableViewer();
    	operationalInputFieldTableViewer = setTableViewer(operationalInputFieldTableViewer,
    			 operationalInputFieldTableViewer.getControl().getParent(), new String[] {Messages.INNER_OPERATION_INPUT_FIELD },
				new ELTFilterContentProvider(), new OperationLabelProvider());
    	 operationalInputFieldTableViewer.getTable().getColumn(0).setWidth(145);
    	 operationalInputFieldTableViewer.setInput(mappingSheetRow.getInputFields());
    	 operationalInputFieldTableViewer.setCellModifier(new ELTCellModifier(operationalInputFieldTableViewer, this,mappingSheetRow));
    	 operationalInputFieldTableViewer.setLabelProvider(new ELTFilterLabelProvider());
    	 attachFocuListenerToParamaterTextBox(expressionComposite.getParameterTextBox());
    	 addModifyAndFocusListenerToOutputField(expressionComposite.getOutputFieldTextBox(),mappingSheetRow);
         DragDropTransformOpImp dragDropTransformOpImpnew = new DragDropTransformOpImp(this,transformMapping,mappingSheetRow.getInputFields(),true,true,operationalInputFieldTableViewer);
 		 DragDropUtility.INSTANCE.applyDrop(operationalInputFieldTableViewer, dragDropTransformOpImpnew);
 		 intializeFunctionalityToExpressionWidget(expressionComposite,mappingSheetRow,operationalInputFieldTableViewer);
 		 showHideValidationMessage();
 	
     }
    else
    {
    	Composite expandItemComposite = new Composite(expandBar, SWT.NONE);
		expandItemComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		expandItemComposite.setVisible(true);
		expandItem.setControl(expandItemComposite);
		expandItemComposite.setLayout(new GridLayout(3, false));
        operationalInputFieldTableViewer = createOperationInputFieldTable(expandItemComposite, mappingSheetRow);
		createMiddleWidgets(expandItemComposite, expandItem, mappingSheetRow);
		TableViewer operationOTableViewer=createOperationOutputFieldTable(expandItemComposite, mappingSheetRow);
        expandItem.setData(OUTPUT_TABLE_VIEWER, operationOTableViewer);
    }
       
        scrollBarComposite.setContent(expandBar);
		scrollBarComposite.setMinSize(expandBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	private void intializeFunctionalityToExpressionWidget(ExpressionComposite expressionComposite,MappingSheetRow mappingSheetRow,TableViewer operationalInputFieldTableViewer)
	{
		Button addButton=expressionComposite.getAddButton();
		addButtonListener(mappingSheetRow,operationalInputFieldTableViewer,addButton);
		Button deleteButton=expressionComposite.getDeletButton();
		deleteButtonListener(mappingSheetRow, operationalInputFieldTableViewer, deleteButton);
	}
	
	private void createMiddleWidgets(Composite expandItemComposite, ExpandItem expandItem,
			final MappingSheetRow mappingSheetRow) {

		Composite innerComposite = new Composite(expandItemComposite, SWT.NONE);
		GridData gd_fileSelectComposite = new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1);
		gd_fileSelectComposite.heightHint = 29;
		gd_fileSelectComposite.widthHint = 360;
		innerComposite.setLayoutData(gd_fileSelectComposite);
        
		Label operationIDLabel = new Label(innerComposite, SWT.NONE);
		operationIDLabel.setBounds(24, 28, 74, 30);
		operationIDLabel.setText(Messages.OPERATION_ID);
		
		operationIDTextBox = new Text(innerComposite, SWT.BORDER);
		operationIDTextBox.setBounds(104, 28, 150, 21);
		operationIDTextBox.setText(expandItem.getText());
        operationIDTextBox.setData(expandItem);
		expandItem.setData(operationIDTextBox);
		attachModifyListenerToIdTextBox(mappingSheetRow,operationIDTextBox);
		attachFocusListenerToIdTextBox(operationIDTextBox);
		operationIDTextBox.getParent().getParent().setData(OPERATION_ID_TEXT_BOX2,operationIDTextBox);
        operationIDTextBox.getParent().addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) 
			{
				Composite composite=(Composite)e.widget;
				Text text=(Text)composite.getParent().getData(OPERATION_ID_TEXT_BOX2);
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(text,
						inputFieldTableViewer,outputFieldViewer,transformMapping,finalSortedList);
			}
		});
       
		Label operationClassLabel = new Label(innerComposite, SWT.NONE);
		operationClassLabel.setBounds(24, 94, 62, 35);
		operationClassLabel.setText(Messages.OP_CLASS);
		
		operationClassTextBox = new Text(innerComposite, SWT.BORDER);
		operationClassTextBox.setBounds(104, 91, 150, 21);
		operationClassTextBox.setEditable(false);
		addModifyListenerToOperationClassAndExpressionTextBox(operationClassTextBox);
		
		expandItem.setData(OPERATION_CLASS_TEXT_BOX,operationClassTextBox);
		mappingSheetRow.setOperationID(operationIDTextBox.getText());
		
		final Button browseButton = new Button(innerComposite, SWT.NONE);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		browseButton.setBounds(284, 91, 35, 21);
		browseButton.setText("...");
		browseButton.setData(Messages.MAPPING_SHEET,mappingSheetRow);
		browseButton.setData(OPERATION_CLASS_TEXT_BOX, operationClassTextBox);
		browseButton.addSelectionListener(new SelectionAdapter() {
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				MappingSheetRow orignalMappingSheetRow = (MappingSheetRow) ((Button) e.widget).getData(Messages.MAPPING_SHEET);
				Text operationClassTextBox=(Text)((Button)e.widget).getData(OPERATION_CLASS_TEXT_BOX);
				MappingSheetRow oldMappingSheetRow = (MappingSheetRow) orignalMappingSheetRow.clone();
				OperationClassDialog operationClassDialog = new OperationClassDialog(browseButton.getShell(), component
						.getComponentName(), orignalMappingSheetRow, propertyDialogButtonBar, widgetConfig,
						transformDialog);
				operationClassDialog.open();
				operationClassTextBox.setText(operationClassDialog.getMappingSheetRow().getOperationClassPath());
				orignalMappingSheetRow.setComboBoxValue(operationClassDialog.getMappingSheetRow().getComboBoxValue());
				orignalMappingSheetRow.setOperationClassPath(operationClassDialog.getMappingSheetRow()
						.getOperationClassPath());
				orignalMappingSheetRow.setClassParameter(operationClassDialog.getMappingSheetRow().isClassParameter());
               
				orignalMappingSheetRow.setOperationClassFullPath(operationClassDialog.getMappingSheetRow()
						.getOperationClassFullPath());
				if (operationClassDialog.isCancelPressed() && (!(operationClassDialog.isApplyPressed()))) {
					orignalMappingSheetRow.setNameValueProperty(oldMappingSheetRow.getNameValueProperty());
				}
				if (operationClassDialog.isNoPressed())
					pressCancel();
				if (operationClassDialog.isYesPressed())
					pressOK();
				super.widgetSelected(e);
			}

		});

		Label lblParameter = new Label(innerComposite, SWT.NONE);
		lblParameter.setBounds(24, 157, 55, 15);
		lblParameter.setText(Messages.PARAMETER_LABEL);

		text = new Text(innerComposite, SWT.BORDER);
		text.setBounds(104, 151, 150, 21);
		text.setEnabled(mappingSheetRow.isWholeOperationParameter());
		if (mappingSheetRow.getWholeOperationParameterValue() != null)
			text.setText(mappingSheetRow.getWholeOperationParameterValue());

		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text t = (Text) e.widget;
				mappingSheetRow.setWholeOperationParameterValue(t.getText());

			}
		});
		
		attachFocuListenerToParamaterTextBox(text);

		isParam = new Button(innerComposite, SWT.CHECK);
		isParam.setData(PARAMETER_TEXT_BOX, text);
		isParam.setData(OPERATION_CLASS_TEXT_BOX, operationClassTextBox);
		isParam.setData(OPERATION_ID_TEXT_BOX, operationIDTextBox);
		isParam.setData(BTN_NEW_BUTTON, browseButton);
		isParam.setData(OPERATION_INPUT_FIELD_TABLE_VIEWER, operationalInputFieldTableViewer);
		isParam.setData(OPERATION_OUTPUT_FIELD_TABLE_VIEWER, operationalOutputFieldTableViewer);
		isParam.setData(INPUT_ADD_BUTTON, operationInputaddButton);
		isParam.setData(INPUT_DELETE_BUTTON, operationInputDeleteButton);
		isParam.setSelection(mappingSheetRow.isWholeOperationParameter());

		isParam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				Button text = (Button) e.widget;
				Text parameterTextBox = (Text) text.getData(PARAMETER_TEXT_BOX);
				TableViewer operationInputFieldTableViewer = (TableViewer) text
						.getData(OPERATION_INPUT_FIELD_TABLE_VIEWER);
				TableViewer operationOutputFieldTableViewer = (TableViewer) text
						.getData(OPERATION_OUTPUT_FIELD_TABLE_VIEWER);
				
				Text operationClassTextBox = (Text) text.getData(OPERATION_CLASS_TEXT_BOX);
				Text operationIDTextBox = (Text) text.getData(OPERATION_ID_TEXT_BOX);
				Button btnNewButton = (Button) text.getData(BTN_NEW_BUTTON);
				Button inputAdd = (Button) text.getData(INPUT_ADD_BUTTON);
				Button inputDelete = (Button) text.getData(INPUT_DELETE_BUTTON);
				Button outputAdd = (Button) text.getData(OUTPUT_ADD_BUTTON);
				Button outputDelete = (Button) text.getData(OUTPUT_DELETE_BUTTON);

				if (text.getSelection()) {
					if (WidgetUtility.eltConfirmMessage(Messages.ALL_DATA_WILL_BE_LOST_DO_YOU_WISH_TO_CONTINUE)) {
						mappingSheetRow.setWholeOperationParameter(text.getSelection());
						parameterTextBox.setEnabled(true);

						operationInputFieldTableViewer.getTable().setEnabled(false);
						operationInputFieldTableViewer.getTable().clearAll();
						operationOutputFieldTableViewer.getTable().setEnabled(false);
						operationOutputFieldTableViewer.getTable().clearAll();
						
						operationClassTextBox.setEnabled(false);
						operationClassTextBox.setText("");
						operationIDTextBox.setEnabled(false);

						btnNewButton.setEnabled(false);
						outputAdd.setEnabled(false);
						outputDelete.setEnabled(false);
						inputAdd.setEnabled(false);
						inputDelete.setEnabled(false);
						transformMapping.getOutputFieldList().removeAll(mappingSheetRow.getOutputList());
				        mappingSheetRow.getOutputList().clear();
						mappingSheetRow.getInputFields().clear();
						mappingSheetRow.setComboBoxValue(Messages.CUSTOM);
						mappingSheetRow.getNameValueProperty().clear();
						mappingSheetRow.setClassParameter(false);
						mappingSheetRow.setOperationClassPath("");
						refreshOutputTable();
						showHideValidationMessage();
                   
					} else
						text.setSelection(false);
				} else {
					parameterTextBox.setText("");
					mappingSheetRow.setWholeOperationParameter(text.getSelection());
					parameterTextBox.setEnabled(false);

					operationInputFieldTableViewer.getTable().setEnabled(true);
					operationInputFieldTableViewer.refresh();
					operationOutputFieldTableViewer.getTable().setEnabled(true);
					operationOutputFieldTableViewer.refresh();
					operationClassTextBox.setEnabled(true);
					operationClassTextBox.setText("");
					operationIDTextBox.setEnabled(true);

					btnNewButton.setEnabled(true);
					inputAdd.setEnabled(true);
					inputDelete.setEnabled(true);
					outputAdd.setEnabled(true);
					outputDelete.setEnabled(true);
					 

				}
			}

		});
		isParam.setBounds(284, 156, 93, 16);
		isParam.setText(Messages.IS_PARAM);
		expandItem.setData(IS_PARAM, isParam);
		
     	if (mappingSheetRow.getOperationClassPath() != null){
		 operationClassTextBox.setText(mappingSheetRow.getOperationClassPath());
		}
	}

	private void addModifyListenerToOperationClassAndExpressionTextBox(Text textBox) {
		textBox.addModifyListener(new ModifyListener() {
		   @Override
			public void modifyText(ModifyEvent e) {
			 showHideValidationMessage();
			}
		});
	}

	private void attachFocusListenerToIdTextBox(Text operationIDTextBox) {
		operationIDTextBox.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				Text textBox = (Text) e.widget;
				if(StringUtils.isBlank(textBox.getText()))
				{
					textBox.setText((String) textBox.getData(Messages.PERVIOUS_VALUE));
				}	
				showHideValidationMessage();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				Text textBox=(Text) e.widget;
				textBox.setData(Messages.PERVIOUS_VALUE,textBox.getText());
			}
		});
	}


	private void attachModifyListenerToIdTextBox(
			final MappingSheetRow mappingSheetRow,Text operationIDTextBox) {
		operationIDTextBox.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text textBox = (Text) e.widget;

				ExpandItem expandItem = (ExpandItem) textBox.getData();

				expandItem.setText(textBox.getText());
				mappingSheetRow.setOperationID(textBox.getText());
			}
		});
	}

	private void attachFocuListenerToParamaterTextBox(Text text) {
		text.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				Text textBox=(Text)e.widget;
				String parameterText=textBox.getText();
				parameterText=StringUtils.replace(StringUtils.replace(parameterText,Constants.PARAMETER_PREFIX , ""),Constants.PARAMETER_SUFFIX,"");
				textBox.setText(Constants.PARAMETER_PREFIX+parameterText+Constants.PARAMETER_SUFFIX);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				Text textBox=(Text)e.widget;
				String parameterText=textBox.getText();
				parameterText=StringUtils.replace(StringUtils.replace(parameterText, Constants.PARAMETER_PREFIX, ""),Constants.PARAMETER_SUFFIX,"");
				textBox.setText(parameterText);
			}
		});
	}
    
	
	private TableViewer createOperationOutputFieldTable(Composite expandItemComposite, final MappingSheetRow mappingSheetRow) {

		Composite operationalOutputFieldComposite = new Composite(expandItemComposite, SWT.NONE);
		GridData gd_operationalOutputFieldComposite = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_operationalOutputFieldComposite.widthHint = 156;
		operationalOutputFieldComposite.setLayoutData(gd_operationalOutputFieldComposite);

		 operationalOutputFieldTableViewer = new TableViewer(operationalOutputFieldComposite,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		final TableViewer operationOutputtableViewer = setTableViewer(operationalOutputFieldTableViewer,
				operationalOutputFieldComposite, new String[] { Messages.INNER_OPERATION_OUTPUT_FIELD },
				new ELTFilterContentProvider(), new OperationLabelProvider());
		operationOutputtableViewer.getTable().setBounds(0, 25, 156, 182);
		operationOutputtableViewer.getTable().getColumn(0).setWidth(152);

		operationOutputtableViewer.setLabelProvider(new ELTFilterLabelProvider());
		isParam.setData(OPERATION_OUTPUT_FIELD_TABLE_VIEWER, operationOutputtableViewer);

		operationOutputtableViewer.setCellModifier(new ELTCellModifier(operationOutputtableViewer, this));
		operationOutputtableViewer.setInput(mappingSheetRow.getOutputList());
        
		CellEditor[] editor=operationOutputtableViewer.getCellEditors();
		fieldNameDecorator = WidgetUtility.addDecorator(editor[0].getControl(),Messages.FIELDNAME_SHOULD_NOT_BE_BLANK);
		isFieldNameAlphanumericDecorator=WidgetUtility.addDecorator(editor[0].getControl(),Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);	
		editor[0].setValidator(new TransformCellEditorFieldValidator(fieldNameDecorator,isFieldNameAlphanumericDecorator));
		isFieldNameAlphanumericDecorator.setMarginWidth(8);
		fieldNameDecorator.setMarginWidth(8);
		operationOutputtableViewer.getControl().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				Composite composite=(Composite)e.widget;
				Text text=(Text)composite.getParent().getParent().getData(OPERATION_ID_TEXT_BOX2);
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(text,
						inputFieldTableViewer,outputFieldViewer,transformMapping,finalSortedList);
				
			}
		});
		operationOutputtableViewer.getTable().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseDown(MouseEvent e) {
				isFieldNameAlphanumericDecorator.hide();
			}
		});
		
		operationalOutputFieldComposite.addMouseListener(new MouseAdapter() 
        {
			public void mouseUp(MouseEvent e) {
				Composite composite=(Composite)e.widget;
				Text text=(Text)composite.getParent().getData(OPERATION_ID_TEXT_BOX2);
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(text,
						inputFieldTableViewer,outputFieldViewer,transformMapping,finalSortedList);
			}
		});
		DragDropTransformOpImp dragDropTransformOpImpnew = new DragDropTransformOpImp(this,
				 temporaryOutputFieldMap,
				mappingSheetRow.getOutputList(), mappingSheetRow.getInputFields(), true,
				operationalInputFieldTableViewer, operationOutputtableViewer,transformMapping.getOutputFieldList());
		DragDropUtility.INSTANCE.applyDrop(operationalInputFieldTableViewer, dragDropTransformOpImpnew);

		Button addLabel = widget.buttonWidget(operationalOutputFieldComposite, SWT.CENTER, new int[] { 60, -1, 20, 15 },"");
		Image addImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON);
		addLabel.setImage(addImage);
		SchemaButtonsSyncUtility.INSTANCE.buttonSize(addLabel,macButtonWidth,macButtonHeight,windowButtonWidth,windowButtonHeight);
		addLabel.setToolTipText(Messages.ADD_SCHEMA_TOOLTIP);
		addLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				FilterProperties f = new FilterProperties();
				f.setPropertyname("");

				if (!mappingSheetRow.getOutputList().contains(f)) {

					mappingSheetRow.getOutputList().add(f);
					operationOutputtableViewer.refresh();
					int i = mappingSheetRow.getOutputList().size() == 0 ? mappingSheetRow.getOutputList().size()
							: mappingSheetRow.getOutputList().size() - 1;
					operationalOutputFieldTableViewer.editElement(operationOutputtableViewer.getElementAt(i), 0);
					component.setLatestChangesInSchema(false);
				}
			}

		});

		
		Button deleteLabel = widget.buttonWidget(operationalOutputFieldComposite, SWT.CENTER,new int[] { 90, -1, 20, 15 }, "");
		Image deleteImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON);
		deleteLabel.setImage(deleteImage);
		SchemaButtonsSyncUtility.INSTANCE.buttonSize(deleteLabel,macButtonWidth,macButtonHeight,windowButtonWidth,windowButtonHeight);
		deleteLabel.setToolTipText(Messages.DELETE_SCHEMA_TOOLTIP);
		deleteLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Table table = operationOutputtableViewer.getTable();
				int temp = table.getSelectionIndex();
				int[] indexs = table.getSelectionIndices();
				mappingSheetRow.getOutputList().size();
				if (temp == -1) {
					WidgetUtility.errorMessage(Messages.SelectRowToDelete);
				} else {
					table.remove(indexs);

					List<FilterProperties> tempList = new ArrayList<FilterProperties>();
					for (int index : indexs) {
						tempList.add(mappingSheetRow.getOutputList().get(index));
					}
					for(FilterProperties filterProperties: tempList)
					{	
					mappingSheetRow.getOutputList().remove(filterProperties);
					int index=-1;	
					for(int j=0;j<transformMapping.getOutputFieldList().size();j++)
					{
						if(transformMapping.getOutputFieldList().get(j)==filterProperties)
						{
							index=j;
							break;
						}	
					}
					if(index!=-1)
					transformMapping.getOutputFieldList().remove(index);
					
					}
					refreshOutputTable();
					showHideValidationMessage();
				}
			}

		});
		isParam.setData(OUTPUT_ADD_BUTTON, addLabel);
		isParam.setData(OUTPUT_DELETE_BUTTON, deleteLabel);
		if (mappingSheetRow.isWholeOperationParameter()) {
			Button text = (Button) isParam;
			Text parameterTextBox = (Text) text.getData(PARAMETER_TEXT_BOX);
			TableViewer operationInputFieldTableViewer = (TableViewer) text.getData(OPERATION_INPUT_FIELD_TABLE_VIEWER);
			TableViewer operationalOutputFieldTableViewer = (TableViewer) text.getData(OPERATION_OUTPUT_FIELD_TABLE_VIEWER);
			Text operationClassTextBox = (Text) text.getData(OPERATION_CLASS_TEXT_BOX);
			Text operationIDTextBox = (Text) text.getData(OPERATION_ID_TEXT_BOX);
			Button btnNewButton = (Button) text.getData(BTN_NEW_BUTTON);
			Button inputAdd = (Button) text.getData(INPUT_ADD_BUTTON);

			Button inputDelete = (Button) text.getData(INPUT_DELETE_BUTTON);
			Button outputAdd = (Button) text.getData(OUTPUT_ADD_BUTTON);
			Button outputDelete = (Button) text.getData(OUTPUT_DELETE_BUTTON);
			parameterTextBox.setEnabled(true);

			operationInputFieldTableViewer.getTable().setEnabled(false);

			operationalOutputFieldTableViewer.getTable().setEnabled(false);
			operationClassTextBox.setEnabled(false);

			operationIDTextBox.setEnabled(false);

			btnNewButton.setEnabled(false);
			inputAdd.setEnabled(false);
			inputDelete.setEnabled(false);

			outputAdd.setEnabled(false);
			outputDelete.setEnabled(false);

		}
    return operationOutputtableViewer;
	}
    
	private void addIsParamSelectionListener(Button btnIsParam,final MappingSheetRow mappingSheetRow) {
		btnIsParam.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				
				Button isParam=(Button)e.widget;
			    TableViewer tableViewer=(TableViewer)isParam.getData("inputFieldTable");
				Button addButton=(Button) isParam.getData("addButton");
				Button deleteButton=(Button) isParam.getData("deleteButton");
				Text expressionIdTextBox=(Text) isParam.getData("expressionIdTextBox");
				Button browseButton=(Button) isParam.getData("expressionEditorButton");
				Text outputFieldTextBox=(Text) isParam.getData("outputFieldTextBox");
				Text expressionTextBox=(Text) isParam.getData("expressionTextBox");
				Text parameterTextBox=(Text)isParam.getData("parameterTextBox");
				if(isParam.getSelection())
				{
					if (WidgetUtility.eltConfirmMessage(Messages.ALL_DATA_WILL_BE_LOST_DO_YOU_WISH_TO_CONTINUE)) 
					{
						parameterTextBox.setEnabled(true);
						mappingSheetRow.setWholeOperationParameter(true);
						mappingSheetRow.getExpressionEditorData().setExpression("");
						mappingSheetRow.getExpressionEditorData().getSelectedInputFieldsForExpression().clear();
						tableViewer.getTable().clearAll();
						tableViewer.getTable().setEnabled(false);
						addButton.setEnabled(false);
						deleteButton.setEnabled(false);
						expressionIdTextBox.setEnabled(false);
						expressionTextBox.setText("");
						browseButton.setEnabled(false);
						outputFieldTextBox.setText("");
						outputFieldTextBox.setEnabled(false);
						mappingSheetRow.getInputFields().clear();
						mappingSheetRow.getOutputList().clear();
						refreshOutputTable();
						showHideValidationMessage();
					}
					else
						isParam.setSelection(false);
				}	
				else
				{
					parameterTextBox.setText("");
					outputFieldTextBox.setText("");
					expressionTextBox.setText("");
					parameterTextBox.setEnabled(false);
					mappingSheetRow.setWholeOperationParameter(false);
					tableViewer.getTable().setEnabled(true);
					tableViewer.refresh();
					addButton.setEnabled(true);
					deleteButton.setEnabled(true);
					expressionIdTextBox.setEnabled(true);
					browseButton.setEnabled(true);
					outputFieldTextBox.setEnabled(true);
				}	
			}
		});
	}
	
	public void refreshOutputTable() 
	{
		finalSortedList=new ArrayList<>();
		temporaryOutputFieldMap.clear();
		temporaryOutputFieldMap
				.put("MapAndPassThroughFields",convertNameValueToFilterProperties(transformMapping.getMapAndPassthroughField()));
		for (MappingSheetRow mappingSheetRow1 : transformMapping.getMappingSheetRows()) {
			temporaryOutputFieldMap.put(mappingSheetRow1.getOperationID(),mappingSheetRow1.getOutputList());

		}
		backwardJobComapatabilityCode();   
		for(FilterProperties filterProperties:transformMapping.getOutputFieldList())
		{
		 ParameterUtil.addPrefixSuffixToParameterFields(filterProperties,transformMapping);
				
		}	
		SchemaSyncUtility.INSTANCE.unionFilter(transformMapping.getOutputFieldList(), finalSortedList);
		
	    outputFieldViewer.setInput(finalSortedList);
		outputFieldViewer.refresh();
		mappingTableViewer.refresh();
	}
	
    /**
     * temporaray method for backward Job compatability
     */
    private void backwardJobComapatabilityCode()
    {
    	boolean isJobOld = false;
    	for(NameValueProperty nameValueProperty:transformMapping.getMapAndPassthroughField())
    	{
    		if(nameValueProperty.getFilterProperty()==null || StringUtils.isBlank(nameValueProperty.getFilterProperty().getPropertyname()))
    		{
    			isJobOld=true;
    			break;
    		}	
    	}
    	if(isJobOld)
    	{
    		for(MappingSheetRow mappingSheetRow:transformMapping.getMappingSheetRows())
    		{
    		transformMapping.getOutputFieldList().addAll(mappingSheetRow.getOutputList());
    		}
    		List<NameValueProperty> tempNameValuePropertyList=new ArrayList<>();
    		for(NameValueProperty nameValueProperty:transformMapping.getMapAndPassthroughField())
    		{
    			NameValueProperty newNameValueProperty=new NameValueProperty();
    			newNameValueProperty.setPropertyName(nameValueProperty.getPropertyName());
    			newNameValueProperty.setPropertyValue(nameValueProperty.getPropertyValue());
    			newNameValueProperty.getFilterProperty().setPropertyname(nameValueProperty.getPropertyValue());
    			tempNameValuePropertyList.add(newNameValueProperty);
    			transformMapping.getOutputFieldList().add(newNameValueProperty.getFilterProperty());
    		}	
    		transformMapping.getMapAndPassthroughField().clear();
    		transformMapping.getMapAndPassthroughField().addAll(tempNameValuePropertyList);
    		tempNameValuePropertyList.clear();
    	}	
    }
    
	/**
	 * @return finalsortedList
	 */
	public List<FilterProperties> getFinalSortedList() {
		return finalSortedList;
	}

	/**
	 * @param temporaryOutputFieldListTemp
	 * @return
	 */
	public Map<String,List<String>> getDuplicateOutputFieldMap(Map<String,List<FilterProperties> > temporaryOutputFieldListTemp) {
		
		Set<String> setToCheckDuplicates = new HashSet<String>();
		int index = 0;
		if(duplicateFieldMap!=null)
		duplicateFieldMap.clear();
		for (Map.Entry<String, List<FilterProperties>> entry: temporaryOutputFieldListTemp.entrySet()) 
		{
			List<FilterProperties>  temporaryOutputFieldList=entry.getValue();
			List<String> duplicateFields=new ArrayList<>();
			for (FilterProperties filterProperties : temporaryOutputFieldList) {
			if (!setToCheckDuplicates.add(filterProperties.getPropertyname())) {
				duplicateFields.add(filterProperties.getPropertyname());
				outputFieldIndices.add(index);
			}
			index++;
		}
			duplicateFieldMap.put(entry.getKey(),duplicateFields);	
		}
		return duplicateFieldMap;
	}

	

	
	
	/**
	 * @param mappingSheetRow
	 */
	public void setDuplicateOperationInputFieldMap(MappingSheetRow mappingSheetRow) {
			List<FilterProperties> temp=mappingSheetRow.getInputFields();
			List<String> duplicateFields=new ArrayList<>();
			Set<String> setToCheckDuplicates = new HashSet<String>();
			for (FilterProperties filterProperties : temp)   {
				if (!setToCheckDuplicates.add(filterProperties.getPropertyname())) {
					
					duplicateFields.add(filterProperties.getPropertyname());
				}
				
			}
			duplicateOperationInputFieldMap.put(mappingSheetRow.getOperationID(),duplicateFields);
	}
    
     /**
     * check if error exist
     */
    public void showHideValidationMessage()
   	{		
		if(errorTableViewer!=null)
		{
		   setErrorMessageForDuplicateOutputField();
		   setErrorMessageForDuplicateInputField(); 
		   setErrorMessageForInvalidMapFields();
		   Set<String> setToCheckDuplicates = showErrorIfOperationClassOrExpressionBlankOrOperationIDDuplicate(); 	
     	   errorTableViewer.getTable().setForeground(new Color(Display.getDefault(), 255, 0, 0));
	       errorTableViewer.refresh();
	       errorLabelList.clear();
	       setToCheckDuplicates.clear();
	       outputFieldIndices.clear();
		}
	}
    
    private void setErrorMessageForInvalidMapFields(){
    	List<String> inputFieldNames = new ArrayList<String>();
    	for(InputField inputField:transformMapping.getInputFields()){
    		inputFieldNames.add(inputField.getFieldName());
    	}
    	
    	for(int i = 0 ;i< transformMapping.getMapAndPassthroughField().size();i++){
    		if(!inputFieldNames.contains(transformMapping.getMapAndPassthroughField().get(i).getPropertyName())){
    		    errorLabel=new Label( errorTableViewer.getTable(), SWT.NONE);
    			errorLabel.setVisible(true);
    			errorLabel.setText("Field '"+transformMapping.getMapAndPassthroughField().get(i).getPropertyName()+"' is not present in Input Fields"); 
    			errorLabelList.add(errorLabel);
    			outputFieldIndices.add(i);
    		}
    	}
    	for(int indices=0 ; indices<mappingTableViewer.getTable().getItemCount();indices++){
    		if(outputFieldIndices.contains(indices)){
    			mappingTableViewer.getTable().getItem(indices).setForeground(new Color(Display.getDefault(), 255,0,0));
    		}else{
    			mappingTableViewer.getTable().getItem(indices).setForeground(new Color(Display.getDefault(), 0, 0, 0));
    		}
    	}
    }

    private Set<String> showErrorIfOperationClassOrExpressionBlankOrOperationIDDuplicate() 
    {
	    Set<String> setToCheckDuplicates = new HashSet<String>();
		 for(ExpandItem item:expandBar.getItems() )
		{
			Text operationClassTextBox=(Text)item.getData(OPERATION_CLASS_TEXT_BOX);
			Text expressionTextBox=(Text)item.getData(EXPRESSION_TEXT_BOX);
			Button isParam=(Button)item.getData(IS_PARAM);
			Text outputFieldTextBox=(Text)item.getData(OUTPUT_FIELD);
			Text idTextBox=(Text)item.getData();
			if(operationClassTextBox!=null)
			{
			   if(StringUtils.isBlank(operationClassTextBox.getText()) && !isParam.getSelection())
			   {
				intializeErrorLabelObject(idTextBox,"Operation Class must not be blank for");
			    }
			}
			if(expressionTextBox!=null)
			{
				if(StringUtils.isBlank(expressionTextBox.getText()) && !isParam.getSelection())
			   {
				intializeErrorLabelObject(idTextBox,"Expression must not be blank for");
			   }
				
			}
			if(outputFieldTextBox!=null)
			{
				if(StringUtils.isBlank(outputFieldTextBox.getText()) && !isParam.getSelection())
				 {
					intializeErrorLabelObject(idTextBox,"Output Field must not be blank for");
				 }
			}	
			if(!setToCheckDuplicates.add(idTextBox.getText())&&!isParam.getSelection())
			{
				intializeErrorLabelObject(idTextBox,"Duplicate Operation Id");
			}	
		}
	 return setToCheckDuplicates;
   }

	private void intializeErrorLabelObject(Text idTextBox,String errorMessage) {
		errorLabel=new Label( errorTableViewer.getTable(), SWT.NONE);
		errorLabel.setVisible(true); 
		errorLabel.setText(errorMessage+" "+idTextBox.getText()); 	
		errorLabelList.add(errorLabel);
	}

   private void setErrorMessageForDuplicateInputField() {
	if(!duplicateOperationInputFieldMap.isEmpty())
   {
	   for(Map.Entry<String, List<String>> entry:duplicateOperationInputFieldMap.entrySet())
	   {
		   for(String f:entry.getValue())
		   {   
			   boolean logError=true;
			   errorLabel=new Label( errorTableViewer.getTable(), SWT.NONE);
			   errorLabel.setVisible(true); 
			   errorLabel.setText("Duplicate Operation Input Field"+" "+f +" "+"exist in"+" "+entry.getKey()); 
			   for(Label tempErrorLabel:errorLabelList) {
				   if(StringUtils.equalsIgnoreCase(errorLabel.getText(),tempErrorLabel.getText()))
				   logError=false;
			   }
			   if(logError)
			   errorLabelList.add(errorLabel);
		   }
	   }
   }
}

   private void setErrorMessageForDuplicateOutputField() {
	Map<String,List<String>> duplicateOutputFieldMap=getDuplicateOutputFieldMap(temporaryOutputFieldMap);
	   if(!duplicateOutputFieldMap.isEmpty())
	   {	for (Map.Entry<String,List<String>> entry: duplicateOutputFieldMap.entrySet()) 
		{
		   for(String f:entry.getValue())
		   {   
			   boolean logError=true;   
		    errorLabel=new Label( errorTableViewer.getTable(), SWT.NONE);
			errorLabel.setVisible(true);
			errorLabel.setText("Duplicate Output Field "+" "+f+" "+"exist in"+" "+entry.getKey()); 
			
			for(Label tempErrorLabel:errorLabelList) {
				   if(StringUtils.equalsIgnoreCase(errorLabel.getText(),tempErrorLabel.getText()))
				   logError=false;
			   }
			if(logError)
			errorLabelList.add(errorLabel);
		   }
	    }
	   }
}

	/**
	 * @param nameValueProperty
	 * @return
	 */
	public List<FilterProperties> convertNameValueToFilterProperties(List<NameValueProperty> nameValueProperty) {
		List<FilterProperties> filterProperties = new ArrayList<>();

		for (NameValueProperty nameValue : nameValueProperty) {
			FilterProperties filterProperty = new FilterProperties();
			filterProperty.setPropertyname(nameValue.getPropertyValue());
			filterProperties.add(filterProperty);
		}
		return filterProperties;
	}

	private TableViewer createOperationInputFieldTable(Composite expandItemComposite,
			final MappingSheetRow mappingSheetRow) {
		Composite operationInputFieldComposite = new Composite(expandItemComposite, SWT.NONE);
		GridData gd_operationInputFieldComposite = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_operationInputFieldComposite.widthHint = 156;
		gd_operationInputFieldComposite.heightHint = 216;
		
		operationInputFieldComposite.setLayoutData(gd_operationInputFieldComposite);

		TableViewer operationInputFieldTableViewer = new TableViewer(operationInputFieldComposite, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI);
		final TableViewer operationalInputFieldTableViewer = setTableViewer(operationInputFieldTableViewer,
				operationInputFieldComposite, new String[] { Messages.INNER_OPERATION_INPUT_FIELD },
				new ELTFilterContentProvider(), new OperationLabelProvider());

		operationalInputFieldTableViewer.setLabelProvider(new ELTFilterLabelProvider());
        
		operationalInputFieldTableViewer.setInput(mappingSheetRow.getInputFields());
		operationalInputFieldTableViewer.getTable().setBounds(0, 25, 156, 182);
		operationalInputFieldTableViewer.getTable().getColumn(0).setWidth(152);
		operationalInputFieldTableViewer.setCellModifier(new ELTCellModifier(operationalInputFieldTableViewer, this,mappingSheetRow));
		CellEditor[] editor=operationalInputFieldTableViewer.getCellEditors();
		fieldNameDecorator = WidgetUtility.addDecorator(editor[0].getControl(),Messages.FIELDNAME_SHOULD_NOT_BE_BLANK);
		isFieldNameAlphanumericDecorator=WidgetUtility.addDecorator(editor[0].getControl(),Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);	
		
		editors[0].setValidator(new TransformCellEditorFieldValidator(fieldNameDecorator,isFieldNameAlphanumericDecorator));
		operationInputaddButton = widget.buttonWidget(operationInputFieldComposite, SWT.CENTER, new int[] { 60, -1, 20,15 }, "");
		Image addImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON);
		operationInputaddButton.setImage(addImage);
		 SchemaButtonsSyncUtility.INSTANCE.buttonSize(operationInputaddButton,macButtonWidth,macButtonHeight,windowButtonWidth,windowButtonHeight);
		operationInputaddButton.setToolTipText(Messages.ADD_SCHEMA_TOOLTIP);
		
		operationInputDeleteButton = widget.buttonWidget(operationInputFieldComposite, SWT.CENTER, new int[] { 90, -1,20, 15 }, "");
		 Image deleteImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON);
		 operationInputDeleteButton.setImage(deleteImage);
		 SchemaButtonsSyncUtility.INSTANCE.buttonSize(operationInputDeleteButton,macButtonWidth,macButtonHeight,windowButtonWidth,windowButtonHeight);
		operationInputDeleteButton.setToolTipText(Messages.DELETE_SCHEMA_TOOLTIP);
		
		isFieldNameAlphanumericDecorator.setMarginWidth(8);
		fieldNameDecorator.setMarginWidth(8);
		
		addButtonListener(mappingSheetRow, operationalInputFieldTableViewer,operationInputaddButton);
		
		operationalInputFieldTableViewer.getControl().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				Composite composite=(Composite)e.widget;
				Text text=(Text)composite.getParent().getParent().getData(OPERATION_ID_TEXT_BOX2);
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(text,
						inputFieldTableViewer,outputFieldViewer,transformMapping,finalSortedList);
				
			}
		});
		operationInputFieldComposite.addMouseListener(new MouseAdapter() 
        {
			public void mouseUp(MouseEvent e) {
				Composite composite=(Composite)e.widget;
				Text text=(Text)composite.getParent().getData(OPERATION_ID_TEXT_BOX2);
				TransformMappingFeatureUtility.INSTANCE.highlightInputAndOutputFields(text,
						inputFieldTableViewer,outputFieldViewer,transformMapping,finalSortedList);
			}
		});
		deleteButtonListener(mappingSheetRow, operationalInputFieldTableViewer,operationInputDeleteButton);
		return operationalInputFieldTableViewer;
	}

	private void deleteButtonListener(final MappingSheetRow mappingSheetRow,
			final TableViewer operationalInputFieldTableViewer,Widget button) {
		button.addListener(SWT.MouseUp,new Listener(){

			@Override
			public void handleEvent(Event event) {
				Table table = operationalInputFieldTableViewer.getTable();
				int temp = table.getSelectionIndex();
				 mappingSheetRow.getInputFields().size();
				int[] indexs = table.getSelectionIndices();
				if (temp == -1) 
				{
					WidgetUtility.errorMessage(Messages.SelectRowToDelete);
				} 
				else 
				{
					table.remove(indexs);
					List<FilterProperties> tempList = new ArrayList<FilterProperties>();
					for (int index : indexs) {
						tempList.add(mappingSheetRow.getInputFields().get(index));
					}
					  for(FilterProperties filterProperties:tempList)
					  {
					   mappingSheetRow.getInputFields().remove(filterProperties);
					  }
					   setDuplicateOperationInputFieldMap( mappingSheetRow);
					    showHideValidationMessage();

				}
			}

			
			
		});
	}
    
private void addModifyAndFocusListenerToOutputField(Text outputFieldTextBox2,final MappingSheetRow mappingSheetRow) {
	 
	outputFieldTextBox2.addFocusListener(new FocusAdapter() {
		
		@Override
		public void focusLost(FocusEvent e) {
         refreshOutputTable();
         showHideValidationMessage();
		}
	});
		outputFieldTextBox2.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Text textBox=(Text) e.widget;
				if(mappingSheetRow.getOutputList().isEmpty())
				{
					FilterProperties filterProperties=new FilterProperties();	
					filterProperties.setPropertyname(textBox.getText());
				    mappingSheetRow.getOutputList().add(filterProperties);
				    transformMapping.getOutputFieldList().add(filterProperties);
				}
				else
				{	
				mappingSheetRow.getOutputList().get(0).setPropertyname(textBox.getText());	
				
				}
			}
		});
	}

private void addButtonListener(final MappingSheetRow mappingSheetRow,
			final TableViewer operationalInputFieldTableViewer,Widget addButton) {
		addButton.addListener(SWT.MouseUp, new Listener(){

			@Override
			public void handleEvent(Event event) {
				FilterProperties filterProperties = new FilterProperties();
				filterProperties.setPropertyname("");
				if (!mappingSheetRow.getInputFields().contains(filterProperties)) {
					mappingSheetRow.getInputFields().add(filterProperties);

					operationalInputFieldTableViewer.refresh();
					int i = mappingSheetRow.getInputFields().size() == 0 ? mappingSheetRow.getInputFields().size()
							: mappingSheetRow.getInputFields().size() - 1;
					operationalInputFieldTableViewer.editElement(operationalInputFieldTableViewer.getElementAt(i), 0);

				}
				
			}
			
		});
	}

	private void setIsOperationInputFieldDuplicate() {
		if (!transformMapping.getMappingSheetRows().isEmpty()) {
			Set<FilterProperties> set = null;
			List<MappingSheetRow> mappingSheetRows = transformMapping.getMappingSheetRows();
			for (MappingSheetRow mappingSheetRow : mappingSheetRows) {
				set = new HashSet<FilterProperties>(mappingSheetRow.getInputFields());
				if (set.size() < mappingSheetRow.getInputFields().size()) {
					isOperationInputFieldDuplicate = true;
					break;
				}
			}
				if(set!=null){
				set.clear();
				}
			}
		}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		Button cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		propertyDialogButtonBar.setPropertyDialogButtonBar(okButton, null, cancelButton);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		container.getShell().layout(true, true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Point newSize = container.getShell().computeSize(screenSize.width, screenSize.height, true);
		container.getShell().setSize(newSize);
		return newSize;
	}

	@Override
	protected void okPressed() {
			transformMapping = new TransformMapping((List<InputField>) inputFieldTableViewer.getInput(),
			transformMapping.getMappingSheetRows(), transformMapping.getMapAndPassthroughField(),
			transformMapping.getOutputFieldList());
			okPressed = true;
			super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		cancelPressed = true;
		super.cancelPressed();
	}

	public TransformMapping getATMapping() {
		return transformMapping;
	}

	public Component getComponent() {
		return component;
	}

	public void pressOK() {
		isYesButtonPressed = true;
		okPressed();
	}

	public void pressCancel() {
		isNoButtonPressed = true;
		cancelPressed();
	}

	public boolean isCancelPressed() {
		return cancelPressed;
	}

	public boolean isOkPressed() {
		return okPressed;
	}

	/**
	 * 
	 * returns true if ok button pressed from code
	 * 
	 * @return boolean
	 */
	public boolean isYesButtonPressed() {
		return isYesButtonPressed;
	}

	public boolean isOperationInputFieldDupluicate() {
		return isOperationInputFieldDuplicate;
	}

	/**
	 * 
	 * returns true of cancel button pressed from code
	 * 
	 * @return boolean
	 */
	public boolean isNoButtonPressed() {
		return isNoButtonPressed;
	}

	public TableViewer setTableViewer(TableViewer tableViewer, Composite composite, String[] prop,
			IStructuredContentProvider iStructuredContentProvider, ITableLabelProvider iTableLabelProvider) {

		tableViewer.setContentProvider(iStructuredContentProvider);

		tableViewer.setColumnProperties(prop);

		tableViewerTable = tableViewer.getTable();

		tableViewerTable.setVisible(true);
		tableViewerTable.setLinesVisible(true);
		tableViewerTable.setHeaderVisible(true);
		createTableColumns(tableViewerTable, prop);
		editors = createCellEditorList(tableViewerTable, prop.length);
		tableViewer.setCellEditors(editors);

		TableViewerEditor.create(tableViewer, new ColumnViewerEditorActivationStrategy(tableViewer),
				ColumnViewerEditor.KEYBOARD_ACTIVATION | ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL);

		return tableViewer;
	}

	public static void createTableColumns(Table table, String[] fields) {
		for (String field : fields) {
			TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
			tableColumn.setText(field);

			tableColumn.setWidth(100);
			tableColumn.pack();
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	public static CellEditor[] createCellEditorList(Table table, int size) {
		CellEditor[] cellEditor = new CellEditor[size];
		for (int i = 0; i < size; i++)
			addTextEditor(table, cellEditor, i);

		return cellEditor;
	}

	protected static void addTextEditor(Table table, CellEditor[] cellEditor, int position) {

		cellEditor[position] = new TextCellEditor(table, SWT.COLOR_GREEN);
	}
	
	
	private void syncTransformFieldsWithSchema() {
		
		List<FilterProperties> filterProperties = convertSchemaToFilterProperty();
		if(checkIfSchemaIsBlank(filterProperties))
		{
		return;
		} 	
		
		SchemaSyncUtility.INSTANCE.removeOpFields(filterProperties, transformMapping.getMappingSheetRows());
		Schema schema = (Schema) component.getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
		List<NameValueProperty> outputFileds= SchemaSyncUtility.INSTANCE.getComponentSchemaAsProperty(schema.getGridRow());
		SchemaSyncUtility.INSTANCE.filterCommonMapFields(outputFileds, transformMapping);
		Map<Integer,FilterProperties> indexValueParameterMap=SchemaSyncUtility.INSTANCE.retainIndexAndValueOfParameterFields
				(transformMapping.getOutputFieldList());
		transformMapping.getOutputFieldList().clear();
		SchemaSyncUtility.INSTANCE.addOperationFieldAndMapPassthroughfieldToOutputField(transformMapping);
		List<FilterProperties> finalSortedList=SchemaSyncUtility.INSTANCE.
				sortOutputFieldToMatchSchemaSequence(filterProperties,transformMapping);
		transformMapping.getOutputFieldList().clear();
		transformMapping.getOutputFieldList().addAll(finalSortedList);
		SchemaSyncUtility.INSTANCE.addParamtereFieldsToSameIndexAsBeforePull(indexValueParameterMap,transformMapping);
		refreshOutputTable();
		for(ExpandItem item:expandBar.getItems())
		{
			TableViewer tableViewer=(TableViewer)item.getData(OUTPUT_TABLE_VIEWER);
			if(tableViewer!=null)
			tableViewer.refresh();
		}
	}

	private boolean checkIfSchemaIsBlank(List<FilterProperties> filterProperties) {
		if(filterProperties.isEmpty())
		{
			
			transformMapping.getOutputFieldList().clear();
			for(MappingSheetRow mappingSheetRow:transformMapping.getMappingSheetRows())
			{
				mappingSheetRow.getOutputList().clear();
			}
			for(ExpandItem item:expandBar.getItems())
			{
				
				TableViewer tableViewer=(TableViewer)item.getData(OUTPUT_TABLE_VIEWER);
				if(tableViewer!=null)
				tableViewer.refresh();
			}
			transformMapping.getMapAndPassthroughField().clear();
			mappingTableViewer.refresh();
			finalSortedList.clear();
			outputFieldViewer.refresh();
			
		}
		return filterProperties.isEmpty();
	}
	/**
	 * convert schema to filter property 
	 * @return  list
	 */
	private List<FilterProperties> convertSchemaToFilterProperty(){
		List<FilterProperties> outputFileds = new ArrayList<>();
		Schema schema = (Schema) component.getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
		    if(schema==null)
			 return outputFileds;  
			for (GridRow gridRow : schema.getGridRow()) {
				FilterProperties filterProperty = new FilterProperties();
				filterProperty.setPropertyname(gridRow.getFieldName());
				outputFileds.add(filterProperty);
			}
		return outputFileds;
	}
	
	private void addOperations() {
		if (expandBar.getItemCount() > 1){
			for (ExpandItem expandItem : expandBar.getItems()) {
				expandItem.setExpanded(false);

			}
		}
		List<FilterProperties> inputFieldList = new ArrayList<>();
		List<FilterProperties> outputList = new ArrayList<>();
		List<NameValueProperty> nameValueProperty = new ArrayList<>();
		int n = transformMapping.getMappingSheetRows().size() + 1;
		String operationID;
		if(optionToSelect)
		{	
			operationID="Expression:"+n;
		}
		
		else
		{
			operationID= Messages.OPERATION_ID_PREFIX + n;	
		}	
		for(int i=0;i<expandBar.getItemCount();i++)
		{
			Text text= (Text) expandBar.getItems()[i].getData();
			if(StringUtils.equalsIgnoreCase(operationID, text.getText()))
			{
				 n++;
				 operationID = Messages.OPERATION_ID_PREFIX + n;
				 i=-1;
			}
		}	
		
		
        if(optionToSelect)
        {
        	ExpressionEditorData expressionEditorData=new ExpressionEditorData("");
        	mappingSheetRow = new MappingSheetRow(inputFieldList, outputList, operationID, Messages.CUSTOM, "",
    				nameValueProperty, false, "", false, "",true,expressionEditorData);
        }
        else
        {
            mappingSheetRow = new MappingSheetRow(inputFieldList, outputList, operationID, Messages.CUSTOM, "",
    				nameValueProperty, false, "", false, "",false,null);
        }
		
        transformMapping.getMappingSheetRows().add(mappingSheetRow);

		addExpandItem(scrolledComposite, mappingSheetRow, operationID);
	}
}
