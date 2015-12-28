package com.bitwise.app.parametergrid.utils;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
/**
 * 
 * Class to load and store property maps to given file
 * 
 * @author Bitwise
 *
 */
public class ParameterFileManager {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ParameterFileManager.class);
	
	private String parameterFilePath;
	
	public ParameterFileManager(String parameterFilePath){
		this.parameterFilePath = parameterFilePath;
		logger.debug("Intantiated parameter file manager");
	}
	
	/**
	 * 
	 * get Parameters from file
	 * 
	 * @return - Parameter map
	 * @throws IOException
	 */
	public Map<String, String> getParameterMap() throws IOException{
		Properties prop = new Properties();
		prop.load(parameterFilePath);
		
		logger.debug("Fetched properties {} from file {}",prop.toString(),parameterFilePath);
		return prop.getProperties();
	}
	
	/**
	 * 
	 * Save parameters to file
	 * 
	 * @param parameterMap
	 * @throws IOException
	 */
	public void storeParameters(Map<String, String> parameterMap) throws IOException{
		Properties prop = new Properties();
		prop.setProperty(parameterMap);
		
		prop.store(parameterFilePath);
		logger.debug("Saved properties {} to file {}", prop.toString(),parameterFilePath);
	}
}
