package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.StraightpullUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.CloneComponent;
import com.bitwise.app.graph.model.components.LimitComponent;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;
import com.bitwiseglobal.graph.straightpulltypes.Clone;
import com.bitwiseglobal.graph.straightpulltypes.Limit;

/**
 * Converter to convert jaxb limit object into limit component
 */
public class LimitUiConverter extends StraightpullUiConverter {

	private static final String MAX_RECORDS = "maxRecords";

	private Limit limit;
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(LimitUiConverter.class);

	public LimitUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new LimitComponent();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Straight-Pull-Properties for -{}", componentName);
		limit = (Limit) typeBaseComponent;

		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(limit.getId());
		
		Object value = getValue(MAX_RECORDS);
		if(value != null){
			propertyMap.put(Constants.PARAM_COUNT, value);
		}else{
			propertyMap.put(Constants.PARAM_COUNT, limit.getMaxRecords().getValue());
		}
		
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.LIMIT.value());
	
		validateComponentProperties(propertyMap);
	}

	protected void getOutPort(TypeStraightPullComponent straightPullComponent) {
		LOGGER.debug("Fetching Straight-Pull Output port for -{}", componentName);
		int portCounter = 0;
		if (straightPullComponent.getOutSocket() != null) {
			for (TypeStraightPullOutSocket outSocket : straightPullComponent.getOutSocket()) {
				uiComponent.engageOutputPort(getOutputSocketType(outSocket) + portCounter);
			}
		}
	}

}