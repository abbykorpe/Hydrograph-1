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
import hydrograph.engine.cascading.assembly.OutputOracleAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.outputtypes.Oracle;

public class OutputOracleAssemblyGenerator extends OutputAssemblyGeneratorBase {

	private Oracle jaxbOutputOracle;
	private OutputRDBMSEntity outputRDBMSEntity;
	private OutputOracleAssembly outputOracleAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(OutputOracleAssemblyGenerator.class);

	public OutputOracleAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSinkTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbOutputOracle = (Oracle) baseComponent;
	}

	@Override
	public void createEntity() {
		outputRDBMSEntity = new OutputRDBMSEntity();
	}

	
	
	
	
	
	
	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file RDBMS component: "
				+ jaxbOutputOracle.getId());

		outputRDBMSEntity.setComponentId(jaxbOutputOracle.getId());

		outputRDBMSEntity
				.setFieldsList(OutputEntityUtils.extractOutputFields(jaxbOutputOracle
						.getInSocket().get(0).getSchema()
						.getFieldOrRecordOrIncludeExternalSchema()));
//		outputRDBMSEntity.setDatabaseName(jaxbOutputOracle.getDatabaseName().getValue());
		outputRDBMSEntity.setTableName(jaxbOutputOracle.getTableName().getValue());
		outputRDBMSEntity.setDatabaseType("Oracle");
		outputRDBMSEntity.setRuntimeProperties(OutputEntityUtils
				.extractRuntimeProperties(jaxbOutputOracle.getRuntimeProperties()));
		outputRDBMSEntity.setPhase(jaxbOutputOracle.getPhase());
		outputRDBMSEntity.setUsername(jaxbOutputOracle.getUsername().getValue());
		outputRDBMSEntity.setPassword(jaxbOutputOracle.getPassword().getValue());
		outputRDBMSEntity.setJdbcurl(jaxbOutputOracle.getJdbcurl().getValue());
		outputRDBMSEntity.setBatchSize(jaxbOutputOracle.getBatchSize().getValue().intValue());
		if (jaxbOutputOracle.getLoadType().getNewTable() != null) 
			outputRDBMSEntity.setLoadType("newTable");
		else if(jaxbOutputOracle.getLoadType().getTruncateLoad() != null) 
			outputRDBMSEntity.setLoadType("truncateLoad");
		else if (jaxbOutputOracle.getLoadType().getInsert() != null) 
			outputRDBMSEntity.setLoadType("insert");
		else 
			outputRDBMSEntity.setLoadType("update");
		

		if("newTable".equals(outputRDBMSEntity.getLoadType()))
		outputRDBMSEntity
				.setPrimaryKeys(jaxbOutputOracle.getLoadType().getNewTable().getPrimaryKeys() == null ? null
						: jaxbOutputOracle.getLoadType().getNewTable().getPrimaryKeys().getField());
		if(outputRDBMSEntity.getLoadType().equals("update"))
			outputRDBMSEntity
			.setUpdateByKeys(jaxbOutputOracle.getLoadType().getUpdate().getUpdateByKeys().getField());
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		outputOracleAssembly = new OutputOracleAssembly(
				outputRDBMSEntity, componentParameters);
	}

	@Override
	public BaseComponent getAssembly() {
		return outputOracleAssembly;
	}
}
	

	
