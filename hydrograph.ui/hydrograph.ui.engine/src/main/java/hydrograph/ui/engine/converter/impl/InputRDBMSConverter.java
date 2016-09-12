package hydrograph.ui.engine.converter.impl;

import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.engine.jaxb.inputtypes.Rdbms;
import hydrograph.engine.jaxb.irdbms.DatabaseType;
import hydrograph.engine.jaxb.irdbms.TypeInputRdbmsOutSocket;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.QueryProperty;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.InputConverter;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public class InputRDBMSConverter extends InputConverter{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputRDBMSConverter.class);

	public InputRDBMSConverter(Component component) {
		super(component);
		this.baseComponent = new Rdbms();
		this.component = component;
		this.properties = component.getProperties();
	}
	
	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Rdbms rdbms = (Rdbms) baseComponent;
		DatabaseType databaseType= new DatabaseType();
		databaseType.setValue(converterHelper.getInputDatabaseTypeValue(PropertyNameConstants.DATABASE_TYPE.value()));
		rdbms.setDatabaseType(databaseType);
		rdbms.setDatabaseName(converterHelper.getString(PropertyNameConstants.DATABASE_NAME.value()));
		rdbms.setTableName(converterHelper.getString(PropertyNameConstants.TABLE_NAME.value()));
		rdbms.setUsername(converterHelper.getString(PropertyNameConstants.USER_NAME.value()));
		rdbms.setPassword(converterHelper.getString(PropertyNameConstants.PASSWORD.value()));
		if(!StringUtils.equals("", getQuery())){
			rdbms.setQuery(converterHelper.getStringTypeValue(getQuery()));
		}
		rdbms.setJdbcurl(converterHelper.getString(PropertyNameConstants.JDBC_URL.value()));
		rdbms.setRuntimeProperties(getRuntimeProperties());
		rdbms.setBatchSize(converterHelper.getInteger(PropertyNameConstants.BATCH_SIZE.value()));
		
			 	
	}
	
	

	private String getQuery() {
		String query = ((QueryProperty) properties.get(PropertyNameConstants.QUERY.value())).getQueryText();
		return query;
	}

	@Override
	protected List<TypeInputOutSocket> getInOutSocket() {
		logger.debug("Generating TypeInputOutSocket data for {}", properties.get(Constants.PARAM_NAME));
		List<TypeInputOutSocket> outSockets = new ArrayList<>();
		for (Link link : component.getSourceConnections()) {
			TypeInputRdbmsOutSocket outSocket = new TypeInputRdbmsOutSocket();
			outSocket.setId(link.getSourceTerminal());
			outSocket.setType(link.getSource().getPort(link.getSourceTerminal()).getPortType());
			outSocket.setSchema(getSchema());
			outSockets.add(outSocket);
		}
		return outSockets;
	}

	@Override
	protected List<TypeBaseField> getFieldOrRecord(List<GridRow> gridList) {
		logger.debug("Generating data for {} for property {}", new Object[] { properties.get(Constants.PARAM_NAME),
				PropertyNameConstants.SCHEMA.value() });

		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if (gridList != null && gridList.size() != 0) {
			for (GridRow object : gridList) {
				typeBaseFields.add(converterHelper.getSchemaGridTargetData(object));
			}
		}
		return typeBaseFields;
	}
}
