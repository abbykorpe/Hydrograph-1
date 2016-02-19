package com.bitwise.app.common.constants.label;

/**
 * 
 * Enum for common labels
 * 
 * @author Bitwise
 *
 */
public enum LabelConstants {
	LOOKUP_PORT("Lookup Port"),
	LOOKUP_KEYS("Lookup Key(s)"),
	PORT_TYPE("Port Type"),
	DRIVER("driver"),
	LOOKUP("lookup"),
	INSERT_IMAGE("Insert Image"),
	
	
	
	LOOKUP_CONFIG("Lookup Configuration");
	
	private String label;
	
	private LabelConstants(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
}
