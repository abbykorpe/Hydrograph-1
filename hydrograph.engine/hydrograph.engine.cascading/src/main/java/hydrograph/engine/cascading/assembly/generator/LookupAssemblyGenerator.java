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
import hydrograph.engine.assembly.entity.LookupEntity;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.cascading.assembly.LookupAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OperationAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.operationstypes.Lookup;

public class LookupAssemblyGenerator extends OperationAssemblyGeneratorBase {

	private LookupEntity lookupEntity;
	private LookupAssembly lookupAssembly;
	private Lookup jaxbLookup;
	private static Logger LOG = LoggerFactory
			.getLogger(LookupAssemblyGenerator.class);

	public LookupAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbLookup = (Lookup) baseComponent;
	}

	@Override
	public void createEntity() {
		lookupEntity = new LookupEntity();
	}

	@Override
	public void initializeEntity() {
		LOG.trace("Initializing hash join entity for component: "
				+ jaxbLookup.getId());
		lookupEntity.setComponentId(jaxbLookup.getId());
		lookupEntity.setBatch(jaxbLookup.getBatch());

		if (jaxbLookup.getOutSocket() == null) {
			throw new NullPointerException("No out socket defined for hash join component: " + jaxbLookup.getId());
		}

		lookupEntity.setKeyFields(
				OperationEntityUtils.extractKeyFieldsListFromOutSocketsForLookup(jaxbLookup.getKeys()));
		lookupEntity.setOutSocketList(OperationEntityUtils.extractOutSocketList(jaxbLookup.getOutSocket()));
//		hashJoinEntity.setInSocketMap(OperationEntityUtils.extractInSocketMap(jaxbHashJoin.getInSocket()));
		lookupEntity.setRuntimeProperties(
				OperationEntityUtils.extractRuntimeProperties(jaxbLookup.getRuntimeProperties()));
		
		if(jaxbLookup.getMatch() != null)
			lookupEntity.setMatch(jaxbLookup.getMatch().getValue().value());
		else
			throw new NullPointerException("No 'match' option set for hash join component: " + jaxbLookup.getId());
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		lookupAssembly = new LookupAssembly(lookupEntity, componentParameters);
	}

	@Override
	public BaseComponent<LookupEntity> getAssembly() {
		return lookupAssembly;
	}

	@Override
	public Map<String, Pipe> getSourcePipe() {
		return null;
	}

}
