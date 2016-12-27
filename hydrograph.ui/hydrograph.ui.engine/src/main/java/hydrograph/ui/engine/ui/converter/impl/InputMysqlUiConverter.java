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
import hydrograph.engine.jaxb.inputtypes.Mysql;
import hydrograph.ui.datastructure.property.DatabaseSelectionConfig;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.InputUiConverter;
import hydrograph.ui.engine.ui.helper.ConverterUiHelper;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.IMysql;
import hydrograph.ui.logging.factory.LogFactory;

/**
 * The Class InputMysqlUiConverter
 * @author Bitwise
 *
 */
public class InputMysqlUiConverter extends InputUiConverter{

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputMysqlUiConverter.class);
	
	public InputMysqlUiConverter(TypeBaseComponent typeBaseComponent, Container container){
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new IMysql();
		this.propertyMap = new LinkedHashMap<>();
	}
	
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching Input-Mysql-Properties for {}", componentName);
		Mysql inputMysql = (Mysql) typeBaseComponent;
		DatabaseSelectionConfig databaseSelectionConfig = new DatabaseSelectionConfig();

		setValueInPropertyMap(PropertyNameConstants.JDBC_DRIVER.value(), 
				inputMysql.getJdbcDriver() == null ? "" : inputMysql.getJdbcDriver().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.HOST_NAME.value(), 
				inputMysql.getHostName() == null ? "" : inputMysql.getHostName().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.PORT_NO.value(), 
				inputMysql.getPort() == null ? "" : inputMysql.getPort().getValue().toString());
		
		setValueInPropertyMap(PropertyNameConstants.DATABASE_NAME.value(), 
				inputMysql.getDatabaseName() == null ? "" : inputMysql.getDatabaseName().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.USER_NAME.value(), 
				inputMysql.getUsername() == null ? "" : inputMysql.getUsername().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.PASSWORD.value(), 
				inputMysql.getPassword()==null ? "" : inputMysql.getPassword().getValue());
		
		if(inputMysql.getTableName() != null && StringUtils.isNotBlank(inputMysql.getTableName().getValue())){
			databaseSelectionConfig.setTableName(inputMysql.getTableName().getValue());
			databaseSelectionConfig.setTableNameSelection(true);
		}
		
		if(inputMysql.getSelectQuery() != null && StringUtils.isNotBlank(inputMysql.getSelectQuery().getValue())){
			databaseSelectionConfig.setSqlQuery(inputMysql.getSelectQuery().getValue());
		}
		
		if(inputMysql.getCountQuery() != null && StringUtils.isNotBlank(inputMysql.getCountQuery().getValue())){
			
			databaseSelectionConfig.setSqlQueryCounter(inputMysql.getCountQuery().getValue());
		}
		
		propertyMap.put(PropertyNameConstants.ORACLE_SELECT_OPTION.value(), databaseSelectionConfig);
		
		uiComponent.setType(UIComponentsConstants.MYSQL.value());
		uiComponent.setCategory(UIComponentsConstants.INPUT_CATEGORY.value());
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(inputMysql.getId());
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
		TypeProperties typeProperties = ((Mysql) typeBaseComponent).getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new TreeMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}

	private void setValueInPropertyMap(String propertyName,String value){
		propertyMap.put(propertyName, StringUtils.isNotBlank(value) ? value : "");
	}
	
}
