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
import hydrograph.engine.assembly.entity.InputRDBMSEntity;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.InputMysqlAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.inputtypes.Mysql;


public class InputMysqlAssemblyGenerator extends
InputAssemblyGeneratorBase {

	private Mysql inputMysqlJaxb;
	private InputRDBMSEntity inputRDBMSEntity;
	private InputMysqlAssembly inputMysqlAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(InputMysqlAssemblyGenerator.class);

	public InputMysqlAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSourceTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		inputMysqlJaxb = (Mysql) baseComponent;
	}

	@Override
	public void createEntity() {
		inputRDBMSEntity = new InputRDBMSEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file MySql component: "
				+ inputMysqlJaxb.getId());

		inputRDBMSEntity.setComponentId(inputMysqlJaxb.getId());

		inputRDBMSEntity
		.setFieldsList(InputEntityUtils
				.extractInputFields(inputMysqlJaxb.getOutSocket()
						.get(0).getSchema()
						.getFieldOrRecordOrIncludeExternalSchema()));
		inputRDBMSEntity.setOutSocketList(InputEntityUtils
				.extractOutSocket(inputMysqlJaxb.getOutSocket()));
		inputRDBMSEntity.setDatabaseName(inputMysqlJaxb.getDatabaseName().getValue());
		inputRDBMSEntity.setTableName(inputMysqlJaxb.getTableName().getValue());
		inputRDBMSEntity.setRuntimeProperties(InputEntityUtils
				.extractRuntimeProperties(inputMysqlJaxb
						.getRuntimeProperties()));
		inputRDBMSEntity.setBatch(inputMysqlJaxb.getBatch());
		inputRDBMSEntity.setQuery(inputMysqlJaxb.getQuery() ==null?null:inputMysqlJaxb.getQuery().getValue());
		inputRDBMSEntity.setUsername(inputMysqlJaxb.getUsername().getValue());
		inputRDBMSEntity.setPassword(inputMysqlJaxb.getPassword().getValue());
		inputRDBMSEntity.setJdbcurl(inputMysqlJaxb.getJdbcurl().getValue());
		inputRDBMSEntity.setDatabaseType("Mysql");
		inputRDBMSEntity.setBatchSize(inputMysqlJaxb.getBatchSize().getValue().intValue());
		inputRDBMSEntity.setCondition(inputMysqlJaxb.getCondition()==null?null:inputMysqlJaxb.getCondition().getValue());
	
		//inputRDBMSEntity.setColumnDefs(inputRDBMS.getOutSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema().get== null?null:inputRDBMS.getPrimaryKeys().getField());
	}


	
	@Override
	public BaseComponent getAssembly() {
		return inputMysqlAssembly;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		inputMysqlAssembly = new InputMysqlAssembly(
				inputRDBMSEntity, componentParameters);
	}
}