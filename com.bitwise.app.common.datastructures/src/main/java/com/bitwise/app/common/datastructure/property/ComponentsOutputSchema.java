package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.SchemaGrid;

/**
 * This class holds output schema of each component in FixedWidth format.
 * 
 * @author Bitwise
 * 
 */
public class ComponentsOutputSchema {

	private String fromSocketId;
	private List<FixedWidthGridRow> fixedWidthGridRowsOutputFields = new ArrayList<>();

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
}
