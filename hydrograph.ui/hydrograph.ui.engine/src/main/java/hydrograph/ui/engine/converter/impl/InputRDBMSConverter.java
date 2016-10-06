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
package hydrograph.ui.engine.converter.impl;

import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.engine.jaxb.imysql.TypeInputMysqlOutSocket;
import hydrograph.engine.jaxb.inputtypes.Mysql;
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

/**
 * 
 * Converter for BinaryLogisticRegressionModelConverter type component.
 *
 * @author Bitwise
 */
public class InputRDBMSConverter extends InputConverter{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(InputRDBMSConverter.class);

	public InputRDBMSConverter(Component component) {
		super(component);
		this.baseComponent = new Mysql();
		this.component = component;
		this.properties = component.getProperties();
	}
	
	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for {}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		Mysql mysql = (Mysql) baseComponent;
		mysql.setDatabaseName(converterHelper.getString(PropertyNameConstants.DATABASE_NAME.value()));
		mysql.setTableName(converterHelper.getString(PropertyNameConstants.TABLE_NAME.value()));
		mysql.setUsername(converterHelper.getString(PropertyNameConstants.USER_NAME.value()));
		mysql.setPassword(converterHelper.getString(PropertyNameConstants.PASSWORD.value()));
		if (converterHelper.getStringTypeValue().getValue() != null) {
			mysql.setQuery(converterHelper.getStringTypeValue());
		}
		mysql.setCondition(converterHelper.getString(PropertyNameConstants.MYSQL_CONDITION.value()));
		mysql.setJdbcurl(converterHelper.getString(PropertyNameConstants.JDBC_URL.value()));
		mysql.setRuntimeProperties(getRuntimeProperties());
		mysql.setBatchSize(converterHelper.getInteger(PropertyNameConstants.BATCH_SIZE.value()));
	}


	@Override
	protected List<TypeInputOutSocket> getInOutSocket() {
		logger.debug("Generating TypeInputOutSocket data for {}", properties.get(Constants.PARAM_NAME));
		List<TypeInputOutSocket> outSockets = new ArrayList<>();
		for (Link link : component.getSourceConnections()) {
			TypeInputMysqlOutSocket outSocket = new TypeInputMysqlOutSocket();
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
