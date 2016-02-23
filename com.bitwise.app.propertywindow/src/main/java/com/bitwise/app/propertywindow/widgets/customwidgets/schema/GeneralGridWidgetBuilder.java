package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import com.bitwise.app.common.datastructure.property.SchemaGrid;
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
		addTextEditor(table, cellEditor, 2);
		addTextEditor(table, cellEditor, 3);
		addTextEditor(table, cellEditor, 4);
		addComboBox(table, cellEditor, getScaleTypeKey(), 5);
		addTextEditor(table, cellEditor, 6);
		return cellEditor;
	}

	/*
	 * Table mouse click event.
	 * Add new column in schema grid with default values.
	 * 
	 */
	@Override
	public void createDefaultSchema(List grids, TableViewer tableViewer, Label errorLabel) {
		int rowSequence = getRowSequence();
		SchemaGrid schemaGrid = new SchemaGrid();
		schemaGrid.setFieldName("DefaultField" + rowSequence);
		schemaGrid.setDateFormat("");
		schemaGrid.setPrecision("");
		schemaGrid.setScale("");
		schemaGrid.setScaleType(Integer.valueOf("0"));
		schemaGrid.setScaleTypeValue(getScaleTypeValue()[Integer.valueOf("0")]);
		schemaGrid.setDataType(Integer.valueOf("0"));
		schemaGrid.setDataTypeValue(getDataTypeValue()[Integer.valueOf("0")]);
		schemaGrid.setDescription("");
		
		int numberOfRows=grids.size();
		do{
			if(!grids.contains(schemaGrid)){
				grids.add(schemaGrid);  
				tableViewer.setInput(grids);
				tableViewer.refresh();
				tableViewer.editElement(tableViewer.getElementAt(grids.size() == 0 ? grids.size() : grids.size() - 1), 0);
				break;
			}
			schemaGrid.setFieldName("DefaultField" + rowSequence++);
			numberOfRows--;
		}while(numberOfRows>=-1);
 		
	}
}
