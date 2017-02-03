/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
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
package hydrograph.engine.spark.components;

import com.ibatis.common.jdbc.ScriptRunner;
import hydrograph.engine.core.component.entity.RunSqlEntity;
import hydrograph.engine.core.component.entity.base.AssemblyEntityBase;
import hydrograph.engine.spark.components.base.CommandComponentSparkFlow;
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

        if (runSqlEntity.getDatabaseConnectionName().equalsIgnoreCase("MYSQL")) {
            log.debug("Request received test connection " + runSqlEntity.getDatabaseConnectionName());
            try {
                properties.setProperty("className", "com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://" + runSqlEntity.getServerName() + ":" + runSqlEntity.getPortNumber() + "/" + runSqlEntity.getDatabaseName() + "?allowMultiQueries=true", runSqlEntity.getDbUserName(), runSqlEntity.getDbPassword());
                conn.setAutoCommit(false);
                testScriptDemo(runSqlEntity.getQueryCommand());
                ScriptRunner sr = new ScriptRunner(conn, false, true);
                Reader reader = new BufferedReader(new FileReader(tempFile));
                sr.runScript(reader);
                reader.close();
                exitStatus_$eq(0);
            } catch (SQLException | IOException | ClassNotFoundException e) {
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
                testScriptDemo(runSqlEntity.getQueryCommand());
                ScriptRunner sr = new ScriptRunner(conn, false, true);
                Reader reader = new BufferedReader(new FileReader(tempFile));
                sr.runScript(reader);
                reader.close();
                exitStatus_$eq(0);
            } catch (SQLException | IOException | ClassNotFoundException e) {
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
                testScriptDemo(runSqlEntity.getQueryCommand());
                ScriptRunner sr = new ScriptRunner(conn, false, true);
                Reader reader = new BufferedReader(new FileReader(tempFile));
                sr.runScript(reader);
                reader.close();
                exitStatus_$eq(0);
            } catch (SQLException | IOException | ClassNotFoundException e) {
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
                testScriptDemo(runSqlEntity.getQueryCommand());
                ScriptRunner sr = new ScriptRunner(conn, false, true);
                Reader reader = new BufferedReader(new FileReader(tempFile));
                sr.runScript(reader);
                reader.close();
                exitStatus_$eq(0);
            } catch (SQLException | IOException | ClassNotFoundException e) {
                exitStatus_$eq(-1);
                log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + e.getMessage());
                throw new RuntimeException(e);
            }
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
            log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + e.getMessage());
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException ex) {
                exitStatus_$eq(-1);
                log.debug("Failed to Execute" + runSqlEntity.getQueryCommand() + " The error is " + ex.getMessage());
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


