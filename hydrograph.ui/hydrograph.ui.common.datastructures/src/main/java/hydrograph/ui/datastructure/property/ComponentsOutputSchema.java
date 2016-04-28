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

 
package hydrograph.ui.datastructure.property;

import hydrograph.ui.common.cloneableinterface.IDataStructure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;

/**
 * This class stores output schema of each component in FixedWidth format.
 * 
 * @author Bitwise
 * 
 */
public class ComponentsOutputSchema implements IDataStructure {

	private String fromSocketId;
	private List<FixedWidthGridRow> fixedWidthGridRowsOutputFields = new ArrayList<>();
	private List<MixedSchemeGridRow> mixedSchemeGridRowsOutputFields = new ArrayList<>();
	private List<String> passthroughFields = new LinkedList<>();
	private Map<String, String> mapFields = new LinkedHashMap<>();
	private Map<String,String> passThroughFieldsPortInfo = new LinkedHashMap<>();
	private Map<String,String> mapFieldsPortInfo = new LinkedHashMap<>();

	private String STRING_TYPE="java.lang.String";
	


	/**
	 * This method adds grid row object as fixed width object
	 * 
	 * @param gridRow
	 */
	public void addSchemaFields(GridRow gridRow) {
		if (gridRow instanceof FixedWidthGridRow) {
			this.fixedWidthGridRowsOutputFields.add((FixedWidthGridRow) gridRow);
		} else if(gridRow instanceof MixedSchemeGridRow){
			this.mixedSchemeGridRowsOutputFields.add((MixedSchemeGridRow) gridRow);
		}else if (gridRow instanceof BasicSchemaGridRow) {
			this.fixedWidthGridRowsOutputFields.add(convertSchemaGridRowToFixedWidthSchema((BasicSchemaGridRow) gridRow));
		}

	}


	/**
	 * This method converts current fixed width object into schema grid.
	 * 
	 * @param fixedWidthGridRow
	 * @return SchemaGrid
	 */
	public BasicSchemaGridRow convertFixedWidthSchemaToSchemaGridRow(FixedWidthGridRow fixedWidthGridRow) {
		BasicSchemaGridRow schemaGrid = null;
		if (fixedWidthGridRow != null) {
			schemaGrid = new BasicSchemaGridRow();
			schemaGrid.setDataType(fixedWidthGridRow.getDataType());
			schemaGrid.setDataTypeValue(fixedWidthGridRow.getDataTypeValue());
			schemaGrid.setDateFormat(fixedWidthGridRow.getDateFormat());
			schemaGrid.setPrecision(fixedWidthGridRow.getPrecision());
			schemaGrid.setFieldName(fixedWidthGridRow.getFieldName());
			schemaGrid.setScale(fixedWidthGridRow.getScale());
			schemaGrid.setScaleType(fixedWidthGridRow.getScaleType());
			schemaGrid.setScaleTypeValue(fixedWidthGridRow.getScaleTypeValue());
			schemaGrid.setDescription(fixedWidthGridRow.getDescription());
		}
		return schemaGrid;
	}

	/**
	 * This method converts current schema object into fixed width.
	 * 
	 * @param fixedWidthGridRow
	 * @return SchemaGrid
	 */
	private FixedWidthGridRow convertSchemaGridRowToFixedWidthSchema(BasicSchemaGridRow schemaGrid) {
		FixedWidthGridRow fixedWidthGridRow = null;
		if (schemaGrid != null) {
			fixedWidthGridRow = new FixedWidthGridRow();
			fixedWidthGridRow.setDataType(schemaGrid.getDataType());
			fixedWidthGridRow.setDataTypeValue(schemaGrid.getDataTypeValue());
			fixedWidthGridRow.setDateFormat(schemaGrid.getDateFormat());
			fixedWidthGridRow.setPrecision(schemaGrid.getPrecision());
			fixedWidthGridRow.setFieldName(schemaGrid.getFieldName());
			fixedWidthGridRow.setScale(schemaGrid.getScale());
			fixedWidthGridRow.setScaleType(schemaGrid.getScaleType());
			fixedWidthGridRow.setScaleTypeValue(schemaGrid.getScaleTypeValue());
			fixedWidthGridRow.setDescription(schemaGrid.getDescription());
			fixedWidthGridRow.setLength("");
		}
		return fixedWidthGridRow;
	}


	/**
	 * It updates the current schema as per its pass-through fields mapping
	 * 
	 * @param sourceOutputSchema
	 * @param string 
	 */
	public void updatePassthroughFieldsSchema(ComponentsOutputSchema sourceOutputSchema, String port) {
		FixedWidthGridRow targetFixedWidthGridRow;
		FixedWidthGridRow sourceFixedWidthGridRow;
		for (String fieldName : passthroughFields) {
			targetFixedWidthGridRow = getFixedWidthGridRowForFieldName(fieldName);
			if (targetFixedWidthGridRow != null && sourceOutputSchema!=null) {
				sourceFixedWidthGridRow = sourceOutputSchema.getFixedWidthGridRowForFieldName(fieldName);
				if (sourceFixedWidthGridRow != null) {
					targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
					targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
					targetFixedWidthGridRow.setLength(sourceFixedWidthGridRow.getLength());
					targetFixedWidthGridRow.setPrecision(sourceFixedWidthGridRow.getPrecision());
					targetFixedWidthGridRow.setScale(sourceFixedWidthGridRow.getScale());
					targetFixedWidthGridRow.setDateFormat(sourceFixedWidthGridRow.getDateFormat());
					targetFixedWidthGridRow.setScaleType(sourceFixedWidthGridRow.getScaleType());
					targetFixedWidthGridRow.setScaleTypeValue(sourceFixedWidthGridRow.getScaleTypeValue());
					targetFixedWidthGridRow.setDescription(sourceFixedWidthGridRow.getDescription());
				}
				
			}
		}
	}

	private FixedWidthGridRow getFixedWidthGridRowForFieldName(String fieldName) {
		for (FixedWidthGridRow fixedWidthGridRow : fixedWidthGridRowsOutputFields) {
			if (fixedWidthGridRow.getFieldName().equals(fieldName))
				return fixedWidthGridRow;
		}
		return null;
	}

	/**
	 * It updates the current schema as per its map-fields mapping
	 * 
	 * @param sourceOutputSchema
	 * @param port 
	 */
	public void updateMapFieldsSchema(ComponentsOutputSchema sourceOutputSchema, String port) {
		System.out.println("The port is: " + port);
		FixedWidthGridRow targetFixedWidthGridRow;
		FixedWidthGridRow sourceFixedWidthGridRow;
		for (Entry<String, String> entry : mapFields.entrySet()) {
			targetFixedWidthGridRow = getFixedWidthGridRowForFieldName(entry.getKey());
			if (targetFixedWidthGridRow != null && sourceOutputSchema!=null) {
				sourceFixedWidthGridRow = sourceOutputSchema.getFixedWidthGridRowForFieldName(entry.getValue());
             	if (sourceFixedWidthGridRow != null) {
					targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
					targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
					targetFixedWidthGridRow.setLength(sourceFixedWidthGridRow.getLength());
					targetFixedWidthGridRow.setPrecision(sourceFixedWidthGridRow.getPrecision());
					targetFixedWidthGridRow.setScale(sourceFixedWidthGridRow.getScale());
					targetFixedWidthGridRow.setDateFormat(sourceFixedWidthGridRow.getDateFormat());
					targetFixedWidthGridRow.setScaleType(sourceFixedWidthGridRow.getScaleType());
					targetFixedWidthGridRow.setScaleTypeValue(sourceFixedWidthGridRow.getScaleTypeValue());
					targetFixedWidthGridRow.setDescription(sourceFixedWidthGridRow.getDescription());
				}
				
				
				
			}
		}
	}

	/**
	 * Copy output-schema from source component. 
	 * 
	 * @param sourceComponentsOutputSchema
	 */
	public void copySchemaFromOther(ComponentsOutputSchema sourceComponentsOutputSchema) {
	
		this.flushCurrentData();
		if (sourceComponentsOutputSchema != null) {
			this.getFixedWidthGridRowsOutputFields().addAll(
					sourceComponentsOutputSchema.getFixedWidthGridRowsOutputFields());
			this.getPassthroughFields().addAll(sourceComponentsOutputSchema.getPassthroughFields());
			this.getMapFields().putAll(sourceComponentsOutputSchema.getMapFields());
			this.getPassthroughFieldsPortInfo().putAll(
					sourceComponentsOutputSchema.getPassthroughFieldsPortInfo());
		}
	}

	private void flushCurrentData() {
		this.getFixedWidthGridRowsOutputFields().clear();
		this.getPassthroughFields().clear();
		this.getMapFields().clear();
		this.getPassthroughFieldsPortInfo().clear();
	}
	
	public List<BasicSchemaGridRow> getSchemaGridOutputFields() {
		List<BasicSchemaGridRow> schemaGrid = new ArrayList<>();
		for (FixedWidthGridRow fixedWidthGridRow : fixedWidthGridRowsOutputFields) {
			schemaGrid.add(convertFixedWidthSchemaToSchemaGridRow(fixedWidthGridRow));
		}
		return schemaGrid;
	}

	@Override
	public ComponentsOutputSchema clone() {
		return new ComponentsOutputSchema();

	}
	
	public List<String> getPassthroughFields() {

		return passthroughFields;
	}

	public Map<String, String> getMapFields() {

		return mapFields;
	}
	
	public Map<String, String> getPassthroughFieldsPortInfo() {
		return passThroughFieldsPortInfo;
	}

	public Map<String, String> getMapFieldsPortInfo() {
		return mapFieldsPortInfo;
	}

	public String getFromSocketId() {
		return fromSocketId;
	}

	public void setFromSocketId(String fromSocketId) {
		this.fromSocketId = fromSocketId;
	}

	public List<FixedWidthGridRow> getFixedWidthGridRowsOutputFields() {
		return fixedWidthGridRowsOutputFields;
	}

	/**
	 * This methods returns fixed-width-schema row of given field-name.
	 * 
	 * @param fieldName
	 * @return
	 */
	public FixedWidthGridRow getFixedWidthSchemaRow(String fieldName) {
		FixedWidthGridRow fixedWidthGridRow = null;
		if (StringUtils.isNotEmpty(fieldName)) {
			for (FixedWidthGridRow row : this.getFixedWidthGridRowsOutputFields())
				if (StringUtils.equals(fieldName, row.getFieldName()))
					fixedWidthGridRow = row;
		}
		return fixedWidthGridRow;
	}
	
	/**
	 * This methods returns schema-grid row of given field-name.
	 * 
	 * @param fieldName
	 * @return
	 */
	public BasicSchemaGridRow getSchemaGridRow(String fieldName) {
		BasicSchemaGridRow schemaGridRow = null;
		if (StringUtils.isNotEmpty(fieldName)) {
		for (BasicSchemaGridRow row : this.getSchemaGridOutputFields())
			if (StringUtils.equals(fieldName, row.getFieldName()))
				schemaGridRow = row;
		}
		return schemaGridRow;
	}


	public MixedSchemeGridRow convertFixedWidthSchemaToMixedSchemaGridRow(
			FixedWidthGridRow fixedWidthGridRow) {
		MixedSchemeGridRow mixedSchemeGridRow=new MixedSchemeGridRow();
		mixedSchemeGridRow.setDataType(fixedWidthGridRow.getDataType());
		mixedSchemeGridRow.setDataTypeValue(fixedWidthGridRow.getDataTypeValue());
		mixedSchemeGridRow.setDateFormat(fixedWidthGridRow.getDateFormat());
		mixedSchemeGridRow.setDescription(fixedWidthGridRow.getDescription());
		mixedSchemeGridRow.setFieldName(fixedWidthGridRow.getFieldName());
		mixedSchemeGridRow.setLength(fixedWidthGridRow.getLength());
		mixedSchemeGridRow.setPrecision(fixedWidthGridRow.getPrecision());
		mixedSchemeGridRow.setScale(fixedWidthGridRow.getScale());
		mixedSchemeGridRow.setScaleType(fixedWidthGridRow.getScaleType());
		mixedSchemeGridRow.setScaleTypeValue(fixedWidthGridRow.getScaleTypeValue());
		mixedSchemeGridRow.setDelimiter("");
	return mixedSchemeGridRow;
	}
}
