package hydrograph.ui.datastructure.property;

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
