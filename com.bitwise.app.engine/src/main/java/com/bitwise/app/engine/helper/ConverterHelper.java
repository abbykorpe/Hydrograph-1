package com.bitwise.app.engine.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.slf4j.Logger;

import com.bitwise.app.common.component.config.PortSpecification;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.datastructure.property.LookupMappingGrid;
import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.OperationField;
import com.bitwise.app.common.datastructure.property.OperationSystemProperties;
import com.bitwise.app.common.datastructure.property.TransformOperation;
import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PortTypeConstant;
import com.bitwise.app.engine.converter.TransformConverter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
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
	private static final String LENGTH_QNAME = "length";

	public ConverterHelper(Component component) {
		this.component = component;
		this.properties = component.getProperties();
		this.componentName = (String) properties.get(Constants.PARAM_NAME);
	}

	public List<TypeTransformOperation> getOperations(TransformPropertyGrid transformPropertyGrid) {
		logger.debug("Generating TypeTransformOperation data :{}", properties.get(Constants.PARAM_NAME));
		List<TypeTransformOperation> operationList = new ArrayList<>();
		if (transformPropertyGrid != null) {
			List<TransformOperation> transformOperations = transformPropertyGrid.getOperation();
			if (transformOperations != null) {
				for (TransformOperation transformOperation : transformOperations) {
					operationList.add(getOperation(transformOperation));
				}
			}
		}
		return operationList;
	}

	private TypeTransformOperation getOperation(TransformOperation transformOperation) {
		TypeTransformOperation operation = new TypeTransformOperation();
		operation.setInputFields(getOperationInputFields(transformOperation.getInputFields()));
		operation.setProperties(getProperties(transformOperation.getNameValueProps()));
		operation.setOutputFields(getOperationOutputFields(transformOperation.getSchemaGridRowList()));
		operation.setId(String.valueOf(transformOperation.getOperationId()));
		operation.setClazz(transformOperation.getOpClassProperty().getOperationClassPath());
		return operation;
	}

	private TypeOperationInputFields getOperationInputFields(List<OperationField> operationFields) {
		TypeOperationInputFields inputFields = null;
		if (operationFields != null && !operationFields.isEmpty()) {
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

	private TypeOperationOutputFields getOperationOutputFields(List<FixedWidthGridRow> fixedWidthGrid) {
		TypeOperationOutputFields outputFields = null;
		if (fixedWidthGrid != null && !fixedWidthGrid.isEmpty()) {
			outputFields = new TypeOperationOutputFields();
			for (FixedWidthGridRow fixedWidthGridRow : fixedWidthGrid) {
				outputFields.getField().add(getFixedWidthTargetData(fixedWidthGridRow));
			}
		}
		return outputFields;
	}

	public List<TypeOperationsOutSocket> getOutSocket(TransformPropertyGrid transformPropertyGrid) {
		logger.debug("Generating TypeOperationsOutSocket data for : {}", properties.get(Constants.PARAM_NAME));
		List<TypeOperationsOutSocket> outSocketList = new ArrayList<TypeOperationsOutSocket>();
		if (component.getSourceConnections() != null && !component.getSourceConnections().isEmpty()) {
			for (Link link : component.getSourceConnections()) {
				TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
				setOutSocketProperties(outSocket, transformPropertyGrid, link);

				outSocket.getOtherAttributes();
				outSocketList.add(outSocket);
			}
		}
		return outSocketList;
	}

	private void setOutSocketProperties(TypeOperationsOutSocket outSocket, TransformPropertyGrid transformPropertyGrid,
			Link link) {

		TypeOutSocketAsInSocket outSocketAsInsocket = new TypeOutSocketAsInSocket();
		outSocket.setId(link.getSourceTerminal());
		outSocketAsInsocket.setInSocketId(link.getTargetTerminal());
		outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addPassThroughFields(transformPropertyGrid));
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addMapFields(transformPropertyGrid));
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().addAll(addOperationFields(transformPropertyGrid));
		if (outSocket.getPassThroughFieldOrOperationFieldOrMapField().isEmpty()) {
			outSocket.setCopyOfInsocket(outSocketAsInsocket);

		}
	}

	private List<TypeInputField> addPassThroughFields(TransformPropertyGrid transformPropertyGrid) {
		List<TypeInputField> typeOperationFieldsList = new ArrayList<>();
		if (transformPropertyGrid != null && transformPropertyGrid.getOpSysProperties() != null
				&& !transformPropertyGrid.getOpSysProperties().isEmpty()) {
			for (OperationSystemProperties systemProperties : transformPropertyGrid.getOpSysProperties()) {
				if (systemProperties.isChecked()) {
					TypeInputField typeInputField = new TypeInputField();
					typeInputField.setInSocketId(TransformConverter.DEFAULT_IN_SOCKET_ID);
					typeInputField.setName(systemProperties.getOpSysValue());
					typeOperationFieldsList.add(typeInputField);
				}
			}
		}
		return typeOperationFieldsList;
	}

	private List<TypeMapField> addMapFields(TransformPropertyGrid transformPropertyGrid) {
		List<TypeMapField> typeMapFieldList = new ArrayList<>();
		if (transformPropertyGrid != null && transformPropertyGrid.getNameValueProps() != null
				&& !transformPropertyGrid.getNameValueProps().isEmpty()) {
			for (NameValueProperty nameValueProperty : transformPropertyGrid.getNameValueProps()) {
				TypeMapField mapField = new TypeMapField();
				mapField.setSourceName(nameValueProperty.getPropertyName());
				mapField.setName(nameValueProperty.getPropertyValue());
				mapField.setInSocketId(TransformConverter.DEFAULT_IN_SOCKET_ID);
				typeMapFieldList.add(mapField);
			}
		}
		return typeMapFieldList;
	}

	private List<TypeOperationField> addOperationFields(TransformPropertyGrid transformPropertyGrid) {
		List<TypeOperationField> typeOperationFieldList = new ArrayList<>();
		if (transformPropertyGrid != null) {
			List<TransformOperation> operations = transformPropertyGrid.getOperation();
			if (operations != null && !operations.isEmpty()) {
				for (TransformOperation operation : operations) {
					List<FixedWidthGridRow> outputFields = operation.getSchemaGridRowList();
					if (outputFields != null && !outputFields.isEmpty()) {
						for (FixedWidthGridRow fixedWidthGridRow : outputFields) {
							TypeOperationField typeOperationField = new TypeOperationField();
							typeOperationField.setName(fixedWidthGridRow.getFieldName());
							typeOperationField.setOperationId(String.valueOf(operation.getOperationId()));
							typeOperationFieldList.add(typeOperationField);
						}
					}
				}
			}
		}
		return typeOperationFieldList;
	}

	public List<TypeBaseInSocket> getInSocket() {
		logger.debug("Generating TypeBaseInSocket data for :{}", component.getProperties().get(Constants.PARAM_NAME));
		List<TypeBaseInSocket> inSocketsList = new ArrayList<>();
		if (component.getTargetConnections() != null || !component.getTargetConnections().isEmpty()) {
			for (Link link : component.getTargetConnections()) {
				TypeBaseInSocket inSocket = new TypeBaseInSocket();
				inSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
				if (isMultipleLinkAllowed(link.getSource(), link.getSourceTerminal()))
					inSocket.setFromSocketId(link.getSource().getPort(link.getSourceTerminal()).getPortType()
							+ link.getLinkNumber());
				else
					inSocket.setFromSocketId(link.getSourceTerminal());
				inSocket.setId(link.getTargetTerminal());
				inSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());
				inSocket.getOtherAttributes();
				inSocketsList.add(inSocket);
			}
		}
		return inSocketsList;
	}

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
			typeBaseField.getOtherAttributes().put(new QName(LENGTH_QNAME), object.getLength());
		}
		return typeBaseField;
	}

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

	public boolean isMultipleLinkAllowed(Component sourceComponent, String portName) {
		logger.debug("Getting port specification for port" + portName);
		for (PortSpecification portSpecification : sourceComponent.getPortSpecification()) {
			if (portSpecification.getNameOfPort().equals(portName)) {
				return portSpecification.isAllowMultipleLinks();
			}
		}
		return false;
	}
}
