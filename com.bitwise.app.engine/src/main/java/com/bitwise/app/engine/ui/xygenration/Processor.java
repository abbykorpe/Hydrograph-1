package com.bitwise.app.engine.ui.xygenration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Component;

public class Processor {
	public static final String NODE="node";
	LinkedHashMap<String, List<String>> mapData = new LinkedHashMap<>();
	List<Node> nodeList=new ArrayList<>();
	LinkedHashMap<String,Node> nodeMap=new LinkedHashMap<>();
	List<String> processedNodes=new ArrayList<>();
	private static final int HORIZONTAL_SPACING=130;
	private static final int VERTICAL_SPACING=100;
	boolean doRecursive=true;

	public void processLinks() {
		Node sNode=null;
		Node dNode=null;
		for (LinkingData linkingData : UIComponentRepo.INSTANCE.getComponentLinkList()) {
//			if(!linkingData.getTargetComponentId().equals(linkingData.getSourceComponentId()))
			{
			if(nodeMap.get(linkingData.getSourceComponentId())==null)
				{	sNode=new Node(linkingData.getSourceComponentId());
					nodeMap.put(linkingData.getSourceComponentId(),sNode);
					nodeList.add(sNode);
				}
			if(nodeMap.get(linkingData.getTargetComponentId())==null)
				{	dNode=new Node(linkingData.getTargetComponentId());
					nodeMap.put(linkingData.getTargetComponentId(),dNode);
					nodeList.add(dNode);
				}
			
			nodeMap.get(linkingData.getSourceComponentId()).getDestinationNodes().add(nodeMap.get(linkingData.getTargetComponentId()));
			nodeMap.get(linkingData.getTargetComponentId()).getSourceNodes().add(nodeMap.get(linkingData.getSourceComponentId()));
			}
		}
		caculateXY();
		processNodes();
	}
	private  void processNodes() {
		
		genrateYCoordinate();		
		for(Node node : nodeList){
			Point point=new Point();
			point.x=node.gethPosition();
			point.y=node.getvPosition();
			UIComponentRepo.INSTANCE.componentUiFactory.get(node.name).setLocation(point);
			System.out.println(node);
		}
	}
	
	private void genrateYCoordinate()
	{
		Component component=null;
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
		processedNodes.add(node.getName());
		doRecursive=true;
		node.sethPosition(position);
		if(node.getDestinationNodes().isEmpty()){
			processedNodes.clear();
			return;
		}
		
		for(Node destinationNode : node.getDestinationNodes()){
			
			for(String processedElement:processedNodes)
			{
				if(destinationNode.getName().equals(processedElement))
				{	processedNodes.clear();
					doRecursive=false;
					break;
				}
				
			}
			if(doRecursive)
			{calculate(destinationNode, position + HORIZONTAL_SPACING);}
		}
	}
}
	
