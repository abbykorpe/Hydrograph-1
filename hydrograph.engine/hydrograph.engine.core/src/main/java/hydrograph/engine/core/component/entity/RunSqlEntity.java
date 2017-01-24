package hydrograph.engine.core.component.entity;

import hydrograph.engine.core.component.entity.base.OperationEntityBase;

/**
 * Created by sandeepv on 1/24/2017.
 */
public class RunSqlEntity extends OperationEntityBase {

    private String databaseConnectionName;
    private String serverName;
    private String portNumber;
    private String databaseName;
    private String dbUserName;
    private String dbPassword;
    private String queryCommand;

    public String getDatabaseConnectionName() {
        return databaseConnectionName;
    }

    public void setDatabaseConnectionName(String databaseConnectionName) {
        this.databaseConnectionName = databaseConnectionName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getQueryCommand() {
        return queryCommand;
    }

    public void setQueryCommand(String queryCommand) {
        this.queryCommand = queryCommand;
    }
}
