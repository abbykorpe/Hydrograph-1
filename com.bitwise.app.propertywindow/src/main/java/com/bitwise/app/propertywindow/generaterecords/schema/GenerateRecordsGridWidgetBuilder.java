package com.bitwise.app.propertywindow.generaterecords.schema;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import com.bitwise.app.common.datastructure.property.GenerateRecordSchemaGridRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;

/**
 * This class creates the grid structure for Generate Records Schema Grid Widget.
 * 
 * @author Bitwise
 *
 */
public class GenerateRecordsGridWidgetBuilder extends GridWidgetCommonBuilder {

	public static GenerateRecordsGridWidgetBuilder INSTANCE = new GenerateRecordsGridWidgetBuilder();
	
	private GenerateRecordsGridWidgetBuilder() {
	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder#createCellEditorList(org.eclipse.swt.widgets.Table, int)
	 */
	public CellEditor[] createCellEditorList(Table table, int size) {

		CellEditor[] cellEditor = createCellEditor(size);
		addTextEditor(table, cellEditor, 0);
		addComboBox(table, cellEditor, getDataTypeKey(), 1);
		addTextEditor(table, cellEditor, 2);
		addTextEditor(table, cellEditor, 3);
		addTextEditor(table, cellEditor, 4);
		addTextEditor(table, cellEditor, 5);
		addTextEditor(table, cellEditor, 6);
		addTextEditor(table, cellEditor, 7);
		return cellEditor;
	}

	
	/* (non-Javadoc)
	 * @see com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder#createDefaultSchema(java.util.List, org.eclipse.jface.viewers.TableViewer, org.eclipse.swt.widgets.Label)
	 */
	@Override
	public void createDefaultSchema(List grids, TableViewer tableViewer, Label errorLabel) {
		int rowSequence = getRowSequence();

		GenerateRecordSchemaGridRow generateRecordSchemaGridRow = new GenerateRecordSchemaGridRow();
		generateRecordSchemaGridRow.setFieldName(Constants.SCHEMA_DEFAULT_FIELD_NAME_SUFFIX+ rowSequence++);
		generateRecordSchemaGridRow.setDateFormat("");
		generateRecordSchemaGridRow.setScale("");
		generateRecordSchemaGridRow.setDataType(Integer.valueOf(Constants.DEFAULT_DATATYPE_INDEX_VALUE));
		generateRecordSchemaGridRow.setDataTypeValue(getDataTypeValue()[Integer.valueOf(Constants.DEFAULT_DATATYPE_INDEX_VALUE)]);
		generateRecordSchemaGridRow.setLength("");
		generateRecordSchemaGridRow.setRangeFrom("");
		generateRecordSchemaGridRow.setRangeTo("");
		generateRecordSchemaGridRow.setDefaultValue("");

		int numberOfRows = grids.size();
		do {
			if (!grids.contains(generateRecordSchemaGridRow)) {
				grids.add(generateRecordSchemaGridRow);
				tableViewer.refresh();
				tableViewer.editElement(tableViewer.getElementAt(grids.size() == 0 ? grids.size() : grids.size() - 1),
						0);
				break;
			}
			generateRecordSchemaGridRow.setFieldName(Constants.SCHEMA_DEFAULT_FIELD_NAME_SUFFIX + rowSequence++);
			numberOfRows--;
		} while (numberOfRows >= -1);

	}
}