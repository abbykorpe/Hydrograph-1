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


/**
 * The Class FixedWidthGridRow.
 * 
 * @author Bitwise
 */
public class FixedWidthGridRow extends GridRow{
	private String length;
	protected String delimiter;

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
	
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
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
		tempschemaGrid.setDataTypeValue(getDataTypeValue());
		tempschemaGrid.setDateFormat(getDateFormat());
		tempschemaGrid.setPrecision(getPrecision());
		tempschemaGrid.setFieldName(getFieldName());
		tempschemaGrid.setScale(getScale());
		tempschemaGrid.setScaleType(getScaleType());
		tempschemaGrid.setScaleTypeValue(getScaleTypeValue());
		tempschemaGrid.setDescription(getDescription());
		tempschemaGrid.setLength(length);
		
		return tempschemaGrid;
	}
		
}
