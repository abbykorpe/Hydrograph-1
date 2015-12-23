package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.converter.StraightpullUiConverter;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.UnionallComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.straightpulltypes.UnionAll;

public class UnionAllUiConverter extends StraightpullUiConverter {

	private UnionAll unionAll;
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(UnionAllUiConverter.class);

	public UnionAllUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new UnionallComponent();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Union-All-Properties for -{}", componentName);
		unionAll = (UnionAll) typeBaseComponent;
		uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY.value());
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(unionAll.getId());
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.CLONE.value());
		uiComponent.setCategory(UIComponentsConstants.STRAIGHTPULL_CATEGORY.value());
		validateComponentProperties(propertyMap);
	}

	protected void getInPort(TypeStraightPullComponent straightPullComponent) {
		LOGGER.debug("Generating Union-All-Input-Port for -{}", componentName);
		int portCounter = 1;
		String fixedInsocket = "in0";
		if (straightPullComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : straightPullComponent.getInSocket()) {
				uiComponent.engageInputPort(getInputSocketType(inSocket)+ portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(), straightPullComponent.getId(), inSocket
								.getFromSocketId(), fixedInsocket

						));

			}
		}
	}

}
