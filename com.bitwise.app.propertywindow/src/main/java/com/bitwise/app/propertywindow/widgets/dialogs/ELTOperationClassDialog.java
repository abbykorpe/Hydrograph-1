package com.bitwise.app.propertywindow.widgets.dialogs;



import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;

/**
 * The Class ELTOperationClassDialog.
 * 
 * @author Bitwise
 */
public class ELTOperationClassDialog extends Dialog {

	private Text fileName;
	private Combo operationClasses;
	private Button btnCheckButton; 
	private Button applyButton;
	private Composite container;
	private OperationClassProperty operationClassProperty;
	private PropertyDialogButtonBar eltOperationClassDialogButtonBar;
	private TootlTipErrorMessage tootlTipErrorMessage = new TootlTipErrorMessage();
	private WidgetConfig widgetConfig; 
	private String componentName;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param operationClassProperty 
	 * @param widgetConfig 
	 * @param componentName 
	 */
	public ELTOperationClassDialog(Shell parentShell,PropertyDialogButtonBar propertyDialogButtonBar, OperationClassProperty operationClassProperty, WidgetConfig widgetConfig, String componentName) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		this.operationClassProperty = operationClassProperty;
		this.widgetConfig = widgetConfig;
		this.componentName=componentName;
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
				comboOfOperationClasses, isParameterCheckbox, fileNameText, tootlTipErrorMessage, widgetConfig);
		fileName=(Text)fileNameText.getSWTWidgetControl();
		operationClasses = (Combo) comboOfOperationClasses.getSWTWidgetControl();

		FilterOperationClassUtility.enableAndDisableButtons(true);
		FilterOperationClassUtility.setComponentName(componentName);
		btnCheckButton=(Button) isParameterCheckbox.getSWTWidgetControl();
		populateWidget();
		return container;
	}

	/**
	 * Populate widget.
	 */
    public void populateWidget() {
        if (!operationClassProperty.getOperationClassPath().equalsIgnoreCase("")) {
			operationClasses.setText(operationClassProperty.getComboBoxValue());
              fileName.setText(operationClassProperty.getOperationClassPath());
              btnCheckButton.setSelection(operationClassProperty.isParameter());
              fileName.setData("path", operationClassProperty.getOperationClassFullPath());
			FilterOperationClassUtility.enableAndDisableButtons(false);
			if(!operationClassProperty.getComboBoxValue().equalsIgnoreCase(Messages.CUSTOM))
			fileName.setEnabled(false);
        }
        else {
			operationClasses.select(0);
		}
  }

	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button okButton=createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		Button cancelButton=createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		
		createApplyButton(parent);	
		
		
		eltOperationClassDialogButtonBar.setPropertyDialogButtonBar(okButton, applyButton, cancelButton);
	}
	
	private void createApplyButton(Composite parent) {
		applyButton = createButton(parent, IDialogConstants.NO_ID,
				"Apply", false);
		disableApplyButton();
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
		// TODO Auto-generated method stub
		
		if(applyButton.isEnabled()){
			ConfirmCancelMessageBox confirmCancelMessageBox = new ConfirmCancelMessageBox(container);
			MessageBox confirmCancleMessagebox = confirmCancelMessageBox.getMessageBox();

			if(confirmCancleMessagebox.open() == SWT.OK){
				super.close();
			}
		}else{
			super.close();
		}
	}

	@Override
	protected void okPressed() {
		operationClassProperty = new OperationClassProperty(operationClasses.getText(), fileName.getText(),
				btnCheckButton.getSelection(), (String) fileName.getData("path"));
		super.okPressed();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == 3){
			operationClassProperty = new OperationClassProperty(operationClasses.getText(), fileName.getText(),
					btnCheckButton.getSelection(), (String) fileName.getData("path"));
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
}
