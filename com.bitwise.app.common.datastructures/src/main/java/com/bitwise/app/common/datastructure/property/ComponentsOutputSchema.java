package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bitwise.app.cloneableinterface.IDataStructure;

/**
 * This class stores output schema of each component in FixedWidth format.
 * 
 * @author Bitwise
 * 
 */
public class ComponentsOutputSchema implements IDataStructure {

	private String fromSocketId;
	private List<FixedWidthGridRow> fixedWidthGridRowsOutputFields = new ArrayList<>();
	private List<String> passthroughFields = new LinkedList<>();
	private Map<String, String> mapFields = new LinkedHashMap<>();
	private Map<String,String> passThroughFieldsPortInfo = new LinkedHashMap<>();
	private Map<String,String> mapFieldsPortInfo = new LinkedHashMap<>();
	private String STRING_TYPE="java.lang.String";
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


	/**
	 * This method adds grid row object as fixed width object
	 * 
	 * @param gridRow
	 */
	public void addSchemaFields(GridRow gridRow) {
		if (gridRow instanceof FixedWidthGridRow) {
			this.fixedWidthGridRowsOutputFields.add((FixedWidthGridRow) gridRow);
		} else if (gridRow instanceof SchemaGrid) {
			this.fixedWidthGridRowsOutputFields.add(convertSchemaGridRowToFixedWidthSchema((SchemaGrid) gridRow));
		}

	}


	/**
	 * This method converts current fixed width object into schema grid.
	 * 
	 * @param fixedWidthGridRow
	 * @return SchemaGrid
	 */
	public SchemaGrid convertFixedWidthSchemaToSchemaGridRow(FixedWidthGridRow fixedWidthGridRow) {
		SchemaGrid schemaGrid = null;
		if (fixedWidthGridRow != null) {
			schemaGrid = new SchemaGrid();
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

	private FixedWidthGridRow convertSchemaGridRowToFixedWidthSchema(SchemaGrid schemaGrid) {
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

	

	public String getFromSocketId() {
		return fromSocketId;
	}

	public void setFromSocketId(String fromSocketId) {
		this.fromSocketId = fromSocketId;
	}

	public List<FixedWidthGridRow> getFixedWidthGridRowsOutputFields() {
		return fixedWidthGridRowsOutputFields;
	}

	public List<SchemaGrid> getSchemaGridOutputFields() {
		List<SchemaGrid> schemaGrid = new ArrayList<>();
		for (FixedWidthGridRow fixedWidthGridRow : fixedWidthGridRowsOutputFields) {
			schemaGrid.add(convertFixedWidthSchemaToSchemaGridRow(fixedWidthGridRow));
		}
		return schemaGrid;
	}

	@Override
	public ComponentsOutputSchema clone() {
		return new ComponentsOutputSchema();

	}

	/**
	 * It updates the current schema as per its pass-through fields mapping
	 * 
	 * @param sourceOutputSchema
	 * @param string 
	 */
	public void updatePassthroughFieldsSchema(ComponentsOutputSchema sourceOutputSchema, String port) {
		System.out.println("The port is: " + port);
		FixedWidthGridRow targetFixedWidthGridRow;
		FixedWidthGridRow sourceFixedWidthGridRow;
		for (String fieldName : passthroughFields) {
			targetFixedWidthGridRow = getFixedWidthGridRowForFieldName(fieldName);
			if (targetFixedWidthGridRow != null && sourceOutputSchema!=null) {
				String portName = passThroughFieldsPortInfo.get(fieldName);
				
				sourceFixedWidthGridRow = sourceOutputSchema.getFixedWidthGridRowForFieldName(fieldName);
				
				
				
				if (portName != null && sourceFixedWidthGridRow != null) {
					targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
					targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
					targetFixedWidthGridRow.setLength(sourceFixedWidthGridRow.getLength());
					targetFixedWidthGridRow.setPrecision(sourceFixedWidthGridRow.getPrecision());
					targetFixedWidthGridRow.setScale(sourceFixedWidthGridRow.getScale());
					targetFixedWidthGridRow.setDateFormat(sourceFixedWidthGridRow.getDateFormat());
					targetFixedWidthGridRow.setScaleType(sourceFixedWidthGridRow.getScaleType());
					targetFixedWidthGridRow.setScaleTypeValue(sourceFixedWidthGridRow.getScaleTypeValue());
				}
				
				/*if(portName!=null){
					if (sourceFixedWidthGridRow != null && portName.equalsIgnoreCase(port)) {
						targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
						targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
						targetFixedWidthGridRow.setLength(sourceFixedWidthGridRow.getLength());
						targetFixedWidthGridRow.setPrecision(sourceFixedWidthGridRow.getPrecision());
						targetFixedWidthGridRow.setScale(sourceFixedWidthGridRow.getScale());
						targetFixedWidthGridRow.setDateFormat(sourceFixedWidthGridRow.getDateFormat());
						targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
						targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
					}
				}else{
					if (sourceFixedWidthGridRow != null) {
						targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
						targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
						targetFixedWidthGridRow.setLength(sourceFixedWidthGridRow.getLength());
						targetFixedWidthGridRow.setPrecision(sourceFixedWidthGridRow.getPrecision());
						targetFixedWidthGridRow.setScale(sourceFixedWidthGridRow.getScale());
						targetFixedWidthGridRow.setDateFormat(sourceFixedWidthGridRow.getDateFormat());
						targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
						targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
					}
				}*/
				
				
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
			targetFixedWidthGridRow = getFixedWidthGridRowForFieldName(entry.getValue());
			if (targetFixedWidthGridRow != null && sourceOutputSchema!=null) {
				sourceFixedWidthGridRow = sourceOutputSchema.getFixedWidthGridRowForFieldName(entry.getKey());


				/*String mapFieldSourceFieldName;
				if(entry.getKey().contains(".")){
					mapFieldSourceFieldName = entry.getKey().split("\\.")[1];
				}else{
					mapFieldSourceFieldName = entry.getKey();
				}
				sourceFixedWidthGridRow = sourceOutputSchema.getFixedWidthGridRowForFieldName(mapFieldSourceFieldName);*/
				

				String portName = mapFieldsPortInfo.get(entry.getValue());
				
				if (portName != null && sourceFixedWidthGridRow != null) {
					targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
					targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
					targetFixedWidthGridRow.setLength(sourceFixedWidthGridRow.getLength());
					targetFixedWidthGridRow.setPrecision(sourceFixedWidthGridRow.getPrecision());
					targetFixedWidthGridRow.setScale(sourceFixedWidthGridRow.getScale());
					targetFixedWidthGridRow.setDateFormat(sourceFixedWidthGridRow.getDateFormat());
					targetFixedWidthGridRow.setScaleType(sourceFixedWidthGridRow.getScaleType());
					targetFixedWidthGridRow.setScaleTypeValue(sourceFixedWidthGridRow.getScaleTypeValue());
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

}
