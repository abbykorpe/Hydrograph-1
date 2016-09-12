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
package hydrograph.ui.graph.model.components;

import hydrograph.ui.graph.model.categories.CommandCategory;

/**
 * The Class SQL Component.
 * 
 * @author Bitwise
 */
public class SQLComponent extends CommandCategory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4128149239279856767L;

	/**
	 * Instantiates a new replicate.
	 */
	public SQLComponent() {
		super();
	}

	@Override
	public String getConverter() {
		return "hydrograph.ui.engine.converter.impl.SQLConverter";
	}

}