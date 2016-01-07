package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import com.bitwise.app.common.datastructure.property.GridRow;



/**
 * The Class SchemaGrid.
 * 
 * @author Bitwise
 */
public class SchemaGrid extends GridRow{

	public SchemaGrid copy() {
		SchemaGrid tempschemaGrid = new SchemaGrid();
		tempschemaGrid.setDataType(getDataType());
		tempschemaGrid.setDateFormat(getDateFormat());
		tempschemaGrid.setFieldName(getFieldName());
		tempschemaGrid.setScale(getScale());
		tempschemaGrid.setDataTypeValue(getDataTypeValue());
		return tempschemaGrid;
	}
}