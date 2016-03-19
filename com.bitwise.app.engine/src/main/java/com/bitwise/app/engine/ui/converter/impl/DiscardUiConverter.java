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

 
package com.bitwise.app.engine.ui.converter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.OutputUiConverter;
import com.bitwise.app.engine.ui.helper.ConverterUiHelper;
import com.bitwise.app.graph.model.Container;
//import com.bitwise.app.graph.model.components.Discard;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.outputtypes.Discard;

/**
 * This class is used to create ui-Discard component from engine's Discard component 
 * 
 * @author Jay Tripathi
 *
 */
public class DiscardUiConverter extends OutputUiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(DiscardUiConverter.class);

	public DiscardUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new com.bitwise.app.graph.model.components.Discard();
		this.propertyMap = new LinkedHashMap<>();
	}

	/* 
	 * Generates properties specific to Discard ui-component
	 * 
	 */
	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Discard-Component for {}", componentName);
		Discard discardUI = (Discard) typeBaseComponent;
		uiComponent.setComponentLabel(discardUI.getId());
		

		uiComponent.setType(UIComponentsConstants.DISCARD.value());
		uiComponent.setCategory(UIComponentsConstants.OUTPUT_CATEGORY.value());
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(discardUI.getId());
		uiComponent.setProperties(propertyMap);
		validateComponentProperties(propertyMap);
	}


	@Override
	protected Object getSchema(TypeOutputInSocket inSocket) {
		LOGGER.debug("Returning Null for UI-Schema data for Discard-Component - {}", componentName);
		Schema schema = null;
		return schema;
	}

	@Override
	protected Map<String, String> getRuntimeProperties() {
		LOGGER.debug("Returning Null for runtime properties for -", componentName);

		return null;
	}

}
