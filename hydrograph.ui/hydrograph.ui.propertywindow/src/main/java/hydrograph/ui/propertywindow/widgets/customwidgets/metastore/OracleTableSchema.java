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
package hydrograph.ui.propertywindow.widgets.customwidgets.metastore;

import java.util.ArrayList;
import java.util.List;

/**
 * OracleTableSchema used for mapping with json string.
 * @author Bitwise
 *
 */
public class OracleTableSchema {
	
	private String tableName="";
	private List<OracleTableSchemaField> schemaFields = new ArrayList<OracleTableSchemaField>();
	private String sid="";
	private String hostName="";
	private String portNo="";
	private String oracleSchema="";
	private String userName="";
	private String password="";
	private String databaseType="";
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<OracleTableSchemaField> getSchemaFields() {
		return schemaFields;
	}
	public void setSchemaFields(List<OracleTableSchemaField> schemaFields) {
		this.schemaFields = schemaFields;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getPortNo() {
		return portNo;
	}
	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}
	public String getOracleSchema() {
		return oracleSchema;
	}
	public void setOracleSchema(String oracleSchema) {
		this.oracleSchema = oracleSchema;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatabaseType() {
		return databaseType;
	}
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	
	

}
