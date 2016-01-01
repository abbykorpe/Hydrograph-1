package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.eclipse.draw2d.geometry.Dimension;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.converter.TransformUiConverter;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.operationstypes.Join;

public class JoinComponentUiConverter extends TransformUiConverter {

	private Join join;

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(JoinComponentUiConverter.class);
	private int inPortCounter = 0;

	public JoinComponentUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new com.bitwise.app.graph.model.components.Join();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Join-Properties for -{}", componentName);
		join = (Join) typeBaseComponent;
		propertyMap.put("input_count", getSize());

		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);

		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.JOIN.value());
		uiComponent.setCategory(UIComponentsConstants.TRANSFORM_CATEGORY.value());
		validateComponentProperties(propertyMap);
	}

	private String getSize() {
		Dimension newSize = uiComponent.getSize();
		uiComponent.setSize(newSize.expand(inPortCounter * 15, inPortCounter * 15));
		return String.valueOf(inPortCounter);
	}

	protected void getInPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating InPut Ports for -{}", componentName);
		if (operationsComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : operationsComponent.getInSocket()) {
				uiComponent.engageInputPort(getInputSocketType(inSocket) + inPortCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(), operationsComponent.getId(), inSocket
								.getFromSocketId(), inSocket.getId()));
				inPortCounter++;
			}

			if (inPortCounter > 2) {
				uiComponent.importPortSettings(inPortCounter);
			}
		}

	}

	protected void getOutPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating OutPut Ports for -{}", componentName);
		if (operationsComponent.getOutSocket() != null) {
			for (TypeOperationsOutSocket outSocket : operationsComponent.getOutSocket()) {
				uiComponent.engageOutputPort(outSocket.getId());

			}

		}
	}
}
