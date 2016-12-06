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
package hydrograph.ui.datastructure.property;

/**
 * The Class QueryProperty.
 * 
 * @author Bitwise
 */
public class QueryProperty {
	
	private String queryText="";

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	@Override
	public String toString() {
		return "QueryProperty [queryText=" + queryText + "]";
	}
	@Override
	public QueryProperty clone() 
	{
		QueryProperty queryProperty=new QueryProperty();
		queryProperty.setQueryText(queryText);
		return queryProperty;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((queryText == null) ? 0 : queryText.hashCode());
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
		QueryProperty other = (QueryProperty) obj;
		if (queryText == null) {
			if (other.queryText != null)
				return false;
		} else if (!queryText.equals(other.queryText))
			return false;
		return true;
	}

}
