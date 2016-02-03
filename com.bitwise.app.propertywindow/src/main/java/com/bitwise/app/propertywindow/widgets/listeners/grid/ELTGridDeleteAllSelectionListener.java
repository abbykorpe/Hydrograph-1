package com.bitwise.app.propertywindow.widgets.listeners.grid;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ELTSelectionTaskListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * The listener interface for receiving ELTGridDeleteAllSelection events. The class that is interested in processing a
 * ELTGridDeleteAllSelection event implements this interface, and the object created with that class is registered with
 * a component using the component's <code>addELTGridDeleteAllSelectionListener<code> method. When
 * the ELTGridDeleteAllSelection event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTGridDeleteAllSelectionEvent
 */
public class ELTGridDeleteAllSelectionListener extends ELTSelectionTaskListener{

	@Override
	public void selectionListenerAction(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helpers, Widget... widgets) {
		propertyDialogButtonBar.enableApplyButton(true);
		Table table = (Table) widgets[0];
		boolean userAns = WidgetUtility.eltConfirmMessage("Do you really want to delete all properties?");
		if (userAns) {
			ELTGridDetails gridDetails = (ELTGridDetails) helpers.get(HelperType.SCHEMA_GRID);
			gridDetails.getGrids().removeAll(gridDetails.getGrids());
			gridDetails.getTableViewer().refresh();
		}
	}
}
