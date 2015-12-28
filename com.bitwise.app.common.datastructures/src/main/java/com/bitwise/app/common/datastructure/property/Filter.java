package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;

public class Filter {
	 
	private List<FilterProperties> filterList;
	private List proprtyList;
	
	public Filter(){
		filterList = new ArrayList<>();
	}

	
	public List getProprtyList() {
		return proprtyList;
	}


	public void setProprtyList(List proprtyList) {
		this.proprtyList = proprtyList;
	}


	public List<FilterProperties> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<FilterProperties> filterList) {
		this.filterList = filterList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((filterList == null) ? 0 : filterList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Filter other = (Filter) obj;
		if (filterList == null) {
			if (other.filterList != null)
				return false;
		} else if (!filterList.equals(other.filterList))
			return false;
		return true;
	}
	
	
}
