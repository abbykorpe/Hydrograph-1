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

package hydrograph.ui.engine.ui.converter;

import hydrograph.ui.common.component.config.Operations;
import hydrograph.ui.common.component.config.TypeInfo;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.ParameterUtil;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.OperationClassProperty;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;
import hydrograph.ui.engine.constants.PropertyNameConstants;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.helper.ConverterUiHelper;
import hydrograph.ui.engine.ui.repository.UIComponentRepo;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.messages.Messages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.engine.jaxb.commontypes.TypeBaseField;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeInputField;
import hydrograph.engine.jaxb.commontypes.TypeMapField;
import hydrograph.engine.jaxb.commontypes.TypeOperationsComponent;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.commontypes.TypeProperties.Property;
import hydrograph.engine.jaxb.commontypes.TypeTransformOperation;
import com.thoughtworks.xstream.mapper.AttributeMapper;

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
	protected TransformMapping getUiPassThroughOrOperationFieldsOrMapFieldGrid(
			TypeOperationsOutSocket outSocket) {
		TransformMapping atMapping = new TransformMapping();
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

	private void getOperationData(TransformMapping atMapping) {
		List<TypeTransformOperation> typeTransformOpertaionList = ((TypeOperationsComponent) typeBaseComponent)
				.getOperation();

		List<MappingSheetRow> mappingSheetRows = atMapping
				.getMappingSheetRows();
		for (TypeTransformOperation item : typeTransformOpertaionList) {

			mappingSheetRows.add(new MappingSheetRow(getInputFieldList(item),
					getOutputFieldList(item),
					getOperationClassName(item.getClazz()),item.getClazz(),
					ParameterUtil.isParameter(item.getClazz()),item.getId(),getProperties(item))
				   );
      }

	}

	protected String getOperationClassName(String fullClassPath) {
		String operationClassName = Messages.CUSTOM;
		Operations operations = XMLConfigUtil.INSTANCE.getComponent(uiComponent.getComponentName()).getOperations();
		List<TypeInfo> typeInfos = operations.getStdOperation();
		if (StringUtils.isNotBlank(fullClassPath) && !ParameterUtil.isParameter(fullClassPath)) {
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

	private List<NameValueProperty> getProperties(TypeTransformOperation item)
	{
		List<NameValueProperty> nameValueProperties=new LinkedList<>();
		if(item!=null && item.getProperties()!=null)
		{
			for(Property property:item.getProperties().getProperty())
			{
				NameValueProperty nameValueProperty=new NameValueProperty();
				nameValueProperty.setPropertyName(property.getName());
				nameValueProperty.setPropertyValue(property.getValue());
				nameValueProperties.add(nameValueProperty);
			}	
		}	
		return nameValueProperties;
	}
	
	
	
	private List<FilterProperties> getOutputFieldList(TypeTransformOperation item) {
		List<FilterProperties> outputFieldList = new LinkedList<>();
		if(item !=null ){
			ConverterUiHelper converterUiHelper = new ConverterUiHelper(uiComponent);
			List<GridRow> gridRow = new ArrayList<>();
			if (item.getOutputFields() != null) {

				for (TypeBaseField record : item.getOutputFields().getField()) {
					gridRow.add(converterUiHelper.getFixedWidthSchema(record));
					FilterProperties filterProperties=new FilterProperties();
					filterProperties.setPropertyname(record.getName());
					outputFieldList.add(filterProperties);
				}

			}
		}
		return outputFieldList;
	}

	private List<FilterProperties> getInputFieldList(TypeTransformOperation item) {
		List<FilterProperties> inputfieldList = new LinkedList<>();
		if(item != null && item.getInputFields()!=null){
			for (TypeInputField inputField : item.getInputFields().getField()) {
				FilterProperties filterProperties=new FilterProperties();
				filterProperties.setPropertyname(inputField.getName());
				inputfieldList.add(filterProperties);
			}
		}		
		return inputfieldList;
	}
	
	private List<NameValueProperty> getMapAndPassThroughField()
	{
		List<TypeOperationsOutSocket> xsdOpertaionList = ((TypeOperationsComponent) typeBaseComponent)
				.getOutSocket();
		List<NameValueProperty> nameValueproperties=new LinkedList<>();
		
		for(TypeOperationsOutSocket typeOperationsOutSocket:xsdOpertaionList)
		{
			for (Object property : typeOperationsOutSocket
					.getPassThroughFieldOrOperationFieldOrMapField())
			{
				NameValueProperty nameValueProperty=new NameValueProperty();
				if(property instanceof TypeInputField)
				{
					nameValueProperty.setPropertyName(((TypeInputField) property).getName());
					nameValueProperty.setPropertyValue(((TypeInputField) property).getName());
					nameValueproperties.add(nameValueProperty);
				}
				else if(property instanceof TypeMapField )
				{
					nameValueProperty.setPropertyName(((TypeMapField) property).getSourceName());
					nameValueProperty.setPropertyValue(((TypeMapField) property).getName());
					nameValueproperties.add(nameValueProperty);
				}
			}	
		}
		return nameValueproperties;
	}

	protected Object createTransformPropertyGrid() {
		TransformMapping atMapping = new TransformMapping();
        getOperationData(atMapping);
		atMapping.setMapAndPassthroughField(getMapAndPassThroughField());
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