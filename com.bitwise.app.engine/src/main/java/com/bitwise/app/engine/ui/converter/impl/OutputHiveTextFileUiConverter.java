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

import com.bitwise.app.graph.model.components.OHiveTextFile;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.ohivetextfile.FieldBasicType;
import com.bitwiseglobal.graph.ohivetextfile.HivePartitionFieldsType;
import com.bitwiseglobal.graph.outputtypes.HiveTextFile;


public class OutputHiveTextFileUiConverter extends OutputUiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputHiveTextFileUiConverter.class);
	private HiveTextFile hiveTextfile;

	public OutputHiveTextFileUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new OHiveTextFile();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching Input-HiveParquet-Properties for {}", componentName);
		hiveTextfile = (HiveTextFile) typeBaseComponent;
		
		if (hiveTextfile.getDatabaseName() != null){
			propertyMap.put(PropertyNameConstants.DATABASE_NAME.value(), (String)(hiveTextfile.getDatabaseName().getValue()));
		}
		if (hiveTextfile.getTableName() != null){
			propertyMap.put(PropertyNameConstants.TABLE_NAME.value(), (String)(hiveTextfile.getTableName().getValue()));
			}
		if (hiveTextfile.getExternalTablePath() != null){
			propertyMap.put(PropertyNameConstants.EXTERNAL_TABLE_PATH.value(), (String)hiveTextfile.getExternalTablePath().getUri());
		}
		propertyMap.put(PropertyNameConstants.PARTITION_KEYS.value(), getPartitionKeys());
		propertyMap.put(PropertyNameConstants.STRICT.value(),
				convertBooleanVlaue(hiveTextfile.getStrict(), PropertyNameConstants.STRICT.value()));
		if (hiveTextfile.getDelimiter() != null)
			propertyMap.put(PropertyNameConstants.DELIMITER.value(), hiveTextfile.getDelimiter().getValue());
		if (hiveTextfile.getQuote() != null)
			propertyMap.put(PropertyNameConstants.QUOTE.value(), hiveTextfile.getQuote().getValue());
		propertyMap.put(PropertyNameConstants.IS_SAFE.value(),
				convertBooleanVlaue(hiveTextfile.getSafe(), PropertyNameConstants.IS_SAFE.value()));
		
		
		uiComponent.setComponentLabel(hiveTextfile.getId());
		uiComponent.setType(UIComponentsConstants.HIVE_TEXTFILE.value());
		uiComponent.setCategory(UIComponentsConstants.INPUT_CATEGORY.value());
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(hiveTextfile.getId());
		uiComponent.setProperties(propertyMap);
		validateComponentProperties(propertyMap);
	}

	private List<String> getPartitionKeys() {
		LOGGER.debug("Fetching Input Hive Parquet-Partition-Keys-Properties for -{}", componentName);
		List<String> partitionKeySet = null;
		hiveTextfile = (HiveTextFile) typeBaseComponent;
		HivePartitionFieldsType typeHivePartitionFields = hiveTextfile.getPartitionKeys();
		if (typeHivePartitionFields != null) {

			partitionKeySet = new ArrayList<String>();
			for (FieldBasicType fieldName : typeHivePartitionFields.getField()) {
				partitionKeySet.add(fieldName.getName());
			}
		}
		return partitionKeySet;
	}


	@Override
	protected Map<String, String> getRuntimeProperties() {
		LOGGER.debug("Generating Runtime Properties for -{}", componentName);
		TreeMap<String, String> runtimeMap = null;
		TypeProperties typeProperties = ((HiveTextFile) typeBaseComponent).getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new TreeMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}

	@Override
	protected Object getSchema(TypeOutputInSocket inSocket) {
		LOGGER.debug("Generating UI-Schema data for OutPut-Hive-TextFile-Component - {}", componentName);
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
	
	
}
