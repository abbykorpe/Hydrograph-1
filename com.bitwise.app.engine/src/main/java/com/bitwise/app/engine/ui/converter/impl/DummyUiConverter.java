package com.bitwise.app.engine.ui.converter.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.converter.UiConverter;
import com.bitwise.app.engine.ui.repository.InSocketDetail;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.DummyComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;

public class DummyUiConverter extends UiConverter {

	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(DummyUiConverter.class);

	public DummyUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new DummyComponent();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching Straight-Pull-Properties for -{}", componentName);
		
		if (getInPort() && getOutPort()) {
			container.getComponentNextNameSuffixes().put(name_suffix, 0);
			container.getComponentNames().add(componentName);
			uiComponent.setProperties(propertyMap);
			
		}
		propertyMap.put(UIComponentsConstants.VALIDITY_STATUS.value(),"ERROR");
		
	}

	private boolean getOutPort() {
		LOGGER.debug("Fetching default-component Output port for -{}", componentName);
		uiComponent.engageOutputPort("out0");
		uiComponent.engageOutputPort("unused0");
		return true;

	}

	private boolean getInPort() {
		LOGGER.debug("Generating default-component inputport for -{}", componentName);
		int portCounter = 1;
		String fixedInsocket = "in0";
		if (UIComponentRepo.INSTANCE.getInsocketMap().get(componentName) != null) {
			for (InSocketDetail inSocketDetail : UIComponentRepo.INSTANCE.getInsocketMap().get(componentName)) {
				if(inSocketDetail.getInSocketType()!=null)
				uiComponent.engageInputPort(inSocketDetail.getInSocketType() + portCounter);
				else
					uiComponent.engageInputPort(Constants.INPUT_SOCKET_TYPE + portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocketDetail.getFromComponentId(), componentName, inSocketDetail
								.getFromSocketId(), fixedInsocket));

			}

		}
		return true;
	}

	@Override
	protected Map<String, String> getRuntimeProperties() {

		return null;
	}

}
