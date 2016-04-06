package hydrograph.ui.propertywindow.testdata;

import java.util.LinkedHashMap;


// TODO: Auto-generated Javadoc
/**
 * 
 * @author Bitwise
 * Sep 08, 2015
 * 
 */

public class ComponentModel {
	
	private LinkedHashMap<String, Object> getInputComponenetProperties(){
		LinkedHashMap<String,Object> inputComponenetProperties = new LinkedHashMap<>();
		inputComponenetProperties.put("path", null);
		inputComponenetProperties.put("delimiter", null);
		inputComponenetProperties.put("charset", null);
		inputComponenetProperties.put("phase", null);
		inputComponenetProperties.put("safe", null);
		inputComponenetProperties.put("has_header", null);		
		inputComponenetProperties.put("Schema", null);
		inputComponenetProperties.put("RuntimeProps", null);
		inputComponenetProperties.put("name", null);
		inputComponenetProperties.put("strict", null);
		
		return inputComponenetProperties;
	}
	
	/**
	 * Gets the properties.
	 * 
	 * @param componentName
	 *            the component name
	 * @return the properties
	 */
	public LinkedHashMap<String,Object> getProperties(String componentName){
		if(componentName.equals("Input")){
			return getInputComponenetProperties();
		}else{
			return null;
		}
	}
}
