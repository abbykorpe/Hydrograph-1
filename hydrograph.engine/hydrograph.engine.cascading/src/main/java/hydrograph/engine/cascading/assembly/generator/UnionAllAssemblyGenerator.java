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

import hydrograph.engine.assembly.entity.UnionAllEntity;
import hydrograph.engine.assembly.entity.utils.StraightPullEntityUtils;
import hydrograph.engine.cascading.assembly.UnionAllAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.StraightPullAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.straightpulltypes.UnionAll;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Pipe;

public class UnionAllAssemblyGenerator extends
		StraightPullAssemblyGeneratorBase {

	private UnionAll jaxbUnionAll;
	private UnionAllEntity unionAllEntity;
	private UnionAllAssembly unionAllAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(UnionAllAssemblyGenerator.class);

	public UnionAllAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Pipe> getPipes() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbUnionAll = (UnionAll) baseComponent;
	}

	@Override
	public void createEntity() {
		unionAllEntity = new UnionAllEntity();
	}

	@Override
	public void initializeEntity() {
		
		LOG.trace("Initializing union all entity for component: "
				+ jaxbUnionAll.getId());
		unionAllEntity.setComponentId(jaxbUnionAll.getId());
		unionAllEntity.setPhase(jaxbUnionAll.getPhase().intValue());
		unionAllEntity.setOutSocket(StraightPullEntityUtils.extractOutSocketList(
				jaxbUnionAll.getOutSocket()).get(0));
		unionAllEntity.setRuntimeProperties(StraightPullEntityUtils
				.extractRuntimeProperties(jaxbUnionAll.getRuntimeProperties()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		unionAllAssembly = new UnionAllAssembly(unionAllEntity,
				componentParameters);
	}

	@Override
	public BaseComponent getAssembly() {
		return unionAllAssembly;
	}
}