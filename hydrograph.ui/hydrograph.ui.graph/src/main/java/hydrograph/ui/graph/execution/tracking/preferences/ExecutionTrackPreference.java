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
package hydrograph.ui.graph.execution.tracking.preferences;

/**
 * The Class ExecutionTrackPreference.
 * @author Bitwise
 */
import java.util.ArrayList;
import java.util.List;

import hydrograph.ui.common.util.PreferenceConstants;
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.graph.Activator;
import hydrograph.ui.graph.execution.tracking.handlers.ExecutionTrackingConsoleHandler;
import hydrograph.ui.graph.execution.tracking.utils.TrackingDisplayUtils;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;

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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

public class ExecutionTrackPreference extends PreferencePage implements IWorkbenchPreferencePage{

	private DirectoryFieldEditor logPathEditor;
	private BooleanFieldEditor trackingFieldEditor;
	private IntegerFieldEditor localPortFieldEditor;
	private Group generalGroup;
	private IntegerFieldEditor remotePortFieldEditor;
	private BooleanFieldEditor useRemoteConfigBooleanFieldEditor;
	private Button useRemoteConfigbutton;
	private StringFieldEditor remoteHostFieldEditor;
	private Label useRemoteConfigLabel;
	private List<FieldEditor> editorList;
	
	public ExecutionTrackPreference() {
		super();
		setPreferenceStore(PlatformUI.getPreferenceStore());
	}
	
	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		final Composite parentComposite = new Composite(parent, SWT.None);
		GridData parentCompositeData = new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 3);
		parentCompositeData.heightHint = 480;
		parentCompositeData.widthHint = 662;
		parentCompositeData.grabExcessHorizontalSpace = true;
		parentCompositeData.grabExcessVerticalSpace = true;
		parentComposite.setLayout(new GridLayout(3, false));
		parentComposite.setLayoutData(parentCompositeData);
		
		generalGroup = new Group(parentComposite, SWT.None);
		generalGroup.setText(hydrograph.ui.graph.Messages.EXECUTION_TRACKING_GROUP_LABEL);
		
		GridLayout generalGroupLayout = new GridLayout(4, true);
		generalGroupLayout.verticalSpacing = 0;
		generalGroupLayout.marginWidth = 2;
		generalGroupLayout.marginHeight = 0;
		generalGroupLayout.horizontalSpacing = 0;
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 3);
		gridData.heightHint =185;
		gridData.widthHint = 580;
		gridData.horizontalSpan = 3;
		gridData.horizontalIndent=5;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		generalGroup.setLayoutData(gridData);
		generalGroup.setLayout(generalGroupLayout);
		
		new Label(generalGroup, SWT.None).setText(SWT.SPACE +hydrograph.ui.graph.Messages.ENABLE_TRACKING_LABEL);
		Composite headerComposite = new Composite(generalGroup, SWT.None);
		headerComposite.setBounds(0, 0, 20, 16);
		
		trackingFieldEditor = new BooleanFieldEditor(ExecutionPreferenceConstants.EXECUTION_TRACKING, "", headerComposite);
		getPreferenceStore().setDefault(ExecutionPreferenceConstants.EXECUTION_TRACKING, true);
		trackingFieldEditor.setPreferenceStore(getPreferenceStore());
		trackingFieldEditor.load();
		
		addEmptySpace();
		
		
		localPortFieldEditor = new IntegerFieldEditor(ExecutionPreferenceConstants.LOCAL_TRACKING_PORT_NO, SWT.SPACE + Messages.LOCAL_PORT_NO_LABEL, generalGroup, 6);
		localPortFieldEditor.setPreferenceStore(getPreferenceStore());
		localPortFieldEditor.load();
		localPortFieldEditor.setErrorMessage(null);
		localPortFieldEditor.getTextControl(generalGroup).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String value = ((Text)event.getSource()).getText();
				validationForIntegerField(value, localPortFieldEditor, Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		
		addEmptySpace();

		remotePortFieldEditor = new IntegerFieldEditor(ExecutionPreferenceConstants.REMOTE_TRACKING_PORT_NO,  SWT.SPACE + Messages.REMOTE_PORT_NO_LABEL, generalGroup, 6);
		remotePortFieldEditor.setPreferenceStore(getPreferenceStore());
		remotePortFieldEditor.load();
		remotePortFieldEditor.setErrorMessage(null);
		remotePortFieldEditor.getTextControl(generalGroup).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String value = ((Text)event.getSource()).getText();
				validationForIntegerField(value, remotePortFieldEditor, Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		
		addEmptySpace();
		
		useRemoteConfigLabel=new Label(generalGroup, SWT.None);
		useRemoteConfigLabel.setText(SWT.SPACE +Messages.OVERRIDE_REMOTE_HOST_LABEL);
		Composite headerRemoteComposite = new Composite(generalGroup, SWT.None);
		headerComposite.setBounds(0, 0, 20, 16);
		
		useRemoteConfigBooleanFieldEditor = new BooleanFieldEditor(ExecutionPreferenceConstants.USE_REMOTE_CONFIGURATION, "", SWT.DEFAULT, headerRemoteComposite);
		useRemoteConfigbutton = (Button) useRemoteConfigBooleanFieldEditor.getDescriptionControl(headerRemoteComposite);
		getPreferenceStore().setDefault(PreferenceConstants.USE_REMOTE_CONFIGURATION, false);
		useRemoteConfigBooleanFieldEditor.setPreferenceStore(getPreferenceStore());
		useRemoteConfigBooleanFieldEditor.load();
		
		addEmptySpace();
		
		remoteHostFieldEditor = new StringFieldEditor(ExecutionPreferenceConstants.REMOTE_TRACKING_HOST,  SWT.SPACE + Messages.REMOTE_HOST_NAME_LABEL, generalGroup);
		remoteHostFieldEditor.setPreferenceStore(getPreferenceStore());
		remoteHostFieldEditor.load();
		remoteHostFieldEditor.setErrorMessage(null);
		remoteHostFieldEditor.getTextControl(generalGroup).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validateRemoteHost();
			}
		});
		remoteHostFieldEditor.setPreferenceStore(getPreferenceStore());
		remoteHostFieldEditor.load();
		remoteHostFieldEditor.setErrorMessage(null);
		
		addListenerToRemoteConfigBooleanEditor();
		addEmptySpace();
		
		logPathEditor = new DirectoryFieldEditor(ExecutionPreferenceConstants.TRACKING_LOG_PATH, SWT.SPACE + Messages.TRACKING_LOG_PATH_LABEL, generalGroup);
		logPathEditor.setPreferenceStore(getPreferenceStore());
		logPathEditor.load();
		
		if(trackingFieldEditor.getBooleanValue()){
			enableOrDisableEditors(true);
		}else{
			enableOrDisableEditors(false);
		}
		
		trackingFieldEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(!trackingFieldEditor.getBooleanValue()){
					enableOrDisableEditors(false);
					setEnableExecutionTrackConsole(false);
				}else{
					enableOrDisableEditors(true);
					setEnableExecutionTrackConsole(true);
				}
			}
		});
	
		addFields(localPortFieldEditor);
		addFields(remotePortFieldEditor);
		addFields(remoteHostFieldEditor);
		return null;
	}

	private void enableOrDisableEditors(boolean enable) {
		logPathEditor.setEnabled(enable, generalGroup);
		localPortFieldEditor.setEnabled(enable, generalGroup);
		remotePortFieldEditor.setEnabled(enable, generalGroup);
		useRemoteConfigbutton.setEnabled(enable);
		useRemoteConfigLabel.setEnabled(enable);
		enableOrDisableRemoteHost();
	}

	private void addEmptySpace() {
		Button unusedButton = new Button(generalGroup, SWT.None);
		unusedButton.setBounds(0, 0, 20, 10);
		unusedButton.setVisible(false);
	}

	private void setEnableExecutionTrackConsole(boolean enable){
		ExecutionTrackingConsoleHandler  consoleHandler=(ExecutionTrackingConsoleHandler) RunStopButtonCommunicator.ExecutionTrackingConsole.getHandler();
		if(consoleHandler!= null){
			consoleHandler.setExecutionTrackingConsoleEnabled(enable);
		}
	}
	
	private void validationForIntegerField(String value, IntegerFieldEditor editor, String message){
		if(StringUtils.isBlank(value) || !value.matches("\\d+") || Integer.parseInt(value) < 1){
			setErrorMessage(message);
			editor.setErrorMessage(message);
			setValid(false);
		}else{
			setErrorMessage(null);
			editor.setErrorMessage("");
			setValid(true);
		}
		checkState();
	}
		
	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.setDefault(ExecutionPreferenceConstants.EXECUTION_TRACKING, true);
		preferenceStore.setDefault(ExecutionPreferenceConstants.TRACKING_LOG_PATH, TrackingDisplayUtils.INSTANCE.getInstallationPath());
		preferenceStore.setDefault(ExecutionPreferenceConstants.LOCAL_TRACKING_PORT_NO, TrackingDisplayUtils.INSTANCE.getExecutiontrackingPortNo());
		preferenceStore.setDefault(ExecutionPreferenceConstants.REMOTE_TRACKING_PORT_NO, TrackingDisplayUtils.INSTANCE.getExecutiontrackingPortNo());
		setPreferenceStore(preferenceStore);
	}
	
	@Override
	protected void performDefaults() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		trackingFieldEditor.loadDefault();
		logPathEditor.setStringValue(preferenceStore.getDefaultString(ExecutionPreferenceConstants.TRACKING_LOG_PATH));
		localPortFieldEditor.setStringValue(preferenceStore.getDefaultString(ExecutionPreferenceConstants.LOCAL_TRACKING_PORT_NO));
		remotePortFieldEditor.setStringValue(preferenceStore.getDefaultString(ExecutionPreferenceConstants.REMOTE_TRACKING_PORT_NO));
		useRemoteConfigBooleanFieldEditor.loadDefault();
		enableOrDisableEditors(true);
		validateRemoteHost();
	}
	
	@Override
	public boolean performOk() {
		trackingFieldEditor.store();
		logPathEditor.store();
		localPortFieldEditor.store();
		remotePortFieldEditor.store();
		useRemoteConfigBooleanFieldEditor.store();
		remoteHostFieldEditor.store();
		return super.performOk();
	}
	
	@Override
	protected void performApply() {
		trackingFieldEditor.store();
		logPathEditor.store();
		localPortFieldEditor.store();
		remotePortFieldEditor.store();
		useRemoteConfigBooleanFieldEditor.store();
		remoteHostFieldEditor.store();
	}
	
	private void validateRemoteHost() {
		if(useRemoteConfigbutton.getSelection() && StringUtils.isEmpty(remoteHostFieldEditor.getStringValue())){
			remoteHostFieldEditor.setErrorMessage(Messages.BLANK_REMOTE_HOST_NAME_ERROR);
		}else{
			setErrorMessage(null);
			remoteHostFieldEditor.setErrorMessage("");
			setValid(true);
		}
		checkState();
	}
	
	private void addListenerToRemoteConfigBooleanEditor() {
		enableOrDisableRemoteHost();
		
		useRemoteConfigbutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableOrDisableRemoteHost();
				validateRemoteHost();
			}
		});
	}

	private void enableOrDisableRemoteHost() {
		
		if(!useRemoteConfigbutton.getSelection() || !trackingFieldEditor.getBooleanValue()){
			remoteHostFieldEditor.setEnabled(false, generalGroup);
		}else{
			remoteHostFieldEditor.setEnabled(true,  generalGroup);
		}
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
}