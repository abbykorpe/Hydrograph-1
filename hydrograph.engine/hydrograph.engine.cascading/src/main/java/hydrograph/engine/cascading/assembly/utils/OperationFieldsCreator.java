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
package hydrograph.engine.cascading.assembly.utils;

import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tuple.Fields;
import hydrograph.engine.assembly.entity.base.OperationAssemblyEntity;
import hydrograph.engine.assembly.entity.elements.Operation;
import hydrograph.engine.assembly.entity.elements.OperationField;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.assembly.entity.utils.OutSocketUtils;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;

public class OperationFieldsCreator<T extends OperationAssemblyEntity> {

	private ArrayList<Fields> inputFieldsList;
	private ArrayList<Fields> outputFieldsList;
	private ArrayList<String> transformClassList;
	private ArrayList<Properties> operationPropertiesList;
	private Fields initPassThroughFields;
	private boolean isOperationExistinOperationField;
	private OperationAssemblyEntity assemblyEntityBase;
	private OutSocket outSocket;
	private ComponentParameters componentParameters;

	private static Logger LOG = LoggerFactory.getLogger(OperationFieldsCreator.class);

	/**
	 * OperationFieldsCreator class is used to initialize passthrough fields
	 * and operation fields
	 * 
	 * @param entity
	 * @param componentParameters
	 * @param outSocket
	 */
	public OperationFieldsCreator(T entity, ComponentParameters componentParameters, OutSocket outSocket) {
		inputFieldsList = new ArrayList<Fields>();
		outputFieldsList = new ArrayList<Fields>();
		transformClassList = new ArrayList<String>();
		operationPropertiesList = new ArrayList<Properties>();

		this.assemblyEntityBase = entity;
		this.componentParameters = componentParameters;
		this.outSocket = outSocket;

		// initialization
		initPassThroughFields();
		initializeOperationFieldsForOutSocket();
	}

	private Fields initPassThroughFields() {
		String[] passThroughFields = OutSocketUtils.getPassThroughFieldsFromOutSocket(
				outSocket.getPassThroughFieldsList(), componentParameters.getInputFields());
		if (passThroughFields != null && passThroughFields.length == 0 && !assemblyEntityBase.isOperationPresent()) {
			return initPassThroughFields = componentParameters.getInputFields();
		} else {
			return initPassThroughFields = new Fields(passThroughFields);
		}
	}

	private void initializeOperationFieldsForOutSocket() {

		// check if the operation component has operation(s)
		if (assemblyEntityBase.isOperationPresent()) {
			for (Operation eachOperation : assemblyEntityBase.getOperationsList()) {
				if (isOperationIDExistsInOperationFields(eachOperation.getOperationId(), outSocket)) {
					inputFieldsList.add(new Fields(eachOperation.getOperationInputFields() == null ? new String[0]
							: eachOperation.getOperationInputFields()));
					outputFieldsList.add(new Fields(eachOperation.getOperationOutputFields() == null ? new String[0]
							: eachOperation.getOperationOutputFields()));
					transformClassList.add(eachOperation.getOperationClass());
					operationPropertiesList.add(eachOperation.getOperationProperties());

				} else {
					LOG.info("Operation: '" + eachOperation.getOperationId() + "' of component Id '"
							+ assemblyEntityBase.getComponentId() + "' not used in out socket");
				}
			}
		}
	}

	private boolean isOperationIDExistsInOperationFields(String operationId, OutSocket outSocket) {
		for (OperationField eachOperationField : outSocket.getOperationFieldList()) {
			if (eachOperationField.getOperationId().equals(operationId)) {
				return isOperationExistinOperationField = true;
			}
		}
		return isOperationExistinOperationField = false;
	}

	/**
	 * @return inputFieldsList
	 */
	public ArrayList<Fields> getOperationalInputFieldsList() {
		return inputFieldsList;
	}

	/**
	 * @return outputFieldsList
	 */
	public ArrayList<Fields> getOperationalOutputFieldsList() {
		return outputFieldsList;
	}

	/**
	 * @return transformClassList
	 */
	public ArrayList<String> getOperationalTransformClassList() {
		return transformClassList;
	}

	/**
	 * @return operationPropertiesList
	 */
	public ArrayList<Properties> getOperationalOperationPropertiesList() {
		return operationPropertiesList;
	}

	/**
	 * @return PassThroughFields
	 */
	public Fields getPassThroughFields() {
		return initPassThroughFields;
	}

	/**
	 * @return boolean value indicating whether operation field is present or
	 *         not
	 */
	public boolean checkIfOperationExistsInOperationFields() {
		return isOperationExistinOperationField;
	}

}