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

 
package com.bitwise.app.propertywindow.widgets.customwidgets.operational;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import org.eclipse.swt.widgets.Control;

import org.eclipse.swt.widgets.Shell;


import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.datastructure.property.mapping.TransformMapping;
import com.bitwise.app.common.datastructure.property.BasicSchemaGridRow;
import com.bitwise.app.common.datastructure.property.mapping.ErrorObject;
import com.bitwise.app.common.datastructure.property.mapping.InputField;
import com.bitwise.app.common.datastructure.property.mapping.MappingSheetRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.OperationClassConfig;
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
	private TransformMapping transformMapping;
	private TransformPropertyGrid transformPropertyGrid;

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

		this.transformMapping = (TransformMapping) componentConfigrationProperty.getPropertyValue();
		if (transformMapping == null) {
			transformMapping = new TransformMapping();
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
		
		setPropertyHelpWidget((Control) defaultLable1.getSWTWidgetControl());
		
		ELTDefaultButton eltDefaultButton = new ELTDefaultButton("Edit").grabExcessHorizontalSpace(false);
		transformComposite.attachWidget(eltDefaultButton);

		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				getPropagatedSChema();

				TransformMapping oldATMappings = (TransformMapping) transformMapping.clone();
				
				TransformDialogNew t=new TransformDialogNew(new Shell(),getComponent().getComponentName(),widgetConfig,transformMapping);
				t.open();
				
				if(t.isCancelPressed())
				transformMapping=oldATMappings;
				
				if(!oldATMappings.equals(transformMapping))
				{
					propertyDialogButtonBar.enableApplyButton(true);
					propagateOuputFieldsToSchemaTabFromTransformWidget();
				}
				
				transformMapping.getInputFields().clear();

			}
		});
		propagateOuputFieldsToSchemaTabFromTransformWidget();
	}

	// PLEASE DO NOT REMOVE THE CODE
	/*private void initSchemaObject() {
		Schema schemaForInternalPapogation = new Schema();
		schemaForInternalPapogation.setIsExternal(false);
		List<GridRow> gridRows = new ArrayList<>();
		schemaForInternalPapogation.setGridRow(gridRows);
		schemaForInternalPapogation.setExternalSchemaPath("");
		setSchemaForInternalPapogation(schemaForInternalPapogation);
	}*/
	
	public void propagateOuputFieldsToSchemaTabFromTransformWidget() {
		
		if (transformMapping == null || transformMapping.getMappingSheetRows() == null)
			return;
		
	
		getSchemaForInternalPapogation().getGridRow().clear();
		getOperationFieldList().clear();
		
		List<String> finalPassThroughFields=new LinkedList<String>();
		Map<String, String> finalMapFields=new LinkedHashMap<String, String>();
		
		List<FilterProperties> operationFieldList=new LinkedList<FilterProperties>();
		
		for (MappingSheetRow mappingSheetRow : transformMapping.getMappingSheetRows()) {
			List<FilterProperties> operationFields = getOpeartionFields(mappingSheetRow);
			
			operationFieldList.addAll(operationFields);
			addOperationFieldsToSchema(operationFields);
		 }
		
		
			
			
		List<String> passThroughFields = getPassThroughFields(transformMapping.getMapAndPassthroughField());
		Map<String, String> mapFields = getMapFields(transformMapping.getMapAndPassthroughField());
		finalMapFields.putAll(mapFields);
		finalPassThroughFields.addAll(passThroughFields);
		
		addPassthroughFieldsToSchema(passThroughFields);
		addMapFieldsToSchema(mapFields);
		
		addPassthroughFieldsAndMappingFieldsToComponentOuputSchema(finalMapFields, finalPassThroughFields);
		for(FilterProperties f:operationFieldList)
		{	
		getOperationFieldList().add(f.getPropertyname());
		}
	}
	
	
	

	private void addPassthroughFieldsAndMappingFieldsToComponentOuputSchema(Map<String, String> mapFields,
			List<String> passThroughFields) {
		ComponentsOutputSchema componentsOutputSchema = null;
		Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) getComponent()
				.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
		if (schemaMap != null && schemaMap.get(Constants.FIXED_OUTSOCKET_ID) != null)
			componentsOutputSchema = schemaMap.get(Constants.FIXED_OUTSOCKET_ID);
		else {
			componentsOutputSchema = new ComponentsOutputSchema();
			schemaMap = new LinkedHashMap<>();
			schemaMap.put(Constants.FIXED_OUTSOCKET_ID, componentsOutputSchema);
		}
		getComponent().getProperties().put(Constants.SCHEMA_TO_PROPAGATE, schemaMap);

		componentsOutputSchema.getPassthroughFields().clear();
		componentsOutputSchema.getMapFields().clear();
		componentsOutputSchema.getPassthroughFields().addAll(passThroughFields);
		componentsOutputSchema.getMapFields().putAll(mapFields);
		Schema tmpSchema = getSchemaForInternalPapogation();
	}

	// PLEASE DO NOT REMOVE THE CODE
	/*private List<String> getCurrentSchemaFields() {
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
	}*/

	private BasicSchemaGridRow getFieldSchema(String fieldName) {
		List<BasicSchemaGridRow> schemaGridRows = getInputFieldSchema();
		for (BasicSchemaGridRow schemaGridRow : schemaGridRows) {
			if (schemaGridRow.getFieldName().equals(fieldName)) {
				return schemaGridRow;
			}
		}
		return null;
	}

	private List<BasicSchemaGridRow> getInputFieldSchema() {
		ComponentsOutputSchema outputSchema = null;
		List<BasicSchemaGridRow> schemaGridRows = new LinkedList<>();
		for (Link link : getComponent().getTargetConnections()) {
			outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
			if (outputSchema != null)
				for (BasicSchemaGridRow row : outputSchema.getSchemaGridOutputFields()) {
					schemaGridRows.add(row);
				}
		}
		return schemaGridRows;
	}

	private void addMapFieldsToSchema(Map<String, String> mapFields) {
		BasicSchemaGridRow tempSchemaGridRow = null;
		Schema schema = getSchemaForInternalPapogation();
		List<String> currentFieldsInProppogatedSchemaObject = new LinkedList<>();
		for (GridRow gridRow : schema.getGridRow()) {
			currentFieldsInProppogatedSchemaObject.add(gridRow.getFieldName());
		}

		for (String inputField : mapFields.keySet()) {
			tempSchemaGridRow = (BasicSchemaGridRow) getFieldSchema(inputField);
			if (tempSchemaGridRow != null) {
				BasicSchemaGridRow schemaGridRow = (BasicSchemaGridRow) tempSchemaGridRow.copy();
				schemaGridRow.setFieldName(mapFields.get(inputField));

				if (!currentFieldsInProppogatedSchemaObject.contains(mapFields.get(inputField))) {
					schema.getGridRow().add(schemaGridRow);
				} else {
					for (int index = 0; index < schema.getGridRow().size(); index++) {
						if (schema.getGridRow().get(index).getFieldName().equals(mapFields.get(inputField))) {
							schema.getGridRow().set(index, schemaGridRow);
						}
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
			BasicSchemaGridRow schemaGridRow= getFieldSchema(passThroughField);
			if(schemaGridRow!=null){
			BasicSchemaGridRow tempSchemaGrid =(BasicSchemaGridRow) schemaGridRow.copy();

			if (!currentFieldsInProppogatedSchemaObject.contains(passThroughField)) {
				schema.getGridRow().add(tempSchemaGrid);
			} else {
				for (int index = 0; index < schema.getGridRow().size(); index++) {
					if (schema.getGridRow().get(index).getFieldName().equals(passThroughField)) {
						schema.getGridRow().set(index, tempSchemaGrid);
					}
				}
			}
		}}
	}

	private void addOperationFieldsToSchema(List<FilterProperties> operationFields) {
		Schema schema = getSchemaForInternalPapogation();
		List<String> currentFieldsInProppogatedSchemaObject = new LinkedList<>();
		for (GridRow gridRow : schema.getGridRow()) {
			currentFieldsInProppogatedSchemaObject.add(gridRow.getFieldName());
		}

		SchemaPropagationHelper schemaPropagationHelper = new SchemaPropagationHelper();

		for (FilterProperties operationField : operationFields) {

			BasicSchemaGridRow schemaGridRow = schemaPropagationHelper.createSchemaGridRow(operationField.getPropertyname());

			if (!currentFieldsInProppogatedSchemaObject.contains(operationField.getPropertyname())) {
				schema.getGridRow().add(schemaGridRow);
			} else {
				for (int index = 0; index < schema.getGridRow().size(); index++) {
					if (schema.getGridRow().get(index).getFieldName().equals(operationField.getPropertyname())) {
						schema.getGridRow().set(index, schemaGridRow);

					}
				}
			}
		}
	}

	private Map<String,String> getMapFields(
			List<NameValueProperty> nameValueProperties) 
		{
			Map<String,String> mapField = new LinkedHashMap<>();
			if (!nameValueProperties.isEmpty()) {

				for (NameValueProperty nameValueProperty : nameValueProperties) {
					if (!(nameValueProperty.getPropertyName().equals(
							nameValueProperty.getPropertyValue()))) {
						mapField.put(nameValueProperty.getPropertyName(),nameValueProperty.getPropertyValue());
					}
				}

			}
			return mapField;
		}
	
	

	private List<String> getPassThroughFields(
		List<NameValueProperty> nameValueProperties) 
	{
		List<String> passthroughField = new LinkedList<>();
		if (!nameValueProperties.isEmpty()) {

			for (NameValueProperty nameValueProperty : nameValueProperties) {
				if (nameValueProperty.getPropertyName().equals(
						nameValueProperty.getPropertyValue())) {
					passthroughField.add(nameValueProperty.getPropertyValue());
				}
			}

		}
		return passthroughField;
	}
	private List<FilterProperties> getOpeartionFields(MappingSheetRow mappingSheetRow) {
		List<FilterProperties> operationFields = new LinkedList<>();
		operationFields.addAll(mappingSheetRow.getOutputList());
		return operationFields;
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		transformMapping.getInputFields().clear();
		property.put(propertyName, transformMapping);

		return property;
	}

   private void getPropagatedSChema() {
		ComponentsOutputSchema outputSchema = null;
		InputField inputField = null;
		List<InputField> inputFieldsList = transformMapping.getInputFields();
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
