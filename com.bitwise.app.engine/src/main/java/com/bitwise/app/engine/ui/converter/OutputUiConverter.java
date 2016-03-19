/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package com.bitwise.app.engine.ui.converter;

import org.slf4j.Logger;

import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.logging.factory.LogFactory;
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
