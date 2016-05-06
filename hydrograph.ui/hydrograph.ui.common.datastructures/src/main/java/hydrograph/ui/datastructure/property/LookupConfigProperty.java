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

import hydrograph.ui.common.cloneableinterface.IDataStructure;

public class LookupConfigProperty implements IDataStructure{
	
	private Boolean isSelected;
	private String driverKey;
	private String lookupPort;
	private String lookupKey;
	
	public LookupConfigProperty(){
		lookupPort="in0";
		isSelected = Boolean.FALSE;
	}
	
	public Boolean isSelected() {
		return isSelected;
	}
	public void setSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getDriverKey() {
		return driverKey;
	}
	public void setDriverKey(String driverKey) {
		this.driverKey = driverKey;
	}
	public String getLookupKey() {
		return lookupKey;
	}
	public void setLookupKey(String lookupKey) {
		this.lookupKey = lookupKey;
	}
	
	public String getLookupPort() {
		return lookupPort;
	}
	public void setLookupPort(String lookupPort) {
		this.lookupPort = lookupPort;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LookupConfigProperty) {
			LookupConfigProperty property = (LookupConfigProperty) obj;

			if(property.driverKey.equals(this.driverKey) &&
					property.lookupKey.equals(this.lookupKey) &&
					property.lookupPort.equals(this.lookupPort) &&
					property.isSelected.equals(this.isSelected)
					)
				return true;
		}
		return false;
	}
	
	@Override
	public Object clone() 
	{
		LookupConfigProperty lookupConfigProperty=new LookupConfigProperty();
		lookupConfigProperty.setDriverKey(getDriverKey());
		lookupConfigProperty.setLookupKey(getLookupKey());
		lookupConfigProperty.setLookupPort(getLookupPort());
		lookupConfigProperty.setSelected(isSelected());
		return lookupConfigProperty;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LookupConfigProperty [isSelected=").append(isSelected)
				.append(", driverKey=").append(driverKey)
				.append(", lookupKey=").append(lookupKey).append("]");
		return builder.toString();
	}
}
