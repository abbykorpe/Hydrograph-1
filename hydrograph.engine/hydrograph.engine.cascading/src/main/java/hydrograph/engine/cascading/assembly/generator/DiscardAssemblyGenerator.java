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

import hydrograph.engine.assembly.entity.DiscardEntity;
import hydrograph.engine.cascading.assembly.DiscardAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OutputAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.outputtypes.Discard;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.Tap;

public class DiscardAssemblyGenerator extends OutputAssemblyGeneratorBase {

	private DiscardEntity discardEntity;
	private Discard jaxbDiscard;
	private DiscardAssembly discardAssembly;
	private static Logger LOG = LoggerFactory
			.getLogger(DiscardAssemblyGenerator.class);

	public DiscardAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Tap> getSinkTap() {
		return null;
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbDiscard = (Discard) baseComponent;
	}

	@Override
	public void createEntity() {
		discardEntity = new DiscardEntity();
	}

	@Override
	public void initializeEntity() {
		
		LOG.trace("Initializing discard entity for component: "
				+ jaxbDiscard.getId());
		discardEntity.setComponentId(jaxbDiscard.getId());
		discardEntity.setPhase(jaxbDiscard.getPhase().intValue());
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		discardAssembly = new DiscardAssembly(discardEntity,
				componentParameters);
	}

	@Override
	public BaseComponent getAssembly() {
		return discardAssembly;
	}
}