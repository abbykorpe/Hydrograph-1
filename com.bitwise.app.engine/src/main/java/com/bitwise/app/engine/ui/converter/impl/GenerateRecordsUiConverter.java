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

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(GenerateRecordsUiConverter.class);
	private GenerateRecord generateRecord;
	private ConverterUiHelper converterUiHelper;

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
			GenerateRecordSchemaGridRow generateRecordsSchemaGridRow = new GenerateRecordSchemaGridRow();
			TypeBaseField typeBaseField = (TypeBaseField) record;
			if (typeBaseField != null) {
				if (typeBaseField.getType() != null) {
					generateRecordsSchemaGridRow.setDataTypeValue(converterUiHelper.getStringValue(typeBaseField
							.getType().value()));
					generateRecordsSchemaGridRow.setDataType(GridWidgetCommonBuilder.getDataTypeByValue(typeBaseField
							.getType().value()));
				}
				if(typeBaseField.getScaleType()!=null)
				{
					generateRecordsSchemaGridRow.setScaleType(GridWidgetCommonBuilder.getScaleTypeByValue(typeBaseField
							.getScaleType().value()));	
				}
				generateRecordsSchemaGridRow.setDateFormat(converterUiHelper.getStringValue(typeBaseField.getFormat()));
				generateRecordsSchemaGridRow.setFieldName(converterUiHelper.getStringValue(typeBaseField.getName()));
				generateRecordsSchemaGridRow.setScale(converterUiHelper.getStringValue(String.valueOf(typeBaseField
						.getScale())));
				generateRecordsSchemaGridRow.setLength(converterUiHelper.getStringValue(converterUiHelper
						.getQnameValue(typeBaseField, Constants.LENGTH_QNAME)));
				generateRecordsSchemaGridRow.setRangeFrom(converterUiHelper.getStringValue(converterUiHelper
						.getQnameValue(typeBaseField, Constants.RANGE_FROM_QNAME)));
				generateRecordsSchemaGridRow.setRangeTo(converterUiHelper.getStringValue(converterUiHelper
						.getQnameValue(typeBaseField, Constants.RANGE_TO_QNAME)));
				generateRecordsSchemaGridRow.setDefaultValue(converterUiHelper.getStringValue(converterUiHelper
						.getQnameValue(typeBaseField, Constants.DEFAULT_VALUE_QNAME)));

				generateRecordsSchemaGridRow.setPrecision(converterUiHelper.getStringValue(String.valueOf(typeBaseField
						.getPrecision())));
				generateRecordsSchemaGridRow.setDescription(converterUiHelper.getStringValue(typeBaseField
						.getDescription()));
			}
			return generateRecordsSchemaGridRow;
		}
		return null;
	}

}
