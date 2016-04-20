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

package hydrograph.ui.propertywindow.filemixedschema;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.MixedSchemeGridRow;
import hydrograph.ui.propertywindow.widgets.utility.GridWidgetCommonBuilder;

public class MixedSchemeGridWidgetBuilder extends GridWidgetCommonBuilder{
	
	public static MixedSchemeGridWidgetBuilder INSTANCE = new MixedSchemeGridWidgetBuilder();
	
	private MixedSchemeGridWidgetBuilder(){}

	@Override
	public void createDefaultSchema(List grids, TableViewer tableViewer,
			Label errorLabel) {
		int rowSequence = getRowSequence();
		
		MixedSchemeGridRow mixedSchemeGridRow = new MixedSchemeGridRow();
		
		mixedSchemeGridRow.setFieldName("DefaultField" + rowSequence++);
		mixedSchemeGridRow.setDateFormat("");
		mixedSchemeGridRow.setPrecision("");
		mixedSchemeGridRow.setScale("");
		mixedSchemeGridRow.setScaleType(Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX));
		mixedSchemeGridRow.setScaleTypeValue(getScaleTypeValue()[Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]); 
		mixedSchemeGridRow.setDataType(Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX));
		mixedSchemeGridRow.setDataTypeValue(getDataTypeValue()[Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX)]); 
		mixedSchemeGridRow.setLength("0");
		mixedSchemeGridRow.setDescription("");
		mixedSchemeGridRow.setDelimiter("");
		
		int numberOfRows=grids.size();
		do{
			if(!grids.contains(mixedSchemeGridRow)){
				grids.add(mixedSchemeGridRow);  
				tableViewer.setInput(grids);
				tableViewer.refresh();
				tableViewer.editElement(tableViewer.getElementAt(grids.size() == 0 ? grids.size() : grids.size() - 1), 0);
				break;
			}
			mixedSchemeGridRow.setFieldName("DefaultField" + rowSequence++);
			numberOfRows--;
		}while(numberOfRows>=-1);
	}

	@Override
	public CellEditor[] createCellEditorList(Table table, int size) {
		CellEditor[] cellEditor = createCellEditor(size);
		addTextEditor(table, cellEditor, 0);
		addComboBox(table, cellEditor, getDataTypeKey(), 1);
		addTextEditor(table, cellEditor, 2);
		addTextEditor(table, cellEditor, 3);
		addTextEditor(table, cellEditor, 4);
		addComboBox(table, cellEditor, getScaleTypeKey(), 5);
		addTextEditor(table, cellEditor, 6);
		addTextEditor(table, cellEditor, 7);
		addTextEditor(table, cellEditor, 8);
		
		return cellEditor;
	}

}
