package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeOperationInputFields;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.Filter;

/**
 * Converter implementation for Filter component
 */
public class FilterConverter extends TransformConverter {
	private static final String FILTER_OPERATION_ID = "opt";
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterConverter.class);
	private ConverterHelper converterHelper;

	public FilterConverter(Component component) {
		super();
		this.baseComponent = new Filter();
		this.component = component;
		this.properties = component.getProperties();
		converterHelper = new ConverterHelper(component);
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Filter filter = (Filter) baseComponent;
		filter.getOperation().addAll(getOperations());
	}

	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {
		logger.debug("Generating TypeStraightPullOutSocket data for : {}", properties.get(Constants.PARAM_NAME));
		List<TypeOperationsOutSocket> outSockectList = new ArrayList<TypeOperationsOutSocket>();
		for (Link link : component.getSourceConnections()) {
			TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
			TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
			outSocketAsInsocket.setInSocketId(Constants.FIXED_INSOCKET_ID);
			outSocketAsInsocket.getOtherAttributes();
			outSocket.setCopyOfInsocket(outSocketAsInsocket);

			outSocket.setId(link.getSourceTerminal());
			outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());

			outSocket.getOtherAttributes();
			outSockectList.add(outSocket);
		}
		return outSockectList;
	}

	@Override
	protected List<TypeTransformOperation> getOperations() {
		logger.debug("Generating TypeTransformOperation data :{}", properties.get(Constants.PARAM_NAME));
		List<TypeTransformOperation> operationList = new ArrayList<>();
		TypeTransformOperation operation = new TypeTransformOperation();
		TypeOperationInputFields operationInputFields = new TypeOperationInputFields();
		operationInputFields.getField().addAll(getOperationField());
		operation.setInputFields(operationInputFields);
		operation.setId(FILTER_OPERATION_ID);
		if (properties.get(PropertyNameConstants.OPERATION_CLASS.value()) != null)
			operation.setClazz(((OperationClassProperty) properties.get(PropertyNameConstants.OPERATION_CLASS.value()))
					.getOperationClassPath());
		operationList.add(operation);
		return operationList;
	}

	private List<TypeInputField> getOperationField() {
		logger.debug("Generating TypeInputField data :{}", properties.get(Constants.PARAM_NAME));
		List<TypeInputField> operationFiledList = new ArrayList<>();
		List<String> componentOperationFileds = (List<String>) component.getProperties().get(
				PropertyNameConstants.OPERATION_FILEDS.value());
		if (componentOperationFileds != null) {
			for (String object : componentOperationFileds) {
				TypeInputField operationFiled = new TypeInputField();
				operationFiled.setName(object);
				operationFiled.setInSocketId(Constants.FIXED_INSOCKET_ID);
				operationFiledList.add(operationFiled);
			}
		}
		return operationFiledList;
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
		logger.debug("Generating TypeBaseInSocket data for :{}", component.getProperties().get(Constants.PARAM_NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeBaseInSocket inSocket = new TypeBaseInSocket();
			inSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));

			if (converterHelper.isMultipleLinkAllowed(link.getSource(), link.getSourceTerminal()))
				inSocket.setFromSocketId(link.getSource().getPort(link.getSourceTerminal()).getPortType()
						+ link.getLinkNumber());
			else
				inSocket.setFromSocketId(link.getSourceTerminal());
			inSocket.setId(link.getTargetTerminal());
			inSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());
			inSocket.getOtherAttributes();
			inSocketsList.add(inSocket);
		}
		return inSocketsList;
	}

}
