package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;

public class JoinConfigTextProperty {

	private List propertyList;
	
	public JoinConfigTextProperty(){
		propertyList = new ArrayList<>();
	}

	public List getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List propertyList) {
		this.propertyList = propertyList;
	}
	
	
}
