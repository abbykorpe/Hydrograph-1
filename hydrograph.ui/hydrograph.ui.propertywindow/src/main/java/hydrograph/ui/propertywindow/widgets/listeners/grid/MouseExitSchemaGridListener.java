package hydrograph.ui.propertywindow.widgets.listeners.grid;

import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.MouseActionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

public class MouseExitSchemaGridListener extends MouseActionListener{
	@Override
	public int getListenerType() {
		return SWT.MouseExit;
	}
	@Override
	public void mouseAction(PropertyDialogButtonBar propertyDialogButtonBar,
			ListenerHelper helpers, Event event, Widget... widgets) {
	       	Label label=(Label )event.widget;
			label.getShell().dispose();
	        
	}
}
