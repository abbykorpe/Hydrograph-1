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

 
package com.bitwise.app.graph.factory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.controller.ComponentLabelEditPart;
import com.bitwise.app.graph.controller.ContainerEditPart;
import com.bitwise.app.graph.controller.LinkEditPart;
import com.bitwise.app.graph.controller.PortEditPart;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.ComponentLabel;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.Port;

/**
 * Factory class to create the edit part for the given model.
 */
public class ComponentsEditPartFactory implements EditPartFactory{
	private static Logger logger = LogFactory.INSTANCE.getLogger(ComponentsEditPartFactory.class);
	/**
	 * Creates edit parts for given model.
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// get EditPart for model element
		EditPart part = null;
		if (model instanceof Container) {
			part = new ContainerEditPart();
		}
		else if (model instanceof Component) {
			part = new ComponentEditPart();
		}
		else if (model instanceof Link) {
			part = new LinkEditPart();
		}
		else if (model instanceof Port){
			part = new PortEditPart();
		}
		else if (model instanceof ComponentLabel){
			part = new ComponentLabelEditPart();
		}
		else{
			logger.error("Can't create edit part for model element {}", ((model != null) ? model.getClass().getName() : "null"));
			throw new RuntimeException("Can't create edit part for model element: "	+ 
						((model != null) ? model.getClass().getName() : "null"));
		}
		logger.debug("Created edit part for : {}", model.getClass().getName()); 
		// store model element in EditPart
		part.setModel(model);
		return part;
	}
}
