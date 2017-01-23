package hydrograph.engine.core.component.entity;

import hydrograph.engine.core.component.entity.base.InputOutputEntityBase;
import hydrograph.engine.core.component.entity.elements.InSocket;
import hydrograph.engine.core.component.entity.elements.SchemaField;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;

import java.util.List;

/**
 * Created by santlalg on 1/19/2017.
 */
public class OutputJdbcUpdateEntity extends InputOutputEntityBase {

    private List<InSocket> inSocketList;
    private List<SchemaField> schemaFieldsList;
    private List<TypeFieldName> updateByKeys;

    private String url;
    private String jdbcDriverClass;
    private String tableName;
    private Integer batchSize;
    private String userName;
    private String password;

    /**
     * @return  inSocketList
     */
    public List<InSocket> getInSocketList() {
        return inSocketList;
    }

    /**
     * @param inSocketList set inSocketList
     */
    public void setInSocketList(List<InSocket> inSocketList) {
        this.inSocketList = inSocketList;
    }

    /**
     * @return schemaFieldsList
     */
    public List<SchemaField> getSchemaFieldsList() {
        return schemaFieldsList;
    }

    /**
     * @param schemaFieldsList set schemaFieldList
     */
    public void setSchemaFieldsList(List<SchemaField> schemaFieldsList) {
        this.schemaFieldsList = schemaFieldsList;
    }

    /**
     * @return updateByKeys
     */
    public List<TypeFieldName> getUpdateByKeys() {
        return updateByKeys;
    }

    /**
     * @param updateByKeys set updateByKeys
     */
    public void setUpdateByKeys(List<TypeFieldName> updateByKeys) {
        this.updateByKeys = updateByKeys;
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url set connection url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return jdbcDiverClass of database
     */
    public String getJdbcDriverClass() {
        return jdbcDriverClass;
    }

    /**
     * @param jdbcDriverClass set jdbcDriverClass of database
     */
    public void setJdbcDriverClass(String jdbcDriverClass) {
        this.jdbcDriverClass = jdbcDriverClass;
    }

    /**
     * @return tableName to be updated
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName set tableName to be updated
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return batchSize
     */
    public Integer getBatchSize() {
        return batchSize;
    }

    /**
     * @param batchSize set batchSize
     */
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName set userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password set password to aceess database
     */
    public void setPassword(String password) {
        this.password = password;
    }
}


