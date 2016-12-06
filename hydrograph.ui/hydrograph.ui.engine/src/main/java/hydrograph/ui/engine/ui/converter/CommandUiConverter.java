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

package hydrograph.ui.engine.ui.converter;

import java.util.Map;
import java.util.TreeMap;

import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.commontypes.TypeStraightPullComponent;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.logging.factory.LogFactory;

import org.slf4j.Logger;

/**
 * The class CommandUiConverter
 * 
 * @author Bitwise
 * 
 */
public abstract class CommandUiConverter extends UiConverter{
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(CommandUiConverter.class);
	
	/**
	 * Generate common properties of Command component.
	 * 
	 * @param
	 * 
	 * @return
	 */
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching common properties for -{}", componentName);
		uiComponent.setCategory(UIComponentsConstants.COMMAND_CATEGORY.value());
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(), getRuntimeProperties());
	}	
	
	/**
	 * Generate runtime properties for Command component.
	 * 
	 * @return Map<String,String>
	 */
	@Override
	protected Map<String, String> getRuntimeProperties() {
		LOGGER.debug("Generating Runtime Properties for -{}", componentName);
		TreeMap<String, String> runtimeMap = null;
		TypeProperties typeProperties = ((TypeStraightPullComponent) typeBaseComponent).getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new TreeMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}
	

}
