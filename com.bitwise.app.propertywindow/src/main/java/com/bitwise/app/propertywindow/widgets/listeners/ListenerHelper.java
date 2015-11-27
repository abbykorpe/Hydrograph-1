package com.bitwise.app.propertywindow.widgets.listeners;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * 
 * @author Bitwise
 * Sep 24, 2015
 * 
 */

public class ListenerHelper {
	
	/**
	 * The Enum HelperType.
	 * 
	 * @author Bitwise
	 */
	public enum HelperType{
		CONTROL_DECORATION,
		VALIDATION_STATUS,
		SCHEMA_GRID,
		TOOLTIP_ERROR_MESSAGE;
	}
	
	private Map<HelperType, Object> helpers;
	
	/**
	 * Instantiates a new listener helper.
	 */
	public ListenerHelper() {
		this.helpers = new HashMap<>();
	}
	
	/**
	 * Put.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void put(HelperType key, Object value){
		helpers.put(key, value);
	}
	
	/**
	 * Gets the.
	 * 
	 * @param key
	 *            the key
	 * @return the object
	 */
	public Object get(HelperType key){
		return helpers.get(key);
	}
	
	
	//TODO : remove this code once all of its references are remove
	String type;
	Object object;
	
	/**
	 * Instantiates a new listener helper.
	 * 
	 * @param type
	 *            the type
	 * @param object
	 *            the object
	 */
	public ListenerHelper(String type, Object object) {
		super();
		this.type = type;
		this.object = object;
	}
	public String getType() {
		return type;
	}
	public Object getObject() {
		return object;
	}
}
