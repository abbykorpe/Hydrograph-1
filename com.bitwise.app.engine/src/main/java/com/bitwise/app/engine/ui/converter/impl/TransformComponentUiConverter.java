package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.TransformUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.operationstypes.Transform;

public class TransformComponentUiConverter extends TransformUiConverter {

	private Transform transform;
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(TransformComponentUiConverter.class);
	
	public TransformComponentUiConverter(TypeBaseComponent typeBaseComponent,
			Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new com.bitwise.app.graph.model.components.Transform();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Transform-Properties for -{}",componentName);
		transform = (Transform) typeBaseComponent;
		propertyMap.put(Constants.PARAM_OPERATION, createTransformPropertyGrid());
	
	
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.TRANSFORM.value());
		validateComponentProperties(propertyMap);
	}


	

	
}
