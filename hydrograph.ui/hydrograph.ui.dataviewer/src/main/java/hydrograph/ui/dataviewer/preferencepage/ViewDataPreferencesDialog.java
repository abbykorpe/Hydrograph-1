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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This class is responsible for making dialog of view data preferences
 * 
 * @author Bitwise
 *
 */
public class ViewDataPreferencesDialog extends Dialog {
	private Label warningLabel;
	private Text delimiterTextBox;
	private Text quoteCharactorTextBox;
	private Button includeHeardersCheckBox;
	private Text fileSizeTextBox;
	private Text pageSizeTextBox;
	private ViewDataPreferences viewDataPreferences;
	private static final String NUMERIC_VALUE_ACCPECTED = "Only integer values are allowed.";
	private static final String FILE_SIZE_BLANK = "File  size  should  not  be  blank.";
	private static final String PAGE_SIZE_BLANK = "Page  size  should  not  be  blank.";
	private static final String WARNING_MESSAGE = " Exported file might not open in Excel due to change in default  delimiter and     \n quote character.";
	private static final String ERROR_MESSAGE = "Delimiter and quote character should not be same.";
	private static final String SINGLE_CHARACTOR_ERROR_MESSAGE = "Only single charactor or hex value is allowed.";
	private static final String MEMORY_OVERFLOW_EXCEPTION = "Page size greater than 5000 may cause memory overflow.";
	private ControlDecoration pageSizeIntegerDecorator;
	private ControlDecoration fileSizeIntegerDecorator;
	private ControlDecoration fileSizeEmptyDecorator;
	private ControlDecoration pageSizeEmptyDecorator;
	private ControlDecoration delimiterDuplicateDecorator;
	private ControlDecoration delimiterSingleCharactorDecorator;
	private ControlDecoration quoteSingleCharactorDecorator;
	private ControlDecoration quoteCharactorDuplicateDecorator;
	private String REGEX = "[\\d]*";
	
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
		container.getShell().setText("View Data Preferences");
		container.setLayout(new GridLayout(2, false));
		Composite composite = new Composite(container, SWT.BORDER);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2);
		gd_composite.widthHint = 601;
		composite.setLayoutData(gd_composite);
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(22, 22, 55, 15);
		lblNewLabel.setText("Delimiter");
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setBounds(22, 61, 94, 15);
		lblNewLabel_1.setText("Quote Character");
		delimiterTextBox = new Text(composite, SWT.BORDER);
		delimiterTextBox.setBounds(132, 19, 86, 21);
		quoteCharactorTextBox = new Text(composite, SWT.BORDER);
		quoteCharactorTextBox.setBounds(132, 58, 86, 21);
		includeHeardersCheckBox = new Button(composite, SWT.CHECK);
		includeHeardersCheckBox.setBounds(493, 21, 109, 16);
		includeHeardersCheckBox.setText("Include Headers");
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setBounds(264, 22, 76, 15);
		lblNewLabel_2.setText("File Size (MB)");

		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
		lblNewLabel_3.setBounds(264, 61, 55, 15);
		lblNewLabel_3.setText("Page Size");

		fileSizeTextBox = new Text(composite, SWT.BORDER);
		fileSizeTextBox.setBounds(350, 22, 76, 21);

		pageSizeTextBox = new Text(composite, SWT.BORDER);
		pageSizeTextBox.setBounds(349, 58, 76, 21);
		delimiterTextBox.setText(viewDataPreferences.getDelimiter());
		quoteCharactorTextBox.setText(viewDataPreferences.getQuoteCharactor());
		includeHeardersCheckBox.setSelection(viewDataPreferences.getIncludeHeaders());
		fileSizeTextBox.setText(Integer.toString(viewDataPreferences.getFileSize()));
		pageSizeTextBox.setText(Integer.toString(viewDataPreferences.getPageSize()));
		fileSizeIntegerDecorator = addDecorator(fileSizeTextBox, NUMERIC_VALUE_ACCPECTED);
		pageSizeIntegerDecorator = addDecorator(pageSizeTextBox, NUMERIC_VALUE_ACCPECTED);
		fileSizeEmptyDecorator = addDecorator(fileSizeTextBox, FILE_SIZE_BLANK);
		pageSizeEmptyDecorator = addDecorator(pageSizeTextBox, PAGE_SIZE_BLANK);
		delimiterDuplicateDecorator = addDecorator(delimiterTextBox, ERROR_MESSAGE);
		delimiterSingleCharactorDecorator = addDecorator(delimiterTextBox, SINGLE_CHARACTOR_ERROR_MESSAGE);
		quoteSingleCharactorDecorator = addDecorator(quoteCharactorTextBox, SINGLE_CHARACTOR_ERROR_MESSAGE);
		quoteCharactorDuplicateDecorator = addDecorator(quoteCharactorTextBox, ERROR_MESSAGE);
		pageSizeIntegerDecorator.hide();
		fileSizeIntegerDecorator.hide();
		fileSizeEmptyDecorator.hide();
		pageSizeEmptyDecorator.hide();
		delimiterDuplicateDecorator.hide();
		delimiterSingleCharactorDecorator.hide();
		quoteSingleCharactorDecorator.hide();
		quoteCharactorDuplicateDecorator.hide();

		delimiterTextBox.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				validateForSingleAndDuplicateCharacter(e, quoteCharactorTextBox.getText(),
						delimiterSingleCharactorDecorator, delimiterDuplicateDecorator);

			}
		});

		quoteCharactorTextBox.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				validateForSingleAndDuplicateCharacter(e, delimiterTextBox.getText(), quoteSingleCharactorDecorator,
						quoteCharactorDuplicateDecorator);

			}

		});
		delimiterTextBox.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				validateDelimiterAndQuoteCharactorProperty(delimiterTextBox.getText(), quoteCharactorTextBox.getText(),
						delimiterSingleCharactorDecorator, delimiterDuplicateDecorator);
			}

			@Override
			public void focusGained(FocusEvent e) {

				enableAndDisableOkButtonIfAnyDecoratorIsVisible();

			}
		});

		quoteCharactorTextBox.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				validateDelimiterAndQuoteCharactorProperty(delimiterTextBox.getText(), quoteCharactorTextBox.getText(),
						delimiterSingleCharactorDecorator, delimiterDuplicateDecorator);
			}

			@Override
			public void focusGained(FocusEvent e) {

				enableAndDisableOkButtonIfAnyDecoratorIsVisible();

			}
		});
		attachListnersToSizeTextBox();
		return container;
	}
	
	
	private void validateForSingleAndDuplicateCharacter(VerifyEvent e, String textBoxValue,
			ControlDecoration singleCharactorDecorator, ControlDecoration duplicateDecorator) {
		singleCharactorDecorator.hide();
		duplicateDecorator.hide();
		String value = ((Text) e.widget).getText();
		String currentValue = (value.substring(0, e.start) + e.text + value.substring(e.end));
		if (StringUtils.isNotEmpty(currentValue)) {
			validateDelimiterAndQuoteCharactorProperty(currentValue, textBoxValue, singleCharactorDecorator,
					duplicateDecorator);
		} else {
			enableAndDisableOkButtonIfAnyDecoratorIsVisible();
			warningLabel.setText(WARNING_MESSAGE);
			warningLabel.setVisible(true);
		}
	}
	
	



	private void attachListnersToSizeTextBox() {
		fileSizeTextBox.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				isTextBoxEmpty(e, fileSizeEmptyDecorator);
			}
		});

		fileSizeTextBox.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent event) {
				validateTextBoxValue(event, fileSizeIntegerDecorator);
			}
		});

		fileSizeTextBox.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				fileSizeIntegerDecorator.hide();
				disableOkButtonWhenTextBoxIsEmpty(fileSizeTextBox);
			}

			@Override
			public void focusGained(FocusEvent e) {
				enableAndDisableOkButtonIfAnyDecoratorIsVisible();
			}
		});
		

		pageSizeTextBox.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				pageSizeIntegerDecorator.hide();
				disableOkButtonWhenTextBoxIsEmpty(pageSizeTextBox);
			}

			@Override
			public void focusGained(FocusEvent e) {
				enableAndDisableOkButtonIfAnyDecoratorIsVisible();
			}
		});
		
		pageSizeTextBox.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent event) {
				validateTextBoxValue(event, pageSizeIntegerDecorator);
			}
		});
		pageSizeTextBox.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String pageSizeValue = ((Text) e.widget).getText();
				if (!pageSizeValue.isEmpty()) {
					if (Integer.parseInt(pageSizeValue) > 5000) {
						warningLabel.setText(MEMORY_OVERFLOW_EXCEPTION);
						warningLabel.setVisible(true);
					} else {
						warningLabel.setVisible(false);
					}
				}
				isTextBoxEmpty(e, pageSizeEmptyDecorator);
			}
		});
	}
	
	private void disableOkButtonWhenTextBoxIsEmpty(Text textBox) {
		if (textBox.getText().isEmpty()) {
			getButton(0).setEnabled(false);
		} else {
			getButton(0).setEnabled(true);
		}
	}

	
	
	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		warningLabel = new Label(parent, SWT.NONE);
		warningLabel.setText(WARNING_MESSAGE);
		warningLabel.setVisible(false);
		if (!delimiterTextBox.getText().equalsIgnoreCase(",")
				|| !quoteCharactorTextBox.getText().equalsIgnoreCase("\"")) {
			warningLabel.setVisible(true);
		}
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(637, 180);
	}

	@Override
	protected void okPressed() {
		viewDataPreferences.setDelimiter(delimiterTextBox.getText());
		viewDataPreferences.setQuoteCharactor(quoteCharactorTextBox.getText());
		viewDataPreferences.setIncludeHeaders(includeHeardersCheckBox.getSelection());
		viewDataPreferences.setFileSize(Integer.parseInt(fileSizeTextBox.getText()));
		viewDataPreferences.setPageSize(Integer.parseInt(pageSizeTextBox.getText()));
		super.okPressed();
	}




	private boolean validateDelimiterAndQuoteCharactorProperty(String textBoxValue, String textBoxValue2,
			ControlDecoration singleCharactorDecorator, ControlDecoration duplicateDecorator) {
		if (StringUtils.length(ConvertHexValues.parseHex(textBoxValue)) == 1) {
			if (!fileSizeEmptyDecorator.isVisible() || !pageSizeEmptyDecorator.isVisible()) {
				getButton(0).setEnabled(true);
			}
			if (!(textBoxValue.equalsIgnoreCase(",") || textBoxValue.equalsIgnoreCase("\""))
					&& !textBoxValue.equalsIgnoreCase(textBoxValue2)) {
				warningLabel.setText(WARNING_MESSAGE);
				warningLabel.setVisible(true);
				hideDelimiterAndQuoteCharactorDecorator();
				if (textBoxValue2.length() > 1) {
					getButton(0).setEnabled(false);
				}
				return false;
			} else {
				if (textBoxValue.equalsIgnoreCase(textBoxValue2)) {
					duplicateDecorator.show();
					getButton(0).setEnabled(false);
					return false;
				} else {
					showWarningMessage(textBoxValue, textBoxValue2);
					duplicateDecorator.hide();
					enableAndDisableOkButtonIfAnyDecoratorIsVisible();
					return true;
				}
			}
		} else {
			if (!textBoxValue.isEmpty()) {
				singleCharactorDecorator.show();
				getButton(0).setEnabled(false);
			}
			return false;
		}
	}

	private void showWarningMessage(String textBoxValue, String textBoxValue2) {
		if ((textBoxValue.equalsIgnoreCase(",") || textBoxValue.equalsIgnoreCase("\""))
				&& (textBoxValue2.equalsIgnoreCase("\"") || textBoxValue2.equalsIgnoreCase(","))) {
			warningLabel.setVisible(false);
		} else {
			warningLabel.setVisible(true);
		}
	}

	private void hideDelimiterAndQuoteCharactorDecorator() {
		if (delimiterDuplicateDecorator.isVisible()) {
			delimiterDuplicateDecorator.hide();
		}
		if (quoteCharactorDuplicateDecorator.isVisible()) {
			quoteCharactorDuplicateDecorator.hide();
		}
	}



	private void enableAndDisableOkButtonIfAnyDecoratorIsVisible() {
		if (quoteCharactorDuplicateDecorator.isVisible() || quoteSingleCharactorDecorator.isVisible()
				|| fileSizeEmptyDecorator.isVisible() || pageSizeEmptyDecorator.isVisible()
				|| delimiterDuplicateDecorator.isVisible() || delimiterSingleCharactorDecorator.isVisible()) {
			getButton(0).setEnabled(false);
		} else {
			getButton(0).setEnabled(true);
		}
	}
	
	public void setViewDataPreferences(ViewDataPreferences viewDataPreferences) {
		this.viewDataPreferences = viewDataPreferences;
	}

	public ViewDataPreferences getViewDataPreferences() {
		return viewDataPreferences;
	}

	private ControlDecoration addDecorator(Control control, String message) {
		ControlDecoration txtDecorator = new ControlDecoration(control, SWT.LEFT);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_ERROR);
		Image img = fieldDecoration.getImage();
		txtDecorator.setImage(img);
		txtDecorator.setDescriptionText(message);
		txtDecorator.setMarginWidth(3);
		return txtDecorator;
	}
	
	private void validateTextBoxValue(VerifyEvent event, ControlDecoration integerDecorator) {
		integerDecorator.hide();
		String string = event.text;
		Matcher matchs = Pattern.compile(REGEX).matcher(string);
		if (!matchs.matches()) {
			integerDecorator.show();
			event.doit = false;
		} else {
			integerDecorator.hide();
		}
	}

	private void isTextBoxEmpty(ModifyEvent e, ControlDecoration emptyDecorator) {
		emptyDecorator.hide();
		String fileSize = ((Text) e.widget).getText();
		if (!fileSize.isEmpty()) {
			emptyDecorator.hide();
			enableAndDisableOkButtonIfAnyDecoratorIsVisible();

		} else {
			getButton(0).setEnabled(false);
			emptyDecorator.show();
		}
	}

}
