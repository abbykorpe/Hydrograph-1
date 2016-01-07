package com.bitwise.app.engine.ui.converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.bitwise.app.common.component.config.Property;
import com.bitwise.app.common.component.config.Usage;
import com.bitwise.app.common.util.ComponentCacheUtil;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.engine.ui.repository.ParameterData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.validators.impl.IValidator;
import com.bitwiseglobal.graph.commontypes.BooleanValueType;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeBaseOutSocket;

/**
 * The class UiConverter
 * 
 * @author Bitwise
 * 
 */

public abstract class UiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(UiConverter.class);
	protected Container container;
	protected Component uiComponent = null;
	protected TypeBaseComponent typeBaseComponent = null;
	protected LinkedHashMap<String, Object> propertyMap = null;
	protected static final String PHASE = "phase";
	protected static final String NAME = "name";
	protected String componentName;
	protected String name_suffix;

	/**
	 * Generate basic properties that are common in all components.
	 */
	public void prepareUIXML() {
		componentName = typeBaseComponent.getId();
		name_suffix = uiComponent.getComponentName() + "_";
		LOGGER.debug("Preparing basic properties for component {}", componentName);
		propertyMap.put(NAME, componentName);
		propertyMap.put(PHASE, typeBaseComponent.getPhase().toString());
		uiComponent.setComponentLabel(componentName);
		uiComponent.setParent(container);
		UIComponentRepo.INSTANCE.getComponentUiFactory().put(componentName, uiComponent);
	}

	/**
	 * @return Component's object
	 */
	public Component getComponent() {
		return uiComponent;
	}

	/**
	 * @param value
	 *            , BooleanValueType value.
	 * 
	 * @param propertyName
	 *            , component property-name having BooleanValueType value.
	 * 
	 * @return String, string value of BooleanValueType value.
	 */
	public String convertBooleanVlaue(BooleanValueType value, String propertyName) {
		LOGGER.debug("Converting Boolean to String - {}", propertyName);
		Object stringValue = null;
		if (value != null && value.isValue() != null && value.isValue().toString().equalsIgnoreCase("true")) {
			return "True";
		} else {
			stringValue = getValue(propertyName);
			if (stringValue != null) {
				return (String) stringValue;
			}

		}
		return "False";
	}

	/**
	 * Replace the default values by parameters that were generated while parsing the engine xml.
	 * 
	 * @param propertyName
	 * @return String
	 */
	protected String getValue(String propertyName) {
		LOGGER.debug("Getting Parameter for - {}", propertyName);
		List<ParameterData> parameterList = UIComponentRepo.INSTANCE.getParammeterFactory().get(componentName);
		if (parameterList != null) {
			for (ParameterData param : parameterList) {
				if (param.getPropertyName().equals(propertyName)) {
					return param.getParameterName();
				}
			}
		}

		return null;
	}

	/**
	 * Checks whether a given string is parameter or not.
	 * 
	 * @param String
	 *            the input,
	 * @return true, if input is parameter
	 */
	protected boolean isParameter(String input) {
		if (input != null) {
			String regex = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}";
			Matcher matchs = Pattern.compile(regex).matcher(input);
			if (matchs.matches()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Generate runtime properties for component.
	 * 
	 * @return Map<String,String>
	 */
	protected abstract Map<String, String> getRuntimeProperties();

	protected Map<String, Object> validateComponentProperties(Map<String, Object> properties) {
		boolean componentHasRequiredValues = Boolean.TRUE;
		com.bitwise.app.common.component.config.Component component = XMLConfigUtil.INSTANCE.getComponent(uiComponent.getComponentName());
		
		for (Property configProperty : component.getProperty()) {
			Object propertyValue = properties.get(configProperty.getName());
			
			List<String> validators = ComponentCacheUtil.INSTANCE.getValidatorsForProperty(uiComponent.getComponentName(), configProperty.getName());
			
			IValidator validator = null;
			for (String validatorName : validators) {
				try {
					validator = (IValidator) Class.forName(Constants.VALIDATOR_PACKAGE_PREFIX + validatorName).newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					LOGGER.error("Failed to create validator", e);
					throw new RuntimeException("Failed to create validator", e);
				}
				boolean status = validator.validate(propertyValue, configProperty.getName());
				//NOTE : here if any of the property is not valid then whole component is not valid 
				if(status == false){
					componentHasRequiredValues = Boolean.FALSE;
				}
			}
		}
		if (!componentHasRequiredValues) {
			properties.put(Component.Props.VALIDITY_STATUS.getValue(), Component.ValidityStatus.WARN.name());
		}
		return properties;
	}

	protected String getInputSocketType(TypeBaseInSocket inSocket) {
		if (inSocket.getType() != null)
			return inSocket.getType();

		return Constants.INPUT_SOCKET_TYPE;
	}

	protected String getOutputSocketType(TypeBaseOutSocket outSocket) {
		if (outSocket.getType() != null)
			return outSocket.getType();

		return Constants.OUTPUT_SOCKET_TYPE;
	}
}
