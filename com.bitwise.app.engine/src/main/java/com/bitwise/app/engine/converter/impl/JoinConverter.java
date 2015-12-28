package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.LookupPropertyGrid;
import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeOperationInputFields;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.join.TypeKeyFields;
import com.bitwiseglobal.graph.operationstypes.Join;


public class JoinConverter extends TransformConverter{
	private static final String JOIN_OPERATION_ID="join";
	private static final Logger logger = LogFactory.INSTANCE.getLogger(JoinConverter.class);
	private ConverterHelper converterHelper;
	private LookupPropertyGrid lookupPropertyGrid;
	
	public JoinConverter(Component component) {
		super();	
		this.baseComponent = new Join();
		this.component = component;
		this.properties = component.getProperties();
		lookupPropertyGrid = (LookupPropertyGrid) properties.get(Constants.JOIN_MAP_FIELD);
		converterHelper = new ConverterHelper(component);
	}
	
	@Override
	public void prepareForXML(){
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Join join = (Join)baseComponent;
	}

	private  List<TypeKeyFields> getJoinConfigKeys(){
		List<TypeKeyFields> typeKeyFieldsList = null;
		TypeKeyFields typeKeyField = null;
		Map<String, String> keyFields = (Map<String, String>) properties.get(Constants.JOIN_CONFIG_FIELD);
		if (keyFields != null && !keyFields.isEmpty()) {
			typeKeyFieldsList = new ArrayList<>();
			typeKeyField = new TypeKeyFields();
			for (Entry<String, String> entry : keyFields.entrySet()) {
			typeKeyField.setInSocketId(entry.getKey());
			//typeKeyField.setJoinType(entry.getValue());
			}
		}			
		
		return typeKeyFieldsList;
	}
	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {
		Object obj = properties.get(Constants.JOIN_MAP_FIELD);
		List<TypeOperationsOutSocket> outSocketList = new ArrayList<TypeOperationsOutSocket>();

		for (Link link : component.getSourceConnections()) {
			TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
			TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
			outSocketAsInsocket.setInSocketId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
			
			outSocket.setCopyOfInsocket(outSocketAsInsocket);
		
			outSocket.setId(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort());
			outSocket.setType(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort()));
			
			outSocket.getOtherAttributes();
			outSocketList.add(outSocket);
				
				if(properties.get(Constants.JOIN_MAP_FIELD) != null)
				outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(converterHelper.getLookuporJoinOutputMaping(lookupPropertyGrid));
				
		}
			
		return outSocketList;
	}

	@Override
	protected List<TypeTransformOperation> getOperations() {
		List<TypeTransformOperation> operationList = new ArrayList<>();
		TypeTransformOperation operation = new TypeTransformOperation();
		TypeOperationInputFields operationInputFields=new TypeOperationInputFields();
		operationInputFields.getField().addAll(getOperationField());
		operation.setInputFields(operationInputFields);
		operation.setId(JOIN_OPERATION_ID);
		if(properties.get(PropertyNameConstants.OPERATION_CLASS.value())!=null)
		operation.setClazz(((OperationClassProperty)properties.get(PropertyNameConstants.OPERATION_CLASS.value())).getOperationClassPath());
		operationList.add(operation);
		return operationList;
	}
	
	private List<TypeInputField> getOperationField() {
		logger.debug("Genrating TypeInputField data :{}", properties.get(Constants.PARAM_NAME));
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
		logger.debug("Genrating TypeBaseInSocket data for :{}", component
				.getProperties().get(Constants.PARAM_NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeBaseInSocket inSocket = new TypeBaseInSocket();
			inSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
			inSocket.setFromSocketId(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort())+link.getLinkNumber());
			inSocket.setId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
			inSocket.setType(PortTypeConstant.getPortType(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort()));
			inSocket.getOtherAttributes();
			inSocketsList.add(inSocket);
		}
		return inSocketsList;
	}

}
