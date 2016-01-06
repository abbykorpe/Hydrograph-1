package com.bitwise.app.engine.ui.converter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.JoinConfigProperty;
import com.bitwise.app.common.datastructure.property.JoinMappingGrid;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.constants.UIComponentsConstants;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.converter.TransformUiConverter;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Container;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeMapField;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;
import com.bitwiseglobal.graph.join.JoinType;
import com.bitwiseglobal.graph.join.TypeKeyFields;
import com.bitwiseglobal.graph.operationstypes.Join;

public class JoinComponentUiConverter extends TransformUiConverter {

	private Join join;

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(JoinComponentUiConverter.class);
	private int inPortCounter = 0;

	public JoinComponentUiConverter(TypeBaseComponent typeBaseComponent, Container container) {
		this.container = container;
		this.typeBaseComponent = typeBaseComponent;
		this.uiComponent = new com.bitwise.app.graph.model.components.Join();
		this.propertyMap = new LinkedHashMap<>();
	}

	@Override
	public void prepareUIXML() {
		join = (Join) typeBaseComponent;
		super.prepareUIXML();
		LOGGER.debug("Fetching Join-Properties for -{}", componentName);

		propertyMap.put("inPortCount", getSize());
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
				joinConfigProperty.setJoinType(getJoinType(typeKeysFields));
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
	
	
	private Integer getJoinType(TypeKeyFields typeKeysFields) {
		int joinTypeNumber = 1;
		if (typeKeysFields.getJoinType() != null && typeKeysFields.getJoinType().value() != null)
			for (JoinType joinType : JoinType.values()) {
				if (typeKeysFields.getJoinType().equals(joinType))
					return joinTypeNumber;
				joinTypeNumber++;
			}
		return joinTypeNumber;
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
				uiComponent.importPortSettings(inPortCounter);
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
