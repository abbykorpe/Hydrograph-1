package hydrograph.engine.spark.components;

import com.ibatis.common.jdbc.ScriptRunner;
import hydrograph.engine.core.component.entity.RunSqlEntity;
import hydrograph.engine.core.component.entity.base.AssemblyEntityBase;
import hydrograph.engine.spark.components.base.CommandComponentSparkFlow;
import hydrograph.engine.spark.components.base.SparkFlow;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by sandeepv on 1/31/2017.
 */
public class RunSQLComponent extends CommandComponentSparkFlow implements Serializable {

    static Logger log = Logger.getLogger(RunSQLComponent.class.getName());
    File tempFile = File.createTempFile("query", ".txt", new File("C:\\tmp\\"));
    private AssemblyEntityBase assemblyEntityBase;
    private RunSqlEntity runSqlEntity;

    public RunSQLComponent(AssemblyEntityBase assemblyEntityBase) throws IOException {
        this.assemblyEntityBase = assemblyEntityBase;
        runSqlEntity = (RunSqlEntity) assemblyEntityBase;
    }

    @Override
    public void execute() {
        Properties properties = new Properties();
        Connection conn = null;
        try {

            if (runSqlEntity.getDatabaseConnectionName().equalsIgnoreCase("MYSQL")) {
                log.debug("Request received test connection " + runSqlEntity.getDatabaseConnectionName());
                try {
                    properties.setProperty("className", "com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://" + runSqlEntity.getServerName() + ":" + runSqlEntity.getPortNumber() + "/" + runSqlEntity.getDatabaseName() + "?allowMultiQueries=true", runSqlEntity.getDbUserName(), runSqlEntity.getDbPassword());
                    conn.setAutoCommit(false);
                    try {
                        testScriptDemo(runSqlEntity.getQueryCommand());
                        ScriptRunner sr = new ScriptRunner(conn, false, true);
                        Reader reader = new BufferedReader(new FileReader(tempFile));
                        sr.runScript(reader);
                        reader.close();
                        exitStatus_$eq(0);
                    } catch (SQLException | IOException e) {
                        exitStatus_$eq(-1);
                        log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                } catch (Exception e) {
                    exitStatus_$eq(-1);
                    log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + e.getMessage());
                    throw new RuntimeException(e);
                }
            } else if (runSqlEntity.getDatabaseConnectionName().equalsIgnoreCase("Oracle")) {
                log.debug("Request received test connection " + runSqlEntity.getDatabaseConnectionName());
                try {
                    properties.setProperty("className", "oracle.jdbc.driver.OracleDriver");
                    conn = DriverManager.getConnection("jdbc:oracle:thin://@" + runSqlEntity.getServerName() + ":" + runSqlEntity.getPortNumber() + "/" + runSqlEntity.getDatabaseName(), runSqlEntity.getDbUserName(), runSqlEntity.getDbPassword());
                    conn.setAutoCommit(false);
                    try {
                        testScriptDemo(runSqlEntity.getQueryCommand());
                        ScriptRunner sr = new ScriptRunner(conn, false, true);
                        Reader reader = new BufferedReader(new FileReader(tempFile));
                        sr.runScript(reader);
                        reader.close();
                        exitStatus_$eq(0);
                    } catch (Exception e) {
                        exitStatus_$eq(-1);
                        log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                } catch (SQLException e) {
                    exitStatus_$eq(-1);
                    log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + e.getMessage());
                    throw new RuntimeException(e);
                }
            } else if (runSqlEntity.getDatabaseConnectionName().equalsIgnoreCase("Teradata")) {
                log.debug("Request received test connection " + runSqlEntity.getDatabaseConnectionName());
                try {
                    properties.setProperty("className", "com.teradata.jdbc.TeraDriver");
                    conn = DriverManager.getConnection("jdbc:teradata://" + runSqlEntity.getServerName() + "/DATABASE=" + runSqlEntity.getDatabaseName() + ",USER=" + runSqlEntity.getDbUserName() + ",PASSWORD=" + runSqlEntity.getDbPassword());
                    conn.setAutoCommit(false);
                    try {
                        testScriptDemo(runSqlEntity.getQueryCommand());
                        ScriptRunner sr = new ScriptRunner(conn, false, true);
                        Reader reader = new BufferedReader(new FileReader(tempFile));
                        sr.runScript(reader);
                        reader.close();
                        exitStatus_$eq(0);
                    } catch (Exception e) {
                        exitStatus_$eq(-1);
                        log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                } catch (SQLException e) {
                    exitStatus_$eq(-1);
                    log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + e.getMessage());
                    throw new RuntimeException(e);
                }
            } else if (runSqlEntity.getDatabaseConnectionName().equalsIgnoreCase("Redshift")) {
                log.debug("Request received test connection " + runSqlEntity.getDatabaseConnectionName());
                try {
                    properties.setProperty("className", "com.amazon.redshift.jdbc42.Driver");
                    conn = DriverManager.getConnection("jdbc:redshift://" + runSqlEntity.getServerName() + ":" + runSqlEntity.getPortNumber() + "/" + runSqlEntity.getDatabaseName(), runSqlEntity.getDbUserName(), runSqlEntity.getDbPassword());
                    conn.setAutoCommit(false);
                    try {
                        testScriptDemo(runSqlEntity.getQueryCommand());
                        ScriptRunner sr = new ScriptRunner(conn, false, true);
                        Reader reader = new BufferedReader(new FileReader(tempFile));
                        sr.runScript(reader);
                        reader.close();
                        exitStatus_$eq(0);
                    } catch (Exception e) {
                        exitStatus_$eq(-1);
                        log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                } catch (SQLException e) {
                    exitStatus_$eq(-1);
                    log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            exitStatus_$eq(-1);
            e.printStackTrace();
        }
    }

    public void testScriptDemo(String statement) throws ClassNotFoundException, SQLException {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
            bufferedWriter.write(statement);
        } catch (IOException e) {
            exitStatus_$eq(-1);
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException ex) {
                exitStatus_$eq(-1);
                ex.printStackTrace();
            }
        }
    }

    @Override
    public int exitStatus() {
        return super.exitStatus();
    }

    @Override
    public void exitStatus_$eq(int exitStatus) {
        super.exitStatus_$eq(exitStatus);
    }
}

