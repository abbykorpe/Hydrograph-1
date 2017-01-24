package hydrograph.ui.engine.converter.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.ElementValueIntegerType;
import hydrograph.engine.jaxb.commontypes.ElementValueStringType;
import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;
import hydrograph.engine.jaxb.commontypes.TypeKeyFields;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;
import hydrograph.engine.jaxb.ojdbcupdate.TypeOutputJdbcupdateOutSocket;
import hydrograph.engine.jaxb.ojdbcupdate.TypeUpdateKeys;
import hydrograph.engine.jaxb.outputtypes.JdbcUpdate;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.JDBCDriverClassWidgetDatastructure;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.OutputConverter;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

public class OutputDBUpdateConverter extends OutputConverter {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(OutputDBUpdateConverter.class);
	
	public OutputDBUpdateConverter(Component component) {
		super(component);
		this.component = component;
		this.properties = component.getProperties();
		this.baseComponent = new JdbcUpdate();
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		JdbcUpdate jdbcUpdate = (JdbcUpdate) baseComponent;
		jdbcUpdate.setRuntimeProperties(getRuntimeProperties());

		if(StringUtils.isNotBlank(String.valueOf(properties.get(PropertyNameConstants.JDBC_DB_DRIVER.value())))){
			JDBCDriverClassWidgetDatastructure db = (JDBCDriverClassWidgetDatastructure) properties.get(PropertyNameConstants.JDBC_DB_DRIVER.value());
			
			
			JDBCDriverClassWidgetDatastructure JDBCDriverClassWidgetDatastructureValue= (JDBCDriverClassWidgetDatastructure)properties.get(PropertyNameConstants.JDBC_DB_DRIVER.value());
			
			ElementValueStringType jdbcDriverClass = new ElementValueStringType();
			jdbcDriverClass.setValue(JDBCDriverClassWidgetDatastructureValue.getJdbcDriverClassValue());
			
			jdbcUpdate.setJdbcDriverClass(jdbcDriverClass);
		}
		
		if(StringUtils.isNotBlank((String) properties.get(PropertyNameConstants.DB_URL.value()))){
			ElementValueStringType url = new ElementValueStringType();
			url.setValue(String.valueOf(properties.get(PropertyNameConstants.DB_URL.value())));
			jdbcUpdate.setUrl(url);
		}
		
		if(StringUtils.isNotBlank((String) properties.get(PropertyNameConstants.TABLE_NAME.value()))){
			ElementValueStringType tableName = new ElementValueStringType();
			tableName.setValue(String.valueOf(properties.get(PropertyNameConstants.TABLE_NAME.value())));
			jdbcUpdate.setTableName(tableName);
		}
		
		if(properties.get(PropertyNameConstants.CHUNK_SIZE.value()) !=null){
			ElementValueIntegerType chunkSize = new ElementValueIntegerType();
			BigInteger db_chunkSize = getBigInteger(PropertyNameConstants.CHUNK_SIZE.value());
			chunkSize.setValue(db_chunkSize);
			jdbcUpdate.setBatchSize(chunkSize);
		}
		
		if(StringUtils.isNotBlank((String) properties.get(PropertyNameConstants.USER_NAME.value()))){
			ElementValueStringType userName = new ElementValueStringType();
			userName.setValue(String.valueOf(properties.get(PropertyNameConstants.USER_NAME.value())));
			jdbcUpdate.setUserName(userName);
		}
		
		if(StringUtils.isNotBlank((String) properties.get(PropertyNameConstants.PASSWORD.value()))){
			ElementValueStringType password = new ElementValueStringType();
			password.setValue(String.valueOf(properties.get(PropertyNameConstants.PASSWORD.value())));
			jdbcUpdate.setPassword(password);
		}
		
		
		if(StringUtils.isNotBlank((String) properties.get(PropertyNameConstants.SELECT_BY_KEYS.value()))){
			String str = (String) properties.get(PropertyNameConstants.SELECT_BY_KEYS.value());
			TypeUpdateKeys updateKeys = new TypeUpdateKeys();
			String[] updateKeyColumnsFeilds = StringUtils.split(str, Constants.LOAD_TYPE_NEW_TABLE_VALUE_SEPERATOR);
			if(updateKeyColumnsFeilds !=null && updateKeyColumnsFeilds.length>0){
				TypeKeyFields updateTypeKeyFields = new TypeKeyFields();
				updateKeys.setUpdateByKeys(updateTypeKeyFields);
				for(String fieldValue : updateKeyColumnsFeilds){
					TypeFieldName updateTypeFieldName = new TypeFieldName();
					updateTypeFieldName.setName(fieldValue);
					updateTypeKeyFields.getField().add(updateTypeFieldName);
				}
				jdbcUpdate.setUpdate(updateKeys);
			}
		}
		
		
		
	}
	

	@Override
	protected List<TypeOutputInSocket> getOutInSocket() {
		logger.debug("Generating TypeOutputInSocket data");
		List<TypeOutputInSocket> outputinSockets = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeOutputJdbcupdateOutSocket outInSocket = new TypeOutputJdbcupdateOutSocket();
			outInSocket.setId(link.getTargetTerminal());
			outInSocket.setFromSocketId(converterHelper.getFromSocketId(link));
			outInSocket.setFromSocketType(link.getSource().getPorts().get(link.getSourceTerminal()).getPortType());
			outInSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());
			outInSocket.setSchema(getSchema());
			outInSocket.getOtherAttributes();
			outInSocket.setFromComponentId(link.getSource().getComponentId());
			outputinSockets.add(outInSocket);
		}
		return outputinSockets;
	}

	@Override
	protected List<TypeBaseField> getFieldOrRecord(List<GridRow> gridRowList) {
		logger.debug("Generating data for {} for property {}",
				new Object[] { properties.get(Constants.PARAM_NAME), PropertyNameConstants.SCHEMA.value() });
		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if (gridRowList != null && gridRowList.size() != 0) {
			for (GridRow object : gridRowList)
				typeBaseFields.add(converterHelper.getSchemaGridTargetData(object));
		}
		return typeBaseFields;
	}
}
