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
package hydrograph.ui.propertywindow.widgets.customwidgets.databasecomponents;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.dialogs.FieldDialog;

import org.eclipse.swt.widgets.Button;
/**
 * LoadTypeConfigurationDialog class creates the dialog for different load types in DB components
 * @author Bitwise
 *
 */
public class LoadTypeConfigurationDialog extends Dialog {
	private Text updateTextBox;
	private Text newTableTextBox;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private String windowLabel;
	private boolean okPressed;
	private String valueForUpdateTextBox;
	private List<String> schemaFields;
	private Button updateRadioButton;
	public Map<String, String> loadTypeConfigurationSelectedValue;
	private Button newTableRadioButton;
	private Button insertRadioButton;
	private Button replaceRadioButton;
	private String valueForNewTableTextBox;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public LoadTypeConfigurationDialog(Shell parentShell, PropertyDialogButtonBar propertyDialogButtonBar , String windowLabel , List<String> fields ,  Map<String, String> loadTypeConfigurationSelectedValue) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL);
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		if(StringUtils.isNotBlank(windowLabel))
			this.windowLabel=windowLabel;
		else
			this.windowLabel=Constants.LOAD_TYPE_CONFIGURATION_WINDOW_LABEL;
		this.schemaFields = fields;
		this.loadTypeConfigurationSelectedValue = loadTypeConfigurationSelectedValue;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		container.getShell().setText(windowLabel);
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		Group grpLoadType = new Group(composite, SWT.NONE);
		grpLoadType.setText(Constants.LOAD_TYPE);
		grpLoadType.setLayout(new GridLayout(1, false));
		grpLoadType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite loadConfigurationComposite = new Composite(grpLoadType, SWT.NONE);
		loadConfigurationComposite.setLayout(new GridLayout(3, false));
		loadConfigurationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		updateRadioButton = new Button(loadConfigurationComposite, SWT.RADIO);
		updateRadioButton.setText(Constants.LOAD_TYPE_UPDATE_KEY);
		
		updateTextBox = new Text(loadConfigurationComposite, SWT.BORDER);
		updateTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button updateKeysButton = new Button(loadConfigurationComposite, SWT.NONE);
		
		updateKeysButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		updateKeysButton.setText(Constants.UPDATE_BY_KEYS);
		updateKeysButton.setEnabled(false);
		
		newTableRadioButton = new Button(loadConfigurationComposite, SWT.RADIO);
		newTableRadioButton.setText(Constants.LOAD_TYPE_NEW_TABLE_KEY);
		
		newTableTextBox = new Text(loadConfigurationComposite, SWT.BORDER);
		newTableTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button primaryKeysButton = new Button(loadConfigurationComposite, SWT.NONE);
		primaryKeysButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		primaryKeysButton.setText(Constants.PRIMARY_KEYS_WINDOW_LABEL);
		primaryKeysButton.setEnabled(false);
		
	updateRadioButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
					updateKeysButton.setEnabled(true);
					updateKeysButton.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							FieldDialog fieldDialog = new FieldDialog(new Shell(), propertyDialogButtonBar);
							fieldDialog.setComponentName(Constants.UPDATE_KEYS_WINDOW_LABEL);
							fieldDialog.setSourceFieldsFromPropagatedSchema(schemaFields);
							fieldDialog.open();
							valueForUpdateTextBox=fieldDialog.getResultAsCommaSeprated();
							if(valueForUpdateTextBox !=null && StringUtils.isNotBlank(valueForUpdateTextBox)){
								updateTextBox.setText(valueForUpdateTextBox);
								}
						}
					}) ;
					
					primaryKeysButton.setEnabled(false);
					newTableTextBox.setText("");
				}
		});
		
		newTableRadioButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
					primaryKeysButton.setEnabled(true);
					primaryKeysButton.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							FieldDialog fieldDialog = new FieldDialog(new Shell(), propertyDialogButtonBar);
							fieldDialog.setComponentName(Constants.PRIMARY_KEYS_WINDOW_LABEL);
							fieldDialog.setSourceFieldsFromPropagatedSchema(schemaFields);
							fieldDialog.open();
							valueForNewTableTextBox = fieldDialog.getResultAsCommaSeprated();
							if(valueForNewTableTextBox !=null && StringUtils.isNotBlank(valueForNewTableTextBox)){
								newTableTextBox.setText(valueForNewTableTextBox);
								}
						}
					});
					updateKeysButton.setEnabled(false);
					updateTextBox.setText("");
			}
		});
		
		insertRadioButton = new Button(loadConfigurationComposite, SWT.RADIO);
		insertRadioButton.setText(Constants.LOAD_TYPE_INSERT_KEY);
		new Label(loadConfigurationComposite, SWT.NONE);
		new Label(loadConfigurationComposite, SWT.NONE);
		
		 replaceRadioButton = new Button(loadConfigurationComposite, SWT.RADIO);
		 replaceRadioButton.setText(Constants.LOAD_TYPE_REPLACE_KEY);
		 new Label(loadConfigurationComposite, SWT.NONE);
		 new Label(loadConfigurationComposite, SWT.NONE);
		
		if(loadTypeConfigurationSelectedValue!=null && !loadTypeConfigurationSelectedValue.isEmpty() ){
			if(loadTypeConfigurationSelectedValue.get(Constants.LOAD_TYPE_NEW_TABLE_KEY) != null){
				newTableRadioButton.setSelection(true);
				primaryKeysButton.setEnabled(true);
				newTableTextBox.setText(loadTypeConfigurationSelectedValue.get(Constants.LOAD_TYPE_NEW_TABLE_KEY));
			}else if(loadTypeConfigurationSelectedValue.get(Constants.LOAD_TYPE_INSERT_KEY) != null){
				insertRadioButton.setSelection(true);
			}else if(loadTypeConfigurationSelectedValue.get(Constants.LOAD_TYPE_REPLACE_KEY) != null){
				replaceRadioButton.setSelection(true);
			}else if(loadTypeConfigurationSelectedValue.get(Constants.LOAD_TYPE_UPDATE_KEY) != null){
				updateRadioButton.setSelection(true);
				updateTextBox.setText(loadTypeConfigurationSelectedValue.get(Constants.LOAD_TYPE_UPDATE_KEY));
				updateKeysButton.setEnabled(true);
			}
		}else{
			updateRadioButton.setEnabled(true);
		}
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	/**
	 * Getter for LoadType Selected value
	 * @return
	 */
	public Map<String, String> getSelectedPropertyValue(){
		return loadTypeConfigurationSelectedValue;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(686, 247);
	}
	
	@Override
	protected void okPressed() {
		loadTypeConfigurationSelectedValue.clear();
		if(updateRadioButton.getSelection()){
				loadTypeConfigurationSelectedValue.put(updateRadioButton.getText() ,updateTextBox.getText() );
		}
		
		if(newTableRadioButton.getSelection()){
			loadTypeConfigurationSelectedValue.put(newTableRadioButton.getText(), newTableTextBox.getText());
		}
		
		if(insertRadioButton.getSelection()){
			loadTypeConfigurationSelectedValue.put(insertRadioButton.getText(), insertRadioButton.getText());
		}
		
		if(replaceRadioButton.getSelection()){
			loadTypeConfigurationSelectedValue.put(replaceRadioButton.getText(), replaceRadioButton.getText());
		}	
		super.okPressed();
	}
	
	public boolean isOkPressed(){
		return this.okPressed;
	}
}
