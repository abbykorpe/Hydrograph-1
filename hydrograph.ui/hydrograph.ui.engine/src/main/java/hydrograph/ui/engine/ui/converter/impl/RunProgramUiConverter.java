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

package hydrograph.ui.engine.ui.converter.impl;

import java.util.LinkedHashMap;

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.straightpulltypes.Clone;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.CommandUiConverter;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.RunProgram;
import hydrograph.ui.logging.factory.LogFactory;

import org.slf4j.Logger;

/**
 * The class RunProgramUiConverter
 * 
 * @author Bitwise
 * 
 */
public class RunProgramUiConverter extends CommandUiConverter {

	private Clone clone;
	private static final Logger LOGGER = LogFactory.INSTANCE
			.getLogger(RunProgramUiConverter.class);

	public RunProgramUiConverter(TypeBaseComponent typeBaseComponent,
			Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new RunProgram();
		this.propertyMap = new LinkedHashMap<>();		
	}
	
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching COMMAND-Properties for -{}", componentName);
		clone = (Clone) typeBaseComponent;	
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(clone.getId());
		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.RUNPROGRAM.value());
	
		//validateComponentProperties(propertyMap);		
		
	}

}
