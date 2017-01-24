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
import hydrograph.engine.jaxb.ojdbcupdate.TypeUpdateKeys;
import hydrograph.engine.jaxb.outputtypes.JdbcUpdate;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.OutputUiConverter;
import hydrograph.ui.engine.ui.helper.ConverterUiHelper;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.components.ODBUpdate;
import hydrograph.ui.logging.factory.LogFactory;

public class OutputDBUpdateUiConverter extends OutputUiConverter{
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(OutputDBUpdateUiConverter.class);
	
	public OutputDBUpdateUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new ODBUpdate();
		this.propertyMap = new LinkedHashMap<>();
	}
	
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching Output-Oracle-Properties for {}", componentName);
		JdbcUpdate jdbcUpdate = (JdbcUpdate) typeBaseComponent;
		
		setValueInPropertyMap(PropertyNameConstants.DB_URL.value(), 
				jdbcUpdate.getUrl() == null ? "" : jdbcUpdate.getUrl().getValue()); 
		
		setValueInPropertyMap(PropertyNameConstants.TABLE_NAME.value(), 
				jdbcUpdate.getTableName() == null ? "" : jdbcUpdate.getTableName().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.CHUNK_SIZE.value(), 
				jdbcUpdate.getBatchSize() == null ? "" : jdbcUpdate.getBatchSize().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.USER_NAME.value(), 
				jdbcUpdate.getUserName() == null ? "" : jdbcUpdate.getUserName().getValue());
		
		setValueInPropertyMap(PropertyNameConstants.PASSWORD.value(), 
				jdbcUpdate.getPassword()== null ? "" : jdbcUpdate.getPassword().getValue());
		
		if(jdbcUpdate.getUpdate() !=null){
			propertyMap.put(PropertyNameConstants.SELECT_BY_KEYS.value(),getUpdateKeyUIValue(jdbcUpdate.getUpdate()));
		}
		
		uiComponent.setType(UIComponentsConstants.DB_UPDATE.value());
		uiComponent.setCategory(UIComponentsConstants.OUTPUT_CATEGORY.value());
		
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(jdbcUpdate.getId());
		uiComponent.setProperties(propertyMap);
	}

	/**
	 *  Appends update keys using a comma
	 * @param typeUpdateKeys
	 */
	private String getUpdateKeyUIValue(TypeUpdateKeys typeUpdateKeys) {
		StringBuffer buffer=new StringBuffer();
			if(typeUpdateKeys!=null && typeUpdateKeys.getUpdateByKeys()!=null){
				TypeKeyFields keyFields=typeUpdateKeys.getUpdateByKeys();
				for(TypeFieldName fieldName:keyFields.getField()){
					buffer.append(fieldName.getName());
					buffer.append(",");
					}
			}
		
		return StringUtils.removeEnd(buffer.toString(), ",");
	}
	
	@Override
	protected Object getSchema(TypeOutputInSocket inSocket) {
		LOGGER.debug("Generating UI-Schema data for OutPut-DBUpdate-Component - {}", componentName);
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
		TypeProperties typeProperties = ((JdbcUpdate) typeBaseComponent).getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new TreeMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}
	
	private void setValueInPropertyMap(String propertyName,Object value){
		propertyMap.put(propertyName, getParameterValue(propertyName,value));;
	}

}
