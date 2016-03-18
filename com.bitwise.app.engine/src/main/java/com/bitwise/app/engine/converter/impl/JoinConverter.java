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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.JoinConfigProperty;
import com.bitwise.app.common.datastructure.property.JoinMappingGrid;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ParameterUtil;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.engine.xpath.ComponentXpathConstants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeMapField;
import com.bitwiseglobal.graph.commontypes.TypeOperationInputFields;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.join.TypeKeyFields;
import com.bitwiseglobal.graph.operationstypes.Join;

public class JoinConverter extends TransformConverter {
	private static final String JOIN_OPERATION_ID = "join";

	private static final Logger logger = LogFactory.INSTANCE.getLogger(JoinConverter.class);
	private ConverterHelper converterHelper;
	private JoinMappingGrid joinupPropertyGrid;

	public JoinConverter(Component component) {
		super();
		this.baseComponent = new Join();
		this.component = component;
		this.properties = component.getProperties();
		joinupPropertyGrid = (JoinMappingGrid) properties.get(Constants.JOIN_MAP_FIELD);
		converterHelper = new ConverterHelper(component);
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Join join = (Join) baseComponent;
		if (properties.get(Constants.JOIN_CONFIG_FIELD) != null) {
			join.getKeys().addAll(getJoinConfigKeys());
		}
	}

	private List<TypeKeyFields> getJoinConfigKeys() {
		List<TypeKeyFields> typeKeyFieldsList = null;
		List<JoinConfigProperty> keyFields = (List<JoinConfigProperty>) properties.get(Constants.JOIN_CONFIG_FIELD);
		typeKeyFieldsList = new ArrayList<>();
		if (keyFields != null) {
			
			for (JoinConfigProperty entry : keyFields) {
				TypeKeyFields typeKeyField = new TypeKeyFields();
				String[] data = entry.getJoinKey().split(",");
				typeKeyField.setInSocketId(entry.getPortIndex());
				typeKeyField.setRecordRequired(getRecordRequiredValue(entry));
				typeKeyFieldsList.add(typeKeyField);
				
				for (String key : data) {
					if (!ParameterUtil.INSTANCE.isParameter(key)) {
						TypeFieldName fieldName = new TypeFieldName();
						fieldName.setName(key);
						typeKeyField.getField().add(fieldName);
					} else {
						converterHelper.getParamTag(this.ID, key, ComponentXpathConstants.JOIN_KEYS.value().replace("$inSocketId", entry.getPortIndex()));
					}
				}
			}
		}
		return typeKeyFieldsList;
	}

	protected boolean getRecordRequiredValue(JoinConfigProperty entry) {
		boolean recordRequired=false;
		if(entry.getRecordRequired()==0)
		{
			recordRequired=true;
		}
		else 
		{
			recordRequired=false;
		}
		return recordRequired;
	
	}

	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {

		JoinMappingGrid joinMappingGrid = (JoinMappingGrid) properties.get(Constants.JOIN_MAP_FIELD);
		List<TypeOperationsOutSocket> outSocketList = new ArrayList<TypeOperationsOutSocket>();
		for (Link link : component.getSourceConnections()) {

			TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
			if (PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getPortType()).equalsIgnoreCase("out")) {
				if (joinMappingGrid != null && !joinMappingGrid.isSelected()) {
					outSocket.setId(link.getSourceTerminal());
					outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
					outSocketList.add(outSocket);
					outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(
							getLookupOrJoinOutputMapping(joinupPropertyGrid));
				} else {
					if (joinMappingGrid != null) {
						TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
						outSocketAsInsocket.setInSocketId(joinMappingGrid.getButtonText().substring(8));
						outSocket.setId(link.getSourceTerminal());
						outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
						outSocket.setCopyOfInsocket(outSocketAsInsocket);
						outSocketList.add(outSocket);
					}
				}
			} else if (PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getPortType())
					.equalsIgnoreCase("unused")) {
				TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
				outSocketAsInsocket.setInSocketId(Constants.INPUT_SOCKET_TYPE + link.getSourceTerminal().substring(6));
				outSocket.setId(link.getSourceTerminal());
				outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
				outSocket.setCopyOfInsocket(outSocketAsInsocket);
				outSocketList.add(outSocket);
			}
		}
		return outSocketList;
	}

	@Override
	protected List<TypeTransformOperation> getOperations() {
		List<TypeTransformOperation> operationList = new ArrayList<>();
		TypeTransformOperation operation = new TypeTransformOperation();
		TypeOperationInputFields operationInputFields = new TypeOperationInputFields();
		operationInputFields.getField().addAll(getOperationField());
		operation.setInputFields(operationInputFields);
		operation.setId(JOIN_OPERATION_ID);
		if (properties.get(PropertyNameConstants.OPERATION_CLASS.value()) != null)
			operation.setClazz(((OperationClassProperty) properties.get(PropertyNameConstants.OPERATION_CLASS.value()))
					.getOperationClassPath());
		operationList.add(operation);
		return operationList;
	}

	private List<TypeInputField> getOperationField() {
		logger.debug("Genrating TypeInputField data :{}", properties.get(Constants.PARAM_NAME));
		List<TypeInputField> operationFiledList = new ArrayList<>();
		Set<String> componentOperationFileds = (HashSet<String>) component.getProperties().get(
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
			inSocket.setFromSocketId(converterHelper.getFromSocketId(link));
			inSocket.setId(link.getTargetTerminal());
			inSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());
			inSocket.getOtherAttributes();
			inSocketsList.add(inSocket);
		}
		return inSocketsList;
	}

	public List<Object> getLookupOrJoinOutputMapping(JoinMappingGrid joinPropertyGrid) {
		List<Object> passThroughFieldorMapFieldList = null;
		if (joinPropertyGrid != null) {
			passThroughFieldorMapFieldList = new ArrayList<>();
			TypeInputField typeInputField = null;
			TypeMapField mapField = null;
			for (LookupMapProperty entry : joinPropertyGrid.getLookupMapProperties()) {
				String[] sourceNameValue = entry.getSource_Field().split(Pattern.quote("."));

				if (sourceNameValue[1].equalsIgnoreCase(entry.getOutput_Field())) {
					typeInputField = new TypeInputField();
					typeInputField.setName(sourceNameValue[1]);
					typeInputField.setInSocketId(sourceNameValue[0]);
					passThroughFieldorMapFieldList.add(typeInputField);
				} else {
					mapField = new TypeMapField();
					mapField.setSourceName(sourceNameValue[1]);
					mapField.setName(entry.getOutput_Field());
					mapField.setInSocketId(sourceNameValue[0]);
					passThroughFieldorMapFieldList.add(mapField);
				}
			}
		}
		return passThroughFieldorMapFieldList;
	}

}
