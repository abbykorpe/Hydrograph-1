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

package hydrograph.ui.engine.ui.converter.impl;

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeExternalSchema;
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.engine.jaxb.ihiveparquet.HivePartitionFieldsType;
import hydrograph.engine.jaxb.ihiveparquet.HivePartitionFilterType;
import hydrograph.engine.jaxb.ihiveparquet.PartitionColumn;
import hydrograph.engine.jaxb.ihiveparquet.PartitionFieldBasicType;
import hydrograph.engine.jaxb.inputtypes.ParquetHiveFile;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.InputHivePartitionColumn;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.InputUiConverter;
import hydrograph.ui.engine.ui.helper.ConverterUiHelper;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.IHiveParquet;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
/**
 * The class InputHiveParquetUiConverter
 * 
 * @author eyy445
 * 
 */

public class InputHiveParquetUiConverter extends InputUiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputHiveParquetUiConverter.class);
	private ParquetHiveFile parquetHive;
	private LinkedHashMap<String, Object> property;
	
	public InputHiveParquetUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new IHiveParquet();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching Input-HiveParquet-Properties for {}", componentName);
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
		uiComponent.setCategory(UIComponentsConstants.INPUT_CATEGORY.value());
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(parquetHive.getId());
		uiComponent.setProperties(propertyMap);
	}

	/*
	 * returns Partition keys list
	 */
	private LinkedHashMap<String, Object> getPartitionKeys(){
		LOGGER.debug("Fetching Input Hive Parquet-Partition-Keys-Properties for -{}", componentName);
		property = new LinkedHashMap<String, Object>();
		parquetHive = (ParquetHiveFile) typeBaseComponent;
		HivePartitionFieldsType typeHivePartitionFields = parquetHive.getPartitionKeys();
			if (typeHivePartitionFields != null){
					if(typeHivePartitionFields.getField()!=null){
					PartitionFieldBasicType partitionFieldBasicType = typeHivePartitionFields.getField();
					property.put(partitionFieldBasicType.getName(),null);
						if(partitionFieldBasicType.getField()!=null){
							getKey(partitionFieldBasicType);
						}
					}
			}
		
			Set<String> keys=new HashSet<String>();
			if(property!=null){
			keys=property.keySet();
			}
			List <String>list= new ArrayList<String>();
			list.addAll(keys);
			List<InputHivePartitionColumn> inputHivePartitionColumn = new ArrayList<InputHivePartitionColumn>();
	
			HivePartitionFilterType hivePartitionFilterType=parquetHive.getPartitionFilter();
				if(hivePartitionFilterType!=null){
				List<PartitionColumn> partitionColumn=hivePartitionFilterType.getPartitionColumn();
		
					if(partitionColumn!=null){
						for(PartitionColumn pc:partitionColumn){
							InputHivePartitionColumn inputHivePartitionColumn3 = new InputHivePartitionColumn();
							inputHivePartitionColumn3.setName(pc.getName());
							inputHivePartitionColumn3.setValue(pc.getValue());
							if(pc.getPartitionColumn()!=null){	
								addFilterKey(pc,inputHivePartitionColumn3);
							}
							inputHivePartitionColumn.add(inputHivePartitionColumn3);
						}	
					}
				    property.put(list.get(0),inputHivePartitionColumn);
				}
		    return property;
		}
	
		private void getKey(PartitionFieldBasicType partition){
			PartitionFieldBasicType partitionFieldBasicType1 = partition.getField();
			property.put(partitionFieldBasicType1.getName(),null);
					if(partitionFieldBasicType1.getField()!=null){
						getKey(partitionFieldBasicType1);
					}
		}
	
	private void addFilterKey(PartitionColumn partitionColumn1,InputHivePartitionColumn inputHivePartitionColumn1)
	{
		   	InputHivePartitionColumn inputHivePartitionColumn2 = new InputHivePartitionColumn();
			PartitionColumn partitionColumn2 = partitionColumn1.getPartitionColumn();
			inputHivePartitionColumn2.setName(partitionColumn2.getName());
		    inputHivePartitionColumn2.setValue(partitionColumn2.getValue());
		    inputHivePartitionColumn1.setInputHivePartitionColumn(inputHivePartitionColumn2);
				if(partitionColumn2.getPartitionColumn()!=null){
						if(partitionColumn2.getPartitionColumn().getValue()!=""){
						addFilterKey(partitionColumn2,inputHivePartitionColumn2);
						}
				}
	}


	@Override
	protected Map<String, String> getRuntimeProperties(){
		LOGGER.debug("Generating Runtime Properties for -{}", componentName);
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

	@Override
	protected Object getSchema(TypeInputOutSocket outSocket) {
		LOGGER.debug("Generating UI-Schema data for {}", componentName);
		Schema schema = null;
		List<GridRow> gridRowList = new ArrayList<>();
		ConverterUiHelper converterUiHelper = new ConverterUiHelper(uiComponent);
		if (outSocket.getSchema() != null
				&& outSocket.getSchema().getFieldOrRecordOrIncludeExternalSchema().size() != 0) {
			schema = new Schema();
			for (Object record : outSocket.getSchema().getFieldOrRecordOrIncludeExternalSchema()) {
				if ((TypeExternalSchema.class).isAssignableFrom(record.getClass())) {
					schema.setIsExternal(true);
					if (((TypeExternalSchema) record).getUri() != null)
						schema.setExternalSchemaPath(((TypeExternalSchema) record).getUri());
				} else {
					gridRowList.add(converterUiHelper.getSchema(record));
					schema.setGridRow(gridRowList);
					schema.setIsExternal(false);
				}
				saveComponentOutputSchema(outSocket.getId(),gridRowList);
			}
		} 
		return schema;

	}
}
