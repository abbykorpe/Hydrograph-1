package com.bitwise.app.eltproperties.widgets.runtimeproperties;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
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

/**
 * 
 * @author Nitin Gupta Sep 09, 2015
 * 
 */

public class RunTimePropertyWizard {
	private Table table;
	private int propertyCounter;
	private Shell shell;
	private List<RuntimeProperties> propertyLst;
	public static final String RUNTIMEPROPNAME = "Property Name";
	public static final String RUNTIMEPROPVALUE = "Property Value";
	private TreeMap<String, String> runtimePropertyMap;
	private String componentName;
	private Label lblHeader;
	private String PROPERTY_EXISTS_ERROR = "Property name already exists";
	public static final String[] PROPS = { RUNTIMEPROPNAME, RUNTIMEPROPVALUE };
	private String PROPERTY_NAME_BLANK_ERROR = "Property name cannot be blank";
	private String PROPERTY_VALUE_BLANK_ERROR = "Property value cannot be blank";
	private Label lblPropertyError;
	private boolean isOkPressed;
	private TableViewer tableViewer;
	private Button addButton, deleteAll, applyButton, okButton, deleteButton,
			cacelButton;
	private boolean isAnyUpdatePerformed;

	public RunTimePropertyWizard() {

		propertyLst = new ArrayList<RuntimeProperties>();
		runtimePropertyMap = new TreeMap<String, String>();
	}

	private void addNewProperty(TableViewer tv) {
		
		
		isAnyUpdatePerformed=true;
		RuntimeProperties p = new RuntimeProperties();
		String autoGenratedPropertyName = "New Property" + (++propertyCounter);
		for (RuntimeProperties temp : propertyLst) {

			System.out.println("Auto Genrated::" + autoGenratedPropertyName);
			System.out.println("List Genrated::" + temp.getPropertyName());
			if (temp.getPropertyName().equalsIgnoreCase(
					autoGenratedPropertyName)) {
				autoGenratedPropertyName = "New Property" + (++propertyCounter);
			}
		}
		
		p.setPropertyName(autoGenratedPropertyName);
		p.setPropertyValue("Value");
		propertyLst.add(p);
		tv.refresh();
	}

	public void setRuntimePropertyMap(TreeMap<String, String> runtimePropertyMap) {
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
				RuntimeProperties p = new RuntimeProperties();
				p.setPropertyName(key);
				p.setPropertyValue(runtimePropertyMap.get(key));
				propertyLst.add(p);
			}
			tv.refresh();

		} else
			System.out.println("LodProperties :: Empty Map");

	}

	// Method for creating Table
	private void createTable() {

		tableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				addNewProperty(tableViewer);
			}
		});

		table.setBounds(10, 50, 465, 365);
		tableViewer.setContentProvider(new PropertyContentProvider());
		tableViewer.setLabelProvider(new PropertyLabelProvider());
		tableViewer.setInput(propertyLst);

		TableColumn tc1 = new TableColumn(table, SWT.CENTER);
		tc1.setText("Property Name");
		TableColumn tc2 = new TableColumn(table, SWT.LEFT_TO_RIGHT);
		tc2.setText("Property Value");

		for (int i = 0, n = table.getColumnCount(); i < n; i++) {
			table.getColumn(i).pack();
		}
		tc2.setWidth(368);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	/**
	 * @return
	 * @wbp.parser.entryPoint
	 */
	public TreeMap<String, String> launchRuntimeWindow(Shell parentShell) {

		shell = new Shell(parentShell, SWT.WRAP | SWT.APPLICATION_MODAL);
		isOkPressed = false;
		isAnyUpdatePerformed=false;
		shell.setSize(506, 540);
		shell.setLayout(null);
		lblHeader = new Label(shell, SWT.NONE);
		lblHeader.setBounds(10, 14, 450, 15);
		lblHeader.setText(getComponentName() + " Runtime Property");
		new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL).setBounds(0, 35, 523,
				2);
		// Below Event will be fired when user closes the Runtime window
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {

				if (table.getItemCount() != 0 && !isOkPressed && isAnyUpdatePerformed) {
					int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
					MessageBox messageBox = new MessageBox(shell, style);
					messageBox.setText("Information");
					messageBox
							.setMessage("Close the window? This will remove all unapplied changes.");
					event.doit = messageBox.open() == SWT.YES;
				}
			}
		});

		createTable();

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 418, 513, 83);
		createButtons(composite);

		lblPropertyError = new Label(composite, SWT.NONE);
		lblPropertyError.setForeground(new Color(Display.getDefault(), 255, 0,
				0));
		lblPropertyError.setBounds(28, 57, 258, 15);
		lblPropertyError.setVisible(false);

		final CellEditor propertyNameeditor = new TextCellEditor(table);

		CellEditor propertyValueeditor = new TextCellEditor(table);
		CellEditor[] editors = new CellEditor[] { propertyNameeditor,
				propertyValueeditor };
		propertyNameeditor.addListener(createEditorListners());
		propertyValueeditor.addListener(createEditorListners());
		propertyNameeditor
				.setValidator(createNameEditorValidator(PROPERTY_NAME_BLANK_ERROR));
		propertyValueeditor
				.setValidator(createValueEditorValidator(PROPERTY_VALUE_BLANK_ERROR));

		tableViewer.setColumnProperties(PROPS);
		tableViewer
				.setCellModifier(new RunTimePropertyCellModifier(tableViewer));
		tableViewer.setCellEditors(editors);

		loadProperties(tableViewer);

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

	// Creates The buttons For the widget
	private void createButtons(Composite composite) {
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setBounds(0, 41,
				513, 2);
		addButton = new Button(composite, SWT.NONE);
		addButton.setText("Add");
		addButton.setBounds(10, 10, 75, 25);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewProperty(tableViewer);
			}
		});

		deleteButton = new Button(composite, SWT.NONE);
		deleteButton.setText("Delete");
		deleteButton.setBounds(91, 10, 75, 25);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int temp = table.getSelectionIndex();
				if (temp == -1)
					MessageDialog.openError(shell, "Error",
							"Please Select row to delete");
				else {
					table.remove(temp);
					propertyLst.remove(temp);
					isAnyUpdatePerformed=true;
				}
			}
		});
		deleteButton.setImage(null);

		deleteAll = new Button(composite, SWT.NONE);
		deleteAll.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table.getItemCount() != 0) {
					boolean userAns = MessageDialog
							.openConfirm(shell, "Remove all",
									"Do you really want to delete all runtime properties?");
					if (userAns) {
						table.removeAll();
						propertyLst.removeAll(propertyLst);
						propertyCounter = 0;
					}
				}
			}
		});
		deleteAll.setBounds(172, 10, 75, 25);
		deleteAll.setText("Delete All");

		applyButton = new Button(composite, SWT.NONE);
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runtimePropertyMap.clear();
				for (RuntimeProperties temp : propertyLst) {
					runtimePropertyMap.put(temp.getPropertyName(),
							temp.getPropertyValue());
				}
				MessageBox messageBox = new MessageBox(shell, SWT.NONE);
				messageBox.setText("Information");
				messageBox
						.setMessage("Property saved sucessfully");
				messageBox.open();
				isAnyUpdatePerformed=false;
			}
		});
		applyButton.setBounds(253, 10, 75, 25);
		applyButton.setText("Apply");

		okButton = new Button(composite, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runtimePropertyMap.clear();
				isOkPressed = true;
				for (RuntimeProperties temp : propertyLst) {
					runtimePropertyMap.put(temp.getPropertyName(),
							temp.getPropertyValue());
				}
				System.out.println(runtimePropertyMap);
				shell.close();
			}
		});
		okButton.setBounds(321, 52, 75, 25);
		okButton.setText("OK");
		cacelButton = new Button(composite, SWT.NONE);
		cacelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(runtimePropertyMap);
				shell.close();
			}
		});
		cacelButton.setBounds(400, 52, 75, 25);

		cacelButton.setText("Cancel");

	}

	private ICellEditorListener createEditorListners() {
		ICellEditorListener propertyEditorListner = new ICellEditorListener() {

			@Override
			public void editorValueChanged(boolean oldValidState,
					boolean newValidState) {

			}

			@Override
			public void cancelEditor() {
System.out.println("CancelEditor");
			}

			@Override
			public void applyEditorValue() {
				enableButtons();
				lblPropertyError.setVisible(false);

			}
		};
		return propertyEditorListner;
	}

	// Creates CellNAme Validator for table's cells
	private ICellEditorValidator createNameEditorValidator(
			final String ErrorMessage) {
		ICellEditorValidator propertyValidator = new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				isAnyUpdatePerformed=true;
				String currentSelectedFld = table.getItem(
						table.getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				if (valueToValidate.isEmpty()) {
					lblPropertyError.setText(ErrorMessage);
					lblPropertyError.setVisible(true);
					disableButtons();
					return "ERROR";
				}

				for (RuntimeProperties temp : propertyLst) {
					if (!currentSelectedFld.equalsIgnoreCase(valueToValidate)
							&& temp.getPropertyName().trim()
									.equalsIgnoreCase(valueToValidate)) {
						lblPropertyError.setText(PROPERTY_EXISTS_ERROR);
						lblPropertyError.setVisible(true);
						disableButtons();
						return "ERROR";
					} else
						enableButtons();
					lblPropertyError.setVisible(false);
				}

				return null;

			}
		};
		return propertyValidator;
	}
	
	// Creates Value Validator for table's cells
	private ICellEditorValidator createValueEditorValidator(
			final String ErrorMessage) {
		ICellEditorValidator propertyValidator = new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				isAnyUpdatePerformed=true;
				String currentSelectedFld = table.getItem(
						table.getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				if (valueToValidate.isEmpty()) {
					lblPropertyError.setText(ErrorMessage);
					lblPropertyError.setVisible(true);
					disableButtons();
					return "ERROR";
				}
				return null;

			}
		};
		return propertyValidator;
	}
	void disableButtons() {
		okButton.setEnabled(false);
		applyButton.setEnabled(false);

	}

	void enableButtons() {
		okButton.setEnabled(true);
		applyButton.setEnabled(true);

	}

}
