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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Simple POJO to store hive related field values.
 *
 */
public class HiveTableSchema {

	String databaseName = "";
	String tableName = "";
	String owner = "";
	List<HiveTableSchemaField> schemaFields = new ArrayList<HiveTableSchemaField>();
	String location = "";
	String externalTableLocation = "";
	String fieldDelimiter = "";
	String partitionKeys = "";
	String inputOutputFormat = "";

	// private int errorCode;
	/**
	 * 
	 * @return external table location - of type String
	 */
	public String getExternalTableLocation() {
		return externalTableLocation;
	}

	/**
	 * 
	 * @param externalTableLocation
	 *            - of type String
	 */
	public void setExternalTableLocation(String externalTableLocation) {
		this.externalTableLocation = externalTableLocation;
	}

	/**
	 * 
	 * @param databaseName
	 *            - of type String
	 * @param tableName
	 *            - of type String
	 */
	public HiveTableSchema(String databaseName, String tableName) {
		this.databaseName = databaseName;
		this.tableName = tableName;
	}

	/**
	 * 
	 * @return databseName - of type String
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * 
	 * @return tableNmae - of type String
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 
	 * @return owner - of type String
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * 
	 * @param owner
	 *            - of type String
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * 
	 * @return schemaField - of type List&lt;HiveTableSchemaField&gt; object
	 */
	public List<HiveTableSchemaField> getSchemaFields() {
		return schemaFields;
	}
	/**
	 * 
	 * @param listOfHiveTableSchemaField - of type List&lt;HiveTableSchemaField&gt; object
	 */
	public void setSchemaFields(List<HiveTableSchemaField> listOfHiveTableSchemaField) {
		this.schemaFields = listOfHiveTableSchemaField;
	}
	/**
	 * 
	 * @return location - of type String
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * 
	 * @param location - of type String
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * 
	 * @return partitionKeys - of type String
	 */
	public String getPartitionKeys() {
		return partitionKeys;
	}
	/**
	 *  
	 * @param partitionKeys - of type String
	 */
	public void setPartitionKeys(String partitionKeys) {
		this.partitionKeys = partitionKeys;
	}
	/**
	 * 
	 * @return inputOutputFormat - of type String
	 */
	public String getInputOutputFormat() {
		return inputOutputFormat;
	}
	/**
	 * 
	 * @param inputOutputFormat - of type String
	 */
	public void setInputOutputFormat(String inputOutputFormat) {
		this.inputOutputFormat = inputOutputFormat;
	}
	/**
	 * 
	 * @return fieldDelimiter - of type String
	 */
	public String getFieldDelimiter() {
		return fieldDelimiter;
	}
	/**
	 * 
	 * @param fieldDelimiter - of type String
	 */
	public void setFieldDelimiter(String fieldDelimiter) {
		this.fieldDelimiter = fieldDelimiter;
	}

}
