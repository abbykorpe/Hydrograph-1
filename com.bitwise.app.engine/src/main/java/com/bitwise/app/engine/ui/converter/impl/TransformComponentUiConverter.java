package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.TransformUIConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.operationstypes.Transform;

public class TransformComponentUiConverter extends TransformUIConverter {

	private Transform transform;
	private static final String COMPONENT_NAME_SUFFIX = "Transform_";

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

		transform = (Transform) typeBaseComponent;
		
	
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),UIComponentsConstants.VALID.value());
			
		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
		container.getComponentNames().add(COMPONENT_NAME);
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.TRANSFORM.value());
		uiComponent.setCategory(UIComponentsConstants.TRANSFORM_CATEGORY.value());
	
	}


	

	
}
