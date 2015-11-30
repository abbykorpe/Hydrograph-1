package com.bitwise.app.engine.ui.xygenration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Point;

import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwise.app.graph.model.Component;

public class Processor {
	public static final String NODE="node";
	HashMap<String, List<String>> mapData = new HashMap<>();

	public void processLinks() {
		int Counter = 1;
		List<String> tempLst;
		for (LinkingData linkingData : UIComponentRepo.INSTANCE.getComponentLinkList()) {
			if (Counter == 1) {
				intializeMap();
				ArrayList<String> lst2 = new ArrayList<>();
				lst2.add(linkingData.getTargetComponentId());
				mapData.put(linkingData.getSourceComponentId(), lst2);
			} else {
				tempLst = mapData.get(linkingData.getSourceComponentId());
				if (tempLst == null) {
					ArrayList<String> childlst = new ArrayList<>();
					childlst.add(linkingData.getTargetComponentId());
					mapData.put(linkingData.getSourceComponentId(), childlst);
				} else {
					tempLst.add(linkingData.getTargetComponentId());
				}
			}
			Counter++;
		}
		setNode();
		showLogicalGraph();
		genrateCordinates();
	}

	private void genrateCordinates() {
		Component uiComponent;
		int x=0;
		int y=0;
		int counter=1;
		System.out.println("Genrating Coordinates");
		for(Entry<String, List<String>> entry :mapData.entrySet() )
		{ 	if(counter==1)
			{
			
			}
			for(String componentId:mapData.get(entry.getKey()))
			{
				uiComponent=UIComponentRepo.INSTANCE.componentUiFactory.get(componentId);
				Point newLocation=new Point();
				newLocation.x=x;
				newLocation.y=y;
				uiComponent.setLocation(newLocation);
				System.out.println(componentId+"["+x+"]"+"["+y+"]");
				y=y+40;
				
			}
			
			x=x+40;
			
		}
	}


	
	private void intializeMap() {
		ArrayList<String> tempLst = new ArrayList<>();
		mapData.put(NODE, tempLst);
		
	}

	private void setNode() {
		
		for(Entry<String, List<String>> entry :mapData.entrySet() )
		{
			if(entry.getKey()!=NODE)
			{
				if(isNode(entry.getKey()))
				{
					mapData.get(NODE).add(entry.getKey());
				}
			}
		}
		
	}

	private boolean isNode(String sourceComponentId) {
		for(Entry<String, List<String>> entry :mapData.entrySet() )
		{
			for(String str:mapData.get(entry.getKey()))
				{
				if(str.equals(sourceComponentId))
					return false;
				}
		}			
		return true;
	}

	public void showLogicalGraph()
	{
		System.out.println(mapData);
	}
	
	
}
