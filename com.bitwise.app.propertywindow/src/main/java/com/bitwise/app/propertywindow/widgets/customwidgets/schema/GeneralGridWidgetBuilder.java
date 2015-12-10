package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;

/**
 * The Class GeneralGridWidgetBuilder.
 * 
 * @author Bitwise
 */
public class GeneralGridWidgetBuilder extends GridWidgetCommonBuilder {
	
	public static GeneralGridWidgetBuilder INSTANCE = new GeneralGridWidgetBuilder();
	
	private GeneralGridWidgetBuilder() {}
	
	public CellEditor[] createCellEditorList(Table table,int size){
		CellEditor[] cellEditor = createCellEditor(size);
		addTextEditor(table,cellEditor, 0);
		addComboBox(table, cellEditor, getDataTypeKey(), 1);
		addTextEditor(table,cellEditor, 2);
		addTextEditor(table,cellEditor, 3);
		return cellEditor;
	}

	/*
	 * Table mouse click event.
	 * Add new column in schema grid with default values.
	 * 
	 */
	@Override
	public void createDefaultSchema(List grids, TableViewer tableViewer, Label errorLabel) {
		SchemaGrid schemaGrid = new SchemaGrid();
		schemaGrid.setFieldName("");
		schemaGrid.setDateFormat(""); 
		schemaGrid.setScale("");
		schemaGrid.setDataType(Integer.valueOf("0"));
		schemaGrid.setDataTypeValue(getDataTypeValue()[Integer.valueOf("0")]);
 		if(!grids.contains(schemaGrid)){
			grids.add(schemaGrid);  
			tableViewer.refresh();
			tableViewer.editElement(tableViewer.getElementAt(grids.size() == 0 ? grids.size() : grids.size() - 1), 0);
		}
	}
}
