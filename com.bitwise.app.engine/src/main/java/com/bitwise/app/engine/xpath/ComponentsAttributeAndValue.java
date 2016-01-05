package com.bitwise.app.engine.xpath;

/**
 * @author Bitwise This class is used to hold component's properties attribute and name
 */
public class ComponentsAttributeAndValue {
	private String attributeName;
	private String attributeValue;
	private static final String DEFAULT_ATTRIBUTE_NAME = "value";

	private ComponentsAttributeAndValue() {

	}

	public ComponentsAttributeAndValue(String attributeName, String attributeValue) {
		if (attributeName == null)
			this.attributeName = DEFAULT_ATTRIBUTE_NAME;
		else
			this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

}
