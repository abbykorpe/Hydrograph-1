package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import org.eclipse.draw2d.geometry.Dimension;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
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
	private static final String NAME_SUFFIX = "Join_";
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(JoinComponentUiConverter.class);
	private int inPortCounter = 1;

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

		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(), UIComponentsConstants.VALID.value());

		container.getComponentNextNameSuffixes().put(NAME_SUFFIX, 0);
		container.getComponentNames().add(componentName);

		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.JOIN.value());
		uiComponent.setCategory(UIComponentsConstants.TRANSFORM_CATEGORY.value());

	}

	private String getSize() {
		Dimension newSize = uiComponent.getSize();
		uiComponent.setSize(newSize.expand(inPortCounter * 10, inPortCounter * 10));
		return String.valueOf(inPortCounter - 1);
	}

	protected void getInPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating InPut Ports for -{}", componentName);
		if (operationsComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : operationsComponent.getInSocket()) {
				uiComponent.engageInputPort(inSocket.getType() + inPortCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(), operationsComponent.getId(), inSocket
								.getFromSocketId(), inSocket.getId()));

				if (inPortCounter > 2) {
					uiComponent.changePortSettings(inPortCounter);
				}
				inPortCounter++;
			}
		}

	}

	protected void getOutPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating OutPut Ports for -{}", componentName);
		int portCounter = 0;
		int unusedportCounter = 0;
		if (operationsComponent.getOutSocket() != null) {
			for (TypeOperationsOutSocket outSocket : operationsComponent.getOutSocket()) {
				if (outSocket.getType().equals("unused"))
					uiComponent.engageOutputPort(outSocket.getType() + (++unusedportCounter));
				else
					uiComponent.engageOutputPort(outSocket.getType() + (++portCounter));

			}

		}
	}
}
