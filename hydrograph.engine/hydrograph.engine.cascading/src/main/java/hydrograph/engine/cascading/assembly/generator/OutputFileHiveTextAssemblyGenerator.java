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
import hydrograph.engine.assembly.entity.OutputFileHiveTextEntity;
import hydrograph.engine.assembly.entity.base.HiveEntityBase;
import hydrograph.engine.assembly.entity.utils.OutputEntityUtils;
import hydrograph.engine.cascading.assembly.OutputFileHiveTextAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OutputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TrueFalse;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.ohivetextfile.HivePartitionFieldsType;
import hydrograph.engine.jaxb.ohivetextfile.PartitionFieldBasicType;
import hydrograph.engine.jaxb.outputtypes.HiveTextFile;
import hydrograph.engine.utilities.GeneralUtilities;

public class OutputFileHiveTextAssemblyGenerator extends
		OutputAssemblyGeneratorBase {

	private HiveTextFile jaxbHiveTextFile;
	private OutputFileHiveTextEntity outputFileHiveTextEntity;
	private OutputFileHiveTextAssembly outputFileHiveTextAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(OutputFileHiveTextAssemblyGenerator.class);

	public OutputFileHiveTextAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSinkTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbHiveTextFile = (HiveTextFile) baseComponent;
	}

	@Override
	public void createEntity() {
		outputFileHiveTextEntity = new OutputFileHiveTextEntity();
	}

	@Override
	public void initializeEntity() {
		LOG.trace("Initializing output file Hive Text entity for component: "
				+ jaxbHiveTextFile.getId());
		outputFileHiveTextEntity.setComponentId(jaxbHiveTextFile.getId());
		outputFileHiveTextEntity.setBatch(jaxbHiveTextFile.getBatch());
		outputFileHiveTextEntity
				.setFieldsList(OutputEntityUtils
						.extractOutputFields(jaxbHiveTextFile.getInSocket()
								.get(0).getSchema()
								.getFieldOrRecordOrIncludeExternalSchema()));
		outputFileHiveTextEntity.setExternalTablePathUri(jaxbHiveTextFile
				.getExternalTablePath() == null ? null : jaxbHiveTextFile
				.getExternalTablePath().getUri());
		outputFileHiveTextEntity.setRuntimeProperties(OutputEntityUtils
				.extractRuntimeProperties(jaxbHiveTextFile
						.getRuntimeProperties()));

		outputFileHiveTextEntity.setDatabaseName(jaxbHiveTextFile
				.getDatabaseName().getValue());
		outputFileHiveTextEntity.setTableName(jaxbHiveTextFile.getTableName()
				.getValue());
		outputFileHiveTextEntity
				.setPartitionKeys(extractPartitionFields(jaxbHiveTextFile
						.getPartitionKeys()));
		outputFileHiveTextEntity
				.setOverWrite(!(jaxbHiveTextFile.getOverWrite() != null && (TrueFalse.FALSE)
						.equals(jaxbHiveTextFile.getOverWrite().getValue())));
		outputFileHiveTextEntity.setDelimiter(jaxbHiveTextFile.getDelimiter() != null
				? GeneralUtilities.parseHex(jaxbHiveTextFile.getDelimiter().getValue()) : null);
		outputFileHiveTextEntity
				.setQuote(jaxbHiveTextFile.getQuote() != null ? jaxbHiveTextFile
						.getQuote().getValue() : "");
		outputFileHiveTextEntity
				.setSafe(jaxbHiveTextFile.getSafe() != null ? jaxbHiveTextFile
						.getSafe().isValue() : false);
		outputFileHiveTextEntity
				.setStrict(jaxbHiveTextFile.getStrict() != null ? jaxbHiveTextFile
						.getStrict().isValue() : false);

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
		outputFileHiveTextAssembly = new OutputFileHiveTextAssembly(
				outputFileHiveTextEntity, componentParameters);
	}

	@Override
	public BaseComponent<HiveEntityBase> getAssembly() {
		return outputFileHiveTextAssembly;
	}
}
