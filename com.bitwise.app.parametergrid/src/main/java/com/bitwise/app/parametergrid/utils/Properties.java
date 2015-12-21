package com.bitwise.app.parametergrid.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Properties {
	
	private Map propertyMap;
	
	public Properties(){
		propertyMap = new LinkedHashMap<>();
	}
	
	public void setProperty(String key,String value){
		propertyMap.put(key, value);
	}
	
	public void setProperty(Map map){
		Set keySet = map.keySet();
		for(Object key:keySet){
			propertyMap.put(key, map.get(key));
		}
	}
	
	public void load(String path) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			if(!line.trim().equals("") && !line.trim().startsWith("#")){
				String[] splittedLine = line.split("=");
				propertyMap.put(splittedLine[0].trim(), line.replace(splittedLine[0]+"=", ""));
			}
		}
	 
		bufferedReader.close();
	}
	
	public void store(String path) throws IOException{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path))));
	 
		for (Object key: propertyMap.keySet()) {
			bw.write(key + "=" + propertyMap.get(key));
			bw.newLine();
		}
	 
		bw.close();
	}
	
	public Map getProperties(){
		return propertyMap;
	}
	
	@Override
	public String toString() {
		return "Properties [propertyMap=" + propertyMap + "]";
	}
	
	
}
