package hydrograph.ui.propertywindow.widgets.utility;

import org.apache.commons.lang.StringUtils;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.model.components.InputSubjobComponent;
import hydrograph.ui.graph.model.components.SubjobComponent;

public class SubjobUtility {
public static final SubjobUtility INSTANCE= new SubjobUtility();
	
	private SubjobUtility(){
		
	}
	
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
					   if(!checkIfSubJobHasTransformComponent(nextComponent))
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
	
	private boolean checkIfSubJobHasTransformComponent(Component component) {
		boolean containsTransformComponent=false;
		Container container=(Container)component.getProperties().get(Constants.SUBJOB_CONTAINER);
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
				containsTransformComponent=true;
			    break;
			}
			else if(component1 instanceof SubjobComponent)
			{
				containsTransformComponent=checkIfSubJobHasTransformComponent(component1);
				if(containsTransformComponent)
				break;	
			}
			}
		}
		return containsTransformComponent;
	}
	
}
