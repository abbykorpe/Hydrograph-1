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

import hydrograph.engine.assembly.entity.OutputFileAvroEntity;
import hydrograph.engine.assembly.entity.utils.OutputEntityUtils;
import hydrograph.engine.cascading.assembly.OutputFileAvroAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OutputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TrueFalse;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.outputtypes.AvroFile;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.Tap;

public class OutputFileAvroAssemblyGenerator extends OutputAssemblyGeneratorBase {

	private AvroFile jaxbAvroFile;
	private OutputFileAvroEntity outputFileAvroEntity;
	private OutputFileAvroAssembly outputFileAvroAssembly;
	private static Logger LOG = LoggerFactory.getLogger(OutputFileAvroAssemblyGenerator.class);

	public OutputFileAvroAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Tap> getSinkTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbAvroFile = (AvroFile) baseComponent;
	}

	@Override
	public void createEntity() {
		outputFileAvroEntity = new OutputFileAvroEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing output file Avro entity for component: " + jaxbAvroFile.getId());
		outputFileAvroEntity.setComponentId(jaxbAvroFile.getId());
		outputFileAvroEntity.setPhase(jaxbAvroFile.getPhase().intValue());
		outputFileAvroEntity.setPath(jaxbAvroFile.getPath().getUri());
		outputFileAvroEntity.setSchemaFieldsList(OutputEntityUtils.extractOutputFields(
				jaxbAvroFile.getInSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema()));

		outputFileAvroEntity.setRuntimeProperties(
				OutputEntityUtils.extractRuntimeProperties(jaxbAvroFile.getRuntimeProperties()));
		if (jaxbAvroFile.getOverWrite() != null
				&& (TrueFalse.FALSE).equals(jaxbAvroFile.getOverWrite().getValue()))
			outputFileAvroEntity.setOverWrite(false);
		else
			outputFileAvroEntity.setOverWrite(true);
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		outputFileAvroAssembly = new OutputFileAvroAssembly(outputFileAvroEntity, componentParameters);
	}

	@Override
	public BaseComponent getAssembly() {
		return outputFileAvroAssembly;
	}
}
