package hydrograph.ui.propertywindow.hivesparqueschema;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.BasicSchemaGridRow;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.utility.GridWidgetCommonBuilder;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;


/**
 * The Class HiveParqueSchemaWidgetBuilder.
 * 
 * @author Bitwise
 *
 */
public class HiveParqueSchemaWidgetBuilder extends GridWidgetCommonBuilder{

	public static HiveParqueSchemaWidgetBuilder INSTANCE = new HiveParqueSchemaWidgetBuilder();
	
	private HiveParqueSchemaWidgetBuilder() {
	}
	@Override
	public void createDefaultSchema(List grids, TableViewer tableViewer,
			Label errorLabel) {
		int rowSequence = getRowSequence();
		BasicSchemaGridRow schemaGrid = new BasicSchemaGridRow();
		schemaGrid.setFieldName("DefaultField" + rowSequence);
		schemaGrid.setDateFormat("");
		schemaGrid.setPrecision("");
		schemaGrid.setScale("");
		schemaGrid.setScaleType(Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX));
		schemaGrid.setScaleTypeValue(getScaleTypeValue()[Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]);
		schemaGrid.setDataType(Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX));
		schemaGrid.setDataTypeValue(getDataTypeValue()[Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]);
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

	@Override
	public CellEditor[] createCellEditorList(Table table,
			Map<String, Integer> columns) {
		CellEditor[] cellEditor = createCellEditor(columns.size());
		addTextEditor(table,cellEditor, columns, (Messages.FIELDNAME));
		addComboBox(table, cellEditor, getDataTypeKey(), 1);
		addTextEditor(table, cellEditor, columns, Messages.PRECISION);
		addTextEditor(table, cellEditor, columns, Messages.SCALE);
		addComboBox(table, cellEditor, getScaleTypeKey(), 4);
		addTextEditor(table, cellEditor, columns, Messages.DATEFORMAT);
		addTextEditor(table, cellEditor, columns, Messages.FIELD_DESCRIPTION);
		
		return cellEditor;
	}

}
