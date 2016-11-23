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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.BasicSchemaGridRow;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.JoinMappingGrid;
import hydrograph.ui.datastructure.property.LookupMappingGrid;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.datastructure.property.mapping.ErrorObject;
import hydrograph.ui.datastructure.property.mapping.InputField;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.model.components.InputSubjobComponent;
import hydrograph.ui.graph.model.components.SubjobComponent;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import hydrograph.ui.propertywindow.widgets.customwidgets.operational.TransformWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.ELTSchemaGridWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.utility.SchemaSyncUtility;
import hydrograph.ui.propertywindow.widgets.utility.SubjobUtility;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;



/**
 * creates propogate widget in property window.
 * 
 * @author Bitwise
 */
public class PropogateWidget extends AbstractWidget{
    
	private List<AbstractWidget> widgets;
	
	public PropogateWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar)
	{
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		ELTDefaultSubgroupComposite eltDefaultSubgroupComposite=new ELTDefaultSubgroupComposite(subGroup.getContainerControl());
		eltDefaultSubgroupComposite.createContainerWidget();

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable(Constants.PROPAGATE_FIELD_FROM_LEFT);
		eltDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		
		AbstractELTWidget eltDefaultButton;
		if(OSValidator.isMac()){
			eltDefaultButton = new ELTDefaultButton(Constants.PROPAGATE).buttonWidth(120);
		}else{
			eltDefaultButton = new ELTDefaultButton(Constants.PROPAGATE);
		}
		eltDefaultSubgroupComposite.attachWidget(eltDefaultButton);
      
       if(getComponent().isContinuousSchemaPropogationAllow())
	   {  
		  if(StringUtils.equalsIgnoreCase(getComponent().getCategory(),Constants.STRAIGHTPULL)
				  ||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.FILTER)	
				  ||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.UNIQUE_SEQUENCE)
						)	
		  {	  
		for (Link link : getComponent().getTargetConnections()) {
		Schema previousComponentSchema=(Schema)link.getSource().getProperties().get(Constants.SCHEMA);
		if(previousComponentSchema!=null)
		getSchemaForInternalPropagation().getGridRow().addAll(SchemaSyncUtility.INSTANCE.
				convertGridRowsSchemaToBasicSchemaGridRows(previousComponentSchema.getGridRow()));
		if(StringUtils.equalsIgnoreCase(Constants.UNION_ALL,getComponent().getComponentName()))
		break;
		 if(StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.UNIQUE_SEQUENCE)
					&&getComponent().getProperties().get(Constants.UNIQUE_SEQUENCE_PROPERTY_NAME)!=null		 
				    )
				 {
			    String fieldName=(String)getComponent().getProperties().get(Constants.UNIQUE_SEQUENCE_PROPERTY_NAME);
			    if(StringUtils.isNotBlank(fieldName))
			    {
			    BasicSchemaGridRow basicSchemaGridRow=SchemaPropagationHelper.INSTANCE.createSchemaGridRow(fieldName);
				basicSchemaGridRow.setDataType(8);
				basicSchemaGridRow.setDataTypeValue(Long.class.getCanonicalName());
				getSchemaForInternalPropagation().getGridRow().add(basicSchemaGridRow);
			    }
				 }
		}
		}
	   }	  
		 
		  //Call when Propogate from left button is Pressed
		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				boolean isUnionAllInputSchemaSync=true;
				for (Link link : getComponent().getTargetConnections()) {
					Schema previousComponentSchema=(Schema)link.getSource().getProperties().get(Constants.SCHEMA);
					if(previousComponentSchema!=null&&!previousComponentSchema.getGridRow().isEmpty())	
				
					if (previousComponentSchema != null){
					if(StringUtils.equalsIgnoreCase(getComponent().getCategory(),Constants.STRAIGHTPULL)
							  ||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.FILTER)	
							  ||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.UNIQUE_SEQUENCE)
							)
					{	
						getSchemaForInternalPropagation().getGridRow().clear();
						if(StringUtils.equalsIgnoreCase(Constants.UNION_ALL,getComponent().getComponentName()))
						{
							if(!SubjobUtility.INSTANCE.isUnionAllInputSchemaInSync(getComponent()))
							{
							isUnionAllInputSchemaSync=false;
							getComponent().getProperties().put(Constants.IS_UNION_ALL_COMPONENT_SYNC,Constants.FALSE);
							WidgetUtility.createMessageBox(Messages.INPUTS_SCHEMA_ARE_NOT_IN_SYNC, Constants.ERROR,SWT.ICON_ERROR|SWT.OK);
							break;
							}
							else
							getComponent().getProperties().put(Constants.IS_UNION_ALL_COMPONENT_SYNC,Constants.TRUE);	
								
						}
					if(previousComponentSchema!=null)
					{
						
						 getSchemaForInternalPropagation().getGridRow().addAll(SchemaSyncUtility.INSTANCE.
								convertGridRowsSchemaToBasicSchemaGridRows(previousComponentSchema.getGridRow()));
						 
						 if(StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.UNIQUE_SEQUENCE)
							&&getComponent().getProperties().get(Constants.UNIQUE_SEQUENCE_PROPERTY_NAME)!=null		 
						    )
						 {
							String fieldName=(String)getComponent().getProperties().get(Constants.UNIQUE_SEQUENCE_PROPERTY_NAME);
						    if(StringUtils.isNotBlank(fieldName))
						    {	
							BasicSchemaGridRow basicSchemaGridRow=SchemaPropagationHelper.INSTANCE.createSchemaGridRow(fieldName);
							basicSchemaGridRow.setDataType(8);
							basicSchemaGridRow.setDataTypeValue(Long.class.getCanonicalName());
							getSchemaForInternalPropagation().getGridRow().add(basicSchemaGridRow);
						    }
						 }		 
						 getComponent().getProperties().put(Constants.SCHEMA_PROPERTY_NAME,getSchemaForInternalPropagation() );
						 if(StringUtils.equalsIgnoreCase(Constants.UNION_ALL,getComponent().getComponentName()))
						 break;	
				    }
				    ELTSchemaGridWidget eltSchemaGridWidget=null;
				    for(AbstractWidget abstractWidget:widgets)
					{
						if(abstractWidget instanceof ELTSchemaGridWidget)
						{
							eltSchemaGridWidget=(ELTSchemaGridWidget)abstractWidget;
							break;
						}
					}
				    if(eltSchemaGridWidget!=null &&!getSchemaForInternalPropagation().getGridRow().isEmpty())
				    eltSchemaGridWidget.refresh();
				    eltSchemaGridWidget.showHideErrorSymbol(!getSchemaForInternalPropagation().getGridRow().isEmpty());
					}
					else if(getComponent() instanceof SubjobComponent)
					{
					Container container=(Container)getComponent().getProperties().get(Constants.SUBJOB_CONTAINER);
					for(Component component:container.getUIComponentList())
					{
						if(component instanceof InputSubjobComponent)
						{
							getComponent().setContinuousSchemaPropogationAllow(true);
							SubjobUtility.INSTANCE.setFlagForContinuousSchemaPropogation(component);
							break;
						}
					}
					break;
					}
				    else if(
				    		StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.AGGREGATE)
				    		||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.CUMULATE)
				    		||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.NORMALIZE)
				    		||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.TRANSFORM))
					{
						TransformWidget transformWidget=null;
						for(AbstractWidget abstractWidget:widgets)
						{
							if(abstractWidget instanceof TransformWidget)
							{
							   transformWidget=(TransformWidget)abstractWidget;
								break;
							}
						}	
						TransformMapping transformMapping=(TransformMapping)transformWidget.getProperties().get(Constants.OPERATION);
						InputField inputField = null;
						transformMapping.getInputFields().clear();
						 if(previousComponentSchema!=null)
						 {	 
							for (BasicSchemaGridRow row : SchemaSyncUtility.INSTANCE.
									convertGridRowsSchemaToBasicSchemaGridRows(previousComponentSchema.getGridRow())) {
								inputField = new InputField(row.getFieldName(), new ErrorObject(false, ""));
									transformMapping.getInputFields().add(inputField);
							}
						 }
					}
				    else if(StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.JOIN))
				    {
				    	ELTJoinMapWidget  joinMapWidget = null;
						for(AbstractWidget abstractWidget:widgets)
						{
							if(abstractWidget instanceof ELTJoinMapWidget)
							{
								joinMapWidget=(ELTJoinMapWidget)abstractWidget;
								break;
							}
						}
						List<List<FilterProperties>> sorceFieldList = SchemaPropagationHelper.INSTANCE
								.sortedFiledNamesBySocketId(getComponent());
						JoinMappingGrid joinMappingGrid=(JoinMappingGrid)joinMapWidget.getProperties().get(Constants.JOIN_MAP_FIELD);
							
						joinMappingGrid.setLookupInputProperties(sorceFieldList);
						
				    }	
					
				    else if(StringUtils.equalsIgnoreCase(getComponent().getComponentName(),Constants.LOOKUP))
				    {
				    	ELTLookupMapWidget  lookupMapWidget = null;
						for(AbstractWidget abstractWidget:widgets)
						{
							if(abstractWidget instanceof ELTLookupMapWidget)
							{
								lookupMapWidget=(ELTLookupMapWidget)abstractWidget;
								break;
							}
						}
						List<List<FilterProperties>> sorceFieldList = SchemaPropagationHelper.INSTANCE
								.sortedFiledNamesBySocketId(getComponent());
						LookupMappingGrid joinMappingGrid=(LookupMappingGrid)lookupMapWidget.getProperties().get(Constants.LOOKUP_MAP_FIELD);
							
						joinMappingGrid.setLookupInputProperties(sorceFieldList);
						
				    }	
				}
					
				}
				if(isUnionAllInputSchemaSync)
				SubjobUtility.INSTANCE.setFlagForContinuousSchemaPropogation(getComponent());
			}

			
		});
	}
    
	@Override
	public LinkedHashMap<String, Object> getProperties() {
		return null;
	}

	@Override
	public boolean isWidgetValid() {
		return true;
	}

	@Override
	public void addModifyListener(Property property, ArrayList<AbstractWidget> widgetList) {
		widgets=widgetList;
		
	}

}
