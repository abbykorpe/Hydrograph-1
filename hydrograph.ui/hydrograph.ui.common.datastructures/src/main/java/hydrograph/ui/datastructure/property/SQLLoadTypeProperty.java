package hydrograph.ui.datastructure.property;


public class SQLLoadTypeProperty {
	private String loadType;
	private String primaryKeys;
	private String updateByKeys;
	
	
	public String getLoadType() {
		return loadType;
	}
	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}
	public String getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(String primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	public String getUpdateByKeys() {
		return updateByKeys;
	}
	public void setUpdateByKeys(String updateByKeys) {
		this.updateByKeys = updateByKeys;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((loadType == null) ? 0 : loadType.hashCode());
		result = prime * result
				+ ((primaryKeys == null) ? 0 : primaryKeys.hashCode());
		result = prime * result
				+ ((updateByKeys == null) ? 0 : updateByKeys.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SQLLoadTypeProperty other = (SQLLoadTypeProperty) obj;
		if (loadType == null) {
			if (other.loadType != null)
				return false;
		} else if (!loadType.equals(other.loadType))
			return false;
		if (primaryKeys == null) {
			if (other.primaryKeys != null)
				return false;
		} else if (!primaryKeys.equals(other.primaryKeys))
			return false;
		if (updateByKeys == null) {
			if (other.updateByKeys != null)
				return false;
		} else if (!updateByKeys.equals(other.updateByKeys))
			return false;
		return true;
	}
	
	@Override
	public Object clone() 
	{
		SQLLoadTypeProperty sqlLoadTypeProperty=new SQLLoadTypeProperty();
		sqlLoadTypeProperty.setLoadType(loadType);
		sqlLoadTypeProperty.setPrimaryKeys(primaryKeys);
		sqlLoadTypeProperty.setUpdateByKeys(updateByKeys);
		return sqlLoadTypeProperty;
	}
	
	
	
}
