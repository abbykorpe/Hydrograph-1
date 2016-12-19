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
import hydrograph.engine.jaxb.inputtypes.Oracle;
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

		if(StringUtils.isNotBlank(inputMysql.getJdbcDriver().getValue())){
			propertyMap.put(PropertyNameConstants.JDBC_DRIVER.value(), (String)(inputMysql.getJdbcDriver().getValue()));
		}
		
		if(StringUtils.isNotBlank(inputMysql.getHostName().getValue())){
			propertyMap.put(PropertyNameConstants.HOST_NAME.value(), (String)(inputMysql.getHostName().getValue()));
		}
		
		if(inputMysql.getPort() != null){
			propertyMap.put(PropertyNameConstants.PORT_NO.value(), inputMysql.getPort().getValue().toString());
		}
		
		if(StringUtils.isNotBlank(inputMysql.getDatabaseName().getValue())){
			propertyMap.put(PropertyNameConstants.DATABASE_NAME.value(), (String)(inputMysql.getDatabaseName().getValue()));
		}
		
		
		if(StringUtils.isNotBlank(inputMysql.getUsername().getValue())){
			propertyMap.put(PropertyNameConstants.USER_NAME.value(), (String)(inputMysql.getUsername().getValue()));
		}
		
		if(StringUtils.isNotBlank(inputMysql.getPassword().getValue())){
			propertyMap.put(PropertyNameConstants.PASSWORD.value(), (String)(inputMysql.getPassword().getValue()));
		}
		
		if(StringUtils.isNotBlank(inputMysql.getTableName().getValue())){
			databaseSelectionConfig.setTableName(inputMysql.getTableName().getValue());
			databaseSelectionConfig.setTableName(true);
		}
		
		if(StringUtils.isNotBlank(inputMysql.getSelectQuery().getValue())){
			databaseSelectionConfig.setSqlQuery(inputMysql.getSelectQuery().getValue());
		}
		
		if(StringUtils.isNotBlank(inputMysql.getCountQuery().getValue())){
			databaseSelectionConfig.setSqlQueryCounter(inputMysql.getCountQuery().getValue());
		}
		
		if(databaseSelectionConfig !=null){
			propertyMap.put(PropertyNameConstants.ORACLE_SELECT_OPTION.value(), databaseSelectionConfig);
		}
		
		uiComponent.setType(UIComponentsConstants.ORACLE.value());
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
