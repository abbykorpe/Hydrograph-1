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

 
package com.bitwise.app.graph.model.components;

import org.apache.commons.lang.StringUtils;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.categories.SubgraphCategory;

/**
 * Return sub graph component converter.
 * 
 * @author Bitwise
 * 
 */
public class SubgraphComponent extends SubgraphCategory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2406782326279531800L;

	@Override
	public String getConverter() {
		String type = (String) this.getProperties().get(Constants.TYPE);
		if (StringUtils.isNotBlank(type)) {
			if (type.equalsIgnoreCase(Constants.INPUT))
				return "com.bitwise.app.engine.converter.impl.InputSubGraphConverter";
			if (type.equalsIgnoreCase(Constants.OUTPUT))
				return "com.bitwise.app.engine.converter.impl.OutputSubGraphConverter";
			if (type.equalsIgnoreCase(Constants.OPERATION))
				return "com.bitwise.app.engine.converter.impl.OperationSubGraphConverter";
		
		}
		return "com.bitwise.app.engine.converter.impl.CommandSubgraphConverter";

	}

}
