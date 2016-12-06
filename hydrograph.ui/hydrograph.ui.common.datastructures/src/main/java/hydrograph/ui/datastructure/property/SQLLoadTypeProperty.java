/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hydrograph.ui.datastructure.property;

/**
 * The Class QueryProperty.
 * 
 * @author Bitwise
 */
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
