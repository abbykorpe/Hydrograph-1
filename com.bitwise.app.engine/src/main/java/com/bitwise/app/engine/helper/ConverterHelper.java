package com.bitwise.app.engine.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.OperationField;
import com.bitwise.app.common.datastructure.property.OperationSystemProperties;
import com.bitwise.app.common.datastructure.property.TransformOperation;
import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.converter.PortTypeConstant;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.propertywindow.fixedwidthschema.FixedWidthGridRow;
import com.bitwiseglobal.graph.commontypes.FieldDataTypes;
import com.bitwiseglobal.graph.commontypes.ScaleTypeList;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeMapField;
import com.bitwiseglobal.graph.commontypes.TypeOperationField;
import com.bitwiseglobal.graph.commontypes.TypeOperationInputFields;
import com.bitwiseglobal.graph.commontypes.TypeOperationOutputFields;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeOutSocketAsInSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;

/**
 * This is a helper class for converter implementation. Contains the helper methods for conversion. 
 */
public class ConverterHelper{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ConverterHelper.class);
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();
	protected Component component = null;
	protected String componentName = null;
	
	public ConverterHelper(Component component) {
		this.component = component;
		this.properties = component.getProperties();
		this.componentName = (String) properties.get(Constants.NAME);
	}
	
	public List<TypeTransformOperation> getOperations(TransformPropertyGrid transformPropertyGrid) {
		logger.debug("Generating TypeTransformOperation data :{}", properties.get(Constants.NAME));
		if(transformPropertyGrid != null){
			List<TransformOperation> transformOperations = transformPropertyGrid.getOperation();
			List<TypeTransformOperation> operationList = new ArrayList<>();
			for (TransformOperation transformOperation : transformOperations) {
				operationList.add(getOperation(transformOperation));
			}
			return operationList;
		}
		else{
			return new ArrayList<>();
		}
	}
	
	private TypeTransformOperation getOperation(TransformOperation transformOperation){
		TypeTransformOperation operation = new TypeTransformOperation();
		operation.setInputFields(getOperationInputFields(transformOperation.getInputFields()));
		operation.setProperties(getProperties(transformOperation.getNameValueProps()));
		operation.setOutputFields(getOperationOutputFields(null));//TODO: transformOperation.getOutputFields()));
		operation.setId(""/*FILTER_OPERATION_ID*/);
		operation.setClazz(transformOperation.getOpClassProperty().getOperationClassPath());
		return operation;
	}
	
	private TypeOperationInputFields getOperationInputFields(List<OperationField> operationFields) {
		TypeOperationInputFields inputFields = null;
		if(operationFields != null && !operationFields.isEmpty()){
			inputFields = new TypeOperationInputFields();
			for (OperationField operationField : operationFields) {
				 TypeInputField typeInputField = new TypeInputField();
				 typeInputField.setInSocketId(TransformConverter.DEFAULT_IN_SOCKET_ID);
				 typeInputField.setName(operationField.getName());
				 inputFields.getField().add(typeInputField);
			}
		}
		return inputFields;
	}
	
	private TypeProperties getProperties(List<NameValueProperty> nameValueProps) {
		TypeProperties typeProperties = null;
		if(nameValueProps != null && !nameValueProps.isEmpty()){
			typeProperties = new TypeProperties();
			for (NameValueProperty nameValueProperty : nameValueProps) {
				Property property = new Property();
				property.setName(nameValueProperty.getPropertyName());
				property.setValue(nameValueProperty.getPropertyValue());
				typeProperties.getProperty().add(property);
			}
		}
		return typeProperties;
	}

	private TypeOperationOutputFields getOperationOutputFields(List<FixedWidthGridRow> fixedWidthGrid) {
		TypeOperationOutputFields outputFields = null;
		if(fixedWidthGrid != null && !fixedWidthGrid.isEmpty()){
			outputFields = new TypeOperationOutputFields();
			for (FixedWidthGridRow fixedWidthGridRow : fixedWidthGrid) {
				 TypeBaseField typeBaseField = new TypeBaseField();
				 typeBaseField.setName(fixedWidthGridRow.getFieldName());
				 //typeBaseField.setDescription(fixedWidthGridRow.get);
				 typeBaseField.setFormat(fixedWidthGridRow.getDateFormat());
				 //typeBaseField.setPrecision(fixedWidthGridRow.get);
				 typeBaseField.setScale(Integer.valueOf(fixedWidthGridRow.getScale()));
				 typeBaseField.setScaleType(ScaleTypeList.EXPLICIT);
				 typeBaseField.setType(FieldDataTypes.valueOf(fixedWidthGridRow.getDataTypeValue()));
				 outputFields.getField().add(typeBaseField);
			}
		}
		return outputFields;
	}

	public List<TypeOperationsOutSocket> getOutSocket(TransformPropertyGrid transformPropertyGrid) {
		logger.debug("Generating TypeOperationsOutSocket data for : {}", properties.get(Constants.NAME));
		if(component.getSourceConnections() == null || component.getSourceConnections().isEmpty()){
			return null;
		}
		List<TypeOperationsOutSocket> outSocketList = new ArrayList<TypeOperationsOutSocket>();
		for (Link link : component.getSourceConnections()) {
			TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
			setOutSocketProperties(outSocket, transformPropertyGrid, link);
			
			outSocket.getOtherAttributes();
			outSocketList.add(outSocket);
		}
		return outSocketList;
	}
	
	private void setOutSocketProperties(TypeOperationsOutSocket outSocket, TransformPropertyGrid transformPropertyGrid, Link link) {
		
		TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
		outSocketAsInsocket.setInSocketId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
		
		outSocket.setCopyOfInsocket(outSocketAsInsocket);
		outSocket.setId(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort());
		outSocket.setType(PortTypeConstant.getPortType(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort()));
		outSocket.getPassThroughfieldOrOperationFieldOrMapField().addAll(addPassThroughFields(transformPropertyGrid));
		outSocket.getPassThroughfieldOrOperationFieldOrMapField().addAll(addMapFields(transformPropertyGrid));
		outSocket.getPassThroughfieldOrOperationFieldOrMapField().addAll(addOperationFields(transformPropertyGrid));
	}

	private List<TypeInputField> addPassThroughFields(TransformPropertyGrid transformPropertyGrid) {
		if(transformPropertyGrid != null){
			List<TypeInputField> typeOperationFieldsList = null; 
			if(transformPropertyGrid.getOpSysProperties() != null &&
					!transformPropertyGrid.getOpSysProperties().isEmpty()){
				typeOperationFieldsList = new ArrayList<>();
				for(OperationSystemProperties systemProperties : transformPropertyGrid.getOpSysProperties()){
					if(systemProperties.isChecked()){
						TypeInputField typeInputField = new TypeInputField(); 
						typeInputField.setInSocketId(TransformConverter.DEFAULT_IN_SOCKET_ID);
						typeInputField.setName(systemProperties.getOpSysValue());
						typeOperationFieldsList.add(typeInputField);
					}
				}
			}
			return typeOperationFieldsList;
		}
		else{
			return new ArrayList<>();
		}
	}

	private List<TypeMapField> addMapFields(TransformPropertyGrid transformPropertyGrid) {
		if(transformPropertyGrid != null){
			List<TypeMapField> typeMapFieldList = new ArrayList<>();
			if(transformPropertyGrid.getNameValueProps() != null &&
					!transformPropertyGrid.getNameValueProps().isEmpty()){
				for(NameValueProperty nameValueProperty : transformPropertyGrid.getNameValueProps()){
					TypeMapField mapField = new TypeMapField();
					mapField.setSourceName(nameValueProperty.getPropertyName());
					mapField.setName(nameValueProperty.getPropertyValue());
					mapField.setInSocketId(TransformConverter.DEFAULT_IN_SOCKET_ID);
					typeMapFieldList.add(mapField);
				}
			}
			return typeMapFieldList;
		}
		else{
			return new ArrayList<>();
		}
	}

	private List<TypeOperationField> addOperationFields(TransformPropertyGrid transformPropertyGrid) {
		if(transformPropertyGrid != null){
			List<TransformOperation> operations = transformPropertyGrid.getOperation();
			List<TypeOperationField> typeOperationFieldList = null;
			if(operations != null && !operations.isEmpty()){
				typeOperationFieldList = new ArrayList<>();
				for(TransformOperation operation : operations){
					List<FixedWidthGridRow> outputFields = operation.getSchemaGridRowList();
					if(outputFields != null && !outputFields.isEmpty()){
						for (FixedWidthGridRow fixedWidthGridRow : outputFields) {
							TypeOperationField typeOperationField = new TypeOperationField();
							typeOperationField.setName(fixedWidthGridRow.getFieldName());
							typeOperationField.setOperationId("");
							typeOperationFieldList.add(typeOperationField);
						}
					}
				}
			}
			return typeOperationFieldList;
		}
		else{
			return new ArrayList<>();
		}
	}
	
	public List<TypeBaseInSocket> getInSocket() {
		logger.debug("Generating TypeBaseInSocket data for :{}", component.getProperties().get(Constants.NAME));
		if(component.getTargetConnections() == null || component.getTargetConnections().isEmpty()){
			return null;
		}
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeBaseInSocket inSocket = new TypeBaseInSocket();
			inSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.NAME));
			inSocket.setFromSocketId(link.getSource().getPort(link.getSourceTerminal()).getNameOfPort());
			inSocket.setId(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort());
			inSocket.setType(PortTypeConstant.getPortType(link.getTarget().getPort(link.getTargetTerminal()).getNameOfPort()));
			inSocket.getOtherAttributes();
			inSocketsList.add(inSocket);
		}
		return inSocketsList;
	}
}
