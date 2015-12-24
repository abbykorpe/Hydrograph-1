package com.bitwise.app.propertywindow.property;

import java.util.LinkedHashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class ComponentConfigrationProperty.
 * 
 * @author Bitwise
 */
public class ComponentConfigrationProperty {
	private String propertyName;
	private Object propertyValue;
	private ELTComponenetProperties eltComponenetProperties;
		
	private ComponentConfigrationProperty(){
		
	}
	
	/**
	 * Instantiates a new component configration property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param propertyValue
	 *            the property value
	 */
	public ComponentConfigrationProperty(String propertyName, Object propertyValue) {
		super();
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getPropertyValue() {
		return propertyValue;
	}
	
	public ELTComponenetProperties getEltComponenetProperties() {
		return eltComponenetProperties;
	}

	public void setEltComponenetProperties(
			ELTComponenetProperties eltComponenetProperties) {
		this.eltComponenetProperties = eltComponenetProperties;
	}
	
}
