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
import hydrograph.engine.assembly.entity.TransformEntity;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.cascading.assembly.TransformAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OperationAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.operationstypes.Transform;

public class TransformAssemblyGenerator extends OperationAssemblyGeneratorBase {

	public TransformAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
		// TODO Auto-generated constructor stub
	}

	private TransformEntity transformEntity;
	private Transform jaxbTransform;
	private TransformAssembly transformAssembly;
	private static Logger LOG = LoggerFactory.getLogger(TransformAssemblyGenerator.class);

	@Override
	public Map<String, Pipe> getSourcePipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbTransform = (Transform) baseComponent;

	}

	@Override
	public void createEntity() {
		transformEntity = new TransformEntity();

	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing transform entity for component: " + jaxbTransform.getId());
		transformEntity.setComponentId(jaxbTransform.getId());
		transformEntity.setBatch(jaxbTransform.getBatch());

		// check if operation is present
		if (jaxbTransform.getOperationOrExpression() != null && jaxbTransform.getOperationOrExpression().size() > 0) {

			LOG.trace("Operation(s) present for transform component: " + jaxbTransform.getId() + ", processing");
			// set the number of operations in the transform component and set
			// operation present to true
			transformEntity.setNumOperations(jaxbTransform.getOperationOrExpression().size());
			transformEntity.setOperationPresent(true);
			transformEntity.setOperationsList(OperationEntityUtils.extractOperations(jaxbTransform.getOperationOrExpression()));
		} else {

			LOG.trace("Operation not present for transform component: " + jaxbTransform.getId()
					+ ", skipped operation processing");
			// default the number of operations in the transform component to 0
			// and set operation present to false
			transformEntity.setNumOperations(0);
			transformEntity.setOperationPresent(false);
		}

		if (jaxbTransform.getOutSocket() == null) {
			throw new NullPointerException("No out socket defined for component: " + jaxbTransform.getId());
		}

		transformEntity.setRuntimeProperties(
				OperationEntityUtils.extractRuntimeProperties(jaxbTransform.getRuntimeProperties()));
		transformEntity.setOutSocketList(OperationEntityUtils.extractOutSocketList(jaxbTransform.getOutSocket()));

	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		transformAssembly = new TransformAssembly(transformEntity, componentParameters);
	}

	@Override
	public BaseComponent<TransformEntity> getAssembly() {
		return transformAssembly;
	}
}
