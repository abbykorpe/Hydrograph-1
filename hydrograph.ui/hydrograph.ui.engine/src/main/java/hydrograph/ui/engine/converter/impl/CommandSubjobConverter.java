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

 
package hydrograph.ui.engine.converter.impl;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.engine.converter.Converter;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.logging.factory.LogFactory;

import org.slf4j.Logger;

import hydrograph.engine.jaxb.commandtypes.Subgraph;

public class CommandSubjobConverter extends Converter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputSubJobConverter.class);

	public CommandSubjobConverter(Component component) {
		super(component);
		this.baseComponent = new Subgraph();
		this.component = component;
		this.properties = component.getProperties();

	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Subgraph subjob = (Subgraph) baseComponent;
		if (properties.get(Constants.JOB_PATH) != null) {
			Subgraph.Path path = new Subgraph.Path();
			String subJobFilePath = getSubJobAbsolutePath(((String) properties.get(Constants.JOB_PATH)).replace(
					Constants.JOB_EXTENSION, Constants.XML_EXTENSION));
			path.setUri(subJobFilePath);
			subjob.setPath(path);
		}
		subjob.setSubgraphParameter(getRuntimeProperties());

	}

}