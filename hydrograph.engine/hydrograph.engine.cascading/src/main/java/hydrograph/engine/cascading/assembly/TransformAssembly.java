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
import java.util.HashMap;
import java.util.Map;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.handlers.FieldManupulatingHandler;
import hydrograph.engine.cascading.assembly.handlers.TransformCustomHandler;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.OperationFieldsCreator;
import hydrograph.engine.core.component.entity.TransformEntity;
import hydrograph.engine.core.component.entity.elements.OutSocket;
import hydrograph.engine.core.component.entity.elements.SchemaField;
import hydrograph.engine.core.component.entity.utils.OutSocketUtils;
import hydrograph.engine.expression.api.ValidationAPI;
import hydrograph.engine.utilities.ComponentHelper;

public class TransformAssembly extends BaseComponent<TransformEntity> {

	private static final long serialVersionUID = 8050470302089972525L;
	private TransformEntity transformEntity;
	private static Logger LOG = LoggerFactory.getLogger(TransformAssembly.class);
	private OperationFieldsCreator<TransformEntity> operationFieldsCreator;

	public TransformAssembly(TransformEntity baseComponentEntity, ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	protected void createAssembly() {

		if (LOG.isTraceEnabled()) {
			LOG.trace(transformEntity.toString());
		}
		for (OutSocket outSocket : transformEntity.getOutSocketList()) {
			LOG.trace("Creating transform assembly for '" + transformEntity.getComponentId() + "' for socket: '"
					+ outSocket.getSocketId() + "' of type: '" + outSocket.getSocketType() + "'");
			operationFieldsCreator = new OperationFieldsCreator<TransformEntity>(transformEntity,
					componentParameters, outSocket);
			createAssemblyForOutSocket(outSocket);
		}
		LOG.debug("Transform Assembly: [ InputFields List : "
				+ Arrays.toString(operationFieldsCreator.getOperationalInputFieldsList().toArray())
				+ ", OperationProperties List : "
				+ Arrays.toString(operationFieldsCreator.getOperationalOperationPropertiesList().toArray())
				+ ", OutputFieldsList : "
				+ Arrays.toString(operationFieldsCreator.getOperationalOutputFieldsList().toArray())
				+ " , TransformClass List : "
				+ Arrays.toString(operationFieldsCreator.getOperationalTransformClassList().toArray())
				+ ", PassThrough Fields : " + operationFieldsCreator.getPassThroughFields() + "]");
	}

	protected void createAssemblyForOutSocket(OutSocket outSocket) {
		Pipe transformPipe = new Pipe(transformEntity.getComponentId()+outSocket.getSocketId(), componentParameters.getInputPipe());

		// initialize the out socket fields
		Fields passThroughFields = operationFieldsCreator.getPassThroughFields();
		Map<String, String> mapFields = OutSocketUtils.getMapFieldsFromOutSocket(outSocket.getMapFieldsList());
		Fields operationFields = new Fields(
				OutSocketUtils.getOperationFieldsFromOutSocket(outSocket.getOperationFieldList()));

		FieldManupulatingHandler fieldManupulatingHandler = new FieldManupulatingHandler(
				operationFieldsCreator.getOperationalInputFieldsList(),
				operationFieldsCreator.getOperationalOutputFieldsList(), passThroughFields, mapFields, operationFields);

		TransformCustomHandler transfromHandler = new TransformCustomHandler(fieldManupulatingHandler,
				operationFieldsCreator.getOperationalOperationPropertiesList(),
				operationFieldsCreator.getOperationalTransformClassList(),operationFieldsCreator.getOperationalExpressionList());

		setHadoopProperties(transformPipe.getStepConfigDef());

		transformPipe = new Each(transformPipe, transfromHandler.getInputFields(), transfromHandler, Fields.RESULTS);

		setOutLink(outSocket.getSocketType(), outSocket.getSocketId(), transformEntity.getComponentId(), transformPipe,
				transfromHandler.getOutputFields());

	}
	

	@Override
	public void initializeEntity(TransformEntity assemblyEntityBase) {
		this.transformEntity=assemblyEntityBase;
	}

}