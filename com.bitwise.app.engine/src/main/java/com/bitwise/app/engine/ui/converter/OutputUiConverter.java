package com.bitwise.app.engine.ui.converter;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsPort;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutputComponent;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;

public abstract class OutputUiConverter extends UiConverter {

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		getInPort((TypeOutputComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),getRuntimeProperties());
	}

	protected void getInPort(TypeOutputComponent typeOutputComponent) {
		int portCounter = 1;
		if (typeOutputComponent.getInSocket() != null) {
			for (TypeOutputInSocket inSocket : typeOutputComponent.getInSocket()) {
				if(inSocket.getSchema()!=null)
					getSchema(inSocket);						
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
	
	
	protected abstract Object getSchema(TypeOutputInSocket inSocket) ;
}
