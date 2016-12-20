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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeExternalSchema;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;
import hydrograph.engine.jaxb.commontypes.TypeKeyFields;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.engine.jaxb.ooracle.TypePriamryKeys;
import hydrograph.engine.jaxb.ooracle.TypeUpdateKeys;
import hydrograph.engine.jaxb.outputtypes.Oracle;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.OutputUiConverter;
import hydrograph.ui.engine.ui.helper.ConverterUiHelper;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.OOracle;
import hydrograph.ui.logging.factory.LogFactory;

public class OutputOracleUiConverter extends OutputUiConverter {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputOracleUiConverter.class);
	private Oracle outputOracle;
	private LinkedHashMap<String, String> loadSelectedDetails;
	
	public OutputOracleUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new OOracle();
		this.propertyMap = new LinkedHashMap<>();
	}
	
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching Output-Oracle-Properties for {}", componentName);
		outputOracle = (Oracle) typeBaseComponent;
		loadSelectedDetails = new LinkedHashMap<String, String>();
		
		if(StringUtils.isNotBlank(outputOracle.getDriverType().getValue())){
			propertyMap.put(PropertyNameConstants.JDBC_DRIVER.value(), (String)(outputOracle.getDriverType().getValue()));
		}
		
		if(StringUtils.isNotBlank(outputOracle.getHostName().getValue())){
			propertyMap.put(PropertyNameConstants.HOST_NAME.value(), (String)(outputOracle.getHostName().getValue()));
		}
		
		if(outputOracle.getPort() != null){
			propertyMap.put(PropertyNameConstants.PORT_NO.value(), outputOracle.getPort().getValue().toString());
		}
		
		if(StringUtils.isNotBlank(outputOracle.getSid().getValue())){
			propertyMap.put(PropertyNameConstants.ORACLE_SID.value(), (String)(outputOracle.getSid().getValue()));
		}
		
		if(StringUtils.isNotBlank(outputOracle.getSchemaName().getValue())){
			propertyMap.put(PropertyNameConstants.ORACLE_SCHEMA.value(), (String)(outputOracle.getSchemaName().getValue()));
		}
		
		if(StringUtils.isNotBlank(outputOracle.getUserName().getValue())){
			propertyMap.put(PropertyNameConstants.USER_NAME.value(), (String)(outputOracle.getUserName().getValue()));
		}
		
		if(StringUtils.isNotBlank(outputOracle.getPassword().getValue())){
			propertyMap.put(PropertyNameConstants.PASSWORD.value(), (String)(outputOracle.getPassword().getValue()));
		}
		
		if(StringUtils.isNotBlank(outputOracle.getTableName().getValue())){
			propertyMap.put(PropertyNameConstants.ORACLE_TABLE_NAME.value(), (String)(outputOracle.getTableName().getValue()));
		}
		
		if(outputOracle.getLoadType() !=null){
			if(outputOracle.getLoadType().getInsert() !=null){
				loadSelectedDetails.put(Constants.LOAD_TYPE_INSERT_KEY, outputOracle.getLoadType().getInsert().toString());
			}else if(outputOracle.getLoadType().getTruncateLoad() !=null){
				loadSelectedDetails.put(Constants.LOAD_TYPE_REPLACE_KEY,outputOracle.getLoadType().getTruncateLoad().toString());
			} else if(outputOracle.getLoadType().getUpdate() !=null){
				loadSelectedDetails.put(Constants.LOAD_TYPE_UPDATE_KEY,getLoadTypeUpdateKeyUIValue(outputOracle.getLoadType().getUpdate()));
			}else if(outputOracle.getLoadType().getNewTable() !=null){
				loadSelectedDetails.put(Constants.LOAD_TYPE_NEW_TABLE_KEY,getLoadTypePrimaryKeyUIValue(outputOracle.getLoadType().getNewTable()));
			}
				
		}
		propertyMap.put(PropertyNameConstants.LOAD_TYPE_CONFIGURATION.value(), loadSelectedDetails);
		
		uiComponent.setType(UIComponentsConstants.ORACLE.value());
		uiComponent.setCategory(UIComponentsConstants.OUTPUT_CATEGORY.value());
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(outputOracle.getId());
		uiComponent.setProperties(propertyMap);
	}
	
	/**
	 * Appends primary keys using a comma
	 * @param newTable
	 */
	private String getLoadTypePrimaryKeyUIValue(TypePriamryKeys newTable) {
		StringBuffer stringBuffer = new StringBuffer();
		if(newTable !=null && newTable.getPrimaryKeys() !=null){
			TypeKeyFields typeKeyFields = newTable.getPrimaryKeys();
			for(TypeFieldName typeFieldName : typeKeyFields.getField()){
				stringBuffer.append(typeFieldName.getName());
				stringBuffer.append(",");
			}
		}
		return StringUtils.removeEnd(stringBuffer.toString(), ",");
	}
	
	/**
	 *  Appends update keys using a comma
	 * @param update
	 */
	private String getLoadTypeUpdateKeyUIValue(TypeUpdateKeys update) {
		StringBuffer buffer=new StringBuffer();
			if(update!=null && update.getUpdateByKeys()!=null){
				TypeKeyFields keyFields=update.getUpdateByKeys();
				for(TypeFieldName fieldName:keyFields.getField()){
					buffer.append(fieldName.getName());
					buffer.append(",");
					}
			}
		
		return StringUtils.removeEnd(buffer.toString(), ",");
	}

	@Override
	protected Object getSchema(TypeOutputInSocket inSocket) {
		LOGGER.debug("Generating UI-Schema data for OutPut-Oracle-Component - {}", componentName);
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
		LOGGER.debug("Generating Runtime Properties for -{}", componentName);
		TreeMap<String, String> runtimeMap = null;
		TypeProperties typeProperties = ((Oracle) typeBaseComponent).getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new TreeMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}

}
