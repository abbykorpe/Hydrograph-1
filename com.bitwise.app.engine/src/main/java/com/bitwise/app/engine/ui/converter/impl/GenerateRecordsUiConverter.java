package com.bitwise.app.engine.ui.converter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.GenerateRecordSchemaGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.InputUiConverter;
import com.bitwise.app.engine.ui.helper.ConverterUiHelper;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.GenerateRecords;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.inputtypes.GenerateRecord;

/**
 * This class is used to create ui-GenerateRecords component from engine's GenerateRecord component 
 * 
 * @author Bitwise
 *
 */
public class GenerateRecordsUiConverter extends InputUiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputFileDelimitedUiConverter.class);
	private GenerateRecord generateRecord;
	ConverterUiHelper converterUiHelper;

	public GenerateRecordsUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new GenerateRecords();
		this.propertyMap = new LinkedHashMap<>();
		converterUiHelper = new ConverterUiHelper(uiComponent);
		generateRecord = (GenerateRecord) typeBaseComponent;
	}

	/* 
	 * Generates properties specific to GenerateRecords ui-component
	 * 
	 */
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching Input-Delimited-Properties for {}", componentName);
		propertyMap.put(Constants.PARAM_NO_OF_RECORDS,
				String.valueOf(generateRecord.getRecordCount().getValue()));
		uiComponent.setType(Constants.GENERATE_RECORDS_COMPONENT_TYPE);
		uiComponent.setCategory(UIComponentsConstants.INPUT_CATEGORY.value());
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		uiComponent.setProperties(propertyMap);
		validateComponentProperties(propertyMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bitwise.app.engine.ui.converter.UiConverter#getRuntimeProperties()
	 */
	@Override
	protected Map<String, String> getRuntimeProperties() {
		LOGGER.debug("Generating Runtime Properties for -{}", componentName);
		Map<String, String> runtimeMap = null;
		TypeProperties typeProperties = generateRecord.getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new LinkedHashMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bitwise.app.engine.ui.converter.InputUiConverter#getSchema(com.bitwiseglobal.graph.commontypes.TypeInputOutSocket
	 * )
	 */
	@Override
	protected Object getSchema(TypeInputOutSocket outSocket) {
		LOGGER.debug("Generating UI-Schema data for {}", componentName);
		Schema schema = null;
		List<GridRow> gridRow = new ArrayList<>();

		if (outSocket.getSchema() != null
				&& outSocket.getSchema().getFieldOrRecordOrIncludeExternalSchema().size() != 0) {
			schema = new Schema();
			for (Object record : outSocket.getSchema().getFieldOrRecordOrIncludeExternalSchema()) {
				if ((TypeExternalSchema.class).isAssignableFrom(record.getClass())) {
					schema.setIsExternal(true);
					if (StringUtils.isNotBlank(((TypeExternalSchema) record).getUri()))
						schema.setExternalSchemaPath(((TypeExternalSchema) record).getUri());
				} else {
					gridRow.add(getGenerateRecordsSchemaGridRow(record));
					schema.setGridRow(gridRow);
					schema.setIsExternal(false);
				}
			}
		}
		return schema;

	}

	private GenerateRecordSchemaGridRow getGenerateRecordsSchemaGridRow(Object record) {
		if ((TypeExternalSchema.class).isAssignableFrom(record.getClass())) {
			return null;
		} else if ((TypeBaseField.class).isAssignableFrom(record.getClass())) {
			GenerateRecordSchemaGridRow genertaeRecordsSchemaGridRow = new GenerateRecordSchemaGridRow();
			TypeBaseField typeBaseField = (TypeBaseField) record;
			genertaeRecordsSchemaGridRow.setDataTypeValue(converterUiHelper.getStringValue(typeBaseField.getType()
					.value()));
			genertaeRecordsSchemaGridRow.setDateFormat(converterUiHelper.getStringValue(typeBaseField.getFormat()));
			genertaeRecordsSchemaGridRow.setFieldName(converterUiHelper.getStringValue(typeBaseField.getName()));
			genertaeRecordsSchemaGridRow.setScale(converterUiHelper.getStringValue(String.valueOf(typeBaseField
					.getScale())));
			genertaeRecordsSchemaGridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(typeBaseField.getType()
					.value()));
			genertaeRecordsSchemaGridRow.setLength(converterUiHelper.getStringValue(converterUiHelper.getQnameValue(
					typeBaseField, Constants.LENGTH_QNAME)));
			genertaeRecordsSchemaGridRow.setRangeFrom(converterUiHelper.getStringValue(converterUiHelper.getQnameValue(
					typeBaseField, Constants.RANGE_FROM_QNAME)));
			genertaeRecordsSchemaGridRow.setRangeTo(converterUiHelper.getStringValue(converterUiHelper.getQnameValue(
					typeBaseField, Constants.RANGE_TO_QNAME)));
			genertaeRecordsSchemaGridRow.setDefaultValue(converterUiHelper.getStringValue(converterUiHelper
					.getQnameValue(typeBaseField, Constants.DEFAULT_VALUE_QNAME)));
			return genertaeRecordsSchemaGridRow;
		}
		return null;
	}

}
