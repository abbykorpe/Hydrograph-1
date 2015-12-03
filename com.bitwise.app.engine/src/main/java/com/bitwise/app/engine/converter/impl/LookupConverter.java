package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.HashJoin;

public class LookupConverter extends TransformConverter{

	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterConverter.class);

	public LookupConverter(Component component) {
		super();	
		this.baseComponent = new HashJoin();
		this.component = component;
		this.properties = component.getProperties();
	}
	
	
	@Override
	public void prepareForXML(){
		super.prepareForXML();
		
		
	}
	
	
	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {
		logger.debug("Genrating TypeStraightPullOutSocket data for : {}",
				properties.get(Constants.PARAM_NAME));
		List<TypeOperationsOutSocket> outSockectList = new ArrayList<TypeOperationsOutSocket>();
		for (Link link : component.getSourceConnections()) {
			TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
			TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
			outSocketAsInsocket.setInSocketId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
			outSocketAsInsocket.getOtherAttributes();
			outSocket.setCopyOfInsocket(outSocketAsInsocket);
		
			outSocket.setId(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort());
			outSocket.setType(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort()));
			
			outSocket.getOtherAttributes();
			outSockectList.add(outSocket);
		}
		return outSockectList;
	}

	@Override
	protected List<TypeTransformOperation> getOperations() {
		
		return null;
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
		logger.debug("Genrating TypeBaseInSocket data for :{}", component
				.getProperties().get(Constants.PARAM_NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		int inSocketCounter = 0;
		for (Link link : component.getTargetConnections()) {
			TypeBaseInSocket inSocket = new TypeBaseInSocket();
			inSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
			inSocket.setFromSocketId(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort())+link.getLinkNumber());
			inSocket.setId(PortTypeConstant.getPortType(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort())+ inSocketCounter);
			inSocket.setType(PortTypeConstant.getPortType(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort()));
			inSocket.getOtherAttributes();
			inSocketsList.add(inSocket);
			inSocketCounter++;
		}
		return inSocketsList;
	}

}
