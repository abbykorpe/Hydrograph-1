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
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.server.debug.utilities.Constants;
import hydrograph.server.metadata.exception.ParamsCannotBeNullOrEmpty;
import hydrograph.server.metadata.schema.TableSchema;
import hydrograph.server.metadata.schema.TableSchemaField;
import hydrograph.server.metadata.type.base.DataBaseType;

/**
 * Concrete implementation for Oracle database and getting the table schema
 * parameters.
 * 
 * @author amiyam
 *
 */
public class OracleDatabaseType implements DataBaseType {
	Logger LOG = LoggerFactory.getLogger(OracleDatabaseType.class);
	final static String ORACLE_JDBC_CLASSNAME = "oracle.jdbc.OracleDriver";
	Connection connection = null;

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * 
	 * 
	 */
	@Override
	public void setConnection(String userId, String password, String host, String port, String sid, String driverType,
			String database, String tableName)
					throws ParamsCannotBeNullOrEmpty, JSONException, ClassNotFoundException, SQLException {
		String jdbcUrl = "jdbc:oracle:" + driverType + "://@" + host + ":" + port + ":" + sid;
		checkNullParams(new String[] { userId, password, host, port, sid, driverType });
		Class.forName(ORACLE_JDBC_CLASSNAME);
		connection = DriverManager.getConnection(jdbcUrl, userId, password);
	}

	/**
	 * Checks the presence of null or empty in field parameter.
	 * 
	 * @param params
	 *            - Of type String[]
	 * @throws ParamsCannotBeNullOrEmpty
	 *             - Throws when the parameter supplied is null or empty.
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
	 * @throws ParamsCannotBeNullOrEmpty
	 */
	@Override
	public TableSchema fillComponentSchema(String query, String tableName, String database)
			throws JSONException, SQLException, ParamsCannotBeNullOrEmpty {
		ResultSet res = null;
		TableSchema tableSchema = new TableSchema();
		List<TableSchemaField> tableSchemaFields = new ArrayList<TableSchemaField>();
		try {
			Statement stmt = connection.createStatement();
			// checks the query or table name . if tableQuery present it will
			// execute the query otherwise selected table name query will be
			// executed.
			if (query != null && !query.isEmpty())
				res = stmt.executeQuery("Select * from (" + query + ") WHERE ROWNUM < 0");
			else if (tableName != null && !tableName.isEmpty())
				res = stmt.executeQuery("Select * from " + tableName + " where 1<0");
			else {
				LOG.error("Table or query in request parameter cannot be null or empty " + Constants.QUERY + " => " + query + " "
						+ Constants.TABLENAME + " => " + tableName + " ");
				throw new ParamsCannotBeNullOrEmpty("Table and query cannot be null or empty in request parameters: "
						+ Constants.QUERY + " => " + query + " " + Constants.TABLENAME + " => " + tableName + " ");
			}

			ResultSetMetaData rsmd = res.getMetaData();
			for (int count = 1; count < rsmd.getColumnCount() + 1; count++) {
				TableSchemaField tableSchemaField = new TableSchemaField();
				tableSchemaField.setFieldName(rsmd.getColumnLabel(count));
				if (rsmd.getColumnClassName(count).equalsIgnoreCase("java.sql.Timestamp")) {
					tableSchemaField.setFormat("yyyy-MM-dd HH:mm:ss");
					tableSchemaField.setFieldType("java.util.Date");
				} else {
					tableSchemaField.setFieldType(rsmd.getColumnClassName(count));
				}
				tableSchemaField.setPrecision(String.valueOf(rsmd.getPrecision(count)));
				tableSchemaField.setScale(String.valueOf(rsmd.getScale(count)));
				tableSchemaFields.add(tableSchemaField);
			}
			tableSchema.setSchemaFields(tableSchemaFields);
		} finally {
			res.close();
			connection.close();
		}
		return tableSchema;
	}

}
