package hydrograph.ui.propertywindow.widgets.customwidgets.oracle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.dialogs.FieldDialog;
import hydrograph.ui.propertywindow.widgets.utility.SchemaSyncUtility;

import org.eclipse.swt.widgets.Button;

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
	private Button primaryKeysButton;
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
		grpLoadType.setText("Load Type");
		grpLoadType.setLayout(new GridLayout(1, false));
		grpLoadType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite loadConfigurationComposite = new Composite(grpLoadType, SWT.NONE);
		loadConfigurationComposite.setLayout(new GridLayout(3, false));
		loadConfigurationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		updateRadioButton = new Button(loadConfigurationComposite, SWT.RADIO);
		updateRadioButton.setText(Constants.LOAD_TYPE_UPDATE_KEY);
		
		updateTextBox = new Text(loadConfigurationComposite, SWT.BORDER);
		updateTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		final Button updateKeysButton = new Button(loadConfigurationComposite, SWT.NONE);
		updateKeysButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		updateKeysButton.setText("Update By Keys");
		updateKeysButton.setEnabled(false);
		updateRadioButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(updateRadioButton.getSelection()){
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
				}else{
					updateKeysButton.setEnabled(false);
					updateTextBox.setText("");
				}
			}
		});
		
		
		
		
		 newTableRadioButton = new Button(loadConfigurationComposite, SWT.RADIO);
		newTableRadioButton.setText(Constants.LOAD_TYPE_NEW_TABLE_KEY);
		
		newTableTextBox = new Text(loadConfigurationComposite, SWT.BORDER);
		newTableTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		primaryKeysButton = new Button(loadConfigurationComposite, SWT.NONE);
		primaryKeysButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		primaryKeysButton.setText("Primary Keys");
		primaryKeysButton.setEnabled(false);
		
		newTableRadioButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(newTableRadioButton.getSelection()){
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
				}else{
					primaryKeysButton.setEnabled(false);
					newTableTextBox.setText("");
				}
				
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
		}else
		
		if(newTableRadioButton.getSelection()){
			{
			loadTypeConfigurationSelectedValue.put(newTableRadioButton.getText(), newTableTextBox.getText());
			}
		}else
		
		if(insertRadioButton.getSelection()){
				loadTypeConfigurationSelectedValue.put(insertRadioButton.getText(), insertRadioButton.getText());
		}else
		
		if(replaceRadioButton.getSelection()){
				loadTypeConfigurationSelectedValue.put(replaceRadioButton.getText(), replaceRadioButton.getText());
		}	
		super.okPressed();
	}
	
	public boolean isOkPressed(){
		return this.okPressed;
	}
}
