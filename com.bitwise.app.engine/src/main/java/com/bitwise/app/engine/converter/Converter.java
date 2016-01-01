package com.bitwise.app.engine.converter;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.exceptions.PhaseException;
import com.bitwise.app.engine.exceptions.SchemaException;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.engine.xpath.ComponentXpath;
import com.bitwise.app.engine.xpath.ComponentXpathConstants;
import com.bitwise.app.engine.xpath.ComponentsAttributeAndValue;
import com.bitwise.app.graph.model.Component;
import com.bitwiseglobal.graph.commontypes.BooleanValueType;
import com.bitwiseglobal.graph.commontypes.StandardCharsets;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeDependsOn;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;

/**
 * Base class for converter implementation. Consists of common methods used by
 * all components. Functionalities specific to some of the converters can be
 * found in {@link ConverterHelper}
 * 
 */
public abstract class Converter {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(Converter.class);

	protected static final String ID = "$id";
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();
	protected Component component = null;
	protected TypeBaseComponent baseComponent = null;
	protected String componentName = null;

	/**
	 * Prepares the class of type {@link TypeBaseComponent} for xml conversion
	 * 
	 * @throws PhaseException
	 * @throws SchemaException
	 */
	public void prepareForXML() {
		componentName = (String) properties.get(Constants.PARAM_NAME);
		baseComponent.setId(componentName);

		try {
			baseComponent.setPhase(new BigInteger((String) properties
					.get(Constants.PARAM_PHASE)));
		} catch (NullPointerException | NumberFormatException nfe) {
			logger.error("Phase id Empty or Invalid for : {}, {}",
					new Object[]{baseComponent.getId(), nfe});
		}
	}

	/**
	 * Converts the String to {@link BooleanValueType}
	 * 
	 * @param propertyName
	 * @return {@link BooleanValueType}
	 */
	protected BooleanValueType getBoolean(String propertyName) {
		logger.debug("Getting boolean Value for {}={}", new Object[] {
				propertyName, properties.get(propertyName) });
		if (properties.get(propertyName) != null) {
			BooleanValueType booleanValue = new BooleanValueType();
			booleanValue.setValue(Boolean.valueOf((String) properties
					.get(propertyName)));

			if (!booleanValue.isValue().toString()
					.equalsIgnoreCase((String) properties.get(propertyName))) {
				ComponentXpath.INSTANCE
						.getXpathMap()
						.put((ComponentXpathConstants.COMPONENT_XPATH_BOOLEAN.value().replace(
								ID, componentName)).replace(Constants.PARAM_PROPERTY_NAME,
								propertyName),
								new ComponentsAttributeAndValue(null, properties.get(propertyName).toString()));
				return booleanValue;
			} else {
				return booleanValue;
			}
		}
		return null;
	}

	/**
	 * Converts String value to {@link StandardCharsets}
	 * 
	 * @return {@link StandardCharsets}
	 */
	protected StandardCharsets getCharset() {
		logger.debug("Getting StandardCharsets for {}", properties.get(Constants.PARAM_NAME));
		String charset = (String) properties.get(PropertyNameConstants.CHAR_SET
				.value());
		StandardCharsets targetCharset = null;
		for (StandardCharsets standardCharsets : StandardCharsets.values()) {
			if (standardCharsets.value().equalsIgnoreCase(charset)) {
				targetCharset = standardCharsets;
				break;
			}
		}
		if (charset != null)
			ComponentXpath.INSTANCE.getXpathMap().put(
					ComponentXpathConstants.COMPONENT_CHARSET_XPATH.value()
							.replace(ID, componentName), new ComponentsAttributeAndValue(null, charset));
		return targetCharset;
	}

	/**
	 * Converts String value to {@link TypeDependsOn}
	 * 
	 * @return {@link TypeDependsOn}
	 */
	protected TypeDependsOn getDependsOn() {
		logger.debug("Getting DependsOn for {}", properties.get(Constants.PARAM_NAME));
		if (properties.get(Constants.PARAM_DEPENDS_ON) != null) {
			TypeDependsOn dependsOn = new TypeDependsOn();
			dependsOn.setComponentId((String) properties.get(Constants.PARAM_DEPENDS_ON));
			return dependsOn;
		}
		return null;
	}

	protected TypeProperties getRuntimeProperties() {
		TypeProperties typeProperties = null;
		if (properties.get(PropertyNameConstants.RUNTIME_PROPERTIES.value()) != null) {
			typeProperties = new TypeProperties();
			List<TypeProperties.Property> runtimePropertyList = typeProperties
					.getProperty();
			for (Map.Entry<String, String> entry : ((Map<String, String>) properties
					.get(PropertyNameConstants.RUNTIME_PROPERTIES.value()))
					.entrySet()) {
				Property runtimeProperty = new Property();
				runtimeProperty.setName(entry.getKey());
				runtimeProperty.setValue(entry.getValue());
				runtimePropertyList.add(runtimeProperty);
			}
		}
		return typeProperties;
	}

	/**
	 * Returns the base type of the component
	 * 
	 * @return {@link TypeBaseComponent}
	 */
	public TypeBaseComponent getComponent() {
		return baseComponent;
	}
}
