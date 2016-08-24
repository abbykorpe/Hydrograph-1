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

package hydrograph.ui.propertywindow.validators;

import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.utils.Utils;

/**
 * 
 * This class checks if component schema is in sync with its mapping.
 * 
 * @author Bitwise
 *
 */
public class ComponentSchemaMapValidator implements IComponentValidator {
	
	@Override
	public String validateComponent(Component component){
		if(!Utils.INSTANCE.isMappingAndSchemaAreInSync(component.getType(), component.getProperties())){
			return Messages.SCHEMA_IS_NOT_IN_SYNC_WITH_MAPPING;
		}else{
			return null;
		}
	}	
}