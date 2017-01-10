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

import hydrograph.engine.core.component.entity.InputRDBMSEntity;
import hydrograph.engine.core.component.entity.utils.InputEntityUtils;
import hydrograph.engine.core.component.generator.base.InputComponentGeneratorBase;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.inputtypes.Redshift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InputRedshiftEntityGenerator extends
InputComponentGeneratorBase {

	private Redshift inputRedshiftJaxb;
	private InputRDBMSEntity inputRDBMSEntity;
	private static Logger LOG = LoggerFactory
			.getLogger(InputRedshiftEntityGenerator.class);

	public InputRedshiftEntityGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		inputRedshiftJaxb = (Redshift) baseComponent;
	}

	@Override
	public void createEntity() {
		inputRDBMSEntity = new InputRDBMSEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file Redshift component: "
				+ inputRedshiftJaxb.getId());

		inputRDBMSEntity.setComponentId(inputRedshiftJaxb.getId());

		inputRDBMSEntity
		.setFieldsList(InputEntityUtils
				.extractInputFields(inputRedshiftJaxb.getOutSocket()
						.get(0).getSchema()
						.getFieldOrRecordOrIncludeExternalSchema()));
		inputRDBMSEntity.setOutSocketList(InputEntityUtils
				.extractOutSocket(inputRedshiftJaxb.getOutSocket()));
		inputRDBMSEntity.setDatabaseName(inputRedshiftJaxb.getDatabaseName().getValue());
		inputRDBMSEntity.setTableName(inputRedshiftJaxb.getTableName().getValue());
		inputRDBMSEntity.setRuntimeProperties(InputEntityUtils
				.extractRuntimeProperties(inputRedshiftJaxb
						.getRuntimeProperties()));
		inputRDBMSEntity.setBatch(inputRedshiftJaxb.getBatch());
//		inputRDBMSEntity.setQuery(inputRedshiftJaxb.getQuery() ==null?null:inputRedshiftJaxb.getQuery().getValue());
		inputRDBMSEntity.setUsername(inputRedshiftJaxb.getUsername().getValue());
		inputRDBMSEntity.setPassword(inputRedshiftJaxb.getPassword().getValue());
//		inputRDBMSEntity.setJdbcurl(inputRedshiftJaxb.getJdbcurl().getValue());
//		inputRDBMSEntity.setBatchSize(inputRedshiftJaxb.getBatchSize().getValue().intValue());
//		inputRDBMSEntity.setCondition(inputRedshiftJaxb.getCondition().getValue());
		
	
		//inputRDBMSEntity.setColumnDefs(inputRDBMS.getOutSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema().get== null?null:inputRDBMS.getPrimaryKeys().getField());
	}



	@Override
	public InputRDBMSEntity getEntity() {
		return inputRDBMSEntity;
	}

	
	
}