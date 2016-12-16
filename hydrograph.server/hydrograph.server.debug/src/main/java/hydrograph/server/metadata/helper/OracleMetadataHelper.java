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
package hydrograph.server.metadata.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hydrograph.server.metadata.entity.TableEntity;
import hydrograph.server.metadata.entity.TableSchemaFieldEntity;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.server.debug.utilities.Constants;
import hydrograph.server.metadata.exception.ParamsCannotBeNullOrEmpty;
import hydrograph.server.metadata.helper.base.MetadataHelperBase;

/**
 * Concrete implementation for Oracle database and getting the table entity
 * parameters.
 *
 * @author amiyam
 */
public class OracleMetadataHelper implements MetadataHelperBase {
    Logger LOG = LoggerFactory.getLogger(OracleMetadataHelper.class);
    final static String ORACLE_JDBC_CLASSNAME = "oracle.jdbc.OracleDriver";
    Connection connection = null;

    /**
     * {@inheritDoc}
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Override
    public void setConnection(String userId, String password, String host, String port, String sid, String driverType,
                              String database, String tableName)
            throws ParamsCannotBeNullOrEmpty, JSONException, ClassNotFoundException, SQLException {
        String jdbcUrl = "jdbc:oracle:" + driverType + "://@" + host + ":" + port + ":" + sid;
        checkNullParams(userId, password, host, port, sid, driverType);
        Class.forName(ORACLE_JDBC_CLASSNAME);
        connection = DriverManager.getConnection(jdbcUrl, userId, password);
    }

    /**
     * Checks the presence of null or empty in field parameter.
     * @param userId
     * @param password
     * @param host
     * @param port
     * @param sid
     * @param driverType
     * @throws ParamsCannotBeNullOrEmpty throws when the parameter supplied is null or empty.
     */
    private void checkNullParams(String userId, String password, String host, String port, String sid, String driverType) throws ParamsCannotBeNullOrEmpty {
        if (userId == null || userId.isEmpty()) {
            throw new ParamsCannotBeNullOrEmpty("UserID can not be null or empty.");
        }
        if (password == null || password.isEmpty()) {
            throw new ParamsCannotBeNullOrEmpty("Password can not be null or empty.");
        }
        if (host == null || host.isEmpty()) {
            throw new ParamsCannotBeNullOrEmpty("Host can not be null or empty.");
        }
        if (port == null || port.isEmpty()) {
            throw new ParamsCannotBeNullOrEmpty("Port can not be null or empty.");
        }
        if (sid == null || sid.isEmpty()) {
            throw new ParamsCannotBeNullOrEmpty("SID can not be null or empty.");
        }
        if (driverType == null || driverType.isEmpty()) {
            throw new ParamsCannotBeNullOrEmpty("Driver helper can not be null or empty.");
        }
    }


    /**
     * {@inheritDoc}
     *
     * @throws SQLException
     * @throws ParamsCannotBeNullOrEmpty
     */
    @Override
    public TableEntity fillComponentSchema(String query, String tableName, String database)
            throws JSONException, SQLException, ParamsCannotBeNullOrEmpty {
        ResultSet res = null;
        TableEntity tableEntity = new TableEntity();
        List<TableSchemaFieldEntity> tableSchemaFieldEntities = new ArrayList<TableSchemaFieldEntity>();
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
                TableSchemaFieldEntity tableSchemaFieldEntity = new TableSchemaFieldEntity();
                tableSchemaFieldEntity.setFieldName(rsmd.getColumnLabel(count));
                if (rsmd.getColumnClassName(count).equalsIgnoreCase("java.sql.Timestamp")) {
                    tableSchemaFieldEntity.setFormat("yyyy-MM-dd HH:mm:ss");
                    tableSchemaFieldEntity.setFieldType("java.util.Date");
                } else {
                    tableSchemaFieldEntity.setFieldType(rsmd.getColumnClassName(count));
                }
                tableSchemaFieldEntity.setPrecision(String.valueOf(rsmd.getPrecision(count)));
                tableSchemaFieldEntity.setScale(String.valueOf(rsmd.getScale(count)));
                tableSchemaFieldEntities.add(tableSchemaFieldEntity);
            }
            tableEntity.setSchemaFields(tableSchemaFieldEntities);
        } finally {
            res.close();
            connection.close();
        }
        return tableEntity;
    }
}