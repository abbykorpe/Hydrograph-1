/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.engine.cascading.assembly.generator;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.Tap;
import hydrograph.engine.assembly.entity.OutputRDBMSEntity;
import hydrograph.engine.assembly.entity.utils.OutputEntityUtils;
import hydrograph.engine.cascading.assembly.generator.base.OutputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.OutputMysqlAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.outputtypes.Mysql;

public class OutputMysqlAssemblyGenerator extends OutputAssemblyGeneratorBase {

	private Mysql jaxbOutputMysql;
	private OutputRDBMSEntity outputRDBMSEntity;
	private OutputMysqlAssembly outputMysqlAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(OutputMysqlAssemblyGenerator.class);

	public OutputMysqlAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSinkTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbOutputMysql = (Mysql) baseComponent;
	}

	@Override
	public void createEntity() {
		outputRDBMSEntity = new OutputRDBMSEntity();
	}

	
	
	
	
	
	
	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file RDBMS component: "
				+ jaxbOutputMysql.getId());

		outputRDBMSEntity.setComponentId(jaxbOutputMysql.getId());

		outputRDBMSEntity
				.setFieldsList(OutputEntityUtils.extractOutputFields(jaxbOutputMysql
						.getInSocket().get(0).getSchema()
						.getFieldOrRecordOrIncludeExternalSchema()));
		outputRDBMSEntity.setDatabaseName(jaxbOutputMysql.getDatabaseName().getValue());
		outputRDBMSEntity.setTableName(jaxbOutputMysql.getTableName().getValue());
		outputRDBMSEntity.setRuntimeProperties(OutputEntityUtils
				.extractRuntimeProperties(jaxbOutputMysql.getRuntimeProperties()));
		outputRDBMSEntity.setBatch(jaxbOutputMysql.getBatch());
		outputRDBMSEntity.setUsername(jaxbOutputMysql.getUsername().getValue());
		outputRDBMSEntity.setPassword(jaxbOutputMysql.getPassword().getValue());
		outputRDBMSEntity.setJdbcurl(jaxbOutputMysql.getJdbcurl().getValue());
		outputRDBMSEntity.setDatabaseType("Mysql");
		outputRDBMSEntity.setBatchSize(jaxbOutputMysql.getBatchSize().getValue().intValue());
		
		if (jaxbOutputMysql.getLoadType().getNewTable() != null) 
			outputRDBMSEntity.setLoadType("newTable");
		else if(jaxbOutputMysql.getLoadType().getTruncateLoad() != null) 
			outputRDBMSEntity.setLoadType("truncateLoad");
		else if (jaxbOutputMysql.getLoadType().getInsert() != null) 
			outputRDBMSEntity.setLoadType("insert");
		else 
			outputRDBMSEntity.setLoadType("update");
		

		if("newTable".equals(outputRDBMSEntity.getLoadType()))
		outputRDBMSEntity
				.setPrimaryKeys(jaxbOutputMysql.getLoadType().getNewTable().getPrimaryKeys() == null ? null
						: jaxbOutputMysql.getLoadType().getNewTable().getPrimaryKeys().getField());
		if(outputRDBMSEntity.getLoadType().equals("update"))
			outputRDBMSEntity
			.setUpdateByKeys(jaxbOutputMysql.getLoadType().getUpdate().getUpdateByKeys().getField());
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		outputMysqlAssembly = new OutputMysqlAssembly(
				outputRDBMSEntity, componentParameters);
	}

	@Override
	public BaseComponent getAssembly() {
		return outputMysqlAssembly;
	}
}