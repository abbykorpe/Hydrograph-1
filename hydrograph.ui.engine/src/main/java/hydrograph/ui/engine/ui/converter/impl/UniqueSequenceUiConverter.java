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
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.TransformUiConverter;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.UniqueSequence;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeOperationField;
import hydrograph.engine.jaxb.commontypes.TypeOperationsComponent;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;

/**
 * This class creates ui-UniqueSequence component from target XML.
 * 
 * @author Bitwise
 * 
 */
public class UniqueSequenceUiConverter extends TransformUiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(UniqueSequenceUiConverter.class);
	private String newFieldName = "";

	public UniqueSequenceUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new UniqueSequence();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Transform-Properties for -{}", componentName);
		propertyMap.put(Constants.UNIQUE_SEQUENCE_PROPERTY_NAME, newFieldName);
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(Constants.UNIQUE_SEQUENCE_TYPE);
		validateComponentProperties(propertyMap);
	}

	protected void getOutPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating OutPut Ports for -{}", componentName);
		if (operationsComponent.getOutSocket() != null) {
			for (TypeOperationsOutSocket outSocket : operationsComponent.getOutSocket()) {
				uiComponent.engageOutputPort(outSocket.getId());
				if (outSocket.getPassThroughFieldOrOperationFieldOrMapField() != null)
					for (Object outSocketProperties : outSocket.getPassThroughFieldOrOperationFieldOrMapField()) {
						if (((TypeOperationField.class).isAssignableFrom(outSocketProperties.getClass()))) {
							newFieldName = ((TypeOperationField) outSocketProperties).getName();
						}
					}
			}

		}
	}

}