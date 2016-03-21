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

 
package com.bitwise.app.propertywindow.widgets.customwidgets.secondarykeys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ImagePathConstant;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.EditButtonWithLabelConfig;

/**
 * 
 * Class to create Secondary column dialog
 * 
 * @author Bitwise
 *
 */

public class SecondaryColumnKeysDialog extends Dialog {
	
	private List<SecondaryColumnKeysInformation> propertyLst;
	private static final String COLUMNNAME = "Column Name"; //$NON-NLS-1$
	private static final String SORTORDER = "Sort Order"; //$NON-NLS-1$
	private Map<String, String> secondaryColumnsMap;
	public static final String[] PROPS = { COLUMNNAME, SORTORDER };
	private Label lblPropertyError;
	private TableViewer targetTableViewer;
	private boolean isAnyUpdatePerformed;
	private Table sourceTable;
	private Table targetTable;
	private DragSource dragSource;
	private DropTarget dropTarget;
	private List<String> sourceFieldsList;
	private EditButtonWithLabelConfig buttonWithLabelConfig;
	private PropertyDialogButtonBar propertyDialogButtonBar;

	private boolean closeDialog;
	private boolean okPressed;
	
	public SecondaryColumnKeysDialog(Shell parentShell, PropertyDialogButtonBar propertyDialogButtonBar, EditButtonWithLabelConfig buttonWithLabelConfig) {
		super(parentShell);
		propertyLst = new ArrayList<SecondaryColumnKeysInformation>();
		secondaryColumnsMap = new LinkedHashMap<String, String>();
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		this.buttonWithLabelConfig = buttonWithLabelConfig;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		isAnyUpdatePerformed = false;
		getShell().setText(buttonWithLabelConfig.getWindowName());

		Composite container = (Composite) super.createDialogArea(parent);
		ColumnLayout cl_container = new ColumnLayout();
		cl_container.verticalSpacing = 0;
		cl_container.maxNumColumns = 1;
		container.setLayout(cl_container);

		addSeperator(container);
		addButtonPanel(container);

		Composite composite_2 = new Composite(container, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		ColumnLayoutData cld_composite_2 = new ColumnLayoutData();
		cld_composite_2.heightHint = 410;
		composite_2.setLayoutData(cld_composite_2);

		createSourceTable(composite_2);

		createTargetTable(composite_2);

		addErrorLabel(container);

		return container;
	}

	private void createTargetTable(Composite container) {
		targetTableViewer = new TableViewer(container, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		targetTable = targetTableViewer.getTable();
		GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
		gd_table_1.heightHint = 401;
		gd_table_1.widthHint = 340;
		targetTable.setLayoutData(gd_table_1);

		attachTargetTableListeners();

		targetTableViewer.setContentProvider(new SecondaryColumnKeysContentProvider());
		targetTableViewer.setLabelProvider(new SecondaryColumnKeysLabelProvider());
		targetTableViewer.setInput(propertyLst);

		TableColumn targetTableColumnFieldName = new TableColumn(targetTable, SWT.CENTER);
		targetTableColumnFieldName.setText(COLUMNNAME); //$NON-NLS-1$
		TableColumn targetTableColumnSortOrder = new TableColumn(targetTable, SWT.LEFT_TO_RIGHT);
		targetTableColumnSortOrder.setText(SORTORDER); //$NON-NLS-1$

		for (int i = 0, n = targetTable.getColumnCount(); i < n; i++) {
			targetTable.getColumn(i).pack();
		}
		targetTableColumnFieldName.setWidth(252);
		targetTableColumnSortOrder.setWidth(111);
		targetTable.setHeaderVisible(true);
		targetTable.setLinesVisible(true);

		// enables the tab functionality
		TableViewerEditor.create(targetTableViewer, new ColumnViewerEditorActivationStrategy(targetTableViewer),
				ColumnViewerEditor.KEYBOARD_ACTIVATION | ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL);

		final CellEditor propertyNameeditor = new TextCellEditor(targetTable);

		ComboBoxViewerCellEditor propertyValueeditor = new ComboBoxViewerCellEditor(targetTable, SWT.READ_ONLY);
		propertyValueeditor.setContentProvider(new ArrayContentProvider());
		propertyValueeditor.setLabelProvider(new LabelProvider());
		propertyValueeditor.setInput(new String[] { Constants.ASCENDING_SORT_ORDER, Constants.DESCENDING_SORT_ORDER });

		CellEditor[] editors = new CellEditor[] { propertyNameeditor, propertyValueeditor };

		propertyNameeditor.setValidator(createNameEditorValidator(Messages.EmptyColumnNotification));
		propertyValueeditor.setValidator(createValueEditorValidator(Messages.EmptySortOrderNotification));

		targetTableViewer.setColumnProperties(PROPS);
		targetTableViewer.setCellModifier(new SecondaryColumnKeysWidgetCellModifier(targetTableViewer));
		targetTableViewer.setCellEditors(editors);

		loadProperties(targetTableViewer);

		dropTarget = new DropTarget(targetTable, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				for (String fieldName : getformatedData((String) event.data))
					if (!isPropertyAlreadyExists(fieldName))
						addNewProperty(targetTableViewer, fieldName);
			}
		});

	}

	private void attachTargetTableListeners() {
		targetTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				addNewProperty(targetTableViewer, null);
			}

			@Override
			public void mouseDown(MouseEvent e) {
				lblPropertyError.setVisible(false);

			}
		});
		targetTableViewer.getTable().addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == SWT.ARROW_UP) {
					e.doit = false;
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					e.doit = false;
				} else if (e.keyCode == SWT.TRAVERSE_ARROW_NEXT) {
					e.doit = false;
				} else if (e.keyCode == SWT.TRAVERSE_ARROW_PREVIOUS) {
					e.doit = false;
				}

			}
		});
	}

	private void createSourceTable(Composite composite_2) {
		sourceTable = new Table(composite_2, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
		gd_table.widthHint = 221;
		gd_table.heightHint = 407;
		sourceTable.setLayoutData(gd_table);
		sourceTable.setHeaderVisible(true);
		sourceTable.setLinesVisible(true);

		TableColumn sourceTableColumnFieldName = new TableColumn(sourceTable, SWT.CENTER);
		sourceTableColumnFieldName.setWidth(237);
		sourceTableColumnFieldName.setText(Messages.AVAILABLE_FIELDS_HEADER);
		getSourceFieldsFromPropagatedSchema(sourceTable);
		dragSource = new DragSource(sourceTable, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				// Set the data to be the first selected item's text
				event.data = formatDataToTransfer(sourceTable.getSelection());
			}

		});
	}

	private void addSeperator(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		ColumnLayout cl_composite = new ColumnLayout();
		cl_composite.maxNumColumns = 1;
		composite.setLayout(cl_composite);
		ColumnLayoutData cld_composite = new ColumnLayoutData();
		cld_composite.heightHint = 28;
		composite.setLayoutData(cld_composite);

		new Label(composite, SWT.NONE);

		new Label(composite, SWT.HORIZONTAL);
	}

	private void addErrorLabel(Composite container) {
		Composite composite_3 = new Composite(container, SWT.NONE);
		composite_3.setLayout(new ColumnLayout());
		ColumnLayoutData cld_composite_3 = new ColumnLayoutData();
		cld_composite_3.heightHint = 25;
		composite_3.setLayoutData(cld_composite_3);

		lblPropertyError = new Label(composite_3, SWT.NONE);
		lblPropertyError.setVisible(false);
		lblPropertyError.setForeground(new Color(Display.getDefault(), 255, 0, 0));
	}

	/**
	 * returns SecondaryColumns Map
	 * 
	 * @return
	 */
	public Map<String, String> getSecondaryColumnsMap() {
		return secondaryColumnsMap;
	}

	private void addButtonPanel(Composite container) {
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, false));
		ColumnLayoutData cld_composite_1 = new ColumnLayoutData();
		cld_composite_1.horizontalAlignment = ColumnLayoutData.RIGHT;
		cld_composite_1.heightHint = 28;
		composite_1.setLayoutData(cld_composite_1);

		Label addButton = new Label(composite_1, SWT.NONE);
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		addButton.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.ADD_BUTTON));
		attachAddButtonListern(addButton);

		Label deleteButton = new Label(composite_1, SWT.NONE);
		deleteButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		deleteButton.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.DELETE_BUTTON));
		attachDeleteButtonListener(deleteButton);

		Label upButton = new Label(composite_1, SWT.NONE);
		upButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upButton.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.MOVEUP_BUTTON));
		attachUpButtonListener(upButton);

		Label downButton = new Label(composite_1, SWT.NONE);
		downButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		downButton.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.MOVEDOWN_BUTTON));
		attachDownButtonListerner(downButton);
	}

	private void attachDownButtonListerner(Label downButton) {
		downButton.addMouseListener(new MouseAdapter() {
			int index1 = 0, index2 = 0;
       
			@Override
			public void mouseUp(MouseEvent e) {
				index1 = targetTable.getSelectionIndex();
				String text = targetTableViewer.getTable().getItem(index1).getText(0);
				String text1 = targetTableViewer.getTable().getItem(index1).getText(1);

				if (index1 < propertyLst.size()) {
					index2 = index1 + 1;

					String data = targetTableViewer.getTable().getItem(index2).getText(0);
					String data1 = targetTableViewer.getTable().getItem(index2).getText(1);

					SecondaryColumnKeysInformation p = new SecondaryColumnKeysInformation();
					p.setPropertyName(data);
					p.setPropertyValue(data1);
					propertyLst.set(index1, p);

					p = new SecondaryColumnKeysInformation();
					p.setPropertyName(text);
					p.setPropertyValue(text1);
					propertyLst.set(index2, p);
					targetTableViewer.refresh();
					targetTable.setSelection(index1 + 1);
				}
			}
		});

	}

	private void attachUpButtonListener(Label upButton) {
		upButton.addMouseListener(new MouseAdapter() {
			int index1 = 0, index2 = 0;

        	@Override
			public void mouseUp(MouseEvent e) {
				index1 = targetTable.getSelectionIndex();
				String text = targetTableViewer.getTable().getItem(index1).getText(0);
				String text1 = targetTableViewer.getTable().getItem(index1).getText(1);

				if (index1 > 0) {
					index2 = index1 - 1;
					String data = targetTableViewer.getTable().getItem(index2).getText(0);
					String data2 = targetTableViewer.getTable().getItem(index2).getText(1);

					SecondaryColumnKeysInformation p = new SecondaryColumnKeysInformation();
					p.setPropertyName(data);
					p.setPropertyValue(data2);
					propertyLst.set(index1, p);

					p = new SecondaryColumnKeysInformation();
					p.setPropertyName(text);
					p.setPropertyValue(text1);
					propertyLst.set(index2, p);
					targetTableViewer.refresh();
					targetTable.setSelection(index1 - 1);

				}
			}
		});

	}

	private void attachDeleteButtonListener(Label deleteButton) {
		deleteButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				IStructuredSelection selection = (IStructuredSelection) targetTableViewer.getSelection();
				for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
					Object selectedObject = iterator.next();
					targetTableViewer.remove(selectedObject);
					propertyLst.remove(selectedObject);
					isAnyUpdatePerformed = true;
				}
			}

		});

	}

	private void attachAddButtonListern(Label addButton) {
		addButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				addNewProperty(targetTableViewer, null);
			}

		});
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(646, 587);
	}

	// Add New Property After Validating old properties
	private void addNewProperty(TableViewer tv, String fieldName) {
		isAnyUpdatePerformed = true;
		SecondaryColumnKeysInformation p = new SecondaryColumnKeysInformation();
		if (fieldName == null)
			fieldName = "";
		if (propertyLst.size() != 0) {
			if (!validate())
				return;
			p.setPropertyName(fieldName); //$NON-NLS-1$
			p.setPropertyValue(Constants.ASCENDING_SORT_ORDER); //$NON-NLS-1$
			propertyLst.add(p);
			tv.refresh();
			targetTableViewer.editElement(targetTableViewer.getElementAt(propertyLst.size() - 1), 0);
		} else {
			p.setPropertyName(fieldName); //$NON-NLS-1$
			p.setPropertyValue(Constants.ASCENDING_SORT_ORDER); //$NON-NLS-1$
			propertyLst.add(p);
			tv.refresh();
			targetTableViewer.editElement(targetTableViewer.getElementAt(0), 0);
		}
	}

	/**
	 * set secondaryColumns Map
	 * 
	 * @param secondaryColumnsMap
	 */
	public void setSecondaryColumnsMap(LinkedHashMap<String, String> secondaryColumnsMap) {
		this.secondaryColumnsMap = secondaryColumnsMap;
	}

	

	// Loads Already Saved Properties..
	private void loadProperties(TableViewer tv) {

		if (secondaryColumnsMap != null && !secondaryColumnsMap.isEmpty()) {
			for (String key : secondaryColumnsMap.keySet()) {
				SecondaryColumnKeysInformation p = new SecondaryColumnKeysInformation();
				if (validateBeforeLoad(key, secondaryColumnsMap.get(key))) {
					p.setPropertyName(key);
					p.setPropertyValue(secondaryColumnsMap.get(key));
					propertyLst.add(p);
				}
			}
			tv.refresh();

		} //$NON-NLS-1$

	}

	private boolean validateBeforeLoad(String key, String keyValue) {

		if (key.trim().isEmpty() || keyValue.trim().isEmpty()) {
			return false;
		}
		return true;

	}

	private void getSourceFieldsFromPropagatedSchema(Table sourceTable) {
		TableItem sourceTableItem = null;
		if (sourceFieldsList != null && !sourceFieldsList.isEmpty())
			for (String filedName : sourceFieldsList) {
				sourceTableItem = new TableItem(sourceTable, SWT.NONE);
				sourceTableItem.setText(filedName);
			}

	}

	/**
	 * Validate.
	 * 
	 * @return true, if successful
	 */
	protected boolean validate() {

		int propertyCounter = 0;
		for (SecondaryColumnKeysInformation temp : propertyLst) {
			if (!temp.getPropertyName().trim().isEmpty() && !temp.getPropertyValue().trim().isEmpty()) {
				//String Regex = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}||[\\w]*"; -- TODO Please do not remove
				Matcher matchName = Pattern.compile(Constants.REGEX).matcher(temp.getPropertyName());
				if (!matchName.matches()) {
					targetTable.setSelection(propertyCounter);
					lblPropertyError.setVisible(true);
					lblPropertyError.setText(Messages.PROPERTY_NAME_ALLOWED_CHARACTERS);
					return false;
				}
				if (!(temp.getPropertyValue().trim().equalsIgnoreCase(Constants.ASCENDING_SORT_ORDER) || temp
						.getPropertyValue().trim().equalsIgnoreCase(Constants.DESCENDING_SORT_ORDER))) {
					targetTable.setSelection(propertyCounter);
					lblPropertyError.setVisible(true);
					lblPropertyError.setText(Messages.INVALID_SORT_ORDER);
					return false;
				}

			} else {
				targetTable.setSelection(propertyCounter);
				lblPropertyError.setVisible(true);
				lblPropertyError.setText(Messages.EmptyFiledNotification);
				return false;
			}
			propertyCounter++;
		}
		return true;
	}

	// Creates CellNAme Validator for table's cells
	private ICellEditorValidator createNameEditorValidator(final String ErrorMessage) {
		ICellEditorValidator propertyValidator = new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				isAnyUpdatePerformed = true;
				String currentSelectedFld = targetTable.getItem(targetTable.getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				if (valueToValidate.isEmpty()) {
					lblPropertyError.setText(ErrorMessage);
					lblPropertyError.setVisible(true);
					return Constants.ERROR; //$NON-NLS-1$
				}
				if (!currentSelectedFld.equalsIgnoreCase(valueToValidate) && isPropertyAlreadyExists(valueToValidate)) {
					lblPropertyError.setText(Messages.RuntimePropertAlreadyExists);
					lblPropertyError.setVisible(true);
					return Constants.ERROR; //$NON-NLS-1$
				}
				
				lblPropertyError.setVisible(false);

				return null;

			}

		};
		return propertyValidator;
	}

	// Creates Value Validator for table's cells
	private ICellEditorValidator createValueEditorValidator(final String ErrorMessage) {
		ICellEditorValidator propertyValidator = new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				isAnyUpdatePerformed = true;
				targetTable.getItem(targetTable.getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				if (valueToValidate.isEmpty()) {
					lblPropertyError.setText(ErrorMessage);
					lblPropertyError.setVisible(true);
					return Constants.ERROR; //$NON-NLS-1$
				} else {
					lblPropertyError.setVisible(false);
				}
				return null;

			}
		};
		return propertyValidator;
	}

	

	/**
	 * This Method is used to set the propagated field names.
	 * 
	 * @param fieldNameList
	 */
	public void setSourceFieldsFromPropagatedSchema(List<String> fieldNameList) {
		this.sourceFieldsList = fieldNameList;

	}

	private String formatDataToTransfer(TableItem[] selectedTableItems) {
		StringBuffer buffer = new StringBuffer();
		for (TableItem tableItem : selectedTableItems) {
			buffer.append(tableItem.getText() + "#");
		}
		return buffer.toString();
	}

	private String[] getformatedData(String formatedString) {
		String[] fieldNameArray = null;
		if (formatedString != null) {
			fieldNameArray = formatedString.split("#");
		}
		return fieldNameArray;
	}

	private boolean isPropertyAlreadyExists(String valueToValidate) {
		for (SecondaryColumnKeysInformation temp : propertyLst)
			if (temp.getPropertyName().trim().equalsIgnoreCase(valueToValidate))
				return true;
		return false;
	}

	@Override
	protected void okPressed() {
		if (validate()) {
			secondaryColumnsMap.clear();
			for (SecondaryColumnKeysInformation temp : propertyLst) {
				secondaryColumnsMap.put(temp.getPropertyName(), temp.getPropertyValue());
			}

			if (isAnyUpdatePerformed) {
				propertyDialogButtonBar.enableApplyButton(true);
			}
			okPressed=true;
			super.okPressed();
		}
	}

	@Override
	protected void cancelPressed() {
		if (isAnyUpdatePerformed) {
			int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
			MessageBox messageBox = new MessageBox(new Shell(), style);
			messageBox.setText(Messages.INFORMATION);
			messageBox.setMessage(Messages.MessageBeforeClosingWindow);

			if (messageBox.open() == SWT.YES) {
				closeDialog = super.close();
			}
		} else {
			closeDialog = super.close();
		}

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
