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

import hydrograph.engine.assembly.entity.InputFileHiveTextEntity;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.cascading.assembly.InputFileHiveTextAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.ihivetextfile.HivePartitionFieldsType;
import hydrograph.engine.jaxb.inputtypes.HiveTextFile;
import hydrograph.engine.utilities.GeneralUtilities;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.Tap;

public class InputFileHiveTextAssemblyGenerator extends
InputAssemblyGeneratorBase {

	private HiveTextFile jaxbHiveTextFile;
	private InputFileHiveTextEntity inputHiveFileEntity;
	private InputFileHiveTextAssembly inputHiveFileAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(InputFileHiveTextAssemblyGenerator.class);

	public InputFileHiveTextAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSourceTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbHiveTextFile = (HiveTextFile) baseComponent;
	}

	@Override
	public void createEntity() {
		inputHiveFileEntity = new InputFileHiveTextEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file parquet entity for component: "
				+ jaxbHiveTextFile.getId());

		inputHiveFileEntity.setComponentId(jaxbHiveTextFile.getId());

		inputHiveFileEntity
		.setFieldsList(InputEntityUtils
				.extractInputFields(jaxbHiveTextFile.getOutSocket()
						.get(0).getSchema()
						.getFieldOrRecordOrIncludeExternalSchema()));
		inputHiveFileEntity.setOutSocketList(InputEntityUtils
				.extractOutSocket(jaxbHiveTextFile.getOutSocket()));
		inputHiveFileEntity.setDelimiter(GeneralUtilities.parseHex(jaxbHiveTextFile.getDelimiter().getValue()));
		inputHiveFileEntity.setDatabaseName(jaxbHiveTextFile.getDatabaseName().getValue());
		inputHiveFileEntity.setTableName(jaxbHiveTextFile.getTableName().getValue());
		inputHiveFileEntity.setExternalTablePathUri(jaxbHiveTextFile.getExternalTablePath() != null?jaxbHiveTextFile.getExternalTablePath().getUri():null);
		inputHiveFileEntity.setQuote(jaxbHiveTextFile.getQuote() != null?jaxbHiveTextFile.getQuote().getValue():"");
		inputHiveFileEntity.setSafe(jaxbHiveTextFile.getSafe() != null?jaxbHiveTextFile.getSafe().isValue():false);
		inputHiveFileEntity.setStrict(jaxbHiveTextFile.getStrict() != null?jaxbHiveTextFile.getStrict().isValue():false);
		inputHiveFileEntity.setRuntimeProperties(InputEntityUtils
				.extractRuntimeProperties(jaxbHiveTextFile
						.getRuntimeProperties()));
		inputHiveFileEntity.setPartitionKeys(extractPartitionFields(jaxbHiveTextFile.getPartitionKeys()));
		inputHiveFileEntity.setPhase(jaxbHiveTextFile.getPhase().intValue());
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
		String[] partitionKeys;
		if (hivePartitionFieldsType != null && hivePartitionFieldsType.getField() != null) {
		 partitionKeys = new String[hivePartitionFieldsType.getField().size()];
		for (int i = 0; i < hivePartitionFieldsType.getField().size(); i++) {
			partitionKeys[i] = hivePartitionFieldsType.getField().get(i).getName();
		}
		}else{
			return new String[0];
		}
		return partitionKeys;
	}

	@Override
	public BaseComponent getAssembly() {
		return inputHiveFileAssembly;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		inputHiveFileAssembly = new InputFileHiveTextAssembly(
				inputHiveFileEntity, componentParameters);
	}
}