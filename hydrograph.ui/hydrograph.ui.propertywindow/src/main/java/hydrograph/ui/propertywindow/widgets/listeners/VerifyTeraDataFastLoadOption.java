package hydrograph.ui.propertywindow.widgets.listeners;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Widget;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

public class VerifyTeraDataFastLoadOption implements IELTListener{

	private static final String INFORMATION = "Information";
	
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
				if (StringUtils.equalsIgnoreCase(((Button) widgetList[0]).getText(), String.valueOf("Fast Load")) && ((Button) widgetList[0]).getSelection() ) {
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(),
							SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setText(INFORMATION);
					messageBox.setMessage("This option works only on empty target tables.");
					messageBox.open();
				}
			}
		};
		return listener;
	}

}
