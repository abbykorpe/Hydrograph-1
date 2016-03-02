package com.bitwise.app.graph.utility;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.components.InputSubgraphComponent;
import com.bitwise.app.graph.model.components.OutputSubgraphComponent;
import com.bitwise.app.graph.model.components.UnionallComponent;

/**
 * Class SubGraphPortLinkUtilty has business logic to link the port in sub graph component.
 * @author Bitwise
 *
 */
public class SubGraphPortLinkUtilty {
	
	/**
	 * Adds the input sub graph component and link.
	 *
	 * @param container the container
	 * @param cacheInputSubgraphComp map of component and number of port.
	 * @param clipboardList the clipboard list
	 * @return the component
	 */
	public static Component addInputSubGraphComponentAndLink(Container container,Map<Component,Integer> cacheInputSubgraphComp,List clipboardList){
	 int outPort=0;
	 InputSubgraphComponent inputSubComponent=new InputSubgraphComponent();
	 for (Map.Entry<Component,Integer> entry : cacheInputSubgraphComp.entrySet()) {
	   		inputSubComponent.setProperties(new LinkedHashMap<String,Object>());
					for(int j=0;j<entry.getValue();j++) {
						Link linkNew = new Link();
						linkNew.setTarget(entry.getKey());
						linkNew.setSource(inputSubComponent); 
						if(entry.getKey() instanceof UnionallComponent)
						linkNew.setTargetTerminal(Constants.IN_0);
						else
						linkNew.setTargetTerminal(Constants.INPUT_SOCKET_TYPE+j);
						linkNew.setSourceTerminal(Constants.OUTPUT_SOCKET_TYPE+outPort);
						entry.getKey().connectInput(linkNew);
						inputSubComponent.connectOutput(linkNew);
						outPort++; 
						}	
					
					inputSubComponent.getProperties().put(Constants.TYPE, Constants.INPUTSUBGRAPH);							   	
				   	container.addSubGraphChild((Component) entry.getKey());
				   	clipboardList.remove(entry.getKey());						
				}
	   	if(cacheInputSubgraphComp.size()>0){
	   		inputSubComponent.getProperties().put(Constants.NAME, Constants.INPUTSUBGRAPH);
	   		inputSubComponent.setComponentLabel(Constants.INPUTSUBGRAPH);
	   		inputSubComponent.setCategory(Constants.SUBGRAPH_COMPONENT_CATEGORY);
	   		inputSubComponent.outputPortSettings(outPort);
	   		fixComponentSize(inputSubComponent, outPort);
	   		setXYCoordinate(inputSubComponent,0 , 0);
	   		inputSubComponent.setParent(container); 
	   		container.addSubGraphChild(inputSubComponent);
	   	}
	   	return inputSubComponent;
	}

	
	/**
	 * Adds the output sub graph component and link.
	 *
	 * @param container the container
	 * @param cacheInputSubgraphComp the cache input subgraph comp
	 * @param cacheOutSubgraphComp the cache out subgraph comp
	 * @param clipboardList the clipboard list
	 * @return the component
	 */
	public static Component addOutputSubGraphComponentAndLink(Container container,Map<Component,Integer> cacheInputSubgraphComp,Map<Component,List<String>> cacheOutSubgraphComp,List clipboardList){
		 int inPort=0;
		OutputSubgraphComponent outSubComponent=new OutputSubgraphComponent();
		Map<String, ComponentsOutputSchema> schemaMap = new HashMap<String, ComponentsOutputSchema>();
		for (Map.Entry<Component,List<String>> entry : cacheOutSubgraphComp.entrySet()) {
		   					outSubComponent.setProperties(new LinkedHashMap<String,Object>());
		   					for (String sourceTerminal : entry.getValue()) {
		   					Link linkNew = new Link();
							linkNew.setSource(entry.getKey());
							linkNew.setTarget(outSubComponent);
							linkNew.setSourceTerminal(sourceTerminal);
							linkNew.setTargetTerminal(Constants.INPUT_SOCKET_TYPE+inPort);
							entry.getKey().connectOutput(linkNew);
							outSubComponent.connectInput(linkNew);
							outSubComponent.getProperties().put(Constants.TYPE, Constants.OUTPUTSUBGRAPH);			
							inPort++;
		   					}		   					
							if(cacheInputSubgraphComp.get(entry.getKey())==null){
								container.addSubGraphChild(entry.getKey());
								clipboardList.remove(entry.getKey());
								}							

		   	}
		   	if(cacheOutSubgraphComp.size()>0){
		   		outSubComponent.getProperties().put(Constants.NAME,Constants.OUTPUTSUBGRAPH);
		   		outSubComponent.setComponentLabel(Constants.OUTPUTSUBGRAPH);
		   		outSubComponent.setCategory(Constants.SUBGRAPH_COMPONENT_CATEGORY);
		   		outSubComponent.inputPortSettings(inPort);	
		   		fixComponentSize(outSubComponent, inPort);
		   		setXYCoordinate(outSubComponent,getMaxXCoordinate(container)+300 , 0);
		   		outSubComponent.getProperties().put(Constants.SCHEMA_TO_PROPAGATE,schemaMap);
		   		outSubComponent.setParent(container); 
		   		container.addSubGraphChild(outSubComponent);
		   		
		   	}
		   	return outSubComponent;
		}
		 
/**
 * Gets the max x coordinate.
 *
 * @param container the container
 * @return the max x coordinate
 */
public static int getMaxXCoordinate(Container container){
	int maxXCoordinate=Integer.MIN_VALUE;
	for (Component component : container.getChildren()) {
		 if(component.getLocation().x > maxXCoordinate){
			 maxXCoordinate = component.getLocation().x ;
	        }
	}
	return maxXCoordinate;
}

/**
 * Fix component size.
 *
 * @param component the component
 * @param portCount the port count
 */
public static void fixComponentSize(Component component,int portCount){
		Dimension newSize = component.getSize();
		component.setSize(newSize.expand(0, portCount * 15));
}

/**
 * Sets the xy coordinate.
 *
 * @param component the component
 * @param x the x Coordinate
 * @param y the y Coordinate
 */
public static void setXYCoordinate(Component component,int x ,int y){
Point point = new Point();
point.x=x;
point.y=y;
component.setLocation(point);
}
	
}
