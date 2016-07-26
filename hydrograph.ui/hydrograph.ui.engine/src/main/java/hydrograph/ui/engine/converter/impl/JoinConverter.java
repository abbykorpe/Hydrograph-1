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
import hydrograph.ui.common.util.ParameterUtil;
import hydrograph.ui.datastructure.property.JoinConfigProperty;
import hydrograph.ui.datastructure.property.JoinMappingGrid;
import hydrograph.ui.datastructure.property.LookupMapProperty;
import hydrograph.ui.datastructure.property.OperationClassProperty;
import hydrograph.ui.engine.constants.PortTypeConstant;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.TransformConverter;
import hydrograph.ui.engine.helper.ConverterHelper;
import hydrograph.ui.engine.xpath.ComponentXpathConstants;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;
import hydrograph.engine.jaxb.commontypes.TypeInputField;
import hydrograph.engine.jaxb.commontypes.TypeMapField;
import hydrograph.engine.jaxb.commontypes.TypeOperationInputFields;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeOutSocketAsInSocket;
import hydrograph.engine.jaxb.commontypes.TypeTransformOperation;
import hydrograph.engine.jaxb.join.TypeKeyFields;
import hydrograph.engine.jaxb.operationstypes.Join;

/**
 * @author Bitwise Converter implementation for Join component
 */

public class JoinConverter extends TransformConverter {
	private static final String JOIN_OPERATION_ID = "join";

	private static final Logger logger = LogFactory.INSTANCE.getLogger(JoinConverter.class);
	private JoinMappingGrid joinupPropertyGrid;

	public JoinConverter(Component component) {
		super(component);
		this.baseComponent = new Join();
		this.component = component;
		this.properties = component.getProperties();
		joinupPropertyGrid = (JoinMappingGrid) properties.get(Constants.JOIN_MAP_FIELD);
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Join join = (Join) baseComponent;
		if (properties.get(Constants.JOIN_CONFIG_FIELD) != null) {
			List<TypeKeyFields> typeKeyFields=getJoinConfigKeys();
			if(typeKeyFields!=null && !typeKeyFields.isEmpty())
				join.getKeys().addAll(typeKeyFields);
		}
	}

	private List<TypeKeyFields> getJoinConfigKeys() {
		List<TypeKeyFields> typeKeyFieldsList = null;
		List<JoinConfigProperty> keyFields = (List<JoinConfigProperty>) properties.get(Constants.JOIN_CONFIG_FIELD);
		int portCount=  Integer.parseInt((String)properties.get(Constants.INPUT_PORT_COUNT_PROPERTY));
		if (keyFields != null && !keyFields.isEmpty()) {
			typeKeyFieldsList = new ArrayList<>();
			for (int i=0;i<portCount;i++) {
				TypeKeyFields typeKeyField = new TypeKeyFields();
				String[] keyList = keyFields.get(i).getJoinKey().split(",");
				if(keyList.length==0 || (keyList.length==1 && StringUtils.isBlank(keyList[0])))
					 continue;
				typeKeyField.setInSocketId(keyFields.get(i).getPortIndex());
				typeKeyField.setRecordRequired(getRecordRequiredValue(keyFields.get(i)));
				typeKeyFieldsList.add(typeKeyField);

				if (!converterHelper.hasAllStringsInArrayAsParams(keyList)) {
					for (String key : keyList) {
						if (!ParameterUtil.isParameter(key)) {
							TypeFieldName fieldName = new TypeFieldName();
							fieldName.setName(key);
							typeKeyField.getField().add(fieldName);
						} else {
							converterHelper.addParamTag(this.ID, key, 
								ComponentXpathConstants.JOIN_KEYS.value()
								.replace("$inSocketId", keyFields.get(i).getPortIndex()), false);
						}
					}
				}else{
					StringBuffer parameterFieldNames=new StringBuffer();
					TypeFieldName field = new TypeFieldName();
					field.setName("");
					typeKeyField.getField().add(field);
					for (String fieldName : keyList){ 
						parameterFieldNames.append(fieldName+ " ");
					}
					converterHelper.addParamTag(this.ID, parameterFieldNames.toString(), 
							ComponentXpathConstants.JOIN_KEYS.value().replace("$inSocketId", keyFields.get(i).getPortIndex()),true);
					
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
			
			List<LookupMapProperty> lookupMapProperties = joinPropertyGrid.getLookupMapProperties();
			if (!converterHelper.hasAllLookupMapPropertiesAsParams(lookupMapProperties)) {
				for (LookupMapProperty lookupMapProperty : lookupMapProperties) {
					
					if(!ParameterUtil.isParameter(lookupMapProperty.getSource_Field())){
						if(lookupMapProperty.getSource_Field()==null){
							continue;
						}
						String[] sourceNameValue = lookupMapProperty.getSource_Field().split(Pattern.quote("."));

						if(sourceNameValue.length == 2){
							if (sourceNameValue[1].equalsIgnoreCase(lookupMapProperty.getOutput_Field())) {
								typeInputField = new TypeInputField();
								typeInputField.setName(sourceNameValue[1]);
								typeInputField.setInSocketId(sourceNameValue[0]);
								passThroughFieldorMapFieldList.add(typeInputField);
							} else {
								mapField = new TypeMapField();
								mapField.setSourceName(sourceNameValue[1]);
								mapField.setName(lookupMapProperty.getOutput_Field());
								mapField.setInSocketId(sourceNameValue[0]);
								passThroughFieldorMapFieldList.add(mapField);
							}
						}
						
						
					}else{
						converterHelper.addParamTag(this.ID, lookupMapProperty.getSource_Field(),
								ComponentXpathConstants.OPERATIONS_OUTSOCKET.value(), false);
					}

				}
			}else{
				
				StringBuffer parameterFieldNames = new StringBuffer();
				TypeInputField inputField = new TypeInputField();
				inputField.setName("");
				inputField.setInSocketId("");
				passThroughFieldorMapFieldList.add(inputField);
				for (LookupMapProperty lookupMapProperty : lookupMapProperties)
					parameterFieldNames.append(lookupMapProperty.getOutput_Field() + " ");
				converterHelper.addParamTag(this.ID, parameterFieldNames.toString(),
						ComponentXpathConstants.OPERATIONS_OUTSOCKET.value(), true);
			}
		}
		return passThroughFieldorMapFieldList;
	}

}
