package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;
import com.bitwise.app.cloneableinterface.IDataStructure;


public class LookupMappingGrid  implements IDataStructure{
	private List<List<FilterProperties>> lookupInputProperties;   //left side
	private List<List<FilterProperties>> clonedLookupInputProperties; 
	private List<FilterProperties> clonedInnerLookupInputProperties;
	private List<LookupMapProperty> lookupMapProperties; //right side grid
	private List<LookupMapProperty> clonedLookupMapProperties;
	
	//TODO


	
	public LookupMappingGrid() {
		lookupInputProperties = new ArrayList<>();
		lookupMapProperties = new ArrayList<>();
	}
	
	public List<LookupMapProperty> getLookupMapProperties() {
		return lookupMapProperties;
	}
	public void setLookupMapProperties(List<LookupMapProperty> lookupMapProperties) {
		this.lookupMapProperties = lookupMapProperties;
	}

	public List<List<FilterProperties>> getLookupInputProperties() {
		return lookupInputProperties;
	}
	public void setLookupInputProperties(List<List<FilterProperties>> lookupInputProperties) {
		this.lookupInputProperties = lookupInputProperties;
	}
	@Override
	public LookupMappingGrid clone()
	{
		clonedLookupMapProperties=new ArrayList<>();
		LookupMappingGrid lookupMappingGrid=new LookupMappingGrid();
		clonedLookupInputProperties=new ArrayList<>();
		for(int i=0;i<lookupInputProperties.size();i++)
		{
			clonedInnerLookupInputProperties=new ArrayList<>();
			for(int j=0;j<lookupInputProperties.get(i).size();j++)
			{
				clonedInnerLookupInputProperties.add(lookupInputProperties.get(i).get(j).clone());
			}
			clonedLookupInputProperties.add(clonedInnerLookupInputProperties);
		
		}
		for(int i=0;i<lookupMapProperties.size();i++)
		{
			clonedLookupMapProperties.add(lookupMapProperties.get(i).clone());
			
		}	
	
		lookupMappingGrid.setLookupInputProperties(clonedLookupInputProperties);
		lookupMappingGrid.setLookupMapProperties(clonedLookupMapProperties);
	    return lookupMappingGrid;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LookupPropertyGrid [lookupMapProperties=");
		builder.append(lookupMapProperties);
		builder.append(", lookupInputProperties=");
		builder.append(lookupInputProperties);
		builder.append(", filterList=");
		
	/*	builder.append(", joinConfigProperties=");
		builder.append(joinConfigProperties);*/
		builder.append("]");
		return builder.toString();
	}
	
	
}
