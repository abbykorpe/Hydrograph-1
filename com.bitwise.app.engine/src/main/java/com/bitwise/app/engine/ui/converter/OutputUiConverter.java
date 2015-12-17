package com.bitwise.app.engine.ui.converter;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwiseglobal.graph.commontypes.TypeOutputComponent;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;

public abstract class OutputUiConverter extends UiConverter {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputUiConverter.class);
	
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching common properties for -{}",COMPONENT_NAME);
		getInPort((TypeOutputComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),getRuntimeProperties());
	}

	protected void getInPort(TypeOutputComponent typeOutputComponent) {
		LOGGER.debug("Generating Input Ports for -{}",COMPONENT_NAME);
		int portCounter = 1;
		if (typeOutputComponent.getInSocket() != null) {
			for (TypeOutputInSocket inSocket : typeOutputComponent.getInSocket()) {
				if(inSocket.getSchema()!=null)
				{
					propertyMap.put(PropertyNameConstants.SCHEMA.value(),getSchema(inSocket));
				}
				uiComponent.engageInputPort(inSocket.getType() + portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(),
								typeOutputComponent.getId(),
								typeOutputComponent.getInSocket().get(0).getFromSocketId(), 
								typeOutputComponent.getInSocket().get(0).getId()
								
								));
			}
		}
	}
	
	protected abstract Object getSchema(TypeOutputInSocket inSocket) ;
}
