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

package hydrograph.ui.graph.model.utils;

import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;

/**
 * @author Bitwise
 *
 */
public class ComponentNameValidator {

	public static final ComponentNameValidator INSTANCE = new ComponentNameValidator();

	/**
	 * @param container
	 * @param componentName
	 *            return true if given component name is not match with other
	 *            component in canvas.
	 * @return boolean
	 */
	public boolean isUniqueComponentName(Container container, String componentName) {
		boolean result = true;
		if (container == null || componentName == null) {
			return false;
		}
		for (Component component : container.getChildren()) {
			if (component.getComponentLabel().getLabelContents().equals(componentName)) {
				result = false;
			}
		}
		return result;
	}

}
