package hydrograph.ui.propertywindow.widgets.listeners.grid;

import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.MouseActionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;

public class DisposeSchemaGridListener extends MouseActionListener{
	public int getListenerType() {
		return SWT.Dispose;
	}
	
	@Override
	public void mouseAction(PropertyDialogButtonBar propertyDialogButtonBar,
			ListenerHelper helpers, Event event, Widget... widgets) {
		   Table table=(Table)event.widget;
		   Shell tip=(Shell) table.getData("tip");
		   Label label=(Label) table.getData("label");
		if(tip!=null) 
		{
		 tip.dispose();
         tip = null;
         label = null;
		}
	}
}
