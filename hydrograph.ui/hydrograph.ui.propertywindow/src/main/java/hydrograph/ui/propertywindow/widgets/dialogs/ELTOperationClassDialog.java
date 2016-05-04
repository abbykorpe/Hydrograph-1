
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
package hydrograph.ui.propertywindow.widgets.dialogs;


import hydrograph.ui.common.component.config.Operations;
import hydrograph.ui.common.component.config.TypeInfo;
import hydrograph.ui.common.datastructures.tooltip.TootlTipErrorMessage;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.OperationClassProperty;
import hydrograph.ui.propertywindow.messagebox.ConfirmCancelMessageBox;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.operational.OperationLabelProvider;
import hydrograph.ui.propertywindow.widgets.customwidgets.operational.PropertyGridCellModifier;
import hydrograph.ui.propertywindow.widgets.customwidgets.operational.PropertyLabelProvider;
import hydrograph.ui.propertywindow.widgets.filterproperty.ELTFilterContentProvider;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultCheckBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultCombo;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTSWTWidgets;
import hydrograph.ui.propertywindow.widgets.interfaces.IOperationClassDialog;
import hydrograph.ui.propertywindow.widgets.utility.FilterOperationClassUtility;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;

public class ELTOperationClassDialog extends Dialog implements IOperationClassDialog {

	private static final String PATH = "path";
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	private boolean okPressed;
	private Table tableViewerTable;
	private CellEditor[] editors;
	private Text fileName;
	private Combo operationClasses;
	private Button isParameterCheckBox;
	private Button applyButton;
	private Button okButton;
	private Composite container;
	private boolean isOkPressed;
	private boolean isCancelPressed;
	private boolean isApplyPressed;
	private boolean closeDialog;
	private PropertyDialogButtonBar operationClassDialogButtonBar;
	private TootlTipErrorMessage tootlTipErrorMessage = new TootlTipErrorMessage();
	private WidgetConfig widgetConfig;
	private String componentName;
	private ControlDecoration alphanumericDecorator;
	private ControlDecoration emptyDecorator;
	private ControlDecoration parameterDecorator;
	private boolean isYesPressed = false;
	private boolean isNoPressed = false;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private PropertyDialogButtonBar opeartionClassDialogButtonBar;
	private Button cancelButton;
    private OperationClassProperty operationClassProperty;

	private Composite buttonComposite;
	private ELTSWTWidgets widget = new ELTSWTWidgets();

	public ELTOperationClassDialog(Shell parentShell,PropertyDialogButtonBar propertyDialogButtonBar, OperationClassProperty operationClassProperty, WidgetConfig widgetConfig, String componentName) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		this.operationClassProperty = operationClassProperty;
		this.widgetConfig = widgetConfig;
		this.componentName=componentName;
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		opeartionClassDialogButtonBar = new PropertyDialogButtonBar(parentShell);
		widget = new ELTSWTWidgets();
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	public Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);

		container.setLayout(new GridLayout(1, false));

		container.getShell().setText(Messages.OPERATION_CLASS);

		

		operationClassDialogButtonBar = new PropertyDialogButtonBar(container);

		Composite composite = new Composite(container, SWT.BORDER);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_composite.heightHint = 104;
		gd_composite.widthHint = 522;
		composite.setLayoutData(gd_composite);

		composite.setLayout(new GridLayout(1, false));

		AbstractELTWidget fileNameText = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(150);

		AbstractELTWidget isParameterCheckbox = new ELTDefaultCheckBox(Constants.IS_PARAMETER).checkBoxLableWidth(100);

		Operations operations = XMLConfigUtil.INSTANCE.getComponent(componentName).getOperations();
		List<TypeInfo> typeInfos = operations.getStdOperation();
		String optionsOfComboOfOperationClasses[] = new String[typeInfos.size() + 1];
		optionsOfComboOfOperationClasses[0] = Messages.CUSTOM;
		for (int i = 0; i < typeInfos.size(); i++) {
			optionsOfComboOfOperationClasses[i + 1] = typeInfos.get(i).getName();
		}
		AbstractELTWidget comboOfOperationClasses = new ELTDefaultCombo().defaultText(optionsOfComboOfOperationClasses)
				.comboBoxWidth(90);

		FilterOperationClassUtility.createOperationalClass(composite, operationClassDialogButtonBar,
				comboOfOperationClasses, isParameterCheckbox, fileNameText, tootlTipErrorMessage, widgetConfig, this,
				propertyDialogButtonBar, opeartionClassDialogButtonBar);
		fileName = (Text) fileNameText.getSWTWidgetControl();
		fileName.setData(PATH, operationClassProperty.getOperationClassFullPath());
		operationClasses = (Combo) comboOfOperationClasses.getSWTWidgetControl();
        
		FilterOperationClassUtility.enableAndDisableButtons(true, false);
		FilterOperationClassUtility.setComponentName(componentName);
		isParameterCheckBox = (Button) isParameterCheckbox.getSWTWidgetControl();
		alphanumericDecorator = WidgetUtility.addDecorator(fileName, Messages.CHARACTERSET);
		emptyDecorator = WidgetUtility.addDecorator(fileName, Messages.OperationClassBlank);
		parameterDecorator = WidgetUtility.addDecorator(fileName, Messages.PARAMETER_ERROR);

		buttonComposite = new Composite(container, SWT.NONE);
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_3.heightHint = 44;
		gd_composite_3.widthHint = 525;
		buttonComposite.setLayoutData(gd_composite_3);

		Composite nameValueComposite = new Composite(container, SWT.None);
		GridData gd_nameValueComposite = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_nameValueComposite.widthHint = 526;
		gd_nameValueComposite.heightHint = 251;
		nameValueComposite.setLayoutData(gd_nameValueComposite);

		nameValueComposite.setLayout(new GridLayout(1, false));

		final TableViewer nameValueTableViewer = new TableViewer(nameValueComposite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		Table table_2 = nameValueTableViewer.getTable();
		GridData gd_table_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_table_2.heightHint = 180;
		gd_table_2.widthHint = 499;
		table_2.setLayoutData(gd_table_2);
		setTableViewer(nameValueTableViewer, nameValueComposite, new String[] {
				Messages.PROPERTY_NAME, Messages.PROPERTY_VALUE }, new ELTFilterContentProvider(),
				new OperationLabelProvider());
		nameValueTableViewer.setLabelProvider(new PropertyLabelProvider());
		nameValueTableViewer.setCellModifier(new PropertyGridCellModifier(nameValueTableViewer,operationClassDialogButtonBar));
		nameValueTableViewer.setInput(operationClassProperty.getNameValuePropertyList());
		table_2.getColumn(0).setWidth(252);
		table_2.getColumn(1).setWidth(259);

		Label addButton = widget.labelWidget(buttonComposite, SWT.CENTER, new int[] { 360, 17, 20, 15 }, "", new Image(
				null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON));
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				NameValueProperty nameValueProperty = new NameValueProperty();
				nameValueProperty.setPropertyName("");
				nameValueProperty.setPropertyValue("");
				if (!operationClassProperty.getNameValuePropertyList().contains(nameValueProperty)) {
					operationClassProperty.getNameValuePropertyList().add(nameValueProperty);
					nameValueTableViewer.refresh();
					int currentSize = operationClassProperty.getNameValuePropertyList().size();
					int i = currentSize == 0 ? currentSize : currentSize - 1;
					nameValueTableViewer.editElement(nameValueTableViewer.getElementAt(i), 0);
					applyButton.setEnabled(true);
				}

			}

		});

		Label deleteButton = widget.labelWidget(buttonComposite, SWT.CENTER, new int[] { 390, 17, 20, 15 }, "",
				new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));

		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {

				Table table = nameValueTableViewer.getTable();
				int temp = table.getSelectionIndex();
				int[] indexs = table.getSelectionIndices();
				if (temp == -1) {
					WidgetUtility.errorMessage(Messages.SelectRowToDelete);
				} else {
					table.remove(indexs);
					ArrayList tempList = new ArrayList();
					for (int index : indexs) {

						tempList.add(operationClassProperty.getNameValuePropertyList().get(index));
					}
					operationClassProperty.getNameValuePropertyList().removeAll(tempList);
					applyButton.setEnabled(true);
				}
			}

		});

		Label upButton = widget.labelWidget(buttonComposite, SWT.CENTER, new int[] { 420, 17, 20, 15 }, "", new Image(
				null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.UP_ICON));

		upButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Table table = nameValueTableViewer.getTable();
				int[] indexes = table.getSelectionIndices();
				for (int index : indexes) {

					if (index > 0) {
						Collections.swap(operationClassProperty.getNameValuePropertyList(), index, index - 1);
						nameValueTableViewer.refresh();
						applyButton.setEnabled(true);
					}
				}
			}

		});
		Label downButton = widget.labelWidget(buttonComposite, SWT.CENTER, new int[] { 450, 17, 20, 15 }, "",
				new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DOWN_ICON));
		downButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Table table = nameValueTableViewer.getTable();
				int[] indexes = table.getSelectionIndices();
				for (int i = indexes.length - 1; i > -1; i--) {

					if (indexes[i] < operationClassProperty.getNameValuePropertyList().size() - 1) {
						Collections.swap(operationClassProperty.getNameValuePropertyList(), indexes[i], indexes[i] + 1);
						nameValueTableViewer.refresh();
						applyButton.setEnabled(true);

					}
				}
			}

		});

		populateWidget();
		return container;
	}

	public void pressOK() {
		okPressed();
		isYesPressed = true;
	}

	/**
	 * Populate widget.
	 */
	public void populateWidget() {
		
		if (operationClassProperty.getOperationClassPath()!=null && StringUtils.isNotBlank(operationClassProperty.getOperationClassPath())) 
		{
			fileName.setBackground(new Color(Display.getDefault(), 255, 255, 255));
			emptyDecorator.hide();
			operationClassProperty.setComboBoxValue(operationClassProperty.getComboBoxValue());
			fileName.setText(operationClassProperty.getOperationClassPath());
			operationClasses.setText(operationClassProperty.getComboBoxValue());
			isParameterCheckBox.setSelection(operationClassProperty.isParameter());
			if (!StringUtils.equalsIgnoreCase(Messages.CUSTOM, operationClassProperty.getComboBoxValue())) {
				fileName.setEnabled(false);
				FilterOperationClassUtility.enableAndDisableButtons(false, false);
				isParameterCheckBox.setEnabled(false);
			} else {
				isParameterCheckBox.setEnabled(true);
				if (isParameterCheckBox.getSelection()) {
					FilterOperationClassUtility.enableAndDisableButtons(true, true);
				}
			}
		} else {
			fileName.setBackground(new Color(Display.getDefault(), 255, 255, 204));
			operationClasses.select(0);
			isParameterCheckBox.setEnabled(false);
		}
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		createApplyButton(parent);

		operationClassDialogButtonBar.setPropertyDialogButtonBar(okButton, applyButton, cancelButton);
		alphanumericDecorator.hide();
		parameterDecorator.hide();
		isParameterCheckBox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isParameterCheckBox.getSelection()) {
					hasTextBoxAlphanumericCharactorsOnly(fileName.getText());
					if (StringUtils.isNotBlank(fileName.getText()) && !fileName.getText().startsWith("@{")
							&& !fileName.getText().endsWith("}")) {
						fileName.setText("@{" + fileName.getText() + "}");
						fileName.setBackground(new Color(Display.getDefault(), 255, 255, 255));
					}
				} else {
					if (StringUtils.isNotBlank(fileName.getText()) && fileName.getText().startsWith("@{")) {
						fileName.setText(fileName.getText().substring(2, fileName.getText().length() - 1));
						fileName.setBackground(new Color(Display.getDefault(), 255, 255, 255));
					}
					okButton.setEnabled(true);
					applyButton.setEnabled(true);
					alphanumericDecorator.hide();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		fileName.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				String currentText = ((Text) e.widget).getText();
				String textBoxValue = (currentText.substring(0, e.start) + e.text + currentText.substring(e.end))
						.trim();
				if (isParameterCheckBox.getSelection()) {
					if (StringUtils.isNotBlank(textBoxValue)
							&& (!textBoxValue.startsWith("@{") || !textBoxValue.endsWith("}"))) {
						((Text) e.widget).setBackground(new Color(Display.getDefault(), 255, 255, 255));
						parameterDecorator.show();
						emptyDecorator.hide();
						okButton.setEnabled(false);
						applyButton.setEnabled(false);
					} else {
						((Text) e.widget).setBackground(new Color(Display.getDefault(), 255, 255, 204));
						emptyDecorator.show();
						parameterDecorator.hide();
						okButton.setEnabled(true);
						hasTextBoxAlphanumericCharactorsOnly(textBoxValue);
					}
				} else {
					if (StringUtils.isNotBlank(textBoxValue)) {
						((Text) e.widget).setBackground(new Color(Display.getDefault(), 255, 255, 255));
						applyButton.setEnabled(true);
						isParameterCheckBox.setEnabled(true);
						emptyDecorator.hide();
					} else {
						((Text) e.widget).setBackground(new Color(Display.getDefault(), 255, 255, 204));
						isParameterCheckBox.setEnabled(false);
						emptyDecorator.show();
					}
				}
			}
		});

	}

	private void hasTextBoxAlphanumericCharactorsOnly(String textBoxValue) {
		if (StringUtils.isNotBlank(textBoxValue)) {
			fileName.setBackground(new Color(Display.getDefault(), 255, 255, 255));
			emptyDecorator.hide();
			Matcher matchs = Pattern.compile(Constants.REGEX).matcher(textBoxValue);
			if (!matchs.matches()) {
				alphanumericDecorator.show();
				okButton.setEnabled(false);
				applyButton.setEnabled(false);
			} else {
				alphanumericDecorator.hide();
				okButton.setEnabled(true);
				applyButton.setEnabled(true);
			}
		} else {
			fileName.setBackground(new Color(Display.getDefault(), 255, 255, 204));
			emptyDecorator.show();
			alphanumericDecorator.hide();
			okButton.setEnabled(false);
			applyButton.setEnabled(false);
		}
	}

	private void createApplyButton(Composite parent) {
		applyButton = createButton(parent, IDialogConstants.NO_ID, "Apply", false);
		disableApplyButton();
		opeartionClassDialogButtonBar.setPropertyDialogButtonBar(okButton, applyButton, cancelButton);
	}

	private void disableApplyButton() {
		applyButton.setEnabled(false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(540, 460);
	}

	

	@Override
	protected void cancelPressed() {
		if (applyButton.isEnabled()) {

			if (!isNoPressed) {
				ConfirmCancelMessageBox confirmCancelMessageBox = new ConfirmCancelMessageBox(container);
				MessageBox confirmCancleMessagebox = confirmCancelMessageBox.getMessageBox();

				if (confirmCancleMessagebox.open() == SWT.OK) {
					closeDialog=super.close();
				}
			} else {
				closeDialog=super.close();
			}

		} else {
			closeDialog=super.close();
		}
		isCancelPressed=true;
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
	
	@Override
	protected void okPressed() {
		operationClassProperty = new OperationClassProperty(operationClasses.getText(), fileName.getText(),
				isParameterCheckBox.getSelection(), (String) fileName.getData("path"),this.operationClassProperty.getNameValuePropertyList());
		okPressed=true;
		super.okPressed();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == 3){
			operationClassProperty = new OperationClassProperty(operationClasses.getText(), fileName.getText(),
					isParameterCheckBox.getSelection(), (String) fileName.getData("path"),this.operationClassProperty.getNameValuePropertyList());
			applyButton.setEnabled(false);
			isApplyPressed=true;
		}else{
			super.buttonPressed(buttonId);
		}
	}
	
	public OperationClassProperty getOperationClassProperty()  {
		return operationClassProperty;
	}
	
	public String getTootlTipErrorMessage() {
		return tootlTipErrorMessage.getErrorMessage();
	}
	
	/**
	 * 
	 * returns true if ok button pressed from code
	 * 
	 * @return boolean
	 */
	public boolean isOKPressed(){
		return isOkPressed;
	}

	public boolean isApplyPressed(){
		return isApplyPressed;
	}
	@Override
	public void pressCancel() {
		isCancelPressed=true;
		cancelPressed();
	}
	
	/**
	 * 
	 * returns true if cancel button pressed from code
	 * 
	 * @return boolean
	 */
	public boolean isCancelPressed(){
		return isCancelPressed;
	}
	
	@Override
	public boolean close() {
		if(!okPressed){
			cancelPressed();			
			return closeDialog;
		}else{
			return super.close();
		}		
	}
	
}
