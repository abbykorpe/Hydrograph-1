package hydrograph.engine.spark.components;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Properties;

/**
 * Created by sandeepv on 1/14/2017.
 */
public class RunSQLComponentImpl implements RunSQLComponentInt {

    static Logger log = Logger.getLogger(RunSQLComponentImpl.class.getName());

    @Override
    public void testDatabaseConnection(Properties properties) throws SQLException {
        Connection conn = null;

        if (!properties.getProperty("databaseConnectionName").isEmpty()) {
            if (properties.getProperty("databaseConnectionName").equalsIgnoreCase("MYSQL")) {
                log.debug("Request received test connection " + properties.getProperty("databaseConnectionName").equalsIgnoreCase("MYSQL"));
                System.out.println("in mysql"+properties.getProperty("databaseConnectionName"));
                try {
                    properties.setProperty("className", "com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://" + properties.getProperty("ip_address") + ":" + properties.getProperty("portNumber") + "/" + properties.getProperty("databaseName")+"?allowMultiQueries=true", properties.getProperty("userName"), properties.getProperty("password"));
                    conn.setAutoCommit(false);
                    PreparedStatement stmt = conn.prepareStatement(properties.getProperty("query"));
                    if ("select".toUpperCase().equals((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {
                        System.out.println("in select");
                        log.debug("Request received test connection for execute " + properties.getProperty("query"));
                        ResultSet rs = stmt.executeQuery(properties.getProperty("query"));
                        while (rs.next()) {
                            conn.close();
                        }
                    } else if ("Insert".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Delete".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Truncate".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {
                        System.out.println("in insert");
                        log.debug("Request received test connection for execute " + properties.getProperty("query"));
                        stmt.execute(properties.getProperty("query"));
                        conn.commit();
                        conn.close();
                    } else if ("Update".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Create".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Alter".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Drop".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {
                        System.out.println("@@@@@@@@@@@@@@@@@@ In SP of MYSQL ");
                        log.debug("Request received test connection for execute " + properties.getProperty("query"));
                        stmt.executeUpdate(properties.getProperty("query"));
                        System.out.println("@@@@@@@@@@@@@@@@@@ after SP of MYSQL ");
                        conn.commit();

                        conn.close();
                    }
                    else if ("Call".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Exec".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {
                        System.out.println("in call");
                        CallableStatement callableStatement = conn.prepareCall(properties.getProperty("query"));
                        callableStatement.executeUpdate();
                        conn.commit();
                        System.out.println("Query executed successfully.");
                        conn.close();
                    }

                } catch (SQLException e) {
                    System.out.println("in exception");
                    log.debug("SQL exception occurred ");
                    e.printStackTrace();
                }

            } else if (properties.getProperty("databaseConnectionName").equalsIgnoreCase("Oracle")) {
                log.debug("Request received test connection " + properties.getProperty("databaseConnectionName").equalsIgnoreCase("Oracle"));
                try {
                    properties.setProperty("className", "oracle.jdbc.driver.OracleDriver");
                    conn = DriverManager.getConnection("jdbc:oracle:thin://@" + properties.getProperty("ip_address") + ":" + properties.getProperty("portNumber") + "/" + properties.getProperty("databaseName"), properties.getProperty("userName"), properties.getProperty("password"));
                    conn.setAutoCommit(false);
                    PreparedStatement stmt = conn.prepareStatement(properties.getProperty("query"));
                    if ("Select".toUpperCase().equals((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {
                        log.debug("Request received test connection for execute " + properties.getProperty("query"));
                        ResultSet rs = stmt.executeQuery(properties.getProperty("query"));
                        while (rs.next()) {
                            conn.close();
                        }
                    } else if ("Insert".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Delete".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Truncate".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {

                        System.out.println("In Query Insert");
                        log.debug("Request received test connection for execute " + properties.getProperty("query"));
                        stmt.execute(properties.getProperty("query"));
                        conn.commit();
                        System.out.println("Query executed successfully.");
                        conn.close();
                    } else if ("Update".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Create".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Alter".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Drop".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {
                        /*  System.out.println("In Query Update");
                        log.debug("Request received test connection for execute " + properties.getProperty("query"));
                        stmt.executeUpdate(properties.getProperty("query"));
                        conn.commit();
                        System.out.println("Query executed successfully.");
                        conn.close();*/
                    } else if ("Call".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Exec".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {
                        log.debug("Request received test connection for execute " + properties.getProperty("query"));
                        CallableStatement callableStatement = conn.prepareCall(properties.getProperty("query"));
                        callableStatement.executeUpdate();
                        conn.commit();
                        System.out.println("Query executed successfully.");
                        conn.close();
                    }

                } catch (SQLException e) {
                    log.debug("SQL exception occurred ");
                    e.printStackTrace();
                }

            } else if (properties.getProperty("databaseConnectionName").equalsIgnoreCase("Teradata")) {
                log.debug("Request received test connection " + properties.getProperty("databaseConnectionName").equalsIgnoreCase("Teradata"));
                try {
                    Class.forName("com.teradata.jdbc.TeraDriver");
                    conn = DriverManager.getConnection("jdbc:teradata://" + properties.getProperty("ip_address") + "/DATABASE=" + properties.getProperty("databaseName") + ",USER=" + properties.getProperty("userName") + ",PASSWORD=" + properties.getProperty("password"));
                    Statement stmt = conn.createStatement();
//                    StringBuilder stringBuilder = new StringBuilder();
                    if ("select".toUpperCase().equals((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {
                        log.debug("Request received test connection for execute " + properties.getProperty("query"));
                        ResultSet rs = stmt.executeQuery(properties.getProperty("query"));
                        while (rs.next()) {
                            conn.close();
                        }
                    } else if ("Insert".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Delete".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Truncate".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {
                        log.debug("Request received test connection for execute " + properties.getProperty("query"));
                        stmt.execute(properties.getProperty("query"));
                        conn.commit();
                        conn.close();
                    } else if ("Update".toUpperCase().equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Create".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase()) || "Drop".equalsIgnoreCase((properties.getProperty("query").split("\\s+"))[0].toUpperCase())) {
                        log.debug("Request received test connection for execute " + properties.getProperty("query"));
                        stmt.executeUpdate(properties.getProperty("query"));
                        conn.commit();
                        System.out.println("Query executed successfully.");
                        conn.close();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    log.debug("SQL exception occurred ");
                    e.printStackTrace();
                }
            } else {
                log.debug("Request received test connection is empty.");
            }
        }

    }
}
