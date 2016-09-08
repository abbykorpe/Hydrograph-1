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
import hydrograph.engine.assembly.entity.JoinEntity;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.cascading.assembly.JoinAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OperationAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.operationstypes.Join;

public class JoinAssemblyGenerator extends OperationAssemblyGeneratorBase {

	private JoinEntity joinEntity;
	private JoinAssembly joinSubAssembly;
	private Join jaxbJoin;
	private static Logger LOG = LoggerFactory
			.getLogger(JoinAssemblyGenerator.class);

	public JoinAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbJoin = (Join) baseComponent;
	}

	@Override
	public void createEntity() {
		joinEntity = new JoinEntity();
	}

	@Override
	public void initializeEntity() {

		LOG.trace("Initializing join entity for component: " + jaxbJoin.getId());
		joinEntity.setComponentId(jaxbJoin.getId());
		joinEntity.setPhase(jaxbJoin.getPhase());

		if (jaxbJoin.getOutSocket() == null) {
			throw new NullPointerException(
					"No out socket defined for join component: "
							+ jaxbJoin.getId());
		}
		joinEntity.setKeyFields(OperationEntityUtils
				.extractKeyFieldsListFromOutSockets(jaxbJoin.getKeys()));
		joinEntity.setOutSocketList(OperationEntityUtils
				.extractOutSocketList(jaxbJoin.getOutSocket()));
		joinEntity.setRuntimeProperties(OperationEntityUtils
				.extractRuntimeProperties(jaxbJoin.getRuntimeProperties()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		joinSubAssembly = new JoinAssembly(joinEntity, componentParameters);
	}

	@Override
	public BaseComponent<JoinEntity> getAssembly() {
		return joinSubAssembly;
	}

	@Override
	public Map<String, Pipe> getSourcePipe() {
		return null;
	}

}
