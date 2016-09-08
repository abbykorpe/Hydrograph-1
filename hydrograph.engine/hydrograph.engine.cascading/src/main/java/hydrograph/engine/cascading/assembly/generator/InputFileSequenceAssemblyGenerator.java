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
import hydrograph.engine.assembly.entity.InputFileSequenceFormatEntity;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.cascading.assembly.InputFileSequenceFormatAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.inputtypes.SequenceInputFile;

public class InputFileSequenceAssemblyGenerator extends InputAssemblyGeneratorBase {

	private InputFileSequenceFormatEntity inputFileSequenceFormatEntity;
	private InputFileSequenceFormatAssembly inputFileSequenceFormatAssembly;
	private SequenceInputFile jaxbFileSequenceFormat;
	private static Logger LOG = LoggerFactory.getLogger(InputFileSequenceAssemblyGenerator.class);

	public InputFileSequenceAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSourceTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbFileSequenceFormat = (SequenceInputFile) baseComponent;
	}

	@Override
	public void createEntity() {
		inputFileSequenceFormatEntity = new InputFileSequenceFormatEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing input file delimited entity for component: " + jaxbFileSequenceFormat.getId());
		inputFileSequenceFormatEntity.setComponentId(jaxbFileSequenceFormat.getId());
		inputFileSequenceFormatEntity.setPhase(jaxbFileSequenceFormat.getPhase());
		inputFileSequenceFormatEntity.setPath(jaxbFileSequenceFormat.getPath().getUri());
		inputFileSequenceFormatEntity.setFieldsList(InputEntityUtils.extractInputFields(
				jaxbFileSequenceFormat.getOutSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema()));
		inputFileSequenceFormatEntity.setRuntimeProperties(
				InputEntityUtils.extractRuntimeProperties(jaxbFileSequenceFormat.getRuntimeProperties()));

		inputFileSequenceFormatEntity.setOutSocketList(InputEntityUtils.extractOutSocket(jaxbFileSequenceFormat.getOutSocket()));

	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		inputFileSequenceFormatAssembly = new InputFileSequenceFormatAssembly(inputFileSequenceFormatEntity, componentParameters);
	}

	@Override
	public BaseComponent<InputFileSequenceFormatEntity> getAssembly() {
		return inputFileSequenceFormatAssembly;
	}
}
