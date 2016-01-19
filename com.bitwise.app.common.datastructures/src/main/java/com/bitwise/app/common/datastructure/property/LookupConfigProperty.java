package com.bitwise.app.common.datastructure.property;

import com.bitwise.app.cloneableinterface.IDataStructure;

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
