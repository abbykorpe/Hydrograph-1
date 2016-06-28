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
package hydrograph.engine.cascading.assembly;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;
import hydrograph.engine.assembly.entity.NormalizeEntity;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.utils.OutSocketUtils;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.handlers.FieldManupulatingHandler;
import hydrograph.engine.cascading.assembly.handlers.NormalizeCustomHandler;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.OperationFieldsCreator;
import hydrograph.engine.utilities.ComponentHelper;

public class NormalizeAssembly extends BaseComponent<NormalizeEntity> {

	private static final long serialVersionUID = 3161412718941460364L;

	private static Logger LOG = LoggerFactory
			.getLogger(NormalizeAssembly.class);
	private NormalizeEntity normalizeEntity;
	private OperationFieldsCreator<NormalizeEntity> operationFieldsCreator;

	public NormalizeAssembly(NormalizeEntity baseComponentEntity,
			ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	protected void createAssembly() {
		try {
			if (LOG.isTraceEnabled()) {
				LOG.trace(normalizeEntity.toString());
			}
			for (OutSocket outSocket : normalizeEntity.getOutSocketList()) {
				LOG.trace("Creating normalize assembly for '"
						+ normalizeEntity.getComponentId() + "' for socket: '"
						+ outSocket.getSocketId() + "' of type: '"
						+ outSocket.getSocketType() + "'");
				operationFieldsCreator = new OperationFieldsCreator<NormalizeEntity>(
						normalizeEntity, componentParameters, outSocket);
				LOG.debug("Normalize Assembly: [ InputFields List : "
						+ Arrays.toString(operationFieldsCreator
								.getOperationalInputFieldsList().toArray())
						+ ", OperationProperties List : "
						+ Arrays.toString(operationFieldsCreator
								.getOperationalOperationPropertiesList()
								.toArray())
						+ ", OutputFieldsList : "
						+ Arrays.toString(operationFieldsCreator
								.getOperationalOutputFieldsList().toArray())
						+ " , TransformClass List : "
						+ Arrays.toString(operationFieldsCreator
								.getOperationalTransformClassList().toArray())
						+ ", PassThrough Fields : "
						+ operationFieldsCreator.getPassThroughFields() + "]");
				createAssemblyForOutSocket(outSocket);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	private void createAssemblyForOutSocket(OutSocket outSocket) {
		// initialize the out socket fields
		Fields passThroughFields = operationFieldsCreator
				.getPassThroughFields();
		Map<String, String> mapFields = OutSocketUtils
				.getMapFieldsFromOutSocket(outSocket.getMapFieldsList());
		Fields operationFields = new Fields(
				OutSocketUtils.getOperationFieldsFromOutSocket(outSocket
						.getOperationFieldList()));

		FieldManupulatingHandler fieldManupulatingHandler = new FieldManupulatingHandler(
				operationFieldsCreator.getOperationalInputFieldsList(),
				operationFieldsCreator.getOperationalOutputFieldsList(),
				passThroughFields, mapFields, operationFields);
		NormalizeCustomHandler normalizeCustomHandler = null;

		Pipe normalizePipe = new Pipe(ComponentHelper.getComponentName("normalize",
				normalizeEntity.getComponentId(),outSocket.getSocketId()),
				componentParameters.getInputPipe());

		normalizeCustomHandler = new NormalizeCustomHandler(
				fieldManupulatingHandler,
				operationFieldsCreator.getOperationalOperationPropertiesList(),
				operationFieldsCreator.getOperationalTransformClassList());

		setHadoopProperties(normalizePipe.getStepConfigDef());

		normalizePipe = new Each(normalizePipe,
				normalizeCustomHandler.getInputFields(),
				normalizeCustomHandler, Fields.RESULTS);

		setOutLink(outSocket.getSocketType(), outSocket.getSocketId(),
				normalizeEntity.getComponentId(), normalizePipe,
				fieldManupulatingHandler.getOutputFields());
	}

	@Override
	public void initializeEntity(NormalizeEntity assemblyEntityBase) {
		this.normalizeEntity=assemblyEntityBase;
	}

}