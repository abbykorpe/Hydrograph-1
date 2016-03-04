package com.bitwise.app.common.datastructure.property;

/**
 * This class is used as data structure for GenerateRecords component's Schema Grid
 * 
 * @author Bitwise
 * 
 */
public class GenerateRecordSchemaGridRow extends FixedWidthGridRow {

	private String rangeFrom;
	private String rangeTo;
	private String defaultValue;

	public String getRangeFrom() {
		return rangeFrom;
	}

	public void setRangeFrom(String rangeFrom) {
		this.rangeFrom = rangeFrom;
	}

	public String getRangeTo() {
		return rangeTo;
	}

	public void setRangeTo(String rangeTo) {
		this.rangeTo = rangeTo;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GenerateRecordSchemaGridRow [rangeFrom=" + getRangeFrom() + ", rangeTo=" + getRangeTo()
				+ ", defaultValue=" + getDefaultValue() + ", length=" + getLength() + ", fieldName=" + getFieldName()
				+ ", dateFormat=" + getDateFormat() + ", dataType=" + getDataType() + ", scale=" + getScale()
				+ ", dataTypeValue=" + getDataTypeValue());
		builder.append("]");
		return builder.toString();
	}

	public GridRow copy() {
		GenerateRecordSchemaGridRow tempschemaGrid = new GenerateRecordSchemaGridRow();
		tempschemaGrid.setDataType(getDataType());
		tempschemaGrid.setDateFormat(getDateFormat());
		tempschemaGrid.setPrecision(getPrecision());
		tempschemaGrid.setFieldName(getFieldName());
		tempschemaGrid.setScale(getScale());
		tempschemaGrid.setScaleType(getScaleType());
		tempschemaGrid.setScaleTypeValue(getScaleTypeValue());
		tempschemaGrid.setDataTypeValue(getDataTypeValue());
		tempschemaGrid.setDescription(getDescription());
		tempschemaGrid.setLength(getLength());
		tempschemaGrid.setRangeFrom(rangeFrom);
		tempschemaGrid.setRangeTo(rangeTo);
		tempschemaGrid.setDefaultValue(defaultValue);
		return tempschemaGrid;
	}
}
