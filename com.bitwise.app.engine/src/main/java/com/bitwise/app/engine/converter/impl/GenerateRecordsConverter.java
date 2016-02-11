package com.bitwise.app.engine.converter.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.GenerateRecordSchemaGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.converter.InputConverter;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwiseglobal.graph.commontypes.FieldDataTypes;
import com.bitwiseglobal.graph.commontypes.ScaleTypeList;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;
import com.bitwiseglobal.graph.inputtypes.GenerateRecord;
import com.bitwiseglobal.graph.inputtypes.GenerateRecord.RecordCount;

/**
 * This class is used to create target XML for GenerateRecords component.
 * 
 * @author Bitwise
 *
 */
public class GenerateRecordsConverter extends InputConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputFileDelimitedConverter.class);

	public GenerateRecordsConverter(Component component) {
		super();
		this.baseComponent = new GenerateRecord();
		this.component = component;
		this.properties = component.getProperties();
	}

	
	/* *
	 * This method initiates target XML generation of GenrateRecords component.
	 * 
	 */
	@Override
	public void prepareForXML() {
		LOGGER.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		GenerateRecord generateRecord = (GenerateRecord) baseComponent;
		generateRecord.setRecordCount(getRecordCount());
		generateRecord.setRuntimeProperties(getRuntimeProperties());
	}

	private RecordCount getRecordCount() {
		RecordCount recordCount = null;
		String recordCountPropertyValue = (String) properties.get(Constants.NO_OF_RECORDS_PROPERTY_NAME);
		if (recordCountPropertyValue != null && !recordCountPropertyValue.trim().isEmpty()) {
			recordCount = new RecordCount();
			recordCount.setValue(Integer.valueOf((String) properties.get(Constants.NO_OF_RECORDS_PROPERTY_NAME)));
		}
		return recordCount;
	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.engine.converter.InputConverter#getInOutSocket()
	 */
	@Override
	protected List<TypeInputOutSocket> getInOutSocket() {
		LOGGER.debug("Generating TypeInputOutSocket data for {}", properties.get(Constants.PARAM_NAME));
		List<TypeInputOutSocket> outSockets = new ArrayList<>();
		for (Link link : component.getSourceConnections()) {
			TypeInputOutSocket outSocket = new TypeInputOutSocket();
			outSocket.setId(link.getSourceTerminal());
			outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
			outSocket.setSchema(getSchema());
			outSocket.getOtherAttributes();
			outSockets.add(outSocket);
		}
		return outSockets;
	}

	/* (non-Javadoc)
	 * @see com.bitwise.app.engine.converter.InputConverter#getFieldOrRecord(java.util.List)
	 */
	@Override
	protected List<TypeBaseField> getFieldOrRecord(List<GridRow> gridList) {
		LOGGER.debug("Generating data for {} for property {}", new Object[] { properties.get(Constants.PARAM_NAME),
				PropertyNameConstants.SCHEMA.value() });

		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if (gridList != null && gridList.size() != 0) {
			for (GridRow object : gridList) {
				typeBaseFields.add(getTypeBaseFieldsFromGenerateRecordsSchema((GenerateRecordSchemaGridRow) object));
			}
		}
		return typeBaseFields;
	}

	private TypeBaseField getTypeBaseFieldsFromGenerateRecordsSchema(
			GenerateRecordSchemaGridRow generateRecordsSchemaGridRow) {

		TypeBaseField typeBaseField = new TypeBaseField();
		typeBaseField.setName(generateRecordsSchemaGridRow.getFieldName());

		if (generateRecordsSchemaGridRow.getDataTypeValue().equals(FieldDataTypes.JAVA_UTIL_DATE.value())
				&& !generateRecordsSchemaGridRow.getDateFormat().trim().isEmpty())
			typeBaseField.setFormat(generateRecordsSchemaGridRow.getDateFormat());

		if (!generateRecordsSchemaGridRow.getScale().trim().isEmpty())
			typeBaseField.setScale(Integer.parseInt(generateRecordsSchemaGridRow.getScale()));

		if (generateRecordsSchemaGridRow.getDataTypeValue().equals(FieldDataTypes.JAVA_LANG_DOUBLE.value())
				|| generateRecordsSchemaGridRow.getDataTypeValue().equals(FieldDataTypes.JAVA_MATH_BIG_DECIMAL.value())) {
			typeBaseField.setScaleType(ScaleTypeList.EXPLICIT);
			if (!generateRecordsSchemaGridRow.getScale().trim().isEmpty())
				typeBaseField.setScale(Integer.parseInt(generateRecordsSchemaGridRow.getScale()));
		}

		for (FieldDataTypes fieldDataType : FieldDataTypes.values()) {
			if (fieldDataType.value().equalsIgnoreCase(generateRecordsSchemaGridRow.getDataTypeValue()))
				typeBaseField.setType(fieldDataType);
		}
		if (generateRecordsSchemaGridRow.getLength() != null
				&& !generateRecordsSchemaGridRow.getLength().trim().isEmpty()) {
			typeBaseField.getOtherAttributes().put(new QName(Constants.LENGTH_QNAME),
					generateRecordsSchemaGridRow.getLength());
		}
		if (generateRecordsSchemaGridRow.getRangeFrom() != null
				&& !generateRecordsSchemaGridRow.getRangeFrom().trim().isEmpty()) {
			typeBaseField.getOtherAttributes().put(new QName(Constants.RANGE_FROM_QNAME),
					generateRecordsSchemaGridRow.getRangeFrom());
		}
		if (generateRecordsSchemaGridRow.getRangeTo() != null
				&& !generateRecordsSchemaGridRow.getRangeTo().trim().isEmpty()) {
			typeBaseField.getOtherAttributes().put(new QName(Constants.RANGE_TO_QNAME),
					generateRecordsSchemaGridRow.getRangeTo());
		}
		if (generateRecordsSchemaGridRow.getDefaultValue() != null
				&& !generateRecordsSchemaGridRow.getDefaultValue().trim().isEmpty()) {
			typeBaseField.getOtherAttributes().put(new QName(Constants.DEFAULT_VALUE_QNAME),
					generateRecordsSchemaGridRow.getDefaultValue());
		}

		return typeBaseField;
	}
}
