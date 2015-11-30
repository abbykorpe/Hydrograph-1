package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsPort;
import com.bitwise.app.engine.ui.converter.StraightfullUIConverter;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.CloneComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;
import com.bitwiseglobal.graph.straightpulltypes.Clone;

public class CloneUiConverter extends StraightfullUIConverter {

	private Clone clone;
	private static final String COMPONENT_NAME_SUFFIX = "Clone";

	public CloneUiConverter(TypeBaseComponent typeBaseComponent,
			Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new CloneComponent();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();

		clone = (Clone) typeBaseComponent;
	
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),UIComponentsConstants.VALID.value());
//		uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY	.value());
	
		
		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
		container.getComponentNames().add(clone.getId());
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.CLONE.value());
		uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY.value());
		
		
	}

	protected void getOutPort(TypeStraightPullComponent straightPullComponent) {
		int portCounter = 1;
		
		if (straightPullComponent.getOutSocket() != null) {
			for (TypeStraightPullOutSocket outSocket : straightPullComponent
					.getOutSocket()) {
				uiComponent.engageOutputPort((UIComponentsPort
						.getPortType(outSocket.getType())) + portCounter);
		}
		}
	}

	
	
	
	
}
