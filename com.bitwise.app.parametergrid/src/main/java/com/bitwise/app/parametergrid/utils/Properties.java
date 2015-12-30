package com.bitwise.app.parametergrid.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;

/**
 * 
 * Class to load and store property map(Map<String,String>)
 * 
 * @author Bitwise
 *
 */
public class Properties {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(Properties.class);
	
	private Map<String,String> propertyMap;
	
	public Properties(){
		propertyMap = new LinkedHashMap<>();
		logger.debug("Intantiated Properties object");
	}
	
	/**
	 * 
	 * Add property name(key) and Property value(value) in property Map
	 * 
	 * @param key - String
	 * @param value - String
	 */
	public void setProperty(String key,String value){
		propertyMap.put(key, value);
		logger.debug("Added key={} , Value={} in propertymap.",key,value);
	}
	
	/**
	 * 
	 * add all properties from map to propertymap
	 * 
	 * @param map - Map<String,String>
	 */
	public void setProperty(Map<String,String> map){
		logger.debug("Adding map {} to propertymap",map);
		Set<String> keySet = map.keySet();
		for(String key:keySet){
			propertyMap.put(key, map.get(key));
			logger.debug("Added key={} , Value={} in propertymap.",key,map.get(key));
		}
	}
	
	/**
	 * 
	 * Load properties from property file
	 * 
	 * @param path - {@link String}
	 * @throws IOException
	 */
	public void load(String path) throws IOException {
		logger.debug("Loading properties from file {}",propertyMap);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
		logger.debug("Created buffered reader");
		String line = null;
		
		logger.debug("Reading property file line by line");
		while ((line = bufferedReader.readLine()) != null) {
			addToPropertyMap(line);
		}
	 
		logger.debug("properties load operation compleleted, current map - ",propertyMap);
		
		bufferedReader.close();
		logger.debug("Closed buffered reader");
	}

	/**
	 * 
	 * Parse the line and add key,value to propertymap
	 * 
	 * @param line - {@link String}
	 */
	private void addToPropertyMap(String line) {
		logger.debug("current Line: {}",line );
		if(!line.trim().equals("") && !line.trim().startsWith("#")){
			String[] splittedLine = line.split("=");
			propertyMap.put(splittedLine[0].trim(), line.replace(splittedLine[0]+"=", ""));
		}
	}
	
	
	/**
	 * Save property map to given file
	 * 
	 * @param path {@link String}
	 * @throws IOException
	 */
	public void store(String path) throws IOException{
		
		logger.debug("Storing propertymap {} to file {}",propertyMap,path);
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path))));
		logger.debug("Created buffered writer");
		for (Object key: propertyMap.keySet()) {
			bw.write(key + "=" + propertyMap.get(key));
			bw.newLine();
			logger.debug("Written property key={}, value=",key,propertyMap.get(key));
		}
	 
		bw.close();
		logger.debug("Closed Buffered writer");
	}
	
	/**
	 * 
	 * get property map
	 * 
	 * @return - Map<String,String>
	 */
	public Map<String,String> getProperties(){
		logger.debug("returning propertymap {}",propertyMap);
		return propertyMap;
	}
	
	@Override
	public String toString() {
		return "Properties [propertyMap=" + propertyMap + "]";
	}
}