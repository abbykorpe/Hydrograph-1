package com.bitwise.app.engine.ui.converter;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsPort;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutputComponent;

public abstract class OutputUIConverter extends UIConverter {

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		getInPort((TypeOutputComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),getRuntimeProperties());
	}

	protected void getInPort(TypeOutputComponent typeOutputComponent) {
		int portCounter = 1;
		if (typeOutputComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : typeOutputComponent.getInSocket()) {
				uiComponent.engageInputPort((UIComponentsPort.getPortType(inSocket.getType())) + portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(),
								typeOutputComponent.getId(),
								typeOutputComponent.getInSocket().get(0).getFromSocketId(), 
								typeOutputComponent.getInSocket().get(0).getId()
								
								));
			}
		}
	}
	
	protected void getInPortFromSingleOutPort(TypeOutputComponent typeOutputComponent) {
		int portCounter = 1;
		int linkCounter = 1;
		if (typeOutputComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : typeOutputComponent.getInSocket()) {
				uiComponent.engageInputPort((UIComponentsPort.getPortType(inSocket.getType())) + portCounter);
				
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(),
								typeOutputComponent.getId(),
								typeOutputComponent.getInSocket().get(0).getFromSocketType(), 
								typeOutputComponent.getInSocket().get(0).getId()
								
								));
			}
		}
	}

	
	protected abstract Object getSchema() ;
}
