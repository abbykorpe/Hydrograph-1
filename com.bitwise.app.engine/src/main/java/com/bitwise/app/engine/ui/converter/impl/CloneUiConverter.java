package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.StraightpullUiConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.CloneComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;
import com.bitwiseglobal.graph.straightpulltypes.Clone;

/**
 * The class CloneUiConverter
 * 
 * @author Bitwise
 * 
 */
public class CloneUiConverter extends StraightpullUiConverter {

	private Clone clone;
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(CloneUiConverter.class);

	public CloneUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new CloneComponent();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Straight-Pull-Properties for -{}", componentName);
		clone = (Clone) typeBaseComponent;

		

		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(clone.getId());
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.CLONE.value());
	
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
