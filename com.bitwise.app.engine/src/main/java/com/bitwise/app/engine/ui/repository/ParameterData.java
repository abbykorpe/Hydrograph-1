package com.bitwise.app.engine.ui.repository;

public class ParameterData {

	private String propertyName;
	private String parameterName;
	
	private ParameterData()
	{}
	public ParameterData(String propertyName,String parameterName)
	{
		this.parameterName=parameterName;
		this.propertyName=propertyName;
		
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Property Name:"+propertyName+
				"Parameter Name:"+parameterName;
	}
	
}
