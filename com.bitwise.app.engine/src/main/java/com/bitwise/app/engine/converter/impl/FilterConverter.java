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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ParameterUtil;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.engine.xpath.ComponentXpathConstants;
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
 * @author Bitwise Converter implementation for Filter component
 */
public class FilterConverter extends TransformConverter {
	private static final String FILTER_OPERATION_ID = "filter_opt";
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
		List<String> componentOperationFields = (List<String>) component.getProperties().get(
				PropertyNameConstants.OPERATION_FILEDS.value());
		if (componentOperationFields != null && !componentOperationFields.isEmpty()) {
			if (!isALLParameterizedFields(componentOperationFields)) {
				for (String fieldName : componentOperationFields) {
					if (!ParameterUtil.isParameter(fieldName)) {
						TypeInputField operationField = new TypeInputField();
						operationField.setName(fieldName);
						operationField.setInSocketId(Constants.FIXED_INSOCKET_ID);
						operationFiledList.add(operationField);
					} else {
						converterHelper.addParamTag(this.ID, fieldName,	ComponentXpathConstants.FILTER_INPUT_FIELDS.value(),false);
					}
				}
			} else {
				StringBuffer parameterFieldNames=new StringBuffer();
				TypeInputField operationField = new TypeInputField();
				operationField.setName("");
				operationFiledList.add(operationField);
				for (String fieldName : componentOperationFields) 
					parameterFieldNames.append(fieldName+ " ");
					converterHelper.addParamTag(this.ID, parameterFieldNames.toString(), ComponentXpathConstants.FILTER_INPUT_FIELDS.value(),true);
				
			}
		}
		return operationFiledList;
	}


	private boolean isALLParameterizedFields(List<String> componentOperationFields){
		for (String fieldName : componentOperationFields) 
			if (!ParameterUtil.isParameter(fieldName)) 
				return false;
		return true;
	}

	@Override
	public List<TypeBaseInSocket> getInSocket() {
		logger.debug("Generating TypeBaseInSocket data for :{}", component.getProperties().get(Constants.PARAM_NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeBaseInSocket inSocket = new TypeBaseInSocket();
			inSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));

			inSocket.setFromSocketId(converterHelper.getFromSocketId(link));
			inSocket.setId(link.getTargetTerminal());
			inSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());
			inSocket.getOtherAttributes();
			inSocketsList.add(inSocket);
		}
		return inSocketsList;
	}

}
