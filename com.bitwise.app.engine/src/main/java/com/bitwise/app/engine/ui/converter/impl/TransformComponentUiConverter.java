package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.TransformUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.operationstypes.Transform;

public class TransformComponentUiConverter extends TransformUiConverter {

	private Transform transform;
	private static final String NAME_SUFFIX = "Transform_";
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
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),UIComponentsConstants.VALID.value());
	
		
		container.getComponentNextNameSuffixes().put(NAME_SUFFIX, 0);
		container.getComponentNames().add(componentName);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.TRANSFORM.value());
		uiComponent.setCategory(UIComponentsConstants.TRANSFORM_CATEGORY.value());
	
	}


	

	
}
