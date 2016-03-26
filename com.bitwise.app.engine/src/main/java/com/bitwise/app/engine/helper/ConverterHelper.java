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

package com.bitwise.app.engine.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.bitwise.app.common.component.config.PortInfo;
import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.datastructure.property.BasicSchemaGridRow;
import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.datastructure.property.LookupMappingGrid;
import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.mapping.MappingSheetRow;
import com.bitwise.app.common.datastructure.property.mapping.TransformMapping;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ParameterUtil;
import com.bitwise.app.engine.xpath.ComponentXpath;
import com.bitwise.app.engine.xpath.ComponentXpathConstants;
import com.bitwise.app.engine.xpath.ComponentsAttributeAndValue;
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
 * @author Bitwise 
 */
public class ConverterHelper {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ConverterHelper.class);
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();
	protected Component component = null;
	protected String componentName = null;
	private static final String ID = "$id";

	public ConverterHelper(Component component) {
		this.component = component;
		this.properties = component.getProperties();
		this.componentName = (String) properties.get(Constants.PARAM_NAME);
	}

	public List<TypeTransformOperation> getOperations(TransformMapping transformPropertyGrid, List<BasicSchemaGridRow> schemaGridRows) {
		logger.debug("Generating TypeTransformOperation data :{}", properties.get(Constants.PARAM_NAME));
		List<TypeTransformOperation> operationList = new ArrayList<>();
		if (transformPropertyGrid != null) {
			List<MappingSheetRow> transformOperations = transformPropertyGrid.getMappingSheetRows();
			if (transformOperations != null) {
				int OperationID = 0;
				for (MappingSheetRow transformOperation : transformOperations) {
					TypeTransformOperation operation = getOperation(transformOperation,OperationID,schemaGridRows);
					if( operation != null){
						operationList.add(operation);
						OperationID++;
					}
				}
			}			
		}
		return operationList;
	}

	private TypeTransformOperation getOperation(MappingSheetRow mappingSheetRow, int operationID, List<BasicSchemaGridRow> schemaGridRows) {
		if(mappingSheetRow != null){
			
				TypeTransformOperation operation = new TypeTransformOperation();			
				operation.setId(mappingSheetRow.getOperationID());
				operation.setInputFields(getOperationInputFields(mappingSheetRow));				
				operation.setProperties(getOperationProperties(mappingSheetRow.getNameValueProperty()));
				operation.setOutputFields(getOperationOutputFields(mappingSheetRow,schemaGridRows));
				if(StringUtils.isNotBlank(mappingSheetRow.getOperationClassPath()))
				operation.setClazz(mappingSheetRow.getOperationClassPath());
				return operation;
							
		}
		
		return null;
		
	}

	
	private TypeProperties getOperationProperties(List<NameValueProperty> nameValueProperties) {
		TypeProperties properties=null;
        if(!nameValueProperties.isEmpty())
        {	properties = new TypeProperties();
        	for (NameValueProperty nameValueProperty : nameValueProperties) {
				Property property = new Property();
				property.setName(nameValueProperty.getPropertyName());
				property.setValue(nameValueProperty.getPropertyValue());
				properties.getProperty().add(property);
			}	
        	
        }	
		return properties;
	}

	private TypeOperationInputFields getOperationInputFields(MappingSheetRow mappingSheetRow) {
		TypeOperationInputFields inputFields = null;
		if (mappingSheetRow != null) {
			inputFields = new TypeOperationInputFields();
			 
			
				for (FilterProperties operationField : mappingSheetRow.getInputFields()) {
					TypeInputField typeInputField = new TypeInputField();
					typeInputField.setInSocketId(Constants.FIXED_INSOCKET_ID);
					typeInputField.setName(operationField.getPropertyname().trim());
					inputFields.getField().add(typeInputField);
				}
			
		}
		return inputFields;
	}

	private TypeOperationOutputFields getOperationOutputFields(MappingSheetRow mappingSheetRow, List<BasicSchemaGridRow> schemaGrid) {
		TypeOperationOutputFields outputFields = null;
		
		
		for(FilterProperties outputFieldName : mappingSheetRow.getOutputList()){					
			for(GridRow gridRow : schemaGrid){
				if(gridRow.getFieldName().equals(outputFieldName.getPropertyname())){					
					if(outputFields == null){
						outputFields = new TypeOperationOutputFields();
					}
					outputFields.getField().add(getSchemaGridTargetData(gridRow));
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
	 * @param gridRows
	 * @return list of {@link TypeOperationsOutSocket}
	 */
	public List<TypeOperationsOutSocket> getOutSocket(TransformMapping atMapping, List<BasicSchemaGridRow> gridRows) {
		logger.debug("Generating TypeOperationsOutSocket data for : {}", properties.get(Constants.PARAM_NAME));
		List<TypeOperationsOutSocket> outSocketList = new ArrayList<TypeOperationsOutSocket>();
		if (component.getSourceConnections() != null && !component.getSourceConnections().isEmpty()) {
			for (Link link : component.getSourceConnections()) {
				TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
				setOutSocketProperties(outSocket, atMapping, gridRows, link);

				outSocket.getOtherAttributes();
				outSocketList.add(outSocket);
			}
		}
		return outSocketList;
	}

	private void setOutSocketProperties(TypeOperationsOutSocket outSocket, TransformMapping atMapping,
			List<BasicSchemaGridRow> gridRows, Link link) {

		TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
		outSocket.setId(link.getSourceTerminal());
		outSocketAsInsocket.setInSocketId(link.getTargetTerminal());
		outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addPassThroughFields(atMapping,gridRows));
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addMapFields(atMapping,gridRows));
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addOperationFields(atMapping,gridRows));
		if (outSocket.getPassThroughFieldOrOperationFieldOrMapField().isEmpty()) {
			outSocket.setCopyOfInsocket(outSocketAsInsocket);

		}
	}

	private List<TypeInputField> addPassThroughFields(TransformMapping atMapping, List<BasicSchemaGridRow> schemaGridRows) {
		List<TypeInputField> typeOperationFieldsList = new ArrayList<>();
		if (atMapping != null) {
			if (!isALLParameterizedFields(atMapping.getMapAndPassthroughField())) {
				{
					for (NameValueProperty nameValueProperty : atMapping.getMapAndPassthroughField()) {
						if (nameValueProperty.getPropertyName().trim().equals(nameValueProperty.getPropertyValue().trim())) {
							if (!ParameterUtil.isParameter(nameValueProperty.getPropertyName())) {
								TypeInputField typeInputField = new TypeInputField();
								typeInputField.setInSocketId(Constants.FIXED_INSOCKET_ID);
								typeInputField.setName(nameValueProperty.getPropertyName().trim());
								typeOperationFieldsList.add(typeInputField);
							} else {
								addParamTag(ID, nameValueProperty.getPropertyName(),
										ComponentXpathConstants.OPERATIONS_OUTSOCKET.value(), false);
							}
						}

					}
				}
			}else{
				StringBuffer parameterFieldNames = new StringBuffer();
				TypeInputField typeInputField = new TypeInputField();
				typeInputField.setInSocketId(Constants.FIXED_INSOCKET_ID);
				typeInputField.setName("");
				typeOperationFieldsList.add(typeInputField);
				for (NameValueProperty nameValueProperty : atMapping.getMapAndPassthroughField())
					parameterFieldNames.append(nameValueProperty.getPropertyName().trim() + " ");
				addParamTag(ID, parameterFieldNames.toString(),
						ComponentXpathConstants.OPERATIONS_OUTSOCKET.value(), true);
			}
		}

		return typeOperationFieldsList;
	}

	private boolean isALLParameterizedFields(List<NameValueProperty> mapAndPassthroughField) {
		for (NameValueProperty nameValueProperty : mapAndPassthroughField)
			if (!ParameterUtil.isParameter(nameValueProperty.getPropertyName()))
				return false;
		return true;
	}

	private List<TypeMapField> addMapFields(TransformMapping atMapping, List<BasicSchemaGridRow> gridRows) {
		List<TypeMapField> typeMapFieldList = new ArrayList<>();

		if (atMapping != null) {

			for (NameValueProperty nameValueProperty : atMapping.getMapAndPassthroughField()) {
				if (!nameValueProperty.getPropertyName().trim().equals(nameValueProperty.getPropertyValue().trim())) {
					TypeMapField mapField = new TypeMapField();
					mapField.setSourceName(nameValueProperty.getPropertyName().trim());
					mapField.setName(nameValueProperty.getPropertyValue().trim());
					mapField.setInSocketId(Constants.FIXED_INSOCKET_ID);
					typeMapFieldList.add(mapField);
				}

			}

		}

		return typeMapFieldList;
	}

	private List<TypeOperationField> addOperationFields(TransformMapping atMapping, List<BasicSchemaGridRow> gridRows) {
		List<TypeOperationField> typeOperationFieldList = new ArrayList<>();
		
		if (atMapping != null) {			
			for(MappingSheetRow operationRow : atMapping.getMappingSheetRows()){				
				
					for(FilterProperties outputField : operationRow.getOutputList()){
						TypeOperationField typeOperationField = new TypeOperationField();
						typeOperationField.setName(outputField.getPropertyname());
						typeOperationField.setOperationId(operationRow.getOperationID());
						typeOperationFieldList.add(typeOperationField);						
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

		if (object.getDataTypeValue().equals(FieldDataTypes.JAVA_UTIL_DATE.value())
				&& !object.getDateFormat().trim().isEmpty())
			typeBaseField.setFormat(object.getDateFormat());

		if (!object.getScale().trim().isEmpty())
			typeBaseField.setScale(Integer.parseInt(object.getScale()));

		if (object.getDataTypeValue().equals(FieldDataTypes.JAVA_LANG_DOUBLE.value())
				|| object.getDataTypeValue().equals(FieldDataTypes.JAVA_MATH_BIG_DECIMAL.value())) {
			typeBaseField.setScaleType(ScaleTypeList.EXPLICIT);
			if (!object.getScale().trim().isEmpty())
				typeBaseField.setScale(Integer.parseInt(object.getScale()));
		}

		for (FieldDataTypes fieldDataType : FieldDataTypes.values()) {
			if (fieldDataType.value().equalsIgnoreCase(object.getDataTypeValue()))
				typeBaseField.setType(fieldDataType);
		}
		if (object.getLength() != null && !object.getLength().trim().isEmpty()) {
			typeBaseField.getOtherAttributes().put(new QName(Constants.LENGTH_QNAME), object.getLength());
		}
		return typeBaseField;
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
				|| object.getDataTypeValue().equals(FieldDataTypes.JAVA_MATH_BIG_DECIMAL.value())) {
			typeBaseField.setScaleType(ScaleTypeList.EXPLICIT);
			if (!object.getScale().trim().isEmpty())
				typeBaseField.setScale(Integer.parseInt(object.getScale()));
		}

		for (FieldDataTypes fieldDataType : FieldDataTypes.values()) {
			if (fieldDataType.value().equalsIgnoreCase(object.getDataTypeValue()))
				typeBaseField.setType(fieldDataType);
		}

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
	
	public void addParamTag(String ID, String fieldName, String paramXpath, boolean hasEmptyNode) {
		ComponentsAttributeAndValue tempAndValue=ComponentXpath.INSTANCE.getXpathMap().get(paramXpath.replace(ID, componentName));
		if(tempAndValue==null)
			ComponentXpath.INSTANCE.getXpathMap().put(
					(paramXpath.replace(ID, componentName)),
					new ComponentsAttributeAndValue(true,fieldName,hasEmptyNode));
		else
			tempAndValue.setNewNodeText(tempAndValue.getNewNodeText()+" "+fieldName);
	}
}
