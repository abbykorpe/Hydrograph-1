package com.bitwise.app.propertywindow.widgets.listeners.grid.transform;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import com.bitwise.app.common.datastructure.property.OperationField;
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
public class ELTTransforAddSelectionListener extends GridWidgetCommonBuilder {

	@Override
	public void createDefaultSchema(List grids, TableViewer tableViewer,
			Label errorLabel) {
		OperationField operationField = new OperationField();
		operationField.setName("Test");
 		if(!grids.contains(operationField)){
			grids.add(operationField);  
			tableViewer.setInput(grids);
			tableViewer.refresh();
		}	 
	} 

	@Override
	public CellEditor[] createCellEditorList(Table table, int size) {
		// TODO Auto-generated method stub
		return null;
	}


}
