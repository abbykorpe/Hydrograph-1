package com.bitwise.app.engine.ui.converter;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwiseglobal.graph.commontypes.TypeOutputComponent;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;

/**
 * The class OutputUiConverter
 * 
 * @author Bitwise
 * 
 */

public abstract class OutputUiConverter extends UiConverter {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputUiConverter.class);

	/**
	 * Generate common properties of output component.
	 * 
	 * @param
	 * 
	 * @return
	 */
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching common properties for -{}", componentName);
		getInPort((TypeOutputComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(), getRuntimeProperties());
	}

	/**
	 * Create input ports for output component.
	 * 
	 * @param TypeOutputComponent
	 *            output-component's object generated from Jaxb classes.
	 * @return
	 */
	protected void getInPort(TypeOutputComponent typeOutputComponent) {
		LOGGER.debug("Generating Input Ports for -{}", componentName);
		if (typeOutputComponent.getInSocket() != null) {
			for (TypeOutputInSocket inSocket : typeOutputComponent.getInSocket()) {
				if (inSocket.getSchema() != null) {
					propertyMap.put(PropertyNameConstants.SCHEMA.value(), getSchema(inSocket));
				}
				uiComponent.engageInputPort(inSocket.getId());
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(), typeOutputComponent.getId(), inSocket
								.getFromSocketId(), inSocket.getId()

						));
			}
		}
	}

	/**
	 * Create schema for for Input Component.
	 * 
	 * @param TypeOutputInSocket
	 *            the inSocket i.e input port on which schema is applied, every input port has its own schema.
	 * 
	 * @return Object
	 */

	protected abstract Object getSchema(TypeOutputInSocket inSocket);
}
