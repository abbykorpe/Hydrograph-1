package hydrograph.ui.propertywindow.widgets.listeners.grid;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.MouseActionListener;

public class MouseDownSchemaGridListener extends MouseActionListener{
	@Override
	public int getListenerType() {
		return SWT.MouseDown;
	}
	@Override
	public void mouseAction(PropertyDialogButtonBar propertyDialogButtonBar,
			ListenerHelper helpers, Event event, Widget... widgets) {
		 Table table=(Table)widgets[0];
		 Label label=(Label)event.widget;
		 Event e = new Event();
         e.item = (TableItem) label.getData("_TABLEITEM");
         table.setSelection(new TableItem[] { (TableItem) e.item });
         table.notifyListeners(SWT.Selection, e);
	}

}
