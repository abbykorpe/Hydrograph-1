package hydrograph.engine.core.component.generator;

import hydrograph.engine.core.component.entity.RunSqlEntity;
import hydrograph.engine.core.component.entity.base.AssemblyEntityBase;
import hydrograph.engine.core.component.generator.base.CommandComponentGeneratorBase;
import hydrograph.engine.jaxb.commandtypes.RunSQL;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;

/**
 * Created by sandeepv on 1/24/2017.
 */
public class RunSqlGenerator extends CommandComponentGeneratorBase {


    private RunSQL runSQL;
    private RunSqlEntity runSqlEntity;

    /**
     * The constructor accepts a {@link TypeBaseComponent} object returned by
     * jaxb and calls the methods to create and initialize the entity object
     * using the {@link TypeBaseComponent} object
     *
     * @param typeCommandComponent
     */
    public RunSqlGenerator(TypeBaseComponent typeCommandComponent) {
        super(typeCommandComponent);
    }

    @Override
    public void castComponentFromBase(TypeBaseComponent baseComponent) {
        runSQL = (RunSQL) baseComponent;
    }

    @Override
    public void createEntity() {
        runSqlEntity = new RunSqlEntity();
    }

    @Override
    public void initializeEntity() {

        runSqlEntity.setComponentId(runSQL.getId());
        runSqlEntity.setBatch(runSQL.getBatch());
        runSqlEntity.setComponentName(runSQL.getName());
        runSqlEntity.setDatabaseConnectionName(runSQL.getDatabaseConnectionName().getDatabaseConnectionName());
        runSqlEntity.setDatabaseName(runSQL.getDatabaseName().getDatabaseName());
        runSqlEntity.setDbPassword(runSQL.getDbPassword().getPassword());
        runSqlEntity.setPortNumber(runSQL.getPortNumber() != null ? runSQL.getPortNumber().getPortNumber() : "");
        runSqlEntity.setDbUserName(runSQL.getDbUserName().getUserName());
        runSqlEntity.setQueryCommand(runSQL.getQueryCommand().getQuery());
        runSqlEntity.setServerName(runSQL.getServerName().getIpAddress());
    }

    @Override
    public AssemblyEntityBase getEntity() {
        return runSqlEntity;
    }
}
