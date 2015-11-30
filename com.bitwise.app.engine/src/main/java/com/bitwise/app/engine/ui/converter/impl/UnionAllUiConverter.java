package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsPort;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.converter.StraightfullUIConverter;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.UnionallComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.straightpulltypes.UnionAll;

public class UnionAllUiConverter extends StraightfullUIConverter {

	private UnionAll unionAll;
	private static final String COMPONENT_NAME_SUFFIX = "UnionAll";

	public UnionAllUiConverter(TypeBaseComponent typeBaseComponent,
			Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new UnionallComponent();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();

		unionAll = (UnionAll) typeBaseComponent;
	
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),
				UIComponentsConstants.VALID.value());
		uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY
				.value());
		container.getComponentNextNameSuffixes().put(COMPONENT_NAME_SUFFIX, 0);
		container.getComponentNames().add(unionAll.getId());
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.CLONE.value());
		uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY
				.value());
		
	}

	protected void getInPort(TypeStraightPullComponent straightPullComponent) {
		int portCounter = 1;
		String fixedInsocket="in0";
		if (straightPullComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : straightPullComponent
					.getInSocket()) {
				uiComponent.engageInputPort((UIComponentsPort
						.getPortType(inSocket.getType())) + portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(new LinkingData(inSocket.getFromComponentId(),
								straightPullComponent.getId(),
								inSocket.getFromSocketId(),
								fixedInsocket
								
				));

			}
		}
	}


}
