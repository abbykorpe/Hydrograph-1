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
import hydrograph.engine.assembly.entity.OutputFileSequenceFormatEntity;
import hydrograph.engine.assembly.entity.utils.OutputEntityUtils;
import hydrograph.engine.cascading.assembly.OutputFileSequenceFormatAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OutputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.outputtypes.SequenceOutputFile;

public class OutputFileSequenceAssemblyGenerator extends OutputAssemblyGeneratorBase {

	private OutputFileSequenceFormatEntity outputFileSequenceFormatEntity;
	private SequenceOutputFile jaxbSequenceOutputFile;
	private OutputFileSequenceFormatAssembly outputFileDelimitedAssembly;
	private static Logger LOG = LoggerFactory.getLogger(OutputFileSequenceAssemblyGenerator.class);

	public OutputFileSequenceAssemblyGenerator(TypeBaseComponent baseComponent) {
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
		jaxbSequenceOutputFile = (SequenceOutputFile) baseComponent;
	}

	@Override
	public void createEntity() {
		outputFileSequenceFormatEntity = new OutputFileSequenceFormatEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing output file delimited entity for component: " + jaxbSequenceOutputFile.getId());
		outputFileSequenceFormatEntity.setComponentId(jaxbSequenceOutputFile.getId());
		outputFileSequenceFormatEntity.setPhase(jaxbSequenceOutputFile.getPhase());
		outputFileSequenceFormatEntity.setPath(jaxbSequenceOutputFile.getPath().getUri());
		outputFileSequenceFormatEntity.setFieldsList(OutputEntityUtils.extractOutputFields(
				jaxbSequenceOutputFile.getInSocket().get(0).getSchema().getFieldOrRecordOrIncludeExternalSchema()));
		outputFileSequenceFormatEntity.setRuntimeProperties(
				OutputEntityUtils.extractRuntimeProperties(jaxbSequenceOutputFile.getRuntimeProperties()));
		
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		outputFileDelimitedAssembly = new OutputFileSequenceFormatAssembly(outputFileSequenceFormatEntity, componentParameters);
	}

	@Override
	public BaseComponent<OutputFileSequenceFormatEntity> getAssembly() {
		return outputFileDelimitedAssembly;
	}
}