package com.bitwise.app.propertywindow.widgets.utility;

/**
 * @author Bitwise
 * Enum for various data types.
 */

public enum DataType {
	
	
	STRING_CLASS("java.lang.String"),
	INTEGER_CLASS("java.lang.Integer"),
	DOUBLE_CLASS("java.lang.Double"),
	FLOAT_CLASS("java.lang.Float"),
	SHORT_CLASS("java.lang.Short"),
	BOLLEAN_CLASS("java.lang.Boolean"),
	DATE_CLASS("java.util.Date"),
	BIGDECIMAL_CLASS("java.math.BigDecimal")
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
