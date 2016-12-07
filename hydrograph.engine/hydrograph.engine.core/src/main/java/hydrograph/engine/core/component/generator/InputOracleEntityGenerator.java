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
package hydrograph.engine.core.component.generator;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.core.component.entity.InputRDBMSEntity;
import hydrograph.engine.core.component.entity.base.AssemblyEntityBase;
import hydrograph.engine.core.component.entity.utils.InputEntityUtils;
import hydrograph.engine.core.component.generator.base.InputComponentGeneratorBase;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.inputtypes.Oracle;


public class InputOracleEntityGenerator extends
InputComponentGeneratorBase {

	private Oracle inputOracleJaxb;
	private InputRDBMSEntity inputRDBMSEntity;
	private static Logger LOG = LoggerFactory
			.getLogger(InputOracleEntityGenerator.class);

	public InputOracleEntityGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
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
	public InputRDBMSEntity getEntity() {
		return inputRDBMSEntity;
	}
}