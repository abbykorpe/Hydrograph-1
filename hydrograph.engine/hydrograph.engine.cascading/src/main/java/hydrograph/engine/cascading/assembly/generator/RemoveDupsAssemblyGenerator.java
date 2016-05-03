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

import hydrograph.engine.assembly.entity.RemoveDupsEntity;
import hydrograph.engine.assembly.entity.utils.StraightPullEntityUtils;
import hydrograph.engine.cascading.assembly.RemoveDupsAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.StraightPullAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.straightpulltypes.RemoveDups;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Pipe;

public class RemoveDupsAssemblyGenerator extends
		StraightPullAssemblyGeneratorBase {

	private RemoveDupsAssembly removeDupsAssembly;
	private RemoveDups jaxbRemoveDups;
	private RemoveDupsEntity removeDupsEntity;
	private static Logger LOG = LoggerFactory
			.getLogger(RemoveDupsAssemblyGenerator.class);

	public RemoveDupsAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public Map<String, Pipe> getPipes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbRemoveDups = (RemoveDups) baseComponent;

	}

	@Override
	public void createEntity() {
		removeDupsEntity = new RemoveDupsEntity();

	}

	@Override
	public void initializeEntity() {
		
		LOG.trace("Initializing remove dups entity for component: "
				+ jaxbRemoveDups.getId());
		removeDupsEntity.setComponentId(jaxbRemoveDups.getId());
		removeDupsEntity.setPhase(jaxbRemoveDups.getPhase().intValue());
		removeDupsEntity.setKeep(jaxbRemoveDups.getKeep().getValue().name());
		removeDupsEntity.setKeyFields(StraightPullEntityUtils
				.extractKeyFields(jaxbRemoveDups.getPrimaryKeys()));
		removeDupsEntity.setSecondaryKeyFields(StraightPullEntityUtils
				.extractSecondaryKeyFields(jaxbRemoveDups.getSecondaryKeys()));
		removeDupsEntity
				.setRuntimeProperties(StraightPullEntityUtils
						.extractRuntimeProperties(jaxbRemoveDups
								.getRuntimeProperties()));
		removeDupsEntity.setOutSocketList(StraightPullEntityUtils
				.extractOutSocketList(jaxbRemoveDups.getOutSocket()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		removeDupsAssembly = new RemoveDupsAssembly(removeDupsEntity,
				componentParameters);
	}

	@Override
	public BaseComponent getAssembly() {
		return removeDupsAssembly;
	}
}
