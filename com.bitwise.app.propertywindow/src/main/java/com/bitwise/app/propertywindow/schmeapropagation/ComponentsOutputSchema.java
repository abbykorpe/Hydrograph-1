package com.bitwise.app.propertywindow.schmeapropagation;

import java.util.ArrayList;
import java.util.List;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.SchemaGrid;

public class ComponentsOutputSchema {

	private String fromSocketId;
	private List<FixedWidthGridRow> fixedWidthGridRowsOutputFields = new ArrayList<>();

	public String getFromSocketId() {
		return fromSocketId;
	}

	public void setFromSocketId(String fromSocketId) {
		this.fromSocketId = fromSocketId;
	}

	public List<FixedWidthGridRow> getFixedWidthGridRowsOutputFields() {
		return fixedWidthGridRowsOutputFields;
	}

	public void addSchemaFields(GridRow gridRow) {
		if (gridRow instanceof FixedWidthGridRow) {
			this.fixedWidthGridRowsOutputFields.add((FixedWidthGridRow) gridRow);
		} else if (gridRow instanceof SchemaGrid) {
			this.fixedWidthGridRowsOutputFields.add(convertSchemaGridRowToFixedWidthSchema((SchemaGrid) gridRow));
		}

	}


	public void addSchemaFields(String fieldName) {
		this.fixedWidthGridRowsOutputFields.add(createFixedWidthGridRow(fieldName));
	}

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

	private SchemaGrid createSchemaGridRow(String fieldName) {
		SchemaGrid schemaGrid = null;
		if (fieldName != null) {
			schemaGrid = new SchemaGrid();
			schemaGrid.setFieldName(fieldName);
			schemaGrid.setDataType(1);
			schemaGrid.setDataTypeValue("java.lang.String");
			schemaGrid.setScale("");
		}
		return schemaGrid;
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

}
