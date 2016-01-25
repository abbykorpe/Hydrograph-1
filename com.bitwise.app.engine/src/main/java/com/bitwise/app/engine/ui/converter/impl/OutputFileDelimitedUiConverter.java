package com.bitwise.app.engine.ui.converter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.OutputUiConverter;
import com.bitwise.app.engine.ui.helper.ConverterUiHelper;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.components.OFDelimited;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.outputtypes.TextFileDelimited;

public class OutputFileDelimitedUiConverter extends OutputUiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputFileDelimitedUiConverter.class);

	public OutputFileDelimitedUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new OFDelimited();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {

		super.prepareUIXML();
		LOGGER.debug("Fetching OutPut-Delimited-Component for {}", componentName);
		TextFileDelimited fileDelimited = (TextFileDelimited) typeBaseComponent;
		uiComponent.setComponentLabel(fileDelimited.getId());
		propertyMap.put(PropertyNameConstants.HAS_HEADER.value(),
				convertBooleanVlaue(fileDelimited.getHasHeader(), PropertyNameConstants.HAS_HEADER.value()));
		if (fileDelimited.getPath() != null)
			propertyMap.put(PropertyNameConstants.PATH.value(), fileDelimited.getPath().getUri());
		propertyMap.put(PropertyNameConstants.STRICT.value(),
				convertBooleanVlaue(fileDelimited.getStrict(), PropertyNameConstants.STRICT.value()));
		propertyMap.put(PropertyNameConstants.IS_SAFE.value(),
				convertBooleanVlaue(fileDelimited.getSafe(), PropertyNameConstants.IS_SAFE.value()));
		propertyMap.put(PropertyNameConstants.CHAR_SET.value(), getCharSet());
		if (fileDelimited.getDelimiter() != null)
			propertyMap.put(PropertyNameConstants.DELIMITER.value(), fileDelimited.getDelimiter().getValue());

		uiComponent.setType(UIComponentsConstants.FILE_DELIMITED.value());
		uiComponent.setCategory(UIComponentsConstants.OUTPUT_CATEGORY.value());
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(fileDelimited.getId());
		uiComponent.setProperties(propertyMap);
		validateComponentProperties(propertyMap);
	}

	private Object getCharSet() {
		TextFileDelimited fileDelimited = (TextFileDelimited) typeBaseComponent;
		Object value = null;
		if (fileDelimited.getCharset() != null) {
			value = fileDelimited.getCharset().getValue();
			if (value != null) {
				return fileDelimited.getCharset().getValue().value();
			} else {
				value = getValue(PropertyNameConstants.CHAR_SET.value());
			}
		}
		return value;

	}

	@Override
	protected Object getSchema(TypeOutputInSocket inSocket) {
		LOGGER.debug("Generating UI-Schema data for OutPut-Delimited-Component - {}", componentName);
		Schema schema = null;
		List<GridRow> gridRow = new ArrayList<>();
		ConverterUiHelper converterUiHelper = new ConverterUiHelper(uiComponent);
		if (inSocket.getSchema() != null && inSocket.getSchema().getFieldOrRecordOrIncludeExternalSchema().size() != 0) {
			schema=new Schema();
			for (Object record : inSocket.getSchema().getFieldOrRecordOrIncludeExternalSchema()) {
				if ((TypeExternalSchema.class).isAssignableFrom(record.getClass())) {
					schema.setIsExternal(true);
					if (((TypeExternalSchema) record).getUri() != null)
						schema.setExternalSchemaPath(((TypeExternalSchema) record).getUri());
				} else {
					gridRow.add(converterUiHelper.getSchema(record));
					schema.setGridRow(gridRow);
					schema.setIsExternal(false);
				}
			}
		}
		return schema;
	}

	@Override
	protected Map<String, String> getRuntimeProperties() {
		LOGGER.debug("Fetching runtime properties for -", componentName);
		TreeMap<String, String> runtimeMap = null;
		TypeProperties typeProperties = ((TextFileDelimited) typeBaseComponent).getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new TreeMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}

}
