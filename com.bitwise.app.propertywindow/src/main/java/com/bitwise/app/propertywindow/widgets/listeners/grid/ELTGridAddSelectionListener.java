package com.bitwise.app.propertywindow.widgets.listeners.grid;

import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ELTSelectionTaskListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

/**
 * The listener interface for receiving ELTGridAddSelection events. The class that is interested in processing a
 * ELTGridAddSelection event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTGridAddSelectionListener<code> method. When
 * the ELTGridAddSelection event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTGridAddSelectionEvent
 */
public class ELTGridAddSelectionListener extends ELTSelectionTaskListener{

	@Override
	public void selectionListenerAction(
			PropertyDialogButtonBar propertyDialogButtonBar,
			ListenerHelper helpers, Widget... widgets) {
		ELTGridDetails eltGridDetails = (ELTGridDetails)helpers.get(HelperType.SCHEMA_GRID);
		eltGridDetails.getGridWidgetCommonBuilder().createDefaultSchema(eltGridDetails.getGrids(), eltGridDetails.getTableViewer(), eltGridDetails.getLabel());
		
	}

	
}
