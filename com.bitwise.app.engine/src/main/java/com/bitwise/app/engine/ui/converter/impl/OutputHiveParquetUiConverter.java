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
import com.bitwise.app.graph.model.components.OHiveParquet;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeExternalSchema;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.ohiveparquet.FieldBasicType;
import com.bitwiseglobal.graph.ohiveparquet.HivePartitionFieldsType;
import com.bitwiseglobal.graph.outputtypes.ParquetHiveFile;

/**
 * The class OutputHiveParquetUiConverter
 * 
 * @author eyy445
 * 
 */
public class OutputHiveParquetUiConverter extends OutputUiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputHiveParquetUiConverter.class);
	private ParquetHiveFile parquetHive;
	
	public OutputHiveParquetUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new OHiveParquet();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching Output-HiveParquet-Properties for {}", componentName);
		parquetHive = (ParquetHiveFile) typeBaseComponent;
		
		if (parquetHive.getDatabaseName() != null){
			propertyMap.put(PropertyNameConstants.DATABASE_NAME.value(), (String)(parquetHive.getDatabaseName().getValue()));
		}
		if (parquetHive.getTableName() != null){
			propertyMap.put(PropertyNameConstants.TABLE_NAME.value(), (String)(parquetHive.getTableName().getValue()));
			}
		if (parquetHive.getExternalTablePath() != null){
			propertyMap.put(PropertyNameConstants.EXTERNAL_TABLE_PATH.value(), (String)parquetHive.getExternalTablePath().getUri());
		}
		propertyMap.put(PropertyNameConstants.PARTITION_KEYS.value(), getPartitionKeys());
		
		uiComponent.setComponentLabel(parquetHive.getId());
		uiComponent.setType(UIComponentsConstants.HIVE_PARQUET.value());
		uiComponent.setCategory(UIComponentsConstants.OUTPUT_CATEGORY.value());
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(parquetHive.getId());
		uiComponent.setProperties(propertyMap);
		validateComponentProperties(propertyMap);
	}

	/*
	 * returns Partition keys list
	 */
	private List<String> getPartitionKeys() {
		LOGGER.debug("Fetching Input Hive Parquet-Partition-Keys-Properties for -{}", componentName);
		List<String> partitionKeySet = null;
		parquetHive = (ParquetHiveFile) typeBaseComponent;
		HivePartitionFieldsType typeHivePartitionFields = parquetHive.getPartitionKeys();
		if (typeHivePartitionFields != null) {

			partitionKeySet = new ArrayList<String>();
			for (FieldBasicType fieldName : typeHivePartitionFields.getField()) {
				partitionKeySet.add(fieldName.getName());
			}
		}
		return partitionKeySet;
	}

	@Override
	protected Object getSchema(TypeOutputInSocket inSocket) {
		LOGGER.debug("Generating UI-Schema data for OutPut-Hive-Parquet-Component - {}", componentName);
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
		TypeProperties typeProperties = ((ParquetHiveFile) typeBaseComponent).getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new TreeMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}

}
