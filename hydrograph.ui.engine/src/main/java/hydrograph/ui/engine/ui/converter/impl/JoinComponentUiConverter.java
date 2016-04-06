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

 
package hydrograph.ui.engine.ui.converter.impl;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.JoinConfigProperty;
import hydrograph.ui.datastructure.property.JoinMappingGrid;
import hydrograph.ui.datastructure.property.LookupMapProperty;
import hydrograph.ui.engine.ui.constants.UIComponentsConstants;
import hydrograph.ui.engine.ui.converter.LinkingData;
import hydrograph.ui.engine.ui.converter.TransformUiConverter;
import hydrograph.ui.engine.ui.repository.UIComponentRepo;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.slf4j.Logger;

import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeMapField;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.join.TypeKeyFields;
import com.bitwiseglobal.graph.operationstypes.Join;

public class JoinComponentUiConverter extends TransformUiConverter {

	private Join join;

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(JoinComponentUiConverter.class);
	private int inPortCounter = 0;

	public JoinComponentUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new hydrograph.ui.graph.model.components.Join();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {
		join = (Join) typeBaseComponent;
		super.prepareUIXML();
		LOGGER.debug("Fetching Join-Properties for -{}", componentName);

		propertyMap.put(Constants.UNUSED_AND_INPUT_PORT_COUNT_PROPERTY, String.valueOf(inPortCounter));
		propertyMap.put(Constants.INPUT_PORT_COUNT_PROPERTY, String.valueOf(inPortCounter));
		propertyMap.put(Constants.UNUSED_PORT_COUNT_PROPERTY, String.valueOf(inPortCounter));
		propertyMap.put(Constants.OUTPUT_PORT_COUNT_PROPERTY, String.valueOf(1));
		propertyMap.put(Constants.JOIN_CONFIG_FIELD, getJoinConfigProperty());
		container.getComponentNextNameSuffixes().put(name_suffix, 0);
		container.getComponentNames().add(componentName);

		uiComponent.setProperties(propertyMap);
		uiComponent.setType(UIComponentsConstants.JOIN.value());
		validateComponentProperties(propertyMap);
	}

	private List<JoinConfigProperty> getJoinConfigProperty() {
		List<JoinConfigProperty> joinConfigPropertyList = null;
		JoinConfigProperty joinConfigProperty = null;
		if (join.getKeys() != null && !join.getKeys().isEmpty()) {
			joinConfigPropertyList = new ArrayList<>();
			for (TypeKeyFields typeKeysFields : join.getKeys()) {
				joinConfigProperty = new JoinConfigProperty();
				joinConfigProperty.setRecordRequired(getRecordRequired(typeKeysFields));
				joinConfigProperty.setJoinKey(getKeyNames(typeKeysFields));
				joinConfigProperty.setPortIndex(typeKeysFields.getInSocketId());
				joinConfigPropertyList.add(joinConfigProperty);
			}
		}
		return joinConfigPropertyList;
	}

	private String getKeyNames(TypeKeyFields typeKeyFields) {
		StringBuilder joinKey = new StringBuilder();
		if (typeKeyFields != null && !typeKeyFields.getField().isEmpty()) {
			for (TypeFieldName typeFieldName : typeKeyFields.getField()) {
				joinKey.append(typeFieldName.getName()).append(",");
			}
		}
		if (joinKey.lastIndexOf(",") != -1)
			joinKey = joinKey.deleteCharAt(joinKey.lastIndexOf(","));
		return joinKey.toString();
	}
	
	
	private Integer getRecordRequired(TypeKeyFields typeKeysFields) {
		int recordRequiredNumber;
		if(typeKeysFields.isRecordRequired())
		{
			recordRequiredNumber=0;
		}
		else
		{
			recordRequiredNumber=1;
		}
		return recordRequiredNumber;
	}

	private String getSize() {
		Dimension newSize = uiComponent.getSize();
		uiComponent.setSize(newSize.expand(inPortCounter * 15, inPortCounter * 15));
		return String.valueOf(inPortCounter);
	}

	protected void getInPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating InPut Ports for -{}", componentName);
		if (operationsComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : operationsComponent.getInSocket()) {
				uiComponent.engageInputPort(getInputSocketType(inSocket) + inPortCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(), operationsComponent.getId(), inSocket
								.getFromSocketId(), inSocket.getId()));
				inPortCounter++;
			}

			if (inPortCounter > 2) {
				uiComponent.inputPortSettings(inPortCounter);
				uiComponent.unusedPortSettings(inPortCounter);
			}
		}

	}

	protected void getOutPort(TypeOperationsComponent operationsComponent) {
		LOGGER.debug("Generating OutPut Ports for -{}", componentName);
		if (operationsComponent.getOutSocket() != null) {
			for (TypeOperationsOutSocket outSocket : operationsComponent.getOutSocket()) {
				uiComponent.engageOutputPort(outSocket.getId());
				if (outSocket.getPassThroughFieldOrOperationFieldOrMapField() != null
						&& !outSocket.getPassThroughFieldOrOperationFieldOrMapField().isEmpty())
					propertyMap.put(Constants.JOIN_MAP_FIELD, getJoinMappingGrid(outSocket));
			}

		}
	}

	private JoinMappingGrid getJoinMappingGrid(TypeOperationsOutSocket outSocket) {
		String dot_separator = ".";
		LookupMapProperty lookupMapProperty = null;
		JoinMappingGrid joinMappingGrid = new JoinMappingGrid();
		for (Object object : outSocket.getPassThroughFieldOrOperationFieldOrMapField()) {
			if ((TypeInputField.class).isAssignableFrom(object.getClass())) {
				lookupMapProperty = new LookupMapProperty();
				lookupMapProperty.setOutput_Field(((TypeInputField) object).getName());
				lookupMapProperty.setSource_Field(((TypeInputField) object).getInSocketId() + dot_separator
						+ ((TypeInputField) object).getName());
				joinMappingGrid.getLookupMapProperties().add(lookupMapProperty);
			}
			if ((TypeMapField.class).isAssignableFrom(object.getClass())) {
				lookupMapProperty = new LookupMapProperty();
				lookupMapProperty.setOutput_Field(((TypeMapField) object).getName());
				lookupMapProperty.setSource_Field(((TypeMapField) object).getInSocketId() + dot_separator
						+ ((TypeMapField) object).getSourceName());
				joinMappingGrid.getLookupMapProperties().add(lookupMapProperty);
			}
		}
		return joinMappingGrid;
	}
}
