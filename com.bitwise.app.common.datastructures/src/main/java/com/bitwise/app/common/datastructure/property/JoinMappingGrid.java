package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;

public class JoinMappingGrid {
	private List<LookupMapProperty> lookupMapProperties; //right side grid
	private List<List<FilterProperties>> lookupInputProperties;   //join left side
	private List<JoinConfigProperty> joinConfigProperties;
	
	public JoinMappingGrid() {
		lookupMapProperties = new ArrayList<>();
		lookupInputProperties = new ArrayList<>();
	}
	
	
	public List<JoinConfigProperty> getJoinConfigProperties() {
		return joinConfigProperties;
	}
	public void setJoinConfigProperties(
			List<JoinConfigProperty> joinConfigProperties) {
		this.joinConfigProperties = joinConfigProperties;
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
