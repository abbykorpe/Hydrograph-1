package com.bitwise.app.propertywindow.widgets.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.datastructures.tooltip.TootlTipErrorMessage;
import com.bitwise.app.propertywindow.messagebox.ConfirmCancelMessageBox;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget.ValidationStatus;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultCheckBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;

// TODO: Auto-generated Javadoc
/**
 * The Class ELTOperationClassDialog.
 * 
 * @author Bitwise
 */
public class ELTOperationClassDialog extends Dialog {

	private Text fileName;
	private Button btnCheckButton; 
	private Button applyButton;
	private Composite container;
	private OperationClassProperty operationClassProperty;
	private PropertyDialogButtonBar eltOperationClassDialogButtonBar;
	private ValidationStatus validationStatus;
	private TootlTipErrorMessage tootlTipErrorMessage = new TootlTipErrorMessage();
	

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param operationClassProperty 
	 */
	public ELTOperationClassDialog(Shell parentShell,PropertyDialogButtonBar propertyDialogButtonBar, OperationClassProperty operationClassProperty) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		this.operationClassProperty = operationClassProperty;
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
				if(containerBox.height >= 185) {
                	container.getShell().setBounds(containerBox.x, containerBox.y, containerBox.width, 185);
                }
				
				cld_composite.heightHint = container.getBounds().height - 10;
			}
			
		});
		
		AbstractELTWidget fileNameText = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(150);
		AbstractELTWidget isParameterCheckbox = new ELTDefaultCheckBox("Is Parameter").checkBoxLableWidth(100);
		
		FilterOperationClassUtility.createOperationalClass(composite, eltOperationClassDialogButtonBar, fileNameText, isParameterCheckbox, validationStatus,tootlTipErrorMessage);
		fileName=(Text)fileNameText.getSWTWidgetControl();
		btnCheckButton=(Button) isParameterCheckbox.getSWTWidgetControl();
		populateWidget();
		return container;
	}

	/**
	 * Populate widget.
	 */
    public void populateWidget() {
        if (!operationClassProperty.getOperationClassPath().equalsIgnoreCase("")) {
              fileName.setText(operationClassProperty.getOperationClassPath());
              btnCheckButton.setSelection(operationClassProperty.isParameter());
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
		return new Point(450, 183);
	}

	private void setPropertyDialogSize() {
		container.getShell().setMinimumSize(450, 185);
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
		
		//super.cancelPressed();
	}

	@Override
	protected void okPressed() {
        operationClassProperty = new OperationClassProperty(fileName.getText(), btnCheckButton.getSelection());
		super.okPressed();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == 3){
			operationClassProperty = new OperationClassProperty(fileName.getText(), btnCheckButton.getSelection());
			applyButton.setEnabled(false);
		}else{
			super.buttonPressed(buttonId);
		}
	}
	
	public OperationClassProperty getOperationClassProperty() {
		OperationClassProperty operationClassProperty = new OperationClassProperty(this.operationClassProperty.getOperationClassPath(),this.operationClassProperty.isParameter());
		return operationClassProperty;
	}

	public void setValidationStatus(ValidationStatus validationStatus) {
		this.validationStatus = validationStatus;
	}
	
	public ValidationStatus getValidationStatus() {
		return validationStatus;
	}
	
	public String getTootlTipErrorMessage() {
		return tootlTipErrorMessage.getErrorMessage();
	}
}
