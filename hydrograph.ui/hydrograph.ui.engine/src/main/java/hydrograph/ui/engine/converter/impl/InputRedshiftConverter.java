package hydrograph.ui.engine.converter.impl;

import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.engine.jaxb.inputtypes.Redshift;
import hydrograph.engine.jaxb.iredshift.TypeInputRedshiftOutSocket;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.InputConverter;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

public class InputRedshiftConverter extends InputConverter{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputRedshiftConverter.class);

	public InputRedshiftConverter(Component component) {
		super(component);
		this.baseComponent = new Redshift();
		this.component = component;
		this.properties = component.getProperties();
	}
	
	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Redshift redshift = (Redshift) baseComponent;
		redshift.setDatabaseName(converterHelper.getString(PropertyNameConstants.DATABASE_NAME.value()));
		redshift.setTableName(converterHelper.getString(PropertyNameConstants.TABLE_NAME.value()));
		redshift.setUsername(converterHelper.getString(PropertyNameConstants.USER_NAME.value()));
		redshift.setPassword(converterHelper.getString(PropertyNameConstants.PASSWORD.value()));
		redshift.setQuery(converterHelper.getStringTypeValue());
		redshift.setJdbcurl(converterHelper.getString(PropertyNameConstants.JDBC_URL.value()));
		redshift.setRuntimeProperties(getRuntimeProperties());
		redshift.setBatchSize(converterHelper.getInteger(PropertyNameConstants.BATCH_SIZE.value()));
	}

	@Override
	protected List<TypeInputOutSocket> getInOutSocket() {
		logger.debug("Generating TypeInputOutSocket data for {}", properties.get(Constants.PARAM_NAME));
		List<TypeInputOutSocket> outSockets = new ArrayList<>();
		for (Link link : component.getSourceConnections()) {
			TypeInputRedshiftOutSocket outSocket = new TypeInputRedshiftOutSocket();
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
