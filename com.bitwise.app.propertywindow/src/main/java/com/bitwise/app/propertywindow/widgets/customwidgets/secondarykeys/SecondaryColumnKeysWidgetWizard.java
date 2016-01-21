package com.bitwise.app.propertywindow.widgets.customwidgets.secondarykeys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;

// TODO: Auto-generated Javadoc
/**
 * The class RunTimePropertyWizard
 * 
 * @author Bitwise
 * 
 */

public class SecondaryColumnKeysWidgetWizard {
	private Table targetTable;

	private Shell shell;
	private List<SecondaryColumnKeysInformation> propertyLst;
	public static final String COLUMNNAME = "Column Name"; //$NON-NLS-1$
	public static final String SORTORDER = "Sort Order"; //$NON-NLS-1$
	private Map<String, String> runtimePropertyMap;
	private String componentName;
	private Label lblHeader;
	private String PROPERTY_EXISTS_ERROR = Messages.RuntimePropertAlreadyExists;
	public static final String[] PROPS = { COLUMNNAME, SORTORDER };
	private String COLUMN_NAME_BLANK_ERROR = Messages.EmptyColumnNotification;
	private String SORT_ORDER_BLANK_ERROR = Messages.EmptySortOrderNotification;
	private Label lblPropertyError;
	private boolean isOkPressed;
	private TableViewer tableViewer;
	private Button addButton, okButton, deleteButton, cacelButton, upButton, downButton;
	private boolean isAnyUpdatePerformed;
	private Table sourceTable;
	private DragSource dragSource;
	private DropTarget dropTarget;
	private List<String> sourceFieldsList;
	private TableItem tableItem;

	// private boolean firstTimeEdit;

	/**
	 * Instantiates a new run time property wizard.
	 */
	public SecondaryColumnKeysWidgetWizard() {

		propertyLst = new ArrayList<SecondaryColumnKeysInformation>();
		runtimePropertyMap = new LinkedHashMap<String, String>();

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
			tableViewer.editElement(tableViewer.getElementAt(propertyLst.size() - 1), 0);
		} else {
			p.setPropertyName(fieldName); //$NON-NLS-1$
			p.setPropertyValue(Constants.ASCENDING_SORT_ORDER); //$NON-NLS-1$
			propertyLst.add(p);
			tv.refresh();
			tableViewer.editElement(tableViewer.getElementAt(0), 0);
		}
		enableButtons();
	}

	

	public void setRuntimePropertyMap(LinkedHashMap<String, String> runtimePropertyMap) {
		this.runtimePropertyMap = runtimePropertyMap;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	// Loads Already Saved Properties..
	private void loadProperties(TableViewer tv) {

		if (runtimePropertyMap != null && !runtimePropertyMap.isEmpty()) {
			for (String key : runtimePropertyMap.keySet()) {
				SecondaryColumnKeysInformation p = new SecondaryColumnKeysInformation();
				if (validateBeforeLoad(key, runtimePropertyMap.get(key))) {
					p.setPropertyName(key);
					p.setPropertyValue(runtimePropertyMap.get(key));
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

	// Method for creating Table
	private void createTable() {

		tableViewer = new TableViewer(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		targetTable = tableViewer.getTable();
		targetTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				addNewProperty(tableViewer,null);
			}

			@Override
			public void mouseDown(MouseEvent e) {
				lblPropertyError.setVisible(false);

			}
		});
		tableViewer.getTable().addTraverseListener(new TraverseListener() {

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
		// table.setBounds(10, 50, 465, 365);
		targetTable.setBounds(210, 68, 339, 400);
		tableViewer.setContentProvider(new SecondaryColumnKeysContentProvider());
		tableViewer.setLabelProvider(new SecondaryColumnKeysLabelProvider());
		tableViewer.setInput(propertyLst);

		TableColumn tc1 = new TableColumn(targetTable, SWT.CENTER);
		tc1.setText("Column Name"); //$NON-NLS-1$
		TableColumn tc2 = new TableColumn(targetTable, SWT.LEFT_TO_RIGHT);
		tc2.setText("Sort Order"); //$NON-NLS-1$

		for (int i = 0, n = targetTable.getColumnCount(); i < n; i++) {
			targetTable.getColumn(i).pack();
		}
		tc1.setWidth(269);
		tc2.setWidth(66);
		targetTable.setHeaderVisible(true);
		targetTable.setLinesVisible(true);

		// enables the tab functionality
		TableViewerEditor.create(tableViewer, new ColumnViewerEditorActivationStrategy(tableViewer),
				ColumnViewerEditor.KEYBOARD_ACTIVATION | ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL);
	}

	/**
	 * @param propertyDialogButtonBar
	 * @return
	 * @wbp.parser.entryPoint
	 */
	public Map<String, String> launchRuntimeWindow(Shell parentShell,
			final PropertyDialogButtonBar propertyDialogButtonBar) {
		
		shell = new Shell(parentShell, SWT.WRAP | SWT.APPLICATION_MODAL);
		isOkPressed = false;
		isAnyUpdatePerformed = false;
		shell.setSize(565, 560);
		shell.setLayout(null);
		shell.setText(Messages.SECONDARY_COLUMN_KEY_WINDOW_NAME);

		sourceTable = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		sourceTable.setBounds(10, 68, 194, 400);
		sourceTable.setHeaderVisible(true);
		sourceTable.setLinesVisible(true);
		TableColumn tc1 = new TableColumn(sourceTable, SWT.CENTER);
		tc1.setWidth(190);
		tc1.setText(Messages.AVAILABLE_FIELDS_HEADER); 
		getSourceFieldsFromPropagatedSchema(sourceTable);
		dragSource = new DragSource(sourceTable, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				// Set the data to be the first selected item's text

				event.data = formatDataToTransfer(sourceTable.getSelection());
			}

		});

		// imageShell(shell);
		lblHeader = new Label(shell, SWT.NONE);
		lblHeader.setBounds(10, 14, 450, 15);
		lblHeader.setText(Messages.SECONDARY_COLUMN_KEY_WINDOW_HEADER);
		new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL).setBounds(0, 35, 559, 2);

		Composite com = new Composite(shell, SWT.NONE);
		com.setBounds(29, 35, 520, 30);
		createIcons(com);

		// Below Event will be fired when user closes the Runtime window
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				if (isOkPressed && isAnyUpdatePerformed) {
					// propertyDialogButtonBar.enableApplyButton(true);
				}
				if ((isAnyUpdatePerformed && !isOkPressed) && (targetTable.getItemCount() != 0 || isAnyUpdatePerformed)) {

					int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
					MessageBox messageBox = new MessageBox(shell, style);
					messageBox.setText("Information"); //$NON-NLS-1$
					messageBox.setMessage(Messages.MessageBeforeClosingWindow);
					event.doit = messageBox.open() == SWT.YES;
				}

			}
		});

		createTable();

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 445, 559, 76);
		createButtons(composite);

		lblPropertyError = new Label(composite, SWT.NONE);
		lblPropertyError.setForeground(new Color(Display.getDefault(), 255, 0, 0));
		lblPropertyError.setBounds(28, 57, 258, 15);
		lblPropertyError.setVisible(false);

		final CellEditor propertyNameeditor = new TextCellEditor(targetTable);

		
		
		ComboBoxViewerCellEditor propertyValueeditor = new ComboBoxViewerCellEditor(targetTable, SWT.READ_ONLY);
		propertyValueeditor.setContentProvider(new ArrayContentProvider());
		propertyValueeditor.setLabelProvider(new LabelProvider());
		propertyValueeditor.setInput(new String[]{Constants.ASCENDING_SORT_ORDER,Constants.DESCENDING_SORT_ORDER});

		
		CellEditor[] editors = new CellEditor[] { propertyNameeditor, propertyValueeditor };

		propertyNameeditor.setValidator(createNameEditorValidator(COLUMN_NAME_BLANK_ERROR));
		propertyValueeditor.setValidator(createValueEditorValidator(SORT_ORDER_BLANK_ERROR));

		tableViewer.setColumnProperties(PROPS);
		tableViewer.setCellModifier(new SecondaryColumnKeysWidgetCellModifier(tableViewer));
		tableViewer.setCellEditors(editors);

		loadProperties(tableViewer);

		dropTarget = new DropTarget(targetTable, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				for (String fieldName : getformatedData((String) event.data))
					if (!isPropertyAlreadyExists(fieldName))
						addNewProperty(tableViewer, fieldName);
			}
		});
		disableButtons();
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		shell.setLocation(x, y);

		shell.open();

		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch())
				shell.getDisplay().sleep();
		}

		return runtimePropertyMap;
	}

	private void getSourceFieldsFromPropagatedSchema(Table sourceTable) {
		TableItem sourceTableItem = null;
		if (sourceFieldsList != null && !sourceFieldsList.isEmpty())
			for (String filedName : sourceFieldsList) {
				sourceTableItem = new TableItem(sourceTable, SWT.NONE);
				sourceTableItem.setText(filedName);
			}

	}

	private void createIcons(Composite composite) {

		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setBounds(0, 41, 513, 60);
		addButton = new Button(composite, SWT.PUSH);
		// addButton.setText("+");
		String addIconPath = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/add.png";
		addButton.setImage(new Image(null, addIconPath));
		addButton.setBounds(433, 10, 20, 20);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewProperty(tableViewer, null);
			}
		});

		deleteButton = new Button(composite, SWT.PUSH);
		// deleteButton.setText("X");
		String deleteIonPath = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/delete.png";
		deleteButton.setImage(new Image(null, deleteIonPath));
		deleteButton.setBounds(452, 10, 25, 20);
		deleteButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
				 * if (temp == -1){ MessageDialog.openError(shell, "Error", //$NON-NLS-1$ Messages.SelectRowToDelete);
				 * 
				 * }else {
				 */

				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
					Object selectedObject = iterator.next();
					tableViewer.remove(selectedObject);
					propertyLst.remove(selectedObject);
				}
				isAnyUpdatePerformed = true;

			}
		});

		upButton = new Button(composite, SWT.PUSH);
		// upButton.setText("^");
		String upIonPath = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/up.png";
		upButton.setImage(new Image(null, upIonPath));
		upButton.setBounds(476, 10, 20, 20);

		upButton.addSelectionListener(new SelectionAdapter() {
			int index1 = 0, index2 = 0;

			@Override
			public void widgetSelected(SelectionEvent e) {
				index1 = targetTable.getSelectionIndex();
				String text = tableViewer.getTable().getItem(index1).getText(0);
				String text1 = tableViewer.getTable().getItem(index1).getText(1);

				if (index1 > 0) {
					index2 = index1 - 1;
					String data = tableViewer.getTable().getItem(index2).getText(0);
					String data2 = tableViewer.getTable().getItem(index2).getText(1);

					SecondaryColumnKeysInformation p = new SecondaryColumnKeysInformation();
					p.setPropertyName(data);
					p.setPropertyValue(data2);
					propertyLst.set(index1, p);

					p = new SecondaryColumnKeysInformation();
					p.setPropertyName(text);
					p.setPropertyValue(text1);
					propertyLst.set(index2, p);
					tableViewer.refresh();
					targetTable.setSelection(index1 - 1);

				}
			}
		});

		downButton = new Button(composite, SWT.PUSH);
		// downButton.setText("->");
		String downIonPath = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/down.png";
		downButton.setImage(new Image(null, downIonPath));
		downButton.setBounds(495, 10, 25, 20);
		downButton.addSelectionListener(new SelectionAdapter() {
			int index1 = 0, index2 = 0;

			@Override
			public void widgetSelected(SelectionEvent e) {
				index1 = targetTable.getSelectionIndex();
				String text = tableViewer.getTable().getItem(index1).getText(0);
				String text1 = tableViewer.getTable().getItem(index1).getText(1);

				if (index1 < propertyLst.size()) {
					index2 = index1 + 1;

					String data = tableViewer.getTable().getItem(index2).getText(0);
					String data1 = tableViewer.getTable().getItem(index2).getText(1);

					SecondaryColumnKeysInformation p = new SecondaryColumnKeysInformation();
					p.setPropertyName(data);
					p.setPropertyValue(data1);
					propertyLst.set(index1, p);

					p = new SecondaryColumnKeysInformation();
					p.setPropertyName(text);
					p.setPropertyValue(text1);
					propertyLst.set(index2, p);
					tableViewer.refresh();
					targetTable.setSelection(index1 + 1);
				}
			}
		});
	}

	// Creates The buttons For the widget
	private void createButtons(Composite composite) {
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setBounds(0, 41, 562, 2);
		/*
		 * addButton = new Button(composite, SWT.NONE); addButton.setText("Add"); //$NON-NLS-1$ addButton.setBounds(10,
		 * 10, 75, 25); addButton.addSelectionListener(new SelectionAdapter() {
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) { addNewProperty(tableViewer); } });
		 * 
		 * deleteButton = new Button(composite, SWT.NONE); deleteButton.setText("Delete"); //$NON-NLS-1$
		 * deleteButton.setBounds(91, 10, 75, 25); deleteButton.addSelectionListener(new SelectionAdapter() {
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) {
		 * 
		 * int temp = table.getSelectionIndex(); if (temp == -1) MessageDialog.openError(shell, "Error", //$NON-NLS-1$
		 * Messages.SelectRowToDelete); else { table.remove(temp); propertyLst.remove(temp); isAnyUpdatePerformed =
		 * true; enableButtons(); } } }); deleteButton.setImage(null);
		 * 
		 * deleteAll = new Button(composite, SWT.NONE); deleteAll.addSelectionListener(new SelectionAdapter() { public
		 * void widgetSelected(SelectionEvent e) { if (table.getItemCount() != 0) { boolean userAns =
		 * MessageDialog.openConfirm(shell, "Remove all", //$NON-NLS-1$ Messages.ConfirmToDeleteAllProperties); if
		 * (userAns) { table.removeAll(); propertyLst.removeAll(propertyLst); lblPropertyError.setVisible(false);
		 * isAnyUpdatePerformed=true; enableButtons(); } } } }); deleteAll.setBounds(172, 10, 75, 25);
		 * deleteAll.setText("Delete All"); //$NON-NLS-1$
		 * 
		 * applyButton = new Button(composite, SWT.NONE); applyButton.addSelectionListener(new SelectionAdapter() {
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) {
		 * 
		 * if (validate()) { if (isAnyUpdatePerformed) { runtimePropertyMap.clear(); for (RuntimeProperties temp :
		 * propertyLst) { runtimePropertyMap.put(temp.getPropertyName(), temp.getPropertyValue()); }
		 * isAnyUpdatePerformed = false; propertyDialogButtonBar.enableApplyButton(true); }
		 * }applyButton.setEnabled(false); } }); applyButton.setBounds(253, 10, 75, 25); applyButton.setText("Apply");
		 * //$NON-NLS-1$
		 */
		okButton = new Button(composite, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (validate()) {
					runtimePropertyMap.clear();
					isOkPressed = true;
					for (SecondaryColumnKeysInformation temp : propertyLst) {
						runtimePropertyMap.put(temp.getPropertyName(), temp.getPropertyValue());
					}

					shell.close();
				} else
					return;
			}
		});
		okButton.setBounds(357, 50, 75, 25);
		okButton.setText("OK"); //$NON-NLS-1$
		cacelButton = new Button(composite, SWT.NONE);
		cacelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				shell.close();
			}
		});
		cacelButton.setBounds(438, 50, 75, 25);

		cacelButton.setText("Cancel"); //$NON-NLS-1$

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
				String Regex = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}||[\\w]*";
				Matcher matchName = Pattern.compile(Regex).matcher(temp.getPropertyName());
				// Matcher matchValue = Pattern.compile(Regex).matcher(temp.getPropertyValue());
				// if(!matchName.matches() || !matchValue.matches())
				if (!matchName.matches()) {
					targetTable.setSelection(propertyCounter);
					lblPropertyError.setVisible(true);
					lblPropertyError.setText(Messages.PROPERTY_NAME_ALLOWED_CHARACTERS);
					// disableButtons();
					return false;
				}
				if (!(temp.getPropertyValue().trim().equalsIgnoreCase(Constants.ASCENDING_SORT_ORDER) || temp.getPropertyValue().trim()
						.equalsIgnoreCase(Constants.DESCENDING_SORT_ORDER))) {
					targetTable.setSelection(propertyCounter);
					lblPropertyError.setVisible(true);
					lblPropertyError.setText(Messages.INVALID_SORT_ORDER);
					disableButtons();
					return false;
				}

			} else {
				targetTable.setSelection(propertyCounter);
				lblPropertyError.setVisible(true);
				lblPropertyError.setText(Messages.EmptyFiledNotification);
				disableButtons();
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
					disableButtons();
					return "ERROR"; //$NON-NLS-1$
				}
				if (!currentSelectedFld.equalsIgnoreCase(valueToValidate) && isPropertyAlreadyExists(valueToValidate)) {
					lblPropertyError.setText(PROPERTY_EXISTS_ERROR);
					lblPropertyError.setVisible(true);
					disableButtons();
					return "ERROR"; //$NON-NLS-1$
				} else
					enableButtons();
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
					disableButtons();
					return "ERROR"; //$NON-NLS-1$
				} else {
					enableButtons();
					lblPropertyError.setVisible(false);
				}
				return null;

			}
		};
		return propertyValidator;
	}

	/**
	 * Disable buttons.
	 */
	void disableButtons() {

		// applyButton.setEnabled(false);

	}

	/**
	 * Enable buttons.
	 */
	void enableButtons() {

		// applyButton.setEnabled(true);

	}

	/**
	 * This Method is used to set the propagated field names.
	 * 
	 * @param fieldNameList
	 */
	public void setSourceFieldsFromPropagatedSchema(List<String> fieldNameList) {
		this.sourceFieldsList = fieldNameList;

	}

	private String formatDataToTransfer(TableItem[] selection) {
		StringBuffer buffer = new StringBuffer();
		for (TableItem tableItem : selection) {
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


	/*
	 * public void imageShell(Shell shell){ String image = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH +
	 * "/icons/property_window_icon.png"; shell.setImage(new Image(null, image)); }
	 */
	public static void main(String[] args) {
		SecondaryColumnKeysWidgetWizard obj = new SecondaryColumnKeysWidgetWizard();
		obj.launchRuntimeWindow(new Shell(), null);
	}
}
