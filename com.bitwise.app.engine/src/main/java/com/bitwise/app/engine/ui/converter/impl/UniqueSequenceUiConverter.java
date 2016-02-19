package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.TransformUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.UniqueSequence;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeOperationField;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;

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
