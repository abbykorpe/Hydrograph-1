package com.bitwise.app.common.datastructure.property;

public class MatchValueProperty {

	Boolean isSelected;
	String matchValue;

	public MatchValueProperty() {
		isSelected = Boolean.FALSE;
	}
	public String getMatchValue() {
		return matchValue;
	}

	public void setMatchValue(String matchValue) {
		this.matchValue = matchValue;
	}

	public Boolean isSelected() {
		return isSelected;
	}
	 
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	@Override
	public String toString() {
		return "MatchValue [matchValue=" + matchValue + "]";
	}
	
	
}
