package hydrograph.engine.core.component.generator;

import hydrograph.engine.core.component.entity.OutputJdbcUpdateEntity;
import hydrograph.engine.core.component.entity.utils.OutputEntityUtils;
import hydrograph.engine.core.component.generator.base.OutputComponentGeneratorBase;
import hydrograph.engine.core.constants.Constants;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.outputtypes.JdbcUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by santlalg on 1/19/2017.
 */
public class OutputJdbcUpdateEntityGenerator extends OutputComponentGeneratorBase {

    private JdbcUpdate jaxbJdbcUpdate;
    private OutputJdbcUpdateEntity outputJdbcUpdateEntity;
    private static Logger LOG = LoggerFactory
            .getLogger(OutputJdbcUpdateEntityGenerator.class);

    public OutputJdbcUpdateEntityGenerator(TypeBaseComponent baseComponent) {
        super(baseComponent);
    }

    @Override
    public void castComponentFromBase(TypeBaseComponent baseComponent) {
        jaxbJdbcUpdate = (JdbcUpdate) baseComponent;
    }

    @Override
    public void createEntity() {
        outputJdbcUpdateEntity = new OutputJdbcUpdateEntity();
    }

    @Override
    public void initializeEntity() {
        LOG.trace("Initializing input file RDBMS component: "
                + jaxbJdbcUpdate.getId());

        outputJdbcUpdateEntity.setComponentId(jaxbJdbcUpdate.getId());
        outputJdbcUpdateEntity.setBatch(jaxbJdbcUpdate.getBatch());

        outputJdbcUpdateEntity
                .setFieldsList(OutputEntityUtils.extractOutputFields(jaxbJdbcUpdate
                        .getInSocket().get(0).getSchema()
                        .getFieldOrRecordOrIncludeExternalSchema()));

        outputJdbcUpdateEntity.setUserName((jaxbJdbcUpdate.getUserName() != null)? jaxbJdbcUpdate.getUserName().getValue():null);
        outputJdbcUpdateEntity.setPassword((jaxbJdbcUpdate.getPassword() != null)? jaxbJdbcUpdate.getPassword().getValue():null );
        outputJdbcUpdateEntity.setUrl(jaxbJdbcUpdate.getUrl().getValue());
        outputJdbcUpdateEntity.setJdbcDriverClass(jaxbJdbcUpdate.getJdbcDriverClass().getValue());
        outputJdbcUpdateEntity.setTableName(jaxbJdbcUpdate.getTableName().getValue());
        outputJdbcUpdateEntity.setBatchSize((jaxbJdbcUpdate.getBatchSize() != null)? jaxbJdbcUpdate.getBatchSize().getValue().intValue(): Constants.DEFAULT_DB_BATCHSIZE);
        outputJdbcUpdateEntity.setUpdateByKeys((jaxbJdbcUpdate.getUpdate().getUpdateByKeys() != null)? jaxbJdbcUpdate.getUpdate().getUpdateByKeys().getField():null);

        outputJdbcUpdateEntity.setRuntimeProperties(jaxbJdbcUpdate.getRuntimeProperties() == null ? new Properties() : OutputEntityUtils
                .extractRuntimeProperties(jaxbJdbcUpdate.getRuntimeProperties()));
    }

    @Override
    public OutputJdbcUpdateEntity getEntity() {
        return outputJdbcUpdateEntity;
    }
}
