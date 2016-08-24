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
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.graph.Activator;
import hydrograph.ui.graph.execution.tracking.handlers.ExecutionTrackingConsoleHandler;
import hydrograph.ui.graph.execution.tracking.utils.TrackingDisplayUtils;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
	private IntegerFieldEditor portFieldEditor;
	private Composite composite_2;
	private Composite composite_3;
	
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
		parentCompositeData.heightHint = 300;
		parentCompositeData.widthHint = 620;
		parentCompositeData.grabExcessHorizontalSpace = true;
		parentCompositeData.grabExcessVerticalSpace = true;
		parentComposite.setLayout(new GridLayout(3, false));
		parentComposite.setLayoutData(parentCompositeData);
		
		Group generalGroup = new Group(parentComposite, SWT.None);
		generalGroup.setText("Execution Tracking");
		
		GridLayout generalGroupLayout = new GridLayout(2, true);
		generalGroupLayout.verticalSpacing = 0;
		generalGroupLayout.marginWidth = 0;
		generalGroupLayout.marginHeight = 0;
		generalGroupLayout.horizontalSpacing = 0;
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true, 2, 2);
		gridData.heightHint = 90;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		generalGroup.setLayoutData(gridData);
		generalGroup.setLayout(generalGroupLayout);
		
		
		Composite composite = new Composite(generalGroup, SWT.None);
		composite.setBounds(50, 0, 100, 16);
		
		trackingFieldEditor = new BooleanFieldEditor(ExecutionPreferenceConstants.EXECUTION_TRACKING, " Tracking ", composite);
		getPreferenceStore().setDefault(ExecutionPreferenceConstants.EXECUTION_TRACKING, true);
		trackingFieldEditor.setPreferenceStore(getPreferenceStore());
		trackingFieldEditor.load();
		
		Composite composite_1 = new Composite(generalGroup, SWT.None);
		GridData gd_headerComp = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_headerComp.heightHint = 30;
		gd_headerComp.widthHint = 640;
		composite_1.setLayoutData(gd_headerComp);
		composite_1.setVisible(false);
		
		composite_2 = new Composite(generalGroup, SWT.None);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_2.widthHint = 600;
		gd_composite_2.heightHint = 30;
		composite_2.setLayoutData(gd_composite_2);
		new Label(generalGroup, SWT.NONE);
		
		logPathEditor = new DirectoryFieldEditor(ExecutionPreferenceConstants.TRACKING_LOG_PATH, "Tracking Log path ", composite_2);
		logPathEditor.setPreferenceStore(getPreferenceStore());
		logPathEditor.load();
		
		composite_3 = new Composite(generalGroup, SWT.None);
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_3.widthHint = 500;
		composite_3.setLayoutData(gd_composite_3);
		
		portFieldEditor = new IntegerFieldEditor(ExecutionPreferenceConstants.TRACKING_PORT_NO, "Port No                   ", composite_3, 6);
		portFieldEditor.setPreferenceStore(getPreferenceStore());
		portFieldEditor.load();
		portFieldEditor.getTextControl(composite_3).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				String value = ((Text)event.getSource()).getText();
				validationForIntegerField(value, portFieldEditor, Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		
		if(trackingFieldEditor.getBooleanValue()){
			logPathEditor.setEnabled(true, composite_2);
			portFieldEditor.setEnabled(true, composite_3);
		}else{
			logPathEditor.setEnabled(false, composite_2);
			portFieldEditor.setEnabled(false, composite_3);
		}
		
		trackingFieldEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(!trackingFieldEditor.getBooleanValue()){
					logPathEditor.setEnabled(false, composite_2);
					portFieldEditor.setEnabled(false, composite_3);
					setEnableExecutionTrackConsole(false);
				}else{
					logPathEditor.setEnabled(true, composite_2);
					portFieldEditor.setEnabled(true, composite_3);
					setEnableExecutionTrackConsole(true);
				}
			}
		});
		
		return null;
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
	}
		
	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.setDefault(ExecutionPreferenceConstants.EXECUTION_TRACKING, true);
		preferenceStore.setDefault(ExecutionPreferenceConstants.TRACKING_LOG_PATH, TrackingDisplayUtils.INSTANCE.getInstallationPath());
		preferenceStore.setDefault(ExecutionPreferenceConstants.TRACKING_PORT_NO, TrackingDisplayUtils.INSTANCE.getExecutiontrackingPortNo());
		
		setPreferenceStore(preferenceStore);
	}
	
	@Override
	protected void performDefaults() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		trackingFieldEditor.loadDefault();
		logPathEditor.setEnabled(true, composite_2);
		portFieldEditor.setEnabled(true, composite_3);
		logPathEditor.setStringValue(preferenceStore.getDefaultString(ExecutionPreferenceConstants.TRACKING_LOG_PATH));
		portFieldEditor.setStringValue(preferenceStore.getDefaultString(ExecutionPreferenceConstants.TRACKING_PORT_NO));
	}
	
	@Override
	public boolean performOk() {
		trackingFieldEditor.store();
		logPathEditor.store();
		portFieldEditor.store();
		return super.performOk();
	}
	
	@Override
	protected void performApply() {
		trackingFieldEditor.store();
		logPathEditor.store();
		portFieldEditor.store();
	}
}
