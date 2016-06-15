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

package hydrograph.ui.dataviewer.preferencepage;

import hydrograph.ui.common.util.ConvertHexValues;
import hydrograph.ui.dataviewer.Activator;
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.dataviewer.constants.PreferenceConstants;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.propertywindow.runconfig.Notification;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;


/**
 * View Data Preference
 * @author Bitwise
 *
 */
public class ViewDataPreference extends PreferencePage implements IWorkbenchPreferencePage{
	private BooleanFieldEditor booleanFieldEditor;
	private IntegerFieldEditor pageSizeEditor;
	private StringFieldEditor delimeterEditor;
	private StringFieldEditor quoteEditor;
	private IntegerFieldEditor memoryFieldEditor;
	private DirectoryFieldEditor tempPathFieldEditor;
	private DirectoryFieldEditor defaultPathFieldEditor;
	private BooleanFieldEditor pergeEditor;
	
	public ViewDataPreference() {
		super();
		setPreferenceStore(PlatformUI.getWorkbench().getPreferenceStore());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.None);
		composite.setToolTipText("Export Data");
		composite.setLayout(new GridLayout(3, false));
		
		Group group = new Group(composite, SWT.None);
		group.setText("General");
		
		GridLayout gl_composite_3 = new GridLayout(3, true);
		gl_composite_3.verticalSpacing = 0;
		gl_composite_3.marginWidth = 0;
		gl_composite_3.marginHeight = 0;
		gl_composite_3.horizontalSpacing = 0;
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 3);
		gridData.heightHint = 90;
		gridData.widthHint = 483;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		group.setLayoutData(gridData);
		group.setLayout(gl_composite_3);
		
		
		memoryFieldEditor =new IntegerFieldEditor(PreferenceConstants.VIEW_DATA_FILE_SIZE, " &File Size (MB)", group, 6);
		memoryFieldEditor.setEmptyStringAllowed(false);
		memoryFieldEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				String value = event.getNewValue().toString();
				validationForIntegerField(value);
				validationForIntegerValue(memoryFieldEditor.getStringValue());
				
			}
		});
		memoryFieldEditor.setPreferenceStore(getPreferenceStore());
		memoryFieldEditor.load();
		
		Button bt = new Button(group, SWT.None);
		bt.setText("mb");
		bt.setBounds(0, 0, 20, 10);
		bt.setVisible(false);
		pageSizeEditor = new IntegerFieldEditor(PreferenceConstants.VIEW_DATA_PAGE_SIZE, " &Page Size                ", group, 6);
		pageSizeEditor.setEmptyStringAllowed(false);
		pageSizeEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				int pageSize = 0;
				String value = event.getNewValue().toString();
				
				validationForIntegerField(value);
				validationForIntegerValue(pageSizeEditor.getStringValue());

				if(value.matches("\\d+")){
					pageSize = Integer.parseInt(pageSizeEditor.getStringValue());
				}
				
				if(pageSize > 5000){
					setMessage(Messages.PAGE_SIZE_WARNING, 2);
				}else{
					setMessage(null);
				}
			}
		});
		pageSizeEditor.setPreferenceStore(getPreferenceStore());
		pageSizeEditor.load();
		Button bt1 = new Button(group, SWT.None);
		bt1.setText("mb");
		bt1.setBounds(0, 0, 20, 10);
		bt1.setVisible(false);
		
		tempPathFieldEditor = new DirectoryFieldEditor(PreferenceConstants.VIEW_DATA_TEMP_FILEPATH, " Local Temp Path", group);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		tempPathFieldEditor.setPreferenceStore(getPreferenceStore());
		tempPathFieldEditor.load();
		
		
		
		 
	 
		Group grpExportData = new Group(composite, SWT.NONE);
		GridData gd_grpExportData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_grpExportData.widthHint = 584;
		gd_grpExportData.heightHint = 92;
		grpExportData.setLayoutData(gd_grpExportData);
		grpExportData.setText("Export Data");
		
		delimeterEditor = new StringFieldEditor(PreferenceConstants.DELIMITER, " Delimiter  ", grpExportData);
		delimeterEditor.setEmptyStringAllowed(false);
		delimeterEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				String value = event.getNewValue().toString();
				
				if(value.length() == 1 && !value.equalsIgnoreCase(",")){
					setMessage(Messages.DELIMITER_WARNING, 2);
				}else{
					setMessage(null);
				}
				
				Notification note =validateDelimiter();
				if(note.hasErrors()){
					setValid(false);
					setErrorMessage(note.errorMessage());
				}else{
					setErrorMessage(null);
					setValid(true);
				}
			}
		});
		delimeterEditor.setPreferenceStore(getPreferenceStore());
		delimeterEditor.load();
		
		
		Button b1 = new Button(grpExportData, SWT.None);
		b1.setText("");
		b1.setVisible(false);
		
		quoteEditor = new StringFieldEditor(PreferenceConstants.QUOTE_CHARACTOR, " Quote Character    ", grpExportData);
		quoteEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				String value = event.getNewValue().toString();
				if(value.length() == 1 && !value.equalsIgnoreCase("\"")){
					setMessage(Messages.QUOTE_WARNING, 2);
				}else{
					setMessage(null);
				}
				
				Notification note =validateQuoteCharacter();
				if(note.hasErrors()){
					setValid(false);
					setErrorMessage(note.errorMessage());
				}else{
					setErrorMessage(null);
					setValid(true);
				}
				 
			}
		});
		
		quoteEditor.setPreferenceStore(getPreferenceStore());
		quoteEditor.load();
		
		Button b2 =new Button(grpExportData, SWT.None);
		b2.setText("");
		b2.setVisible(false);
		
		defaultPathFieldEditor = new DirectoryFieldEditor(PreferenceConstants.DEFAULTPATH, " Default Path  ", grpExportData);
		defaultPathFieldEditor.setPreferenceStore(getPreferenceStore());
		defaultPathFieldEditor.load();
		
		booleanFieldEditor = new BooleanFieldEditor(PreferenceConstants.INCLUDE_HEADER, " Include Headers  ", SWT.DEFAULT, composite);
		getPreferenceStore().setDefault(PreferenceConstants.INCLUDE_HEADER, true);
		booleanFieldEditor.setPreferenceStore(getPreferenceStore());
		booleanFieldEditor.setPreferenceStore(getPreferenceStore());
		booleanFieldEditor.load();
		
		pergeEditor = new BooleanFieldEditor(PreferenceConstants.PURGE_DATA_FILES, " Purge View Data Files  ", composite);
		getPreferenceStore().setDefault(PreferenceConstants.PURGE_DATA_FILES, true);
		pergeEditor.setPreferenceStore(getPreferenceStore());
		pergeEditor.load();
		
		Composite composite01 = new Composite(parent, SWT.None);
		composite01.setBounds(200, 0, 300, 650);
		composite01.setLayout(new GridLayout(1, false));
		Group group_1 = new Group(composite01, SWT.NONE);
		group_1.setVisible(false);
		Label lblNewLabel = new Label(group_1, SWT.NONE);
		lblNewLabel.setBounds(10, 52, 139, 298);
		lblNewLabel.setText(" ");
		
		return null;
	}

	private Notification validateDelimiter(){
		Notification notification = new Notification();
		if(delimeterEditor.getStringValue().equalsIgnoreCase(quoteEditor.getStringValue())){
			notification.addError(Messages.DELIMITER_VALUE_MATCH_ERROR);
		}
		if(StringUtils.length(ConvertHexValues.parseHex(delimeterEditor.getStringValue())) != 1){
			notification.addError(Messages.SINGLE_CHARACTOR_ERROR_MESSAGE);
		}
		if(delimeterEditor.getStringValue().length() > 1 && StringUtils.length(ConvertHexValues.parseHex(quoteEditor.getStringValue())) != 1){
			notification.addError(Messages.CHARACTER_LENGTH_ERROR);
		}
		return notification;
	}

	private void validationForIntegerValue(String value){
		if(StringUtils.isNotBlank(value) && value.matches("\\d+")){
			if(Integer.parseInt(value)<=0){
				setErrorMessage(Messages.NUMERIC_VALUE_ACCPECTED);
				setValid(false);
			}else{
				setErrorMessage(null);
				setValid(true);
			}
		}
	}
	
	private Notification validateQuoteCharacter(){
		Notification notification = new Notification();
		if(quoteEditor.getStringValue().equalsIgnoreCase(delimeterEditor.getStringValue())){
			notification.addError(Messages.QUOTE_VALUE_MATCH_ERROR);
		}
		if(StringUtils.length(ConvertHexValues.parseHex(quoteEditor.getStringValue())) != 1){
			notification.addError(Messages.SINGLE_CHARACTOR_ERROR_MESSAGE);
		}
		if(quoteEditor.getStringValue().length() > 1 && StringUtils.length(ConvertHexValues.parseHex(quoteEditor.getStringValue())) != 1){
			notification.addError(Messages.CHARACTER_LENGTH_ERROR);
		}
		return notification;
	}
	
	private void validationForIntegerField(String value){
		if(!StringUtils.isNotBlank(value) || !value.matches("\\d+")){
			setErrorMessage(Messages.INTEGER_FIELD_VALIDATION);
			setValid(false);
		}else{
			setErrorMessage(null);
			setValid(true);
		}
	}
	
	
	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.setDefault(PreferenceConstants.VIEW_DATA_TEMP_FILEPATH, Utils.INSTANCE.getInstallationPath());
		preferenceStore.setDefault(PreferenceConstants.DEFAULTPATH, "");
		preferenceStore.setDefault(PreferenceConstants.VIEW_DATA_FILE_SIZE, "100");
		preferenceStore.setDefault(PreferenceConstants.VIEW_DATA_PAGE_SIZE, "100");
		preferenceStore.setDefault(PreferenceConstants.DELIMITER, ",");
		preferenceStore.setDefault(PreferenceConstants.QUOTE_CHARACTOR, "\"");
		preferenceStore.setDefault(PreferenceConstants.INCLUDE_HEADER, true);
		preferenceStore.setDefault(PreferenceConstants.PURGE_DATA_FILES, true);
		
		setPreferenceStore(preferenceStore);
	}
	
	
	@Override
	protected void performDefaults() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		tempPathFieldEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.VIEW_DATA_TEMP_FILEPATH));
		defaultPathFieldEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.DEFAULTPATH));
		pageSizeEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.VIEW_DATA_PAGE_SIZE));
		memoryFieldEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.VIEW_DATA_FILE_SIZE));
		delimeterEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.DELIMITER));
		quoteEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.QUOTE_CHARACTOR));
		booleanFieldEditor.loadDefault();
		pergeEditor.loadDefault();
	}
	
	@Override
	protected void performApply() {
		memoryFieldEditor.store();
		pageSizeEditor.store();
		delimeterEditor.store();
		quoteEditor.store();
		tempPathFieldEditor.store();
		defaultPathFieldEditor.store();
		booleanFieldEditor.store();
		pageSizeEditor.store();
	}
	
	@Override
	public boolean performOk() {
		memoryFieldEditor.store();
		pageSizeEditor.store();
		delimeterEditor.store();
		quoteEditor.store();
		tempPathFieldEditor.store();
		defaultPathFieldEditor.store();
		booleanFieldEditor.store();
		pergeEditor.store();
		
		return super.performOk();
	}
}
