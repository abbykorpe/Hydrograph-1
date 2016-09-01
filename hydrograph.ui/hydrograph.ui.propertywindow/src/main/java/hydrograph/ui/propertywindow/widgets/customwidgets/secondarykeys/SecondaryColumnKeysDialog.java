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

package hydrograph.ui.propertywindow.widgets.customwidgets.secondarykeys;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.ImagePathConstant;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.EditButtonWithLabelConfig;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.util.ArrayList;
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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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


/**
 * 
 * Class to create Secondary column dialog
 * 
 * @author Bitwise
 *
 */

public class SecondaryColumnKeysDialog extends Dialog {
	
	private List<SecondaryColumnKeysInformation> propertyList;
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
	private Button deleteButton;
	private Button upButton;
	private Button downButton;
	private static final String INFORMATION="Information";
	
	private boolean ctrlKeyPressed = false;
	
	public SecondaryColumnKeysDialog(Shell parentShell, PropertyDialogButtonBar propertyDialogButtonBar, EditButtonWithLabelConfig buttonWithLabelConfig) {
		super(parentShell);
		propertyList = new ArrayList<SecondaryColumnKeysInformation>();
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
		cld_composite_2.heightHint = 453;
		composite_2.setLayoutData(cld_composite_2);

		createSourceTable(composite_2);

		createTargetTable(composite_2);
		
		attachShortcutListner();
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
		targetTableViewer.setInput(propertyList);

		TableColumn targetTableColumnFieldName = new TableColumn(targetTable, SWT.LEFT);
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
		propertyValueeditor.setInput(new String[] { Constants.ASCENDING_SORT_ORDER, Constants.DESCENDING_SORT_ORDER, Constants.NONE_SORT_ORDER });

		CellEditor[] editors = new CellEditor[] { propertyNameeditor, propertyValueeditor };

		propertyNameeditor.setValidator(createNameEditorValidator(Messages.EmptyColumnNotification));
		propertyValueeditor.setValidator(createValueEditorValidator(Messages.EmptySortOrderNotification));

		targetTableViewer.setColumnProperties(PROPS);
		targetTableViewer.setCellModifier(new SecondaryColumnKeysWidgetCellModifier(targetTableViewer));
		targetTableViewer.setCellEditors(editors);

		loadProperties(targetTableViewer);
		
		if (propertyList.size() != 0) {
			deleteButton.setEnabled(true);
		}
		if (propertyList.size() >= 2) {
			upButton.setEnabled(true);
			downButton.setEnabled(true);
		}

		dropTarget = new DropTarget(targetTable, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				for (String fieldName : getformatedData((String) event.data))
						addNewProperty(targetTableViewer, fieldName);
				enableControlButtons();
			}
		});

	}
	
	private void attachShortcutListner(){
		Control currentControl= targetTable;
		
		currentControl.addKeyListener(new KeyListener() {						
			
			@Override
			public void keyReleased(KeyEvent event) {				
				if(event.keyCode == SWT.CTRL || event.keyCode == SWT.COMMAND){					
					ctrlKeyPressed = false;
				}							
			}
			
			@Override
			public void keyPressed(KeyEvent event) {
				if(event.keyCode == SWT.CTRL || event.keyCode == SWT.COMMAND){					
					ctrlKeyPressed = true;
				}
								
				if (ctrlKeyPressed && event.keyCode == Constants.KEY_D) {				
					deleteRow();
				}
				
				else if (ctrlKeyPressed && event.keyCode == Constants.KEY_N){
					addNewRow();
				}
				
				else if (ctrlKeyPressed && event.keyCode == SWT.ARROW_UP){
					moveRowUp();				
				}
				
				else if (ctrlKeyPressed && event.keyCode == SWT.ARROW_DOWN){
					moveRowDown();
				}
			}
		});
	}
	
	private void deleteRow(){

		WidgetUtility.setCursorOnDeleteRow(targetTableViewer, propertyList);
		isAnyUpdatePerformed = true;
		targetTableViewer.refresh();
		if (propertyList.size() < 1) {
			deleteButton.setEnabled(false);
		}
		if (propertyList.size()<= 1) {
			upButton.setEnabled(false);
			downButton.setEnabled(false);
		} 
	
	}

	
	private void addNewRow(){

		addNewProperty(targetTableViewer, null);
		enableControlButtons();
	
	}
	
	private void moveRowUp(){
		int index1 = 0, index2 = 0;
		index1 = targetTable.getSelectionIndex();
		String text = targetTableViewer.getTable().getItem(index1).getText(0);
		String text1 = targetTableViewer.getTable().getItem(index1).getText(1);

		if (index1 > 0) {
			index2 = index1 - 1;
			String data = targetTableViewer.getTable().getItem(index2).getText(0);
			String data2 = targetTableViewer.getTable().getItem(index2).getText(1);

			SecondaryColumnKeysInformation p = new SecondaryColumnKeysInformation();
			p.setColumnName(data);
			p.setSortOrder(data2);
			propertyList.set(index1, p);

			p = new SecondaryColumnKeysInformation();
			p.setColumnName(text);
			p.setSortOrder(text1);
			propertyList.set(index2, p);
			targetTableViewer.refresh();
			targetTable.setSelection(index1 - 1);

		}
	
		
	}
	
	private void moveRowDown(){
		int index1 = 0, index2 = 0;

		index1 = targetTable.getSelectionIndex();
		String text = targetTableViewer.getTable().getItem(index1).getText(0);
		String text1 = targetTableViewer.getTable().getItem(index1).getText(1);

		if (index1 < propertyList.size()) {
			index2 = index1 + 1;

			String data = targetTableViewer.getTable().getItem(index2).getText(0);
			String data1 = targetTableViewer.getTable().getItem(index2).getText(1);

			SecondaryColumnKeysInformation p = new SecondaryColumnKeysInformation();
			p.setColumnName(data);
			p.setSortOrder(data1);
			propertyList.set(index1, p);

			p = new SecondaryColumnKeysInformation();
			p.setColumnName(text);
			p.setSortOrder(text1);
			propertyList.set(index2, p);
			targetTableViewer.refresh();
			targetTable.setSelection(index1 + 1);
		}
	
	}

	private void attachTargetTableListeners() {
		targetTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				addNewProperty(targetTableViewer, null);
				enableControlButtons();
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
		sourceTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if(sourceTable.getSelection().length==1){
					addNewProperty(targetTableViewer, sourceTable.getSelection()[0].getText());
					enableControlButtons();
				}
			}
		});
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
		gd_table.widthHint = 221;
		gd_table.heightHint = 407;
		sourceTable.setLayoutData(gd_table);
		sourceTable.setHeaderVisible(true);
		sourceTable.setLinesVisible(true);

		TableColumn sourceTableColumnFieldName = new TableColumn(sourceTable, SWT.LEFT);
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
		cld_composite.heightHint = 0;
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
		cld_composite_1.heightHint = 30;
		composite_1.setLayoutData(cld_composite_1);

		Button addButton = new Button(composite_1, SWT.NONE);
		addButton.setToolTipText(Messages.ADD_SCHEMA_TOOLTIP);
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		addButton.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.ADD_BUTTON));
		attachAddButtonListern(addButton);

		deleteButton = new Button(composite_1, SWT.NONE);
		deleteButton.setToolTipText(Messages.DELETE_SCHEMA_TOOLTIP);
		deleteButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		deleteButton.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.DELETE_BUTTON));
		attachDeleteButtonListener(deleteButton);

		upButton = new Button(composite_1, SWT.NONE);
		upButton.setToolTipText(Messages.MOVE_SCHEMA_UP_TOOLTIP);
		upButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upButton.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.MOVEUP_BUTTON));
		attachUpButtonListener(upButton);

		downButton = new Button(composite_1, SWT.NONE);
		downButton.setToolTipText(Messages.MOVE_SCHEMA_DOWN_TOOLTIP);
		downButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		downButton.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.MOVEDOWN_BUTTON));
		attachDownButtonListerner(downButton);
		deleteButton.setEnabled(false);
		upButton.setEnabled(false);
		downButton.setEnabled(false);
	}

	private void attachDownButtonListerner(Button downButton) {
		downButton.addMouseListener(new MouseAdapter() {
       
			@Override
			public void mouseUp(MouseEvent e) {
				moveRowDown();
			}
		});

	}

	private void attachUpButtonListener(Button upButton) {
		upButton.addMouseListener(new MouseAdapter() {

        	@Override
			public void mouseUp(MouseEvent e) {
        		moveRowUp();
        	}
		});

	}

	private void attachDeleteButtonListener(final Button deleteButton) {
		deleteButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				deleteRow();
			}

		});

	}

	private void attachAddButtonListern(Button addButton) {
		addButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				addNewRow();
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
		if (isPropertyAlreadyExists(fieldName))
			return ;
		isAnyUpdatePerformed = true;
		SecondaryColumnKeysInformation p = new SecondaryColumnKeysInformation();
		if (fieldName == null)
			fieldName = "";
		if (propertyList.size() != 0) {
			if (!validate())
				return;
			p.setColumnName(fieldName); //$NON-NLS-1$
			p.setSortOrder(Constants.ASCENDING_SORT_ORDER); //$NON-NLS-1$
			propertyList.add(p);
			tv.refresh();
			targetTableViewer.editElement(targetTableViewer.getElementAt(propertyList.size() - 1), 0);
		} else {
			p.setColumnName(fieldName); //$NON-NLS-1$
			p.setSortOrder(Constants.ASCENDING_SORT_ORDER); //$NON-NLS-1$
			propertyList.add(p);
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
					p.setColumnName(key);
					p.setSortOrder(secondaryColumnsMap.get(key));
					propertyList.add(p);
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
		for (SecondaryColumnKeysInformation temp : propertyList) {
			if (!temp.getColumnName().trim().isEmpty() && !temp.getSortOrder().trim().isEmpty()) {

				//String Regex = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}||[\\w]*"; -- TODO Please do not remove
				Matcher matchName = Pattern.compile(Constants.REGEX).matcher(temp.getColumnName());
				if (!matchName.matches()) {
					targetTable.setSelection(propertyCounter);
					lblPropertyError.setVisible(true);
					lblPropertyError.setText(Messages.PROPERTY_NAME_ALLOWED_CHARACTERS);
					return false;
				}
				if (!(temp.getSortOrder().trim().equalsIgnoreCase(Constants.ASCENDING_SORT_ORDER) || 
						temp.getSortOrder().trim().equalsIgnoreCase(Constants.DESCENDING_SORT_ORDER)||
						temp.getSortOrder().trim().equalsIgnoreCase(Constants.NONE_SORT_ORDER))) {
					targetTable.setSelection(propertyCounter);
					lblPropertyError.setVisible(true);
					lblPropertyError.setText(Messages.INVALID_SORT_ORDER);
					return false;
				}

			} else {
				targetTable.setSelection(propertyCounter);
				lblPropertyError.setVisible(true);
				lblPropertyError.setText(Messages.EmptyColumnNotification);
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
		for (SecondaryColumnKeysInformation temp : propertyList)
			if (temp.getColumnName().trim().equalsIgnoreCase(valueToValidate))
				return true;
		return false;
	}

	@Override
	protected void okPressed() {
		if (validate()) {
			secondaryColumnsMap.clear();
			for (SecondaryColumnKeysInformation temp : propertyList) {
				secondaryColumnsMap.put(temp.getColumnName(), temp.getSortOrder());
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
			messageBox.setText(INFORMATION);
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

	private void enableControlButtons() {
		if (propertyList.size() >= 1) {
			deleteButton.setEnabled(true);
		} 
		if (propertyList.size() >= 2) {
			upButton.setEnabled(true);
			downButton.setEnabled(true);
		}
	}
}
