package hydrograph.ui.propertywindow.widgets.listeners;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

public  abstract class MouseActionListener implements IELTListener  {
	@Override
	public int getListenerType() {
		return SWT.MouseHover;
	}

	@Override
	public Listener getListener(
			final PropertyDialogButtonBar propertyDialogButtonBar,
			final ListenerHelper helpers, final Widget... widgets) {
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				mouseAction(propertyDialogButtonBar,helpers,event,widgets);
			}
		};
		return listener;
	}
	public abstract void mouseAction(
			PropertyDialogButtonBar propertyDialogButtonBar,
			final ListenerHelper helpers,Event event, Widget... widgets);	
}
