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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
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

		AbstractELTWidget eltDefaultLable = new ELTDefaultLable("Propagate Field\nFrom Left");
		eltDefaultSubgroupComposite.attachWidget(eltDefaultLable);
		
		AbstractELTWidget eltDefaultButton = new ELTDefaultButton("Propagate");
		eltDefaultSubgroupComposite.attachWidget(eltDefaultButton);
      
      // for Continuous Schema Propogation
			
	  if(getComponent().isContinuousSchemaPropogationAllow())
	   {  
		  if(StringUtils.equalsIgnoreCase(getComponent().getCategory(),"STRAIGHTPULL")
				  ||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),"filter")	
				  ||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),"uniquesequence")
						)	
		  {	  
		  ComponentsOutputSchema outputSchema = null;
		for (Link link : getComponent().getTargetConnections()) {
			outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
		getSchemaForInternalPropagation().getGridRow().addAll(outputSchema.getBasicGridRowsOutputFields());
		
		}
		}
	   }	  
		 
		  //Call when Propogate from left button is Pressed
		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ComponentsOutputSchema outputSchema = null;
				getSchemaForInternalPropagation().getGridRow().clear();	
				for (Link link : getComponent().getTargetConnections()) {
					Schema schema=(Schema)link.getSource().getProperties().get("schema");
					if(schema!=null&&!schema.getGridRow().isEmpty())	
					outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
					if (outputSchema != null){
					if(StringUtils.equalsIgnoreCase(getComponent().getCategory(),"STRAIGHTPULL")
							  ||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),"filter")	
							  ||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),"uniquesequence")
							)
					{	
					
					if(schema!=null&&!schema.getGridRow().isEmpty())	
				    getSchemaForInternalPropagation().getGridRow().addAll(outputSchema.getBasicGridRowsOutputFields());
				    ELTSchemaGridWidget eltSchemaGridWidget=null;
				    for(AbstractWidget abstractWidget:widgets)
					{
						if(abstractWidget instanceof ELTSchemaGridWidget)
						{
							eltSchemaGridWidget=(ELTSchemaGridWidget)abstractWidget;
							break;
						}
					}
				    if(eltSchemaGridWidget!=null &&!outputSchema.getBasicGridRowsOutputFields().isEmpty())
				    eltSchemaGridWidget.showHideErrorSymbol(!outputSchema.getBasicGridRowsOutputFields().isEmpty());
					}
					else if(getComponent() instanceof SubjobComponent)
					{
						
					getComponent().setContinuousSchemaPropogationAllow(true);
					Container container=(Container)getComponent().getProperties().get(Constants.SUBJOB_CONTAINER);
					for(Object object:container.getChildren())
					{
						if(object instanceof Component)
						{
						Component subjobComponent=(Component)object;	
						subjobComponent.setContinuousSchemaPropogationAllow(true);
						}
					}	
					}
				    else if(
				    		StringUtils.equalsIgnoreCase(getComponent().getComponentName(),"Aggregate")
				    		||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),"Cumulate")
				    		||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),"Normalize")
				    		||StringUtils.equalsIgnoreCase(getComponent().getComponentName(),"Transform"))
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
						TransformMapping transformMapping=(TransformMapping)transformWidget.getProperties().get("operation");
						InputField inputField = null;
						transformMapping.getInputFields().clear();
							for (FixedWidthGridRow row : outputSchema.getFixedWidthGridRowsOutputFields()) {
								inputField = new InputField(row.getFieldName(), new ErrorObject(false, ""));
									transformMapping.getInputFields().add(inputField);
							}
						
					}
				    else if(StringUtils.equalsIgnoreCase(getComponent().getComponentName(),"Join"))
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
						JoinMappingGrid joinMappingGrid=(JoinMappingGrid)joinMapWidget.getProperties().get("join_mapping");
							
						joinMappingGrid.setLookupInputProperties(sorceFieldList);
						
				    }	
					
				    else if(StringUtils.equalsIgnoreCase(getComponent().getComponentName(),"lookup"))
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
						LookupMappingGrid joinMappingGrid=(LookupMappingGrid)lookupMapWidget.getProperties().get("hash_join_map");
							
						joinMappingGrid.setLookupInputProperties(sorceFieldList);
						
				    }	
				}
					
				}
				setFlagForContinuousSchemaPropogation(getComponent());
			}

			
		});
	}
	private void setFlagForContinuousSchemaPropogation(Component component) {
		for(Link link:component.getSourceConnections())
		{
			Component nextComponent=link.getTarget();
			
			while(StringUtils.equalsIgnoreCase(nextComponent.getCategory(), "STRAIGHTPULL")
					||StringUtils.equalsIgnoreCase(nextComponent.getComponentName(),"filter")	
					 ||StringUtils.equalsIgnoreCase(nextComponent.getComponentName(),"uniquesequence")
					)
			{
				nextComponent.setContinuousSchemaPropogationAllow(true);
				if(!nextComponent.getSourceConnections().isEmpty())
				{
				if(nextComponent.getSourceConnections().size()==1)	
				nextComponent=nextComponent.getSourceConnections().get(0).getTarget();
				else
				{
					setFlagForContinuousSchemaPropogation(nextComponent);
					break;
				}
				}
				else
				break;	
			}
			if(nextComponent instanceof SubjobComponent)
			{	
				nextComponent.setContinuousSchemaPropogationAllow(true);
				Container container=(Container)nextComponent.getProperties().get(Constants.SUBJOB_CONTAINER);
				for(Object object:container.getChildren())
				{
					if(object instanceof Component)
					{
					Component subjobComponent=(Component)object;
					if(subjobComponent instanceof InputSubjobComponent)
					{
						
						setFlagForContinuousSchemaPropogation(subjobComponent);
						break;
					}
					}
				}	
			}
			if(StringUtils.equalsIgnoreCase(nextComponent.getCategory(),"TRANSFORM"))
			{
				nextComponent.setContinuousSchemaPropogationAllow(true);
			}
		}
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
