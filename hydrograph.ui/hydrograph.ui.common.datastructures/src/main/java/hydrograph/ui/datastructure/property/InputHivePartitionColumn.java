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
/**
 * 
 * @author Bitwise
 *
 */
public class InputHivePartitionColumn  implements IDataStructure{
	InputHivePartitionColumn inputHivePartitionColumn;
	String name;
	String value;
	
	public InputHivePartitionColumn getInputHivePartitionColumn() {
		return inputHivePartitionColumn;
	}
	public void setInputHivePartitionColumn(
			InputHivePartitionColumn inputHivePartitionColumn) {
		this.inputHivePartitionColumn = inputHivePartitionColumn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public Object clone() {
		
		InputHivePartitionColumn column= new InputHivePartitionColumn();
		column.setName(this.name);
		column.setValue(this.value);	
		column.setInputHivePartitionColumn(getClonedObject(this.inputHivePartitionColumn));
		
		
		return column;
	}
	
	private InputHivePartitionColumn getClonedObject(InputHivePartitionColumn column){
		
		if(null==column){
			return column;
		}
				
		InputHivePartitionColumn clonedColumn= new InputHivePartitionColumn();
		
		clonedColumn.setName(column.getName());
		clonedColumn.setValue(column.getValue());
		
		if(null!=column.getInputHivePartitionColumn()){
			clonedColumn.setInputHivePartitionColumn(getClonedObject(column.getInputHivePartitionColumn()));
		}
		
		return clonedColumn;
	}
	

 @Override
 public String toString() {
	 StringBuilder builder = new StringBuilder();
		builder.append(" InputHivePartitionColumn [name=" + getName() + ", value=" + getValue()+",InputHivePartitionColumn="+getInputHivePartitionColumn());
		builder.append("]");
		return builder.toString();
}
}
