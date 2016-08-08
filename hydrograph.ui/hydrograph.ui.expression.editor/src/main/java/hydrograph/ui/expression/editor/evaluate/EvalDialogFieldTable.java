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

package hydrograph.ui.expression.editor.evaluate;

import hydrograph.ui.expression.editor.enums.DataTypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class EvalDialogFieldTable {

	private Table table;
	private TableViewer tableViewer;
	private List<FieldNameAndValue> propertyList;
	private TableColumn fieldValueColumn;
	private TableColumn fieldNameColumn;
	public static final String FIELD_NAME_PROPERTY = "FIELD_NAME_PROPERTY";
	public static final String FIELD_VALUE_PROPERTY = "FIELD_VALUE_PROPERTY";

	
	private static final String[] PROPS = { FIELD_NAME_PROPERTY, FIELD_VALUE_PROPERTY };

	EvalDialogFieldTable createFieldTable(Composite fieldTableComposite,Map<String,Class<?>> fieldMap) {
		
		
		loadDefaultProperties(fieldMap);
		tableViewer = new TableViewer(fieldTableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		fieldNameColumn = new TableColumn(table, SWT.NONE);
		fieldNameColumn.setWidth(370);
		fieldNameColumn.setText("Field Name");
		
		fieldValueColumn = new TableColumn(table, SWT.NONE);
		fieldValueColumn.setWidth(148);
		fieldValueColumn.setText("Value");
		
		tableViewer.setContentProvider(new EvalFiedContentProvider());
		tableViewer.setLabelProvider(new EvalFieldLabelProvider());
		tableViewer.setInput(propertyList);
		
		createCellEditors();
		
		
		addListenerToCompositeForResizingNameColumn(fieldNameColumn);
		return this;
	}

	private void createCellEditors() {
		final CellEditor fieldNameeditor = new TextCellEditor(table);
		final CellEditor fieldValueeditor = new TextCellEditor(table);
		CellEditor[] editors = new CellEditor[] { fieldNameeditor, fieldValueeditor };
		tableViewer.setColumnProperties(PROPS);
		tableViewer.setCellModifier(new FieldNameAndValueCellModifier(tableViewer));
		tableViewer.setCellEditors(editors);
	}

	private void loadDefaultProperties(Map<String, Class<?>> fieldMap) {
		propertyList=new ArrayList<FieldNameAndValue>();
		if(fieldMap!=null){
			for(Entry< String, Class<?>> entry:fieldMap.entrySet()){
				FieldNameAndValue fieldNameAndValue=new FieldNameAndValue(entry.getKey(), 
						String.valueOf(DataTypes.getDefaulltValuefromDataTypesSimpleName(entry.getValue().getSimpleName())),entry.getValue().getSimpleName());
				propertyList.add(fieldNameAndValue);
			}
		}
	}

	private void addListenerToCompositeForResizingNameColumn(TableColumn fieldNameColumn2) {
		
	}

	public Object[] validateDataTypeValues() throws InvalidDataTypeValueException {
		String[] fieldNames=new String[propertyList.size()];
		Object[] fieldValues=new Object[propertyList.size()];
		for(FieldNameAndValue fieldNameAndValue:propertyList){
		fieldValues[propertyList.indexOf(fieldNameAndValue)]=DataTypes.validateInputeAndGetEquivalentObject(fieldNameAndValue.getFieldValue(), fieldNameAndValue.getFieldName(), fieldNameAndValue.getDataType());
		fieldNames[propertyList.indexOf(fieldNameAndValue)]=fieldNameAndValue.getFieldName();
		}
		return new Object[]{fieldNames,fieldValues};
	}
	
}
