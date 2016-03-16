/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package com.bitwise.app.engine.ui.converter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.bitwise.app.common.component.config.Property;
import com.bitwise.app.common.util.ComponentCacheUtil;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.engine.ui.repository.ParameterData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.logging.factory.LogFactory;
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

//	/**
//	 * Checks whether a given string is parameter or not.
//	 * 
//	 * @param String
//	 *            the input,
//	 * @return true, if input is parameter
//	 */
//	protected boolean isParameter(String input) {
//		if (input != null) {
//			String regex = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}";
//			Matcher matchs = Pattern.compile(regex).matcher(input);
//			if (matchs.matches()) {
//				return true;
//			}
//		}
//		return false;
//	}

	/**
	 * Generate runtime properties for component.
	 * 
	 * @return Map<String,String>
	 */
	protected abstract Map<String, String> getRuntimeProperties();

	protected Map<String, Object> validateComponentProperties(Map<String, Object> properties) {
		return uiComponent.validateComponentProperties();
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
