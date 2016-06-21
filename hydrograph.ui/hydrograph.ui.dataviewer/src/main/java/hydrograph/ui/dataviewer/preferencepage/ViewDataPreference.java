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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
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
	private StringFieldEditor delimiterEditor;
	private IntegerFieldEditor memoryFieldEditor;
	private IntegerFieldEditor portNo;
	private StringFieldEditor quoteEditor;
	private DirectoryFieldEditor tempPathFieldEditor;
	private DirectoryFieldEditor defaultPathFieldEditor;
	private BooleanFieldEditor purgeEditor;
	private List<FieldEditor> editorList;
	
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
		final Composite composite = new Composite(parent, SWT.None);
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
		gridData.widthHint = 610;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		group.setLayoutData(gridData);
		group.setLayout(gl_composite_3);
		
		
		memoryFieldEditor =new IntegerFieldEditor(PreferenceConstants.VIEW_DATA_FILE_SIZE, " File Size (MB)", group, 6);
		memoryFieldEditor.setErrorMessage(null);
		memoryFieldEditor.setFocus();
		memoryFieldEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				validationForIntegerField(memoryFieldEditor.getStringValue(), memoryFieldEditor, Messages.FILE_INTEGER_FIELD_VALIDATION);
				validationForIntegerValue(memoryFieldEditor.getStringValue(), memoryFieldEditor, Messages.File_FIELD_NUMERIC_VALUE_ACCPECTED);
			}
		});
		memoryFieldEditor.setPreferenceStore(getPreferenceStore());
		memoryFieldEditor.load();
		
		Button unusedButton0 = new Button(group, SWT.None);
		unusedButton0.setText("mb");
		unusedButton0.setBounds(0, 0, 20, 10);
		unusedButton0.setVisible(false);
		
		pageSizeEditor = new IntegerFieldEditor(PreferenceConstants.VIEW_DATA_PAGE_SIZE, " Page Size", group, 6);
		pageSizeEditor.setErrorMessage(null);
		pageSizeEditor.setFocus();
		pageSizeEditor.setPreferenceStore(getPreferenceStore());
		pageSizeEditor.load();
		
		Button unusedButton1 = new Button(group, SWT.None);
		unusedButton1.setText("unused");
		unusedButton1.setBounds(0, 0, 20, 10);
		unusedButton1.setVisible(false);
		
		pageSizeEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				int pageSize = 0;
				String value = event.getNewValue().toString();
				validationForIntegerField(pageSizeEditor.getStringValue(), pageSizeEditor, Messages.PAGE_INTEGER_FIELD_VALIDATION);
				validationForIntegerValue(pageSizeEditor.getStringValue(), pageSizeEditor, Messages.PAGE_FIELD_NUMERIC_VALUE_ACCPECTED);
				if(value.equalsIgnoreCase("true")){
					pageSize = Integer.parseInt(pageSizeEditor.getStringValue());
				}
				if(pageSize > 5000){
					setMessage(Messages.PAGE_SIZE_WARNING, 2);
				}else{ setMessage(null); }
			}
		});
		pageSizeEditor.getTextControl(group).addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) { }
			@Override
			public void focusGained(FocusEvent e) {
				Text text = (Text) e.getSource();
				String value = text.getText();
				int pageSize = 0;
				if(value.matches("\\d+")){
					pageSize = Integer.parseInt(pageSizeEditor.getStringValue());
				}
				if(pageSize > 5000){
					setMessage(Messages.PAGE_SIZE_WARNING, 2);
				}else{ setMessage(null); }
			}
		});
		tempPathFieldEditor = new DirectoryFieldEditor(PreferenceConstants.VIEW_DATA_TEMP_FILEPATH, " Local Temp Path", group);
		tempPathFieldEditor.setPreferenceStore(getPreferenceStore());
		tempPathFieldEditor.setFocus();
		tempPathFieldEditor.setErrorMessage(null);
		tempPathFieldEditor.load();
		
	 
		Group grpExportData = new Group(composite, SWT.NONE);
		GridData gd_grpExportData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_grpExportData.widthHint = 610;
		gd_grpExportData.heightHint = 92;
		grpExportData.setLayoutData(gd_grpExportData);
		grpExportData.setText("Export Data");
		
		delimiterEditor = new StringFieldEditor(PreferenceConstants.DELIMITER, " Delimiter", grpExportData);
		delimiterEditor.setErrorMessage(null);
		delimiterEditor.setFocus();
		delimiterEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				String value = event.getNewValue().toString();
				
				Notification note1 =validateQuoteCharacter();
				if(note1.hasErrors()){
					setValid(false);
					quoteEditor.setErrorMessage(note1.errorMessage());
					setErrorMessage(note1.errorMessage());
				}else{
					setErrorMessage(null);
					quoteEditor.setErrorMessage("");
				}
				
				Notification note = validateDelimiter();
				if(note.hasErrors()){
					setValid(false);
					delimiterEditor.setErrorMessage(note.errorMessage());
					setErrorMessage(note.errorMessage());
				}else{
					setErrorMessage(null);
					delimiterEditor.setErrorMessage("");
					checkState();
				} 
				if(value.length() == 1 && !value.equalsIgnoreCase(",")){
					setMessage(Messages.DELIMITER_WARNING, 2);
				}else{ setMessage(null); }
			}
		});
		delimiterEditor.getTextControl(grpExportData).addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) { }
			@Override
			public void focusGained(FocusEvent event) {
				Text text = (Text) event.getSource();
				String value = text.getText();
				if(value.length() == 1 && !value.equalsIgnoreCase(",")){
					setMessage(Messages.DELIMITER_WARNING, 2);
				}else{ setMessage(null); }
			}
		});
		delimiterEditor.setPreferenceStore(getPreferenceStore());
		delimiterEditor.load();
		
		Button unusedButton2 = new Button(grpExportData, SWT.None);
		unusedButton2.setText("");
		unusedButton2.setVisible(false);
		
		quoteEditor = new StringFieldEditor(PreferenceConstants.QUOTE_CHARACTOR, " Quote Character", grpExportData);
		quoteEditor.setErrorMessage(null);
		quoteEditor.setFocus();
		
		quoteEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				String value = event.getNewValue().toString();
				
				Notification note1 = validateDelimiter();
				if(note1.hasErrors()){
					setValid(false);
					delimiterEditor.setErrorMessage(note1.errorMessage());
					setErrorMessage(note1.errorMessage());
				}else{
					setErrorMessage(null);
					delimiterEditor.setErrorMessage("");
					checkState();
				}
				Notification note =validateQuoteCharacter();
				if(note.hasErrors()){
					setValid(false);
					quoteEditor.setErrorMessage(note.errorMessage());
					setErrorMessage(note.errorMessage());
				}else{
					setErrorMessage(null);
					quoteEditor.setErrorMessage("");
					checkState();
				} 
				if(value.length() == 1 && !value.equalsIgnoreCase("\"")){
					setMessage(Messages.QUOTE_WARNING, 2);
				}else{ setMessage(null); }
			}
		});
		quoteEditor.getTextControl(grpExportData).addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) { }
			@Override
			public void focusGained(FocusEvent event) {
				Text text = (Text) event.getSource();
				String value = text.getText();
				if(value.length() == 1 && !value.equalsIgnoreCase("\"")){
					setMessage(Messages.QUOTE_WARNING, 2);
				}else{ setMessage(null); }
			}
		});
		
		quoteEditor.setPreferenceStore(getPreferenceStore());
		quoteEditor.load();
		
		Button unusedButton3 =new Button(grpExportData, SWT.None);
		unusedButton3.setText("");
		unusedButton3.setVisible(false);
		
		defaultPathFieldEditor = new DirectoryFieldEditor(PreferenceConstants.DEFAULTPATH, " Default Path", grpExportData);
		defaultPathFieldEditor.setErrorMessage(null);
		defaultPathFieldEditor.setFocus();
		defaultPathFieldEditor.setPreferenceStore(getPreferenceStore());
		defaultPathFieldEditor.load();
		
		Group grpServiceDetails = new Group(composite, SWT.NONE);
		GridLayout gl_grpServiceDetails = new GridLayout(1, true);
		GridData gd_grpServiceDetailsData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_grpServiceDetailsData.widthHint = 584;
		gd_grpServiceDetailsData.heightHint = 34;
		grpServiceDetails.setLayoutData(gd_grpServiceDetailsData);
		grpServiceDetails.setLayout(gl_grpServiceDetails);
		grpServiceDetails.setText("Service Details");
		
		Composite grpServiceDetailsCmposite = new Composite(grpServiceDetails, SWT.None);
		grpServiceDetailsCmposite.setBounds(0, 0, 280, 16);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 26;
		gd_composite_1.widthHint = 505;
		grpServiceDetailsCmposite.setLayoutData(gd_composite_1);

		portNo = new IntegerFieldEditor(PreferenceConstants.PORT_NO, "Port No               ", grpServiceDetailsCmposite,4);
		portNo.setPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				String value = event.getNewValue().toString();
				validationForIntegerField(value,portNo,Messages.PORTNO_FIELD_VALIDATION);
				
			}
		});
		portNo.setPreferenceStore(getPreferenceStore());
		portNo.load();
		 
		booleanFieldEditor = new BooleanFieldEditor(PreferenceConstants.INCLUDE_HEADER, " Include Headers  ", SWT.DEFAULT, composite);
		getPreferenceStore().setDefault(PreferenceConstants.INCLUDE_HEADER, true);
		booleanFieldEditor.setPreferenceStore(getPreferenceStore());
		booleanFieldEditor.setPreferenceStore(getPreferenceStore());
		booleanFieldEditor.load();
		
		purgeEditor = new BooleanFieldEditor(PreferenceConstants.PURGE_DATA_FILES, " Purge View Data Files  ", composite);
		getPreferenceStore().setDefault(PreferenceConstants.PURGE_DATA_FILES, true);
		purgeEditor.setPreferenceStore(getPreferenceStore());
		purgeEditor.load();
		
		Composite composite01 = new Composite(parent, SWT.None);
		composite01.setBounds(200, 0, 300, 450);
		composite01.setLayout(new GridLayout(1, false));
		Group group_1 = new Group(composite01, SWT.NONE);
		group_1.setVisible(false);
		Label lblNewLabel = new Label(group_1, SWT.NONE);
		lblNewLabel.setBounds(10, 52, 139, 100);
		lblNewLabel.setText(" ");
		
		addFields(memoryFieldEditor);
		addFields(defaultPathFieldEditor);
		addFields(delimiterEditor);
		addFields(pageSizeEditor);
		addFields(quoteEditor);
		addFields(tempPathFieldEditor);
		
		return null;
	}



	private void addFields(FieldEditor editor){
		if (editorList == null) {
			editorList = new ArrayList<>();
		}
		editorList.add(editor);
	}
	

	private void checkState() {
		if(editorList != null){
			int size = editorList.size();
			for(int i=0; i<size; i++){
				FieldEditor fieldEditor = editorList.get(i);
				if(StringUtils.isNotBlank(((StringFieldEditor)fieldEditor).getErrorMessage())){
					setErrorMessage(((StringFieldEditor)fieldEditor).getErrorMessage());
					 setValid(false);
					 break;
				}else{
					setValid(true);
				}
			}
		}
	}
	
	
	private Notification validateDelimiter(){
		Notification notification = new Notification();
		if(delimiterEditor.getStringValue().equalsIgnoreCase(quoteEditor.getStringValue())){
			notification.addError(Messages.DELIMITER_VALUE_MATCH_ERROR);
		}
		if(StringUtils.length(ConvertHexValues.parseHex(delimiterEditor.getStringValue())) != 1){
			notification.addError(Messages.SINGLE_CHARACTOR_ERROR_MESSAGE);
		}
		return notification;
	}

	
	private void validationForIntegerValue(String value , IntegerFieldEditor editor, String message){
		if(StringUtils.isNotBlank(value) && value.matches("\\d+")){
			if(Integer.parseInt(value)<=0){
				setErrorMessage(message);
				editor.setErrorMessage(message);
				setValid(false);
			}else{
				setValid(true);
				editor.setErrorMessage("");
				checkState();
			}
		}
	}
	
	private Notification validateQuoteCharacter(){
		Notification notification = new Notification();
		if(quoteEditor.getStringValue().equalsIgnoreCase(delimiterEditor.getStringValue())){
			notification.addError(Messages.QUOTE_VALUE_MATCH_ERROR);
		}
		if(StringUtils.length(ConvertHexValues.parseHex(quoteEditor.getStringValue())) != 1){
			notification.addError(Messages.SINGLE_CHARACTOR_ERROR_MESSAGE);
		}
		return notification;
	}
	
	private void validationForIntegerField(String value, IntegerFieldEditor editor, String message){
		if(!StringUtils.isNotBlank(value) || !value.matches("\\d+")){
			setErrorMessage(message);
			editor.setErrorMessage(message);
			setValid(false);
		}else{
			setErrorMessage(null);
			editor.setErrorMessage("");
			setValid(true);
			checkState();
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
		preferenceStore.setDefault(PreferenceConstants.PORT_NO, "8004");
		
		setPreferenceStore(preferenceStore);
	}
	
	
	@Override
	protected void performDefaults() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		tempPathFieldEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.VIEW_DATA_TEMP_FILEPATH));
		defaultPathFieldEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.DEFAULTPATH));
		pageSizeEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.VIEW_DATA_PAGE_SIZE));
		memoryFieldEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.VIEW_DATA_FILE_SIZE));
		delimiterEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.DELIMITER));
		quoteEditor.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.QUOTE_CHARACTOR));
		portNo.setStringValue(preferenceStore.getDefaultString(PreferenceConstants.PORT_NO));
		booleanFieldEditor.loadDefault();
		purgeEditor.loadDefault();
	}
	
	@Override
	protected void performApply() {
		memoryFieldEditor.store();
		pageSizeEditor.store();
		delimiterEditor.store();
		quoteEditor.store();
		tempPathFieldEditor.store();
		defaultPathFieldEditor.store();
		booleanFieldEditor.store();
		pageSizeEditor.store();
		portNo.store();
	}
	
	@Override
	public boolean performOk() {
		memoryFieldEditor.store();
		pageSizeEditor.store();
		delimiterEditor.store();
		quoteEditor.store();
		tempPathFieldEditor.store();
		defaultPathFieldEditor.store();
		booleanFieldEditor.store();
		purgeEditor.store();
		portNo.store();
		return super.performOk();
	}
}
