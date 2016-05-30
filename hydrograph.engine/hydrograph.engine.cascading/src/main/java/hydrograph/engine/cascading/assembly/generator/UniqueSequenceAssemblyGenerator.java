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

import hydrograph.engine.assembly.entity.UniqueSequenceEntity;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.cascading.assembly.UniqueSequenceAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OperationAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.operationstypes.GenerateSequence;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Pipe;

public class UniqueSequenceAssemblyGenerator extends
		OperationAssemblyGeneratorBase {

	private UniqueSequenceEntity uniqueSequenceEntity;
	private GenerateSequence jaxbGenerateSequence;
	private UniqueSequenceAssembly uniqueSequenceAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(UniqueSequenceAssemblyGenerator.class);

	public UniqueSequenceAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbGenerateSequence = (GenerateSequence) baseComponent;
	}

	@Override
	public void createEntity() {
		uniqueSequenceEntity = new UniqueSequenceEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing generate sequence entity for component: "
				+ jaxbGenerateSequence.getId());
		uniqueSequenceEntity.setComponentId(jaxbGenerateSequence.getId());
		uniqueSequenceEntity.setPhase(jaxbGenerateSequence.getPhase()
				.intValue());
		// check if operation is present
		if (jaxbGenerateSequence.getOperation() != null) {
			LOG.trace("Operation(s) present for unique sequence component: "
					+ jaxbGenerateSequence.getId() + ", processing");
			// set the number of operations in the transform component and set
			// operation present to true
			uniqueSequenceEntity.setNumOperations(jaxbGenerateSequence
					.getOperation().size());
			uniqueSequenceEntity.setOperationPresent(true);
			uniqueSequenceEntity.setOperationList(OperationEntityUtils
					.extractOperations(jaxbGenerateSequence.getOperation()));
		} else {
			LOG.trace("Operation not present for unique sequence component: "
					+ jaxbGenerateSequence.getId()
					+ ", skipped operation processing");
			// default the number of operations in the transform component to 0
			// and set operation present to false
			uniqueSequenceEntity.setNumOperations(0);
			uniqueSequenceEntity.setOperationPresent(false);
		}

		if (jaxbGenerateSequence.getOutSocket() == null) {
			throw new NullPointerException(
					"No out socket defined for unique sequence component: "
							+ jaxbGenerateSequence.getId());
		}

		uniqueSequenceEntity.setOutSocketList(OperationEntityUtils
				.extractOutSocketList(jaxbGenerateSequence.getOutSocket()));
		uniqueSequenceEntity.setRuntimeProperties(OperationEntityUtils
				.extractRuntimeProperties(jaxbGenerateSequence
						.getRuntimeProperties()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		uniqueSequenceAssembly = new UniqueSequenceAssembly(
				uniqueSequenceEntity, componentParameters);
	}

	@Override
	public BaseComponent getAssembly() {
		return uniqueSequenceAssembly;
	}

	@Override
	public Map<String, Pipe> getSourcePipe() {
		return null;
	}
}