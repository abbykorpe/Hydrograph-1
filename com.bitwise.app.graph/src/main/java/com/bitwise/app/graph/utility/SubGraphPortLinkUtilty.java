package com.bitwise.app.graph.utility;

import java.util.LinkedHashMap;
import java.util.List;

import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.components.SubgraphComponent;

/**
 * Class SubGraphPortLinkUtilty has business logic to link the port in sub graph component.
 * @author Bitwise
 *
 */
public class SubGraphPortLinkUtilty {
	
	public static void addInputSubGraphComponentAndLink(Container container,List<Component> cacheInputSubgraphComp,List clipboardList){
	 int inPort=0;
	 int outPort=0;
	 SubgraphComponent inputSubComponent=new SubgraphComponent();
		for (Component com : cacheInputSubgraphComp) {
	   		inputSubComponent.setProperties(new LinkedHashMap<String,Object>());
					for(int j=com.getTargetConnections().size();j<com.getInPortCount();j++) {
						Link linkNew = new Link();
						linkNew.setTarget(com);
						linkNew.setSource(inputSubComponent); 
						linkNew.setTargetTerminal("in"+j);
						linkNew.setSourceTerminal("in"+inPort);
						com.connectInput(linkNew);
						inputSubComponent.connectOutput(linkNew);
						inPort++; 
						}	
					
					inputSubComponent.getProperties().put("type", "inputsubgraph");							   	
				   	container.addChild((Component) com);
				   	clipboardList.remove(com);						
				}
	   	if(cacheInputSubgraphComp.size()>0){
	   		inputSubComponent.getProperties().put("name", "subgraph");
	   		inputSubComponent.setComponentLabel("subgraph");
	   		inputSubComponent.inputPortSettings(inPort);	
	   		inputSubComponent.outputPortSettings(outPort);
	   		inputSubComponent.setParent(container); 
	   		container.addChild(inputSubComponent);
	   	}
	}

	
	public static void addOutputSubGraphComponentAndLink(Container container,List<Component> cacheInputSubgraphComp,List<Component> cacheOutSubgraphComp,List clipboardList){
		 int inPort=0;
		 int outPort=0;
		 SubgraphComponent outSubComponent=new SubgraphComponent();
		   	for (Component com : cacheOutSubgraphComp) {
		   		outSubComponent.setProperties(new LinkedHashMap<String,Object>());
							Link linkNew = new Link();
							linkNew.setSource(com);
							linkNew.setTarget(outSubComponent);
							linkNew.setSourceTerminal("out0");
							linkNew.setTargetTerminal("out"+outPort);
							com.connectOutput(linkNew);
							outSubComponent.connectInput(linkNew);
							outSubComponent.getProperties().put("type", "outputsubgraph");			
							outPort++;
							if(!cacheInputSubgraphComp.contains(com)){
							container.addChild((Component) com);
							clipboardList.remove(com);
							}
					}
		   	if(cacheOutSubgraphComp.size()>0){
		   		outSubComponent.getProperties().put("name", "subgraph");
		   		outSubComponent.setComponentLabel("subgraph");
		   		outSubComponent.inputPortSettings(inPort);	
		   		outSubComponent.outputPortSettings(outPort);
		   		outSubComponent.setParent(container); 
		   		container.addChild(outSubComponent);
		   	}
		}
	
}
