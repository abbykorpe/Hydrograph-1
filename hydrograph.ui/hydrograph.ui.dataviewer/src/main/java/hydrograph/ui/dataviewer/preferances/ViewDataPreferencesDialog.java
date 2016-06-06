package hydrograph.ui.dataviewer.preferances;



import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class ViewDataPreferencesDialog extends Dialog {
	private Text delimiterTextBox;
	private Text quoteCharactorTextBox;
	private Button includeHeardersCheckBox;
	private ViewDataPreferences viewDataPreferences;
	private static final String WARNING="Warning";
	private static final String ERROR_MESSAGE="Exported file might not open in Excel due to change in default delimiter and quote character.";
	private static final String DEFAULT_DELIMITER=",";
	private static final String DEFAULT_QUOTE_CHARACTOR="\"";

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
		includeHeardersCheckBox.setBounds(265, 21, 109, 16);
		includeHeardersCheckBox.setText("Include Headers");
		delimiterTextBox.setText(viewDataPreferences.getDelimiter());
		quoteCharactorTextBox.setText(viewDataPreferences.getQuoteCharactor());
		includeHeardersCheckBox.setSelection(viewDataPreferences.getIncludeHeaders());
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
		return new Point(416, 177);
	}

	@Override
	protected void okPressed() {
		if (!delimiterTextBox.getText().equalsIgnoreCase(DEFAULT_DELIMITER)
				&& !quoteCharactorTextBox.getText().equalsIgnoreCase(DEFAULT_QUOTE_CHARACTOR)) {
			MessageBox messageBox = new MessageBox(new Shell(), SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
			messageBox.setText(WARNING);
			messageBox.setMessage(ERROR_MESSAGE);
			int response = messageBox.open();
			if (response == SWT.OK) {
				setPreferences();
				super.okPressed();
			}
		} else {
			setPreferences();
			super.okPressed();
		}
	}

	private void setPreferences() {
		viewDataPreferences.setDelimiter(delimiterTextBox.getText());
		viewDataPreferences.setQuoteCharactor(quoteCharactorTextBox.getText());
		viewDataPreferences.setIncludeHeaders(includeHeardersCheckBox.getSelection());
	}



	public void setViewDataPreferences(ViewDataPreferences viewDataPreferences) {
		this.viewDataPreferences = viewDataPreferences;
	}

	public ViewDataPreferences getViewDataPreferences() {
		return viewDataPreferences;
	}
	
}
