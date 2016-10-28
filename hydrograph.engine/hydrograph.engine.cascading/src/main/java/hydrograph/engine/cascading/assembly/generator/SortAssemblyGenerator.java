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
/**
 * 
 */
package hydrograph.engine.cascading.assembly.generator;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Pipe;
import hydrograph.engine.assembly.entity.SortEntity;
import hydrograph.engine.assembly.entity.utils.StraightPullEntityUtils;
import hydrograph.engine.cascading.assembly.SortAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.StraightPullAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.straightpulltypes.Sort;

/**
 * @author Prabodh
 *
 */
public class SortAssemblyGenerator extends StraightPullAssemblyGeneratorBase {

	SortAssembly sortAssembly;
	SortEntity sortEntity;
	Sort jaxbSort;
	private static Logger LOG = LoggerFactory.getLogger(SortAssemblyGenerator.class);

	public SortAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hydrograph.engine.cascading.assembly.generator.base.
	 * StraightPullAssemblyGeneratorBase #getPipes()
	 */
	@Override
	public Map<String, Pipe> getPipes() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hydrograph.engine.cascading.assembly.generator.base.AssemblyGeneratorBase#
	 * castComponentFromBase
	 * (hydrograph.engine.graph.commontypes.TypeBaseComponent)
	 */
	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbSort = (Sort) baseComponent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hydrograph.engine.cascading.assembly.generator.base.AssemblyGeneratorBase#
	 * createEntity ()
	 */
	@Override
	public void createEntity() {
		sortEntity = new SortEntity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hydrograph.engine.cascading.assembly.generator.base.AssemblyGeneratorBase#
	 * initializeEntity()
	 */
	@Override
	public void initializeEntity() {

		LOG.trace("Initializing sort entity for component: " + jaxbSort.getId());
		sortEntity.setComponentId(jaxbSort.getId());
		sortEntity.setBatch(jaxbSort.getBatch());
		sortEntity.setComponentName(jaxbSort.getName());
		sortEntity.setRuntimeProperties(
				StraightPullEntityUtils.extractRuntimeProperties(jaxbSort.getRuntimeProperties()));
		sortEntity.setKeyFields(StraightPullEntityUtils.extractKeyFields(jaxbSort.getPrimaryKeys()));
		sortEntity
				.setSecondaryKeyFields(StraightPullEntityUtils.extractSecondaryKeyFields(jaxbSort.getSecondaryKeys()));

		if (jaxbSort.getOutSocket() == null) {
			throw new NullPointerException("No out socket defined for sort component: " + jaxbSort.getId());
		}

		sortEntity.setOutSocketList(StraightPullEntityUtils.extractOutSocketList(jaxbSort.getOutSocket()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hydrograph.engine.cascading.assembly.generator.base.AssemblyGeneratorBase#
	 * createAssembly
	 * (hydrograph.engine.cascading.assembly.infra.ComponentParameters)
	 */
	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		sortAssembly = new SortAssembly(sortEntity, componentParameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hydrograph.engine.cascading.assembly.generator.base.AssemblyGeneratorBase#
	 * getAssembly ()
	 */
	@Override
	public BaseComponent<SortEntity> getAssembly() {
		return sortAssembly;
	}
}