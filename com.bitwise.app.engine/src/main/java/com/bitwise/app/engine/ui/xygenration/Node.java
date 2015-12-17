package com.bitwise.app.engine.ui.xygenration;

import java.util.ArrayList;
import java.util.List;

public class Node {
	String name;
	private List<Node> sourceNodes = new ArrayList<Node>();
	private List<Node> destinationNodes = new ArrayList<Node>();
	private int hPosition = 0;
	private int vPosition = 0;
	
	public Node(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Node> getSourceNodes() {
		return sourceNodes;
	}
	
	public List<Node> getDestinationNodes() {
		return destinationNodes;
	}
	
	public void sethPosition(int hPosition) {
		this.hPosition = hPosition;
	}
	public int gethPosition() {
		return hPosition;
	}
	public int getvPosition() {
		return vPosition;
	}
	
	public void setvPosition(int vPosition) {
		this.vPosition = vPosition;
	}

	@Override
	public String toString() {
		return "Node [name=" + name + ", hPosition=" + hPosition + ", vPosition "+vPosition+", " +"]";//+sourceNodes;
//				"Destination "+getDestinationNodes()+", Source "+getSourceNodes()+"]";
	}
}
