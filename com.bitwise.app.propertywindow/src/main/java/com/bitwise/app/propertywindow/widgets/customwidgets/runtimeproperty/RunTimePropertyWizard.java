package com.bitwise.app.propertywindow.widgets.customwidgets.runtimeproperty;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
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

import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;

/**
 * Wizard class for rendering Runtime Properties window
 * 
 * @author Bitwise
 * 
 */
public class RunTimePropertyWizard {
	private static final String ERROR = "ERROR";
	public static final String PROPERTY_NAME = "PROPERTY_NAME"; 
	public static final String PROPERTY_VALUE = "PROPERTY_VALUE";
	
	private static final String INFORMATION = "Information";
	private static final String REGEX_PPATTERN = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}||[\\w]*";
	private static final String[] PROPS = { PROPERTY_NAME, PROPERTY_VALUE };
	
	private static final String ICONS_DOWN_PNG = "/icons/down.png";
	private static final String ICONS_UP_PNG = "/icons/up.png";
	private static final String ICONS_DELETE_PNG = "/icons/delete.png";
	private static final String ICONS_ADD_PNG = "/icons/add.png";
	private static final String ICONS_PROPERTY_WINDOW_ICON_PNG = "/icons/property_window_icon.png";
	
	private String PROPERTY_EXISTS_ERROR = Messages.RuntimePropertAlreadyExists;
	private String PROPERTY_NAME_BLANK_ERROR = Messages.EmptyNameNotification;
	private String PROPERTY_VALUE_BLANK_ERROR = Messages.EmptyValueNotification;

	private boolean isOkPressed;
	private boolean isAnyUpdatePerformed;

	private List<RuntimeProperties> propertyList;
	private Map<String, String> runtimePropertyMap;

	private Table table;
	private Shell shell;
	private Label lblHeader;
	private Label lblPropertyError;
	private TableViewer tableViewer;
	private Button addButton, okButton, deleteButton, cacelButton, upButton, downButton;

	/**
	 * Instantiates a new run time property wizard.
	 */
	public RunTimePropertyWizard() {
		propertyList = new LinkedList<RuntimeProperties>();
		runtimePropertyMap = new LinkedHashMap<String, String>();
	}

	// Add New Property After Validating old properties
	private void addNewProperty(TableViewer tabViewer) {
		isAnyUpdatePerformed = true;
		RuntimeProperties property = new RuntimeProperties();
		if (propertyList.size() != 0) {
			if (!validate()){
				return;
			}
			property.setPropertyName(""); 
			property.setPropertyValue(""); 
			propertyList.add(property);
			tabViewer.refresh();
			tableViewer.editElement(tableViewer.getElementAt(propertyList.size() - 1), 0);
		} else {
			property.setPropertyName(""); 
			property.setPropertyValue(""); 
			propertyList.add(property);
			tabViewer.refresh();
			tableViewer.editElement(tableViewer.getElementAt(0), 0);
		}
	}

	public void setRuntimePropertyMap(Map<String, String> runtimePropertyMap) {
		this.runtimePropertyMap = runtimePropertyMap;
	}

	/**
	 * @param propertyDialogButtonBar
	 * @return
	 * @wbp.parser.entryPoint
	 */
	public Map<String, String> launchRuntimeWindow(Shell parentShell, final PropertyDialogButtonBar propertyDialogButtonBar) {

		shell = new Shell(parentShell, SWT.WRAP | SWT.APPLICATION_MODAL);
		isOkPressed = false;
		isAnyUpdatePerformed = false;
		shell.setSize(506, 542);
		shell.setLayout(null);
		shell.setText(Messages.RUNTIME_WINDOW_NAME);
		imageShell(shell);
		lblHeader = new Label(shell, SWT.NONE);
		lblHeader.setBounds(10, 14, 450, 15);
		lblHeader.setText(Messages.RUNTIME_HEADER);
		new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL).setBounds(0, 35, 523, 2);
		
		Composite com = new Composite(shell, SWT.NONE);
		com.setBounds(0, 38, 520, 30);
		createIcons(com);
		
		// Below Event will be fired when user closes the Runtime window
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				if (isOkPressed && isAnyUpdatePerformed) {
					propertyDialogButtonBar.enableApplyButton(true);
				}
				if ((isAnyUpdatePerformed && !isOkPressed) &&(table.getItemCount()!=0 || isAnyUpdatePerformed)) {
					int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
					MessageBox messageBox = new MessageBox(shell, style);
					messageBox.setText(INFORMATION); 
					messageBox.setMessage(Messages.MessageBeforeClosingWindow);
					event.doit = messageBox.open() == SWT.YES;
				}
			}
		});

		createTable();

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 436, 513, 83);
		createButtons(composite);

		lblPropertyError = new Label(composite, SWT.NONE);
		lblPropertyError.setForeground(new Color(Display.getDefault(), 255, 0, 0));
		lblPropertyError.setBounds(28, 57, 258, 15);
		lblPropertyError.setVisible(false);

		final CellEditor propertyNameeditor = new TextCellEditor(table);

		CellEditor propertyValueeditor = new TextCellEditor(table);
		CellEditor[] editors = new CellEditor[] { propertyNameeditor, propertyValueeditor };

		propertyNameeditor.setValidator(createNameEditorValidator(PROPERTY_NAME_BLANK_ERROR));
		propertyValueeditor.setValidator(createValueEditorValidator(PROPERTY_VALUE_BLANK_ERROR));

		tableViewer.setColumnProperties(PROPS);
		tableViewer.setCellModifier(new RunTimePropertyCellModifier(tableViewer));
		tableViewer.setCellEditors(editors);
		
		//enables the tab functionality
		TableViewerEditor.create(tableViewer, new ColumnViewerEditorActivationStrategy(tableViewer), 
				ColumnViewerEditor.KEYBOARD_ACTIVATION | ColumnViewerEditor.TABBING_HORIZONTAL | 
				ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL);
		
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


	private void imageShell(Shell shell){
		String image = XMLConfigUtil.CONFIG_FILES_PATH + ICONS_PROPERTY_WINDOW_ICON_PNG;
		shell.setImage(new Image(null, image));
	}
	
	// Loads Already Saved Properties..
		private void loadProperties(TableViewer tv) {
			if (runtimePropertyMap != null && !runtimePropertyMap.isEmpty()) {
				for (String key : runtimePropertyMap.keySet()) {
					RuntimeProperties property = new RuntimeProperties();
					if (validateBeforeLoad(key, runtimePropertyMap.get(key))) {
						property.setPropertyName(key);
						property.setPropertyValue(runtimePropertyMap.get(key));
						propertyList.add(property);
					}
				}
				tv.refresh();
			}  
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
			table = tableViewer.getTable();
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					addNewProperty(tableViewer);
				}

				@Override
				public void mouseDown(MouseEvent e) {
					lblPropertyError.setVisible(false);
				}
			});
			tableViewer.getTable().addTraverseListener(new TraverseListener() {
				@Override
				public void keyTraversed(TraverseEvent e) {
					if(e.keyCode == SWT.ARROW_UP){
						e.doit=false;
					}else if(e.keyCode == SWT.ARROW_DOWN){
						e.doit = false;
					}else if(e.keyCode == SWT.TRAVERSE_ARROW_NEXT){
						e.doit = false;
					}else if(e.keyCode == SWT.TRAVERSE_ARROW_PREVIOUS){
						e.doit = false;
					}
					
				}
			});
			
			table.setBounds(10, 68, 465, 400);
			tableViewer.setContentProvider(new PropertyContentProvider());
			tableViewer.setLabelProvider(new PropertyLabelProvider());
			tableViewer.setInput(propertyList);

			TableColumn column1 = new TableColumn(table, SWT.CENTER);
			column1.setText(Messages.PROPERTY_NAME);
			TableColumn column2 = new TableColumn(table, SWT.LEFT_TO_RIGHT);
			column2.setText(Messages.PROPERTY_VALUE);

			for (int i = 0, n = table.getColumnCount(); i < n; i++) {
				table.getColumn(i).pack();
			}
			column1.setWidth(230);
			column2.setWidth(230);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);

		}
		
	private void createIcons(Composite composite){
		new Label(composite, SWT.SEPARATOR|SWT.HORIZONTAL).setBounds(0, 41, 513, 60);
		addButton = new Button(composite, SWT.PUSH);
		String addIconPath = XMLConfigUtil.CONFIG_FILES_PATH + ICONS_ADD_PNG;
		addButton.setImage(new Image(null, addIconPath));
		addButton.setBounds(388, 10, 20, 20);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewProperty(tableViewer);
			}
		});
		
		
		deleteButton = new Button(composite, SWT.PUSH);
		// deleteButton.setText("X");
		String deleteIonPath = XMLConfigUtil.CONFIG_FILES_PATH + ICONS_DELETE_PNG;
		deleteButton.setImage(new Image(null, deleteIonPath));
		deleteButton.setBounds(407, 10, 25, 20);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {				
				IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
				for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();){
				   Object selectedObject = iterator.next();
				   tableViewer.remove(selectedObject);
				   propertyList.remove(selectedObject);
				}
				isAnyUpdatePerformed = true;
			}
		});
		
		upButton = new Button(composite, SWT.PUSH);
		String upIonPath = XMLConfigUtil.CONFIG_FILES_PATH + ICONS_UP_PNG;
		upButton.setImage(new Image(null, upIonPath));
		upButton.setBounds(431, 10, 20, 20);
		
		upButton.addSelectionListener(new SelectionAdapter() {
			int index1=0,index2=0;

			@Override
			public void widgetSelected(SelectionEvent e) {
				index1 = table.getSelectionIndex();
				
				if(index1 > 0){
					String text1 = tableViewer.getTable().getItem(index1).getText(0);
					String value1 = tableViewer.getTable().getItem(index1).getText(1);
					index2 = index1 - 1;
					String text2 = tableViewer.getTable().getItem(index2).getText(0);
					String value2 = tableViewer.getTable().getItem(index2).getText(1);
					
					RuntimeProperties property = new RuntimeProperties();
					property.setPropertyName(text2);
					property.setPropertyValue(value2);
					propertyList.set(index1, property);
					
					property = new RuntimeProperties();
					property.setPropertyName(text1);
					property.setPropertyValue(value1);
					propertyList.set(index2, property);
					tableViewer.refresh();
					table.setSelection(index1 - 1);
					
				}
			}
		});
		
		downButton = new Button(composite, SWT.PUSH);
		String downIonPath = XMLConfigUtil.CONFIG_FILES_PATH + ICONS_DOWN_PNG;
		downButton.setImage(new Image(null, downIonPath));
		downButton.setBounds(450, 10, 25, 20);
		downButton.addSelectionListener(new SelectionAdapter() {
			int index1=0,index2=0;
				
			@Override
			public void widgetSelected(SelectionEvent e) {
				index1=table.getSelectionIndex();
				
				if(index1 < propertyList.size()-1){
					String text1 = tableViewer.getTable().getItem(index1).getText(0);
					String value1 = tableViewer.getTable().getItem(index1).getText(1);
					
					index2 = index1 + 1;
					
					String text2=tableViewer.getTable().getItem(index2).getText(0);
					String value2=tableViewer.getTable().getItem(index2).getText(1);
					
					RuntimeProperties property = new RuntimeProperties();
					property.setPropertyName(text2);
					property.setPropertyValue(value2);
					propertyList.set(index1, property);
					
					property = new RuntimeProperties();
					property.setPropertyName(text1);
					property.setPropertyValue(value1);
					propertyList.set(index2, property);
					tableViewer.refresh();
					table.setSelection(index1 + 1);
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
					runtimePropertyMap.clear();
					isOkPressed = true;
					for (RuntimeProperties temp : propertyList) {
						runtimePropertyMap.put(temp.getPropertyName(), temp.getPropertyValue());
					}
					shell.close();
				} else
					return;
			}
		});
		okButton.setBounds(321, 50, 75, 25);
		okButton.setText("OK"); //$NON-NLS-1$
		cacelButton = new Button(composite, SWT.NONE);
		cacelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				shell.close();
			}
		});
		cacelButton.setBounds(400, 50, 75, 25);
		cacelButton.setText("Cancel");

	}

	/**
	 * Validate.
	 * @return true, if successful
	 */
	protected boolean validate() {
		int propertyCounter = 0;
		for (RuntimeProperties temp : propertyList) {
			if (!temp.getPropertyName().trim().isEmpty()&& !temp.getPropertyValue().trim().isEmpty()) {
				String Regex=REGEX_PPATTERN;
				Matcher matchName = Pattern.compile(Regex).matcher(temp.getPropertyName());
				Matcher matchValue = Pattern.compile(Regex).matcher(temp.getPropertyValue());
				if(!matchName.matches() || !matchValue.matches()){
					table.setSelection(propertyCounter);
					lblPropertyError.setVisible(true);
					lblPropertyError.setText(Messages.ALLOWED_CHARACTERS);
					return false;
				}
				
			} else {
				table.setSelection(propertyCounter);
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
				String currentSelectedFld = table.getItem(table.getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				if (StringUtils.isEmpty(valueToValidate)) {
					lblPropertyError.setText(ErrorMessage);
					lblPropertyError.setVisible(true);
					return ERROR;
				}

				for (RuntimeProperties temp : propertyList) {
					if (!currentSelectedFld.equalsIgnoreCase(valueToValidate)&& 
							temp.getPropertyName().equalsIgnoreCase(valueToValidate)) {
						lblPropertyError.setText(PROPERTY_EXISTS_ERROR);
						lblPropertyError.setVisible(true);
						return ERROR;
					} 
					else{
						lblPropertyError.setVisible(false);
					}
				}
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
				table.getItem(table.getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				if (valueToValidate.isEmpty()) {
					lblPropertyError.setText(ErrorMessage);
					lblPropertyError.setVisible(true);
					return ERROR; //$NON-NLS-1$
				} else {
					lblPropertyError.setVisible(false);
				}
				return null;

			}
		};
		return propertyValidator;
	}
}