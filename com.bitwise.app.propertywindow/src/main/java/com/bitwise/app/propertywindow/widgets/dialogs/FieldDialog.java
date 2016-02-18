package com.bitwise.app.propertywindow.widgets.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTCellModifier;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterContentProvider;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterLabelProvider;


/**
 * 
 * The class to create key field dialog. 
 * 
 * @author Bitwise
 *
 */

public class FieldDialog extends Dialog {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FieldDialog.class);

	private final List<FilterProperties> propertyLst;
	private static final String FilterInputFieldName = "Component Name"; //$NON-NLS-1$
	private List<String> fieldNameList;
	private String componentName;

	private final String PROPERTY_EXISTS_ERROR = Messages.RuntimePropertAlreadyExists;
	private static final String[] PROPS = { FilterInputFieldName };
	private final String PROPERTY_NAME_BLANK_ERROR = Messages.EmptyNameNotification;
	private Label lblPropertyError;
	private TableViewer targetTableViewer;
	private TableViewer sourceTableViewer;
	private Table sourceTable;
	private Table targetTable;

	private boolean isAnyUpdatePerformed;

	private TableColumn sourceTableColumn;
	private TableViewerColumn tableViewerColumn;
	private DragSource dragSource;
	private DropTarget dropTarget;
	private List<String> sourceFieldsList;

	PropertyDialogButtonBar propertyDialogButtonBar;

	
	public FieldDialog(Shell parentShell, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(parentShell);

		propertyLst = new ArrayList<FilterProperties>();
		fieldNameList = new ArrayList<String>();
		this.propertyDialogButtonBar = propertyDialogButtonBar;
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

	/**
	 * 
	 * Returns the list of key field names
	 * 
	 * @return - list of key fields
	 */
	public List<String> getFieldNameList() {
		return fieldNameList;
	}

	/**
	 * set the list of key fields
	 * 
	 * @param runtimePropertySet
	 */
	public void setRuntimePropertySet(List<String> runtimePropertySet) {
		this.fieldNameList = runtimePropertySet;
	}

	/**
	 * 
	 * returns the name of component
	 * 
	 * @return - name of component
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * 
	 * Set the name of component
	 * 
	 * @param componentName
	 */
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

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		isAnyUpdatePerformed = false;

		if (Constants.COLUMN_NAME2.equalsIgnoreCase(componentName)) {
			getShell().setText(Constants.COLUMN_NAME2);
		}
		if (Constants.OPERATION_FIELD.equalsIgnoreCase(componentName)) {
			getShell().setText(Constants.OPERATION_FIELD);
		}

		Composite container = (Composite) super.createDialogArea(parent);
		ColumnLayout cl_container = new ColumnLayout();
		cl_container.verticalSpacing = 0;
		cl_container.maxNumColumns = 1;
		container.setLayout(cl_container);

		addSeperator(container);
		addButtonPanel(container);

		Composite tableComposite = new Composite(container, SWT.NONE);
		tableComposite.setLayout(new GridLayout(2, false));
		ColumnLayoutData cld_composite_2 = new ColumnLayoutData();
		cld_composite_2.heightHint = 355;
		tableComposite.setLayoutData(cld_composite_2);

		createSourceTable(tableComposite);
		createTargetTable(tableComposite);

		addErrorLabel(container);
		return container;
	}

	private void addSeperator(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		ColumnLayout cl_composite = new ColumnLayout();
		cl_composite.maxNumColumns = 1;
		composite.setLayout(cl_composite);
		ColumnLayoutData cld_composite = new ColumnLayoutData();
		cld_composite.heightHint = 28;
		composite.setLayoutData(cld_composite);

		Label lblTestlabel = new Label(composite, SWT.NONE);
		lblTestlabel.setText(getComponentName() + "Properties");

		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
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

	private void addButtonPanel(Composite container) {
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, false));
		ColumnLayoutData cld_composite_1 = new ColumnLayoutData();
		cld_composite_1.horizontalAlignment = ColumnLayoutData.RIGHT;
		cld_composite_1.heightHint = 28;
		composite_1.setLayoutData(cld_composite_1);

		Label addButton = new Label(composite_1, SWT.NONE);
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		addButton.setImage(new Image(null, XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/add.png"));
		attachAddButtonListern(addButton);

		Label deleteButton = new Label(composite_1, SWT.NONE);
		deleteButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		deleteButton.setImage(new Image(null, XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/delete.png"));
		attachDeleteButtonListener(deleteButton);

		Label upButton = new Label(composite_1, SWT.NONE);
		upButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upButton.setImage(new Image(null, XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/up.png"));
		attachUpButtonListener(upButton);

		Label downButton = new Label(composite_1, SWT.NONE);
		downButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		downButton.setImage(new Image(null, XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/down.png"));
		attachDownButtonListerner(downButton);
	}

	private void attachDownButtonListerner(Label downButton) {
		downButton.addMouseListener(new MouseListener() {
			int index1 = 0, index2 = 0;

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// Nothing to do

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// Nothing to do

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

	private void attachUpButtonListener(Label upButton) {
		upButton.addMouseListener(new MouseListener() {
			int index1 = 0, index2 = 0;

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// Nothing to do

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// Nothing to do

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

	}

	private void attachDeleteButtonListener(Label deleteButton) {
		deleteButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// Nothing to do
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// Nothing to do
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

	}

	private void attachAddButtonListern(Label addButton) {
		addButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// Nothing to do

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// Nothing to do

			}

			@Override
			public void mouseUp(MouseEvent e) {
				targetTable.getParent().setFocus();
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
		return new Point(550, 522);
	}

	private void createTargetTable(Composite container) {
		targetTableViewer = new TableViewer(container, SWT.BORDER | SWT.MULTI);
		targetTable = targetTableViewer.getTable();
		GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table_1.widthHint = 285;
		targetTable.setLayoutData(gd_table_1);

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

		targetTable.setBounds(196, 70, 324, 400);
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

		CellEditor propertyNameEditor = new TextCellEditor(targetTable);

		CellEditor[] editors = new CellEditor[] { propertyNameEditor };
		propertyNameEditor.setValidator(createNameEditorValidator(PROPERTY_NAME_BLANK_ERROR));

		targetTableViewer.setColumnProperties(PROPS);
		targetTableViewer.setCellModifier(new ELTCellModifier(targetTableViewer));
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

	public void createSourceTable(Composite container) {

		sourceTableViewer = new TableViewer(container, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		sourceTable = sourceTableViewer.getTable();
		sourceTable.setLinesVisible(true);
		sourceTable.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.widthHint = 189;
		sourceTable.setLayoutData(gd_table);

		tableViewerColumn = new TableViewerColumn(sourceTableViewer, SWT.NONE);
		sourceTableColumn = tableViewerColumn.getColumn();
		sourceTableColumn.setWidth(200);
		sourceTableColumn.setText(Messages.AVAILABLE_FIELDS_HEADER);
		getSourceFieldsFromPropagatedSchema(sourceTable);
		dragSource = new DragSource(sourceTable, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) { // Set the data to be the first selected item's text

				event.data = formatDataToTransfer(sourceTable.getSelection());
			}

		});
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
					return "ERROR"; //$NON-NLS-1$
				} else {
					lblPropertyError.setVisible(false);
				}

				for (FilterProperties temp : propertyLst) {
					if (!currentSelectedFld.equalsIgnoreCase(valueToValidate)
							&& temp.getPropertyname().trim().equalsIgnoreCase(valueToValidate)) {
						lblPropertyError.setText(PROPERTY_EXISTS_ERROR);
						lblPropertyError.setVisible(true);
						return "ERROR"; //$NON-NLS-1$
					} 
					
					lblPropertyError.setVisible(false);
				}

				return null;

			}
		};
		return propertyValidator;
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
	 * @return String, String having comma separated field names
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
	 * @param commaSeperatedString
	 *            , Comma separated string,
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

	@Override
	protected void okPressed() {
		if (validate()) {
			fieldNameList.clear();
			for (FilterProperties temp : propertyLst) {
				fieldNameList.add(temp.getPropertyname());
			}

			if (isAnyUpdatePerformed) {
				propertyDialogButtonBar.enableApplyButton(true);
			}

			super.okPressed();
		} else {
			return;
		}
	}

	@Override
	protected void cancelPressed() {
		if ((isAnyUpdatePerformed) && (targetTable.getItemCount() != 0 || isAnyUpdatePerformed)) {
			int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
			MessageBox messageBox = new MessageBox(getShell(), style);
			messageBox.setText("Information"); //$NON-NLS-1$
			messageBox.setMessage(Messages.MessageBeforeClosingWindow);
			if (messageBox.open() == SWT.YES) {
				super.cancelPressed();
			}
		} else {
			super.cancelPressed();
		}

	}

}
