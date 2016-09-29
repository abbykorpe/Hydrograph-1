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
/**
 * 
 */
package hydrograph.engine.assembly.entity;

import java.util.List;
import hydrograph.engine.assembly.entity.base.InputOutputEntityBase;
import hydrograph.engine.assembly.entity.elements.InSocket;
import hydrograph.engine.assembly.entity.elements.SchemaField;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;

/**
 * @author bitwise1
 *
 */
public class OutputRDBMSEntity  extends InputOutputEntityBase{
	
	private List<InSocket> inSocketList;
	private List<SchemaField> schemaFieldsList;
	private String databaseName;
	private String tableName;
	private boolean overWrite;
	private String username;
	private String password;
	private String jdbcurl;
	private String query;
	private List<TypeFieldName> primaryKeys;
	private String databaseType;
	private String loadType;
	private Integer batchSize;
	private List<TypeFieldName> updateByKeys;
	/**
	 * @return the inSocketList
	 */
	public List<InSocket> getInSocketList() {
		return inSocketList;
	}
	/**
	 * @param inSocketList the inSocketList to set
	 */
	public void setInSocketList(List<InSocket> inSocketList) {
		this.inSocketList = inSocketList;
	}
	/**
	 * @return the schemaFieldsList
	 */
	public List<SchemaField> getFieldsList() {
		return schemaFieldsList;
	}
	/**
	 * @param schemaFieldsList the schemaFieldsList to set
	 */
	public void setFieldsList(List<SchemaField> schemaFieldsList) {
		this.schemaFieldsList = schemaFieldsList;
	}
	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return databaseName;
	}
	/**
	 * @param databaseName the databaseName to set
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return the overWrite
	 */
	public boolean isOverWrite() {
		return overWrite;
	}
	/**
	 * @param overWrite the overWrite to set
	 */
	public void setOverWrite(boolean overWrite) {
		this.overWrite = overWrite;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the jdbcurl
	 */
	public String getJdbcurl() {
		return jdbcurl;
	}
	/**
	 * @param jdbcurl the jdbcurl to set
	 */
	public void setJdbcurl(String jdbcurl) {
		this.jdbcurl = jdbcurl;
	}
	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}
	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	/**
	 * @return the primaryKeys
	 */
	public List<TypeFieldName> getPrimaryKeys() {
		return primaryKeys;
	}
	/**
	 * @param primaryKeys the primaryKeys to set
	 */
	public void setPrimaryKeys(List<TypeFieldName> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	/**
	 * @return the databaseType
	 */
	public String getDatabaseType() {
		return databaseType;
	}
	/**
	 * @param databaseType the databaseType to set
	 */
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	/**
	 * @return the loadType
	 */
	public String getLoadType() {
		return loadType;
	}
	/**
	 * @param loadType the loadType to set
	 */
	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}
	/**
	 * @return the updateByKeys
	 */
	public List<TypeFieldName> getUpdateByKeys() {
		return updateByKeys;
	}
	/**
	 * @param updateByKeys the updateByKeys to set
	 */
	public void setUpdateByKeys(List<TypeFieldName> updateByKeys) {
		this.updateByKeys = updateByKeys;
	}
	/**
	 * @return the batchSize
	 */
	public Integer getBatchSize() {
		return batchSize;
	}
	/**
	 * @param batchSize the batchSize to set
	 */
	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}
	
	
	
	
	
	
	
}
