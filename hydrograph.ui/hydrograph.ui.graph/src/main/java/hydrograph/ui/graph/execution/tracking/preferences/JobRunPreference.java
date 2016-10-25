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

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.dataviewer.constants.PreferenceConstants;
import hydrograph.ui.graph.Activator;
import hydrograph.ui.graph.Messages;

import org.apache.commons.lang.StringUtils;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.internal.ui.preferences.DebugPreferencesMessages;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * This class adds Job Run preference in preference window.
 * 
 * @author Bitwise
 *
 */
public class JobRunPreference extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{
	
	public static final String JOB_RUN_PREFRENCE="job_run_prefrence";
	private RadioGroupFieldEditor edit;
	private StringFieldEditor hostFieldEditor;
	
	public JobRunPreference() {
		super();
		setPreferenceStore(PlatformUI.getPreferenceStore());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		getPreferenceStore().setDefault(JOB_RUN_PREFRENCE, MessageDialogWithToggle.PROMPT);
		loadHostNameValue();
	}

	private String loadHostNameValue() {
		String value = PlatformUI.getPreferenceStore().getString(Constants.HOST);
		if (StringUtils.isNotBlank(value)) {
			getPreferenceStore().setValue(Constants.HOST, value);
			getPreferenceStore().setDefault(Constants.HOST,value); 
		} else {
			getPreferenceStore().setValue(Constants.HOST, "");
			getPreferenceStore().setDefault(Constants.HOST, "");
		}
		return value;
	}

	private void loadSavePromptValue() {
		String value=Activator.getDefault().getPreferenceStore().getString(JobRunPreference.JOB_RUN_PREFRENCE);
		if(StringUtils.equals(MessageDialogWithToggle.ALWAYS, value)){
			getPreferenceStore().setValue(JOB_RUN_PREFRENCE,value);
		}else{
			getPreferenceStore().setValue(JOB_RUN_PREFRENCE,MessageDialogWithToggle.PROMPT);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		createHostNameFieldEditor(parent); 

		Composite comp = SWTFactory.createComposite(parent, 1, 1, GridData.FILL_HORIZONTAL);
		edit = new RadioGroupFieldEditor(JOB_RUN_PREFRENCE, Messages.SAVE_JOBS_BEFORE_LAUNCHING_MESSAGE, 3,  
				 new String[][] {{DebugPreferencesMessages.LaunchingPreferencePage_3, MessageDialogWithToggle.ALWAYS}, 
				 {DebugPreferencesMessages.LaunchingPreferencePage_5, MessageDialogWithToggle.PROMPT}}, 
				 comp,
				 true);
		addField(hostFieldEditor);
		addField(edit);
		initialize();
		return comp;
	}

	private void createHostNameFieldEditor(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.RUN_CONFIG_PREFRENCE_TITLE, 2, 0, GridData.FILL_HORIZONTAL);
		Composite composite = new Composite(group, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		hostFieldEditor = new StringFieldEditor(PreferenceConstants.HOST, "Host        ", composite);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		boolean returnCode= super.performOk();
		Activator.getDefault().getPreferenceStore().setValue(JOB_RUN_PREFRENCE, getPreferenceStore().getString(JOB_RUN_PREFRENCE));
		Activator.getDefault().getPreferenceStore().setValue(Constants.HOST, getPreferenceStore().getString(Constants.HOST));
		return returnCode; 
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
	}

}
