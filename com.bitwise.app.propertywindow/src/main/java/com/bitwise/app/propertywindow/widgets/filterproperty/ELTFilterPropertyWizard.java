package com.bitwise.app.propertywindow.widgets.filterproperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
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
import org.eclipse.swt.events.MouseListener;
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
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;

/**
 * The Class ELTFilterPropertyWizard.
 * 
 * @author Bitwise
 */
public class ELTFilterPropertyWizard {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(ELTFilterPropertyWizard.class);

	private Table targetTable;

	private Shell shell;
	private final List<FilterProperties> propertyLst;
	public static final String FilterInputFieldName = "Component Name"; //$NON-NLS-1$
	private List<String> fieldNameList;
	private String componentName;
	private Label lblHeader;
	private final String PROPERTY_EXISTS_ERROR = Messages.RuntimePropertAlreadyExists;
	public static final String[] PROPS = { FilterInputFieldName };
	private final String PROPERTY_NAME_BLANK_ERROR = Messages.EmptyNameNotification;
	private Label lblPropertyError;
	private boolean isOkPressed;
	private TableViewer targetTableViewer;
	private ControlDecoration decorator;
	private List<ControlDecoration> decoratorList;
	public ControlDecoration scaleDecorator;
	private Button okButton, cacelButton;
	private boolean isAnyUpdatePerformed;
	private Label addButton, deleteButton, upButton, downButton;
	private Table sourceTable;
	private TableColumn sourceTableColumn;
	private TableViewerColumn tableViewerColumn;
	private DragSource dragSource;
	private DropTarget dropTarget;
	private List<String> sourceFieldsList;

	/**
	 * Instantiates a new ELT filter property wizard.
	 */
	public ELTFilterPropertyWizard() {
		propertyLst = new ArrayList<FilterProperties>();
		fieldNameList = new ArrayList<String>();
	}

	// Add New Property After Validating old properties
	private void addNewProperty(TableViewer tv, String fieldName) {

		isAnyUpdatePerformed = true;
		FilterProperties filter = new FilterProperties();
		if (fieldName == null)
			fieldName = "";
		if (propertyLst.size() != 0) {
			if (!validate())
				return;
			filter.setPropertyname(fieldName); //$NON-NLS-1$
			propertyLst.add(filter);
			tv.refresh();
			targetTableViewer.editElement(targetTableViewer.getElementAt(propertyLst.size() - 1), 0);
		} else {
			filter.setPropertyname(fieldName);//$NON-NLS-1$
			propertyLst.add(filter);
			tv.refresh();
			targetTableViewer.editElement(targetTableViewer.getElementAt(0), 0);
		}
	}

	public void setRuntimePropertySet(List<String> runtimePropertySet) {
		this.fieldNameList = runtimePropertySet;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	// Loads Already Saved Properties..
	private void loadProperties(TableViewer tv) {

		if (fieldNameList != null && !fieldNameList.isEmpty()) {
			for (String key : fieldNameList) {
				FilterProperties filter = new FilterProperties();
				if (validateBeforeLoad(key)) {
					filter.setPropertyname(key);
					propertyLst.add(filter);
				}
			}
			tv.refresh();
		} else {

			logger.debug("LodProperties :: Empty Map");
		}
	}

	private boolean validateBeforeLoad(String key) {
		if (key.trim().isEmpty()) {
			return false;
		}
		return true;

	}

	// Method for creating Table
	private void createTable() {

		targetTableViewer = new TableViewer(shell, SWT.BORDER | SWT.MULTI);
		targetTable = targetTableViewer.getTable();
		// table.setBackground(new Color(Display.getCurrent(),204,204,204));
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
				}

			}
		});

		targetTable.setBounds(196, 70, 279, 400);
		targetTableViewer.setContentProvider(new ELTFilterContentProvider());
		targetTableViewer.setLabelProvider(new ELTFilterLabelProvider());
		targetTableViewer.setInput(propertyLst);

		TableColumn targetTableColumn = new TableColumn(targetTable, SWT.CENTER);
		targetTableColumn.setText("Field Name");
		targetTableColumn.setWidth(275);
		targetTable.setHeaderVisible(true);
		targetTable.setLinesVisible(true);

		// enables the tab functionality
		TableViewerEditor.create(targetTableViewer, new ColumnViewerEditorActivationStrategy(targetTableViewer),
				ColumnViewerEditor.KEYBOARD_ACTIVATION | ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL);

	}

	/**
	 * @return
	 * @wbp.parser.entryPoint
	 */
	public List<String> launchRuntimeWindow(Shell parentShell, final PropertyDialogButtonBar propertyDialogButtonBar) {

		shell = new Shell(parentShell, SWT.WRAP | SWT.APPLICATION_MODAL);
		isOkPressed = false;
		isAnyUpdatePerformed = false;
		shell.setSize(506, 548);
		shell.setLayout(null);
		if (Constants.COLUMN_NAME2.equalsIgnoreCase(componentName)) {
			shell.setText(Constants.COLUMN_NAME2);
		}
		if(Constants.OPERATION_FIELD.equalsIgnoreCase(componentName)){
			shell.setText(Constants.OPERATION_FIELD);	
		}
		imageShell(shell);

		TableViewer sourceTableViewer = new TableViewer(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		sourceTable = sourceTableViewer.getTable();
		sourceTable.setLinesVisible(true);
		sourceTable.setHeaderVisible(true);
		sourceTable.setBounds(10, 70, 180, 400);

		tableViewerColumn = new TableViewerColumn(sourceTableViewer, SWT.NONE);
		sourceTableColumn = tableViewerColumn.getColumn();
		sourceTableColumn.setWidth(175);
		sourceTableColumn.setText(Messages.AVAILABLE_FIELDS_HEADER);
		getSourceFieldsFromPropagatedSchema(sourceTable);
		dragSource = new DragSource(sourceTable, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				// Set the data to be the first selected item's text

				event.data = formatDataToTransfer(sourceTable.getSelection());
			}

		});

		lblHeader = new Label(shell, SWT.NONE);
		lblHeader.setBounds(10, 14, 450, 15);
		if (getComponentName() != null) {
			lblHeader.setText(getComponentName() + "Properties"); //$NON-NLS-1$
		}/*
		 * else lblHeader.setText("Filter Operation Field");
		 */
		new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL).setBounds(0, 35, 523, 2);

		Composite com = new Composite(shell, SWT.NONE);
		com.setBounds(0, 40, 520, 30);
		createIcons(com);

		// Below Event will be fired when user closes the Runtime window
		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (isOkPressed && isAnyUpdatePerformed) {
					propertyDialogButtonBar.enableApplyButton(true);
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
		composite.setBounds(0, 438, 513, 83);
		// composite.setBounds(0, 30, 250, 30);
		createButtons(composite);

		lblPropertyError = new Label(composite, SWT.NONE);
		lblPropertyError.setForeground(new Color(Display.getDefault(), 255, 0, 0));
		lblPropertyError.setBounds(28, 57, 258, 15);
		lblPropertyError.setVisible(false);

		CellEditor propertyNameEditor = new TextCellEditor(targetTable);

		CellEditor[] editors = new CellEditor[] { propertyNameEditor };
		propertyNameEditor.setValidator(createNameEditorValidator(PROPERTY_NAME_BLANK_ERROR));

		targetTableViewer.setColumnProperties(PROPS);
		targetTableViewer.setCellModifier(new ELTCellModifier(targetTableViewer));
		targetTableViewer.setCellEditors(editors);

		// decorator =
		// WidgetUtility.addDecorator(propertyNameEditor.getControl(),
		// Messages.CHARACTERSET);
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
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		shell.setLocation(x, y);
		// shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch())
				shell.getDisplay().sleep();
		}

		return fieldNameList;
	}

	private void createIcons(Composite composite) {

		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setBounds(0, 41, 513, 60);
		addButton = new Label(composite, SWT.None);
		addButton.setImage(new Image(null, XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/add.png"));
		addButton.setBounds(388, 10, 20, 20);
		addButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseUp(MouseEvent e) {
				targetTable.getParent().setFocus();
				addNewProperty(targetTableViewer, null);

			}

		});

		deleteButton = new Label(composite, SWT.PUSH);
		deleteButton.setImage(new Image(null, XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/delete.png"));
		deleteButton.setBounds(407, 10, 25, 20);
		deleteButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseUp(MouseEvent e) {
				IStructuredSelection selection = (IStructuredSelection) targetTableViewer.getSelection();
				for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
					Object selectedObject = iterator.next();
					targetTableViewer.remove(selectedObject);
					propertyLst.remove(selectedObject);
				}
				isAnyUpdatePerformed = true;

			}

		});

		upButton = new Label(composite, SWT.PUSH);
		upButton.setImage(new Image(null, XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/up.png"));
		upButton.setBounds(431, 10, 20, 20);
		upButton.addMouseListener(new MouseListener() {
			int index1 = 0, index2 = 0;

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseUp(MouseEvent e) {
				index1 = targetTable.getSelectionIndex();

				if (index1 > 0) {
					index2 = index1 - 1;
					String data = targetTableViewer.getTable().getItem(index1).getText();
					String data2 = targetTableViewer.getTable().getItem(index2).getText();

					FilterProperties filter = new FilterProperties();
					filter.setPropertyname(data2);
					propertyLst.set(index1, filter);

					filter = new FilterProperties();
					filter.setPropertyname(data);
					propertyLst.set(index2, filter);
					targetTableViewer.refresh();
					targetTable.setSelection(index1 - 1);
				}
			}
		});

		downButton = new Label(composite, SWT.PUSH);
		downButton.setImage(new Image(null, XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/down.png"));
		downButton.setBounds(450, 10, 25, 20);
		downButton.addMouseListener(new MouseListener() {
			int index1 = 0, index2 = 0;

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseUp(MouseEvent e) {
				index1 = targetTable.getSelectionIndex();

				index1 = targetTable.getSelectionIndex();

				if (index1 < propertyLst.size() - 1) {
					String data = targetTableViewer.getTable().getItem(index1).getText();
					index2 = index1 + 1;
					String data2 = targetTableViewer.getTable().getItem(index2).getText();

					FilterProperties filter = new FilterProperties();
					filter.setPropertyname(data2);
					propertyLst.set(index1, filter);

					filter = new FilterProperties();
					filter.setPropertyname(data);
					propertyLst.set(index2, filter);
					targetTableViewer.refresh();
					targetTable.setSelection(index1 + 1);
				}
			}
		});
	}

	// Creates The buttons For the widget
	private void createButtons(Composite composite) {
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setBounds(0, 41, 513, 2);

		okButton = new Button(composite, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (validate()) {
					fieldNameList.clear();
					isOkPressed = true;
					for (FilterProperties temp : propertyLst) {
						fieldNameList.add(temp.getPropertyname());
					}

					shell.close();
				} else {
					return;
				}
			}
		});
		okButton.setBounds(321, 52, 75, 25);
		okButton.setText("OK"); //$NON-NLS-1$
		cacelButton = new Button(composite, SWT.NONE);
		cacelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				shell.close();
			}
		});
		cacelButton.setBounds(400, 52, 75, 25);

		cacelButton.setText("Cancel"); //$NON-NLS-1$

	}

	/**
	 * Validate.
	 * 
	 * @return true, if successful
	 */
	protected boolean validate() {

		int propertyCounter = 0;

		for (FilterProperties temp : propertyLst) {
			if (!temp.getPropertyname().trim().isEmpty()) {
				String Regex = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}||[\\w]*";
				Matcher matchs = Pattern.compile(Regex).matcher(temp.getPropertyname().trim());
				if (!matchs.matches()) {
					targetTable.setSelection(propertyCounter);
					lblPropertyError.setVisible(true);
					lblPropertyError.setText(Messages.ALLOWED_CHARACTERS);
					// disableButtons();
					return false;
				}
			} else {
				targetTable.setSelection(propertyCounter);
				lblPropertyError.setVisible(true);
				lblPropertyError.setText(Messages.EmptyNameNotification);
				// disableButtons();
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
					// disableButtons();
					return "ERROR"; //$NON-NLS-1$
				}/*
				 * else if(!valueToValidate.matches("[\\w+]*") && !valueToValidate.startsWith("@"))
				 * //!(valueToValidate.contains("@") && valueToValidate.contains("}") && valueToValidate.contains("{")))
				 * { lblPropertyError.setText(Messages .ALLOWED_CHARACTERS.replace("$", valueToValidate));
				 * lblPropertyError.setVisible(true); //disableButtons(); return "Invalid"; }
				 */else {
					lblPropertyError.setVisible(false);
					enableButtons();
				}

				for (FilterProperties temp : propertyLst) {
					if (!currentSelectedFld.equalsIgnoreCase(valueToValidate)
							&& temp.getPropertyname().trim().equalsIgnoreCase(valueToValidate)) {
						lblPropertyError.setText(PROPERTY_EXISTS_ERROR);
						lblPropertyError.setVisible(true);
						// disableButtons();
						return "ERROR"; //$NON-NLS-1$
					} else
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
		okButton.setEnabled(false);

	}

	/**
	 * Enable buttons.
	 */
	void enableButtons() {
		okButton.setEnabled(true);

	}

	public void imageShell(Shell shell) {
		String image = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/property_window_icon.png";
		shell.setImage(new Image(null, image));
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
		for (FilterProperties temp : propertyLst)
			if (temp.getPropertyname().trim().equalsIgnoreCase(valueToValidate))
				return true;
		return false;
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
	 * @param fieldNameList
	 */
	public void setSourceFieldsFromPropagatedSchema(List<String> fieldNameList) {
		this.sourceFieldsList = fieldNameList;

	}

	/**
	 * @return String,
	 * 			String having comma separated field names 
	 */
	public String getResultAsCommaSeprated() {
		StringBuffer result = new StringBuffer();
		for (String fieldName : fieldNameList)
			result.append(fieldName + ",");
		if (result.lastIndexOf(",") != -1)
			result = result.deleteCharAt(result.lastIndexOf(","));
		return result.toString();
	}

	/**
	 * This method sets the property from comma separated String
	 * 
	 * @param commaSeperatedString,
	 * 					Comma separated string, 
	 * 
	 */
	public void setPropertyFromCommaSepratedString(String commaSeperatedString) {
		String[] fieldNameArray = null;
		if (commaSeperatedString != null) {
			fieldNameArray = commaSeperatedString.split(",");
			for (String fieldName : fieldNameArray) {
				fieldNameList.add(fieldName);
			}
		}
	}
}
