/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package hydrograph.ui.graph.execution.tracking.preferences;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import hydrograph.ui.common.swt.customwidget.HydroGroup;
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.graph.execution.tracking.handlers.ExecutionTrackingConsoleHandler;
import hydrograph.ui.graph.job.RunStopButtonCommunicator;

/**
 * @author Bitwise
 * The class creates the preference page for Execution Tracking.
 *
 */
public class ExecutionTrackingPreferanceComposite extends Composite {
	private static final String ERROR_KEY = "ERROR";
	private Text localPortNoText;
	private Text remotePortNoText;
	private Text remoteHostNameText;
	private Text trackingLogPathText;
	private PreferencePage executionTrackPreference;
	private Button enableTrackingCheckBox;
	private Button overrideRemoteHostCheckBox;
	private Label localPortNoLabel;
	private Label remotePortNo;
	private Label overrideRemoteHostLabel;
	private Label remoteHostNameLabel;
	private Label trackingLogPathLabel;
	private ExecutionTrackingPreferencesDataStructure storePrefernces;
	private List<Text> editorList = new ArrayList<Text>();

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @param executionTrackPreference
	 * @param enableExeTrac
	 */
	public ExecutionTrackingPreferanceComposite(Composite parent, int style, PreferencePage executionTrackPreference,
			ExecutionTrackingPreferencesDataStructure prefernce) {
		super(parent, style);
		this.executionTrackPreference = executionTrackPreference;
		setLayout(new GridLayout(1, false));
		storePrefernces = new ExecutionTrackingPreferencesDataStructure();

		HydroGroup hydroGroup = new HydroGroup(this, SWT.NONE);
		hydroGroup.setHydroGroupText(hydrograph.ui.graph.Messages.EXECUTION_TRACKING_GROUP_LABEL);
		hydroGroup.setLayout(new GridLayout(1, false));
		hydroGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		hydroGroup.getHydroGroupClientArea().setLayout(new GridLayout(2, false));
		hydroGroup.getHydroGroupClientArea().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Label enableTrackingLabel = new Label(hydroGroup.getHydroGroupClientArea(), SWT.NONE);
		enableTrackingLabel.setText(hydrograph.ui.graph.Messages.ENABLE_TRACKING_LABEL);

		enableTrackingCheckBox = new Button(hydroGroup.getHydroGroupClientArea(), SWT.CHECK);
		enableTrackingCheckBox.setSelection(prefernce.isEnableTrackingCheckBox());


		localPortNoLabel = new Label(hydroGroup.getHydroGroupClientArea(), SWT.NONE);
		localPortNoLabel.setText(Messages.LOCAL_PORT_NO_LABEL);

		localPortNoText = new Text(hydroGroup.getHydroGroupClientArea(), SWT.BORDER);
		localPortNoText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		localPortNoText.setText(prefernce.getLocalPortNoText());
		localPortNoText.setData(ERROR_KEY, null);
		localPortNoText.setTextLimit(6);

		remotePortNo = new Label(hydroGroup.getHydroGroupClientArea(), SWT.NONE);
		remotePortNo.setText(Messages.REMOTE_PORT_NO_LABEL);

		remotePortNoText = new Text(hydroGroup.getHydroGroupClientArea(), SWT.BORDER);
		remotePortNoText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		remotePortNoText.setText(prefernce.getRemotePortNoText());
		remotePortNoText.setData(ERROR_KEY, null);
		remotePortNoText.setTextLimit(6);

		overrideRemoteHostLabel = new Label(hydroGroup.getHydroGroupClientArea(), SWT.NONE);
		overrideRemoteHostLabel.setText(Messages.OVERRIDE_REMOTE_HOST_LABEL);

		overrideRemoteHostCheckBox = new Button(hydroGroup.getHydroGroupClientArea(), SWT.CHECK);
		overrideRemoteHostCheckBox.setSelection(prefernce.isOverrideRemoteHostButton());

		remoteHostNameLabel = new Label(hydroGroup.getHydroGroupClientArea(), SWT.NONE);
		remoteHostNameLabel.setText(Messages.REMOTE_HOST_NAME_LABEL);

		remoteHostNameText = new Text(hydroGroup.getHydroGroupClientArea(), SWT.BORDER);
		remoteHostNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		remoteHostNameText.setText(prefernce.getRemoteHostNameText());
		if (!prefernce.isOverrideRemoteHostButton()) {
			remoteHostNameText.setEnabled(false);
		}
		remoteHostNameText.setData(ERROR_KEY, null);

		trackingLogPathLabel = new Label(hydroGroup.getHydroGroupClientArea(), SWT.NONE);
		trackingLogPathLabel.setText(Messages.TRACKING_LOG_PATH_LABEL);

		trackingLogPathText = new Text(hydroGroup.getHydroGroupClientArea(), SWT.BORDER);
		trackingLogPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		trackingLogPathText.setText(prefernce.getTrackingLogPathText());
		trackingLogPathText.setData(ERROR_KEY, null);

		trackingLogPathText.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent event) {
					validationForTextField(trackingLogPathText , Messages.BLANK_TRACKING_LOG_PATH_ERROR );
			}
		});
		
		
		enableOrDisableFields(enableTrackingCheckBox.getSelection());
		localPortNoText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validationForIntegerField(localPortNoText, localPortNoText.getText(), Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		
		enableTrackingCheckBox.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Button btn = (Button) event.getSource();
				enableOrDisableFields(btn.getSelection());
				setEnableExecutionTrackConsole(btn.getSelection());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		overrideRemoteHostCheckBox.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Button btn = (Button) event.getSource();
				remoteHostNameText.setEnabled(btn.getSelection());
				remoteHostNameLabel.setEnabled(btn.getSelection());
				if (!btn.getSelection()) {
					remoteHostNameText.setData(ERROR_KEY, null);
					checkState();
				} else {
					if (StringUtils.isBlank(remoteHostNameText.getText())) {
						remoteHostNameText.setData(ERROR_KEY, Messages.BLANK_REMOTE_HOST_NAME_ERROR);
						checkState();
					} else {
						remoteHostNameText.setData(ERROR_KEY, null);
						checkState();
					}
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		remotePortNoText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String value = ((Text) e.getSource()).getText();
				validationForIntegerField(remotePortNoText, value, Messages.PORTNO_FIELD_VALIDATION);
			}
		});
		
		remoteHostNameText.addModifyListener(new ModifyListener() {
			@Override
		public void modifyText(ModifyEvent e) {
				validationForTextField(remoteHostNameText,  Messages.BLANK_REMOTE_HOST_NAME_ERROR );
			}
		});
		editorList.add(localPortNoText);
		editorList.add(remotePortNoText);
		editorList.add(remoteHostNameText);
		editorList.add(trackingLogPathText);
	}

	/**
	 * Enable/disable all preference
	 * @param enable
	 */
	private void enableOrDisableFields(boolean enable) {
		trackingLogPathText.setEnabled(enable);
		localPortNoText.setEnabled(enable);
		remotePortNoText.setEnabled(enable);
		overrideRemoteHostCheckBox.setEnabled(enable);
		remoteHostNameText.setEnabled(enable);
		localPortNoLabel.setEnabled(enable);
		remotePortNo.setEnabled(enable);
		overrideRemoteHostLabel.setEnabled(enable);
		remoteHostNameLabel.setEnabled(enable);
		trackingLogPathLabel.setEnabled(enable);
		enableOrDisableRemoteHost();
	}

	/**
	 * Enable/Disable remote host name section
	 */
	public void enableOrDisableRemoteHost() {
		if (!enableTrackingCheckBox.getSelection() || !overrideRemoteHostCheckBox.getSelection()) {
			remoteHostNameText.setEnabled(false);
			remoteHostNameLabel.setEnabled(false);
		} else {
			remoteHostNameText.setEnabled(true);
			remoteHostNameLabel.setEnabled(true);
		}
	}

	/**
	 * Data sturcture to store all preferences
	 * @return
	 */
	public ExecutionTrackingPreferencesDataStructure getPreferences() {
		storePrefernces.setEnableTrackingCheckBox(enableTrackingCheckBox.getSelection());
		storePrefernces.setLocalPortNoText(localPortNoText.getText());
		storePrefernces.setRemotePortNoText(remotePortNoText.getText());
		storePrefernces.setOverrideRemoteHostButton(overrideRemoteHostCheckBox.getSelection());
		storePrefernces.setRemoteHostNameText(remoteHostNameText.getText());
		storePrefernces.setTrackingLogPathText(trackingLogPathText.getText());
		return storePrefernces;
	}

	/**
	 * Enable/disable ExecutionTrackingConsole icon
	 * @param enable
	 */
	private void setEnableExecutionTrackConsole(boolean enable) {
		ExecutionTrackingConsoleHandler consoleHandler = (ExecutionTrackingConsoleHandler) RunStopButtonCommunicator.ExecutionTrackingConsole
				.getHandler();
		if (consoleHandler != null) {
			consoleHandler.setExecutionTrackingConsoleEnabled(enable);
		}
	}

	/**
	 * set Default values for all fields
	 * @param defaultPreference
	 */
	public void setDefaults(ExecutionTrackingPreferencesDataStructure defaultPreference) {
		enableTrackingCheckBox.setSelection(defaultPreference.isEnableTrackingCheckBox());
		localPortNoText.setText(defaultPreference.getLocalPortNoText());
		remotePortNoText.setText(defaultPreference.getRemotePortNoText());
		overrideRemoteHostCheckBox.setSelection(defaultPreference.isOverrideRemoteHostButton());
		overrideRemoteHostCheckBox.setSelection(false);
		remoteHostNameText.setEnabled(false);
		trackingLogPathText.setText(defaultPreference.getTrackingLogPathText());

	}

	/**
	 * Check if text box contains any alphabets
	 * @param textBox
	 * @param value
	 * @param message
	 */
	private void validationForIntegerField(Text textBox, String value, String message) {
		if (StringUtils.isBlank(value) || !value.matches("\\d+") || Integer.parseInt(value) < 1) {
			textBox.setData(ERROR_KEY, message);
			executionTrackPreference.setValid(false);
		} else {
			textBox.setData(ERROR_KEY, null);
			executionTrackPreference.setValid(true);
		}
		checkState();
	}

	/**
	 * Check if text box is blank
	 * @param textBox
	 * @param value
	 * @param message
	 */
	protected void validationForTextField(Text textBox, String message) {
		if (StringUtils.isBlank(textBox.getText())) {
			textBox.setData(ERROR_KEY, message);
			executionTrackPreference.setValid(false);
		} else {
			textBox.setData(ERROR_KEY, null);
			executionTrackPreference.setValid(true);
		}
		checkState();
	}

	/**
	 * Checks if all values are correct and enable/disable ok button
	 */
	private void checkState() {
		if (editorList != null) {
			int size = editorList.size();
			for (int i = 0; i < size; i++) {
				Text fieldEditor = editorList.get(i);
				String errorMessage = (String) fieldEditor.getData(ERROR_KEY);
				if (StringUtils.isNotBlank(errorMessage)) {
					executionTrackPreference.setErrorMessage(errorMessage);
					executionTrackPreference.setValid(false);
					break;
				} else {
					executionTrackPreference.setErrorMessage(null);
					executionTrackPreference.setValid(true);
				}
			}
		}
	}

}
