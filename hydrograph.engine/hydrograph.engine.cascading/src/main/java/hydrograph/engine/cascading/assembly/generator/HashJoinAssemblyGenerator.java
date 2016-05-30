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

import hydrograph.engine.assembly.entity.HashJoinEntity;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.cascading.assembly.HashJoinAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OperationAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.operationstypes.HashJoin;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Pipe;

public class HashJoinAssemblyGenerator extends OperationAssemblyGeneratorBase {

	private HashJoinEntity hashJoinEntity;
	private HashJoinAssembly hashJoinAssembly;
	private HashJoin jaxbHashJoin;
	private static Logger LOG = LoggerFactory
			.getLogger(HashJoinAssemblyGenerator.class);

	public HashJoinAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbHashJoin = (HashJoin) baseComponent;
	}

	@Override
	public void createEntity() {
		hashJoinEntity = new HashJoinEntity();
	}

	@Override
	public void initializeEntity() {
		LOG.trace("Initializing hash join entity for component: "
				+ jaxbHashJoin.getId());
		hashJoinEntity.setComponentId(jaxbHashJoin.getId());
		hashJoinEntity.setPhase(jaxbHashJoin.getPhase().intValue());

		if (jaxbHashJoin.getOutSocket() == null) {
			throw new NullPointerException("No out socket defined for hash join component: " + jaxbHashJoin.getId());
		}

		hashJoinEntity.setKeyFields(
				OperationEntityUtils.extractKeyFieldsListFromOutSocketsForHashJoin(jaxbHashJoin.getKeys()));
		hashJoinEntity.setOutSocketList(OperationEntityUtils.extractOutSocketList(jaxbHashJoin.getOutSocket()));
//		hashJoinEntity.setInSocketMap(OperationEntityUtils.extractInSocketMap(jaxbHashJoin.getInSocket()));
		hashJoinEntity.setRuntimeProperties(
				OperationEntityUtils.extractRuntimeProperties(jaxbHashJoin.getRuntimeProperties()));
		
		if(jaxbHashJoin.getMatch() != null)
			hashJoinEntity.setMatch(jaxbHashJoin.getMatch().getValue().value());
		else
			throw new NullPointerException("No 'match' option set for hash join component: " + jaxbHashJoin.getId());
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		hashJoinAssembly = new HashJoinAssembly(hashJoinEntity, componentParameters);
	}

	@Override
	public BaseComponent getAssembly() {
		return hashJoinAssembly;
	}

	@Override
	public Map<String, Pipe> getSourcePipe() {
		return null;
	}

}
