package com.bitwise.app.engine.ui.converter;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;

public abstract class StraightfullUiConverter extends UiConverter {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(StraightfullUiConverter.class);
	
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching common properties for -{}",COMPONENT_NAME);
		getInPort((TypeStraightPullComponent) typeBaseComponent);
		getOutPort((TypeStraightPullComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),getRuntimeProperties());
	}

	protected void getInPort(TypeStraightPullComponent straightPullComponent) {
		LOGGER.debug("Generating InPut Ports for -{}",COMPONENT_NAME);
		int portCounter = 1;
		if (straightPullComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : straightPullComponent.getInSocket()) {
				uiComponent.engageInputPort(inSocket.getType() + portCounter);
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
		LOGGER.debug("Generating OutPut Ports for -{}",COMPONENT_NAME);
		int portCounter = 0;
		int unusedportCounter=0;
		if (straightPullComponent.getOutSocket() != null) {
			for (TypeStraightPullOutSocket outSocket : straightPullComponent.getOutSocket()) {
				if(outSocket.getType().equals("unused"))
				uiComponent.engageOutputPort(outSocket.getType() + (++unusedportCounter));
				else
					uiComponent.engageOutputPort(outSocket.getType() + (++portCounter));
				
				}
			
		}
	}
	
	@Override
	protected Map<String,String> getRuntimeProperties()
	{	LOGGER.debug("Generating Runtime Properties for -{}",COMPONENT_NAME);
			TreeMap<String,String> runtimeMap=null;
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
