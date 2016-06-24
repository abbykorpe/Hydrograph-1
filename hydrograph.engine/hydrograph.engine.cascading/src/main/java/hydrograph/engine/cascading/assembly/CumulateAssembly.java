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

import hydrograph.engine.assembly.entity.CumulateEntity;
import hydrograph.engine.assembly.entity.elements.KeyField;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.utils.OutSocketUtils;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.handlers.CumulateCustomHandler;
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

public class CumulateAssembly extends BaseComponent<CumulateEntity> {

	private static final long serialVersionUID = 8050470302089972525L;
	private CumulateEntity cumulateEntity;
	private static Logger LOG = LoggerFactory.getLogger(CumulateAssembly.class);
	private OperationFieldsCreator<CumulateEntity> operationFieldsCreator;

	public CumulateAssembly(CumulateEntity assemblyEntityBase,
			ComponentParameters parameters) {
		super(assemblyEntityBase, parameters);
	}

	@Override
	protected void createAssembly() {
		try {
			if (LOG.isTraceEnabled()) {
				LOG.trace(cumulateEntity.toString());
			}
			for (OutSocket outSocket : cumulateEntity.getOutSocketList()) {
				LOG.trace("Creating cumulate assembly for '"
						+ cumulateEntity.getComponentId() + "' for socket: '"
						+ outSocket.getSocketId() + "' of type: '"
						+ outSocket.getSocketType() + "'");
				operationFieldsCreator = new OperationFieldsCreator<CumulateEntity>(
						cumulateEntity, componentParameters, outSocket);
				LOG.debug("Cumulate Assembly: [ InputFields List : "
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

		Pipe scanSortPipe = new Pipe("cumulate:"+cumulateEntity.getComponentId() + "_"+outSocket.getSocketId(),
				componentParameters.getInputPipe());

		// perform groupby operation on keys
		Fields keyFields = getFieldsFromKeyFields(cumulateEntity.getKeyFields());
		Fields secondaryKeyFields = getFieldsFromKeyFields(cumulateEntity
				.getSecondaryKeyFields());

		scanSortPipe = new GroupBy(scanSortPipe, keyFields, secondaryKeyFields);

		// get the object of fieldmanupulating handler to handle i/o fields
		FieldManupulatingHandler fieldManupulatingHandler = new FieldManupulatingHandler(
				operationFieldsCreator.getOperationalInputFieldsList(),
				operationFieldsCreator.getOperationalOutputFieldsList(),
				keyFields, passThroughFields, mapFields, operationFields);

		CumulateCustomHandler scanHandler = new CumulateCustomHandler(
				fieldManupulatingHandler,
				operationFieldsCreator.getOperationalOperationPropertiesList(),
				operationFieldsCreator.getOperationalTransformClassList());

		setHadoopProperties(scanSortPipe.getStepConfigDef());

		scanSortPipe = new Every(scanSortPipe, scanHandler.getInputFields(),
				scanHandler, Fields.RESULTS);

		setOutLink(outSocket.getSocketType(), outSocket.getSocketId(),
				cumulateEntity.getComponentId(), scanSortPipe,
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
				fields.setComparator(eachField.getName(),
						Collections.reverseOrder());
			}
			i++;
		}
		return fields;
	}

	@Override
	public void initializeEntity(CumulateEntity assemblyEntityBase) {
		this.cumulateEntity = (CumulateEntity) assemblyEntityBase;
	}
}