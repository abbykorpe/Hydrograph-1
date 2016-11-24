package hydrograph.ui.propertywindow.widgets.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.model.components.InputSubjobComponent;
import hydrograph.ui.graph.model.components.SubjobComponent;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;

public class SubjobUtility {
public static final SubjobUtility INSTANCE= new SubjobUtility();
	
     /**
	 * This method set continuousSchema propagation flag to true until it encounters transform or union All component.
	 * @param component through which continuous propagation starts. 
	 */
	public void setFlagForContinuousSchemaPropogation(Component component) {
		for(Link link:component.getSourceConnections())
		{
			Component nextComponent=link.getTarget();
			
			while(StringUtils.equalsIgnoreCase(nextComponent.getCategory(), Constants.STRAIGHTPULL)
					||StringUtils.equalsIgnoreCase(nextComponent.getComponentName(),Constants.FILTER)	
					 ||StringUtils.equalsIgnoreCase(nextComponent.getComponentName(),Constants.UNIQUE_SEQUENCE)
					 ||nextComponent instanceof SubjobComponent
					)
			{
				if(StringUtils.equalsIgnoreCase(Constants.UNION_ALL,nextComponent.getComponentName()))
				{
					if(!isUnionAllInputSchemaInSync(nextComponent))
					{	
					nextComponent.getProperties().put(Constants.IS_UNION_ALL_COMPONENT_SYNC,Constants.FALSE);
					break;
					}
					else
					nextComponent.getProperties().put(Constants.IS_UNION_ALL_COMPONENT_SYNC,Constants.TRUE);	
				}
				Schema schema=(Schema)nextComponent.getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
				if(schema==null)
				schema=new Schema();	
				ComponentsOutputSchema outputSchema=SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
				if(schema.getGridRow()==null)
				{
					List<GridRow> gridRows=new ArrayList<>();
					schema.setGridRow(gridRows);
				}	
				schema.getGridRow().clear();
				if(outputSchema!=null)
				schema.getGridRow().addAll(outputSchema.getBasicGridRowsOutputFields());
				if(!(nextComponent instanceof SubjobComponent))
				nextComponent.getProperties().put(Constants.SCHEMA_PROPERTY_NAME,schema);
				
				nextComponent.setContinuousSchemaPropogationAllow(true);
				if(nextComponent instanceof SubjobComponent)
				{	
					Container container=(Container)nextComponent.getProperties().get(Constants.SUBJOB_CONTAINER);
					for(Object object:container.getChildren())
					{
						if(object instanceof Component)
						{
						Component subjobComponent=(Component)object;
						if(subjobComponent instanceof InputSubjobComponent)
						{
							initializeSchemaMapForInputSubJobComponent(subjobComponent,nextComponent);
							setFlagForContinuousSchemaPropogation(subjobComponent);
							break;
						}
						}
					}
				}
				if(!nextComponent.getSourceConnections().isEmpty())
				{
				   if(nextComponent.getSourceConnections().size()==1)
			    	{
				     if(nextComponent instanceof SubjobComponent)
				     {
					   if(!checkIfSubJobHasTransformOrUnionAllComponent(nextComponent))
					    {
						nextComponent=nextComponent.getSourceConnections().get(0).getTarget();	
					    }
					   else
						break;   
				     }	
				    else
				    {
				    nextComponent=nextComponent.getSourceConnections().get(0).getTarget();
				    }
				   }
			       else
				   {
					setFlagForContinuousSchemaPropogation(nextComponent);
					break;
				   }
				}
				else
				break;	
			}
			if(StringUtils.equalsIgnoreCase(nextComponent.getCategory(),Constants.TRANSFORM))
			{
				nextComponent.setContinuousSchemaPropogationAllow(true);
			}
		}
	}
	
	/**
	 * check whether union compoent's  input schema are in sync or not
	 * @param union All component
	 * @return true if input schema are in sync otherwise false
	 */
	public boolean isUnionAllInputSchemaInSync(Component component) {
		List<Component>	unionAllJustPreviousComponents=new ArrayList<>();
        for(Link link:component.getTargetConnections())
        {
        	unionAllJustPreviousComponents.add(link.getSource());
        }	
        for(Component outerComponent:unionAllJustPreviousComponents)
        {
        	Schema outerSchema=(Schema)outerComponent.getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
        	
        	for(Component innerComponent:unionAllJustPreviousComponents)
        	{
        		Schema innerSchema=(Schema)innerComponent.getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
        	if(outerSchema!=null &&innerSchema!=null)
        	{	
        	if(outerSchema.getGridRow()!=null &&innerSchema.getGridRow()!=null)
        	{	
        	 if(outerSchema.getGridRow().size()!=innerSchema.getGridRow().size())
        	 {
        		 return false;
        	 }	 
        	
        		 for(GridRow inner:innerSchema.getGridRow())
        		 {
        			 if(!outerSchema.getGridRow().get(innerSchema.getGridRow().indexOf(inner)).checkGridRowEqauality(inner))
        				return false; 
        		 }	 
        	
        	}
        	}
        	else if(outerSchema!=null && innerSchema==null ||outerSchema==null &&innerSchema!=null)
        	return false;	
        	}
        }	
       return true;
	}
	/**
	 * check if sub job contains transform or union All component
	 * 
	 * @param Subjob component
	 * @return true if Sub job contains transform or union all component otherwise false
	 */
	public boolean checkIfSubJobHasTransformOrUnionAllComponent(Component component) {
		boolean containsTransformOrUnionAllComponent=false;
		Container container=(Container)component.getProperties().get(Constants.SUBJOB_CONTAINER);
		if(container!=null)
		{
		for(Object object:container.getChildren())
		{
			if(object instanceof Component)
			{
			Component component1=(Component)object;	
			if((StringUtils.equalsIgnoreCase(component1.getCategory(), Constants.TRANSFORM)
					&&!StringUtils.equalsIgnoreCase(component1.getComponentName(), Constants.FILTER)
					&&!StringUtils.equalsIgnoreCase(component1.getComponentName(), Constants.UNIQUE_SEQUENCE))
					&& component1.isContinuousSchemaPropogationAllow()
					 )
			{
				containsTransformOrUnionAllComponent=true;
			    break;
			}
			else if((StringUtils.equalsIgnoreCase( Constants.UNION_ALL, component1.getComponentName())))
			{
				if(!isUnionAllInputSchemaInSync(component1))
				{
					containsTransformOrUnionAllComponent=true;
				    break;
				}	
			}		
			else if(component1 instanceof SubjobComponent)
			{
				containsTransformOrUnionAllComponent=checkIfSubJobHasTransformOrUnionAllComponent(component1);
				if(containsTransformOrUnionAllComponent)
				break;	
			}
			}
		}
		}
		return containsTransformOrUnionAllComponent;
	}
	/**
	 * 
	 * initialize SchemaMap for inputSubjobComponent.
	 * @param inputSubJobComponent
	 * @param subjobComponent
	 */
	public void initializeSchemaMapForInputSubJobComponent(Component inputSubJobComponent,Component subjobComponent) {
		Map<String,Schema> inputSubJobComponentHashMap=new HashMap<>();
		for(int i=0;i<subjobComponent.getTargetConnections().size();i++)
		{
			inputSubJobComponentHashMap.put(Constants.INPUT_SOCKET_TYPE+i,((Schema)subjobComponent.getTargetConnections()
					.get(i).getSource().getProperties().get(Constants.SCHEMA)));
		}	
		inputSubJobComponent.getProperties().put(Constants.SCHEMA_FOR_INPUTSUBJOBCOMPONENT, inputSubJobComponentHashMap);
	}
	
	
}
