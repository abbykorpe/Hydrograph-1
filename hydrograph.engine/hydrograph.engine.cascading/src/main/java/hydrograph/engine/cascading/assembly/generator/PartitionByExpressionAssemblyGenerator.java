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
import hydrograph.engine.assembly.entity.PartitionByExpressionEntity;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.cascading.assembly.PartitionByExpressionAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OperationAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.operationstypes.PartitionByExpression;

public class PartitionByExpressionAssemblyGenerator extends OperationAssemblyGeneratorBase {

	private PartitionByExpressionEntity partitionByExpressionEntity;
	private PartitionByExpressionAssembly partitionByExpressionAssembly;
	private PartitionByExpression jaxbPartitionByExpression;
	
	private static Logger LOG = LoggerFactory.getLogger(PartitionByExpressionAssemblyGenerator.class);

	public PartitionByExpressionAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Pipe> getSourcePipe() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbPartitionByExpression = (PartitionByExpression) baseComponent;
	}

	@Override
	public void createEntity() {
		partitionByExpressionEntity = new PartitionByExpressionEntity();
	}

	@Override
	public void initializeEntity() {
		LOG.trace("Initializing aggregate entity for component: " + jaxbPartitionByExpression.getId());

		partitionByExpressionEntity.setComponentId(jaxbPartitionByExpression.getId());
		partitionByExpressionEntity.setPhase(jaxbPartitionByExpression.getPhase());

		// check if operation is present
		if (jaxbPartitionByExpression.getOperationOrExpression() != null) {

			LOG.trace("Operation present for PartitionByExpression component: " + jaxbPartitionByExpression.getId()
					+ ", processing");
			partitionByExpressionEntity.setOperation(
					OperationEntityUtils.extractOperations(jaxbPartitionByExpression.getOperationOrExpression()).get(0));
		} else {
			LOG.trace("Operation not present for aggregate component: " + jaxbPartitionByExpression.getId()
					+ ", skipped operation processing");
		}
		if (jaxbPartitionByExpression.getOutSocket() == null) {
			throw new NullPointerException(
					"No out socket defined for partitionByExpression component: " + jaxbPartitionByExpression.getId());
		}
		partitionByExpressionEntity.setOutSocketList(OperationEntityUtils.extractOutSocketList(jaxbPartitionByExpression.getOutSocket()));
		partitionByExpressionEntity.setNumPartitions(jaxbPartitionByExpression.getNoOfPartitions().getValue());
		partitionByExpressionEntity.setRuntimeProperties(
				OperationEntityUtils.extractRuntimeProperties(jaxbPartitionByExpression.getRuntimeProperties()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		partitionByExpressionAssembly = new PartitionByExpressionAssembly(partitionByExpressionEntity,
				componentParameters);
	}

	@Override
	public BaseComponent<PartitionByExpressionEntity> getAssembly() {
		return partitionByExpressionAssembly;
	}
}
