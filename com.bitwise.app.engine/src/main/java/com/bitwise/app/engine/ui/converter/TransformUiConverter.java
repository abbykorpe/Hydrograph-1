package com.bitwise.app.engine.ui.converter;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;

public abstract class TransformUiConverter extends UiConverter {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(TransformUiConverter.class);
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching common properties for -{}",COMPONENT_NAME);
		getInPort((TypeOperationsComponent) typeBaseComponent);
		getOutPort((TypeOperationsComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),getRuntimeProperties());
	}

	protected void getInPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating InPut Ports for -{}",COMPONENT_NAME);
		int portCounter = 1;
		if (operationsComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : operationsComponent
					.getInSocket()) {
				uiComponent.engageInputPort(inSocket.getType() + portCounter);
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
		LOGGER.debug("Generating OutPut Ports for -{}",COMPONENT_NAME);
		int portCounter = 0;
		int unusedportCounter=0;
		if (operationsComponent.getOutSocket() != null) {
			for (TypeOperationsOutSocket outSocket : operationsComponent.getOutSocket()) {
				if(outSocket.getType().equals("unused"))
				uiComponent.engageOutputPort(outSocket.getType() + (++unusedportCounter));
				else
					uiComponent.engageOutputPort(outSocket.getType() + (++portCounter));
				
				}
			
		}
	}
	
	@Override
	protected Map<String,String> getRuntimeProperties()
	{
		LOGGER.debug("Generating Runtime Properties for -{}",COMPONENT_NAME);
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
