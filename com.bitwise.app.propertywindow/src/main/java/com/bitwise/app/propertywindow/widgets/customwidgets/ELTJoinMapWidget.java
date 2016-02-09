package com.bitwise.app.propertywindow.widgets.customwidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.JoinMappingGrid;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.common.datastructure.property.mapping.ErrorObject;
import com.bitwise.app.common.datastructure.property.mapping.InputField;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.schema.propagation.SchemaPropagation;
import com.bitwise.app.propertywindow.property.ComponentConfigrationProperty;
import com.bitwise.app.propertywindow.property.ComponentMiscellaneousProperties;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import com.bitwise.app.propertywindow.widgets.customwidgets.joinproperty.JoinMapGrid;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;

public class ELTJoinMapWidget extends AbstractWidget {

	public static int value;
	private Object properties;
	private String propertyName;
	private JoinMappingGrid joinMappingGrid;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();

	public ELTJoinMapWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propertyDialogButtonBar);
		this.propertyName = componentConfigrationProperty.getPropertyName();
		this.properties = componentConfigrationProperty.getPropertyValue();
		if (componentConfigProp.getPropertyValue() == null) {
			joinMappingGrid = new JoinMappingGrid();
		} else {
			joinMappingGrid = (JoinMappingGrid) componentConfigProp.getPropertyValue();
		}
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(
				subGroup.getContainerControl());
		eltSuDefaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Join\n Mapping");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultLable);

		final AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Edit");
		eltSuDefaultSubgroupComposite.attachWidget(eltDefaultButton);
		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getPropagatedSchema();
				JoinMapGrid joinMapGrid = new JoinMapGrid(((Button) eltDefaultButton.getSWTWidgetControl()).getShell(),
						joinMappingGrid,propertyDialogButtonBar);
				joinMapGrid.open();
				propagateInternalSchema();
			}
		});
		
		propagateInternalSchema();
	}

	
	
	private void propagateInternalSchema() {
		if(joinMappingGrid ==null)
			return;
		
			 Schema internalSchema = getSchemaForInternalPapogation();			 
			 internalSchema.getGridRow().clear();
			 
			 
			 List<String> finalPassThroughFields=new LinkedList<String>();
			 Map<String, String> finalMapFields=new LinkedHashMap<String, String>();
			 
			Map<String,String> passThroughFieldsPortInfo = new LinkedHashMap<>();
			Map<String,String> mapFieldsPortInfo = new LinkedHashMap<>();
			 
			 
			 List<LookupMapProperty> lookupMapRows = joinMappingGrid.clone().getLookupMapProperties();
			 
			 List<GridRow> outputSchemaGridRowList = new LinkedList<>();
			 
			 for(LookupMapProperty row : lookupMapRows){
				 GridRow inputFieldSchema = getInputFieldSchema(row.getSource_Field());
				 GridRow outputFieldSchema = getOutputFieldSchema(inputFieldSchema,row.getOutput_Field());
				 
				 
				 if(row.getOutput_Field().equals(row.getSource_Field().split("\\.")[1])){
					 finalPassThroughFields.add(row.getOutput_Field());
					 passThroughFieldsPortInfo.put(row.getOutput_Field(), row.getSource_Field().split("\\.")[0]);
				 }else{
					 finalMapFields.put(row.getSource_Field().split("\\.")[1], row.getOutput_Field());
					 mapFieldsPortInfo.put(row.getOutput_Field(), row.getSource_Field().split("\\.")[0]);
				 }
				 
				 outputSchemaGridRowList.add(outputFieldSchema);
			 }
			 
			 addPassthroughFieldsAndMappingFieldsToComponentOuputSchema(finalMapFields, finalPassThroughFields,passThroughFieldsPortInfo,mapFieldsPortInfo);
			 
			 
			 internalSchema.getGridRow().addAll(outputSchemaGridRowList);
		
	}
	
	private void addPassthroughFieldsAndMappingFieldsToComponentOuputSchema(Map<String, String> mapFields,
			List<String> passThroughFields, Map<String, String> passThroughFieldsPortInfo, Map<String, String> mapFieldsPortInfo) {
		ComponentsOutputSchema componentsOutputSchema = (ComponentsOutputSchema) getComponent().getProperties().get(
				Constants.SCHEMA_TO_PROPAGATE);
		if (componentsOutputSchema == null){
			componentsOutputSchema = new ComponentsOutputSchema();
			getComponent().getProperties().put(Constants.SCHEMA_TO_PROPAGATE, componentsOutputSchema);
		}
		else {
			componentsOutputSchema.getPassthroughFields().clear();
			componentsOutputSchema.getMapFields().clear();
			componentsOutputSchema.getPassthroughFieldsPortInfo().clear();
			componentsOutputSchema.getMapFieldsPortInfo().clear();
		}
		componentsOutputSchema.getPassthroughFields().addAll(passThroughFields);
		componentsOutputSchema.getMapFields().putAll(mapFields);
		
		for(String field : passThroughFieldsPortInfo.keySet()){
			componentsOutputSchema.getPassthroughFieldsPortInfo().put(field, passThroughFieldsPortInfo.get(field));
		}
		
		componentsOutputSchema.getMapFieldsPortInfo().putAll(mapFieldsPortInfo);
	}	

	private GridRow getOutputFieldSchema(GridRow inputFieldSchema, String output_Field) {
		if (inputFieldSchema != null) {
			GridRow gridRow = inputFieldSchema.copy();
			gridRow.setFieldName(output_Field);
			return gridRow.copy();
		} else
			return null;
	}

	private GridRow getInputFieldSchema(String source_Field) {		
		String[] source = source_Field.split("\\.");
		return getInputFieldSchema(source[1],source[0]);
	
		
	}
	
	private GridRow getInputFieldSchema(String fieldName,String linkNumber) {
		ComponentsOutputSchema outputSchema = null;
		//List<FixedWidthGridRow> fixedWidthGridRows = new LinkedList<>();
		for (Link link : getComponent().getTargetConnections()) {
			
			if(linkNumber.equals(link.getTargetTerminal())){				
				outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
				if (outputSchema != null)
					for (FixedWidthGridRow row : outputSchema.getFixedWidthGridRowsOutputFields()) {
						if(row.getFieldName().equals(fieldName)){
							return row.copy();
						}
					}
			}
		}
		return null;
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		property.put(propertyName, joinMappingGrid);
		return property;
	}

	private void getPropagatedSchema() {
		List<List<FilterProperties>> sorceFieldList = SchemaPropagationHelper.INSTANCE
				.sortedFiledNamesBySocketId(getComponent());
		if (sorceFieldList != null)
			joinMappingGrid.setLookupInputProperties(sorceFieldList);
	}
	
}
