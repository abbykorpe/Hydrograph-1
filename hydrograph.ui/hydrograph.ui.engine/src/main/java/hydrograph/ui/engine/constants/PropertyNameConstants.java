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

 
package hydrograph.ui.engine.constants;


public enum PropertyNameConstants {
	
		PATH("Path"),
		IS_SAFE("safe"),
		CHAR_SET("charset"),
		SCHEMA ("schema"),
		DELIMITER("delimiter"),
		RUNTIME_PROPERTIES("runtime_properties"),
		HAS_HEADER("hasHeader"),
		STRICT("strict"),
		OPERATION_CLASS("OPERATION_CLASS"),
		RETENTION_LOGIC_KEEP("retention_logic"),
		OPERATION_FILEDS("operation_fields"),
		SECONDARY_COLUMN_KEYS("Secondary_keys"),
		DEDUP_FILEDS("Key_fields"),
		DATABASE_NAME("databaseName"),
		ORACLE_SID("sid"),
		ORACLE_HOST_NAME("hostName"),
		ORACLE_PORT_NO("port"),
		ORACLE_JDBC_DRIVER("jdbcDriver"),
		ORACLE_TABLE_NAME("oracleTableName"),
		ORACLE_USER_NAME("userName"),
		ORACLE_PASSWORD("password"),
		ORACLE_CHUNK_SIZE("chunkSize"),
		TABLE_NAME("tableName"),
		ORACLE_SCHEMA("oracleSchema"),
		ORACLE_SELECT_OPTION("selectOption"),
		EXTERNAL_TABLE_PATH("externalTablePath"),
		LOAD_TYPE_CONFIGURATION("load_type_configuration"),
		PARTITION_KEYS("partitionKeys"),
		QUOTE("quote"),
		REDSHIFT_JDBC_DRIVER("jdbcDriver"),
		REDSHIFT_HOST_NAME("hostName"),
		REDSHIFT_PORT_NAME("port"),
		REDSHIFT_PASSWORD("password"),
		SELECT_OPTION("selectOption"),
		REDSHIFT_USER_NAME("userName"),
		REDSHIFT_TABLE_NAME("tableName"), 
		REDSHIFT_SQL_QUERY("selectQuery"),
		REDSHIFT_COUNT_QUERY("countQuery"), 
		REDSHIFT_CHUNK_SIZE("chunkSize"),
		OVER_WRITE("overWrite");
	
		private final String value;

	PropertyNameConstants(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static PropertyNameConstants fromValue(String value) {
		for (PropertyNameConstants propertyNameConstant : PropertyNameConstants.values()) {
			if (propertyNameConstant.value.equals(value)) {
				return propertyNameConstant;
			}
		}
		return null;
	}
	
	
}
