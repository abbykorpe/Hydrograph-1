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
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.InputConverter;
import hydrograph.ui.engine.helper.ConverterHelper;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.engine.jaxb.inputtypes.Subgraph;
/**
 * 
 * @author Bitwise
 *Input type sub graph converter.
 */
public class InputSubJobConverter extends InputConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputSubJobConverter.class);
	
	public InputSubJobConverter(Component component) {
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
		Subgraph.Path path = new Subgraph.Path();
		if(properties.get(Constants.JOB_PATH)!=null){
			String subJobFilePath = getSubJobAbsolutePath(((String)properties.get(Constants.JOB_PATH)).replace(Constants.JOB_EXTENSION, Constants.XML_EXTENSION));
			path.setUri(subJobFilePath);
			subjob.setPath(path);
		}
		subjob.setSubgraphParameter(getRuntimeProperties());
		
	}

	@Override
	protected List<TypeInputOutSocket> getInOutSocket() {
		List<TypeInputOutSocket> outSockets = new ArrayList<>();
		for (Link link : component.getSourceConnections()) {
			TypeInputOutSocket outSocket = new TypeInputOutSocket();
			outSocket.setId(link.getSourceTerminal());
			outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
			outSocket.setSchema(getSchema());
			outSocket.getOtherAttributes();
			outSockets.add(outSocket);
		}
		return outSockets;
	}

	@Override
	protected List<TypeBaseField> getFieldOrRecord(List<GridRow> gridList) {
		logger.debug("Generating data for {} for property {}", new Object[] { properties.get(Constants.PARAM_NAME),
				PropertyNameConstants.SCHEMA.value() });

		List<TypeBaseField> typeBaseFields = new ArrayList<>();

		if (gridList != null && gridList.size() != 0) {
			for (GridRow object : gridList)
				typeBaseFields.add(converterHelper.getFixedWidthTargetData((FixedWidthGridRow) object));
		}
		return typeBaseFields;
	}


}