package hydrograph.ui.propertywindow.widgets.listeners;





import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
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
