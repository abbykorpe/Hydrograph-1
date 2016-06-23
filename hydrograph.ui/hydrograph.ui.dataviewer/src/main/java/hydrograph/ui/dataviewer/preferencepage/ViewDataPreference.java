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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
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
		final Composite parentComposite = new Composite(parent, SWT.None);
		parentComposite.setToolTipText("Export Data");
		GridData parentCompositeData = new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 3);
		parentCompositeData.heightHint = 390;
		parentCompositeData.widthHint = 650;
		parentCompositeData.grabExcessHorizontalSpace = true;
		parentCompositeData.grabExcessVerticalSpace = true;
		parentComposite.setLayout(new GridLayout(3, false));
		parentComposite.setLayoutData(parentCompositeData);
		
		Group generalGroup = new Group(parentComposite, SWT.None);
		generalGroup.setText("General");
		
		GridLayout generalGroupLayout = new GridLayout(4, true);
		generalGroupLayout.verticalSpacing = 0;
		generalGroupLayout.marginWidth = 0;
		generalGroupLayout.marginHeight = 0;
		generalGroupLayout.horizontalSpacing = 0;
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 3);
		gridData.heightHint =110;
		gridData.widthHint = 580;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		generalGroup.setLayoutData(gridData);
		generalGroup.setLayout(generalGroupLayout);
		
		
		memoryFieldEditor =new IntegerFieldEditor(PreferenceConstants.VIEW_DATA_FILE_SIZE, " File Size (MB)", generalGroup, 6);
		memoryFieldEditor.setErrorMessage(null);
		memoryFieldEditor.setFocus();
		memoryFieldEditor.getTextControl(generalGroup).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String value = ((Text)event.getSource()).getText();
				validationForIntegerField(value, memoryFieldEditor, Messages.FILE_INTEGER_FIELD_VALIDATION);
				validationForIntegerValue(value, memoryFieldEditor, Messages.File_FIELD_NUMERIC_VALUE_ACCPECTED);
			}
		});
		memoryFieldEditor.getTextControl(generalGroup).addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) { }
			@Override
			public void focusGained(FocusEvent event) {
				String value = ((Text)event.getSource()).getText();
				validationForIntegerField(value, memoryFieldEditor, Messages.FILE_INTEGER_FIELD_VALIDATION);
				validationForIntegerValue(value, memoryFieldEditor, Messages.File_FIELD_NUMERIC_VALUE_ACCPECTED);
			}
		});
		memoryFieldEditor.setPreferenceStore(getPreferenceStore());
		memoryFieldEditor.load();
		
		Button unusedButton0 = new Button(generalGroup, SWT.None);
		unusedButton0.setText("unused");
		unusedButton0.setBounds(0, 0, 20, 10);
		unusedButton0.setVisible(false);
		
		pageSizeEditor = new IntegerFieldEditor(PreferenceConstants.VIEW_DATA_PAGE_SIZE, " Page Size", generalGroup, 6);
		pageSizeEditor.setErrorMessage(null);
		pageSizeEditor.setFocus();
		pageSizeEditor.setPreferenceStore(getPreferenceStore());
		pageSizeEditor.load();
		
		Button unusedButton1 = new Button(generalGroup, SWT.None);
		unusedButton1.setText("unused");
		unusedButton1.setBounds(0, 0, 20, 10);
		unusedButton1.setVisible(false);
		pageSizeEditor.getTextControl(generalGroup).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String value = ((Text)event.getSource()).getText();
				validationForIntegerField(value, pageSizeEditor, Messages.PAGE_INTEGER_FIELD_VALIDATION);
				validationForIntegerValue(value, pageSizeEditor, Messages.PAGE_FIELD_NUMERIC_VALUE_ACCPECTED);
			}
		});
		pageSizeEditor.getTextControl(generalGroup).addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) { }
			@Override
			public void focusGained(FocusEvent event) {
				String value = ((Text)event.getSource()).getText();
				validationForIntegerField(value, pageSizeEditor, Messages.PAGE_INTEGER_FIELD_VALIDATION);
				validationForIntegerValue(value, pageSizeEditor, Messages.PAGE_FIELD_NUMERIC_VALUE_ACCPECTED);
			}
		});
		tempPathFieldEditor = new DirectoryFieldEditor(PreferenceConstants.VIEW_DATA_TEMP_FILEPATH, " Local Temp Path", generalGroup);
		tempPathFieldEditor.setPreferenceStore(getPreferenceStore());
		tempPathFieldEditor.setFocus();
		tempPathFieldEditor.setErrorMessage(null);
		tempPathFieldEditor.load();

		
		Composite compositeInGeneral = new Composite(generalGroup, SWT.None);
		GridData gdcompositeNote = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gdcompositeNote.heightHint = 16;
		gdcompositeNote.widthHint = 500;
		compositeInGeneral.setLayoutData(gdcompositeNote);
		Label lblNote = new Label(compositeInGeneral, SWT.WRAP);
		lblNote.setBounds(4, 0, 40, 20);
		lblNote.setText("Note : ");
		FontData fontData = lblNote.getFont().getFontData()[0];
		Font font = new Font(compositeInGeneral.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		lblNote.setFont(font);
		Label lblmsg = new Label(compositeInGeneral, SWT.WRAP);
		lblmsg.setBounds(44, 0, 308, 18);
		lblmsg.setText(Messages.MEMORY_OVERFLOW_EXCEPTION);
		
	 
		Group grpExportData = new Group(parentComposite, SWT.NONE);
		grpExportData.setText("Export Data");
		
		GridLayout grpExportLayout = new GridLayout();
		grpExportLayout.verticalSpacing = 0;
		grpExportLayout.marginWidth = 0;
		grpExportLayout.marginHeight = 0;
		grpExportLayout.horizontalSpacing = 0;
		GridData gd_grpExportData = new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 3);
		gd_grpExportData.widthHint = 580;
		gd_grpExportData.heightHint = 130;
		gd_grpExportData.grabExcessHorizontalSpace = true;
		gd_grpExportData.grabExcessVerticalSpace = true;
		
		grpExportData.setLayoutData(gd_grpExportData);
		grpExportData.setLayout(grpExportLayout);
		
		
		delimiterEditor = new StringFieldEditor(PreferenceConstants.DELIMITER, " Delimiter", grpExportData);
		delimiterEditor.setErrorMessage(null);
		delimiterEditor.setFocus();
		delimiterEditor.getTextControl(grpExportData).addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) { }
			@Override
			public void focusGained(FocusEvent event) {
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
			}
		});
		delimiterEditor.setPreferenceStore(getPreferenceStore());
		delimiterEditor.load();
		
		Button unusedButton2 = new Button(grpExportData, SWT.None);
		unusedButton2.setText("unused");
		unusedButton2.setVisible(false);
		
		quoteEditor = new StringFieldEditor(PreferenceConstants.QUOTE_CHARACTOR, " Quote Character", grpExportData);
		quoteEditor.setErrorMessage(null);
		quoteEditor.setFocus();
		quoteEditor.getTextControl(grpExportData).addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
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
			}
		});
		quoteEditor.getTextControl(grpExportData).addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) { }
			@Override
			public void focusGained(FocusEvent event) {
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
			}
		});
		
		delimiterEditor.getTextControl(grpExportData).addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
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
		
		new Label(grpExportData, SWT.None).setText(" Include Header ");
		Composite headerComposite = new Composite(grpExportData, SWT.None);
		headerComposite.setBounds(0, 0, 20, 16);
		
		Composite com = new Composite(grpExportData, SWT.None);
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd.heightHint = 20;
		gd.widthHint = 600;
		com.setLayoutData(gd);
		Label lb = new Label(com, SWT.None);
		lb.setBounds(4, 4, 38, 16);
		lb.setText("Note : ");
		FontData messageFontData = lblNote.getFont().getFontData()[0];
		Font messageFont = new Font(compositeInGeneral.getDisplay(), new FontData(messageFontData.getName(), messageFontData.getHeight(), SWT.BOLD));
		lb.setFont(messageFont);
		Label labelMsg = new Label(com, SWT.None);
		labelMsg.setBounds(42, 4, 500, 16);
		labelMsg.setText(Messages.WARNING_MESSAGE);
		
		Group grpServiceDetails = new Group(parentComposite, SWT.NONE);
		GridLayout gl_grpServiceDetails = new GridLayout(1, true);
		GridData gd_grpServiceDetailsData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_grpServiceDetailsData.widthHint = 580;
		gd_grpServiceDetailsData.heightHint = 34;
		grpServiceDetails.setLayoutData(gd_grpServiceDetailsData);
		grpServiceDetails.setLayout(gl_grpServiceDetails);
		grpServiceDetails.setText("Service Details");
		
		Composite grpServiceDetailsCmposite = new Composite(grpServiceDetails, SWT.None);
		GridData serviceGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		serviceGridData.heightHint = 26;
		serviceGridData.widthHint = 530;
		grpServiceDetailsCmposite.setLayout(new GridLayout());
		grpServiceDetailsCmposite.setLayoutData(serviceGridData);

		portNo = new IntegerFieldEditor(PreferenceConstants.PORT_NO, "Port No               ", grpServiceDetailsCmposite, 4);
		portNo.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				String value = event.getNewValue().toString();
				validationForIntegerField(value,portNo,Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		portNo.getTextControl(grpServiceDetailsCmposite).addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) { }
			@Override
			public void focusGained(FocusEvent event) {
				String value = ((Text)event.getSource()).getText();
				validationForIntegerField(value,portNo,Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		portNo.setPreferenceStore(getPreferenceStore());
		portNo.load();
		 
		booleanFieldEditor = new BooleanFieldEditor(PreferenceConstants.INCLUDE_HEADER, "", SWT.DEFAULT, headerComposite);
		getPreferenceStore().setDefault(PreferenceConstants.INCLUDE_HEADER, true);
		booleanFieldEditor.setPreferenceStore(getPreferenceStore());
		booleanFieldEditor.setPreferenceStore(getPreferenceStore());
		booleanFieldEditor.load();
		
		Button unusedButton4 = new Button(parentComposite, SWT.None);
		unusedButton4.setText("unused");
		unusedButton4.setVisible(false);
		
		new Label(parentComposite, SWT.None).setText("   Purge View Data Files ");
		Composite purgeComposite = new Composite(parentComposite, SWT.None);
		GridData purgeCompositeData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		purgeCompositeData.heightHint = 18;
		purgeComposite.setLayoutData(purgeCompositeData);
		 
		purgeEditor = new BooleanFieldEditor(PreferenceConstants.PURGE_DATA_FILES, "", purgeComposite);
		getPreferenceStore().setDefault(PreferenceConstants.PURGE_DATA_FILES, true);
		purgeEditor.setPreferenceStore(getPreferenceStore());
		purgeEditor.load();
		
		addFields(memoryFieldEditor);
		addFields(delimiterEditor);
		addFields(pageSizeEditor);
		addFields(quoteEditor);
		
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
