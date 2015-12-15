package com.bitwise.app.propertywindow.fixedwidthschema;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;

/**
 *
 */
public class FixedWidthGridWidgetBuilder extends GridWidgetCommonBuilder {
	
	public static FixedWidthGridWidgetBuilder INSTANCE = new FixedWidthGridWidgetBuilder();
	
	private FixedWidthGridWidgetBuilder() {}
	
	public CellEditor[] createCellEditorList(Table table,int size){
		
		CellEditor[] cellEditor = createCellEditor(size);
		addTextEditor(table,cellEditor, 0);
		addComboBox(table, cellEditor, getDataTypeKey(), 1);
		addTextEditor(table,cellEditor, 2);
		addTextEditor(table,cellEditor, 3);
		addTextEditor(table,cellEditor, 4);
		return cellEditor;
	}

	/*
	 * Table mouse click event.
	 * Add new column in schema grid with default values.
	 * 
	 */
	@Override
	public void createDefaultSchema(List grids, TableViewer tableViewer, Label errorLabel) {
		FixedWidthGridRow fixedGrid = new FixedWidthGridRow();
		fixedGrid.setFieldName("");
		fixedGrid.setDateFormat("");
		fixedGrid.setScale("");
		fixedGrid.setDataType(Integer.valueOf("0"));
		fixedGrid.setDataTypeValue(getDataTypeValue()[Integer.valueOf("0")]); 
		fixedGrid.setLength("0");
		
		if(!grids.contains(fixedGrid)){
			grids.add(fixedGrid);  
			tableViewer.refresh();
			tableViewer.editElement(tableViewer.getElementAt(grids.size() == 0 ? grids.size() : grids.size() - 1), 0);
		}
	}
}
