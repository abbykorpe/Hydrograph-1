package com.bitwise.app.engine.ui.xygenration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Point;

import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Component;

public class CoordinateProcessor {
	
	List<Node> nodeList=new ArrayList<>();
	LinkedHashMap<String,Node> nodeMap=new LinkedHashMap<>();
	public  LinkedHashMap<String, Component> tempUiFactory = new LinkedHashMap<>();
	private static final int HORIZONTAL_SPACING=130;
	private static final int VERTICAL_SPACING=80;
	

	public void processLinks() {
		Node sNode=null;
		Node dNode=null;
		tempUiFactory.putAll(UIComponentRepo.INSTANCE.componentUiFactory);
		for (LinkingData linkingData : UIComponentRepo.INSTANCE.getComponentLinkList()) {
			if(nodeMap.get(linkingData.getSourceComponentId())==null)
				{	sNode=new Node(linkingData.getSourceComponentId());
					nodeMap.put(linkingData.getSourceComponentId(),sNode);
					nodeList.add(sNode);
					tempUiFactory.remove(linkingData.getSourceComponentId());
				}
			if(nodeMap.get(linkingData.getTargetComponentId())==null)
				{	dNode=new Node(linkingData.getTargetComponentId());
					nodeMap.put(linkingData.getTargetComponentId(),dNode);
					nodeList.add(dNode);
					tempUiFactory.remove(linkingData.getTargetComponentId());
				}
			nodeMap.get(linkingData.getSourceComponentId()).getDestinationNodes().add(nodeMap.get(linkingData.getTargetComponentId()));
			nodeMap.get(linkingData.getTargetComponentId()).getSourceNodes().add(nodeMap.get(linkingData.getSourceComponentId()));
		}
		genrateExtraComponents();
		caculateXY();
		processNodes();
	}
	
	private void genrateExtraComponents() {
		for(Entry<String, Component> entry :tempUiFactory.entrySet() )
		{
			nodeList.add(new Node(entry.getKey()));
		}
		
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
	
