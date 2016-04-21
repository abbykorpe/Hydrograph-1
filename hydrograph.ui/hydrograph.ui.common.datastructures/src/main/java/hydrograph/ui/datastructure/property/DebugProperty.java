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
 * @author Bitwise
 *
 */
public class DebugProperty {

	private String limit;
	private String basePath;
	private int comboBoxIndex;
	private Boolean isDebug;
	
	public DebugProperty() {
		isDebug = Boolean.FALSE;
	}

	
	public boolean isDebug() {
		return isDebug;
	}


	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}


	public int getComboBoxIndex() {
		return comboBoxIndex;
	}
	public void setComboBoxIndex(int comboBoxIndex) {
		this.comboBoxIndex = comboBoxIndex;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DebugProperty [limit=");
		builder.append(limit);
		builder.append(", basePath=");
		builder.append(basePath);
		builder.append("]");
		return builder.toString();
	}
	
}
