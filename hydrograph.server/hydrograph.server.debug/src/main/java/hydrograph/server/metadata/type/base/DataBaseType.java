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
package hydrograph.server.metadata.type.base;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import hydrograph.server.metadata.exception.ParamsCannotBeNullOrEmpty;
import hydrograph.server.metadata.exception.TableOrQueryParamNotFound;
import hydrograph.server.metadata.schema.TableSchema;
import spark.Request;

/**
 * Interface is used to get the database connection and table schema.
 * 
 * @author amiya
 *
 */
public interface DataBaseType {
	/**
	 * Used to get the database specific connection object.
	 * 
	 * @param request
	 *            - of type Request
	 * @throws ParamsCannotBeNullOrEmpty
	 *             - throws when parameter passes through JSON if null or empty.
	 * @throws JSONException
	 *             - throws due to unable to parse the JSON at runtime.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	void setConnection(String userId,String password,String host,String port,String sid,String driverType,String database,String tableName) throws ParamsCannotBeNullOrEmpty, JSONException, ClassNotFoundException, SQLException;

	/**
	 * Gets the database specific table schema. Which contains meta data details
	 * of table columns.
	 * 
	 * @param request
	 *            - of type {@link Request}
	 * @return schema object of type - {@link TableSchema}
	 * @throws JSONException
	 *             - throws due to unable to parse the JSON at runtime.
	 * @throws TableOrQueryParamNotFound
	 *             - Exception throws if one of the parameter i.e table or query
	 *             not found.
	 * @throws SQLException
	 *             - Error while fetching data
	 * @throws ParamsCannotBeNullOrEmpty 
	 */
	TableSchema fillComponentSchema(String query,String tableName,String database)
			throws JSONException, SQLException, ParamsCannotBeNullOrEmpty;

}
