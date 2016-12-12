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

package hydrograph.server.metadata.schema;

/**
 * 
 * POJO Stores all the hive table related fields.
 *
 */
public class HiveTableSchemaField {
	String fieldName = "";
	String fieldType = "";
	String precision = "";
	String scale = "";
	String format = "";

	/**
	 * 
	 * @return fieldName - of type String
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 
	 * @param fieldName
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 
	 * @return fieldType - of type String
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * 
	 * @param fieldType
	 *            - of type String
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * 
	 * @return precision - of type String
	 */
	public String getPrecision() {
		return precision;
	}

	/**
	 * 
	 * @param precision
	 *            - of type String
	 */
	public void setPrecision(String precision) {
		this.precision = precision;
	}

	/**
	 * 
	 * @return scale - of type String
	 */
	public String getScale() {
		return scale;
	}

	/**
	 * 
	 * @param scale
	 *            - of type String
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}

	/**
	 * 
	 * @return format - of type String
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 
	 * @param format
	 *            - of type String
	 */
	public void setFormat(String format) {
		this.format = format;
	}

}