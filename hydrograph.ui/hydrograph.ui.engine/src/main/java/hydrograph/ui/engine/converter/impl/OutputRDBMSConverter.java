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
import hydrograph.engine.jaxb.commontypes.TypeFieldName;
import hydrograph.engine.jaxb.commontypes.TypeKeyFields;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;
import hydrograph.engine.jaxb.omysql.DatabaseType;
import hydrograph.engine.jaxb.omysql.TypeLoadChoice;
import hydrograph.engine.jaxb.omysql.TypeOutputMysqlOutSocket;
import hydrograph.engine.jaxb.omysql.TypePriamryKeys;
import hydrograph.engine.jaxb.omysql.TypeUpdateKeys;
import hydrograph.engine.jaxb.outputtypes.Mysql;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.BasicSchemaGridRow;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.SQLLoadTypeProperty;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.converter.OutputConverter;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
/**
 * 
 * Converter for OutputRDBMS type component.
 *
 * @author Bitwise
 */
public class OutputRDBMSConverter extends OutputConverter{
	private static final String INSERT = "insert";
	private static final String UPDATE = "update";
	private static final String NEW_TABLE = "newTable";
	private static final Logger logger = LogFactory.INSTANCE.getLogger(OutputRDBMSConverter.class);

	public OutputRDBMSConverter(Component component) {
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
		DatabaseType databaseType= new DatabaseType();
		databaseType.setValue(converterHelper.getOutputDatabaseTypeValue(PropertyNameConstants.DATABASE_TYPE.value()));
		mysql.setDatabaseName(converterHelper.getString(PropertyNameConstants.DATABASE_NAME.value()));
		mysql.setTableName(converterHelper.getString(PropertyNameConstants.TABLE_NAME.value()));
		mysql.setUsername(converterHelper.getString(PropertyNameConstants.USER_NAME.value()));
		mysql.setPassword(converterHelper.getString(PropertyNameConstants.PASSWORD.value()));
		mysql.setJdbcurl(converterHelper.getString(PropertyNameConstants.JDBC_URL.value()));
		mysql.setRuntimeProperties(getRuntimeProperties());
		mysql.setBatchSize(converterHelper.getInteger(PropertyNameConstants.BATCH_SIZE.value()));
		mysql.setLoadType(getLoadType());			 	
	}
	

	@Override
	protected List<TypeOutputInSocket> getOutInSocket() {
		logger.debug("Generating TypeOutputInSocket data");
		List<TypeOutputInSocket> outputinSockets = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			TypeOutputMysqlOutSocket outInSocket = new TypeOutputMysqlOutSocket();
			outInSocket.setId(link.getTargetTerminal());
			outInSocket.setFromSocketId(converterHelper.getFromSocketId(link));
			outInSocket.setType(link.getTarget().getPort(link.getTargetTerminal()).getPortType());
			outInSocket.setSchema(getSchema());
			outInSocket.setFromComponentId((String) link.getSource().getProperties().get(Constants.PARAM_NAME));
			outputinSockets.add(outInSocket);
		}
		return outputinSockets;
	}
	
	@Override
	protected List<TypeBaseField> getFieldOrRecord(List<GridRow> gridList) {
		logger.debug("Generating data for {} for property {}", new Object[] { properties.get(Constants.PARAM_NAME),
				PropertyNameConstants.SCHEMA.value() });

		List<TypeBaseField> typeBaseFields = new ArrayList<>();
		if (gridList != null && gridList.size() != 0) {
			for (GridRow object : gridList) {
				typeBaseFields.add(converterHelper.getSQLTargetData((BasicSchemaGridRow)object));
			}
		}
		return typeBaseFields;
	
	}
	
	
	private TypeLoadChoice getLoadType() {
		
		SQLLoadTypeProperty sqlLoadTypeProperty =  (SQLLoadTypeProperty) properties.get("loadtype_properties");
		TypeLoadChoice typeLoadChoice = new TypeLoadChoice();
		if(sqlLoadTypeProperty != null && StringUtils.isNotBlank(sqlLoadTypeProperty.getLoadType())){
			if(StringUtils.equals(NEW_TABLE,sqlLoadTypeProperty.getLoadType())){
				TypePriamryKeys primaryKeys = new TypePriamryKeys(); 
				List<String> keys = new ArrayList<String>();
				if(!StringUtils.equals(sqlLoadTypeProperty.getPrimaryKeys(), "")){
					keys=Arrays.asList(sqlLoadTypeProperty.getPrimaryKeys().split(","));
					TypeKeyFields typeKeyFields = new TypeKeyFields();
	
					for (String fieldName:keys){
						TypeFieldName fieldname = new TypeFieldName();
						fieldname.setName(fieldName);
						typeKeyFields.getField().add(fieldname);
					}
	
					primaryKeys.setPrimaryKeys(typeKeyFields);
				}
				typeLoadChoice.setNewTable(primaryKeys);
			}else if(StringUtils.equals(UPDATE,sqlLoadTypeProperty.getLoadType())){
				TypeUpdateKeys updateKeys = new TypeUpdateKeys();
				List<String> keys = new ArrayList<String>();
				if(!StringUtils.equals(sqlLoadTypeProperty.getUpdateByKeys(), "")){
					keys=Arrays.asList(sqlLoadTypeProperty.getUpdateByKeys().split(","));
					TypeKeyFields typeKeyFields = new TypeKeyFields();
	
					for (String fieldName:keys){
						TypeFieldName fieldname = new TypeFieldName();
						fieldname.setName(fieldName);
						typeKeyFields.getField().add(fieldname);
					}
	
					updateKeys.setUpdateByKeys(typeKeyFields);
					typeLoadChoice.setUpdate(updateKeys);
				}else{
					//throw new Exception("Update keys need to be specified.");
				}
			}else if(StringUtils.equals(INSERT,sqlLoadTypeProperty.getLoadType())){
				typeLoadChoice.setInsert("");
			}
		}
		return typeLoadChoice;
	}
}

