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

package hydrograph.ui.dataviewer.preferances;



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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ViewDataPreferencesDialog extends Dialog {
	private Text delimiterTextBox;
	private Text quoteCharactorTextBox;
	private Button includeHeardersCheckBox;
	private ViewDataPreferences viewDataPreferences;
	private static final String WARNING="Warning";
	private static final String WARNING_MESSAGE="Exported file might not open in Excel due to change in default delimiter and quote character.";
	private static final String ERROR_MESSAGE="Delimiter and quote character should not be same.";
	private static final String SINGLE_CHARACTOR_ERROR_MESSAGE="Only single charactor is allowed.";
	private static final String DEFAULT_DELIMITER=",";
	private static final String DEFAULT_QUOTE_CHARACTOR="\"";
	private Label message;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ViewDataPreferencesDialog(Shell parentShell) {
		super(parentShell);

	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText("Export Data Preferences");
		container.setLayout(new GridLayout(2, false));
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,2));
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(22, 22, 55, 15);
		lblNewLabel.setText("Delimiter");
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setBounds(22, 61, 94, 15);
		lblNewLabel_1.setText("Quote Character");
		delimiterTextBox = new Text(composite, SWT.BORDER);
		delimiterTextBox.setBounds(122, 19, 86, 21);
		quoteCharactorTextBox = new Text(composite, SWT.BORDER);
		quoteCharactorTextBox.setBounds(122, 58, 86, 21);
		includeHeardersCheckBox = new Button(composite, SWT.CHECK);
		includeHeardersCheckBox.setBounds(306, 21, 109, 16);
		includeHeardersCheckBox.setText("Include Headers");
		delimiterTextBox.setText(viewDataPreferences.getDelimiter());
		quoteCharactorTextBox.setText(viewDataPreferences.getQuoteCharactor());
		includeHeardersCheckBox.setSelection(viewDataPreferences.getIncludeHeaders());
		
		message = new Label(composite, SWT.NONE);
		message.setBounds(10, 94, 492, 16);
		message.setText(WARNING_MESSAGE);
		
		
		message.setVisible(false);
		
		if(!delimiterTextBox.getText().equalsIgnoreCase(",")|| !quoteCharactorTextBox.getText().equalsIgnoreCase("\""))
		{
			message.setVisible(true);
		}
		
		
		delimiterTextBox.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String delimiterTextBoxValue=((Text)e.widget).getText();
				if(!delimiterTextBoxValue.isEmpty())
				{
					if(delimiterTextBoxValue.length()!=1)
					{
							message.setText(SINGLE_CHARACTOR_ERROR_MESSAGE);
							message.setForeground(new Color(Display.getDefault(), 255, 0, 0));
							message.setVisible(true);
							getButton(0).setEnabled(false);
					}
					else
					{
						getButton(0).setEnabled(true);
					if(!delimiterTextBoxValue.equalsIgnoreCase(",") && !delimiterTextBoxValue.equalsIgnoreCase(quoteCharactorTextBox.getText()))
					{
						message.setText(WARNING_MESSAGE);
						message.setForeground(new Color(Display.getDefault(),0,0,0));
						message.setVisible(true);
					}
					else
					{
						if(delimiterTextBoxValue.equalsIgnoreCase(quoteCharactorTextBox.getText()))
						{
							message.setText(ERROR_MESSAGE);
							message.setForeground(new Color(Display.getDefault(), 255, 0, 0));
							message.setVisible(true);
							getButton(0).setEnabled(false);
						}
						else
						{
							message.setVisible(false);
							getButton(0).setEnabled(true);
						}
					}
					
					}
				}
				else
				{
					message.setVisible(false);
					getButton(0).setEnabled(true);
				}
				
				
			}
			
		});
		
	quoteCharactorTextBox.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String quoteCharactorTextBoxValue=((Text)e.widget).getText();
				
				if(!quoteCharactorTextBoxValue.isEmpty())
				{
					if(quoteCharactorTextBoxValue.length()!=1)
					{
						message.setText(SINGLE_CHARACTOR_ERROR_MESSAGE);
						message.setForeground(new Color(Display.getDefault(), 255, 0, 0));
						message.setVisible(true);
						getButton(0).setEnabled(false);
					}
					else
					{
					if(!quoteCharactorTextBoxValue.equalsIgnoreCase("\"") && !quoteCharactorTextBoxValue.equalsIgnoreCase(delimiterTextBox.getText()))
					{
						message.setText(WARNING_MESSAGE);
						message.setForeground(new Color(Display.getDefault(),0,0,0));
						message.setVisible(true);
					}
					else
					{
						if(quoteCharactorTextBoxValue.equalsIgnoreCase(delimiterTextBox.getText()))
						{
							message.setText(ERROR_MESSAGE);
							message.setForeground(new Color(Display.getDefault(), 255, 0, 0));
							message.setVisible(true);
							getButton(0).setEnabled(false);
						}
						else
						{
							message.setVisible(false);
							getButton(0).setEnabled(true);
						}
					}
					
					}
				}
				else
				{
					message.setVisible(false);
					getButton(0).setEnabled(true);
				}
				
				
			}
			
		});
		

	
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,true);
		createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(520, 200);
	}

	@Override
	protected void okPressed() {
	
	
		viewDataPreferences.setDelimiter(delimiterTextBox.getText());
		viewDataPreferences.setQuoteCharactor(quoteCharactorTextBox.getText());
		viewDataPreferences.setIncludeHeaders(includeHeardersCheckBox.getSelection());
		super.okPressed();
	}





	public void setViewDataPreferences(ViewDataPreferences viewDataPreferences) {
		this.viewDataPreferences = viewDataPreferences;
	}

	public ViewDataPreferences getViewDataPreferences() {
		return viewDataPreferences;
	}
}
