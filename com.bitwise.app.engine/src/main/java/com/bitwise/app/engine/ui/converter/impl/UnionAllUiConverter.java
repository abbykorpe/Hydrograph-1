package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.converter.StraightfullUiConverter;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.UnionallComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.straightpulltypes.UnionAll;

public class UnionAllUiConverter extends StraightfullUiConverter {

	private UnionAll unionAll;
	private static final String COMPONENT_NAME_SUFFIX = "UnionAll";
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(UnionAllUiConverter.class);
	
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
		LOGGER.debug("Fetching Union-All-Properties for -{}",COMPONENT_NAME);
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
		LOGGER.debug("Generating Union-All-Input-Port for -{}",COMPONENT_NAME);
		int portCounter = 1;
		String fixedInsocket="in0";
		if (straightPullComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : straightPullComponent
					.getInSocket()) {
				uiComponent.engageInputPort(inSocket.getType() + portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(new LinkingData(inSocket.getFromComponentId(),
								straightPullComponent.getId(),
								inSocket.getFromSocketId(),
								fixedInsocket
								
				));

			}
		}
	}


}
