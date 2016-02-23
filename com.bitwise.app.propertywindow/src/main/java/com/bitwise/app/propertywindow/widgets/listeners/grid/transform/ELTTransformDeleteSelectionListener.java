package com.bitwise.app.propertywindow.widgets.listeners.grid.transform;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.common.datastructure.property.PropertyField;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ELTSelectionTaskListener;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridDetails;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

/**
 * The listener interface for receiving ELTGridDeleteSelection events. The class that is interested in processing a
 * ELTGridDeleteSelection event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addELTGridDeleteSelectionListener<code> method. When
 * the ELTGridDeleteSelection event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTGridDeleteSelectionEvent
 */
public class ELTTransformDeleteSelectionListener extends ELTSelectionTaskListener{

	@Override
	public void selectionListenerAction(
			PropertyDialogButtonBar propertyDialogButtonBar,
			ListenerHelper helpers, Widget... widgets) {

		ELTGridDetails gridDetails = (ELTGridDetails) helpers.get(HelperType.SCHEMA_GRID);
		Table table =gridDetails.getTableViewer().getTable();
		int temp = table.getSelectionIndex();
		int[] indexs=table.getSelectionIndices();
		if (temp == -1) {
			WidgetUtility.errorMessage("Please Select row to delete");
		} else {
			table.remove(indexs);
			ArrayList tempList= new ArrayList();
			for (int index :indexs) { 
//				PropertyField test =(PropertyField) gridDetails.getGrids().get(index);
//				tempList.add(test);
			}
			 gridDetails.getGrids().removeAll(tempList); 
			
		}		
	}

	
}
