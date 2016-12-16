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
package hydrograph.server.metadata.helper.base;

import java.sql.SQLException;

import hydrograph.server.metadata.entity.TableEntity;
import org.json.JSONException;

import hydrograph.server.metadata.exception.ParamsCannotBeNullOrEmpty;

/**
 * Interface is used to get the database connection and table entity.
 * 
 * @author amiya
 *
 */
public interface MetadataHelperBase {
	/**
	 * 
	 * Used to set the database specific connection object.
	 * 
	 * @param userId - Username of database
	 * @param password - Password of user
	 * @param host - host name 
	 * @param port - port to connect
	 * @param sid - Service identifier
	 * @param driverType
	 * @param database - database to connect
	 * @param tableName - table name
	 * @throws ParamsCannotBeNullOrEmpty - throws when supplied parameters are null or empty
	 * @throws JSONException - {@linkplain JSONException}
	 * @throws ClassNotFoundException - {@linkplain ClassNotFoundException}
	 * @throws SQLException - {@linkplain SQLException}
	 */
	void setConnection(String userId,String password,String host,String port,String sid,String driverType,String database,String tableName) throws ParamsCannotBeNullOrEmpty, JSONException, ClassNotFoundException, SQLException;

	/**
	 * Gets the database specific table entity. Which contains meta data details of table columns.
	 * 
	 * @param query - query to execute
	 * @param tableName - query to execute with tablename only
	 * @param database - database need to connect
	 * @return
	 * @throws JSONException - {@linkplain JSONException}
	 * @throws SQLException - {@linkplain SQLException}
	 * @throws ParamsCannotBeNullOrEmpty - throws when supplied parameters are null or empty
	 */
	TableEntity fillComponentSchema(String query, String tableName, String database)
			throws JSONException, SQLException, ParamsCannotBeNullOrEmpty;

}
