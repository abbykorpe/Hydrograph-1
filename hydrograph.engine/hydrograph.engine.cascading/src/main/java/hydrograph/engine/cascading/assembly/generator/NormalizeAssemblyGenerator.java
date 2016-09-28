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

import cascading.pipe.Pipe;
import hydrograph.engine.assembly.entity.NormalizeEntity;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.cascading.assembly.NormalizeAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OperationAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.operationstypes.Normalize;

public class NormalizeAssemblyGenerator extends OperationAssemblyGeneratorBase {

	private NormalizeEntity normalizeEntity;
	private NormalizeAssembly normalizeCustomAssembly;
	private Normalize jaxbNormalize;
	private static Logger LOG = LoggerFactory.getLogger(NormalizeAssemblyGenerator.class);

	public NormalizeAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Pipe> getSourcePipe() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbNormalize = (Normalize) baseComponent;

	}

	@Override
	public void createEntity() {
		normalizeEntity = new NormalizeEntity();

	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing normalize entity for component: " + jaxbNormalize.getId());
		normalizeEntity.setComponentId(jaxbNormalize.getId());
		normalizeEntity.setBatch(jaxbNormalize.getBatch());
		// check if operation is present
		if (jaxbNormalize.getOperationOrExpression() != null && jaxbNormalize.getOperationOrExpression().size() > 0) {

			LOG.trace("Operation(s) present for normalize component: " + jaxbNormalize.getId() + ", processing");
			// set the number of operations in the normalize component and set
			// operation present to true
			normalizeEntity.setNumOperations(jaxbNormalize.getOperationOrExpression().size());
			normalizeEntity.setOperationPresent(true);
			normalizeEntity.setOperationsList(OperationEntityUtils.extractOperations(jaxbNormalize.getOperationOrExpression()));
		} else {

			LOG.trace("Operation not present for normalize component: " + jaxbNormalize.getId()
					+ ", skipped operation processing");
			// default the number of operations in the normalize component to 0
			// and set operation present to false
			normalizeEntity.setNumOperations(0);
			normalizeEntity.setOperationPresent(false);
		}

		if (jaxbNormalize.getOutSocket() == null) {
			throw new NullPointerException("No out socket defined for normalize component: " + jaxbNormalize.getId());
		}

		normalizeEntity.setRuntimeProperties(
				OperationEntityUtils.extractRuntimeProperties(jaxbNormalize.getRuntimeProperties()));
		normalizeEntity.setOutSocketList(OperationEntityUtils.extractOutSocketList(jaxbNormalize.getOutSocket()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		normalizeCustomAssembly = new NormalizeAssembly(normalizeEntity, componentParameters);
	}

	@Override
	public BaseComponent<NormalizeEntity> getAssembly() {
		return normalizeCustomAssembly;
	}
}
