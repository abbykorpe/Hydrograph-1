package hydrograph.server.metadata.strategy;

import hydrograph.server.utilities.Constants;
import hydrograph.server.metadata.entity.TableEntity;
import hydrograph.server.metadata.entity.TableSchemaFieldEntity;
import hydrograph.server.metadata.exception.ParamsCannotBeNullOrEmpty;
import hydrograph.server.metadata.strategy.base.MetadataStrategyTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by santlalg on 12/22/2016.
 */
public class MysqlMetadataStrategy  extends MetadataStrategyTemplate {

    Logger LOG = LoggerFactory.getLogger(MysqlMetadataStrategy.class);
    final static String MYSQL_JDBC_CLASSNAME = "com.mysql.jdbc.Driver";
    Connection connection = null;
    private String query=null, tableName=null;

    /**
     * Used to set the connection for Mysql
     *
     * @param connectionProperties
     *            - contain request params details
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setConnection(Map connectionProperties) throws ClassNotFoundException, SQLException {
        String host = connectionProperties
                .getOrDefault(Constants.HOST_NAME,
                        new ParamsCannotBeNullOrEmpty(Constants.HOST_NAME + " not found in request parameter"))
                .toString();
        String port = connectionProperties
                .getOrDefault(Constants.PORT_NUMBER,
                        new ParamsCannotBeNullOrEmpty(Constants.PORT_NUMBER + " not found in request parameter"))
                .toString();
        String userId = connectionProperties
                .getOrDefault(Constants.USERNAME,
                        new ParamsCannotBeNullOrEmpty(Constants.USERNAME + " not found in request parameter"))
                .toString();
        String password = connectionProperties
                .getOrDefault(Constants.PASSWORD,
                        new ParamsCannotBeNullOrEmpty(Constants.PASSWORD + " not found in request parameter"))
                .toString();
        String database = connectionProperties
                .getOrDefault(Constants.DATABASE_NAME,
                        new ParamsCannotBeNullOrEmpty(Constants.DATABASE_NAME + " not found in request parameter"))
                .toString();
        String jdbcurl = "jdbc:mysql://" + host + ":" + port + "/" + database;
        LOG.info("Connection url for mysql = '" + jdbcurl + "'");
        LOG.info("Connecting with '" + userId + "' user id.");
        Class.forName(MYSQL_JDBC_CLASSNAME);
        connection = DriverManager.getConnection(jdbcurl, userId, password);
    }

    /**
     * @param componentSchemaProperties
     *            - Contains request parameter details
     * @return {@link TableEntity}
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public TableEntity fillComponentSchema(Map componentSchemaProperties) throws SQLException, ParamsCannotBeNullOrEmpty {
        if(componentSchemaProperties.get(Constants.TABLENAME) != null)
            tableName = componentSchemaProperties.get(Constants.TABLENAME).toString().trim();
        else
            query = componentSchemaProperties.get(Constants.QUERY).toString().trim();

         LOG.info("Generating schema for mysql using " + ((tableName!=null)?"table : " + tableName : "query : " + query));

        ResultSet res = null;
        TableEntity tableEntity = new TableEntity();
        List<TableSchemaFieldEntity> tableSchemaFieldEntities = new ArrayList<TableSchemaFieldEntity>();
        try {
            Statement stmt = connection.createStatement();
            if (query != null && !query.isEmpty())
                res = stmt.executeQuery("Select * from (" + query + ") as alias WHERE 1<0");
            else if (tableName != null && !tableName.isEmpty())
                res = stmt.executeQuery("Select * from " + tableName + " where 1<0");
            else {
                LOG.error("Table or query in request parameter cannot be null or empty " + Constants.QUERY + " => "
                        + query + " " + Constants.TABLENAME + " => " + tableName );
                throw new ParamsCannotBeNullOrEmpty("Table and query cannot be null or empty in request parameters: "
                        + Constants.QUERY + " => " + query + " " + Constants.TABLENAME + " => " + tableName);
            }
            ResultSetMetaData rsmd = res.getMetaData();
            for (int count = 1; count <= rsmd.getColumnCount(); count++) {
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
            if(componentSchemaProperties.get(Constants.TABLENAME) == null)
                tableEntity.setQuery(componentSchemaProperties.get(Constants.QUERY).toString()) ;
            else
                tableEntity.setTableName(componentSchemaProperties.get(Constants.TABLENAME).toString());
            tableEntity.setDatabaseName(componentSchemaProperties.get(Constants.dbType).toString());
            tableEntity.setSchemaFields(tableSchemaFieldEntities);
            res.close();
        } finally {
            connection.close();
        }
        return tableEntity;
    }
}