package hydrograph.engine.spark.components;

import hydrograph.engine.core.component.entity.RunSqlEntity;
import hydrograph.engine.core.component.entity.base.AssemblyEntityBase;
import hydrograph.engine.spark.components.base.SparkFlow;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.*;
import java.util.Properties;

/**
 * Created by sandeepv on 1/14/2017.
 */
public class RunSQLComponent extends SparkFlow implements Serializable {

    static Logger log = Logger.getLogger(RunSQLComponent.class.getName());
    private AssemblyEntityBase assemblyEntityBase;
    private RunSqlEntity runSqlEntity;

    public RunSQLComponent(AssemblyEntityBase assemblyEntityBase) {
        this.assemblyEntityBase = assemblyEntityBase;
        runSqlEntity = (RunSqlEntity) assemblyEntityBase;
    }

    @Override
    public void execute() {
        Connection conn = null;
        Properties properties = new Properties();
        if (runSqlEntity.getDatabaseConnectionName().equalsIgnoreCase("MYSQL")) {
            log.debug("Request received test connection " + runSqlEntity.getDatabaseName().equalsIgnoreCase("MYSQL"));
            try {
                properties.setProperty("className", "com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://" + runSqlEntity.getServerName() + ":" + runSqlEntity.getPortNumber() + "/" + runSqlEntity.getDatabaseName() + "?allowMultiQueries=true", runSqlEntity.getDbUserName(), runSqlEntity.getDbPassword());
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement(runSqlEntity.getQueryCommand());
                if ("select".toUpperCase().equals((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    log.debug("Request received test connection for execute " + runSqlEntity.getQueryCommand());
                    ResultSet rs = stmt.executeQuery(runSqlEntity.getQueryCommand());
                    while (rs.next()) {
                        conn.close();
                    }
                } else if ("Insert".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Delete".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Truncate".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    log.debug("Request received test connection for execute " + runSqlEntity.getQueryCommand());
                    stmt.execute(runSqlEntity.getQueryCommand());
                    conn.commit();
                    conn.close();
                } else if ("Update".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Create".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Alter".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Drop".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    log.debug("Request received test connection for execute " + runSqlEntity.getQueryCommand());
                    stmt.executeUpdate(runSqlEntity.getQueryCommand());
                    conn.commit();
                    conn.close();
                } else if ("Call".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Exec".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    CallableStatement callableStatement = conn.prepareCall(runSqlEntity.getQueryCommand());
                    callableStatement.executeUpdate();
                    conn.commit();
                    conn.close();
                }
            } catch (SQLException e) {
                log.debug("SQL exception occurred ");
                throw new RuntimeException(e);
            }

        } else if (runSqlEntity.getDatabaseConnectionName().equalsIgnoreCase("Oracle")) {
            log.debug("Request received test connection " + runSqlEntity.getDatabaseName().equalsIgnoreCase("Oracle"));
            try {
                properties.setProperty("className", "oracle.jdbc.driver.OracleDriver");
                conn = DriverManager.getConnection("jdbc:oracle:thin://@" + runSqlEntity.getServerName() + ":" + runSqlEntity.getPortNumber() + "/" + runSqlEntity.getDatabaseName(), runSqlEntity.getDbUserName(), runSqlEntity.getDbPassword());
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement(runSqlEntity.getQueryCommand());
                if ("Select".toUpperCase().equals((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    log.debug("Request received test connection for execute " + runSqlEntity.getQueryCommand());
                    ResultSet rs = stmt.executeQuery(runSqlEntity.getQueryCommand());
                    while (rs.next()) {
                        conn.close();
                    }
                } else if ("Insert".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Delete".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Truncate".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    log.debug("Request received test connection for execute " + runSqlEntity.getQueryCommand());
                    stmt.execute(runSqlEntity.getQueryCommand());
                    conn.commit();
                    System.out.println("Query executed successfully.");
                    conn.close();
                } else if ("Update".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Create".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Alter".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Drop".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    log.debug("Request received test connection for execute " + runSqlEntity.getQueryCommand());
                    stmt.executeUpdate(runSqlEntity.getQueryCommand());
                    conn.commit();
                    conn.close();
                } else if ("Call".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Exec".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    log.debug("Request received test connection for execute " + runSqlEntity.getQueryCommand());
                    CallableStatement callableStatement = conn.prepareCall(runSqlEntity.getQueryCommand());
                    callableStatement.executeUpdate();
                    conn.commit();
                    conn.close();
                }
            } catch (SQLException e) {
                log.debug("SQL exception occurred ");
                throw new RuntimeException(e);
            }

        } else if (runSqlEntity.getDatabaseConnectionName().equalsIgnoreCase("Teradata")) {
            log.debug("Request received test connection " + runSqlEntity.getDatabaseName().equalsIgnoreCase("Teradata"));
            try {
                properties.setProperty("className", "com.teradata.jdbc.TeraDriver");
                conn = DriverManager.getConnection("jdbc:teradata://" + runSqlEntity.getServerName() + "/DATABASE=" + runSqlEntity.getDatabaseName() + ",USER=" + runSqlEntity.getDbUserName() + ",PASSWORD=" + runSqlEntity.getDbPassword());
                Statement stmt = conn.createStatement();
                if ("select".toUpperCase().equals((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    log.debug("Request received test connection for execute " + runSqlEntity.getQueryCommand());
                    ResultSet rs = stmt.executeQuery(runSqlEntity.getQueryCommand());
                    while (rs.next()) {
                        conn.close();
                    }
                } else if ("Insert".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Delete".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Truncate".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    log.debug("Request received test connection for execute " + runSqlEntity.getQueryCommand());
                    stmt.execute(runSqlEntity.getQueryCommand());
                    conn.commit();
                    conn.close();
                } else if ("Update".toUpperCase().equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Create".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase()) || "Drop".equalsIgnoreCase((runSqlEntity.getQueryCommand().split("\\s+"))[0].toUpperCase())) {
                    log.debug("Request received test connection for execute " + runSqlEntity.getQueryCommand());
                    stmt.executeUpdate(runSqlEntity.getQueryCommand());
                    conn.commit();
                    conn.close();
                }
            } catch (SQLException e) {
                log.debug("SQL exception occurred ");
                throw new RuntimeException(e);
            }
        } else {
            log.debug("Request received test connection is empty.");
        }
    }
}
