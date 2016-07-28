package hydrograph.ui.propertywindow.widgets.customwidgets.operational;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;

public class ExpressionOrOperationClassSelectDialog extends Dialog {
    
	public boolean okPressed;
	private boolean optionToSelect;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ExpressionOrOperationClassSelectDialog(Shell parentShell,boolean optionToSelect) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM);
		this.optionToSelect=optionToSelect;
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(4, false));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
	   Button	expression= new Button(container, SWT.RADIO);
		GridData gd_btnRadioButton = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnRadioButton.heightHint = 26;
		expression.setLayoutData(gd_btnRadioButton);
		expression.setText("Expression");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		expression.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				optionToSelect=true;
			}
		});
		Button operation= new Button(container, SWT.RADIO);
		GridData gd_button = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_button.heightHint = 30;
		operation.setLayoutData(gd_button);
		operation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				optionToSelect=false;
			}
		});
		operation.setToolTipText("create operation class");
		operation.setGrayed(true);
		operation.setText(" Class");
		getShell().setText("Select");
        if(optionToSelect)
        {
        	expression.setSelection(true);
        }	
        else
        {	
        	 operation.setSelection(true); 	
        }
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		Button button = createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
	}

	@Override
	protected Point getInitialSize() {
		return new Point(220, 220);
	}

  
   
   public boolean getOptionToSelect() {
		return optionToSelect;
	}


@Override
	protected void okPressed() {
			okPressed=true;
			super.okPressed();
	}


  public boolean isOkPressed() 
  {
	return okPressed;
   }
   
}
