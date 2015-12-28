package com.bitwise.app.common.datastructure.property;

import java.util.List;

public class LookupPropertyGrid {
	
	private List<LookupMapProperty> lookupMapProperties;
	private List<List<FilterProperties>> lookupInputProperties;
	private List<Filter> filterList;
	private List<JoinConfigTextProperty> joinConfigText;
	private List<LookupConfigProperty> lookupConfigProperties;
	
	
	public List<LookupConfigProperty> getLookupConfigProperties() {
		return lookupConfigProperties;
	}
	public void setLookupConfigProperties(
			List<LookupConfigProperty> lookupConfigProperties) {
		this.lookupConfigProperties = lookupConfigProperties;
	}
	public List<JoinConfigTextProperty> getJoinConfigText() {
		return joinConfigText;
	}
	public void setJoinConfigText(List<JoinConfigTextProperty> joinConfigText) {
		this.joinConfigText = joinConfigText;
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
		return "LookupPropertyGrid [lookupMapProperties=" + lookupMapProperties + ", lookupInputProperties="
				+ lookupInputProperties + "]";
	}
	
	
}
