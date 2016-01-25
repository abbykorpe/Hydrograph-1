package com.bitwise.app.engine.ui.converter;

import org.slf4j.Logger;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeInputComponent;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;

/**
 * The class InputUiConverter
 * 
 * @author Bitwise
 * 
 */

public abstract class InputUiConverter extends UiConverter {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputUiConverter.class);

	@Override
	/**
	 * Generate common properties of input components.
	 * 
	 * @param 
	 *            
	 * @return 
	 */
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching common properties for " + componentName);
		getOutPort((TypeInputComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(), getRuntimeProperties());
	}

	/**
	 * Create output ports for input component.
	 * 
	 * @param TypeInputComponent
	 *            input-component's object generated from Jaxb classes.
	 * @return
	 */
	protected void getOutPort(TypeInputComponent inputComponent) {
		LOGGER.debug("Generating OutPut ports for " + componentName);
		if (inputComponent.getOutSocket() != null) {
			for (TypeInputOutSocket outSocket : inputComponent.getOutSocket()) {
				uiComponent.engageOutputPort(outSocket.getId());
				if (outSocket.getSchema() != null) {
					propertyMap.put(PropertyNameConstants.SCHEMA.value(), getSchema(outSocket));
				}
				
			}
		}
	}

	/**
	 * Create schema for for Input Component.
	 * 
	 * @param TypeInputOutSocket
	 *            the TypeInputOutSocket i.e output port on which schema is applied, every output port has its own
	 *            schema.
	 * 
	 * @return Object
	 */
	protected abstract Object getSchema(TypeInputOutSocket outSocket);

}