package hydrograph.ui.datastructure.property;

import hydrograph.ui.common.cloneableinterface.IDataStructure;

public class DatabaseSelectionConfig implements IDataStructure {

	private boolean isTableName;
	private String tableName;
	private String sqlQuery;
	private String sqlQueryCounter;

	public String getSqlQueryCounter() {
		return sqlQueryCounter;
	}

	public void setSqlQueryCounter(String sqlQueryCounter) {
		this.sqlQueryCounter = sqlQueryCounter;
	}

	public boolean isTableName() {
		return isTableName;
	}

	public void setTableName(boolean isTableName) {
		this.isTableName = isTableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	@Override
	public Object clone() {
		DatabaseSelectionConfig oracleSelectionConfig = new DatabaseSelectionConfig();
		oracleSelectionConfig.setTableName(isTableName());
		oracleSelectionConfig.setTableName(getTableName());
		oracleSelectionConfig.setSqlQuery(getSqlQuery());
		oracleSelectionConfig.setSqlQueryCounter(getSqlQueryCounter());
		return oracleSelectionConfig;
	}

}
