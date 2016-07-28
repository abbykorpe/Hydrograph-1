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


package hydrograph.ui.propertywindow.widgets.customwidgets;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.ParameterUtil;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.LookupMapProperty;
import hydrograph.ui.datastructure.property.LookupMappingGrid;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import hydrograph.ui.propertywindow.widgets.dialogs.lookup.LookupMapDialog;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.utility.SchemaSyncUtility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


public class ELTLookupMapWidget extends AbstractWidget {

	private String propertyName;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	private LookupMappingGrid lookupMappingGrid;
	private List<AbstractWidget> widgets;

	public ELTLookupMapWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propertyDialogButtonBar);
		if (componentConfigProp.getPropertyValue() == null) {
			lookupMappingGrid = new LookupMappingGrid();
		} else {
			lookupMappingGrid = (LookupMappingGrid) componentConfigProp.getPropertyValue();
		}
		this.propertyName = componentConfigProp.getPropertyName();
	}

	private void syncSchema() {
		if(isSyncRequired()){
			MessageDialog dialog = new MessageDialog(new Shell(), Constants.SYNC_WARNING, null, Constants.SCHEMA_NOT_SYNC_MESSAGE, MessageDialog.CONFIRM, new String[] { Messages.SYNC_NOW, Messages.MANUAL_SYNC }, 0);
			if (dialog.open() == 0) {
				getSchemaGridWidget().updateSchemaWithPropogatedSchema();
			}
		}
	}
	
	private boolean isSyncRequired() {
		Schema schema = (Schema) getComponent().getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
		
		List<String> lookupOutputFieldList = SchemaSyncUtility.INSTANCE.getSchemaFieldList(getSchemaForInternalPropagation().getGridRow());
		
		if(schema==null && lookupOutputFieldList.size()!=0){
			return true;
		}
		
		if(schema==null && lookupOutputFieldList.size()==0){
			return false;
		}
		
		List<String> schemaFieldList = SchemaSyncUtility.INSTANCE.getSchemaFieldList(schema.getGridRow());
		
		
		if(schemaFieldList == null && lookupOutputFieldList == null){
			return false;
		}
		
		if(schemaFieldList.size()!=lookupOutputFieldList.size()){
			return true;
		}
		
		for(int index=0;index<schemaFieldList.size();index++){
			if(!StringUtils.equals(schemaFieldList.get(index), lookupOutputFieldList.get(index))){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				subGroup.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Lookup\nMapping");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		setPropertyHelpWidget((Control) eltDefaultLable.getSWTWidgetControl());

		final AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getPropagatedSchema();

				LookupMapDialog lookupMapDialog = new LookupMapDialog(((Button) eltDefaultButton.getSWTWidgetControl()).getShell(), getComponent(),
						lookupMappingGrid,propertyDialogButtonBar);

				lookupMapDialog.open();
				propagateInternalSchema();
				syncSchema();
				showHideErrorSymbol(widgets);
			}
		});
		propagateInternalSchema();
	}


	private Schema propagateInternalSchema() {
		if(lookupMappingGrid ==null){
			return null;
		}

		Schema internalSchema = getSchemaForInternalPropagation();			 
		internalSchema.getGridRow().clear();


		List<String> finalPassThroughFields=new LinkedList<String>();
		Map<String, String> finalMapFields=new LinkedHashMap<String, String>();

		Map<String,String> passThroughFieldsPortInfo = new LinkedHashMap<>();
		Map<String,String> mapFieldsPortInfo = new LinkedHashMap<>();


		List<LookupMapProperty> lookupMapRows = lookupMappingGrid.clone().getLookupMapProperties();

		List<GridRow> outputSchemaGridRowList = new LinkedList<>();

		for(LookupMapProperty row : lookupMapRows){

			if(!ParameterUtil.isParameter(row.getSource_Field())){
				GridRow inputFieldSchema = getInputFieldSchema(row.getSource_Field());
				GridRow outputFieldSchema = null;

				if(inputFieldSchema==null){
					outputFieldSchema = SchemaPropagationHelper.INSTANCE.createSchemaGridRow(row.getOutput_Field());
				}else{
					outputFieldSchema = getOutputFieldSchema(inputFieldSchema,row.getOutput_Field());
				}

				if(!StringUtils.isBlank(row.getSource_Field())){
					if(row.getSource_Field().trim().length() > 0){
						String[] sourceField = row.getSource_Field().split("\\.");
						if(sourceField.length == 2){
							if(row.getOutput_Field().equals(sourceField[1])){
								finalPassThroughFields.add(row.getOutput_Field());
								passThroughFieldsPortInfo.put(row.getOutput_Field(), sourceField[0]);
							}else{
								finalMapFields.put(sourceField[1], row.getOutput_Field());
								mapFieldsPortInfo.put(row.getOutput_Field(), sourceField[0]);
							}
						}
					}
				}

				outputSchemaGridRowList.add(outputFieldSchema);
			}
		}

		addPassthroughFieldsAndMappingFieldsToComponentOuputSchema(finalMapFields, finalPassThroughFields,passThroughFieldsPortInfo,mapFieldsPortInfo);


		internalSchema.getGridRow().addAll(outputSchemaGridRowList);
		return internalSchema;
	}

	private void addPassthroughFieldsAndMappingFieldsToComponentOuputSchema(Map<String, String> mapFields,
			List<String> passThroughFields, Map<String, String> passThroughFieldsPortInfo, Map<String, String> mapFieldsPortInfo) {
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
		componentsOutputSchema.getPassthroughFieldsPortInfo().clear();
		componentsOutputSchema.getMapFieldsPortInfo().clear();

		componentsOutputSchema.getPassthroughFields().addAll(passThroughFields);
		componentsOutputSchema.getMapFields().putAll(mapFields);

		for(String field : passThroughFieldsPortInfo.keySet()){
			componentsOutputSchema.getPassthroughFieldsPortInfo().put(field, passThroughFieldsPortInfo.get(field));
		}

		componentsOutputSchema.getMapFieldsPortInfo().putAll(mapFieldsPortInfo);
	}	

	private GridRow getOutputFieldSchema(GridRow inputFieldSchema,
			String output_Field) {
		if(inputFieldSchema!=null)
		{
			GridRow gridRow = inputFieldSchema.copy();
			gridRow.setFieldName(output_Field);
			return gridRow.copy();
		}
		return null;
	}

	private GridRow getInputFieldSchema(String source_Field) {		

		String[] source = source_Field.split("\\.");
		if(source.length == 2){
			return getInputFieldSchema(source[1],source[0]);
		}else{
			return null;
		}
	}

	private GridRow getInputFieldSchema(String fieldName,String linkNumber) {
		ComponentsOutputSchema outputSchema = null;
		//List<FixedWidthGridRow> fixedWidthGridRows = new LinkedList<>();
		for (Link link : getComponent().getTargetConnections()) {

			if(linkNumber.equals(link.getTargetTerminal())){				
				outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
				if (outputSchema != null){
					for (GridRow row : outputSchema.getSchemaGridOutputFields(null)) {
						if(row.getFieldName().equals(fieldName)){
							return row.copy();
						}
					}
				}
			}
		}
		return null;
	}

	private void getPropagatedSchema() {
		List<List<FilterProperties>> sorceFieldList = SchemaPropagationHelper.INSTANCE
				.sortedFiledNamesBySocketId(getComponent());
		if (sorceFieldList != null)
			lookupMappingGrid.setLookupInputProperties(sorceFieldList);
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		property.put(propertyName, lookupMappingGrid);
		return property;
	}

	@Override  
	public boolean isWidgetValid() {
		return validateAgainstValidationRule(lookupMappingGrid); 
	}



	@Override
	public void addModifyListener(Property property,  ArrayList<AbstractWidget> widgetList) {
		widgets=widgetList;

	}

	private ELTSchemaGridWidget getSchemaGridWidget(){
		for(AbstractWidget widget: widgets){
			if(widget instanceof ELTSchemaGridWidget){
				return (ELTSchemaGridWidget) widget;
			}
		}
		return null;
	}
}
