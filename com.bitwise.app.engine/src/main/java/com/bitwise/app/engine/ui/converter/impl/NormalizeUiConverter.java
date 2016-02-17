package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.ui.converter.TransformUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;

/**
 * The NormalizeUiConverter converts engine's Normalize component into ui-Normalize component
 * 
 * @author Bitwise
 * 
 */
public class NormalizeUiConverter extends TransformUiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(NormalizeUiConverter.class);

	public NormalizeUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new com.bitwise.app.graph.model.components.Normalize();
		this.propertyMap = new LinkedHashMap<>();
	}

	/* *
	 * This method creates properties specific to Normalize component
	 */
	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Transform-Properties for -{}", componentName);

		propertyMap.put(Constants.PARAM_OPERATION, createTransformPropertyGrid());

		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(Constants.NORMALIZE_DISPLAYNAME);
		validateComponentProperties(propertyMap);
	}

}
