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
import hydrograph.engine.jaxb.inputtypes.Teradata;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.DatabaseSelectionConfig;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.MatchValueProperty;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.InputUiConverter;
import hydrograph.ui.engine.ui.helper.ConverterUiHelper;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.ITeradata;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.messages.Messages;

/**
 * The Class InputTeradata Converter implementation for Input Teradata Component
 * @author Bitwise
 *
 */
public class InputTeradataUiConverter extends InputUiConverter{

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(InputTeradataUiConverter.class);
	
	public InputTeradataUiConverter(TypeBaseComponent typeBaseComponent, Container container){
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new ITeradata();
		this.propertyMap = new LinkedHashMap<>();
	}
	
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching Input-Teradata-Properties for {}", componentName);
		Teradata inputTeradata = (Teradata) typeBaseComponent;
		DatabaseSelectionConfig databaseSelectionConfig = new DatabaseSelectionConfig();

		setValueInPropertyMap(PropertyNameConstants.JDBC_DRIVER.value(), 
				inputTeradata.getJdbcDriver() == null ? "" : inputTeradata.getJdbcDriver().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.HOST_NAME.value(), 
				inputTeradata.getHostName() == null ? "" : inputTeradata.getHostName().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.PORT_NO.value(), 
				inputTeradata.getPort() == null ? "" : inputTeradata.getPort().getValue().toString());
		
		setValueInPropertyMap(PropertyNameConstants.DATABASE_NAME.value(), 
				inputTeradata.getDatabaseName() == null ? "" : inputTeradata.getDatabaseName().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.USER_NAME.value(), 
				inputTeradata.getUsername() == null ? "" : inputTeradata.getUsername().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.PASSWORD.value(), 
				inputTeradata.getPassword()==null ? "" : inputTeradata.getPassword().getValue());
		
		if(inputTeradata.getTableName() != null && StringUtils.isNotBlank(inputTeradata.getTableName().getValue())){
			databaseSelectionConfig.setTableName(inputTeradata.getTableName().getValue());
			databaseSelectionConfig.setTableNameSelection(true);
		}
		
		if(inputTeradata.getSelectQuery() != null && StringUtils.isNotBlank(inputTeradata.getSelectQuery().getValue())){
			databaseSelectionConfig.setSqlQuery(inputTeradata.getSelectQuery().getValue());
		}
		
		if(inputTeradata.getCountQuery() != null && StringUtils.isNotBlank(inputTeradata.getCountQuery().getValue())){
			
			databaseSelectionConfig.setSqlQueryCounter(inputTeradata.getCountQuery().getValue());
		}
		
		propertyMap.put(PropertyNameConstants.ORACLE_SELECT_OPTION.value(), databaseSelectionConfig);
		
		propertyMap.put(PropertyNameConstants.SELECT_INTERFACE.value(), getInterface());
			
		uiComponent.setType(UIComponentsConstants.TERADATA.value());
		uiComponent.setCategory(UIComponentsConstants.INPUT_CATEGORY.value());
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(inputTeradata.getId());
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

	private MatchValueProperty getInterface() {
		LOGGER.debug("Generating Match for -{}", componentName);
		MatchValueProperty matchValue =  new MatchValueProperty();
		Teradata outputTeradata = (Teradata) typeBaseComponent;
		if(Constants.DEFAULT.equalsIgnoreCase(outputTeradata.getInterface().getValue())){
			matchValue.setMatchValue(Messages.STANDARD);
		}else if(Constants.FASTEXPORT.equalsIgnoreCase(outputTeradata.getInterface().getValue())){
			matchValue.setMatchValue(Messages.FAST_EXPORT);
		}
		matchValue.setRadioButtonSelected(true);
		return matchValue;
	}
	
	@Override
	protected Map<String, String> getRuntimeProperties() {
		LOGGER.debug("Generating Runtime Properties for -{}", componentName);
		TreeMap<String, String> runtimeMap = null;
		TypeProperties typeProperties = ((Teradata) typeBaseComponent).getRuntimeProperties();
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
