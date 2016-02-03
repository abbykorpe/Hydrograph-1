package com.bitwise.app.common.datastructure.property;

/**
 * @author Bitwise
 *
 */
public class MatchValueProperty {

	private Boolean radioButtonSelected;
	private String matchValue;

	public MatchValueProperty() {
		radioButtonSelected = Boolean.FALSE;
	}
	public String getMatchValue() {
		return matchValue;
	}

	public void setMatchValue(String matchValue) {
		this.matchValue = matchValue;
	}

	public Boolean isRadioButtonSelected() {
		return radioButtonSelected;
	}
	 
	public void setRadioButtonSelected(Boolean radioButtonSelected) {
		this.radioButtonSelected = radioButtonSelected;
	}
	@Override
	public String toString() {
		return "MatchValue [matchValue=" + matchValue + "]";
	}
	
	
}
