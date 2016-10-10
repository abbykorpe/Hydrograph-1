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
import hydrograph.engine.cascading.assembly.InputOracleAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.inputtypes.Oracle;


public class InputOracleAssemblyGenerator extends
InputAssemblyGeneratorBase {

	private Oracle inputOracleJaxb;
	private InputRDBMSEntity inputRDBMSEntity;
	private InputOracleAssembly inputOracleAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(InputOracleAssemblyGenerator.class);

	public InputOracleAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSourceTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		inputOracleJaxb = (Oracle) baseComponent;
	}

	@Override
	public void createEntity() {
		inputRDBMSEntity = new InputRDBMSEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file Oracle component: "
				+ inputOracleJaxb.getId());

		inputRDBMSEntity.setComponentId(inputOracleJaxb.getId());

		inputRDBMSEntity
		.setFieldsList(InputEntityUtils
				.extractInputFields(inputOracleJaxb.getOutSocket()
						.get(0).getSchema()
						.getFieldOrRecordOrIncludeExternalSchema()));
		inputRDBMSEntity.setOutSocketList(InputEntityUtils
				.extractOutSocket(inputOracleJaxb.getOutSocket()));
//		inputRDBMSEntity.setDatabaseName(inputOracleJaxb.getDatabaseName()==null?null:inputOracleJaxb.getDatabaseName().getValue());
		inputRDBMSEntity.setTableName(inputOracleJaxb.getTableName().getValue());
		inputRDBMSEntity.setRuntimeProperties(InputEntityUtils
				.extractRuntimeProperties(inputOracleJaxb
						.getRuntimeProperties()));
		inputRDBMSEntity.setBatch(inputOracleJaxb.getBatch());
		inputRDBMSEntity.setQuery(inputOracleJaxb.getQuery() ==null?null:inputOracleJaxb.getQuery().getValue());
		inputRDBMSEntity.setUsername(inputOracleJaxb.getUsername().getValue());
		inputRDBMSEntity.setPassword(inputOracleJaxb.getPassword().getValue());
		inputRDBMSEntity.setJdbcurl(inputOracleJaxb.getJdbcurl().getValue());
		inputRDBMSEntity.setBatchSize(inputOracleJaxb.getBatchSize().getValue().intValue());
		inputRDBMSEntity.setCondition(inputOracleJaxb.getCondition()==null?null:inputOracleJaxb.getCondition().getValue());
	
		//inputRDBMSEntity.setColumnDefs(inputRDBMS.getOutSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema().get== null?null:inputRDBMS.getPrimaryKeys().getField());
	}


	
	@Override
	public BaseComponent getAssembly() {
		return inputOracleAssembly;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		inputOracleAssembly = new InputOracleAssembly(
				inputRDBMSEntity, componentParameters);
	}
}