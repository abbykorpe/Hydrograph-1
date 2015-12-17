package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.TransformUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.operationstypes.HashJoin;

public class LookupUiConverter extends TransformUiConverter {


	private HashJoin lookup;
	private static final String componentName_SUFFIX = "Lookup_";
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(LookupUiConverter.class);
		
	
	public LookupUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new com.bitwise.app.graph.model.components.Lookup();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Lookup-Properties for -{}", componentName);
		lookup = (HashJoin) typeBaseComponent;
		
		
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(), UIComponentsConstants.VALID.value());

		container.getComponentNextNameSuffixes().put(componentName_SUFFIX, 0);
		container.getComponentNames().add(componentName);

		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.LOOKUP.value());
		uiComponent.setCategory(UIComponentsConstants.TRANSFORM_CATEGORY.value());

	}

	
}

