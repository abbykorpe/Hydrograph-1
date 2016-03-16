package com.bitwise.app.propertywindow.widgets.dialogs;



import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;

import com.bitwise.app.common.component.config.Operations;
import com.bitwise.app.common.component.config.TypeInfo;
import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.datastructures.tooltip.TootlTipErrorMessage;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.messagebox.ConfirmCancelMessageBox;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultCheckBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultCombo;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import com.bitwise.app.propertywindow.widgets.interfaces.IOperationClassDialog;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * The Class ELTOperationClassDialog.
 * 
 * @author Bitwise
 */
public class ELTOperationClassDialog extends Dialog implements IOperationClassDialog{

	private Text fileName;
	private Combo operationClasses;
	private Button isParameterCheckBox; 
	private Button applyButton;
	private Button okButton;
	private Composite container;
	private OperationClassProperty operationClassProperty;
	private PropertyDialogButtonBar eltOperationClassDialogButtonBar;
	private TootlTipErrorMessage tootlTipErrorMessage = new TootlTipErrorMessage();
	private WidgetConfig widgetConfig; 
	private String componentName;
	private ControlDecoration alphanumericDecorator;
	private ControlDecoration emptyDecorator;
	private ControlDecoration parameterDecorator;
	private boolean isOkPressed=false;
	private boolean isCancelPressed=false;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private PropertyDialogButtonBar opeartionClassDialogButtonBar;
	private Button cancelButton;
	
	private boolean closeDialog;
	private boolean okPressed;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param operationClassProperty 
	 * @param widgetConfig 
	 * @param componentName 
	 */
	public ELTOperationClassDialog(Shell parentShell,PropertyDialogButtonBar propertyDialogButtonBar, OperationClassProperty operationClassProperty, WidgetConfig widgetConfig, String componentName) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		this.operationClassProperty = operationClassProperty;
		this.widgetConfig = widgetConfig;
		this.componentName=componentName;
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		opeartionClassDialogButtonBar = new PropertyDialogButtonBar(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	public Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		ColumnLayout cl_container = new ColumnLayout();
		cl_container.maxNumColumns = 1;
		container.setLayout(cl_container);
		
		container.getShell().setText("Operation Class");
		
		setPropertyDialogSize();
		
		eltOperationClassDialogButtonBar = new PropertyDialogButtonBar(container);
		
		
		Composite composite = new Composite(container, SWT.BORDER);
		ColumnLayout cl_composite = new ColumnLayout();
		cl_composite.maxNumColumns = 1;
		composite.setLayout(cl_composite);
		final ColumnLayoutData cld_composite = new ColumnLayoutData();
		cld_composite.heightHint = 82;
		composite.setLayoutData(cld_composite);
		
		
		container.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {				
               
				Rectangle containerBox = container.getShell().getBounds();
				if(containerBox.height >= 210) {
                	container.getShell().setBounds(containerBox.x, containerBox.y, containerBox.width, 210);
                }
				
				cld_composite.heightHint = container.getBounds().height - 10;
			}
			
		});
		AbstractELTWidget fileNameText = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(150);

		AbstractELTWidget isParameterCheckbox = new ELTDefaultCheckBox(Constants.IS_PARAMETER).checkBoxLableWidth(100);

		Operations operations = XMLConfigUtil.INSTANCE.getComponent(componentName).getOperations();
		List<TypeInfo> typeInfos = operations.getStdOperation();
		String optionsOfComboOfOperationClasses[] = new String[typeInfos.size() + 1];
		optionsOfComboOfOperationClasses[0] =Messages.CUSTOM;
		for (int i = 0; i < typeInfos.size(); i++) {
			optionsOfComboOfOperationClasses[i + 1] = typeInfos.get(i).getName();
		}
		AbstractELTWidget comboOfOperationClasses = new ELTDefaultCombo().defaultText(optionsOfComboOfOperationClasses)
				.comboBoxWidth(90);

		
		FilterOperationClassUtility.createOperationalClass(composite, eltOperationClassDialogButtonBar,
				comboOfOperationClasses, isParameterCheckbox, fileNameText, tootlTipErrorMessage, widgetConfig,this,propertyDialogButtonBar,opeartionClassDialogButtonBar);
		fileName=(Text)fileNameText.getSWTWidgetControl();
		operationClasses = (Combo) comboOfOperationClasses.getSWTWidgetControl();

		FilterOperationClassUtility.enableAndDisableButtons(true,false);
		FilterOperationClassUtility.setComponentName(componentName);
		isParameterCheckBox=(Button) isParameterCheckbox.getSWTWidgetControl();
		alphanumericDecorator=WidgetUtility.addDecorator(fileName,Messages.CHARACTERSET);
		emptyDecorator=WidgetUtility.addDecorator(fileName,Messages.OperationClassBlank);
		parameterDecorator=WidgetUtility.addDecorator(fileName,Messages.PARAMETER_ERROR);
		populateWidget();
		return container;
	}
	
	public void pressOK(){
		okPressed();
		isOkPressed=true;
	}

	/**
	 * Populate widget.
	 */
    public void populateWidget() {
		if (!operationClassProperty.getOperationClassPath().equalsIgnoreCase("")) {
			fileName.setBackground(new Color(Display.getDefault(), 255, 255, 255));
			emptyDecorator.hide();
			operationClasses.setText(operationClassProperty.getComboBoxValue());
			fileName.setText(operationClassProperty.getOperationClassPath());
			isParameterCheckBox.setSelection(operationClassProperty.isParameter());
			fileName.setData("path", operationClassProperty.getOperationClassFullPath());
			if (!operationClassProperty.getComboBoxValue().equalsIgnoreCase(Messages.CUSTOM)) {
				fileName.setEnabled(false);
				FilterOperationClassUtility.enableAndDisableButtons(false, false);
				isParameterCheckBox.setEnabled(false);
			} else {
				isParameterCheckBox.setEnabled(true);
				if (isParameterCheckBox.getSelection()) {
					FilterOperationClassUtility.enableAndDisableButtons(true, true);
				}
			}
		} else {
			fileName.setBackground(new Color(Display.getDefault(), 255, 255, 204));
			operationClasses.select(0);
			isParameterCheckBox.setEnabled(false);
		}
  }

	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	  okButton=createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	   cancelButton=createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		
		createApplyButton(parent);	
		
		
		eltOperationClassDialogButtonBar.setPropertyDialogButtonBar(okButton, applyButton, cancelButton);
		alphanumericDecorator.hide();
		parameterDecorator.hide();
		isParameterCheckBox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isParameterCheckBox.getSelection()) {
					hasTextBoxAlphanumericCharactorsOnly(fileName.getText());
					if (StringUtils.isNotBlank(fileName.getText()) && !fileName.getText().startsWith("@{")&&!fileName.getText().endsWith("}")) {
						fileName.setText("@{" + fileName.getText() + "}");
						fileName.setBackground(new Color(Display.getDefault(), 255, 255, 255));
					} 
				} else {
					if (StringUtils.isNotBlank(fileName.getText())&&fileName.getText().startsWith("@{")) {
						fileName.setText(fileName.getText().substring(2,fileName.getText().length()-1));
						fileName.setBackground(new Color(Display.getDefault(), 255, 255, 255));
					}
					okButton.setEnabled(true);
					applyButton.setEnabled(true);
					alphanumericDecorator.hide();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		fileName.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				String currentText = ((Text) e.widget).getText();
				String textBoxValue = (currentText.substring(0, e.start) + e.text + currentText.substring(e.end))
						.trim();
				if (isParameterCheckBox.getSelection()) {
					if (StringUtils.isNotBlank(textBoxValue)
							&& (!textBoxValue.startsWith("@{") || !textBoxValue.endsWith("}"))) {
						((Text) e.widget).setBackground(new Color(Display.getDefault(), 255, 255, 255));
						parameterDecorator.show();
						emptyDecorator.hide();
						okButton.setEnabled(false);
						applyButton.setEnabled(false);
					} else {
						((Text) e.widget).setBackground(new Color(Display.getDefault(), 255, 255, 204));
						emptyDecorator.show();
						parameterDecorator.hide();
						okButton.setEnabled(true);
						hasTextBoxAlphanumericCharactorsOnly(textBoxValue);
					}
				}
				else
				{
					if(StringUtils.isNotBlank(textBoxValue))
					{
						((Text) e.widget).setBackground(new Color(Display.getDefault(), 255, 255, 255));
						applyButton.setEnabled(true);
						isParameterCheckBox.setEnabled(true);
						emptyDecorator.hide();
					}
					else
					{
						((Text) e.widget).setBackground(new Color(Display.getDefault(), 255, 255, 204));
						isParameterCheckBox.setEnabled(false);
						emptyDecorator.show();
					}
				}
			}
		});

	}
	
	private void hasTextBoxAlphanumericCharactorsOnly(String textBoxValue) {
		if (StringUtils.isNotBlank(textBoxValue)) {
			fileName.setBackground(new Color(Display.getDefault(), 255, 255, 255));
			emptyDecorator.hide();
			Matcher matchs = Pattern.compile(Constants.REGEX).matcher(textBoxValue);
			if (!matchs.matches()) {
				alphanumericDecorator.show();
				okButton.setEnabled(false);
				applyButton.setEnabled(false);
			} else {
				alphanumericDecorator.hide();
				okButton.setEnabled(true);
				applyButton.setEnabled(true);
			}
		} else {
			fileName.setBackground(new Color(Display.getDefault(), 255, 255, 204));
			emptyDecorator.show();
			alphanumericDecorator.hide();
			okButton.setEnabled(false);
			applyButton.setEnabled(false);
		}
	}
	
	private void createApplyButton(Composite parent) {
		applyButton = createButton(parent, IDialogConstants.NO_ID,
				"Apply", false);
		disableApplyButton();
		opeartionClassDialogButtonBar.setPropertyDialogButtonBar(okButton, applyButton, cancelButton);
	}
	
	private void disableApplyButton() {
		applyButton.setEnabled(false);
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(540, 210);
	}

	private void setPropertyDialogSize() {
		container.getShell().setMinimumSize(540, 210);
	}

	@Override
	protected void cancelPressed() {		
		if(applyButton.isEnabled()){
			
			if(!isCancelPressed){
				ConfirmCancelMessageBox confirmCancelMessageBox = new ConfirmCancelMessageBox(container);
				MessageBox confirmCancleMessagebox = confirmCancelMessageBox.getMessageBox();

				if(confirmCancleMessagebox.open() == SWT.OK){
					closeDialog = super.close();
				}
			}else{
				closeDialog = super.close();
			}
			
			
		}else{
			closeDialog = super.close();
		}
	}

	@Override
	protected void okPressed() {
		operationClassProperty = new OperationClassProperty(operationClasses.getText(), fileName.getText(),
				isParameterCheckBox.getSelection(), (String) fileName.getData("path"));
		okPressed=true;
		super.okPressed();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == 3){
			operationClassProperty = new OperationClassProperty(operationClasses.getText(), fileName.getText(),
					isParameterCheckBox.getSelection(), (String) fileName.getData("path"));
			applyButton.setEnabled(false);
		}else{
			super.buttonPressed(buttonId);
		}
	}
	
	public OperationClassProperty getOperationClassProperty() {
		OperationClassProperty operationClassProperty = new OperationClassProperty(
				this.operationClassProperty.getComboBoxValue(), this.operationClassProperty.getOperationClassPath(),
				this.operationClassProperty.isParameter(), this.operationClassProperty.getOperationClassFullPath());
		return operationClassProperty;
	}

	public String getTootlTipErrorMessage() {
		return tootlTipErrorMessage.getErrorMessage();
	}
	
	/**
	 * 
	 * returns true if ok button pressed from code
	 * 
	 * @return boolean
	 */
	public boolean isOKPressed(){
		return isOkPressed;
	}

	@Override
	public void pressCancel() {
		isCancelPressed=true;
		cancelPressed();
	}
	
	/**
	 * 
	 * returns true if cancel button pressed from code
	 * 
	 * @return boolean
	 */
	public boolean isCancelPressed(){
		return isCancelPressed;
	}
	
	@Override
	public boolean close() {
		if(!okPressed){
			cancelPressed();			
			return closeDialog;
		}else{
			return super.close();
		}		
	}
}
