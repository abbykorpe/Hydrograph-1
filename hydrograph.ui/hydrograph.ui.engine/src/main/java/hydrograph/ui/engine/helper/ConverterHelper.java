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

package hydrograph.ui.engine.helper;

import hydrograph.engine.jaxb.commontypes.FieldDataTypes;
import hydrograph.engine.jaxb.commontypes.ScaleTypeList;
import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeInputField;
import hydrograph.engine.jaxb.commontypes.TypeMapField;
import hydrograph.engine.jaxb.commontypes.TypeOperationField;
import hydrograph.engine.jaxb.commontypes.TypeOperationInputFields;
import hydrograph.engine.jaxb.commontypes.TypeOperationOutputFields;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeOutSocketAsInSocket;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.engine.jaxb.commontypes.TypeTransformOperation;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.ParameterUtil;
import hydrograph.ui.datastructure.property.BasicSchemaGridRow;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.LookupMapProperty;
import hydrograph.ui.datastructure.property.LookupMappingGrid;
import hydrograph.ui.datastructure.property.MixedSchemeGridRow;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;
import hydrograph.ui.engine.xpath.ComponentXpath;
import hydrograph.ui.engine.xpath.ComponentXpathConstants;
import hydrograph.ui.engine.xpath.ComponentsAttributeAndValue;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.model.Port;
import hydrograph.ui.graph.model.PortDetails;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

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
					if(!transformOperation.isWholeOperationParameter()){
						TypeTransformOperation operation = getOperation(transformOperation,OperationID,schemaGridRows);
						if( operation != null){
							operationList.add(operation);
							OperationID++;
						}
					}else{
						addWholeOperationParam(transformOperation);
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
		if(!nameValueProperties.isEmpty()){
			properties = new TypeProperties();
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

			if (!hasAllFilterPropertiesAsParams(mappingSheetRow.getInputFields())) {	
				for (FilterProperties operationField : mappingSheetRow.getInputFields()) {
					if(!ParameterUtil.isParameter(operationField.getPropertyname())){
						TypeInputField typeInputField = new TypeInputField();
						typeInputField.setInSocketId(Constants.FIXED_INSOCKET_ID);
						typeInputField.setName(operationField.getPropertyname().trim());
						inputFields.getField().add(typeInputField);
					}else{
						addParamTag(ID, operationField.getPropertyname(),
								ComponentXpathConstants.TRANSFORM_INPUT_FIELDS.value().replace("$operationId", mappingSheetRow.getOperationID()), false);
					}
				}
			}else{
				StringBuffer parameterFieldNames = new StringBuffer();
				TypeInputField typeInputField = new TypeInputField();
				typeInputField.setInSocketId(Constants.FIXED_INSOCKET_ID);
				typeInputField.setName("");
				inputFields.getField().add(typeInputField);
				for (FilterProperties operationField : mappingSheetRow.getInputFields()) {
					parameterFieldNames.append(operationField.getPropertyname().trim() + " ");
				}
				addParamTag(ID, parameterFieldNames.toString(),
						ComponentXpathConstants.TRANSFORM_INPUT_FIELDS.value().replace("$operationId", mappingSheetRow.getOperationID()), true);
			}
		}
		return inputFields;
	}

	private TypeOperationOutputFields getOperationOutputFields(MappingSheetRow mappingSheetRow, List<BasicSchemaGridRow> schemaGrid) {
		TypeOperationOutputFields outputFields = new TypeOperationOutputFields();
		if (mappingSheetRow != null) {
			if (!hasAllFilterPropertiesAsParams(mappingSheetRow.getOutputList())) {	
				for(FilterProperties outputFieldName : mappingSheetRow.getOutputList()){	
					if(!ParameterUtil.isParameter(outputFieldName.getPropertyname())){
						for(GridRow gridRow : schemaGrid){
							if(gridRow.getFieldName().equals(outputFieldName.getPropertyname())){					

								outputFields.getField().add(getSchemaGridTargetData(gridRow));
							}
						}
					}else{
						addParamTag(ID, outputFieldName.getPropertyname(),
								ComponentXpathConstants.TRANSFORM_OUTPUT_FIELDS.value().replace("$operationId", mappingSheetRow.getOperationID()), false);
					}
				}
			}else{
				StringBuffer parameterFieldNames = new StringBuffer();
				TypeBaseField typeBaseField = new TypeBaseField();
				typeBaseField.setName("");
				outputFields.getField().add(typeBaseField);

				for (FilterProperties operationField : mappingSheetRow.getOutputList()) {
					parameterFieldNames.append(operationField.getPropertyname().trim() + " ");
				}
				addParamTag(ID, parameterFieldNames.toString(),
						ComponentXpathConstants.TRANSFORM_OUTPUT_FIELDS.value().replace("$operationId", mappingSheetRow.getOperationID()), true);

			}
		}
		return outputFields;
	}


	/**
	 * 
	 * returns output socket
	 * 
	 * 
	 * @param transformMapping
	 * @param gridRows
	 * @return list of {@link TypeOperationsOutSocket}
	 */
	public List<TypeOperationsOutSocket> getOutSocket(TransformMapping transformMapping, List<BasicSchemaGridRow> gridRows) {
		logger.debug("Generating TypeOperationsOutSocket data for : {}", properties.get(Constants.PARAM_NAME));
		List<TypeOperationsOutSocket> outSocketList = new ArrayList<TypeOperationsOutSocket>();
		if (component.getSourceConnections() != null && !component.getSourceConnections().isEmpty()) {
			for (Link link : component.getSourceConnections()) {
				TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
				setOutSocketProperties(outSocket, transformMapping, gridRows, link);
				outSocket.getOtherAttributes();
				outSocketList.add(outSocket);
			}
		}
		return outSocketList;
	}

	private void setOutSocketProperties(TypeOperationsOutSocket outSocket, TransformMapping transformMapping,
			List<BasicSchemaGridRow> gridRows, Link link) {

		TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
		outSocket.setId(link.getSourceTerminal());
		outSocketAsInsocket.setInSocketId(link.getTargetTerminal());
		outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addPassThroughFields(transformMapping,gridRows));
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addMapFields(transformMapping,gridRows));
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addOperationFields(transformMapping,gridRows));
		addMapFieldParams(transformMapping);
		addOutputFieldParams(transformMapping);
		if (outSocket.getPassThroughFieldOrOperationFieldOrMapField().isEmpty()) {
			outSocket.setCopyOfInsocket(outSocketAsInsocket);

		}
	}

	private List<TypeInputField> addPassThroughFields(TransformMapping transformMapping, List<BasicSchemaGridRow> schemaGridRows) {
		List<TypeInputField> typeOperationFieldsList = new ArrayList<>();
		if (transformMapping != null) {	
			{
				for (NameValueProperty nameValueProperty : transformMapping.getMapAndPassthroughField()) {
					if (nameValueProperty.getPropertyName().trim().equals(nameValueProperty.getPropertyValue().trim())
							&& !(ParameterUtil.isParameter(nameValueProperty.getPropertyName().trim()))) {

						TypeInputField typeInputField = new TypeInputField();
						typeInputField.setInSocketId(Constants.FIXED_INSOCKET_ID);
						typeInputField.setName(nameValueProperty.getPropertyName().trim());
						typeOperationFieldsList.add(typeInputField);	
					}

				}
			}
		}

		return typeOperationFieldsList;
	}

	private void addMapFieldParams(TransformMapping transformMapping) {
		if (transformMapping != null) {
			StringBuffer parameterFieldNames = new StringBuffer();		
			for (NameValueProperty nameValueProperty : transformMapping.getMapAndPassthroughField()) {

				if(ParameterUtil.isParameter(nameValueProperty.getPropertyName())){
					parameterFieldNames.append(nameValueProperty.getPropertyName().trim() + " ");
				}
			}
			addParamTag(ID, parameterFieldNames.toString(),
					ComponentXpathConstants.OPERATIONS_OUTSOCKET.value(), false);

		}
	}

	private void addWholeOperationParam(MappingSheetRow transformMapping) {
		if(transformMapping!= null && ParameterUtil.isParameter(transformMapping.getWholeOperationParameterValue())){
			addParamTag(ID, transformMapping.getWholeOperationParameterValue(),
					ComponentXpathConstants.TRANSFORM_OPERATION.value(), false);	
		}
	}

	private void addOutputFieldParams(TransformMapping transformMapping) {
		if (transformMapping != null) {	
			StringBuffer parameterFieldNames = new StringBuffer();

			for (FilterProperties filterProperty : transformMapping.getOutputFieldList()) {
				if(ParameterUtil.isParameter(filterProperty.getPropertyname())){
					parameterFieldNames.append(filterProperty.getPropertyname().trim() + " ");
				}

			}
			addParamTag(ID, parameterFieldNames.toString(),
					ComponentXpathConstants.OPERATIONS_OUTSOCKET.value(), false);

		}
	}

	public boolean hasAllLookupMapPropertiesAsParams(List<LookupMapProperty> lookupMapProperties){
		for (LookupMapProperty lookupMapProperty : lookupMapProperties) 
			if (!ParameterUtil.isParameter(lookupMapProperty.getSource_Field())) 
				return false;
		return true;
	}

	public boolean hasAllFilterPropertiesAsParams(List<FilterProperties> filterProperties){
		for (FilterProperties filterProperty : filterProperties) 
			if (!ParameterUtil.isParameter(filterProperty.getPropertyname())) 
				return false;
		return true;
	}

	public boolean hasAllStringsInListAsParams(List<String> componentOperationFields) {
		for (String fieldName : componentOperationFields)
			if (!ParameterUtil.isParameter(fieldName))
				return false;
		return true;
	}

	public boolean hasAllKeysAsParams(Map<String, String> secondaryKeyRow) {
		for (Entry<String, String> secondaryKeyRowEntry : secondaryKeyRow.entrySet())
			if (!ParameterUtil.isParameter(secondaryKeyRowEntry.getKey()))
				return false;
		return true;
	}

	public boolean hasAllStringsInArrayAsParams(String keys[]){
		for (String fieldName : keys) 
			if (!ParameterUtil.isParameter(fieldName)) 
				return false;
		return true;
	}


	private List<TypeMapField> addMapFields(TransformMapping transformMapping, List<BasicSchemaGridRow> gridRows) {
		List<TypeMapField> typeMapFieldList = new ArrayList<>();

		if (transformMapping != null) {

			for (NameValueProperty nameValueProperty : transformMapping.getMapAndPassthroughField()) {
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

	private List<TypeOperationField> addOperationFields(TransformMapping transformMapping, List<BasicSchemaGridRow> gridRows) {
		List<TypeOperationField> typeOperationFieldList = new ArrayList<>();

		if (transformMapping != null) {			
			for(MappingSheetRow operationRow : transformMapping.getMappingSheetRows()){				

				for(FilterProperties outputField : operationRow.getOutputList()){
					if( !(ParameterUtil.isParameter(outputField.getPropertyname()))){
						TypeOperationField typeOperationField = new TypeOperationField();
						typeOperationField.setName(outputField.getPropertyname());
						typeOperationField.setOperationId(operationRow.getOperationID());
						typeOperationFieldList.add(typeOperationField);	
					}
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
	 * @param {@link MixedSchemeGridRow}
	 * @return {@link TypeBaseField}
	 */	
	public TypeBaseField getFileMixedSchemeTargetData(MixedSchemeGridRow object) {
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
		if (object.getDelimiter() != null && !object.getDelimiter().trim().isEmpty()) {
			typeBaseField.getOtherAttributes().put(new QName(Constants.DELIMITER_QNAME), object.getDelimiter());
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
			if (!hasAllLookupMapPropertiesAsParams(lookupPropertyGrid.getLookupMapProperties())) {
				for (LookupMapProperty entry : lookupPropertyGrid.getLookupMapProperties()) {
					if(!ParameterUtil.isParameter(entry.getSource_Field())){
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
					}else{
						addParamTag(this.ID, entry.getSource_Field(),
								ComponentXpathConstants.OPERATIONS_OUTSOCKET.value(), false);
					}

				}
			}else{

				StringBuffer parameterFieldNames = new StringBuffer();
				TypeInputField inputField = new TypeInputField();
				inputField.setName("");
				inputField.setInSocketId("");
				passThroughFieldorMapFieldList.add(inputField);
				for (LookupMapProperty lookupMapProperty : lookupPropertyGrid.getLookupMapProperties())
					parameterFieldNames.append(lookupMapProperty.getOutput_Field() + " ");
				addParamTag(this.ID, parameterFieldNames.toString(),
						ComponentXpathConstants.OPERATIONS_OUTSOCKET.value(), true);
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
		for (PortDetails portDetails : sourceComponent.getPortDetails()) {
			for( Port port: portDetails.getPorts().values()){
				if(port.getTerminal().equals(portName)){
					return port.isAllowMultipleLinks();
				}
			}
		}
		return false;
	}

	public String getFromSocketId(Link link) {
		String inSocketId = link.getSourceTerminal();

		if (isMultipleLinkAllowed(link.getSource(), link.getSourceTerminal()))
			inSocketId = link.getSource().getPort(link.getSourceTerminal()).getPortType() + link.getLinkNumber();
		if (link.getSource().getComponentName().equals("InputSubjobComponent")) {
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
