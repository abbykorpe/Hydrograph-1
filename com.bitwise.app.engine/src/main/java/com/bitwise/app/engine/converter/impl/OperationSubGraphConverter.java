package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.converter.SubgraphConverter;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.Subgraph;

/**
 * Operation type sub graph converter
 * @author Bitwise
 *
 */
public class OperationSubGraphConverter extends SubgraphConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(OperationSubGraphConverter.class);
	private ConverterHelper converterHelper;
	private String JOBPATH="path";
	private String JOBEXTENSION=".job";
	private String XMLEXTENSION=".xml";

	public OperationSubGraphConverter(Component component) {
		super();
		this.baseComponent = new Subgraph();
		this.component = component;
		this.properties = component.getProperties();
		converterHelper=new ConverterHelper(component);
	}

	/*
	 * 
	 *Prepare Operation type sub graph.
	 */
	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Subgraph subgraph = (Subgraph) baseComponent;
		Subgraph.Path path = new Subgraph.Path();
		String[] temp;
		String subGraphFilePath=	((String)properties.get(JOBPATH)).replace(JOBEXTENSION, XMLEXTENSION);
		temp = subGraphFilePath.split("\\\\",3);
		path.setUri(temp[temp.length-1].replaceAll("\\\\", "/"));
		subgraph.setPath(path);
		subgraph.setSubgraphParameter(getRuntimeProperties());
		 
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
