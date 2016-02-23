package com.bitwise.app.propertywindow.widgets.utility;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Bitwise
 * Enum for various data types.
 */

public enum DataType {
	
	
	STRING_CLASS(String.class.getCanonicalName()),
	INTEGER_CLASS(Integer.class.getCanonicalName()),
	DOUBLE_CLASS(Double.class.getCanonicalName()),
	FLOAT_CLASS(Float.class.getCanonicalName()),
	SHORT_CLASS(Short.class.getCanonicalName()),
	BOOLEAN_CLASS(Boolean.class.getCanonicalName()),
	DATE_CLASS(Date.class.getCanonicalName()),
	BIGDECIMAL_CLASS(BigDecimal.class.getCanonicalName())
	;
	
	private String value;

	private DataType(String value) {
		this.value = value;
	}

	/**
	 * Returns the value for enum
	*/
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Equals method 
	*/
	public boolean equals(String property) {
		return this.value.equals(property);
	}
	
}
