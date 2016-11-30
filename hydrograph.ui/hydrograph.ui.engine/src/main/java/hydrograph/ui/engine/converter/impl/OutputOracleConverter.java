package hydrograph.ui.engine.converter.impl;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.ElementValueIntegerType;
import hydrograph.engine.jaxb.commontypes.ElementValueStringType;
import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;
import hydrograph.engine.jaxb.commontypes.TypeKeyFields;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;
import hydrograph.engine.jaxb.ooracle.TypeLoadChoice;
import hydrograph.engine.jaxb.ooracle.TypeOutputOracleInSocket;
import hydrograph.engine.jaxb.ooracle.TypePriamryKeys;
import hydrograph.engine.jaxb.ooracle.TypeUpdateKeys;
import hydrograph.engine.jaxb.outputtypes.Oracle;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.OutputConverter;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

public class OutputOracleConverter extends OutputConverter {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(OutputOracleConverter.class);
	private Oracle oracleOutput;


	public OutputOracleConverter(Component component) {
		super(component);
		this.component = component;
		this.properties = component.getProperties();
		this.baseComponent = new Oracle();
	}
	
	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		oracleOutput = (Oracle) baseComponent;
		oracleOutput.setRuntimeProperties(getRuntimeProperties());
		
		ElementValueStringType sid = new ElementValueStringType();
		sid.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_SID.value())));
		oracleOutput.setSid(sid);
		
		ElementValueStringType tableName = new ElementValueStringType();
		tableName.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_TABLE_NAME.value())));
		oracleOutput.setTableName(tableName);
		
		ElementValueStringType hostName = new ElementValueStringType();
		hostName.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_HOST_NAME.value())));
		oracleOutput.setHostname(hostName);
		
		ElementValueIntegerType portNo = new ElementValueIntegerType();
		BigInteger portValue = new BigInteger(String.valueOf(properties.get(PropertyNameConstants.ORACLE_PORT_NO.value())));
		portNo.setValue(portValue);
		oracleOutput.setPort(portNo);
		
		ElementValueStringType jdbcDriver = new ElementValueStringType();
		jdbcDriver.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_JDBC_DRIVER.value())));
		oracleOutput.setDrivertype(jdbcDriver);
		
		ElementValueStringType oracleSchema = new ElementValueStringType();
		oracleSchema.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_SCHEMA.value())));
		oracleOutput.setSchemaname(oracleSchema);
		
		ElementValueStringType userName = new ElementValueStringType();
		userName.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_USER_NAME.value())));
		oracleOutput.setUsername(userName);
		
		ElementValueStringType password = new ElementValueStringType();
		password.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_PASSWORD.value())));
		oracleOutput.setPassword(password);
		
		ElementValueIntegerType chunkSize =  new ElementValueIntegerType();
		BigInteger chunkValue = new BigInteger(String.valueOf(properties.get(PropertyNameConstants.ORACLE_CHUNK_SIZE.value())));
		chunkSize.setValue(chunkValue);
		oracleOutput.setChunkSize(chunkSize);
		
		addTypeLoadChoice();
		
	}
	
	private void addTypeLoadChoice() {
		TypeLoadChoice loadValue = new TypeLoadChoice();
		Map<String, String> uiValue = (Map<String, String>) properties.get(PropertyNameConstants.ORACLE_LOAD_TYPE.value());
		if (uiValue.containsKey(Constants.LOAD_TYPE_UPDATE_KEY)) {
			loadValue.setUpdate(getUpdateKeys((String) uiValue.get(Constants.LOAD_TYPE_UPDATE_KEY)));
		} else if (uiValue.containsKey(Constants.LOAD_TYPE_NEW_TABLE_KEY)) {
			loadValue.setNewTable(getPrimaryKeys((String) uiValue.get(Constants.LOAD_TYPE_NEW_TABLE_KEY)));
		} else if (uiValue.containsKey(Constants.LOAD_TYPE_INSERT_KEY)) {
			loadValue.setInsert(uiValue.get(Constants.LOAD_TYPE_INSERT_KEY));
		} else if (uiValue.containsKey(Constants.LOAD_TYPE_REPLACE_KEY)) {
			loadValue.setTruncateLoad(uiValue.get(Constants.LOAD_TYPE_REPLACE_KEY));
		}

		oracleOutput.setLoadType(loadValue);
	}

	private TypePriamryKeys getPrimaryKeys(String primaryKeyFeilds) {
		TypePriamryKeys primaryKeys = new TypePriamryKeys();
		String[] primaryKeyColumsFeilds = StringUtils.split(primaryKeyFeilds, Constants.LOAD_TYPE_NEW_TABLE_VALUE_SEPERATOR);
		if(primaryKeyColumsFeilds !=null && primaryKeyColumsFeilds.length>0){
			TypeKeyFields primaryTypeKeyFields = new TypeKeyFields();
			primaryKeys.setPrimaryKeys(primaryTypeKeyFields);
			for(String fieldValue : primaryKeyColumsFeilds){
				TypeFieldName primaryTypeFieldName = new TypeFieldName();
				primaryTypeFieldName.setName(fieldValue);
				primaryTypeKeyFields.getField().add(primaryTypeFieldName);
			}
		}
				
		return primaryKeys;
	}

	private TypeUpdateKeys getUpdateKeys(String fields) {
		TypeUpdateKeys updateKeys = null;
		String[] columnFields = StringUtils.split(fields, Constants.LOAD_TYPE_UPDATE_VALUE_SEPERATOR);
		if (columnFields != null && columnFields.length > 0) {
			TypeKeyFields typeKeyFields = new TypeKeyFields();
			updateKeys = new TypeUpdateKeys();
			updateKeys.setUpdateByKeys(typeKeyFields);
			for (String field : columnFields) {
				TypeFieldName typeFieldName = new TypeFieldName();
				typeFieldName.setName(field);
				typeKeyFields.getField().add(typeFieldName);
			}
		}
		
		return updateKeys;
	}

	@Override
	protected List<TypeOutputInSocket> getOutInSocket() {
		logger.debug("Generating TypeOutputInSocket data");
		List<TypeOutputInSocket> outputinSockets = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeOutputOracleInSocket outInSocket = new TypeOutputOracleInSocket();
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
	protected List<TypeBaseField> getFieldOrRecord(List<GridRow> list) {
		logger.debug("Generating data for {} for property {}", new Object[] { properties.get(Constants.PARAM_NAME),
				PropertyNameConstants.SCHEMA.value() });

		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if (list != null && list.size() != 0) {
			for (GridRow object : list)
				typeBaseFields.add(converterHelper.getSchemaGridTargetData(object));

		}
		return typeBaseFields;
	}
	

}
