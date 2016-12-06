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

import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeInputField;
import hydrograph.engine.jaxb.commontypes.TypeOperationInputFields;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeOutSocketAsInSocket;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.engine.jaxb.commontypes.TypeTransformOperation;
import hydrograph.engine.jaxb.operationstypes.PartitionByExpression;
import hydrograph.engine.jaxb.operationstypes.PartitionByExpression.NoOfPartitions;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.OperationClassProperty;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.TransformConverter;
import hydrograph.ui.engine.helper.ConverterHelper;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

/**
 * 
 * Converter for PartitionByExpression type component.
 *
 * @author Bitwise
 */
public class PartitionByExpressionConverter extends TransformConverter {

	private static final String OUT_PORT_COUNT = "outPortCount";
	private static final String PARTITION_OPERATION_ID = "opt";
	private static final Logger logger = LogFactory.INSTANCE.getLogger(PartitionByExpressionConverter.class);
	private ConverterHelper converterHelper;

	public PartitionByExpressionConverter(Component component) {
		super(component);
		this.baseComponent = new PartitionByExpression();
		this.component = component;
		this.properties = component.getProperties();
		converterHelper = new ConverterHelper(component);
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}",properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		PartitionByExpression partByExp = (PartitionByExpression) baseComponent;
		
		NoOfPartitions noOfPartitions = new NoOfPartitions();
		String partitionValue = (String) this.properties.get(OUT_PORT_COUNT);
		if(StringUtils.isNotBlank(partitionValue)){
			int partitionValueInt = Integer.parseInt(partitionValue);
			noOfPartitions.setValue(partitionValueInt);
			partByExp.setNoOfPartitions(noOfPartitions);
		}
		
		//partByExp.getOperation().addAll(getOperations());
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

	/*@Override
	protected List<TypeTransformOperation> getOperations() {
		logger.debug("Generating TypeTransformOperation data :{}", properties.get(Constants.PARAM_NAME));
		List<TypeTransformOperation> operationList = new ArrayList<>();
		TypeTransformOperation operation = new TypeTransformOperation();
		TypeOperationInputFields operationInputFields = new TypeOperationInputFields();
		operationInputFields.getField().addAll(getOperationField());
		operation.setInputFields(operationInputFields);
		operation.setId(PARTITION_OPERATION_ID);
		if (properties.get(PropertyNameConstants.OPERATION_CLASS.value()) != null){
			operation.setClazz(((OperationClassProperty) properties.get(PropertyNameConstants.OPERATION_CLASS.value()))
					.getOperationClassPath());
		}
		operation.setProperties(getPartitionProperties());
		operationList.add(operation);
		return operationList;
	}*/

	@Override
	protected List<Object> getOperations() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<TypeInputField> getOperationField() {
		logger.debug("Generating TypeInputField data :{}",properties.get(Constants.PARAM_NAME));
		List<TypeInputField> operationFiledList = new ArrayList<>();
		List<String> componentOperationFileds = (List<String>) component.getProperties()
		.get(PropertyNameConstants.OPERATION_FILEDS.value());
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
	
	protected TypeProperties getPartitionProperties() {
		TypeProperties typeProperties = null;
		Map<String, String> runtimeProps = (Map<String, String>) properties.get(PropertyNameConstants.PARTITION_BY_EXPRESSION_PROPERTIES.value());
		if (runtimeProps != null && !runtimeProps.isEmpty()) {
			typeProperties = new TypeProperties();
			List<TypeProperties.Property> runtimePropertyList = typeProperties.getProperty();
			for (Map.Entry<String, String> entry : runtimeProps.entrySet()) {
				Property runtimeProperty = new Property();
				runtimeProperty.setName(entry.getKey());
				runtimeProperty.setValue(entry.getValue());
				runtimePropertyList.add(runtimeProperty);
			}
		}
		return typeProperties;
	}
}
