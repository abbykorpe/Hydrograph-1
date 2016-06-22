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
import hydrograph.engine.assembly.entity.AggregateEntity;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.cascading.assembly.AggregateAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OperationAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.operationstypes.Aggregate;

public class AggregateAssemblyGenerator extends OperationAssemblyGeneratorBase {

	private AggregateEntity aggregateEntity;
	private AggregateAssembly aggregateCustomAssembly;
	private Aggregate jaxbAggregate;
	private static Logger LOG = LoggerFactory.getLogger(AggregateAssemblyGenerator.class);

	public AggregateAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbAggregate = (Aggregate) baseComponent;

	}

	@Override
	public Map<String, Pipe> getSourcePipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createEntity() {
		aggregateEntity = new AggregateEntity();
	}

	@Override
	public void initializeEntity() {
		LOG.trace("Initializing aggregate entity for component: " + jaxbAggregate.getId());
		aggregateEntity.setComponentId(jaxbAggregate.getId());
		aggregateEntity.setPhase(jaxbAggregate.getPhase());

		// check if operation is present
		if (jaxbAggregate.getOperationOrExpression() != null && jaxbAggregate.getOperationOrExpression().size() > 0) {

			LOG.trace("Operation(s) present for aggregate component: " + jaxbAggregate.getId() + ", processing");
			// set the number of operations in the transform component and set
			// operation present to true
			aggregateEntity.setNumOperations(jaxbAggregate.getOperationOrExpression().size());
			aggregateEntity.setOperationPresent(true);
			aggregateEntity.setOperationsList(OperationEntityUtils.extractOperations(jaxbAggregate.getOperationOrExpression()));
		} else {
			LOG.trace("Operation not present for aggregate component: " + jaxbAggregate.getId()
					+ ", skipped operation processing");
			// default the number of operations in the transform component to 0
			// and set operation present to false
			aggregateEntity.setNumOperations(0);
			aggregateEntity.setOperationPresent(false);
		}

		if (jaxbAggregate.getOutSocket() == null) {
			throw new NullPointerException("No out socket defined for aggregate component: " + jaxbAggregate.getId());
		}

		aggregateEntity.setOutSocketList(OperationEntityUtils.extractOutSocketList(jaxbAggregate.getOutSocket()));

		aggregateEntity.setKeyFields(OperationEntityUtils.extractKeyFields(jaxbAggregate.getPrimaryKeys()));
		aggregateEntity.setSecondaryKeyFields(
				OperationEntityUtils.extractSecondaryKeyFields(jaxbAggregate.getSecondaryKeys()));

		aggregateEntity.setRuntimeProperties(
				OperationEntityUtils.extractRuntimeProperties(jaxbAggregate.getRuntimeProperties()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		aggregateCustomAssembly = new AggregateAssembly(aggregateEntity, componentParameters);
	}

	@Override
	public BaseComponent<AggregateEntity> getAssembly() {
		return aggregateCustomAssembly;
	}
}
