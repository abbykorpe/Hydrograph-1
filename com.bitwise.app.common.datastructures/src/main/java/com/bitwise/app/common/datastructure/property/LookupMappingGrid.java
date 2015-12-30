package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;

public class LookupMappingGrid {
	
	private List<List<FilterProperties>> lookupInputProperties;   //left side
	private List<LookupMapProperty> lookupMapProperties; //right side grid
	
	//TODO
	private List<Filter> filterList; 							
	private List<JoinConfigProperty> joinConfigProperties;
	
	public LookupMappingGrid() {
		lookupInputProperties = new ArrayList<>();
		lookupMapProperties = new ArrayList<>();
	}
	
	public List<JoinConfigProperty> getJoinConfigProperties() {
		return joinConfigProperties;
	}
	public void setJoinConfigProperties(
			List<JoinConfigProperty> joinConfigProperties) {
		this.joinConfigProperties = joinConfigProperties;
	}
	public List<Filter> getFilterList() {
		return filterList;
	}
	public void setFilterList(List<Filter> filterList) {
		this.filterList = filterList;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LookupPropertyGrid [lookupMapProperties=");
		builder.append(lookupMapProperties);
		builder.append(", lookupInputProperties=");
		builder.append(lookupInputProperties);
		builder.append(", filterList=");
		builder.append(filterList);
		builder.append(", joinConfigProperties=");
		builder.append(joinConfigProperties);
		builder.append("]");
		return builder.toString();
	}
	
	
}
