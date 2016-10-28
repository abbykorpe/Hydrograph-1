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
import hydrograph.engine.assembly.entity.CumulateEntity;
import hydrograph.engine.assembly.entity.utils.OperationEntityUtils;
import hydrograph.engine.cascading.assembly.CumulateAssembly;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.generator.base.OperationAssemblyGeneratorBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.operationstypes.Cumulate;

public class CumulateAssemblyGenerator extends OperationAssemblyGeneratorBase {

	private CumulateEntity cumulateEntity;
	private CumulateAssembly cumulateAssembly;
	private Cumulate jaxbCumulate;
	private static Logger LOG = LoggerFactory.getLogger(CumulateAssemblyGenerator.class);

	public CumulateAssemblyGenerator(TypeBaseComponent baseComponent) {
		super(baseComponent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void castComponentFromBase(TypeBaseComponent baseComponent) {
		jaxbCumulate = (Cumulate) baseComponent;
	}

	@Override
	public Map<String, Pipe> getSourcePipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createEntity() {
		cumulateEntity = new CumulateEntity();
	}

	@Override
	public void initializeEntity() {
		LOG.trace("Initializing cumulate entity for component: " + jaxbCumulate.getId());
		cumulateEntity.setComponentId(jaxbCumulate.getId());
		cumulateEntity.setBatch(jaxbCumulate.getBatch());
		cumulateEntity.setComponentName(jaxbCumulate.getName());

		// check if operation is present
		if (jaxbCumulate.getOperationOrExpression() != null && jaxbCumulate.getOperationOrExpression().size() > 0) {

			LOG.trace("Operation(s) present for cumulate component: " + jaxbCumulate.getId() + ", processing");
			// set the number of operations in the transform component and set
			// operation present to true
			cumulateEntity.setNumOperations(jaxbCumulate.getOperationOrExpression().size());
			cumulateEntity.setOperationPresent(true);
			cumulateEntity.setOperationsList(OperationEntityUtils.extractOperations(jaxbCumulate.getOperationOrExpression()));
		} else {
			LOG.trace("Operation not present for cumulate component: " + jaxbCumulate.getId()
					+ ", skipped operation processing");
			// default the number of operations in the transform component to 0
			// and set operation present to false
			cumulateEntity.setNumOperations(0);
			cumulateEntity.setOperationPresent(false);
		}

		if (jaxbCumulate.getOutSocket() == null) {
			throw new NullPointerException("No out socket defined for cumulate component: " + jaxbCumulate.getId());
		}

		cumulateEntity.setOutSocketList(OperationEntityUtils.extractOutSocketList(jaxbCumulate.getOutSocket()));
		cumulateEntity.setKeyFields(OperationEntityUtils.extractKeyFields(jaxbCumulate.getPrimaryKeys()));
		cumulateEntity
				.setSecondaryKeyFields(OperationEntityUtils.extractSecondaryKeyFields(jaxbCumulate.getSecondaryKeys()));
		cumulateEntity.setRuntimeProperties(
				OperationEntityUtils.extractRuntimeProperties(jaxbCumulate.getRuntimeProperties()));
	}

	@Override
	public void createAssembly(ComponentParameters componentParameters) {
		cumulateAssembly = new CumulateAssembly(cumulateEntity, componentParameters);
	}

	@Override
	public BaseComponent<CumulateEntity> getAssembly() {
		return cumulateAssembly;
	}
}
