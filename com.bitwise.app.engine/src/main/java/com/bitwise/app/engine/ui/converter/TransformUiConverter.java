package com.bitwise.app.engine.ui.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.draw2d.geometry.Dimension;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.datastructure.property.OperationField;
import com.bitwise.app.common.datastructure.property.OperationSystemProperties;
import com.bitwise.app.common.datastructure.property.TransformOperation;
import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.constants.PropertyNameConstants;
import com.bitwise.app.engine.ui.helper.ConverterUiHelper;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.propertywindow.fixedwidthschema.FixedWidthGridRow;
import com.bitwiseglobal.graph.commontypes.TypeBaseField;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeInputField;
import com.bitwiseglobal.graph.commontypes.TypeMapField;
import com.bitwiseglobal.graph.commontypes.TypeOperationInputFields;
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
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(TransformUiConverter.class);

	/**
	 * Generate common properties of transform-component that will appear in property window.
	 * 
	 */
	@Override
	public void prepareUIXML() {
		super.prepareUIXML();
		LOGGER.debug("Fetching common properties for -{}", componentName);
		getInPort((TypeOperationsComponent) typeBaseComponent);
		getOutPort((TypeOperationsComponent) typeBaseComponent);
		propertyMap.put(PropertyNameConstants.RUNTIME_PROPERTIES.value(), getRuntimeProperties());
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
		int portCounter = 1;

		if (operationsComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : operationsComponent.getInSocket()) {
				uiComponent.engageInputPort(inSocket.getType() + portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(), operationsComponent.getId(), inSocket
								.getFromSocketId(), inSocket.getId()));

				portCounter++;
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
		int portCounter = 0;
		int unusedPortsCounter = 0;
		if (operationsComponent.getOutSocket() != null) {
			for (TypeOperationsOutSocket outSocket : operationsComponent.getOutSocket()) {
				if (outSocket.getType().equals("unused"))
					uiComponent.engageOutputPort(outSocket.getType() + (++unusedPortsCounter));
				else
					uiComponent.engageOutputPort(outSocket.getType() + (++portCounter));
				if (outSocket.getPassThroughFieldOrOperationFieldOrMapField() != null)
					propertyMap.put(Constants.PARAM_OPERATION,
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
	 * @return TransformPropertyGrid, transformPropertyGrid object which is responsible to display transform grid.
	 */
	protected TransformPropertyGrid getUiPassThroughOrOperationFieldsOrMapFieldGrid(TypeOperationsOutSocket outSocket) {
		LOGGER.debug("Generating out-socket related UI-Grid for -{}", componentName);
		TransformPropertyGrid transformPropertyGrid = new TransformPropertyGrid();
		transformPropertyGrid.setNameValueProps(new ArrayList<NameValueProperty>());
		transformPropertyGrid.setOpSysProperties(new ArrayList<OperationSystemProperties>());
		transformPropertyGrid.setOutputTreeFields(new ArrayList<OperationField>());
		for (Object property : outSocket.getPassThroughFieldOrOperationFieldOrMapField()) {
			if (property instanceof TypeInputField) {
				transformPropertyGrid.getOpSysProperties().add(getOperationSystemProperties((TypeInputField) property));
				transformPropertyGrid.getOutputTreeFields().add(
						new OperationField(((TypeInputField) property).getName()));
			} else if (property instanceof TypeMapField)
				transformPropertyGrid.getNameValueProps().add(getNameValueProps((TypeMapField) property));
		}
		return transformPropertyGrid;
	}

	/**
	 * Create transform property grid for transform component.
	 * 
	 * @return TransformPropertyGrid, transformPropertyGrid object which is responsible to display transform grid.
	 */
	protected TransformPropertyGrid createTransformPropertyGrid() {
		LOGGER.debug("Generating UI-Grid for -{}", componentName);
		TransformPropertyGrid transformPropertyGrid = null;
		List<TypeTransformOperation> typeTransformOperationlist = ((TypeOperationsComponent) typeBaseComponent)
				.getOperation();
		if (propertyMap.get(Constants.PARAM_OPERATION) != null)
			transformPropertyGrid = (TransformPropertyGrid) propertyMap.get(Constants.PARAM_OPERATION);
		else
			transformPropertyGrid = new TransformPropertyGrid();

		if (typeTransformOperationlist != null && typeTransformOperationlist.size() != 0) {
			transformPropertyGrid.setOperation(new ArrayList<TransformOperation>());
			for (TypeTransformOperation transformOperation : typeTransformOperationlist) {
				TransformOperation uITransformOperation = new TransformOperation();
				uITransformOperation.setOperationId(Long.parseLong(transformOperation.getId()));
				uITransformOperation.setOpClassProperty(new OperationClassProperty(transformOperation.getClazz(),
						isParameter(transformOperation.getClazz())));
				uITransformOperation.setSchemaGridRowList(getFixedWidthSchemaList(transformOperation));
				uITransformOperation.setNameValueProps(getNameValueProperty(transformOperation.getProperties()));
				uITransformOperation.setInputFields(getInputFields(transformOperation.getInputFields()));
				transformPropertyGrid.getOperation().add(uITransformOperation);
			}
		}
		return transformPropertyGrid;
	}

	/**
	 * Create input field property of transform property grid for transform component.
	 * 
	 * @param TypeOperationInputFields
	 *            the inputFields
	 * 
	 * @return List<OperationField>, list of input-field's object which is responsible to display input-fields under
	 *         transform grid.
	 */
	private List<OperationField> getInputFields(TypeOperationInputFields inputFields) {
		LOGGER.debug("Fetching Input-Fields property of UI-Grid for -{}", componentName);
		List<OperationField> operationFieldList = null;
		OperationField operationField = null;
		if (inputFields != null && inputFields.getField().size() != 0) {
			operationFieldList = new ArrayList<>();
			for (TypeInputField typeInputField : inputFields.getField()) {
				operationField = new OperationField();
				operationField.setName(typeInputField.getName());
				operationFieldList.add(operationField);
			}
		}

		return operationFieldList;
	}

	/**
	 * Create name-value property of transform property grid for transform component.
	 * 
	 * @param TypeProperties
	 *            the properties
	 * 
	 * @return List<NameValueProperty>, list of NameValueProperty object which is responsible to display name-value
	 *         property under transform grid.
	 */
	private List<NameValueProperty> getNameValueProperty(TypeProperties properties) {
		LOGGER.debug("Fetching Name-Value property of UI-Grid for -{}", componentName);
		List<NameValueProperty> nameValuePropertyList = null;
		NameValueProperty nameValueProperty = null;
		if (properties != null && properties.getProperty().size() != 0) {
			nameValuePropertyList = new ArrayList<>();
			for (Property property : properties.getProperty()) {
				nameValueProperty = new NameValueProperty();
				nameValueProperty.setPropertyName(property.getName());
				nameValueProperty.setPropertyValue(property.getValue());
				nameValuePropertyList.add(nameValueProperty);
			}
		}
		return nameValuePropertyList;
	}

	/**
	 * Create fixed-width-schema property for transform component.
	 * 
	 * @param TypeTransformOperation
	 *            the transformOperation
	 * 
	 * @return List<FixedWidthGridRow>, list of FixedWidthGridRow object which is responsible to display fixed-width
	 *         schema under transform grid.
	 */
	private List<FixedWidthGridRow> getFixedWidthSchemaList(TypeTransformOperation transformOperation) {
		List<FixedWidthGridRow> fixedWidthGridRowsList = new ArrayList<>();
		ConverterUiHelper converterUiHelper = new ConverterUiHelper(uiComponent);
		if(transformOperation.getOutputFields()!=null){
		for (TypeBaseField field : transformOperation.getOutputFields().getField()) {
			fixedWidthGridRowsList.add(converterUiHelper.getFixedWidthSchema(field));
		}
		}
		return fixedWidthGridRowsList;
	}

	/**
	 * Create single name-value property.
	 * 
	 * @param TypeMapField
	 *            the property
	 * 
	 * @return NameValueProperty
	 */
	private NameValueProperty getNameValueProps(TypeMapField property) {
		NameValueProperty nameValueProperty = new NameValueProperty();
		nameValueProperty.setPropertyName(property.getSourceName());
		nameValueProperty.setPropertyValue(property.getName());
		return nameValueProperty;
	}

	/**
	 * Create single System operation property.
	 * 
	 * @param TypeInputField
	 *            the property
	 * 
	 * @return OperationSystemProperties
	 */
	private OperationSystemProperties getOperationSystemProperties(TypeInputField property) {
		OperationSystemProperties operationSystemProperty = new OperationSystemProperties();
		operationSystemProperty.setChecked(true);
		operationSystemProperty.setOpSysValue(property.getName());
		return operationSystemProperty;

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
		TypeProperties typeProperties = ((TypeOperationsComponent) typeBaseComponent).getRuntimeProperties();
		if (typeProperties != null) {
			runtimeMap = new TreeMap<>();
			for (Property runtimeProperty : typeProperties.getProperty()) {
				runtimeMap.put(runtimeProperty.getName(), runtimeProperty.getValue());
			}
		}
		return runtimeMap;
	}

}
