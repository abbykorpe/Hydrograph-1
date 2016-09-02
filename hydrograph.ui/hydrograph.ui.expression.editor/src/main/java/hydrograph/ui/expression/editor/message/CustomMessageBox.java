package hydrograph.ui.expression.editor.message;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class CustomMessageBox  {

	private int messageType;
	private String message;
	private String title;

	public CustomMessageBox(int messageType,String message,String title) {
		this.messageType=messageType;
		this.message=message;
		this.title=title;
	}
	
	public void open(){
		if(messageType==SWT.ERROR){
			MessageDialog.setDefaultOrientation(SWT.NONE);
			MessageDialog.openError(Display.getCurrent().getActiveShell(), title, message);
		}
		if(messageType==SWT.ICON_INFORMATION){
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), title, message);
		}
		if(messageType==SWT.ICON_WARNING){
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), title, message);
		}
	}
	
}
