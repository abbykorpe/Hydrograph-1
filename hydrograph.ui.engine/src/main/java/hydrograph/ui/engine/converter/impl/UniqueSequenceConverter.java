/********************************************************************************
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
 ******************************************************************************/

 
package hydrograph.ui.engine.converter.impl;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.engine.converter.TransformConverter;
import hydrograph.ui.engine.helper.ConverterHelper;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.bitwiseglobal.graph.commontypes.FieldDataTypes;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeOperationField;
import com.bitwiseglobal.graph.commontypes.TypeOperationOutputFields;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;
import com.bitwiseglobal.graph.operationstypes.GenerateSequence;

/**
 * This class is used to create target XML for UniqueSequence component.
 * 
 * @author Bitwise
 * 
 */
public class UniqueSequenceConverter extends TransformConverter {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(UniqueSequenceConverter.class);
	private String defaultOperationId = "opt1";
	private String newFieldName;

	public UniqueSequenceConverter(Component component) {
		super();
		this.baseComponent = new GenerateSequence();
		this.component = component;
		this.properties = component.getProperties();
		converterHelper = new ConverterHelper(component);
		newFieldName = (String) properties.get(Constants.UNIQUE_SEQUENCE_PROPERTY_NAME);
	}

	/* *
	 * This method initiates target XML generation of UniqueSequence component.
	 */
	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();
		List<TypeTransformOperation> operationsList = null;
		GenerateSequence generateSequence = (GenerateSequence) baseComponent;
		operationsList = getOperations();
		if (operationsList != null)
			generateSequence.getOperation().addAll(operationsList);
	}

	/* *
	 * This method creates operation field in target XML under UniqueSequence component.
	 */
	@Override
	protected List<TypeTransformOperation> getOperations() {
		logger.debug("Generating TypeTransformOperation data :{}", properties.get(Constants.PARAM_NAME));
		List<TypeTransformOperation> operationList = null;
		if (StringUtils.isNotBlank(newFieldName)) {
			operationList = new ArrayList<>();
			TypeTransformOperation operation = new TypeTransformOperation();
			operation.setId(defaultOperationId);
			operation.setOutputFields(getOutPutFields());
			operationList.add(operation);
		}
		return operationList;
	}

	private TypeOperationOutputFields getOutPutFields() {
		TypeOperationOutputFields operationOutputFields = new TypeOperationOutputFields();
		TypeBaseField baseField = new TypeBaseField();
		baseField.setName(newFieldName);
		baseField.setType(FieldDataTypes.JAVA_LANG_INTEGER);
		operationOutputFields.getField().add(baseField);
		return operationOutputFields;
	}

	/* *
	 * Generates output socket of UniqueSequence component in target XML.
	 */
	@Override
	protected List<TypeOperationsOutSocket> getOutSocket() {
		logger.debug("Generating TypeOperationsOutSocket data for : {}", properties.get(Constants.PARAM_NAME));
		List<TypeOperationsOutSocket> outSocketList = new ArrayList<TypeOperationsOutSocket>();
		if (component.getSourceConnections() != null) {
			for (Link link : component.getSourceConnections()) {
				TypeOperationsOutSocket outSocket = new TypeOperationsOutSocket();
				outSocket.setId(link.getSourceTerminal());
				setOutSocketProperties(outSocket);
				outSocket.getOtherAttributes();
				outSocketList.add(outSocket);
			}
		}
		return outSocketList;
	}

	private void setOutSocketProperties(TypeOperationsOutSocket outSocket) {
		TypeInputField inputField = new TypeInputField();
		inputField.setInSocketId(Constants.FIXED_INSOCKET_ID);
		inputField.setName(Constants.ADD_ALL_FIELDS_SYMBOL);
		outSocket.getPassThroughFieldOrOperationFieldOrMapField().add(inputField);
		if (StringUtils.isNotBlank(newFieldName)) {
			TypeOperationField operationField = new TypeOperationField();
			operationField.setName(newFieldName);
			operationField.setOperationId(defaultOperationId);
			outSocket.getPassThroughFieldOrOperationFieldOrMapField().add(operationField);
		}
	}

	/**
	 * Generates input socket of UniqueSequence component in target XML.
	 */
	@Override
	public List<TypeBaseInSocket> getInSocket() {
		return converterHelper.getInSocket();
	}

}
