package hydrograph.ui.propertywindow.widgets.dialogs;

import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class FieldDialogWithAddValue extends FieldDialog {

	public FieldDialogWithAddValue(Shell parentShell,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(parentShell, propertyDialogButtonBar);
	}

	
	@Override
	protected Composite addButtonPanel(final Composite container) {
	
		Composite btncomposite= super.addButtonPanel(container);
		Button button=new Button(btncomposite, SWT.NONE);
		button.setText("Add Values");
		
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				FieldDialogForAddingValue addingValue=new FieldDialogForAddingValue(container.getShell());
				addingValue.open();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		return btncomposite;
	}
}


