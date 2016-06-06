/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.engine.assembly.entity.elements;

import hydrograph.engine.utilities.Constants;

import java.lang.reflect.Type;

/**
 * This is a POJO which holds the information for one field of any Input and
 * Output components like
 * {@link hydrograph.engine.cascading.assembly.InputFileDelimitedAssembly
 * InputFile} ,
 * {@link hydrograph.engine.cascading.assembly.OutputFileFixedWidthAssembly
 * OutputFile} etc. The object of this class is supposed to be used in the
 * entity classes for the Input/Output type components
 * 
 * @author gurdits
 *
 */
public class SchemaField implements Cloneable {

	private String fieldName;
	private String fieldDataType;
	private int fieldLength;
	private String fieldFormat;
	private int fieldScale = Constants.DEFAULT_SCALE;
	private String fieldScaleType;
	private int fieldPrecision = Constants.DEFAULT_PRECISION;
	private String fieldLengthDelimiter;
	private Type typeFieldLengthDelimiter;
	private String fieldDefaultValue;
	private String fieldToRangeValue;
	private String fieldFromRangeValue;

	public SchemaField(String fieldName, String fieldDataType) {
		this.fieldName = fieldName;
		this.fieldDataType = fieldDataType;
	}

	/**
	 * @param fieldName
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @return the fieldDataType
	 */
	public String getFieldDataType() {
		return fieldDataType;
	}

	/**
	 * @return the fieldLength
	 */
	public int getFieldLength() {
		return fieldLength;
	}

	/**
	 * @param fieldLength
	 *            the fieldLength to set
	 */
	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

	/**
	 * @return the fieldFormat
	 */
	public String getFieldFormat() {
		return fieldFormat;
	}

	/**
	 * @param fieldFormat
	 *            the fieldFormat to set
	 */
	public void setFieldFormat(String fieldFormat) {
		this.fieldFormat = fieldFormat;
	}

	/**
	 * @return the fieldScale
	 */
	public int getFieldScale() {
		return fieldScale;
	}

	/**
	 * @param fieldScale
	 *            the fieldScale to set
	 */
	public void setFieldScale(int fieldScale) {
		this.fieldScale = fieldScale;
	}

	/**
	 * @return the fieldScaleType
	 */
	public String getFieldScaleType() {
		return fieldScaleType;
	}

	/**
	 * @param fieldScaleType
	 *            the fieldScaleType to set
	 */
	public void setFieldScaleType(String fieldScaleType) {
		this.fieldScaleType = fieldScaleType;
	}

	/**
	 * @param fieldPrecision
	 *            the fieldPrecision to set
	 */
	public void setFieldPrecision(Integer precision) {
		this.fieldPrecision = precision;
	}

	/**
	 * @return the fieldPrecision
	 */
	public int getFieldPrecision() {
		return fieldPrecision;
	}

	/**
	 * @return the fieldDefaultValue
	 */
	public String getFieldDefaultValue() {
		return fieldDefaultValue;
	}

	/**
	 * @param fieldDefaultValue
	 *            the fieldDefaultValue to set
	 */
	public void setFieldDefaultValue(String fieldDefaultValue) {
		this.fieldDefaultValue = fieldDefaultValue;
	}

	/**
	 * @return the fieldToRangeValue
	 */
	public String getFieldToRangeValue() {
		return fieldToRangeValue;
	}

	/**
	 * @param fieldToRangeValue
	 *            the fieldToRangeValue to set
	 */
	public void setFieldToRangeValue(String fieldToRangeValue) {
		this.fieldToRangeValue = fieldToRangeValue;
	}

	/**
	 * @return the fieldFromRangeValue
	 */
	public String getFieldFromRangeValue() {
		return fieldFromRangeValue;
	}

	/**
	 * @param fieldFromRangeValue
	 *            the fieldFromRangeValue to set
	 */
	public void setFieldFromRangeValue(String fieldFromRangeValue) {
		this.fieldFromRangeValue = fieldFromRangeValue;
	}

	/**
	 * @return the fieldLengthDelimiter
	 */
	public String getFieldLengthDelimiter() {
		return fieldLengthDelimiter;
	}

	/**
	 * @param fieldLengthDelimiter
	 *            the fieldLengthDelimiter to set
	 */
	public void setFieldLengthDelimiter(String fieldLengthDelimiter) {
		this.fieldLengthDelimiter = fieldLengthDelimiter;
	}

	/**
	 * @return the typeFieldLengthDelimiter
	 */
	public Type getTypeFieldLengthDelimiter() {
		return typeFieldLengthDelimiter;
	}

	/**
	 * @param typeFieldLengthDelimiter
	 *            the typeFieldLengthDelimiter to set
	 */
	public void setTypeFieldLengthDelimiter(Type typeFieldLengthDelimiter) {
		this.typeFieldLengthDelimiter = typeFieldLengthDelimiter;
	}

	public String toString() {
		StringBuilder str = new StringBuilder("schema field: ");
		str.append("name: " + fieldName);
		str.append(" | data type: " + fieldDataType);
		str.append(" | length: " + fieldLength);
		str.append(" | format: " + fieldFormat);
		str.append(" | scale: " + fieldScale);
		str.append(" | scale type: " + fieldScaleType);
		str.append(" | precision: " + fieldPrecision);
		str.append(" | mixed scheme length / delimiter: "
				+ fieldLengthDelimiter);
		str.append(" | mixed scheme length / delimiter type: "
				+ typeFieldLengthDelimiter);
		str.append(" | default value: " + fieldDefaultValue);
		str.append(" | range from: " + fieldFromRangeValue);
		str.append(" | range to: " + fieldToRangeValue);

		return str.toString();
	}

	public SchemaField clone() {
		try {
			return (SchemaField) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}