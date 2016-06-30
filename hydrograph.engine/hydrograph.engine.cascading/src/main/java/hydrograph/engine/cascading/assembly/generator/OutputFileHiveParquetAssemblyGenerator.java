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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.Tap;
import hydrograph.engine.assembly.entity.OutputFileHiveParquetEntity;
import hydrograph.engine.assembly.entity.base.HiveEntityBase;
import hydrograph.engine.assembly.entity.utils.OutputEntityUtils;
import hydrograph.engine.cascading.assembly.OutputFileHiveParquetAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OutputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TrueFalse;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.ohiveparquet.HivePartitionFieldsType;
import hydrograph.engine.jaxb.ohiveparquet.PartitionFieldBasicType;
import hydrograph.engine.jaxb.outputtypes.ParquetHiveFile;

public class OutputFileHiveParquetAssemblyGenerator extends
		OutputAssemblyGeneratorBase {

	private ParquetHiveFile jaxbHiveParquetFile;
	private OutputFileHiveParquetEntity outputFileHiveParquetEntity;
	private OutputFileHiveParquetAssembly outputFileHiveParquetAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(OutputFileHiveParquetAssemblyGenerator.class);

	public OutputFileHiveParquetAssemblyGenerator(
			TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSinkTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbHiveParquetFile = (ParquetHiveFile) baseComponent;
	}

	@Override
	public void createEntity() {
		outputFileHiveParquetEntity = new OutputFileHiveParquetEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing output file Hive parquet entity for component: "
				+ jaxbHiveParquetFile.getId());
		outputFileHiveParquetEntity.setComponentId(jaxbHiveParquetFile.getId());
		outputFileHiveParquetEntity.setPhase(jaxbHiveParquetFile.getPhase());
		outputFileHiveParquetEntity
				.setFieldsList(OutputEntityUtils
						.extractOutputFields(jaxbHiveParquetFile.getInSocket()
								.get(0).getSchema()
								.getFieldOrRecordOrIncludeExternalSchema()));
		outputFileHiveParquetEntity.setExternalTablePathUri(jaxbHiveParquetFile
				.getExternalTablePath() == null ? null : jaxbHiveParquetFile
				.getExternalTablePath().getUri());
		outputFileHiveParquetEntity.setRuntimeProperties(OutputEntityUtils
				.extractRuntimeProperties(jaxbHiveParquetFile
						.getRuntimeProperties()));

		outputFileHiveParquetEntity.setDatabaseName(jaxbHiveParquetFile
				.getDatabaseName().getValue());
		outputFileHiveParquetEntity.setTableName(jaxbHiveParquetFile
				.getTableName().getValue());
		outputFileHiveParquetEntity
				.setPartitionKeys(extractPartitionFields(jaxbHiveParquetFile
						.getPartitionKeys()));
		outputFileHiveParquetEntity.setOverWrite(!(jaxbHiveParquetFile
				.getOverWrite() != null && (TrueFalse.FALSE)
				.equals(jaxbHiveParquetFile.getOverWrite().getValue())));
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
	private String[] extractPartitionFields(
			HivePartitionFieldsType hivePartitionFieldsType) {
		String[] partitionKeys;
		List<String> partitionFieldsList = new ArrayList<String>();
		if (hivePartitionFieldsType != null
				&& hivePartitionFieldsType.getField() != null) {
			partitionFieldsList = getPartitionFieldsList(
					hivePartitionFieldsType.getField(), partitionFieldsList);
			partitionKeys = partitionFieldsList
					.toArray(new String[partitionFieldsList.size()]);
			return partitionKeys;
		} else {
			return new String[0];
		}

	}

	private List<String> getPartitionFieldsList(
			PartitionFieldBasicType partitionFieldBasicType,
			List<String> partitionFieldsList) {
		partitionFieldsList.add(partitionFieldBasicType.getName());
		if (partitionFieldBasicType.getField() != null) {
			getPartitionFieldsList(partitionFieldBasicType.getField(),
					partitionFieldsList);
		}
		return partitionFieldsList;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		outputFileHiveParquetAssembly = new OutputFileHiveParquetAssembly(
				outputFileHiveParquetEntity, componentParameters);
	}

	@Override
	public BaseComponent<HiveEntityBase> getAssembly() {
		return outputFileHiveParquetAssembly;
	}
}
