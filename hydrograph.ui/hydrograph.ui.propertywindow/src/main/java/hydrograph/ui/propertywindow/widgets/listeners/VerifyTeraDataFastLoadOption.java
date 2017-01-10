package hydrograph.ui.propertywindow.widgets.listeners;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Widget;

import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

public class VerifyTeraDataFastLoadOption implements IELTListener{

	private static final String INFORMATION = "Information";
	private static final String FAST_LOAD = "Fast Load";
	
	@Override
	public int getListenerType() {
		return SWT.Selection;
	}

	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helpers,
			Widget... widgets) {
		final Widget[] widgetList = widgets;
		
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (StringUtils.equalsIgnoreCase(((Button) widgetList[0]).getText(), String.valueOf(FAST_LOAD)) && ((Button) widgetList[0]).getSelection() ) {
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(),
							SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setText(INFORMATION);
					messageBox.setMessage(Messages.FAST_LOAD_ERROR_MESSAGE);
					messageBox.open();
				}
			}
		};
		return listener;
	}

}
