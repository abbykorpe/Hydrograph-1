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


package hydrograph.ui.perspective.dialog;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


import hydrograph.ui.perspective.Messages;

/**
 * Dialog class for getting JDK path from user.
 * 
 * @author Bitwise
 *
 */
public class JdkPathDialog extends Dialog {
	private Text text;
	private Button okButton;
	private String inputValue;
	private Label errorLbl;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public JdkPathDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		container.getShell().setText(Messages.JDK_PATH_DIALOG_TITLE);
		
		Composite mainComposite = new Composite(container, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label title = new Label(mainComposite, SWT.NONE);
		title.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		title.setText(Messages.JDK_PATH_DIALOG_MESSAGE);
		
//		Composite txtBoxComposite = new Composite(mainComposite, SWT.NONE);
//		txtBoxComposite.setLayout(new GridLayout(1, false));
//		txtBoxComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
//		txtBoxComposite.setBounds(0, 0, 64, 64);
		
		createTextBox(mainComposite);
		
		Label noteMessage = new Label(mainComposite, SWT.WRAP);
		noteMessage.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		noteMessage.setText(Messages.JDK_PATH_DIALOG_NOTE);

		return container;
	}

	private void createTextBox(Composite mainComposite) {
		text = new Text(mainComposite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 0, 0));
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (PreStartActivity.isValidJDKPath(getTextBoxValue(),false)) {
						errorLbl.setVisible(false);
						okButton.setEnabled(true);
					} else {
						errorLbl.setVisible(true);
						okButton.setEnabled(false);
					}
		}});
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayout(new GridLayout(3,false));
		parent.setLayoutData(new GridData(SWT.FILL,SWT.BOTTOM,true,true));
		createErrorLabel(parent);
	okButton = createButton(parent, IDialogConstants.OK_ID, "Save and &Restart", true);
	okButton.setEnabled(false);
	createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	private void createErrorLabel(Composite parent) {
		Composite composite=new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3,false));
		composite.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		errorLbl = new Label(composite, SWT.NONE);
		errorLbl.setForeground(new Color(null, 250,0,0));
		errorLbl.setText("Invalid JDK  bin path");
		errorLbl.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		String textValue = getTextBoxValue();
		inputValue = textValue;
		super.okPressed();
	}

	private String getTextBoxValue() {
		String textValue = text.getText();
		if (!StringUtils.endsWithIgnoreCase(textValue, "\\bin")) {
			textValue = textValue + "\\bin";
		}
		return textValue;
	}
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 209);
	}

	/**
	 * Returns JDK path give by user.
	 * 
	 */
	public String getInputVlue() {
		return inputValue;
	}
}

