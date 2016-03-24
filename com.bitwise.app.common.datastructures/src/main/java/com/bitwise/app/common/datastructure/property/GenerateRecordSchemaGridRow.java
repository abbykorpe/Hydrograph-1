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
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof GenerateRecordSchemaGridRow) {
			GenerateRecordSchemaGridRow generateRecordSchemaGridRow = (GenerateRecordSchemaGridRow) obj;
			
			if ( generateRecordSchemaGridRow.getFieldName().equals(this.getFieldName()) &&
					generateRecordSchemaGridRow.getDataType().equals(this.getDataType()) &&
					generateRecordSchemaGridRow.getDataTypeValue().equals(this.getDataTypeValue()) &&
					generateRecordSchemaGridRow.getDateFormat().equals( this.getDateFormat()) &&
					generateRecordSchemaGridRow.getScaleType().equals(this.getScaleType()) &&
					generateRecordSchemaGridRow.getScaleTypeValue().equals(this.getScaleTypeValue()) &&
					generateRecordSchemaGridRow.getPrecision().equals(this.getPrecision())	&&
					generateRecordSchemaGridRow.getDescription().equals(this.getDescription()) &&
					generateRecordSchemaGridRow.getLength().equals(this.getLength()) &&
					generateRecordSchemaGridRow.getRangeFrom().equals(this.getRangeFrom()) &&
					generateRecordSchemaGridRow.getRangeTo().equals(this.getRangeTo()) &&
					generateRecordSchemaGridRow.getDefaultValue().equals(this.getDefaultValue())
				)
				return true;
		}
		return false;
		
	}
}
