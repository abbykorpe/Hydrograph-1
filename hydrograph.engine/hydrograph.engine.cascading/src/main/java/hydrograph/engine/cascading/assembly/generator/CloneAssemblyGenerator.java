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
import hydrograph.engine.assembly.entity.CloneEntity;
import hydrograph.engine.assembly.entity.utils.StraightPullEntityUtils;
import hydrograph.engine.cascading.assembly.CloneAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.StraightPullAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.straightpulltypes.Clone;

public class CloneAssemblyGenerator extends StraightPullAssemblyGeneratorBase {

	CloneEntity cloneEntity;
	private Clone jaxbClone;
	private CloneAssembly cloneAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(CloneAssemblyGenerator.class);
	
	public CloneAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Pipe> getPipes() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbClone = (Clone) baseComponent;
	}

	@Override
	public void createEntity() {
		cloneEntity = new CloneEntity();
	}

	@Override
	public void initializeEntity() {
		LOG.trace("Initializing clone entity for component: "
				+ jaxbClone.getId());
		cloneEntity.setComponentId(jaxbClone.getId());
		cloneEntity.setPhase(jaxbClone.getPhase());
		cloneEntity.setOutSocketList(StraightPullEntityUtils
				.extractOutSocketList(jaxbClone.getOutSocket()));
		cloneEntity.setRuntimeProperties(StraightPullEntityUtils
				.extractRuntimeProperties(jaxbClone.getRuntimeProperties()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		cloneAssembly = new CloneAssembly(cloneEntity, componentParameters);
	}

	@Override
	public BaseComponent<CloneEntity> getAssembly() {
		return cloneAssembly;
	}
}
