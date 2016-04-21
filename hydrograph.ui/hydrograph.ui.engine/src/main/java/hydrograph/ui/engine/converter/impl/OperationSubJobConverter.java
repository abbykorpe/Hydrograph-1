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
import hydrograph.ui.engine.converter.SubjobConverter;
import hydrograph.ui.engine.helper.ConverterHelper;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeOutSocketAsInSocket;
import hydrograph.engine.jaxb.commontypes.TypeTransformOperation;
import hydrograph.engine.jaxb.operationstypes.Subgraph;

/**
 * Operation type sub graph converter
 * @author Bitwise
 *
 */
public class OperationSubJobConverter extends SubjobConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(OperationSubJobConverter.class);

	public OperationSubJobConverter(Component component) {
		super(component);
		this.baseComponent = new Subgraph();
		this.component = component;
		this.properties = component.getProperties();
	}

	/*
	 * 
	 *Prepare Operation type sub graph.
	 */
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
 
	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {
		logger.debug("Generating TypeOperationsOutSocket data for : {}", properties.get(Constants.PARAM_NAME));
		List<TypeOperationsOutSocket> outSocketList = new ArrayList<TypeOperationsOutSocket>();
		if (component.getSourceConnections() != null && !component.getSourceConnections().isEmpty()) {
			for (Link link : component.getSourceConnections()) {
				TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
				TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
				outSocket.setId(link.getSourceTerminal());
				outSocketAsInsocket.setInSocketId(link.getTargetTerminal());
				outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
				outSocket.getOtherAttributes();
				outSocketList.add(outSocket);
			}
		}
		return outSocketList;
	}

	@Override
	protected List<TypeTransformOperation> getOperations() {
		return null;
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
		logger.debug("Generating TypeBaseInSocket data for :{}", component.getProperties().get(Constants.PARAM_NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		if (component.getTargetConnections() != null || !component.getTargetConnections().isEmpty()) {
			for (Link link : component.getTargetConnections()) {
				TypeBaseInSocket inSocket = new TypeBaseInSocket();
				inSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
				inSocket.setFromSocketId(converterHelper.getFromSocketId(link));
				inSocket.setId(link.getTargetTerminal());
				inSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());
				inSocket.getOtherAttributes();
				inSocketsList.add(inSocket);
			}
		} 
		return inSocketsList;
	}	

}
