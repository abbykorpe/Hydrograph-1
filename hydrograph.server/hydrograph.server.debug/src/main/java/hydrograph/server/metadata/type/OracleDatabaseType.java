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
package hydrograph.server.metadata.type;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.server.debug.utilities.Constants;
import hydrograph.server.metadata.exception.ParamsCannotBeNullOrEmpty;
import hydrograph.server.metadata.exception.TableOrQueryParamNotFound;
import hydrograph.server.metadata.schema.TableSchema;
import hydrograph.server.metadata.schema.TableSchemaField;
import spark.Request;

/**
 * Concrete implementation for Oracle database and getting the table schema
 * parameters.
 * 
 * @author amiyam
 *
 */
public class OracleDatabaseType implements DataBaseType {
	Logger LOG = LoggerFactory.getLogger(OracleDatabaseType.class);
	final static String jdbcClassName = "oracle.jdbc.OracleDriver";
	Connection connection = null;
	JSONObject jsonObject = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getConnection(Request request) throws ParamsCannotBeNullOrEmpty, JSONException {
		jsonObject = new JSONObject(request.queryParams(Constants.JSON));
		String userId = jsonObject.getString(Constants.userName);
		String passwd = jsonObject.getString(Constants.PASSWORD);
		String host = jsonObject.getString(Constants.hostName);
		String port = jsonObject.getString(Constants.portNo);
		String sid = jsonObject.getString(Constants.SID);
		String drivername = jsonObject.getString(Constants.driverType);
		String jdbcurl = "jdbc:oracle:" + drivername + "://@" + host + ":" + port + ":" + sid;

		try {
			checkNullParams(new String[] { userId, passwd, host, port, sid, drivername });
			Class.forName(jdbcClassName);
			connection = DriverManager.getConnection(jdbcurl, userId, passwd);
		} catch (ClassNotFoundException e) {
			LOG.error("No defination for the specified name found " + e);
		} catch (SQLException e) {
			LOG.error("Unable to access the database : " + e);
		}
	}

	/**
	 * Checks the presence of null or empty in field parameter.
	 * 
	 * @param params
	 *            - of type String[]
	 * @throws ParamsCannotBeNullOrEmpty
	 *             - throws when the parameter supplied is null or empty.
	 */
	private void checkNullParams(String[] params) throws ParamsCannotBeNullOrEmpty {
		for (String eachParam : params) {
			if (eachParam == null) {
				throw new ParamsCannotBeNullOrEmpty("Value for Parameter " + eachParam + " can not be null.");
			} else if (eachParam.isEmpty()) {
				throw new ParamsCannotBeNullOrEmpty("Value for Parameter " + eachParam + " can not be empty.");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SQLException
	 */
	@Override
	public TableSchema fillComponentSchema(Request request)
			throws JSONException, TableOrQueryParamNotFound, SQLException {
		ResultSet res = null;
		String tableQuery = null;
		String table = null;
		Iterator<?> keys = jsonObject.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			if (key.equalsIgnoreCase(Constants.query)) {
				tableQuery = jsonObject.getString(Constants.query);
			} else if (key.equalsIgnoreCase(Constants.tableName)) {
				table = jsonObject.getString(Constants.tableName);
			}
		}

		TableSchema tableSchema = new TableSchema();
		List<TableSchemaField> tableSchemaFields = new ArrayList<TableSchemaField>();
		try {
			Statement stmt = connection.createStatement();
			// checks the query or table name . if tableQuery present it will
			// execute the query otherwise selected table name query will be
			// executed.
			if (tableQuery != null)
				res = stmt.executeQuery("Select * from ("+ tableQuery + ") WHERE ROWNUM < 0");
			else
				res = stmt.executeQuery("Select * from " + table + " where 1<0");
			ResultSetMetaData rsmd = res.getMetaData();
			for (int count = 1; count < rsmd.getColumnCount() + 1; count++) {
				TableSchemaField tableSchemaField = new TableSchemaField();
				tableSchemaField.setFieldName(rsmd.getColumnLabel(count));
				tableSchemaField.setFieldType(rsmd.getColumnTypeName(count));
				if (rsmd.getColumnClassName(count).equalsIgnoreCase("java.sql.Timestamp")) {
					tableSchemaField.setFormat("yyyy-MM-dd HH:mm:ss");
					tableSchemaField.setFieldType("java.util.Date");
				} else {
					tableSchemaField.setFieldType(rsmd.getColumnClassName(count));
				}
				tableSchemaField.setFieldType(rsmd.getColumnTypeName(count));
				tableSchemaField.setFieldType(rsmd.getColumnClassName(count));
				tableSchemaField.setPrecision(String.valueOf(rsmd.getPrecision(count)));
				tableSchemaField.setScale(String.valueOf(rsmd.getScale(count)));
				tableSchemaFields.add(tableSchemaField);
			}
			tableSchema.setSchemaFields(tableSchemaFields);
			return tableSchema;
		} catch (SQLException e) {
			LOG.error("Unale to execute the query : " + e);
		} finally {
			res.close();
			connection.close();
		}
		return tableSchema;
	}

}
