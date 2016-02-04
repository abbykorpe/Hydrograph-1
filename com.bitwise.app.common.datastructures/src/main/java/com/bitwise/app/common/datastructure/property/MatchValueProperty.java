package com.bitwise.app.common.datastructure.property;

import com.bitwise.app.cloneableinterface.IDataStructure;

/**
 * THE Class MatchValueProperty
 * 
 * @author Bitwise
 *
 */
public class MatchValueProperty implements IDataStructure{

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
	public MatchValueProperty clone() {
		MatchValueProperty matchValueProperty = new MatchValueProperty();
		matchValueProperty.setMatchValue(getMatchValue());
		
		return matchValueProperty;
	}
	@Override
	public String toString() {
		return "MatchValue [matchValue=" + matchValue + "]";
	}
	
	
}
