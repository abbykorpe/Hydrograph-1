package com.bitwise.app.propertywindow.widgets.listeners.grid;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ELTSelectionTaskListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;

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

	private Table table;
	private int rowSequence=1;
	@Override
	public int getListenerType() {
      return SWT.MouseUp;
	}
	
	@Override
	public void selectionListenerAction(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helpers, Widget... widgets) {
		propertyDialogButtonBar.enableApplyButton(true);
		for(Widget widget:widgets){
			table=(Table)widget;
			table.getParent().getShell().setFocus();
		}
		ELTGridDetails eltGridDetails = (ELTGridDetails)helpers.get(HelperType.SCHEMA_GRID);
		GridWidgetCommonBuilder gridCommonBuilder = eltGridDetails.getGridWidgetCommonBuilder();
		gridCommonBuilder.setRowSequence(rowSequence);
		gridCommonBuilder.createDefaultSchema(eltGridDetails.getGrids(), eltGridDetails.getTableViewer(), eltGridDetails.getLabel());
		rowSequence++;
	}
}
