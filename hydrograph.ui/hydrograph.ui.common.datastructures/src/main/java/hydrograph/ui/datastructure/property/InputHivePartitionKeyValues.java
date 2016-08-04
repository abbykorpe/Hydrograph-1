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

import hydrograph.ui.common.cloneableinterface.IDataStructure;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Bitwise
 *
 */
public class InputHivePartitionKeyValues implements IDataStructure {

	private List<String> key;
	private List<InputHivePartitionColumn> keyValues;
	
	public List<String> getKey() {
		return key;
	}

	public void setKey(List<String> key) {
		this.key = key;
	}

	public List<InputHivePartitionColumn> getKeyValues() {
		return keyValues;
	}

	public void setKeyValues(List<InputHivePartitionColumn> keyValues) {
		this.keyValues = keyValues;
	}

	@Override
	public Object clone() {
		
	InputHivePartitionKeyValues inputHivePartitionKeyValues = new InputHivePartitionKeyValues();
	
	List<String> forCloneKeys = new ArrayList<>(this.key);
	
	List<InputHivePartitionColumn> forCloneKeyValues = new ArrayList<>();
		
	 for (InputHivePartitionColumn inputHivePartitionColumn : this.keyValues) {
	
		 forCloneKeyValues.add((InputHivePartitionColumn) inputHivePartitionColumn.clone());
	
	 }
	
	inputHivePartitionKeyValues.setKey(forCloneKeys);
	inputHivePartitionKeyValues.setKeyValues(forCloneKeyValues);
	
	return inputHivePartitionKeyValues;
	
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InputHivePartitionColumn [key=" + getKey()+ ", keyValue=" + getKeyValues());
		builder.append("]");
		return builder.toString();
	}
}
