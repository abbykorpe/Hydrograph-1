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

import hydrograph.engine.assembly.entity.InputFileHiveParquetEntity;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.cascading.assembly.InputFileHiveParquetAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.ihiveparquet.HivePartitionFieldsType;
import hydrograph.engine.jaxb.inputtypes.ParquetHiveFile;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.Tap;

/**
 * @author Ganesh
 *
 */

public class InputFileHiveParquetAssemblyGenerator extends InputAssemblyGeneratorBase {

	private ParquetHiveFile jaxbInputFileHiveParquetFile;
	private InputFileHiveParquetEntity inputFileHiveParquetEntity;
	private InputFileHiveParquetAssembly inputFileHiveParquetAssembly;
	private static Logger LOG = LoggerFactory.getLogger(InputFileHiveParquetAssemblyGenerator.class);

	public InputFileHiveParquetAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Tap> getSourceTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbInputFileHiveParquetFile = (ParquetHiveFile) baseComponent;
	}

	@Override
	public void createEntity() {
		inputFileHiveParquetEntity = new InputFileHiveParquetEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file hive parquet entity for component: " + jaxbInputFileHiveParquetFile.getId());
		inputFileHiveParquetEntity.setComponentId(jaxbInputFileHiveParquetFile.getId());
		inputFileHiveParquetEntity.setFieldsList(InputEntityUtils.extractInputFields(jaxbInputFileHiveParquetFile
				.getOutSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema()));
		inputFileHiveParquetEntity.setRuntimeProperties(
				InputEntityUtils.extractRuntimeProperties(jaxbInputFileHiveParquetFile.getRuntimeProperties()));
		inputFileHiveParquetEntity
				.setOutSocketList(InputEntityUtils.extractOutSocket(jaxbInputFileHiveParquetFile.getOutSocket()));
		inputFileHiveParquetEntity.setDatabaseName(jaxbInputFileHiveParquetFile.getDatabaseName().getValue());
		inputFileHiveParquetEntity.setTableName(jaxbInputFileHiveParquetFile.getTableName().getValue());
		inputFileHiveParquetEntity
				.setPartitionKeys(extractPartitionFields(jaxbInputFileHiveParquetFile.getPartitionKeys()));
		inputFileHiveParquetEntity.setExternalTablePathUri(jaxbInputFileHiveParquetFile.getExternalTablePath() == null
				? null : jaxbInputFileHiveParquetFile.getExternalTablePath().getUri());
	}

	/**
	 * This method extracts partition keys from {@link HivePartitionFieldsType}
	 * hivePartitionFieldsType which is passed as a parameter.
	 * 
	 * If hivePartitionFieldsType object is null then string array of size of 0
	 * will be returned.
	 * 
	 * @param hivePartitionFieldsType
	 * @return String[]
	 */
	private String[] extractPartitionFields(HivePartitionFieldsType hivePartitionFieldsType) {
		if (hivePartitionFieldsType != null && hivePartitionFieldsType.getField() != null) {
			String[] partitionKeys = new String[hivePartitionFieldsType.getField().size()];
			for (int i = 0; i < hivePartitionFieldsType.getField().size(); i++) {
				partitionKeys[i] = hivePartitionFieldsType.getField().get(i).getName();
			}
			return partitionKeys;	
		}else{
			return new String[0];
		}
		
		
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		inputFileHiveParquetAssembly = new InputFileHiveParquetAssembly(inputFileHiveParquetEntity,
				componentParameters);
	}

	@Override
	public BaseComponent getAssembly() {
		return inputFileHiveParquetAssembly;
	}
}