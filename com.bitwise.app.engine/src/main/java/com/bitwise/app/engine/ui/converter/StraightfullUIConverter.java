package com.bitwise.app.engine.ui.converter;

import java.util.TreeMap;

import org.eclipse.draw2d.geometry.Dimension;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsPort;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;

public abstract class StraightfullUIConverter extends UIConverter {

	@Override
	public void prepareUIXML() {
		// TODO Auto-generated method stub
		super.prepareUIXML();
		getInPort((TypeStraightPullComponent) typeBaseComponent);
		getOutPort((TypeStraightPullComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),getRuntimeProperties());
	}

	protected void getInPort(TypeStraightPullComponent straightPullComponent) {
		int portCounter = 1;
		if (straightPullComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : straightPullComponent
					.getInSocket()) {
				uiComponent.engageInputPort((UIComponentsPort
						.getPortType(inSocket.getType())) + portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(),
								straightPullComponent.getId(),
								straightPullComponent.getInSocket().get(0).getFromSocketId(),
								straightPullComponent.getInSocket().get(0).getId()
							));
				portCounter++;
			}
		}
	}

	protected void getOutPort(TypeStraightPullComponent straightPullComponent) {
		int portCounter = 1;
		Dimension newSize = uiComponent.getSize();
		if (straightPullComponent.getOutSocket() != null) {
			for (TypeStraightPullOutSocket outSocket : straightPullComponent.getOutSocket()) {
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
	{	TreeMap<String,String> runtimeMap=null;
		TypeProperties typeProperties = ((TypeStraightPullComponent)typeBaseComponent).getRuntimeProperties();
		if(typeProperties!=null ){
			runtimeMap=new TreeMap<>();
				for(Property runtimeProperty:typeProperties.getProperty()){
					runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
				}
		}
		return runtimeMap;
	}
}
