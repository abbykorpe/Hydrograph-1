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

 
package hydrograph.ui.propertywindow.widgets.customwidgets.sql;

import hydrograph.ui.datastructure.property.SQLLoadTypeProperty;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.dialogs.FieldDialog;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The class RDBMSLoadTypeConfigGrid
 * 
 * @author Bitwise
 * 
 */
public class RDBMSLoadTypeConfigGrid extends Dialog {


	private SQLLoadTypeProperty configProperty;
	private SQLLoadTypeProperty oldConfigProperty;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private Button[] loadTyepRadio = new Button[3];
	
	private Map<String, List<String>> propagatedFieldNames;
	private Text updateKeys2;
	private Text primaryKeys;
	private String primaryKeysList, updateKeysList;
	private Button btnUpdate2, btnNewTable;
	private final String LOADTYPE_NEW_TABLE = "newTable";
	private final String LOADTYPE_UPDATE = "update";
	private final String LOADTYPE_INSERT = "insert";
	private final String NEW_TABLE_RADIO_BUTTON_TEXT = "New Table";
	private final String UPDATE_RADIO_BUTTON_TEXT = "Update";
	private final String INSERT_RADIO_BUTTON_TEXT = "Insert";
	private final String UPDATE_BUTTON_TEXT = "Update By Keys";
	private final String NEW_TABLE_BUTTON_TEXT = "Primary Keys";
	private final String LOAD_TYPE_HEADER = "Load Type";
	private final String LOAD_TYPE_CONFIG_DIALOG_TEXT = "Load Type Configuration";
	
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param propertyDialogButtonBar
	 * @param sqlLoadTypeProperty
	 */
	public RDBMSLoadTypeConfigGrid(Shell parentShell, PropertyDialogButtonBar propertyDialogButtonBar,
			SQLLoadTypeProperty sqlLoadTypeProperty) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL);
		configProperty = sqlLoadTypeProperty;
		oldConfigProperty = (SQLLoadTypeProperty) sqlLoadTypeProperty.clone();
		
		primaryKeysList = configProperty.getPrimaryKeys();
		updateKeysList = configProperty.getUpdateByKeys();
		
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		setShellStyle(SWT.CLOSE);
		
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	public Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText(LOAD_TYPE_CONFIG_DIALOG_TEXT);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		new Label(container, SWT.NONE);
		
		Label lblHeader = new Label(container, SWT.NONE);
		lblHeader.setText(LOAD_TYPE_HEADER);
		new Label(container, SWT.NONE);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(16, false));
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 102;
		gd_composite.widthHint = 574;
		composite.setLayoutData(gd_composite);
		
		
		Button btnRadioUpdate2 = new Button(composite, SWT.RADIO);
		btnRadioUpdate2.setText(UPDATE_RADIO_BUTTON_TEXT);
		new Label(composite, SWT.NONE);
		loadTyepRadio[0] = btnRadioUpdate2;
		
		updateKeys2 = new Text(composite, SWT.BORDER);
		GridData gd_updateKeys2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 12, 1);
		gd_updateKeys2.widthHint = 283;
		updateKeys2.setLayoutData(gd_updateKeys2);
		
		btnUpdate2  = new Button(composite, SWT.NONE);
		GridData gd_btnUpdate2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btnUpdate2.widthHint = 110;
		btnUpdate2.setLayoutData(gd_btnUpdate2);
		btnUpdate2.setText(UPDATE_BUTTON_TEXT);
		btnUpdate2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				updateKeysList = launchDialogToSelectFields(updateKeysList, UPDATE_BUTTON_TEXT);
				updateKeys2.setText(updateKeysList);
			}
		});
		
		Button btnRadioNewTable = new Button(composite, SWT.RADIO);
		btnRadioNewTable.setText(NEW_TABLE_RADIO_BUTTON_TEXT);
		new Label(composite, SWT.NONE);
		loadTyepRadio[1] = btnRadioNewTable;
		
		primaryKeys = new Text(composite, SWT.BORDER);
		primaryKeys.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 12, 1));
		
		btnNewTable = new Button(composite, SWT.NONE);
		GridData gd_btnNewTable = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btnNewTable.widthHint = 109;
		btnNewTable.setLayoutData(gd_btnNewTable);
		btnNewTable.setText(NEW_TABLE_BUTTON_TEXT);
		btnNewTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				primaryKeysList = launchDialogToSelectFields(primaryKeysList, NEW_TABLE_BUTTON_TEXT );
				primaryKeys.setText(primaryKeysList);
			}
		});
		
		Button btnRadioInsert = new Button(composite, SWT.RADIO);
		GridData gd_btnRadioInsert = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRadioInsert.heightHint = 27;
		btnRadioInsert.setLayoutData(gd_btnRadioInsert);
		btnRadioInsert.setText(INSERT_RADIO_BUTTON_TEXT);
		loadTyepRadio[2] = btnRadioInsert;
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		
		for (int i = 0; i < loadTyepRadio.length; i++) {
			loadTyepRadio[i].addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					Button button = (Button) event.widget;
					if (button.getText().equalsIgnoreCase(UPDATE_RADIO_BUTTON_TEXT )) {
						loadTyepRadio[0].setSelection(true);
						loadTyepRadio[1].setSelection(false);
						loadTyepRadio[2].setSelection(false);
						configProperty.setLoadType(LOADTYPE_UPDATE );
						primaryKeys.setText("");
						primaryKeysList="";
						btnNewTable.setEnabled(false);
						btnUpdate2.setEnabled(true);
					} else if (button.getText().equalsIgnoreCase(NEW_TABLE_RADIO_BUTTON_TEXT)){
						loadTyepRadio[0].setSelection(false);
						loadTyepRadio[1].setSelection(true);
						loadTyepRadio[2].setSelection(false);
						configProperty.setLoadType(LOADTYPE_NEW_TABLE);
						updateKeys2.setText("");
						updateKeysList="";
						btnNewTable.setEnabled(true);
						btnUpdate2.setEnabled(false);
					} else if (button.getText().equalsIgnoreCase(INSERT_RADIO_BUTTON_TEXT)){
						loadTyepRadio[0].setSelection(false);
						loadTyepRadio[1].setSelection(false);
						loadTyepRadio[2].setSelection(true);
						configProperty.setLoadType(LOADTYPE_INSERT);
						primaryKeys.setText("");
						updateKeysList="";
						btnNewTable.setEnabled(false);
						updateKeys2.setText("");
						primaryKeysList="";
						btnUpdate2.setEnabled(false);
					}
				}
			});
		}
		
		populateWidget();
		return container;
	}
	
	private String launchDialogToSelectFields(String availableValues, String title) {
		FieldDialog fieldDialog = new FieldDialog(Display.getCurrent().getActiveShell(), propertyDialogButtonBar);
		fieldDialog.setPropertyFromCommaSepratedString(availableValues);
		fieldDialog.setSourceFieldsFromPropagatedSchema(propagatedFieldNames.get("in0"));
		fieldDialog.setComponentName(title);
		fieldDialog.open();
		return fieldDialog.getResultAsCommaSeprated();

	}

	public void populateWidget() {
		
		
		if(StringUtils.equals(LOADTYPE_UPDATE , configProperty.getLoadType())){
			loadTyepRadio[0].setSelection(true);
			loadTyepRadio[1].setSelection(false);
			loadTyepRadio[2].setSelection(false);
			btnUpdate2.setEnabled(true);
			btnNewTable.setEnabled(false);
			
			if (StringUtils.isNotBlank(configProperty.getUpdateByKeys())) {
				updateKeys2.setText(configProperty.getUpdateByKeys());
			}
		}else if(StringUtils.equals(LOADTYPE_NEW_TABLE, configProperty.getLoadType())){
			loadTyepRadio[0].setSelection(false);
			loadTyepRadio[1].setSelection(true);
			loadTyepRadio[2].setSelection(false);
			btnNewTable.setEnabled(true);
			btnUpdate2.setEnabled(false);
			if (StringUtils.isNotBlank(configProperty.getPrimaryKeys())) {
				primaryKeys.setText(configProperty.getPrimaryKeys());
			}
		}else if(StringUtils.equals(LOADTYPE_INSERT, configProperty.getLoadType())){
			loadTyepRadio[0].setSelection(false);
			loadTyepRadio[1].setSelection(false);
			loadTyepRadio[2].setSelection(true);
			btnNewTable.setEnabled(false);
			btnUpdate2.setEnabled(false);
		}
		
	}

	public Button buttonWidget(Composite parent, int style, int[] bounds, String value) {
		Button button = new Button(parent, style);
		button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		button.setText(value);

		return button;
	}

	public Text textBoxWidget(Composite parent, int[] bounds, String textValue, boolean value) {
		Text text = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.LEFT);
		text.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		text.setText(textValue);
		text.setEditable(value);

		return text;
	}

	public Label labelWidget(Composite parent, int style, int[] bounds, String value) {
		Label label = new Label(parent, style);
		label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		label.setText(value);

		return label;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(643, 284);
	}
	


	/**
	 * set propagated field names
	 * 
	 * @param propagatedFiledNames
	 */
	public void setPropagatedFieldProperty(Map<String, List<String>> propagatedFiledNames) {
		this.propagatedFieldNames = propagatedFiledNames;
	}

	@Override
	protected void okPressed() {
		configProperty.setPrimaryKeys(primaryKeysList);
		configProperty.setUpdateByKeys(updateKeysList);
		if(!oldConfigProperty.equals(configProperty)){
			propertyDialogButtonBar.enableApplyButton(true);
			
		}
		
		
		super.okPressed();
	}
}
