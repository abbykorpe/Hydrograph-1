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
import hydrograph.engine.assembly.entity.InputFileAvroEntity;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.cascading.assembly.InputFileAvroAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.inputtypes.AvroFile;

public class InputFileAvroAssemblyGenerator extends
		InputAssemblyGeneratorBase {

	private AvroFile jaxbAvroFile;
	private InputFileAvroEntity inputFileAvroEntity;
	private InputFileAvroAssembly inputFileAvroAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(InputFileAvroAssemblyGenerator.class);

	public InputFileAvroAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSourceTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbAvroFile = (AvroFile) baseComponent;
	}

	@Override
	public void createEntity() {
		inputFileAvroEntity = new InputFileAvroEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file Avro entity for component: "
				+ jaxbAvroFile.getId());
		inputFileAvroEntity.setComponentId(jaxbAvroFile.getId());
		inputFileAvroEntity.setPath(jaxbAvroFile.getPath().getUri());
		inputFileAvroEntity
				.setFieldsList(InputEntityUtils
						.extractInputFields(jaxbAvroFile.getOutSocket()
								.get(0).getSchema()
								.getFieldOrRecordOrIncludeExternalSchema()));
		inputFileAvroEntity.setOutSocketList(InputEntityUtils
				.extractOutSocket(jaxbAvroFile.getOutSocket()));
		inputFileAvroEntity.setRuntimeProperties(InputEntityUtils
				.extractRuntimeProperties(jaxbAvroFile
						.getRuntimeProperties()));
	}

	@Override
	public BaseComponent<InputFileAvroEntity> getAssembly() {
		return inputFileAvroAssembly;
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		inputFileAvroAssembly = new InputFileAvroAssembly(
				inputFileAvroEntity, componentParameters);
	}
}