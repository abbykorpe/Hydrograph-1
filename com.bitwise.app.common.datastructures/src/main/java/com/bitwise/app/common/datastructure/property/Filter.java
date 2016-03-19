/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
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
