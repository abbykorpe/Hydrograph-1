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
package hydrograph.engine.assembly.entity.base;

/**
 * @author bitwise1
 *
 */
public class HiveEntityBase extends InputOutputEntityBase {
	private String[] partitionKeys;
	protected String databaseName;
	protected String tableName;
	private String externalTablePathUri;
	private boolean overWrite;
	private String partitionFilterRegex;

	/**
	 * @return the partitionKeys
	 */
	public String[] getPartitionKeys() {
		return partitionKeys;
	}

	/**
	 * @param partitionKeys
	 *            the partitionKeys to set
	 */
	public void setPartitionKeys(String[] partitionKeys) {
		this.partitionKeys = partitionKeys;
	}

	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * @param databaseName
	 *            the databaseName to set
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
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the externalTablePathUri
	 */
	public String getExternalTablePathUri() {
		return externalTablePathUri;
	}

	/**
	 * @param externalTablePathUri
	 *            the externalTablePathUri to set
	 */
	public void setExternalTablePathUri(String externalTablePathUri) {
		this.externalTablePathUri = externalTablePathUri;
	}

	/**
	 * @return the overWrite
	 */
	public boolean getOverWrite() {
		return overWrite;
	}

	/**
	 * @param overWrite
	 *            the overWrite to set
	 */
	public void setOverWrite(boolean overWrite) {
		this.overWrite = overWrite;
	}

	/**
	 * @return the partitionFilterRegex
	 */
	public String getPartitionFilterRegex() {
		return partitionFilterRegex;
	}

	/**
	 * @param partitionFilterRegex
	 *            the partitionFilterRegex to set
	 */
	public void setPartitionFilterRegex(String partitionFilterRegex) {
		this.partitionFilterRegex = partitionFilterRegex;
	}

}
