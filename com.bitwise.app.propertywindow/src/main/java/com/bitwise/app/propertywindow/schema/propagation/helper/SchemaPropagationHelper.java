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

 
package com.bitwise.app.propertywindow.schema.propagation.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.SchemaGrid;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.propertywindow.messages.Messages;

public class SchemaPropagationHelper {

	public final static SchemaPropagationHelper INSTANCE = new SchemaPropagationHelper();

	public Map<String, List<String>> getFieldsForFilterWidget(Component component) {
		Map<String, List<String>> propagatedFiledMap = new HashMap<String, List<String>>();
		List<String> genratedProperty = null;
		ComponentsOutputSchema outputSchema = null;
		for (Link link : component.getTargetConnections()) {
			outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
			genratedProperty = new ArrayList<String>();
			if (outputSchema != null)
				for (FixedWidthGridRow row : outputSchema.getFixedWidthGridRowsOutputFields())
					genratedProperty.add(row.getFieldName());
			propagatedFiledMap.put(link.getTargetTerminal(), genratedProperty);
		}

		return propagatedFiledMap;
	}

	public List<List<FilterProperties>> sortedFiledNamesBySocketId(Component component) {
		int inputPortCount = 2;
		List<List<FilterProperties>> listofFiledNameList = new ArrayList<>();
		if (component.getProperties().get("inPortCount") != null)
			inputPortCount = Integer.parseInt((String) component.getProperties().get("inPortCount"));
		for (int i = 0; i < inputPortCount; i++) {
			listofFiledNameList.add(getFieldNameList(component, Constants.INPUT_SOCKET_TYPE + i));
		}
		return listofFiledNameList;
	}

	private List<FilterProperties> getFieldNameList(Component component, String targetTerminal) {
		FilterProperties filedName = null;
		ComponentsOutputSchema schema = null;
		List<FilterProperties> filedNameList = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {

			if (link.getTargetTerminal().equals(targetTerminal)) {
				schema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);

				if (schema != null) {
					for (FixedWidthGridRow row : schema.getFixedWidthGridRowsOutputFields()) {
						filedName = new FilterProperties();
						filedName.setPropertyname(row.getFieldName());
						filedNameList.add(filedName);
					}
				}

			}
		}
		return filedNameList;
	}
	

	public FixedWidthGridRow createFixedWidthGridRow(String fieldName) {

		FixedWidthGridRow fixedWidthGridRow = null;
		if (fieldName != null) {
			fixedWidthGridRow = new FixedWidthGridRow();
			fixedWidthGridRow.setFieldName(fieldName);
			fixedWidthGridRow.setDataType(Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX));
			fixedWidthGridRow.setDataTypeValue(String.class.getCanonicalName());
			fixedWidthGridRow.setScale("");
			fixedWidthGridRow.setLength("");
			fixedWidthGridRow.setPrecision("");
			fixedWidthGridRow.setDateFormat("");
			fixedWidthGridRow.setScaleType(Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX));
			fixedWidthGridRow.setScaleTypeValue(Messages.SCALE_TYPE_NONE);
			
		}
		return fixedWidthGridRow;
	}


	public SchemaGrid createSchemaGridRow(String fieldName) {

		SchemaGrid	 schemaGrid = null;
		if (fieldName != null) {
			schemaGrid = new SchemaGrid();
			schemaGrid.setFieldName(fieldName);
			schemaGrid.setDataType(Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX));
			schemaGrid.setDataTypeValue(String.class.getCanonicalName());
			schemaGrid.setScale("");
			schemaGrid.setPrecision("");
			schemaGrid.setDateFormat("");
			schemaGrid.setScaleType(Integer.valueOf(Constants.DEFAULT_INDEX_VALUE_FOR_COMBOBOX));
			schemaGrid.setScaleTypeValue(Messages.SCALE_TYPE_NONE);
			
		}
		return schemaGrid;
	}
	
	/**
	 * This method fetches input schema fields from source component.
	 * 
	 * @param sourceComponent
	 * @return
	 */
	public List<String> getInputFieldListForLink(Link link) {
		ComponentsOutputSchema sourceComponentsOutputSchema;
		List<String> availableFields = new ArrayList<>();
		sourceComponentsOutputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
		if (sourceComponentsOutputSchema != null) {
			for (GridRow gridRow : sourceComponentsOutputSchema.getFixedWidthGridRowsOutputFields())
				availableFields.add(StringUtils.lowerCase(gridRow.getFieldName()));
		}
		return availableFields;
	}

}
