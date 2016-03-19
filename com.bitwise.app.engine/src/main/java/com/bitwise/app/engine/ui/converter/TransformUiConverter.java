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

 
package com.bitwise.app.engine.ui.converter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.bitwise.app.common.component.config.Operations;
import com.bitwise.app.common.component.config.TypeInfo;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.common.datastructure.property.mapping.ATMapping;
import com.bitwise.app.common.datastructure.property.mapping.MappingSheetRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.helper.ConverterUiHelper;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeMapField;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.commontypes.TypeProperties.Property;
import com.bitwiseglobal.graph.commontypes.TypeTransformOperation;

/**
 * The class TransformUiConverter
 * 
 * @author Bitwise
 * 
 */
public abstract class TransformUiConverter extends UiConverter {
	private static final Logger LOGGER = LogFactory.INSTANCE
			.getLogger(TransformUiConverter.class);
	private Schema schema;

	/**
	 * Generate common properties of transform-component that will appear in
	 * property window.
	 * 
	 */
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching common properties for -{}", componentName);
		getInPort((TypeOperationsComponent) typeBaseComponent);
		getOutPort((TypeOperationsComponent) typeBaseComponent);
		uiComponent.setCategory(UIComponentsConstants.TRANSFORM_CATEGORY
				.value());
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(),
				getRuntimeProperties());
	}

	/**
	 * Create input ports for transform component.
	 * 
	 * @param TypeOperationsComponent
	 *            the operationsComponent
	 * 
	 */
	protected void getInPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating InPut Ports for -{}", componentName);
		if (operationsComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : operationsComponent.getInSocket()) {
				uiComponent.engageInputPort(inSocket.getId());
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(),
								operationsComponent.getId(), inSocket
										.getFromSocketId(), inSocket.getId()));
			}
		}
	}

	/**
	 * Create output ports for transform component.
	 * 
	 * @param TypeOperationsComponent
	 *            the operationsComponent
	 * 
	 */
	protected void getOutPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating OutPut Ports for -{}", componentName);
		if (operationsComponent.getOutSocket() != null) {
			for (TypeOperationsOutSocket outSocket : operationsComponent
					.getOutSocket()) {
				uiComponent.engageOutputPort(outSocket.getId());
				if (outSocket.getPassThroughFieldOrOperationFieldOrMapField() != null)
					propertyMap
							.put(Constants.PARAM_OPERATION,
									getUiPassThroughOrOperationFieldsOrMapFieldGrid(outSocket));
			}

		}
	}

	/**
	 * Create transform property grid for transform component.
	 * 
	 * @param TypeOperationsOutSocket
	 *            the outSocket
	 * 
	 * @return TransformPropertyGrid, transformPropertyGrid object which is
	 *         responsible to display transform grid.
	 */
	protected ATMapping getUiPassThroughOrOperationFieldsOrMapFieldGrid(
			TypeOperationsOutSocket outSocket) {
		ATMapping atMapping = new ATMapping();
		List<MappingSheetRow> mappingSheetRows = new LinkedList<>();

		for (Object property : outSocket
				.getPassThroughFieldOrOperationFieldOrMapField()) {
			MappingSheetRow mappingSheetRow = null;
			if (property instanceof TypeInputField) {

				List inputFieldList = new LinkedList<>();
				inputFieldList.add(((TypeInputField) property).getName());

				List outputFieldList = new LinkedList<>();
				outputFieldList.add(((TypeInputField) property).getName());

				mappingSheetRow = new MappingSheetRow(inputFieldList, null,
						outputFieldList);
				mappingSheetRows.add(mappingSheetRow);

			} else if (property instanceof TypeMapField) {
				List inputFieldList = new LinkedList<>();
				inputFieldList.add(((TypeMapField) property).getSourceName());

				List outputFieldList = new LinkedList<>();
				outputFieldList.add(((TypeMapField) property).getName());

				mappingSheetRow = new MappingSheetRow(inputFieldList, null,
						outputFieldList);
				mappingSheetRows.add(mappingSheetRow);
			}

		}
		atMapping.setMappingSheetRows(mappingSheetRows);
		return atMapping;

	}

	private void getOperationData(ATMapping atMapping) {
		List<TypeTransformOperation> xsdOpertaionList = ((TypeOperationsComponent) typeBaseComponent)
				.getOperation();

		List<MappingSheetRow> mappingSheetRows = atMapping
				.getMappingSheetRows();
		for (TypeTransformOperation item : xsdOpertaionList) {

			mappingSheetRows.add(new MappingSheetRow(getInputFieldList(item),
					new OperationClassProperty(getOperationClassName(item.getClazz()),item.getClazz(),
							isParameter(item.getClazz()), item.getClazz()),
					getOutputFieldList(item)));
		}

	}

	protected String getOperationClassName(String fullClassPath) {
		String operationClassName = Messages.CUSTOM;
		Operations operations = XMLConfigUtil.INSTANCE.getComponent(uiComponent.getComponentName()).getOperations();
		List<TypeInfo> typeInfos = operations.getStdOperation();
		if (StringUtils.isNotBlank(fullClassPath) && !isParameter(fullClassPath)) {
			String str[] = fullClassPath.split("\\.");
			for (int i = 0; i < typeInfos.size(); i++) {
				if(typeInfos.get(i).getName().equalsIgnoreCase(str[str.length - 1]))
				{
					operationClassName = str[str.length - 1];
				}
			}
		}
		return operationClassName;
	}

	private List<String> getOutputFieldList(TypeTransformOperation item) {
		List<String> outputFieldList = new LinkedList<>();
		if(item !=null ){
			ConverterUiHelper converterUiHelper = new ConverterUiHelper(uiComponent);
			List<GridRow> gridRow = new ArrayList<>();
			if (item.getOutputFields() != null) {
				if (schema == null) {
					schema = new Schema();
					schema.setGridRow(gridRow);
					schema.setIsExternal(false);
				}
				
				for (TypeBaseField record : item.getOutputFields().getField()) {
					gridRow.add(converterUiHelper.getFixedWidthSchema(record));
					outputFieldList.add(record.getName());
				}

			}
			propertyMap.put("schema",schema);
		}
		return outputFieldList;
	}

	private List<String> getInputFieldList(TypeTransformOperation item) {
		List<String> inputfieldList = new LinkedList<>();
		if(item != null && item.getInputFields()!=null){
			for (TypeInputField inputField : item.getInputFields().getField()) {
				inputfieldList.add(inputField.getName());
			}
		}		
		return inputfieldList;
	}

	protected Object createTransformPropertyGrid() {
		ATMapping atMapping = null;
		if (propertyMap.get(Constants.PARAM_OPERATION) != null)
			atMapping = (ATMapping) propertyMap.get(Constants.PARAM_OPERATION);
		else
			atMapping = new ATMapping();

		getOperationData(atMapping);
		return atMapping;
	}

	/**
	 * Generate runtime properties for component.
	 * 
	 * @return Map<String,String>
	 */
	@Override
	protected Map<String, String> getRuntimeProperties() {
		LOGGER.debug("Generating Runtime Properties for -{}", componentName);
		TreeMap<String, String> runtimeMap = null;
		TypeProperties typeProperties = ((TypeOperationsComponent) typeBaseComponent)
				.getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new TreeMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(),
						runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}

}
