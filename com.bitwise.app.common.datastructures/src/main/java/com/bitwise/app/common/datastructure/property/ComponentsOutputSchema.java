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

	public List<String> getPassthroughFields() {

		return passthroughFields;
	}

	public Map<String, String> getMapFields() {

		return mapFields;
	}
	
	public Map<String, String> getPassthroughFieldsPortInfo() {
		return passThroughFieldsPortInfo;
	}

	/*public void setPassthroughFieldsPortInfo(
			Map<String, String> passthroughFieldsPortInfo) {
		this.passthroughFieldsPortInfo = passthroughFieldsPortInfo;
	}*/

	public Map<String, String> getMapFieldsPortInfo() {
		return mapFieldsPortInfo;
	}

	/*public void setMapFieldsPortInfo(Map<String, String> mapFieldsPortInfo) {
		this.mapFieldsPortInfo = mapFieldsPortInfo;
	}*/

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
	 * Accepts Field-Name and stores it in fixed width format.
	 * 
	 * @param fieldName
	 */
	public void addSchemaFields(String fieldName) {
		this.fixedWidthGridRowsOutputFields.add(createFixedWidthGridRow(fieldName));
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
			schemaGrid.setFieldName(fixedWidthGridRow.getFieldName());
			schemaGrid.setScale(fixedWidthGridRow.getScale());
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
			fixedWidthGridRow.setFieldName(schemaGrid.getFieldName());
			fixedWidthGridRow.setScale(schemaGrid.getScale());
			fixedWidthGridRow.setLength("0");
		}
		return fixedWidthGridRow;
	}

	private FixedWidthGridRow createFixedWidthGridRow(String fieldName) {

		FixedWidthGridRow fixedWidthGridRow = null;
		if (fieldName != null) {
			fixedWidthGridRow = new FixedWidthGridRow();
			fixedWidthGridRow.setFieldName(fieldName);
			fixedWidthGridRow.setDataType(1);
			fixedWidthGridRow.setDataTypeValue("java.lang.String");
			fixedWidthGridRow.setScale("");
			fixedWidthGridRow.setLength("0");
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
			if (targetFixedWidthGridRow != null) {
				String portName = passThroughFieldsPortInfo.get(fieldName);
				//if(targetFixedWidthGridRow.)
				
				sourceFixedWidthGridRow = sourceOutputSchema.getFixedWidthGridRowForFieldName(fieldName);
				
				if(portName!=null){
					if (sourceFixedWidthGridRow != null && portName.equalsIgnoreCase(port)) {
						targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
						targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
						targetFixedWidthGridRow.setLength(sourceFixedWidthGridRow.getLength());
						targetFixedWidthGridRow.setScale(sourceFixedWidthGridRow.getScale());
						targetFixedWidthGridRow.setDateFormat(sourceFixedWidthGridRow.getDateFormat());
					}
				}else{
					if (sourceFixedWidthGridRow != null) {
						targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
						targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
						targetFixedWidthGridRow.setLength(sourceFixedWidthGridRow.getLength());
						targetFixedWidthGridRow.setScale(sourceFixedWidthGridRow.getScale());
						targetFixedWidthGridRow.setDateFormat(sourceFixedWidthGridRow.getDateFormat());
					}
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
			targetFixedWidthGridRow = getFixedWidthGridRowForFieldName(entry.getValue());
			if (targetFixedWidthGridRow != null) {
				sourceFixedWidthGridRow = sourceOutputSchema.getFixedWidthGridRowForFieldName(entry.getKey());
				/*String mapFieldSourceFieldName;
				if(entry.getKey().contains(".")){
					mapFieldSourceFieldName = entry.getKey().split("\\.")[1];
				}else{
					mapFieldSourceFieldName = entry.getKey();
				}
				sourceFixedWidthGridRow = sourceOutputSchema.getFixedWidthGridRowForFieldName(mapFieldSourceFieldName);*/
				String portName = mapFieldsPortInfo.get(entry.getValue());
				if(portName!=null){
					if (sourceFixedWidthGridRow != null && portName.equalsIgnoreCase(port)) {
						targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
						targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
						targetFixedWidthGridRow.setLength(sourceFixedWidthGridRow.getLength());
						targetFixedWidthGridRow.setScale(sourceFixedWidthGridRow.getScale());
						targetFixedWidthGridRow.setDateFormat(sourceFixedWidthGridRow.getDateFormat());
					}
				}else{
					if (sourceFixedWidthGridRow != null) {
						targetFixedWidthGridRow.setDataType(sourceFixedWidthGridRow.getDataType());
						targetFixedWidthGridRow.setDataTypeValue(sourceFixedWidthGridRow.getDataTypeValue());
						targetFixedWidthGridRow.setLength(sourceFixedWidthGridRow.getLength());
						targetFixedWidthGridRow.setScale(sourceFixedWidthGridRow.getScale());
						targetFixedWidthGridRow.setDateFormat(sourceFixedWidthGridRow.getDateFormat());
					}
				}
				
			}
		}
	}
}
