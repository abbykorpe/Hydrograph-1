package com.bitwise.app.common.datastructure.property;

/**
 * The Class FixedWidthGridRow.
 * 
 * @author Bitwise
 */
public class FixedWidthGridRow extends GridRow{
	private String length;

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FixedWidthGridRow [length=");
		builder.append(length);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}
	
	public GridRow copy() {
		FixedWidthGridRow tempschemaGrid = new FixedWidthGridRow();
		tempschemaGrid.setDataType(getDataType());
		tempschemaGrid.setDateFormat(getDateFormat());
		tempschemaGrid.setFieldName(getFieldName());
		tempschemaGrid.setScale(getScale());
		tempschemaGrid.setDataTypeValue(getDataTypeValue());
		tempschemaGrid.setLength(length);
		return tempschemaGrid;
	}
	
	
}
