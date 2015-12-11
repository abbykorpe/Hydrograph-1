package com.bitwise.app.engine.ui.xygenration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Point;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Component;

public class CoordinateProcessor {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(CoordinateProcessor.class);
	private List<Node> nodeList=new ArrayList<>();
	private Map<String,Node> nodeMap=new LinkedHashMap<>();
	private  Map<String, Component> tempUiComponentFactory = new LinkedHashMap<>();
	private static final int HORIZONTAL_SPACING=130;
	private static final int VERTICAL_SPACING=80;
	
	public void initiateCoordinateGenration() {
		LOGGER.debug("Processing link-data for creating individual nodes");
		Node sourceNode=null;
		Node destinationNode=null;
		tempUiComponentFactory.putAll(UIComponentRepo.INSTANCE.getComponentUiFactory());
		for (LinkingData linkingData : UIComponentRepo.INSTANCE.getComponentLinkList()) {
			if(nodeMap.get(linkingData.getSourceComponentId())==null)
				{	sourceNode=new Node(linkingData.getSourceComponentId());
					nodeMap.put(linkingData.getSourceComponentId(),sourceNode);
					nodeList.add(sourceNode);
					tempUiComponentFactory.remove(linkingData.getSourceComponentId());
				}
			if(nodeMap.get(linkingData.getTargetComponentId())==null)
				{	destinationNode=new Node(linkingData.getTargetComponentId());
					nodeMap.put(linkingData.getTargetComponentId(),destinationNode);
					nodeList.add(destinationNode);
					tempUiComponentFactory.remove(linkingData.getTargetComponentId());
				}
			nodeMap.get(linkingData.getSourceComponentId()).getDestinationNodes().add(nodeMap.get(linkingData.getTargetComponentId()));
			nodeMap.get(linkingData.getTargetComponentId()).getSourceNodes().add(nodeMap.get(linkingData.getSourceComponentId()));
		}
		genrateExtraComponents();
		caculateXY();
		processNodes();
	}
	
	private void genrateExtraComponents() {
		LOGGER.debug("Generating nodes of non-connected UI-Components");
		for(Entry<String, Component> entry :tempUiComponentFactory.entrySet() )
		{
			nodeList.add(new Node(entry.getKey()));
		}
		
	}
	
	private  void processNodes() {
		LOGGER.debug("Process individual links");
		genrateYCoordinate();		
		for(Node node : nodeList){
			Point point=new Point();
			point.x=node.gethPosition();
			point.y=node.getvPosition();
			UIComponentRepo.INSTANCE.getComponentUiFactory().get(node.name).setLocation(point);
		}
	}
	
	private void genrateYCoordinate()
	{
		LOGGER.debug("Generating Y coordinates for all UI-Components");
		LinkedHashMap<Integer, Integer> ycoordinate=new LinkedHashMap<>();
		for(Node node : nodeList){
			if(ycoordinate.get(node.gethPosition())==null)
			{
				ycoordinate.put(node.gethPosition(),1);
				node.setvPosition(1);
			}
			else
			{
				ycoordinate.put(node.gethPosition(),ycoordinate.get(node.gethPosition())+VERTICAL_SPACING);
				node.setvPosition(ycoordinate.get(node.gethPosition()));
			}
		}
	}
	
	
	private void caculateXY() {
		LOGGER.debug("Calculating logical Linking and positioning of Nodes");
		int position = 1;
		List<Node> noSource = new ArrayList<>(); 
		List<Node> noDestination = new ArrayList<>(); 
		for(Node node : nodeList){
			if(node.getSourceNodes().isEmpty()){
				node.sethPosition(position);
				noSource.add(node);
				
			}
			if(node.getDestinationNodes().isEmpty()){
				noDestination.add(node);
			}
		}
		
		for(Node node : noSource){
			calculate(node, position);
		}
	}

	private void calculate(Node node, int position){
		LOGGER.debug("Applying X coordinates for UI-Components");
		node.sethPosition(position);
		if(node.getDestinationNodes().isEmpty()){
				return;
		}
		
		for(Node destinationNode : node.getDestinationNodes()){
			if(destinationNode.gethPosition()==0 && destinationNode.getvPosition()==0)
				calculate(destinationNode, position + HORIZONTAL_SPACING);
		}
	}
}
	
