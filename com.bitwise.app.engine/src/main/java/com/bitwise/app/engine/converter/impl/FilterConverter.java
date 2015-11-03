package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.converter.PortTypeConstant;
import com.bitwise.app.engine.converter.PropertyNameConstants;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.exceptions.PhaseException;
import com.bitwise.app.engine.exceptions.SchemaException;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.propertywindow.datastructures.filter.OperationClassProperty;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeOperationInputFields;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.operationstypes.Filter;

/**
 * Converter implementation for Filter component
 */
public class FilterConverter extends TransformConverter {
	private static final String FILTER_OPERATION_ID="opt";
	private static final String DEFAULT_IN_SOCKET_ID = "in0";
	Logger LOGGER = LogFactory.INSTANCE.getLogger(FilterConverter.class);
	
	public FilterConverter(Component component) {
		super();	
		this.baseComponent = new Filter();
		this.component = component;
		this.properties = component.getProperties();
	}

	@Override
	public void prepareForXML() throws PhaseException, SchemaException {
		LOGGER.debug("Genrating XML for :{}", properties.get(NAME));
		super.prepareForXML();
	}


	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {
		LOGGER.debug("Genrating TypeStraightPullOutSocket data for : {}",
				properties.get(NAME));
		List<TypeOperationsOutSocket> outSockectList = new ArrayList<TypeOperationsOutSocket>();
		String temp,temp2;
		int outSocketCounter=1;
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
			outSocketCounter++;
		}
		return outSockectList;
	}

	@Override
	protected List<TypeTransformOperation> getOperations() {
		LOGGER.debug("Genrating TypeTransformOperation data :{}", properties.get(NAME));
		List<TypeTransformOperation> operationList = new ArrayList<>();
		TypeTransformOperation operation = new TypeTransformOperation();
		TypeOperationInputFields operationInputFields=new TypeOperationInputFields();
		operationInputFields.getField().addAll(getOperationField());
		operation.setInputFields(operationInputFields);
		operation.setId(FILTER_OPERATION_ID);
		if(properties.get(PropertyNameConstants.OPERATION_CLASS.value())!=null)
		operation.setClazz(((OperationClassProperty)properties.get(PropertyNameConstants.OPERATION_CLASS.value())).getOperationClassPath());
		operationList.add(operation);
		return operationList;
	}

	private List<TypeInputField> getOperationField() {
		LOGGER.debug("Genrating TypeInputField data :{}", properties.get(NAME));
		List<TypeInputField> operationFiledList=new ArrayList<>();
		Set<String> componentOperationFileds = (HashSet<String>) component.getProperties().get(PropertyNameConstants.OPERATION_FILEDS.value());
		if(componentOperationFileds!=null){
			for(String object:componentOperationFileds){
				TypeInputField operationFiled=new TypeInputField();
				operationFiled.setName(object);
				operationFiled.setInSocketId(DEFAULT_IN_SOCKET_ID);
				operationFiledList.add(operationFiled);
			}
		}
		return operationFiledList;
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
			LOGGER.debug("Genrating TypeBaseInSocket data for :{}", component
					.getProperties().get(NAME));
			List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
			for (Link link : component.getTargetConnections()) {
				TypeBaseInSocket inSocket = new TypeBaseInSocket();
				inSocket.setFromComponentId((String) link.getSource()
						.getProperties().get(NAME));
				inSocket.setFromSocketId(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort());
				inSocket.setId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
				inSocket.setType(PortTypeConstant.getPortType(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort()));
				inSocket.getOtherAttributes();
				inSocketsList.add(inSocket);
			}
			return inSocketsList;
		}

	
}
