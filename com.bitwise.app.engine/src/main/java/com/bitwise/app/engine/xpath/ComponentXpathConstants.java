package com.bitwise.app.engine.xpath;

public enum ComponentXpathConstants {
	COMPONENT_CHARSET_XPATH("/graph/*[@id='$id']/charset"),
	COMPONENT_JOIN_TYPE_XPATH("/graph/operations[@id='$id']/keys[@inSocketId='$inSocketId'] [not(@joinType)]"),
	COMPONENT_XPATH_BOOLEAN("/graph/*[@id='$id']/propertyName");
	///graph/*[@id='$id']/*[@id='$inSocketId']/joinType
	private final String value;

	ComponentXpathConstants(String value) {
		this.value = value;
	}

	public String value() { 
		return value;
	}

	public static ComponentXpathConstants fromValue(String value) {
		for (ComponentXpathConstants xpathConstants : ComponentXpathConstants.values()) {
			if (xpathConstants.value.equals(value)) {
				return xpathConstants;
			}
		}
		throw new IllegalArgumentException(value);
	}

}
