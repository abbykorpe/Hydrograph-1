package com.bitwise.app.propertywindow.widgets.customwidgets.operational;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.Schema;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.mapping.ATMapping;
import com.bitwise.app.common.datastructure.property.mapping.ErrorObject;
import com.bitwise.app.common.datastructure.property.mapping.InputField;
import com.bitwise.app.common.datastructure.property.mapping.MappingSheetRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.OperationClassConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.MappingDialog;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

/**
 * The Class ELTOperationClassWidget.
 * 
 * @author Bitwise
 */
public class TransformWidget extends AbstractWidget {

	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	private ATMapping atMapping;


	/**
	 * Instantiates a new ELT operation class widget.
	 * 
	 * @param componentConfigrationProperty
	 *            the component configration property
	 * @param componentMiscellaneousProperties
	 *            the component miscellaneous properties
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public TransformWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties, propertyDialogButtonBar);

		this.atMapping = (ATMapping) componentConfigrationProperty.getPropertyValue();
		if (atMapping == null) {
			atMapping = new ATMapping();
		}
		
		this.propertyName = componentConfigrationProperty.getPropertyName();

	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void attachToPropertySubGroup(final AbstractELTContainerWidget container) {

		final ELTDefaultSubgroupComposite transformComposite = new ELTDefaultSubgroupComposite(
				container.getContainerControl());
		transformComposite.createContainerWidget();
		OperationClassConfig operationClassConfig = (OperationClassConfig) widgetConfig;
		ELTDefaultLable defaultLable1 = new ELTDefaultLable(operationClassConfig.getComponentDisplayName());
		transformComposite.attachWidget(defaultLable1);

		ELTDefaultButton eltDefaultButton = new ELTDefaultButton("Edit").grabExcessHorizontalSpace(false);
		transformComposite.attachWidget(eltDefaultButton);

		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				getPropagatedSChema();

				MappingDialog mappingDialog = new MappingDialog(transformComposite.getContainerControl().getShell(),
						propertyDialogButtonBar, atMapping, widgetConfig);
				mappingDialog.open();

				atMapping = mappingDialog.getATMapping();

				prapogateOuputFieldsToSchemaTabFromTransformWidget();

				atMapping.getInputFields().clear();
				super.widgetSelected(e);
			}

		});

		prapogateOuputFieldsToSchemaTabFromTransformWidget();
		// prapogateOuputFieldsToSchemaTab();
	}

	private void initSchemaObject() {
		Schema schemaForInternalPapogation = new Schema();
		schemaForInternalPapogation.setIsExternal(false);
		List<GridRow> gridRows = new ArrayList<>();
		schemaForInternalPapogation.setGridRow(gridRows);
		schemaForInternalPapogation.setExternalSchemaPath("");
		setSchemaForInternalPapogation(schemaForInternalPapogation);
	}
	
	private void prapogateOuputFieldsToSchemaTabFromTransformWidget() {
		
		if (atMapping == null || atMapping.getMappingSheetRows() == null)
			return;
		
	
		getSchemaForInternalPapogation().getGridRow().clear();
		getOperationFieldList().clear();
		
		List<String> finalPassThroughFields=new LinkedList<String>();
		Map<String, String> finalMapFields=new LinkedHashMap<String, String>();
		
		List<String> operationFieldList=new LinkedList<String>();
		
		for (MappingSheetRow mappingSheetRow : atMapping.getMappingSheetRows()) {
			List<String> operationFields = getOpeartionFields(mappingSheetRow);
			List<String> passThroughFields = getPassThroughFields(mappingSheetRow);
			Map<String, String> mapFields = getMapFields(mappingSheetRow);
			finalMapFields.putAll(mapFields);
			finalPassThroughFields.addAll(passThroughFields);
			operationFieldList.addAll(operationFields);
			addOperationFieldsToSchema(operationFields);
			addPassthroughFieldsToSchema(passThroughFields);
			addMapFieldsToSchema(mapFields);

		}
		addPassthroughFieldsAndMappingFieldsToComponentOuputSchema(finalMapFields, finalPassThroughFields);
		getOperationFieldList().addAll(operationFieldList);
	}
	
	
	

	private void addPassthroughFieldsAndMappingFieldsToComponentOuputSchema(Map<String, String> mapFields,
			List<String> passThroughFields) {
		ComponentsOutputSchema componentsOutputSchema = (ComponentsOutputSchema) getComponent().getProperties().get(
				Constants.SCHEMA_TO_PROPAGATE);
		if (componentsOutputSchema == null)
			componentsOutputSchema = new ComponentsOutputSchema();
		else {
			componentsOutputSchema.getPassthroughFields().clear();
			componentsOutputSchema.getMapFields().clear();
		}
		componentsOutputSchema.getPassthroughFields().addAll(passThroughFields);
		componentsOutputSchema.getMapFields().putAll(mapFields);
	}

	private List<String> getCurrentSchemaFields() {
		Component component = getComponent();
		Schema schema = (Schema) component.getProperties().get("schema");
		List<String> schemaFields = new LinkedList<>();
		if (schema != null) {
			for (GridRow gridRow : schema.getGridRow()) {
				FixedWidthGridRow fixedWidthGridRow = (FixedWidthGridRow) gridRow;
				schemaFields.add(fixedWidthGridRow.getFieldName());
			}
		}
		return schemaFields;
	}

	private FixedWidthGridRow getFieldSchema(String fieldName) {
		List<FixedWidthGridRow> fixedWidthGridRows = getInputFieldSchema();
		for (FixedWidthGridRow fixedWidthGridRow : fixedWidthGridRows) {
			if (fixedWidthGridRow.getFieldName().equals(fieldName)) {
				return fixedWidthGridRow;
			}
		}
		return null;
	}

	private List<FixedWidthGridRow> getInputFieldSchema() {
		ComponentsOutputSchema outputSchema = null;
		List<FixedWidthGridRow> fixedWidthGridRows = new LinkedList<>();
		for (Link link : getComponent().getTargetConnections()) {
			outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
			if (outputSchema != null)
				for (FixedWidthGridRow row : outputSchema.getFixedWidthGridRowsOutputFields()) {
					fixedWidthGridRows.add(row);
				}
		}
		return fixedWidthGridRows;
	}

	private void addMapFieldsToSchema(Map<String, String> mapFields) {
		Schema schema = getSchemaForInternalPapogation();
		List<String> currentFieldsInProppogatedSchemaObject = new LinkedList<>();
		for (GridRow gridRow : schema.getGridRow()) {
			currentFieldsInProppogatedSchemaObject.add(gridRow.getFieldName());
		}

		for (String inputField : mapFields.keySet()) {
			FixedWidthGridRow fixedWidthGridRow = (FixedWidthGridRow) getFieldSchema(inputField).copy();
			fixedWidthGridRow.setFieldName(mapFields.get(inputField));

			if (!currentFieldsInProppogatedSchemaObject.contains(mapFields.get(inputField))) {
				schema.getGridRow().add(fixedWidthGridRow);
			} else {
				for (int index = 0; index < schema.getGridRow().size(); index++) {
					if (schema.getGridRow().get(index).getFieldName().equals(mapFields.get(inputField))) {
						schema.getGridRow().set(index, fixedWidthGridRow);
					}
				}
			}
		}
		
	}

	private void addPassthroughFieldsToSchema(List<String> passThroughFields) {
		Schema schema = getSchemaForInternalPapogation();
		List<String> currentFieldsInProppogatedSchemaObject = new LinkedList<>();
		for (GridRow gridRow : schema.getGridRow()) {
			currentFieldsInProppogatedSchemaObject.add(gridRow.getFieldName());
		}

		for (String passThroughField : passThroughFields) {

			FixedWidthGridRow fixedWidthGridRow = (FixedWidthGridRow) getFieldSchema(passThroughField).copy();

			if (!currentFieldsInProppogatedSchemaObject.contains(passThroughField)) {
				schema.getGridRow().add(fixedWidthGridRow);
			} else {
				for (int index = 0; index < schema.getGridRow().size(); index++) {
					if (schema.getGridRow().get(index).getFieldName().equals(passThroughField)) {
						schema.getGridRow().set(index, fixedWidthGridRow);
					}
				}
			}
		}
	}

	private void addOperationFieldsToSchema(List<String> operationFields) {
		Schema schema = getSchemaForInternalPapogation();
		List<String> currentFieldsInProppogatedSchemaObject = new LinkedList<>();
		for (GridRow gridRow : schema.getGridRow()) {
			currentFieldsInProppogatedSchemaObject.add(gridRow.getFieldName());
		}

		SchemaPropagationHelper schemaPropagationHelper = new SchemaPropagationHelper();

		for (String operationField : operationFields) {

			FixedWidthGridRow fixedWidthGridRow = schemaPropagationHelper.createFixedWidthGridRow(operationField);
			if (!currentFieldsInProppogatedSchemaObject.contains(operationField)) {
				schema.getGridRow().add(fixedWidthGridRow);
			} else {
				for (int index = 0; index < schema.getGridRow().size(); index++) {
					if (schema.getGridRow().get(index).getFieldName().equals(operationField)) {
						schema.getGridRow().set(index, fixedWidthGridRow);

					}
				}
			}
		}
	}

	private Map<String, String> getMapFields(MappingSheetRow mappingSheetRow) {

		Map<String, String> mapFields = new LinkedHashMap<>();
		if (mappingSheetRow.getOperationClassProperty() == null
				|| mappingSheetRow.getOperationClassProperty().getOperationClassPath() == null
				|| mappingSheetRow.getOperationClassProperty().getOperationClassPath().trim().equals("")) {

			List<String> inputFields = mappingSheetRow.getImputFields();
			List<String> outputFields = mappingSheetRow.getOutputList();
			int index = 0;
			for (String inputField : inputFields) {
				if (!inputField.trim().equals(outputFields.get(index).trim())) {
					mapFields.put(inputField.trim(), outputFields.get(index).trim());
				}
				index++;
			}
		}
		return mapFields;

	}

	private List<String> getPassThroughFields(MappingSheetRow mappingSheetRow) {
		List<String> passThroughFields = new LinkedList<>();
		if (mappingSheetRow.getOperationClassProperty() == null
				|| mappingSheetRow.getOperationClassProperty().getOperationClassPath() == null
				|| mappingSheetRow.getOperationClassProperty().getOperationClassPath().trim().equals("")) {

			List<String> inputFields = mappingSheetRow.getImputFields();
			List<String> outputFields = mappingSheetRow.getOutputList();
			int index = 0;
			for (String inputField : inputFields) {
				if (inputField.trim().equals(outputFields.get(index).trim())) {
					passThroughFields.add(inputField.trim());
				}
				index++;
			}
		}
		return passThroughFields;
	}

	private List<String> getOpeartionFields(MappingSheetRow mappingSheetRow) {
		List<String> operationFields = new LinkedList<>();
		if (mappingSheetRow.getOperationClassProperty() != null
				&& mappingSheetRow.getOperationClassProperty().getOperationClassPath() != null
				&& !mappingSheetRow.getOperationClassProperty().getOperationClassPath().equalsIgnoreCase("")) {
			operationFields.addAll(mappingSheetRow.getOutputList());
		}
		return operationFields;
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		atMapping.getInputFields().clear();
		property.put(propertyName, atMapping);

		return property;
	}

	private void getPropagatedSChema() {
		ComponentsOutputSchema outputSchema = null;
		InputField inputField = null;
		List<InputField> inputFieldsList = atMapping.getInputFields();
		for (Link link : getComponent().getTargetConnections()) {
			outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
			if (outputSchema != null)
				for (FixedWidthGridRow row : outputSchema.getFixedWidthGridRowsOutputFields()) {
					inputField = new InputField(row.getFieldName(), new ErrorObject(false, ""));
					inputFieldsList.add(inputField);
				}
		}
	}
}
