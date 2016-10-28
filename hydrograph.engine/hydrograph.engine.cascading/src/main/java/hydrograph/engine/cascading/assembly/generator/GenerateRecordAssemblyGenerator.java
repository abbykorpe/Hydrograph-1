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
import hydrograph.engine.assembly.entity.GenerateRecordEntity;
import hydrograph.engine.assembly.entity.utils.InputEntityUtils;
import hydrograph.engine.cascading.assembly.GenerateRecordAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.InputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.inputtypes.GenerateRecord;

public class GenerateRecordAssemblyGenerator extends InputAssemblyGeneratorBase {
	private GenerateRecordAssembly generateRecord;
	private hydrograph.engine.jaxb.inputtypes.GenerateRecord jaxbGenerateRecord;
	private GenerateRecordEntity generateRecordEntity;
	private static Logger LOG = LoggerFactory.getLogger(GenerateRecordAssemblyGenerator.class);

	public GenerateRecordAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Tap> getSourceTap() {

		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbGenerateRecord = (GenerateRecord) baseComponent;
	}

	@Override
	public void createEntity() {
		generateRecordEntity = new GenerateRecordEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing generate record entity for component: " + jaxbGenerateRecord.getId());
		generateRecordEntity.setComponentId(jaxbGenerateRecord.getId());
		generateRecordEntity.setBatch(jaxbGenerateRecord.getBatch());
		generateRecordEntity.setComponentName(jaxbGenerateRecord.getName());
		generateRecordEntity.setRecordCount(jaxbGenerateRecord.getRecordCount().getValue());

		generateRecordEntity.setFieldsList(InputEntityUtils.extractInputFields(
				jaxbGenerateRecord.getOutSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema()));

		generateRecordEntity.setOutSocketList(InputEntityUtils.extractOutSocket(jaxbGenerateRecord.getOutSocket()));
		generateRecordEntity.setRuntimeProperties(
				InputEntityUtils.extractRuntimeProperties(jaxbGenerateRecord.getRuntimeProperties()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		generateRecord = new GenerateRecordAssembly(generateRecordEntity, componentParameters);
	}

	@Override
	public BaseComponent<GenerateRecordEntity> getAssembly() {
		return generateRecord;
	}
}