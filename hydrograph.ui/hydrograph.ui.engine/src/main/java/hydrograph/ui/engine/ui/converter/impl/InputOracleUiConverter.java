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
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.engine.jaxb.inputtypes.Oracle;
import hydrograph.ui.datastructure.property.DatabaseSelectionConfig;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.InputUiConverter;
import hydrograph.ui.engine.ui.helper.ConverterUiHelper;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.IOracle;
import hydrograph.ui.logging.factory.LogFactory;

/**
 * Converter to convert jaxb oracle object into input Oracle component
 * @author Bitwise
 *
 */
public class InputOracleUiConverter extends InputUiConverter {
	
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputOracleUiConverter.class);
	
	public InputOracleUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new IOracle();
		this.propertyMap = new LinkedHashMap<>();
		
	}
	
	
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching Input-Oracle-Properties for {}", componentName);
		 Oracle inputOracle = (Oracle) typeBaseComponent;
		 DatabaseSelectionConfig databaseSelectionConfig = new DatabaseSelectionConfig();
		
		if(inputOracle.getDriverType() !=null && StringUtils.isNotBlank(inputOracle.getDriverType().getValue())){
			propertyMap.put(PropertyNameConstants.JDBC_DRIVER.value(), (String)(inputOracle.getDriverType().getValue()));
		}
		
		if(inputOracle.getHostName() !=null && StringUtils.isNotBlank(inputOracle.getHostName().getValue())){
			propertyMap.put(PropertyNameConstants.HOST_NAME.value(), (String)(inputOracle.getHostName().getValue()));
		}
		
		if(inputOracle.getPort() != null){
			propertyMap.put(PropertyNameConstants.PORT_NO.value(), inputOracle.getPort().getValue().toString());
		}
		
		if(inputOracle.getSchemaName() !=null && StringUtils.isNotBlank(inputOracle.getSchemaName().getValue())){
			propertyMap.put(PropertyNameConstants.ORACLE_SCHEMA.value(), (String)(inputOracle.getSchemaName().getValue()));
		}
		
		if(inputOracle.getUserName() != null && StringUtils.isNotBlank(inputOracle.getUserName().getValue())){
			propertyMap.put(PropertyNameConstants.USER_NAME.value(), (String)(inputOracle.getUserName().getValue()));
		}
		
		if(inputOracle.getPassword() !=null && StringUtils.isNotBlank(inputOracle.getPassword().getValue())){
			propertyMap.put(PropertyNameConstants.PASSWORD.value(), (String)(inputOracle.getPassword().getValue()));
		}
		
		if(inputOracle.getTableName() !=null &&  StringUtils.isNotBlank(inputOracle.getTableName().getValue())){
			databaseSelectionConfig.setTableName(inputOracle.getTableName().getValue());
			databaseSelectionConfig.setTableNameSelection(true);
		}
		
		if(inputOracle.getSelectQuery() !=null && StringUtils.isNotBlank(inputOracle.getSelectQuery().getValue())){
			databaseSelectionConfig.setSqlQuery(inputOracle.getSelectQuery().getValue());
		}
		
		if(inputOracle.getCountQuery()!=null && StringUtils.isNotBlank(inputOracle.getCountQuery().getValue())){
			databaseSelectionConfig.setSqlQueryCounter(inputOracle.getCountQuery().getValue());
		}
		
		if(databaseSelectionConfig !=null){
			propertyMap.put(PropertyNameConstants.ORACLE_SELECT_OPTION.value(), databaseSelectionConfig);
		}
		
		uiComponent.setType(UIComponentsConstants.ORACLE.value());
		uiComponent.setCategory(UIComponentsConstants.INPUT_CATEGORY.value());
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(inputOracle.getId());
		uiComponent.setProperties(propertyMap);
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