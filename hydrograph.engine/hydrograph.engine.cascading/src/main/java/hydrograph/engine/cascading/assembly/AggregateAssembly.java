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

import hydrograph.engine.assembly.entity.AggregateEntity;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.assembly.entity.elements.KeyField;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.utils.OutSocketUtils;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.handlers.AggregateCustomHandler;
import hydrograph.engine.cascading.assembly.handlers.FieldManupulatingHandler;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.OperationFieldsCreator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.pipe.Every;
import cascading.pipe.GroupBy;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AggregateAssembly extends BaseComponent {

	private static final long serialVersionUID = 8050470302089972525L;

	private AggregateEntity aggregateEntity;
	private static Logger LOG = LoggerFactory.getLogger(AggregateAssembly.class);
	private OperationFieldsCreator operationFieldsCreator;

	public AggregateAssembly(AssemblyEntityBase baseComponentEntity, ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	public void castEntityFromBase(AssemblyEntityBase assemblyEntityBase) {
		aggregateEntity = (AggregateEntity) assemblyEntityBase;
	}

	@Override
	protected void createAssembly() {
		try {
			if (LOG.isTraceEnabled()) {
				LOG.trace(aggregateEntity.toString());
			}
			for (OutSocket outSocket : aggregateEntity.getOutSocketList()) {
				LOG.trace("Creating aggregate assembly for '" + aggregateEntity.getComponentId() + "' for socket: '"
						+ outSocket.getSocketId() + "' of type: '" + outSocket.getSocketType() + "'");
				operationFieldsCreator = new OperationFieldsCreator<AssemblyEntityBase>(aggregateEntity,
						componentParameters, outSocket);
				createAssemblyForOutSocket(outSocket);
			}
			LOG.debug("Aggregate Assembly: [ InputFields List : "
					+ Arrays.toString(operationFieldsCreator.getOperationalInputFieldsList().toArray())
					+ ", OperationProperties List : "
					+ Arrays.toString(operationFieldsCreator.getOperationalOperationPropertiesList().toArray())
					+ ", OutputFieldsList : "
					+ Arrays.toString(operationFieldsCreator.getOperationalOutputFieldsList().toArray())
					+ " , TransformClass List : "
					+ Arrays.toString(operationFieldsCreator.getOperationalTransformClassList().toArray())
					+ ", PassThrough Fields : " + operationFieldsCreator.getPassThroughFields() + "]");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	private void createAssemblyForOutSocket(OutSocket outSocket) {
		// initialize the out socket fields
		Fields passThroughFields = operationFieldsCreator.getPassThroughFields();
		Map<String, String> mapFields = OutSocketUtils.getMapFieldsFromOutSocket(outSocket.getMapFieldsList());
		Fields operationFields = new Fields(
				OutSocketUtils.getOperationFieldsFromOutSocket(outSocket.getOperationFieldList()));

		Pipe sortAggPipe = new Pipe(aggregateEntity.getComponentId() + "_out", componentParameters.getInputPipe());

		// perform groupby operation on keys
		Fields keyFields = getFieldsFromKeyFields(aggregateEntity.getKeyFields());
		Fields secondaryKeyFields = getFieldsFromKeyFields(aggregateEntity.getSecondaryKeyFields());

		sortAggPipe = new GroupBy(sortAggPipe, keyFields, secondaryKeyFields);

		FieldManupulatingHandler fieldManupulatingHandler = new FieldManupulatingHandler(
				operationFieldsCreator.getOperationalInputFieldsList(),
				operationFieldsCreator.getOperationalOutputFieldsList(), keyFields, passThroughFields, mapFields,
				operationFields);

		AggregateCustomHandler groupingHandler = new AggregateCustomHandler(fieldManupulatingHandler,
				operationFieldsCreator.getOperationalOperationPropertiesList(),
				operationFieldsCreator.getOperationalTransformClassList());
		setHadoopProperties(sortAggPipe.getStepConfigDef());
		sortAggPipe = new Every(sortAggPipe, groupingHandler.getInputFields(), groupingHandler, Fields.RESULTS);

		setOutLink(outSocket.getSocketType(), outSocket.getSocketId(), aggregateEntity.getComponentId(), sortAggPipe,
				fieldManupulatingHandler.getOutputFields());
	}

	/**
	 * Creates an object of type {@link Fields} from array of {@link KeyField}
	 * 
	 * @param keyFields
	 *            an array of {@link KeyField} containing the field name and
	 *            sort order
	 * @return an object of type {@link Fields}
	 */
	private Fields getFieldsFromKeyFields(KeyField[] keyFields) {
		if (keyFields == null) {
			return Fields.NONE;
		}

		String[] fieldNames = new String[keyFields.length];
		int i = 0;
		for (KeyField eachField : keyFields) {
			fieldNames[i] = eachField.getName();
			i++;
		}

		Fields fields = new Fields(fieldNames);
		i = 0;
		for (KeyField eachField : keyFields) {
			if (eachField.getSortOrder().equalsIgnoreCase("desc")) {
				fields.setComparator(eachField.getName(), Collections.reverseOrder());
			}
			i++;
		}
		return fields;
	}
}