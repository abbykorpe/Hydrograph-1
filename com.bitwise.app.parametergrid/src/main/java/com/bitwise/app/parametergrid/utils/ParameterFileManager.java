package com.bitwise.app.parametergrid.utils;

import java.io.IOException;
import java.util.Map;

public class ParameterFileManager {
	private String parameterFilePath;
	public ParameterFileManager(String parameterFilePath){
		this.parameterFilePath = parameterFilePath;
	}
	
	public Map<String, String> getParameterMap() throws IOException{
		Properties prop = new Properties();
		prop.load(parameterFilePath);
		return prop.getProperties();
	}
	
	public void storeParameters(Map<String, String> parameterMap) throws IOException{
		Properties prop = new Properties();
		prop.setProperty(parameterMap);
		prop.store(parameterFilePath);
	}
}
