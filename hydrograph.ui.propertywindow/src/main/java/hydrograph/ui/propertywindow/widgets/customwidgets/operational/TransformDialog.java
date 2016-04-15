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
import hydrograph.ui.common.util.ParameterUtil;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.NameValueProperty;
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
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class TransformDialog extends Dialog implements IOperationClassDialog {

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
	private Label operationInputaddButton;
	private Label operationInputDeleteButton;
	private Composite composite_1;
	private ScrolledComposite scrolledComposite;
	Map<Text, Button> opClassMap = new LinkedHashMap<Text, Button>();
	private TableViewer inputFieldTableViewer;
	private TableViewer mappingTableViewer;
	private TransformMapping transformMapping;
	private TableViewer outputFieldViewer;
	private Map<String,List<FilterProperties>> temporaryOutputFieldMap = new HashMap<String,List<FilterProperties>>();
	private MappingSheetRow mappingSheetRow;
	private Label errorLabel;
	private boolean isOperationInputFieldDuplicate;
	private TransformDialog transformDialog;
	List<Label> errorLabelList = new ArrayList<>();
	private TableViewer errorTableViewer;
	private Composite errorComposite;
	private Map<String,List<String>> duplicateOperationInputFieldMap = new HashMap<String,List<String>>();
	private Map<String,List<String>> duplicateFieldMap = new HashMap<String,List<String>>();
	private ControlDecoration isFieldNameAlphanumericDecorator;
	private ControlDecoration fieldNameDecorator;
	public TransformDialog(Shell parentShell, Component component, WidgetConfig widgetConfig, TransformMapping atMapping) {

		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL);
		this.transformMapping = atMapping;
		isYesButtonPressed = false;
		isNoButtonPressed = false;
		this.component = component;
		this.widgetConfig = widgetConfig;
		this.transformDialog = this;

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
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		container.getShell().setText("Transform Editor");

		propertyDialogButtonBar = new PropertyDialogButtonBar(container);

		composite_1 = new Composite(container, SWT.NONE);
		composite_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		composite_1.setLayout(new GridLayout(3, false));
		createInputFieldTable(composite_1);

		createOperationClassGrid(composite_1);

		createOutputFieldTable(composite_1);

		return container;
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

		inputFieldTableViewer.getTable().addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				Table table = (Table) e.widget;
				Rectangle area = table.getClientArea();
				int totalAreaWidth = area.width;
				int diff = totalAreaWidth - (table.getColumn(0).getWidth());
				table.getColumn(0).setWidth(diff + table.getColumn(0).getWidth());
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});
	}

	private void createOutputFieldTable(Composite composite) {

		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gridData.widthHint = 250;
		rightComposite.setLayoutData(gridData);

		Composite buttonComposite = new Composite(rightComposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(2, false));
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		Composite outputFieldComposite = new Composite(rightComposite, SWT.NONE);
		outputFieldComposite.setLayout(new GridLayout(1, false));
		outputFieldComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		Button btnPull = new Button(buttonComposite, SWT.NONE);
		btnPull.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<NameValueProperty> outputFileds = new ArrayList<>();
				Map<String, ComponentsOutputSchema> schema = (Map<String, ComponentsOutputSchema>) component
						.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
				for (Map.Entry<String, ComponentsOutputSchema> entry : schema.entrySet()) {
					ComponentsOutputSchema componentsOutputSchema = entry.getValue();
					for (FixedWidthGridRow fixedWidthGridRow : componentsOutputSchema
							.getFixedWidthGridRowsOutputFields()) {
						NameValueProperty nameValueProperty = new NameValueProperty();
						nameValueProperty.setPropertyName("");
						nameValueProperty.setPropertyValue(fixedWidthGridRow.getFieldName());
						outputFileds.add(nameValueProperty);
					}
				}
				List<NameValueProperty> mapNameValueProperties = transformMapping.getMapAndPassthroughField();
				DragDropUtility.union(outputFileds, mapNameValueProperties);
				refreshOutputTable();

			}
		});
		btnPull.setBounds(20, 10, 20, 20);

		btnPull.setText(Messages.PULL_BUTTON_LABEL);

		Label deletLabel = widget.labelWidget(buttonComposite, SWT.CENTER, new int[] { 160, 10, 20, 15 }, "",
				new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));
		deletLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				Table table = outputFieldViewer.getTable();
				int temp = table.getSelectionIndex();
				int[] indexs = table.getSelectionIndices();
				if (temp == -1) {
					WidgetUtility.errorMessage(Messages.SelectRowToDelete);
				} else {
					table.remove(indexs);
					ArrayList tempList = new ArrayList();
					for (int index : indexs) {

						tempList.add(temporaryOutputFieldMap.get(index));
					}
					for (Map.Entry<String, List<FilterProperties>> entry: temporaryOutputFieldMap.entrySet()) {
					
						entry.getValue().removeAll(tempList);
					}
					transformMapping.getOutputFieldList().removeAll(tempList);
					refreshOutputTable();

					showHideValidationMessage();
				}
			}

		});
		deletLabel.setEnabled(false);

		outputFieldViewer = new TableViewer(outputFieldComposite, SWT.BORDER | SWT.FULL_SELECTION);
		setTableViewer(outputFieldViewer, outputFieldComposite, new String[] { Messages.OUTPUT_FIELD },
				new ELTFilterContentProvider(), new OperationLabelProvider());
		outputFieldViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		outputFieldViewer.setCellModifier(new ELTCellModifier(outputFieldViewer));
		outputFieldViewer.setLabelProvider(new ELTFilterLabelProvider());

		refreshOutputTable();
		setIsOperationInputFieldDuplicate();
		showHideValidationMessage();
		outputFieldViewer.getTable().addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				Table table = (Table) e.widget;
				Rectangle area = table.getClientArea();
				int totalAreaWidth = area.width;
				int diff = totalAreaWidth - (table.getColumn(0).getWidth());
				table.getColumn(0).setWidth(diff + table.getColumn(0).getWidth());
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});
	}

	private void createOperationClassGrid(Composite parentComposite) {

		Composite middleComposite = new Composite(parentComposite, SWT.NONE);
		middleComposite.setLayout(new GridLayout(1, false));
		middleComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite topAddButtonComposite = new Composite(middleComposite, SWT.NONE);

		GridData gd_topAddButtonComposite = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);

		topAddButtonComposite.setLayoutData(gd_topAddButtonComposite);

		scrolledComposite = new ScrolledComposite(middleComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		scrolledComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scrolledComposite.setLayout(new GridLayout(1, false));

		GridData gd_scrolledComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_scrolledComposite.minimumHeight = 250;
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

		
		Label addLabel = widget.labelWidget(topAddButtonComposite, SWT.CENTER, new int[] { 184, 10, 20, 15 }, "",
				new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON));
		addLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				if (expandBar.getItemCount() > 1)
					for (ExpandItem expandItem : expandBar.getItems()) {
						expandItem.setExpanded(false);

					}

				List<FilterProperties> inputFieldList = new ArrayList<>();
				List<FilterProperties> outputList = new ArrayList<>();
				List<NameValueProperty> nameValueProperty = new ArrayList<>();
				int n = transformMapping.getMappingSheetRows().size() + 1;
				String operationID = Messages.OPERATION_ID_PREFIX + n;
				mappingSheetRow = new MappingSheetRow(inputFieldList, outputList, operationID, Messages.CUSTOM, "",
						nameValueProperty, false, "", false, "");

				transformMapping.getMappingSheetRows().add(mappingSheetRow);

				addExpandItem(scrolledComposite, mappingSheetRow, operationID);
			}
		});

		final Label deleteLabel = widget.labelWidget(topAddButtonComposite, SWT.CENTER, new int[] { 213, 10, 20, 15 },
				"", new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));
		deleteLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				if (transformMapping.getMappingSheetRows().isEmpty()) {
					WidgetUtility.errorMessage(Messages.OPERATION_LIST_EMPTY);

				} else {
					OperationClassDeleteDialog operationClassDeleteDialog = new OperationClassDeleteDialog(deleteLabel
							.getShell(), transformMapping.getMappingSheetRows(), expandBar);
					operationClassDeleteDialog.open();
					refreshOutputTable();
					showHideValidationMessage();

				}
			}
		});

		Label lblOperationsControl = new Label(topAddButtonComposite, SWT.NONE);
		lblOperationsControl.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		lblOperationsControl.setBounds(50, 10, 129, 28);
		lblOperationsControl.setText(Messages.OPERATION_CONTROL);
		if (!transformMapping.getMappingSheetRows().isEmpty()) {
			for (MappingSheetRow mappingSheetRow : transformMapping.getMappingSheetRows()) {
				addExpandItem(scrolledComposite, mappingSheetRow, mappingSheetRow.getOperationID());
				setDuplicateOperationInputFieldMap(mappingSheetRow);
			}

		}
		createMapAndPassthroughTable(middleComposite);

	}

	private void createMapAndPassthroughTable(Composite middleComposite) {
		Composite mappingTableComposite = new Composite(middleComposite, SWT.NONE);
		mappingTableComposite.setLayout(new GridLayout(2, false));
		GridData gd_mappingTableComposite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_mappingTableComposite.minimumHeight=200;
		
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
		
		mappingTableViewer.getTable().addControlListener(new ControlListener() {
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

			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub

			}
		});

		DragDropUtility.INSTANCE.applyDrop(mappingTableViewer,
				new DragDropTransformOpImp(this, transformMapping.getMapAndPassthroughField(), false,
						mappingTableViewer));

		Label lblNewLabel = new Label(labelComposite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblNewLabel.setText(Messages.MAP_FIELD);

		Label mapFieldAddLabel = widget.labelWidget(buttonComposite, SWT.CENTER, new int[] { 635, 10, 20, 15 }, "",
				new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON)

		);
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

				}

			}
		});

		Label mapFieldDeletLabel = widget.labelWidget(buttonComposite, SWT.CENTER, new int[] { 665, 10, 20, 15 }, "",
				new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));

		mapFieldDeletLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				Table table = mappingTableViewer.getTable();
				int temp = table.getSelectionIndex();
				int[] indexs = table.getSelectionIndices();
				if (temp == -1) {
					WidgetUtility.errorMessage(Messages.SelectRowToDelete);
				} else {
					table.remove(indexs);
					List<NameValueProperty> tempList = new ArrayList<NameValueProperty >();
					for (int index : indexs) {

						tempList.add(transformMapping.getMapAndPassthroughField().get(index));
					}
					for(NameValueProperty nameValueProperty:tempList)
					{	
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
		errorCompositeData.minimumHeight=70;
		
		
		errorComposite.setLayoutData(errorCompositeData);

		errorTableViewer = new TableViewer(errorComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		setTableViewer(errorTableViewer, errorComposite, new String[] { "Error Log" }, new ELTFilterContentProvider(),
				new OperationLabelProvider());
		errorTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		errorTableViewer.setLabelProvider(new ErrorLabelProvider());
		  errorTableViewer.setInput(errorLabelList);
		errorTableViewer.getTable().addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				Table table = (Table) e.widget;
				Rectangle area = table.getClientArea();
				int totalAreaWidth = area.width;
				int diff = totalAreaWidth - (table.getColumn(0).getWidth());
				table.getColumn(0).setWidth(diff + table.getColumn(0).getWidth());
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});
		
		
	}

	private void addExpandItem(ScrolledComposite scrollBarComposite, MappingSheetRow mappingSheetRow, String operationID) {

		ExpandItem expandItem = new ExpandItem(expandBar, SWT.V_SCROLL);
		expandItem.setExpanded(true);
		expandItem.setHeight(230);
		if (operationID != null)
			expandItem.setText(operationID);

		Composite expandItemComposite = new Composite(expandBar, SWT.NONE);
		expandItemComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		expandItemComposite.setVisible(true);
		expandItem.setControl(expandItemComposite);
		expandItemComposite.setLayout(new GridLayout(3, false));

		operationalInputFieldTableViewer = createOperationInputFieldTable(expandItemComposite, mappingSheetRow);

		createMiddleWidgets(expandItemComposite, expandItem, mappingSheetRow);
		createOperationOutputFieldTable(expandItemComposite, mappingSheetRow);

		scrollBarComposite.setContent(expandBar);
		scrollBarComposite.setMinSize(expandBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void createMiddleWidgets(Composite expandItemComposite, ExpandItem expandItem,
			final MappingSheetRow mappingSheetRow) {

		Composite innerComposite = new Composite(expandItemComposite, SWT.NONE);
		GridData gd_fileSelectComposite = new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1);
		gd_fileSelectComposite.heightHint = 29;
		gd_fileSelectComposite.widthHint = 360;
		innerComposite.setLayoutData(gd_fileSelectComposite);
		isParam = new Button(innerComposite, SWT.CHECK);
		operationClassTextBox = new Text(innerComposite, SWT.BORDER);
		operationClassTextBox.setBounds(104, 91, 150, 21);
		operationClassTextBox.setData(mappingSheetRow.getOperationID());
		expandItem.setData(OPERATION_CLASS_TEXT_BOX,operationClassTextBox);
		expandItem.setData(IS_PARAM, isParam);
		operationClassTextBox.addModifyListener(new ModifyListener() {
		
			@Override
			public void modifyText(ModifyEvent e) {
			 showHideValidationMessage();
			}
		});
		
		if (mappingSheetRow.getOperationClassPath() != null)
		operationClassTextBox.setText(mappingSheetRow.getOperationClassPath());

		operationClassTextBox.setEditable(false);
		operationClassTextBox.setData(mappingSheetRow.getOperationID());
		
		Label operationClassLabel = new Label(innerComposite, SWT.NONE);
		operationClassLabel.setBounds(24, 94, 62, 35);
		operationClassLabel.setText(Messages.OP_CLASS);

		operationIDTextBox = new Text(innerComposite, SWT.BORDER);
		operationIDTextBox.setBounds(104, 28, 150, 21);
		operationIDTextBox.setText(expandItem.getText());

		operationIDTextBox.setData(expandItem);
		
		mappingSheetRow.setOperationID(operationIDTextBox.getText());

		operationIDTextBox.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text textBox = (Text) e.widget;

				ExpandItem expandItem = (ExpandItem) textBox.getData();

				expandItem.setText(textBox.getText());
				mappingSheetRow.setOperationID(textBox.getText());
			 }
		});

		operationIDTextBox.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				Text textBox = (Text) e.widget;
				if(StringUtils.isBlank(textBox.getText()))
				{
					textBox.setText((String) textBox.getData("perviousValue"));
				}	
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				Text textBox=(Text) e.widget;
				textBox.setData("perviousValue",textBox.getText());
			}
		});
		
		
		Label operationIDLabel = new Label(innerComposite, SWT.NONE);
		operationIDLabel.setBounds(24, 28, 74, 30);
		operationIDLabel.setText(Messages.OPERATION_ID);

		final Button browseButton = new Button(innerComposite, SWT.NONE);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		browseButton.setBounds(284, 91, 35, 21);
		browseButton.setText("...");
		browseButton.setData("mappingSheet",mappingSheetRow);
		browseButton.setData("opertionClassTextbox", operationClassTextBox);
		browseButton.addSelectionListener(new SelectionAdapter() {
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				MappingSheetRow orignalMappingSheetRow = (MappingSheetRow) ((Button) e.widget).getData("mappingSheet");
				Text operationClassTextBox=(Text)((Button)e.widget).getData("opertionClassTextbox");
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
				Label inputAdd = (Label) text.getData(INPUT_ADD_BUTTON);
				Label inputDelete = (Label) text.getData(INPUT_DELETE_BUTTON);
				Label outputAdd = (Label) text.getData(OUTPUT_ADD_BUTTON);
				Label outputDelete = (Label) text.getData(OUTPUT_DELETE_BUTTON);

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

				        mappingSheetRow.getOutputList().clear();
						mappingSheetRow.getInputFields().clear();
						mappingSheetRow.setComboBoxValue(Messages.CUSTOM);
						mappingSheetRow.getNameValueProperty().clear();
						mappingSheetRow.setClassParameter(false);
						mappingSheetRow.setOperationClassPath("");

					} else
						text.setSelection(false);
				} else {
					parameterTextBox.setText("");
					mappingSheetRow.setWholeOperationParameter(text.getSelection());
					parameterTextBox.setEnabled(false);

					operationInputFieldTableViewer.getTable().setEnabled(true);

					operationOutputFieldTableViewer.getTable().setEnabled(true);

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
        
	}

	private void createOperationOutputFieldTable(Composite expandItemComposite, final MappingSheetRow mappingSheetRow) {

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
		
		DragDropTransformOpImp dragDropTransformOpImpnew = new DragDropTransformOpImp(this,
				transformMapping.getMappingSheetRows(), outputFieldViewer,
				transformMapping.getMapAndPassthroughField(), temporaryOutputFieldMap,
				mappingSheetRow.getOutputList(), mappingSheetRow.getInputFields(), true,
				operationalInputFieldTableViewer, operationOutputtableViewer);
		DragDropUtility.INSTANCE.applyDrop(operationalInputFieldTableViewer, dragDropTransformOpImpnew);

		Label addLabel = widget.labelWidget(operationalOutputFieldComposite, SWT.CENTER, new int[] { 60, 3, 20, 15 },
				"", new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON));
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

				}
			}

		});

		Label deleteLabel = widget.labelWidget(operationalOutputFieldComposite, SWT.CENTER,
				new int[] { 90, 3, 20, 15 }, "",
				new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));

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
			Label inputAdd = (Label) text.getData(INPUT_ADD_BUTTON);

			Label inputDelete = (Label) text.getData(INPUT_DELETE_BUTTON);
			Label outputAdd = (Label) text.getData(OUTPUT_ADD_BUTTON);
			Label outputDelete = (Label) text.getData(OUTPUT_DELETE_BUTTON);
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

	}

	public void refreshOutputTable() {
		  
		List<FilterProperties> validatorOutputFields = new ArrayList<>();
		
		  for (Map.Entry<String, List<FilterProperties>> entry: temporaryOutputFieldMap.entrySet()) {
			  for(FilterProperties filterproperty :entry.getValue())
			  {
				  if(!ParameterUtil.isParameter(filterproperty.getPropertyname()))
				  validatorOutputFields.add(filterproperty);
			  }  
		 }
		validatorOutputFields.clear();
		temporaryOutputFieldMap.clear();
		
		temporaryOutputFieldMap
				.put("MapAndPassThroughFields",convertNameValueToFilterProperties(transformMapping.getMapAndPassthroughField()));
		for (MappingSheetRow mappingSheetRow1 : transformMapping.getMappingSheetRows()) {
			temporaryOutputFieldMap.put(mappingSheetRow1.getOperationID(),mappingSheetRow1.getOutputList());

		}
		temporaryOutputFieldMap.put("OutputFields",transformMapping.getOutputFieldList());

		DragDropUtility.unionFilter(convertNameValueToFilterProperties(transformMapping.getMapAndPassthroughField()),
				validatorOutputFields);
		for (MappingSheetRow mappingSheetRow1 : transformMapping.getMappingSheetRows()) {
			List<FilterProperties> operationOutputFieldList=mappingSheetRow1.getOutputList();
			List<FilterProperties> nonParameterOutputFieldList=new ArrayList<>();  
            for(FilterProperties filterProperties : operationOutputFieldList)
            {
                  if(!ParameterUtil.isParameter(filterProperties.getPropertyname()))
                	  nonParameterOutputFieldList.add(filterProperties);
                 
            } 
            DragDropUtility.unionFilter(nonParameterOutputFieldList,validatorOutputFields);

		}
		DragDropUtility.unionFilter(transformMapping.getOutputFieldList(), validatorOutputFields);
		outputFieldViewer.setInput(validatorOutputFields);
		outputFieldViewer.refresh();
		mappingTableViewer.refresh();
	}

	
	
	
	

	public Map<String,List<String>> getDuplicateOutputFieldMap(Map<String,List<FilterProperties> > temporaryOutputFieldListTemp) {
		
		Set<String> setToCheckDuplicates = new HashSet<String>();
		
		for (Map.Entry<String, List<FilterProperties>> entry: temporaryOutputFieldListTemp.entrySet()) 
		{
			List<FilterProperties>  temporaryOutputFieldList=entry.getValue();
			List<String> duplicateFields=new ArrayList<>();
			for (FilterProperties filterProperties : temporaryOutputFieldList) {
			if (!setToCheckDuplicates.add(filterProperties.getPropertyname())) {
				duplicateFields.add(filterProperties.getPropertyname());
			}
			
		}
			duplicateFieldMap.put(entry.getKey(),duplicateFields);		
		}
		
		return duplicateFieldMap;
	}

	

	
	
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
    
   public void showHideValidationMessage()
   	{		
		if(errorTableViewer!=null)
		{
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
		
		
			for(ExpandItem item:expandBar.getItems() )
			{
				Text textBox=(Text)item.getData(OPERATION_CLASS_TEXT_BOX);
				Button isParam=(Button)item.getData(IS_PARAM);
				
				if(StringUtils.isBlank(textBox.getText()) && !isParam.getSelection())
				{
					errorLabel=new Label( errorTableViewer.getTable(), SWT.NONE);
				    errorLabel.setVisible(true); 
					errorLabel.setText("Operation Class must not be blank for"+" "+textBox.getData()); 	
					errorLabelList.add(errorLabel);
					
				}		
			} 	
			
	   errorTableViewer.getTable().setForeground(new Color(Display.getDefault(), 255, 0, 0));
	   errorTableViewer.refresh();
	   errorLabelList.clear();
		}
	}

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
		gd_operationInputFieldComposite.widthHint = 185;
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
		operationalInputFieldTableViewer.setCellModifier(new ELTCellModifier(operationalInputFieldTableViewer, this,
				mappingSheetRow));

		CellEditor[] editor=operationalInputFieldTableViewer.getCellEditors();
		fieldNameDecorator = WidgetUtility.addDecorator(editor[0].getControl(),Messages.FIELDNAME_SHOULD_NOT_BE_BLANK);
		isFieldNameAlphanumericDecorator=WidgetUtility.addDecorator(editor[0].getControl(),Messages.FIELDNAME_NOT_ALPHANUMERIC_ERROR);	
		
		editors[0].setValidator(new TransformCellEditorFieldValidator(fieldNameDecorator,isFieldNameAlphanumericDecorator));
		operationInputaddButton = widget.labelWidget(operationInputFieldComposite, SWT.CENTER, new int[] { 60, 3, 20,
				15 }, "", new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON));

		operationInputDeleteButton = widget.labelWidget(operationInputFieldComposite, SWT.CENTER, new int[] { 90, 3,
				20, 15 }, "", new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));
		
		isFieldNameAlphanumericDecorator.setMarginWidth(8);
		fieldNameDecorator.setMarginWidth(8);
		
		operationInputaddButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				FilterProperties f = new FilterProperties();
				f.setPropertyname("");
				if (!mappingSheetRow.getInputFields().contains(f)) {
					mappingSheetRow.getInputFields().add(f);

					operationalInputFieldTableViewer.refresh();
					int i = mappingSheetRow.getInputFields().size() == 0 ? mappingSheetRow.getInputFields().size()
							: mappingSheetRow.getInputFields().size() - 1;
					operationalInputFieldTableViewer.editElement(operationalInputFieldTableViewer.getElementAt(i), 0);

				}

			}

		});

		operationInputDeleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {

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
		return operationalInputFieldTableViewer;
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
			set.clear();
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
		Button cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
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
			TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
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
}
