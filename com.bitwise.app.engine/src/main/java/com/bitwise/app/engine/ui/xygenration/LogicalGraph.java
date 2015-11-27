package com.bitwise.app.engine.ui.xygenration;

import java.util.List;

public class LogicalGraph {

	String root;
	String node;
	List<LogicalGraph>ChildList;
		
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public List<LogicalGraph> getChildList() {
		return ChildList;
	}
	public void setChildList(List<LogicalGraph> childList) {
		ChildList = childList;
	}
	
	
}
