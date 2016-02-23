package com.bitwise.app.engine.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.slf4j.Logger;

import com.bitwise.app.common.component.config.PortInfo;
import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.datastructure.property.LookupMappingGrid;
import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.mapping.ATMapping;
import com.bitwise.app.common.datastructure.property.mapping.MappingSheetRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
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
public class ConverterHelper {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ConverterHelper.class);
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();
	protected Component component = null;
	protected String componentName = null;

	public ConverterHelper(Component component) {
		this.component = component;
		this.properties = component.getProperties();
		this.componentName = (String) properties.get(Constants.PARAM_NAME);
	}

	public List<TypeTransformOperation> getOperations(ATMapping transformPropertyGrid, List<FixedWidthGridRow> fixedWidthGridRows) {
		logger.debug("Generating TypeTransformOperation data :{}", properties.get(Constants.PARAM_NAME));
		List<TypeTransformOperation> operationList = new ArrayList<>();
		if (transformPropertyGrid != null) {
			List<MappingSheetRow> transformOperations = transformPropertyGrid.getMappingSheetRows();
			if (transformOperations != null) {
				int OperationID = 0;
				for (MappingSheetRow transformOperation : transformOperations) {
					TypeTransformOperation operation = getOperation(transformOperation,OperationID,fixedWidthGridRows);
					if( operation != null){
						operationList.add(operation);
						OperationID++;
					}
				}
			}			
		}
		return operationList;
	}

	private TypeTransformOperation getOperation(MappingSheetRow transformOperation, int operationID, List<FixedWidthGridRow> fixedWidthGridRows) {
		if(transformOperation != null && transformOperation.getOperationClassProperty() !=null){
			if(!transformOperation.getOperationClassProperty().getOperationClassPath().trim().equals("")){
				TypeTransformOperation operation = new TypeTransformOperation();			
				operation.setId("operationID" + operationID);
				operation.setInputFields(getOperationInputFields(transformOperation));				
				operation.setOutputFields(getOperationOutputFields(transformOperation,fixedWidthGridRows));
				operation.setClazz(transformOperation.getOperationClassProperty().getOperationClassPath());
				return operation;
			}				
		}
		
		return null;
		
	}

	private TypeOperationInputFields getOperationInputFields(MappingSheetRow transformOperation) {
		TypeOperationInputFields inputFields = null;
		if (transformOperation != null) {
			inputFields = new TypeOperationInputFields();
			
			if(transformOperation.getOperationClassProperty() != null && transformOperation.getOperationClassProperty().getOperationClassPath() !=null){
				for (String operationField : transformOperation.getInputFields()) {
					TypeInputField typeInputField = new TypeInputField();
					typeInputField.setInSocketId(Constants.FIXED_INSOCKET_ID);
					typeInputField.setName(operationField.trim());
					inputFields.getField().add(typeInputField);
				}
			}
		}
		return inputFields;
	}

	// TODO - check if this will be used in future
	private TypeProperties getProperties(List<NameValueProperty> nameValueProps) {
		TypeProperties typeProperties = null;
		if (nameValueProps != null && !nameValueProps.isEmpty()) {
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

	private TypeOperationOutputFields getOperationOutputFields(MappingSheetRow transformOperation, List<FixedWidthGridRow> fixedWidthGrid) {
		TypeOperationOutputFields outputFields = null;
		
		
		for(String outputFieldName : transformOperation.getOutputList()){					
			for(FixedWidthGridRow fixedWidthRow : fixedWidthGrid){
				if(fixedWidthRow.getFieldName().equals(outputFieldName)){					
					if(outputFields == null){
						outputFields = new TypeOperationOutputFields();
					}
					outputFields.getField().add(getFixedWidthTargetData(fixedWidthRow));
				}
			}
		}
		return outputFields;
	}

	/**
	 * 
	 * returns output socket
	 * 
	 * 
	 * @param atMapping
	 * @param fixedWidthGridRows
	 * @return list of {@link TypeOperationsOutSocket}
	 */
	public List<TypeOperationsOutSocket> getOutSocket(ATMapping atMapping, List<FixedWidthGridRow> fixedWidthGridRows) {
		logger.debug("Generating TypeOperationsOutSocket data for : {}", properties.get(Constants.PARAM_NAME));
		List<TypeOperationsOutSocket> outSocketList = new ArrayList<TypeOperationsOutSocket>();
		if (component.getSourceConnections() != null && !component.getSourceConnections().isEmpty()) {
			for (Link link : component.getSourceConnections()) {
				TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
				setOutSocketProperties(outSocket, atMapping, fixedWidthGridRows, link);

				outSocket.getOtherAttributes();
				outSocketList.add(outSocket);
			}
		}
		return outSocketList;
	}

	private void setOutSocketProperties(TypeOperationsOutSocket outSocket, ATMapping atMapping,
			List<FixedWidthGridRow> fixedWidthGridRows, Link link) {

		TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
		outSocket.setId(link.getSourceTerminal());
		outSocketAsInsocket.setInSocketId(link.getTargetTerminal());
		outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addPassThroughFields(atMapping,fixedWidthGridRows));
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addMapFields(atMapping,fixedWidthGridRows));
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addOperationFields(atMapping,fixedWidthGridRows));
		if (outSocket.getPassThroughFieldOrOperationFieldOrMapField().isEmpty()) {
			outSocket.setCopyOfInsocket(outSocketAsInsocket);

		}
	}

	private List<TypeInputField> addPassThroughFields(ATMapping atMapping, List<FixedWidthGridRow> fixedWidthGridRows) {
		List<TypeInputField> typeOperationFieldsList = new ArrayList<>();
		if (atMapping != null) {			
			for(MappingSheetRow operationRow : atMapping.getMappingSheetRows()){				
				if( operationRow.getOperationClassProperty() ==null || operationRow.getOperationClassProperty().getOperationClassPath() == null || 
						operationRow.getOperationClassProperty().getOperationClassPath().trim().equals("")){
					List<String> inputFields = operationRow.getInputFields();
					List<String> outputFields = operationRow.getOutputList();
					int index = 0;
					for(String inputField : inputFields){
						if(inputField.trim().equals(outputFields.get(index).trim())){
							TypeInputField typeInputField = new TypeInputField();
							typeInputField.setInSocketId(Constants.FIXED_INSOCKET_ID);
							typeInputField.setName(inputField.trim());
							typeOperationFieldsList.add(typeInputField);
						}
						index++;
					}
				}
			}
		}
		return typeOperationFieldsList;
	}

	private List<TypeMapField> addMapFields(ATMapping atMapping, List<FixedWidthGridRow> fixedWidthGridRows) {		
		List<TypeMapField> typeMapFieldList = new ArrayList<>();
		
		if (atMapping != null) {			
			for(MappingSheetRow operationRow : atMapping.getMappingSheetRows()){				
				if( operationRow.getOperationClassProperty() ==null || operationRow.getOperationClassProperty().getOperationClassPath() == null || 
						operationRow.getOperationClassProperty().getOperationClassPath().trim().equals("")){
						List<String> inputFields = operationRow.getInputFields();
						List<String> outputFields = operationRow.getOutputList();
						int index = 0;
						for(String inputField : inputFields){
							if(!inputField.trim().equals(outputFields.get(index).trim())){
								TypeMapField mapField = new TypeMapField();
								mapField.setSourceName(inputField.trim());
								mapField.setName(outputFields.get(index).trim());
								mapField.setInSocketId(Constants.FIXED_INSOCKET_ID);
								typeMapFieldList.add(mapField);
							}
							index++;
						}
				}
			}
		}
		return typeMapFieldList;
	}

	private List<TypeOperationField> addOperationFields(ATMapping atMapping, List<FixedWidthGridRow> fixedWidthGridRows) {
		List<TypeOperationField> typeOperationFieldList = new ArrayList<>();
		
		if (atMapping != null) {			
			for(MappingSheetRow operationRow : atMapping.getMappingSheetRows()){				
				if(operationRow.getOperationClassProperty()!=null && operationRow.getOperationClassProperty().getOperationClassPath() != null && !operationRow.getOperationClassProperty().getOperationClassPath().equalsIgnoreCase("")){
					List<String> outputFields = operationRow.getOutputList();
					int index = 0;
					for(String outputField : outputFields){
						TypeOperationField typeOperationField = new TypeOperationField();
						typeOperationField.setName(outputField);
						typeOperationField.setOperationId("operationID" + index);
						typeOperationFieldList.add(typeOperationField);						
					}
					index++;
				}
			}
		}
		
		return typeOperationFieldList;
	}

	/**
	 * 
	 * returns Input sockets
	 * 
	 * @return list of {@link TypeBaseInSocket}
	 */
	public List<TypeBaseInSocket> getInSocket() {
		logger.debug("Generating TypeBaseInSocket data for :{}", component.getProperties().get(Constants.PARAM_NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		if (component.getTargetConnections() != null || !component.getTargetConnections().isEmpty()) {
			for (Link link : component.getTargetConnections()) {
				TypeBaseInSocket inSocket = new TypeBaseInSocket();
				inSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
				inSocket.setFromSocketId(getFromSocketId(link));
				inSocket.setId(link.getTargetTerminal());
				inSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());
				inSocket.getOtherAttributes();
				inSocketsList.add(inSocket);
			}
		}
		return inSocketsList;
	}

	/**
	 * returns Schema
	 * 
	 * @param {@link FixedWidthGridRow}
	 * @return {@link TypeBaseField}
	 */
	public TypeBaseField getFixedWidthTargetData(FixedWidthGridRow object) {
		TypeBaseField typeBaseField = new TypeBaseField();
		typeBaseField.setName(object.getFieldName());

		populateCommonFieldInfo(object, typeBaseField);
		return typeBaseField;
	}

	private void populateCommonFieldInfo(FixedWidthGridRow object, TypeBaseField typeBaseField) {
		
		populateCommonFieldInfo(object, typeBaseField);
		if (object.getLength() != null && !object.getLength().trim().isEmpty()) {
			typeBaseField.getOtherAttributes().put(new QName(Constants.LENGTH_QNAME), object.getLength());
		}
	}

	/**
	 * returns Schema
	 * 
	 * @param {@link GridRow}
	 * @return {@link TypeBaseField}
	 */
	public TypeBaseField getSchemaGridTargetData(GridRow object) {
		TypeBaseField typeBaseField = new TypeBaseField();
		typeBaseField.setName(object.getFieldName());
		
		if (object.getDataTypeValue().equals(FieldDataTypes.JAVA_UTIL_DATE.value())
				&& !object.getDateFormat().trim().isEmpty())
			typeBaseField.setFormat(object.getDateFormat());

		if (!object.getScale().trim().isEmpty())
			typeBaseField.setScale(Integer.parseInt(object.getScale()));

		if (object.getDataTypeValue().equals(FieldDataTypes.JAVA_LANG_DOUBLE.value())
				|| object.getDataTypeValue().equals(FieldDataTypes.JAVA_MATH_BIG_DECIMAL.value())
				|| object.getDataTypeValue().equals(FieldDataTypes.JAVA_LANG_FLOAT.value())) {
			
			for (ScaleTypeList scaleType : ScaleTypeList.values()) {
				if (scaleType.value().equalsIgnoreCase(object.getScaleTypeValue()))
					typeBaseField.setScaleType(scaleType);
			}
			
			if (!object.getScale().trim().isEmpty())
				typeBaseField.setScale(Integer.parseInt(object.getScale()));
		}

		for (FieldDataTypes fieldDataType : FieldDataTypes.values()) {
			if (fieldDataType.value().equalsIgnoreCase(object.getDataTypeValue()))
				typeBaseField.setType(fieldDataType);
		}

		if (!object.getPrecision().trim().isEmpty())
			typeBaseField.setPrecision(Integer.parseInt(object.getPrecision()));
		
		typeBaseField.setDescription(object.getDescription());
		return typeBaseField;
	}

	/**
	 * returns mapping rows list
	 * 
	 * @param lookupPropertyGrid
	 * @return {@link Object}
	 */
	public List<Object> getLookuporJoinOutputMaping(LookupMappingGrid lookupPropertyGrid) {
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

	/**
	 * 
	 * returns true if multiple links allowed at give component and ant given port
	 * 
	 * @param sourceComponent
	 * @param portName
	 * @return
	 */
	public boolean isMultipleLinkAllowed(Component sourceComponent, String portName) {
		logger.debug("Getting port specification for port" + portName);
		for (PortSpecification portSpecification : sourceComponent.getPortSpecification()) {
			for(PortInfo portInfo:portSpecification.getPort()){
				if (portInfo.getNameOfPort().equals(portName)) {
					return portInfo.isAllowMultipleLinks();
				}
			}
		}
		return false;
	}
	
	public String getFromSocketId(Link link) {
		String inSocketId = link.getSourceTerminal();

		if (isMultipleLinkAllowed(link.getSource(), link.getSourceTerminal()))
			inSocketId = link.getSource().getPort(link.getSourceTerminal()).getPortType() + link.getLinkNumber();

		if (link.getSource().getComponentName().equals("InputSubgraphComponent")) {
			return inSocketId.replace("out", "in");
		}
		return inSocketId;

	}
}
