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

package hydrograph.ui.dataviewer.preferances;

public class ViewDataPreferences {
	private String delimiter;
	private String quoteCharactor;
	private Boolean includeHeaders;
	private String fileSize;
	private String pageSize;

	public ViewDataPreferences() {
		delimiter = ",";
		quoteCharactor = "\"";
		includeHeaders = true;
		fileSize="100";
		pageSize="100";

	}
	public ViewDataPreferences(String delimiter, String quoteCharactor, boolean includeHeaders,String fileSize,String pageSize) {
		this.delimiter=delimiter;
		this.quoteCharactor=quoteCharactor;
		this.includeHeaders=includeHeaders;
		this.fileSize=fileSize;
		this.pageSize=pageSize;
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
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	
}
