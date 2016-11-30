package hydrograph.ui.propertywindow.widgets.customwidgets.oracle;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.StyledText;

public class SQLQueryStatementDialog extends Dialog {

	private StyledText styledText;
	private  String styleTextValue;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SQLQueryStatementDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL | SWT.RESIZE);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText("SQL Query");
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label sqlQueryLabel = new Label(composite, SWT.NONE);
		sqlQueryLabel.setText("SQL Query Statement");
		
		styledText = new StyledText(composite, SWT.BORDER | SWT.V_SCROLL);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		styledText.setFont(new Font(Display.getCurrent(),"Courier New",9,SWT.NORMAL));
		styledText.addListener(SWT.Verify, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int position = styledText.getCaretOffset();
				String contents = styledText.getText(); 
				if(styledText.getCaretOffset()>= 80){
					container.getShell().setMinimumSize(1132, 254);
				}
				if ((position - contents.lastIndexOf('\n')) >= 140) { 
					event.text += '\n'; 
					} 
			}
		});

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		 createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	 @Override
	protected void okPressed() {
		 if(styledText !=null){
			  styleTextValue = styledText.getText();
			 
			 if(StringUtils.isNotBlank(styleTextValue)){
				 setStyleTextSqlQuery(styleTextValue);
			 }
			 
		 }
		 
		super.okPressed();
	}
	 
	 public String getStyleTextSqlQuery(){
		 return styleTextValue;
	 }
	 
	 
	 public String setStyleTextSqlQuery(String styleTextValue){
		 return styleTextValue;
	 }
	 
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(612, 254);
	}

}
