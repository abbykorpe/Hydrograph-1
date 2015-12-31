package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.JoinConfigProperty;
import com.bitwise.app.common.datastructure.property.JoinMappingGrid;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.datastructure.property.LookupMappingGrid;
import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.engine.helper.ConverterHelper;
import com.bitwise.app.engine.xpath.ComponentXpath;
import com.bitwise.app.engine.xpath.ComponentXpathConstants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwiseglobal.graph.commontypes.StandardCharsets;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeMapField;
import com.bitwiseglobal.graph.commontypes.TypeOperationInputFields;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.join.JoinType;
import com.bitwiseglobal.graph.join.TypeKeyFields;
import com.bitwiseglobal.graph.operationstypes.Join;


public class JoinConverter extends TransformConverter{
	private static final String JOIN_OPERATION_ID="join";
	private List<String> ITEMS = Arrays.asList(StringUtils.lowerCase(Constants.INNER), StringUtils.lowerCase(Constants.OUTER));
	
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
	public void prepareForXML(){
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Join join = (Join)baseComponent;
		if (properties.get(Constants.JOIN_CONFIG_FIELD) != null){
			join.getKeys().addAll(getJoinConfigKeys());
		}
	}

	private  List<TypeKeyFields> getJoinConfigKeys(){
		List<TypeKeyFields> typeKeyFieldsList = null;
		List<JoinConfigProperty> keyFields = (List<JoinConfigProperty>) properties.get(Constants.JOIN_CONFIG_FIELD);
		typeKeyFieldsList = new ArrayList<>();
		if (keyFields != null ) {
			for (JoinConfigProperty entry : keyFields) {
				TypeKeyFields typeKeyField = new TypeKeyFields();
				TypeFieldName fieldName = new TypeFieldName();
				fieldName.setName(entry.getJoinKey());
				typeKeyField.setInSocketId(entry.getPortIndex());
				typeKeyField.setJoinType(getParamValue(entry));
				typeKeyField.getField().add(fieldName);
				typeKeyFieldsList.add(typeKeyField);
			}
		}			
		return typeKeyFieldsList;
	}
	protected JoinType getParamValue(JoinConfigProperty entry) {
		logger.debug("Getting JoinType for {}", properties.get(Constants.PARAM_NAME));
		JoinType targetJoinType = null;
		if(entry.getJoinType() <= 1){
			for (JoinType type : JoinType.values()) {
				if (type.value().equalsIgnoreCase(ITEMS.get(entry.getJoinType()))) {
					targetJoinType = type;
					break;
				}
			}
		}
		
		if (StringUtils.isNotBlank(entry.getParamValue()))
			ComponentXpath.INSTANCE.getXpathMap().put(ComponentXpathConstants.COMPONENT_JOIN_TYPE_XPATH.value().replace(ID, componentName).replace("$inSocketId", entry.getJoinKey()), entry.getParamValue());
		return targetJoinType;
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
				outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(getLookuporJoinOutputMaping(joinupPropertyGrid));
				
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
	
	public List<Object> getLookuporJoinOutputMaping(JoinMappingGrid lookupPropertyGrid) {
		List<Object> passThroughFieldorMapFieldList = null;
		if (lookupPropertyGrid != null) {
			passThroughFieldorMapFieldList = new ArrayList<>();
			TypeInputField typeInputField = null;
			TypeMapField mapField = null;
			for (LookupMapProperty entry : lookupPropertyGrid.getLookupMapProperties()) {
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
