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

 
package hydrograph.ui.engine.ui.converter.impl;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.JoinMappingGrid;
import hydrograph.ui.datastructure.property.LookupConfigProperty;
import hydrograph.ui.datastructure.property.LookupMapProperty;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.TransformUiConverter;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;
import hydrograph.engine.jaxb.commontypes.TypeInputField;
import hydrograph.engine.jaxb.commontypes.TypeMapField;
import hydrograph.engine.jaxb.commontypes.TypeOperationsComponent;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;
import hydrograph.engine.jaxb.lookup.TypeKeyFields;
import hydrograph.engine.jaxb.operationstypes.Lookup;

public class LookupUiConverter extends TransformUiConverter {


	private Lookup lookup;
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(LookupUiConverter.class);
		
	
	public LookupUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new hydrograph.ui.graph.model.components.Lookup();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Lookup-Properties for -{}", componentName);
		lookup = (Lookup) typeBaseComponent;
		LOGGER.info("LOOKUP_CONFIG_FIELD::{}",getLookupConfigProperty());
		propertyMap.put(Constants.LOOKUP_CONFIG_FIELD, getLookupConfigProperty());

		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);

		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.LOOKUP.value());
		validateComponentProperties(propertyMap);
	}

	private LookupConfigProperty getLookupConfigProperty() {
		LookupConfigProperty lookupConfigProperty = null;
		List<TypeKeyFields> typeKeyFieldsList = lookup.getKeys();
		if (typeKeyFieldsList != null && !typeKeyFieldsList.isEmpty()) {
			lookupConfigProperty = new LookupConfigProperty();
			for (TypeKeyFields typeKeyFields : typeKeyFieldsList) {
				if (typeKeyFields.getInSocketId().equalsIgnoreCase("in0")
						|| typeKeyFields.getInSocketId().equalsIgnoreCase("lookup"))
					lookupConfigProperty.setLookupKey(getKeyNames(typeKeyFields));
				else if (typeKeyFields.getInSocketId().equalsIgnoreCase("in1")
						|| typeKeyFields.getInSocketId().equalsIgnoreCase("driver"))
					lookupConfigProperty.setDriverKey(getKeyNames(typeKeyFields));
			}
		}
		return lookupConfigProperty;
	}

	private String getKeyNames(TypeKeyFields typeKeyFields) {
		StringBuilder lookupKey = new StringBuilder("");
		if (typeKeyFields != null && !typeKeyFields.getField().isEmpty()) {
			for (TypeFieldName typeFieldName : typeKeyFields.getField()) {
				lookupKey.append(typeFieldName.getName()).append(",");
			}
		}
		if( lookupKey.lastIndexOf(",")!=-1)
			lookupKey=lookupKey.deleteCharAt(lookupKey.lastIndexOf(","));
		return lookupKey.toString();
	}
	
	protected void getOutPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating OutPut Ports for -{}", componentName);
		if (operationsComponent.getOutSocket() != null) {
			for (TypeOperationsOutSocket outSocket : operationsComponent.getOutSocket()) {
				uiComponent.engageOutputPort(outSocket.getId());
				if (outSocket.getPassThroughFieldOrOperationFieldOrMapField() != null
						&& !outSocket.getPassThroughFieldOrOperationFieldOrMapField().isEmpty())
					propertyMap.put("hash_join_map", getJoinMappingGrid(outSocket));
			}

		}
	}

	private JoinMappingGrid getJoinMappingGrid(TypeOperationsOutSocket outSocket) {
		String dot_separator = ".";
		LookupMapProperty lookupMapProperty = null;
		JoinMappingGrid joinMappingGrid = new JoinMappingGrid();
		for (Object object : outSocket.getPassThroughFieldOrOperationFieldOrMapField()) {
			if ((TypeInputField.class).isAssignableFrom(object.getClass())) {
				lookupMapProperty = new LookupMapProperty();
				lookupMapProperty.setOutput_Field(((TypeInputField) object).getName());
				lookupMapProperty.setSource_Field(((TypeInputField) object).getInSocketId() + dot_separator
						+ ((TypeInputField) object).getName());
				joinMappingGrid.getLookupMapProperties().add(lookupMapProperty);
			}
			if ((TypeMapField.class).isAssignableFrom(object.getClass())) {
				lookupMapProperty = new LookupMapProperty();
				lookupMapProperty.setOutput_Field(((TypeMapField) object).getName());
				lookupMapProperty.setSource_Field(((TypeMapField) object).getInSocketId() + dot_separator
						+ ((TypeMapField) object).getSourceName());
				joinMappingGrid.getLookupMapProperties().add(lookupMapProperty);
			}
		}
		return joinMappingGrid;
	}
}
