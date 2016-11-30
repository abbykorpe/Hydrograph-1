package hydrograph.ui.engine.converter.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.ElementValueIntegerType;
import hydrograph.engine.jaxb.commontypes.ElementValueStringType;
import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.engine.jaxb.inputtypes.Oracle;
import hydrograph.engine.jaxb.ioracle.TypeInputOracleOutSocket;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.DatabaseSelectionConfig;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.InputConverter;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

public class InputOracleConverter extends InputConverter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputOracleConverter.class);
	private Oracle oracleInput;

	public InputOracleConverter(Component component) {
		super(component);
		this.baseComponent = new Oracle();
		this.component = component;
		this.properties = component.getProperties();
	}

	@Override
	protected List<TypeInputOutSocket> getInOutSocket() {
		logger.debug("Generating TypeInputOutSocket data for {}", properties.get(Constants.PARAM_NAME));
		List<TypeInputOutSocket> outSockets = new ArrayList<>();
		for (Link link : component.getSourceConnections()) {
			TypeInputOracleOutSocket outSocket = new TypeInputOracleOutSocket();
			outSocket.setId(link.getSourceTerminal());
			outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
			outSocket.setSchema(getSchema());
			outSocket.getOtherAttributes();
			outSockets.add(outSocket);
		}
		return outSockets;
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		oracleInput = (Oracle) baseComponent;
		oracleInput.setRuntimeProperties(getRuntimeProperties());

		ElementValueStringType sid = new ElementValueStringType();
		sid.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_SID.value())));
		oracleInput.setSid(sid);

		ElementValueStringType hostName = new ElementValueStringType();
		hostName.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_HOST_NAME.value())));
		oracleInput.setHostname(hostName);

		ElementValueIntegerType portNo = new ElementValueIntegerType();
		BigInteger portValue = new BigInteger(String.valueOf(properties.get(PropertyNameConstants.ORACLE_PORT_NO.value())));
		portNo.setValue(portValue);
		oracleInput.setPort(portNo);

		ElementValueStringType jdbcDriver = new ElementValueStringType();
		jdbcDriver.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_JDBC_DRIVER.value())));
		oracleInput.setDrivertype(jdbcDriver);
		
		ElementValueStringType oracleSchema = new ElementValueStringType();
		oracleSchema.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_SCHEMA.value())));
		oracleInput.setSchemaname(oracleSchema);
		
		ElementValueStringType userName = new ElementValueStringType();
		userName.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_USER_NAME.value())));
		oracleInput.setUsername(userName);
		
		ElementValueStringType password = new ElementValueStringType();
		password.setValue(String.valueOf(properties.get(PropertyNameConstants.ORACLE_PASSWORD.value())));
		oracleInput.setPassword(password);

		DatabaseSelectionConfig databaseSelectionConfig = (DatabaseSelectionConfig) properties
				.get(PropertyNameConstants.ORACLE_SELECT_OPTION.value());

		if (databaseSelectionConfig != null) {

			if (databaseSelectionConfig.isTableName()) {
				ElementValueStringType tableName = new ElementValueStringType();
				tableName.setValue(databaseSelectionConfig.getTableName());
				oracleInput.setTableName(tableName);
				
			} else {
				ElementValueStringType sqlQuery = new ElementValueStringType();
				if(databaseSelectionConfig.getSqlQuery() !=null && StringUtils.isNotBlank(databaseSelectionConfig.getSqlQuery())){
				sqlQuery.setValue(databaseSelectionConfig.getSqlQuery());
				oracleInput.setSelectQuery(sqlQuery);
				}

				ElementValueStringType sqlQueryCounter = new ElementValueStringType();
				if(databaseSelectionConfig.getSqlQueryCounter() !=null && StringUtils.isNotBlank(databaseSelectionConfig.getSqlQueryCounter())){
				sqlQueryCounter.setValue(databaseSelectionConfig.getSqlQueryCounter());
				oracleInput.setCountQuery(sqlQueryCounter);
				}
			}
		}

	}

	@Override
	protected List<TypeBaseField> getFieldOrRecord(List<GridRow> list) {
		logger.debug("Generating data for {} for property {}",
				new Object[] { properties.get(Constants.PARAM_NAME), PropertyNameConstants.SCHEMA.value() });

		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if (list != null && list.size() != 0) {
			for (GridRow object : list) {
				typeBaseFields.add(converterHelper.getSchemaGridTargetData(object));
			}
		}
		return typeBaseFields;
	}

}
