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
import hydrograph.engine.jaxb.omysql.TypePriamryKeys;
import hydrograph.engine.jaxb.omysql.TypeUpdateKeys;
import hydrograph.engine.jaxb.outputtypes.Mysql;
import hydrograph.engine.jaxb.outputtypes.Oracle;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.OutputUiConverter;
import hydrograph.ui.engine.ui.helper.ConverterUiHelper;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.OMysql;
import hydrograph.ui.logging.factory.LogFactory;

public class OutputMysqlUiConverter extends OutputUiConverter{

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputMysqlUiConverter.class);
	private Mysql outputMysql;
	private LinkedHashMap<String, String> loadSelectedDetails;
	
	public OutputMysqlUiConverter(TypeBaseComponent typeBaseComponent, Container container){
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new OMysql();
		this.propertyMap = new LinkedHashMap<>();
	}
	
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching Output-MySql-Properties for {}", componentName);
		outputMysql = (Mysql) typeBaseComponent;
		loadSelectedDetails = new LinkedHashMap<String, String>();
		
		if(StringUtils.isNotBlank(outputMysql.getJdbcDriver().getValue())){
			propertyMap.put(PropertyNameConstants.JDBC_DRIVER.value(), (String)(outputMysql.getJdbcDriver().getValue()));
		}
		
		if(StringUtils.isNotBlank(outputMysql.getHostName().getValue())){
			propertyMap.put(PropertyNameConstants.HOST_NAME.value(), (String)(outputMysql.getHostName().getValue()));
		}
		
		if(outputMysql.getPort() != null){
			propertyMap.put(PropertyNameConstants.PORT_NO.value(), outputMysql.getPort().getValue().toString());
		}
		
		if(StringUtils.isNotBlank(outputMysql.getDatabaseName().getValue())){
			propertyMap.put(PropertyNameConstants.DATABASE_NAME.value(), (String)(outputMysql.getDatabaseName().getValue()));
		}
		
		if(StringUtils.isNotBlank(outputMysql.getUsername().getValue())){
			propertyMap.put(PropertyNameConstants.USER_NAME.value(), (String)(outputMysql.getUsername().getValue()));
		}
		
		if(StringUtils.isNotBlank(outputMysql.getPassword().getValue())){
			propertyMap.put(PropertyNameConstants.PASSWORD.value(), (String)(outputMysql.getPassword().getValue()));
		}
		
		if(StringUtils.isNotBlank(outputMysql.getTableName().getValue())){
			propertyMap.put(PropertyNameConstants.ORACLE_TABLE_NAME.value(), (String)(outputMysql.getTableName().getValue()));
		}
		
		if(outputMysql.getChunkSize() != null){
			propertyMap.put(PropertyNameConstants.CHUNK_SIZE.value(), (String)(outputMysql.getChunkSize().getValue().toString()));
		}
		
		if(outputMysql.getLoadType() !=null){
			if(outputMysql.getLoadType().getInsert() !=null){
				loadSelectedDetails.put(Constants.LOAD_TYPE_INSERT_KEY, outputMysql.getLoadType().getInsert().toString());
			}else if(outputMysql.getLoadType().getTruncateLoad() !=null){
				loadSelectedDetails.put(Constants.LOAD_TYPE_REPLACE_KEY,outputMysql.getLoadType().getTruncateLoad().toString());
			} else if(outputMysql.getLoadType().getUpdate() !=null){
				loadSelectedDetails.put(Constants.LOAD_TYPE_UPDATE_KEY,getLoadTypeUpdateKeyUIValue(outputMysql.getLoadType().getUpdate()));
			}else if(outputMysql.getLoadType().getNewTable() !=null){
				loadSelectedDetails.put(Constants.LOAD_TYPE_NEW_TABLE_KEY,getLoadTypePrimaryKeyUIValue(outputMysql.getLoadType().getNewTable()));
			}
				
		}
		propertyMap.put(PropertyNameConstants.LOAD_TYPE_CONFIGURATION.value(), loadSelectedDetails);
		
		uiComponent.setType(UIComponentsConstants.ORACLE.value());
		uiComponent.setCategory(UIComponentsConstants.OUTPUT_CATEGORY.value());
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(outputMysql.getId());
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
