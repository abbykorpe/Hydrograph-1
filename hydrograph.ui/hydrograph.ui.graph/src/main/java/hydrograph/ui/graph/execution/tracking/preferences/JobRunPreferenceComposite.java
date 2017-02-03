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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import hydrograph.ui.common.swt.customwidget.HydroGroup;
import hydrograph.ui.graph.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;

/**
 * @author Bitwise This class created the preference page for job run.
 *
 */
public class JobRunPreferenceComposite extends Composite {

	private Button btnRadioButtonAlways;
	private Button btnRadioButtonPrompt;

	JobRunPreferenceComposite(Composite parent, int none, String selection) {
		super(parent, none);
		setLayout(new GridLayout(1, false));

		HydroGroup hydroGroup = new HydroGroup(this, SWT.NONE);
		hydroGroup.setHydroGroupText(Messages.SAVE_JOBS_BEFORE_LAUNCHING_MESSAGE);
		GridData gd_hydroGroup = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		hydroGroup.setLayout(new GridLayout(1, false));
		hydroGroup.setLayoutData(gd_hydroGroup);
		hydroGroup.getHydroGroupClientArea().setLayout(new GridLayout(2, false));
		hydroGroup.getHydroGroupClientArea().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		btnRadioButtonAlways = new Button(hydroGroup.getHydroGroupClientArea(), SWT.RADIO);
		btnRadioButtonAlways.setText(StringUtils.capitalize((MessageDialogWithToggle.ALWAYS)));

		btnRadioButtonPrompt = new Button(hydroGroup.getHydroGroupClientArea(), SWT.RADIO);
		btnRadioButtonPrompt.setText(StringUtils.capitalize(MessageDialogWithToggle.PROMPT));

		if (StringUtils.equals(selection, MessageDialogWithToggle.ALWAYS)) {
			btnRadioButtonAlways.setSelection(true);
		} else {
			btnRadioButtonPrompt.setSelection(true);
		}
	}

	/**
	 * @return selection of radio button
	 */
	public boolean getAlwaysButtonSelection() {
		return btnRadioButtonAlways.getSelection();
	}

	/**
	 * Set defaults values of job run preference
	 */
	public void storeDefaults() {
		btnRadioButtonPrompt.setSelection(true);
		btnRadioButtonAlways.setSelection(false);
	}
}
