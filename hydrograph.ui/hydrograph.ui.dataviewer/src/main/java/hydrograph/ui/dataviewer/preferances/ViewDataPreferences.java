package hydrograph.ui.dataviewer.preferances;



public class ViewDataPreferences {
	private String delimiter;
	private String quoteCharactor;
	private Boolean includeHeaders;

	public ViewDataPreferences() {
		delimiter = ",";
		quoteCharactor = "\"";
		includeHeaders = true;

	}

	public ViewDataPreferences(String delimiter, String quoteCharactor, boolean includeHeaders) {
		this.delimiter=delimiter;
		this.quoteCharactor=quoteCharactor;
		this.includeHeaders=includeHeaders;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getQuoteCharactor() {
		return quoteCharactor;
	}

	public void setQuoteCharactor(String quoteCharactor) {
		this.quoteCharactor = quoteCharactor;
	}

	public Boolean getIncludeHeaders() {
		return includeHeaders;
	}

	public void setIncludeHeaders(Boolean includeHeaders) {
		this.includeHeaders = includeHeaders;
	}

}
