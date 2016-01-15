package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;



import com.bitwise.app.cloneableinterface.CloneObject;
import com.bitwise.app.common.util.LogFactory;


public class JoinMappingGrid implements CloneObject{
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(JoinMappingGrid.class);
	private Boolean isSelected;
	private String buttonText;
	private List<LookupMapProperty> lookupMapProperties; //right side grid
	private List<LookupMapProperty> clonedLookupMapProperties;
	private List<List<FilterProperties>> lookupInputProperties;   //join left side
	private List<FilterProperties> clonedInnerLookupInputProperties;
	private List<List<FilterProperties>> clonedLookupInputProperties;
	
	public JoinMappingGrid() {
		lookupMapProperties = new ArrayList<>();
		lookupInputProperties = new ArrayList<>();
		isSelected = Boolean.FALSE;
	}	
	
	public String getButtonText() {
		return buttonText;
	}
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}
	public Boolean isSelected() {
		return isSelected;
	}
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	public List<List<FilterProperties>> getLookupInputProperties() {
		return lookupInputProperties;
	}
	public void setLookupInputProperties(List<List<FilterProperties>> lookupInputProperties) {
		this.lookupInputProperties = lookupInputProperties;
	}
	public List<LookupMapProperty> getLookupMapProperties() {
		return lookupMapProperties;
	}
	public void setLookupMapProperties(List<LookupMapProperty> lookupMapProperties) {
		this.lookupMapProperties = lookupMapProperties;
	}
	
	@Override
	public JoinMappingGrid clone() 
	{
		
		clonedLookupMapProperties=new ArrayList<>();
		clonedLookupInputProperties=new ArrayList<>();
		JoinMappingGrid joinMappingGrid=null;
		try {
			joinMappingGrid=this.getClass().newInstance();
			
		} catch (Exception e) {
			logger.debug("Unable to instantiate cloning object",e);
		}
		
		
		for(int i=0;i<lookupMapProperties.size();i++)
		{
			clonedLookupMapProperties.add(lookupMapProperties.get(i).clone());
			
		}	
		
		for(int i=0;i<lookupInputProperties.size();i++)
		{
			clonedInnerLookupInputProperties=new ArrayList<>();
			for(int j=0;j<lookupInputProperties.get(i).size();j++)
			{
				clonedInnerLookupInputProperties.add(lookupInputProperties.get(i).get(j).clone());
			}
			clonedLookupInputProperties.add(clonedInnerLookupInputProperties);
		
		}
	
		joinMappingGrid.setButtonText(getButtonText());
		joinMappingGrid.setIsSelected(isSelected());
		joinMappingGrid.setLookupInputProperties(clonedLookupInputProperties);
		joinMappingGrid.setLookupMapProperties(clonedLookupMapProperties);
		return joinMappingGrid;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JoinMappingGrid [lookupMapProperties=");
		builder.append(lookupMapProperties);
		builder.append(", lookupInputProperties=");
		builder.append(lookupInputProperties);
		builder.append("]");
		return builder.toString();
	}
}
