package com.bitwise.app.engine.ui.converter;

import java.util.TreeMap;

import org.eclipse.draw2d.geometry.Dimension;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsPort;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;

public abstract class TransformUIConverter extends UIConverter {

	@Override
	public void prepareUIXML() {
		// TODO Auto-generated method stub
		super.prepareUIXML();
		getInPort((TypeOperationsComponent) typeBaseComponent);
		getOutPort((TypeOperationsComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),getRuntimeProperties());
	}

	protected void getInPort(TypeOperationsComponent operationsComponent) {
		int portCounter = 1;
		if (operationsComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : operationsComponent
					.getInSocket()) {
				uiComponent.engageInputPort((UIComponentsPort
						.getPortType(inSocket.getType())) + portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(),
								operationsComponent.getId(),
								operationsComponent.getInSocket().get(0).getFromSocketId(),
								operationsComponent.getInSocket().get(0).getId()
							));
				portCounter++;
			}
		}
	}

	protected void getOutPort(TypeOperationsComponent operationsComponent) {
		int portCounter = 1;
		Dimension newSize = uiComponent.getSize();
		if (operationsComponent.getOutSocket() != null) {
			for (TypeOperationsOutSocket outSocket : operationsComponent.getOutSocket()) {
				uiComponent.engageOutputPort((UIComponentsPort.getPortType(outSocket.getType())) + portCounter);
				if (portCounter != 1) {
					uiComponent.setSize(newSize.expand(0, 15));
				}
				portCounter++;
			}
		}
	}
	
	@Override
	protected TreeMap<String,String> getRuntimeProperties()
	{
		TreeMap<String,String> runtimeMap=null;
		TypeProperties typeProperties = ((TypeOperationsComponent)typeBaseComponent).getRuntimeProperties();
		if(typeProperties!=null ){
			runtimeMap=new TreeMap<>();
					for(Property runtimeProperty:typeProperties.getProperty()){
						runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
					}
		}
		return runtimeMap;
	}
	
	
}
