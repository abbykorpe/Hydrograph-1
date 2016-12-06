/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hydrograph.ui.propertywindow.sqlschema;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.utility.GridWidgetCommonBuilder;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
/**
 * The Class SQLGridWidgetBuilder.
 * 
 * @author Bitwise
 */
public class SQLGridWidgetBuilder extends GridWidgetCommonBuilder{

	public static SQLGridWidgetBuilder INSTANCE = new SQLGridWidgetBuilder();
	
	@Override
	public void createDefaultSchema(List grids, TableViewer tableViewer,
			Label errorLabel) {
int rowSequence = getRowSequence();
		
		FixedWidthGridRow sqlGrid = new FixedWidthGridRow();
		sqlGrid.setFieldName("DefaultField" + rowSequence++);
		sqlGrid.setDateFormat("");
		sqlGrid.setPrecision("");
		sqlGrid.setScale("");
		sqlGrid.setScaleType(Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX));
		sqlGrid.setScaleTypeValue(getScaleTypeValue()[Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]); 
		sqlGrid.setDataType(Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX));
		sqlGrid.setDataTypeValue(getDataTypeValue()[Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]); 
		sqlGrid.setDescription("");
		sqlGrid.setColumnDefinition("");
		
		
		int numberOfRows=grids.size();
		do{
			if(!grids.contains(sqlGrid)){
				grids.add(sqlGrid);  
				tableViewer.setInput(grids);
				tableViewer.refresh();
				tableViewer.editElement(tableViewer.getElementAt(grids.size() == 0 ? grids.size() : grids.size() - 1), 0);
				break;
			}
			sqlGrid.setFieldName("DefaultField" + rowSequence++);
			numberOfRows--;
		}while(numberOfRows>=-1);

	}


	@Override
	public CellEditor[] createCellEditorList(Table table,
			Map<String, Integer> columns) {
		CellEditor[] cellEditor = createCellEditor(columns.size());
		addTextEditor(table, cellEditor, columns, (Messages.FIELDNAME));
		addComboBox(table, cellEditor, getDataTypeKey(), 1);
		addTextEditor(table, cellEditor,columns, Messages.DATEFORMAT);
		addTextEditor(table, cellEditor, columns, Messages.PRECISION);
		addTextEditor(table, cellEditor, columns, Messages.SCALE);
		addComboBox(table, cellEditor, getScaleTypeKey(), 4);
		addTextEditor(table, cellEditor, columns, Messages.FIELD_DESCRIPTION);
		addTextEditor(table, cellEditor, columns, Messages.COL_DEF);
		return cellEditor;
	}
}
