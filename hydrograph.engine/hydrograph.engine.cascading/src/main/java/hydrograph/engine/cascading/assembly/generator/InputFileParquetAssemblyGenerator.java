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

import hydrograph.engine.assembly.entity.InputFileParquetEntity;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.cascading.assembly.InputFileParquetAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.inputtypes.ParquetFile;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.Tap;

public class InputFileParquetAssemblyGenerator extends InputAssemblyGeneratorBase {

	private ParquetFile jaxbParquetFile;
	private InputFileParquetEntity inputFileParquetEntity;
	private InputFileParquetAssembly inputFileParquetAssembly;
	private static Logger LOG = LoggerFactory.getLogger(InputFileParquetAssemblyGenerator.class);

	public InputFileParquetAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSourceTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbParquetFile = (ParquetFile) baseComponent;
	}

	@Override
	public void createEntity() {
		inputFileParquetEntity = new InputFileParquetEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file parquet entity for component: " + jaxbParquetFile.getId());
		inputFileParquetEntity.setComponentId(jaxbParquetFile.getId());
		inputFileParquetEntity.setPath(jaxbParquetFile.getPath().getUri());
		inputFileParquetEntity.setFieldsList(InputEntityUtils.extractInputFields(
				jaxbParquetFile.getOutSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema()));
		inputFileParquetEntity.setOutSocketList(InputEntityUtils.extractOutSocket(jaxbParquetFile.getOutSocket()));
		inputFileParquetEntity.setRuntimeProperties(
				InputEntityUtils.extractRuntimeProperties(jaxbParquetFile.getRuntimeProperties()));
	}

	@Override
	public BaseComponent getAssembly() {
		return inputFileParquetAssembly;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		inputFileParquetAssembly = new InputFileParquetAssembly(inputFileParquetEntity, componentParameters);
	}
}