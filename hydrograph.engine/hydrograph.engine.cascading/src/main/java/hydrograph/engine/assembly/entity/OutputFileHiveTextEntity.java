/*******************************************************************************
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
 *******************************************************************************/
/**
 * 
 */
package hydrograph.engine.assembly.entity;

import hydrograph.engine.assembly.entity.base.HiveEntityBase;

/**
 * @author bitwise1
 *
 */
public class OutputFileHiveTextEntity  extends HiveEntityBase{
	
	private String quote;
	private String delimiter;
	private boolean safe;
	private boolean strict;
	
	
	/**
	 * @return the quote
	 */
	public String getQuote() {
		return quote;
	}
	/**
	 * @param quote the quote to set
	 */
	public void setQuote(String quote) {
		this.quote = quote;
	}
	/**
	 * @return the safe
	 */
	public boolean isSafe() {
		return safe;
	}
	/**
	 * @param safe the safe to set
	 */
	public void setSafe(boolean safe) {
		this.safe = safe;
	}
	/**
	 * @return the strict
	 */
	public boolean isStrict() {
		return strict;
	}
	/**
	 * @param strict the strict to set
	 */
	public void setStrict(boolean strict) {
		this.strict = strict;
	}
	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}
	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	
	
	
}