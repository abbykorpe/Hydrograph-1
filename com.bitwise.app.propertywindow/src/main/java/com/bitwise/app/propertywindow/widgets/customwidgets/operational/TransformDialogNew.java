
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

package com.bitwise.app.propertywindow.widgets.customwidgets.operational;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.mapping.ATMapping;
import com.bitwise.app.common.datastructure.property.mapping.InputField;
import com.bitwise.app.common.datastructure.property.mapping.MappingSheetRow;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.utils.SWTResourceManager;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.inputtable.InputFieldColumnLabelProvider;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.inputtable.TableContentProvider;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTCellModifier;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterContentProvider;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterLabelProvider;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTSWTWidgets;
import com.bitwise.app.propertywindow.widgets.interfaces.IOperationClassDialog;
import com.bitwise.app.propertywindow.widgets.utility.DragDropUtility;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

public class TransformDialogNew extends Dialog implements IOperationClassDialog {

	private static final String OUTPUT_DELETE_BUTTON = "outputDeleteButton";
	private static final String OUTPUT_ADD_BUTTON = "outputAddButton";
	private static final String OPERATION_OUTPUT_FIELD_TABLE_VIEWER = "operationOutputFieldTableViewer";
	private static final String INPUT_DELET_BUTTON = "inputDeletButton";
	private static final String INPUT_ADD_BUTTON = "inputAddButton";
	private static final String OPERATION_INPUT_FIELD_TABLE_VIEWER = "operationInputFieldTableViewer";
	private static final String OPERATION_ID_TEXT_BOX = "operationIDTextBox";
	private static final String OPERATION_CLASS_TEXT_BOX = "operationClassTextBox";
	private static final String PARAMETER_TEXT_BOX = "parameterTextBox";
	private static final String BTN_NEW_BUTTON = "btnNewButton";
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
	private boolean isOKButtonPressed = false;
	private boolean isCancelButtonPressed = false;
	private Table tableViewerTable;
	private String componentName;
	private WidgetConfig widgetConfig;
	private Text text;
	private Button isParam;
	private TableViewer operationalInputFieldTableViewer;
	private Label operationInputaddButton;
	private Label operationInputDeleteButton;
	private Composite composite_1;
	private ScrolledComposite scrolledComposite;
	Map<Text, Button> opClassMap = new LinkedHashMap<Text, Button>();
	private TableViewer inputFieldTableViewer;
	private ATMapping atMapping;
	private TableViewer outputFieldViewer;
	private List<FilterProperties> temporaryOutputFieldList = new ArrayList<>();
	private MappingSheetRow mappingSheetRow;
	private static ControlDecoration fieldNameDecorator;

	public TransformDialogNew(Shell parentShell, String componentName, WidgetConfig widgetConfig, ATMapping atMapping) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL);
		this.atMapping = atMapping;

		this.componentName = componentName;
		this.widgetConfig = widgetConfig;
	}

	public TransformDialogNew() {
		super(new Shell());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		propertyDialogButtonBar = new PropertyDialogButtonBar(container);

		composite_1 = new Composite(container, SWT.NONE);
		composite_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));

		createInputFieldTable(composite_1);

		createOperationClassGrid(composite_1);

		createOutputFieldTable(composite_1);

		return container;
	}

	private void createInputFieldTable(Composite container) {
		composite_1.setLayout(new GridLayout(3, false));
		Composite inputFieldComposite = new Composite(container, SWT.NONE);
		inputFieldComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));

		inputFieldTableViewer = new TableViewer(inputFieldComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		setTableViewer(inputFieldTableViewer, inputFieldComposite, new String[] { Messages.OPERATIONAL_SYSTEM_FIELD },
				new TableContentProvider(), new OperationLabelProvider());
		inputFieldTableViewer.getTable().setBounds(0, 30, 229, 850);
		inputFieldTableViewer.setInput(atMapping.getInputFields());
		inputFieldTableViewer.getTable().getColumn(0).setWidth(221);
		DragDropUtility.INSTANCE.applyDragFromTableViewer(inputFieldTableViewer.getTable());

		inputFieldTableViewer.setLabelProvider(new InputFieldColumnLabelProvider());
		inputFieldTableViewer.setInput(atMapping.getInputFields());

	}

	private void createOutputFieldTable(Composite composite) {

		Composite outputFieldComposite = new Composite(composite, SWT.NONE);
		outputFieldComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		outputFieldViewer = new TableViewer(outputFieldComposite, SWT.BORDER | SWT.FULL_SELECTION);
		setTableViewer(outputFieldViewer, outputFieldComposite, new String[] { Messages.OUTPUT_FIELD },
				new ELTFilterContentProvider(), new OperationLabelProvider());
		outputFieldViewer.getTable().setBounds(0, 30, 253, 850);
		outputFieldViewer.getTable().getColumn(0).setWidth(249);
		outputFieldViewer.setCellModifier(new ELTCellModifier(outputFieldViewer));
		outputFieldViewer.setLabelProvider(new ELTFilterLabelProvider());

		refreshOutputTable();
		Label addLabel = widget.labelWidget(outputFieldComposite, SWT.CENTER, new int[] { 130, 10, 20, 15 }, "",
				new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON));
		addLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				FilterProperties f = new FilterProperties();
				f.setPropertyname("");
				if (!atMapping.getOutputFieldList().contains(f)) {

					temporaryOutputFieldList.add(f);

					outputFieldViewer.refresh();

					int i = temporaryOutputFieldList.size() == 0 ? temporaryOutputFieldList.size()
							: temporaryOutputFieldList.size() - 1;
					outputFieldViewer.editElement(outputFieldViewer.getElementAt(i), 0);
					atMapping.getOutputFieldList().add(
							temporaryOutputFieldList.get(temporaryOutputFieldList.size() - 1));

				}
			}
		});
		Label deletLabel = widget.labelWidget(outputFieldComposite, SWT.CENTER, new int[] { 160, 10, 20, 15 }, "",
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

						tempList.add(temporaryOutputFieldList.get(index));
					}
					temporaryOutputFieldList.removeAll(tempList);
					atMapping.getOutputFieldList().removeAll(tempList);
					refreshOutputTable();
				}
			}

		});

	}

	private void createOperationClassGrid(Composite parentComposite) {

		Composite middleComposite = new Composite(parentComposite, SWT.NONE);
		middleComposite.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		middleComposite.setLayout(new GridLayout(1, false));

		Composite topAddButtonComposite = new Composite(middleComposite, SWT.NONE);
		GridData gd_topAddButtonComposite = new GridData(SWT.TOP, SWT.CENTER, false, false, 1, 1);
		gd_topAddButtonComposite.heightHint = 48;
		gd_topAddButtonComposite.widthHint = 745;
		topAddButtonComposite.setLayoutData(gd_topAddButtonComposite);

		scrolledComposite = new ScrolledComposite(middleComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		scrolledComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_scrolledComposite = new GridData(SWT.TOP, SWT.CENTER, false, false, 1, 1);
		gd_scrolledComposite.widthHint = 730;
		gd_scrolledComposite.heightHint = 513;
		scrolledComposite.setLayoutData(gd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setVisible(true);

		expandBar = new ExpandBar(scrolledComposite, SWT.NONE);
		expandBar.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		expandBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		expandBar.setVisible(true);

		Label addLabel = widget.labelWidget(topAddButtonComposite, SWT.CENTER, new int[] { 315, 10, 20, 15 }, "",
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
				int n = atMapping.getMappingSheetRows().size() + 1;
				String operationID = Messages.OPERATION_ID_PREFIX + n;
				mappingSheetRow = new MappingSheetRow(inputFieldList, outputList, operationID, Messages.CUSTOM, "",
						nameValueProperty, false, "", false);

				atMapping.getMappingSheetRows().add(mappingSheetRow);

				addExpandItem(scrolledComposite, mappingSheetRow, operationID);
			}
		});

		final Label deleteLabel = widget.labelWidget(topAddButtonComposite, SWT.CENTER, new int[] { 345, 10, 20, 15 },
				"", new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));
		deleteLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				if (atMapping.getMappingSheetRows().isEmpty()) {
					WidgetUtility.errorMessage(Messages.OPERATION_LIST_EMPTY);

				} else {
					OperationClassDeleteDialog operationClassDeleteDialog = new OperationClassDeleteDialog(deleteLabel
							.getShell(), atMapping.getMappingSheetRows(), expandBar);
					operationClassDeleteDialog.open();
					refreshOutputTable();

				}
			}
		});
		Button btnPull = new Button(topAddButtonComposite, SWT.NONE);
		btnPull.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnPull.setBounds(573, 10, 129, 25);
		btnPull.setText(Messages.PULL_BUTTON_LABEL);

		Label lblOperationsControl = new Label(topAddButtonComposite, SWT.NONE);
		lblOperationsControl.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		lblOperationsControl.setBounds(181, 10, 129, 28);
		lblOperationsControl.setText(Messages.OPERATION_CONTROL);
		
		if (!atMapping.getMappingSheetRows().isEmpty()) {
			for (MappingSheetRow mappingSheetRow : atMapping.getMappingSheetRows()) {
				addExpandItem(scrolledComposite, mappingSheetRow, mappingSheetRow.getOperationID());
			}

		}
		createMapAndPassthroughTable(middleComposite);

	}

	private void createMapAndPassthroughTable(Composite middleComposite) {
		Composite mappingTableComposite = new Composite(middleComposite, SWT.NONE);
		GridData gd_mappingTableComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_mappingTableComposite.heightHint = 289;
		gd_mappingTableComposite.widthHint = 753;
		mappingTableComposite.setLayoutData(gd_mappingTableComposite);
		atMapping.getMapAndPassthroughField();

		final TableViewer mappingTableViewer = new TableViewer(mappingTableComposite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		setTableViewer(mappingTableViewer, mappingTableComposite, new String[] { Messages.SOURCE, Messages.TARGET },
				new ELTFilterContentProvider(), new OperationLabelProvider());
		mappingTableViewer.setLabelProvider(new PropertyLabelProvider());
		mappingTableViewer.setCellModifier(new PropertyGridCellModifier(this, mappingTableViewer));
		mappingTableViewer.getTable().setBounds(10, 32, 733, 250);
		mappingTableViewer.setInput(atMapping.getMapAndPassthroughField());

		DragDropUtility.INSTANCE.applyDrop(mappingTableViewer,
				new DragDropTransformOpImp(this, atMapping.getMapAndPassthroughField(), false, mappingTableViewer));

		Label mapFieldAddLabel = widget.labelWidget(mappingTableComposite, SWT.CENTER, new int[] { 635, 10, 20, 15 },
				"", new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON)

		);

		mapFieldAddLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				NameValueProperty nameValueProperty = new NameValueProperty();
				nameValueProperty.setPropertyName("");
				nameValueProperty.setPropertyValue("");
				if (!atMapping.getMapAndPassthroughField().contains(nameValueProperty)) {
					atMapping.getMapAndPassthroughField().add(nameValueProperty);
					mappingTableViewer.refresh();
					int currentSize = atMapping.getMapAndPassthroughField().size();
					int i = currentSize == 0 ? currentSize : currentSize - 1;
					mappingTableViewer.editElement(mappingTableViewer.getElementAt(i), 0);
				}

			}
		});

		Label mapFieldDeletLabel = widget.labelWidget(mappingTableComposite, SWT.CENTER, new int[] { 665, 10, 20, 15 },
				"", new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));

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
					ArrayList tempList = new ArrayList();
					for (int index : indexs) {

						tempList.add(atMapping.getMapAndPassthroughField().get(index));
					}
					atMapping.getMapAndPassthroughField().removeAll(tempList);
					refreshOutputTable();
				}
			}
		});

		Label lblNewLabel = new Label(mappingTableComposite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblNewLabel.setBounds(355, 10, 95, 24);
		lblNewLabel.setText(Messages.MAP_FIELD);

		mappingTableViewer.getTable().getColumn(0).setWidth(362);
		mappingTableViewer.getTable().getColumn(1).setWidth(362);
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
		GridData gd_fileSelectComposite = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_fileSelectComposite.heightHint = 29;
		gd_fileSelectComposite.widthHint = 360;
		innerComposite.setLayoutData(gd_fileSelectComposite);

		operationClassTextBox = new Text(innerComposite, SWT.BORDER);
		operationClassTextBox.setBounds(104, 91, 150, 21);
		if (mappingSheetRow.getOperationClassPath() != null)
			operationClassTextBox.setText(mappingSheetRow.getOperationClassPath());

		operationClassTextBox.setEditable(false);

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
		browseButton.setData(mappingSheetRow);
		browseButton.addSelectionListener(new SelectionAdapter() {
		

			@Override
			public void widgetSelected(SelectionEvent e) {
				MappingSheetRow orignalMappingSheetRow = (MappingSheetRow) ((Button) e.widget).getData();
				OperationClassDialog operationClassDialog = new OperationClassDialog(browseButton.getShell(),
						componentName, orignalMappingSheetRow, propertyDialogButtonBar, widgetConfig);
				operationClassDialog.open();
                operationClassTextBox.setText(operationClassDialog.getMappingSheetRow().getOperationClassPath());
				orignalMappingSheetRow.setComboBoxValue(operationClassDialog.getMappingSheetRow().getComboBoxValue());
				orignalMappingSheetRow.setOperationClassPath(operationClassDialog.getMappingSheetRow()
						.getOperationClassPath());
				orignalMappingSheetRow.setClassParameter(operationClassDialog.getMappingSheetRow().isClassParameter());
				orignalMappingSheetRow.setNameValueProperty(operationClassDialog.getMappingSheetRow()
						.getNameValueProperty());
                
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

		isParam = new Button(innerComposite, SWT.CHECK);
		isParam.setData(PARAMETER_TEXT_BOX, text);
		isParam.setData(OPERATION_CLASS_TEXT_BOX, operationClassTextBox);
		isParam.setData(OPERATION_ID_TEXT_BOX, operationIDTextBox);
		isParam.setData(BTN_NEW_BUTTON, browseButton);
		isParam.setData(OPERATION_INPUT_FIELD_TABLE_VIEWER, operationalInputFieldTableViewer);
		isParam.setData(INPUT_ADD_BUTTON, operationInputaddButton);
		isParam.setData(INPUT_DELET_BUTTON, operationInputDeleteButton);
		isParam.setSelection(mappingSheetRow.isWholeOperationParameter());
		final List<MappingSheetRow> mappingsheetRowList = atMapping.getMappingSheetRows();
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
				Label inputDelete = (Label) text.getData(INPUT_DELET_BUTTON);
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
						inputAdd.setEnabled(false);
						inputDelete.setEnabled(false);
						outputAdd.setEnabled(false);
						outputDelete.setEnabled(false);

						mappingSheetRow.getInputFields().clear();
						mappingSheetRow.getOutputList().clear();
						mappingSheetRow.setComboBoxValue(Messages.CUSTOM);
						mappingSheetRow.getNameValueProperty().clear();
						mappingSheetRow.setClassParameter(false);
						mappingSheetRow.setOperationClassPath("");

						refreshOutputTable();
					} else
						text.setSelection(false);
				} else {
					parameterTextBox.setText("");
					mappingSheetRow.setWholeOperationParameter(text.getSelection());
					parameterTextBox.setEnabled(false);

					operationInputFieldTableViewer.getTable().setEnabled(true);

					operationOutputFieldTableViewer.getTable().setEnabled(true);

					operationClassTextBox.setEnabled(true);

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

		final TableViewer operationalOutputFieldTableViewer = new TableViewer(operationalOutputFieldComposite,
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

		DragDropTransformOpImp dragDropTransformOpImpnew = new DragDropTransformOpImp(this,
				atMapping.getMappingSheetRows(), outputFieldViewer, atMapping.getMapAndPassthroughField(),
				temporaryOutputFieldList, mappingSheetRow.getOutputList(), mappingSheetRow.getInputFields(), true,
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
				if (temp == -1) {
					WidgetUtility.errorMessage(Messages.SelectRowToDelete);
				} else {
					table.remove(indexs);
					ArrayList tempList = new ArrayList();
					for (int index : indexs) {

						tempList.add(mappingSheetRow.getOutputList().get(index));
					}
					mappingSheetRow.getOutputList().removeAll(tempList);
					refreshOutputTable();
				}
			}

		});
		isParam.setData(OUTPUT_ADD_BUTTON, addLabel);
		isParam.setData(OUTPUT_DELETE_BUTTON, deleteLabel);
		if (mappingSheetRow.isWholeOperationParameter()) {
			Button text = (Button) isParam;
			Text parameterTextBox = (Text) text.getData(PARAMETER_TEXT_BOX);
			TableViewer operationInputFieldTableViewer = (TableViewer) text.getData(OPERATION_INPUT_FIELD_TABLE_VIEWER);
			TableViewer operationOutputFieldTableViewer = (TableViewer) text
					.getData(OPERATION_OUTPUT_FIELD_TABLE_VIEWER);
			Text operationClassTextBox = (Text) text.getData(OPERATION_CLASS_TEXT_BOX);
			Text operationIDTextBox = (Text) text.getData(OPERATION_ID_TEXT_BOX);
			Button btnNewButton = (Button) text.getData(BTN_NEW_BUTTON);
			Label inputAdd = (Label) text.getData(INPUT_ADD_BUTTON);
			Label inputDelete = (Label) text.getData(INPUT_DELET_BUTTON);
			Label outputAdd = (Label) text.getData(OUTPUT_ADD_BUTTON);
			Label outputDelete = (Label) text.getData(OUTPUT_DELETE_BUTTON);

			parameterTextBox.setEnabled(true);

			operationInputFieldTableViewer.getTable().setEnabled(false);

			operationOutputFieldTableViewer.getTable().setEnabled(false);

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
		temporaryOutputFieldList.clear();
		temporaryOutputFieldList.addAll(convertNameValueToFilterProperties(atMapping.getMapAndPassthroughField()));
		for (MappingSheetRow mappingSheetRow1 : atMapping.getMappingSheetRows()) {

			temporaryOutputFieldList.addAll(mappingSheetRow1.getOutputList());
		}
		temporaryOutputFieldList.addAll(atMapping.getOutputFieldList());
		outputFieldViewer.setInput(temporaryOutputFieldList);
		outputFieldViewer.refresh();

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
		GridData gd_operationInputFieldComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
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
		operationalInputFieldTableViewer.setCellModifier(new ELTCellModifier(operationalInputFieldTableViewer));

		CellEditor[] editors = operationalInputFieldTableViewer.getCellEditors();

		fieldNameDecorator = WidgetUtility.addDecorator(editors[0].getControl(), Messages.DUPLICATE_FIELDS);
		editors[0].setValidator(new TransformCellEditorFieldValidator(operationalInputFieldTableViewer.getTable(),
				mappingSheetRow.getInputFields(), fieldNameDecorator, propertyDialogButtonBar));
		operationInputaddButton = widget.labelWidget(operationInputFieldComposite, SWT.CENTER, new int[] { 60, 3, 20,
				15 }, "", new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON));

		operationInputDeleteButton = widget.labelWidget(operationInputFieldComposite, SWT.CENTER, new int[] { 90, 3,
				20, 15 }, "", new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));

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
				int[] indexs = table.getSelectionIndices();
				if (temp == -1) {
					WidgetUtility.errorMessage(Messages.SelectRowToDelete);
				} else {
					table.remove(indexs);
					ArrayList tempList = new ArrayList();
					for (int index : indexs) {

						tempList.add(mappingSheetRow.getInputFields().get(index));
					}
					mappingSheetRow.getInputFields().removeAll(tempList);

				}
			}

		});
		return operationalInputFieldTableViewer;
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
		return new Point(1500, 1500);
	}

	@Override
	protected void okPressed() {

		atMapping = new ATMapping((List<InputField>) inputFieldTableViewer.getInput(), atMapping.getMappingSheetRows(),
				atMapping.getMapAndPassthroughField(), atMapping.getOutputFieldList());
		super.okPressed();
	}

	
	public ATMapping getATMapping() {
		return atMapping;
	}

	@Override
	public void pressOK() {
		isOKButtonPressed = true;
		okPressed();
	}

	@Override
	public void pressCancel() {
		isCancelButtonPressed = true;
		cancelPressed();
	}

	/**
	 * 
	 * returns true if ok button pressed from code
	 * 
	 * @return boolean
	 */
	public boolean isOkPressed() {
		return isOKButtonPressed;
	}

	/**
	 * 
	 * returns true of cancel button pressed from code
	 * 
	 * @return boolean
	 */
	public boolean isCancelPressed() {
		return isCancelButtonPressed;
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

		// enables the tab functionality
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
