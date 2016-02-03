package com.bitwise.app.common.datastructure.property;

/**
 * @author vibhort
 *
 */
public class MatchValueProperty {

	private Boolean radioButtonSelection;
	private String matchValue;

	public MatchValueProperty() {
		radioButtonSelection = Boolean.FALSE;
	}
	public String getMatchValue() {
		return matchValue;
	}

	public void setMatchValue(String matchValue) {
		this.matchValue = matchValue;
	}

	public Boolean isSelected() {
		return radioButtonSelection;
	}
	 
	public void setIsSelected(Boolean isSelected) {
		this.radioButtonSelection = isSelected;
	}
	@Override
	public String toString() {
		return "MatchValue [matchValue=" + matchValue + "]";
	}
	
	
}
