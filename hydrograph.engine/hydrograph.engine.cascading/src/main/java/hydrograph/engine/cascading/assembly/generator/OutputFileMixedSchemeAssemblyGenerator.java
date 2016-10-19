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

import hydrograph.engine.assembly.entity.OutputFileMixedSchemeEntity;
import hydrograph.engine.assembly.entity.utils.OutputEntityUtils;
import hydrograph.engine.cascading.assembly.OutputFileMixedSchemeAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OutputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TrueFalse;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.outputtypes.TextFileMixedScheme;

import java.util.Map;

import cascading.tap.Tap;

public class OutputFileMixedSchemeAssemblyGenerator extends OutputAssemblyGeneratorBase {

	private OutputFileMixedSchemeEntity outputFileMixedSchemeEntity;
	private TextFileMixedScheme jaxbTextFileMixedScheme;
	private OutputFileMixedSchemeAssembly outputMixedFileAssembly;

	public OutputFileMixedSchemeAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, Tap> getSinkTap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbTextFileMixedScheme = (TextFileMixedScheme) baseComponent;
	}

	@Override
	public void createEntity() {
		outputFileMixedSchemeEntity = new OutputFileMixedSchemeEntity();
	}

	@Override
	public void initializeEntity() {

		outputFileMixedSchemeEntity.setComponentId(jaxbTextFileMixedScheme.getId());
		outputFileMixedSchemeEntity.setBatch(jaxbTextFileMixedScheme.getBatch());
		outputFileMixedSchemeEntity.setCharset(jaxbTextFileMixedScheme.getCharset() != null
				? jaxbTextFileMixedScheme.getCharset().getValue().value() : "UTF-8");

		outputFileMixedSchemeEntity.setPath(jaxbTextFileMixedScheme.getPath().getUri());
		outputFileMixedSchemeEntity.setSafe(
				jaxbTextFileMixedScheme.getSafe() != null ? jaxbTextFileMixedScheme.getSafe().isValue() : false);

		outputFileMixedSchemeEntity.setFieldsList(OutputEntityUtils.extractOutputFields(
				jaxbTextFileMixedScheme.getInSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema()));

		outputFileMixedSchemeEntity.setRuntimeProperties(
				OutputEntityUtils.extractRuntimeProperties(jaxbTextFileMixedScheme.getRuntimeProperties()));
		outputFileMixedSchemeEntity
		.setQuote(jaxbTextFileMixedScheme.getQuote() != null ? jaxbTextFileMixedScheme
				.getQuote().getValue() : "");

		outputFileMixedSchemeEntity.setStrict(
				jaxbTextFileMixedScheme.getStrict() != null ? jaxbTextFileMixedScheme.getStrict().isValue() : true);
		if (jaxbTextFileMixedScheme.getOverWrite() != null
				&& (TrueFalse.FALSE).equals(jaxbTextFileMixedScheme.getOverWrite().getValue()))
			outputFileMixedSchemeEntity.setOverWrite(false);
		else
			outputFileMixedSchemeEntity.setOverWrite(true);

	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		outputMixedFileAssembly = new OutputFileMixedSchemeAssembly(outputFileMixedSchemeEntity, componentParameters);
	}

	@Override
	public BaseComponent<OutputFileMixedSchemeEntity> getAssembly() {
		return outputMixedFileAssembly;
	}
}
