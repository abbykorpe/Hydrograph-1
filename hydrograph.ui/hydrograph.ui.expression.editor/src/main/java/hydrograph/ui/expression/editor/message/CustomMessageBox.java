package hydrograph.ui.expression.editor.message;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

public class CustomMessageBox extends MessageBox {

	public CustomMessageBox(int messageType,String message,String title) {
		super(Display.getCurrent().getActiveShell(), messageType);
		if(StringUtils.isNotBlank(message))
			this.setMessage(message);
		this.setText(title);
	}
	
	@Override
	protected void checkSubclass() {
		
	}
}
