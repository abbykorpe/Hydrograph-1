package hydrograph.ui.expression.editor.message;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

public class CustomMessageBox extends MessageBox {

	public CustomMessageBox(int messageType,String message,String title) {
		super(Display.getCurrent().getActiveShell(), messageType);
		this.setMessage(message);
		this.setText(title);
	}
	
	@Override
	protected void checkSubclass() {
		
	}
}
