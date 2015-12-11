package com.bitwise.app.engine.ui.converter;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwiseglobal.graph.commontypes.TypeInputComponent;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;

public abstract class InputUiConverter extends UiConverter {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputUiConverter.class);
		@Override
		public void prepareUIXML() {
			super.prepareUIXML();
			LOGGER.debug("Fetching common properties for "+COMPONENT_NAME);
			getOutPort((TypeInputComponent)typeBaseComponent);
			propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),getRuntimeProperties());
		}
	protected void getOutPort(TypeInputComponent inputComponent) {
		LOGGER.debug("Generating OutPut ports for "+COMPONENT_NAME);
		int portCounter = 1;
		if (inputComponent.getOutSocket() != null) {
			for (TypeInputOutSocket outSocket : inputComponent.getOutSocket()) {
				uiComponent.engageOutputPort(outSocket.getType() + portCounter);
				if(outSocket.getSchema()!=null)
					{
						propertyMap.put(PropertyNameConstants.SCHEMA.value(),getSchema(outSocket));
					}
				portCounter++;
			}
		}
	}
	
	protected abstract Object getSchema(TypeInputOutSocket outSocket) ;
		
}