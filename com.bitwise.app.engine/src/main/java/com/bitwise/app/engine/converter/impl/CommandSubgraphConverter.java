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

 
package com.bitwise.app.engine.converter.impl;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.converter.Converter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commandtypes.Subgraph;

public class CommandSubgraphConverter extends Converter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputSubGraphConverter.class);

	public CommandSubgraphConverter(Component component) {
		super();
		this.baseComponent = new Subgraph();
		this.component = component;
		this.properties = component.getProperties();

	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Subgraph subgraph = (Subgraph) baseComponent;
		if (properties.get(Constants.JOB_PATH) != null) {
			Subgraph.Path path = new Subgraph.Path();
			String subGraphFilePath = getSubGraphAbsolutePath(((String) properties.get(Constants.JOB_PATH)).replace(
					Constants.JOB_EXTENSION, Constants.XML_EXTENSION));
			path.setUri(subGraphFilePath);
			subgraph.setPath(path);
		}
		subgraph.setSubgraphParameter(getRuntimeProperties());

	}

}
