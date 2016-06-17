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

import cascading.tap.Tap;
import hydrograph.engine.assembly.entity.OutputFileFixedWidthEntity;
import hydrograph.engine.assembly.entity.utils.OutputEntityUtils;
import hydrograph.engine.cascading.assembly.OutputFileFixedWidthAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OutputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TrueFalse;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.outputtypes.TextFileFixedWidth;

public class OutputFileFixedWidthAssemblyGenerator extends OutputAssemblyGeneratorBase {

	public OutputFileFixedWidthAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
		// TODO Auto-generated constructor stub
	}

	private OutputFileFixedWidthAssembly outputFileFixedWidthAssembly;
	private OutputFileFixedWidthEntity outputFileFixedWidthEntity;
	private TextFileFixedWidth jaxbOutputFileFixedWidth;

	@Override
	public Map<String, Tap> getSinkTap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbOutputFileFixedWidth = (TextFileFixedWidth) baseComponent;
	}

	@Override
	public void createEntity() {
		outputFileFixedWidthEntity = new OutputFileFixedWidthEntity();
	}

	@Override
	public void initializeEntity() {

		outputFileFixedWidthEntity.setComponentId(jaxbOutputFileFixedWidth.getId());
		outputFileFixedWidthEntity.setPhase(jaxbOutputFileFixedWidth.getPhase());
		outputFileFixedWidthEntity.setPath(jaxbOutputFileFixedWidth.getPath().getUri());
		outputFileFixedWidthEntity.setSafe(
				jaxbOutputFileFixedWidth.getSafe() != null ? jaxbOutputFileFixedWidth.getSafe().isValue() : false);
		outputFileFixedWidthEntity.setStrict(
				jaxbOutputFileFixedWidth.getStrict() != null ? jaxbOutputFileFixedWidth.getStrict().isValue() : false);
		outputFileFixedWidthEntity.setCharset(jaxbOutputFileFixedWidth.getCharset() != null
				? jaxbOutputFileFixedWidth.getCharset().getValue().value() : "UTF-8");
		outputFileFixedWidthEntity.setRuntimeProperties(
				OutputEntityUtils.extractRuntimeProperties(jaxbOutputFileFixedWidth.getRuntimeProperties()));
		outputFileFixedWidthEntity.setFieldsList(OutputEntityUtils.extractOutputFields(
				jaxbOutputFileFixedWidth.getInSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema()));
		if (jaxbOutputFileFixedWidth.getOverWrite() != null
				&& (TrueFalse.FALSE).equals(jaxbOutputFileFixedWidth.getOverWrite().getValue()))
			outputFileFixedWidthEntity.setOverWrite(false);
		else
			outputFileFixedWidthEntity.setOverWrite(true);
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		outputFileFixedWidthAssembly = new OutputFileFixedWidthAssembly(outputFileFixedWidthEntity,
				componentParameters);
	}

	@Override
	public BaseComponent<OutputFileFixedWidthEntity> getAssembly() {
		return outputFileFixedWidthAssembly;
	}
}