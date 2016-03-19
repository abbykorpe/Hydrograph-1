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

 
package com.bitwise.app.common.datastructure.property.mapping;

import java.util.LinkedList;
import java.util.List;

import com.bitwise.app.cloneableinterface.IDataStructure;
import com.bitwise.app.common.datastructure.property.OperationClassProperty;

/**
 * 
 * This class stores rows in mapping sheet 
 * 
 * @author Bitwise
 *
 */
public class MappingSheetRow implements IDataStructure {

	private List<String> inputFields;
	private OperationClassProperty operationClass;
	private List<String> outputList;
	private String CUSTOM="custom";
	
	/**
	 * 
	 * @param input - list of input fields
	 * @param operationClass - operation class
	 * @param outputList - list of output fields
	 */
	public MappingSheetRow(List<String> input, OperationClassProperty operationClass,
			List<String> outputList) {
		this.inputFields = input;
		this.operationClass = operationClass;
		this.outputList = outputList;
	}
	
	/**
	 * 
	 * returns list of input fields
	 * 
	 * @return - List of input fields
	 */
	public List<String> getInputFields() {
		return inputFields;
	}

	/**
	 * 
	 * set list of input fields
	 * 
	 * @param inputFields
	 */
	public void setInputFields(List<String> inputFields) {
		this.inputFields = inputFields;
	}
	
	/**
	 * 
	 * returns {@link OperationClassProperty} 
	 * 
	 * @return
	 */
	public OperationClassProperty getOperationClassProperty() {
		return operationClass;
	}
	
	/**
	 * set {@link OperationClassProperty}
	 * 
	 * @param operationClass
	 */
	public void setOperationClassProperty(
			OperationClassProperty operationClass) {
		this.operationClass = operationClass;
	}

	/**
	 * 
	 * returns list of output fields
	 * 
	 * @return - output field list 
	 */
	public List<String> getOutputList() {
		return outputList;
	}

	/**
	 * 
	 * set list of output fields
	 * 
	 * @param outputList
	 */
	public void setOutputList(List<String> outputList) {
		this.outputList = outputList;
	}

	@Override
	public Object clone(){
		
		List<String> inputFields = new LinkedList<>();
		OperationClassProperty operationClass = new OperationClassProperty(CUSTOM,"", false, "");
		List<String> outputList = new LinkedList<>();		
		
		inputFields.addAll(this.inputFields);
		outputList.addAll(this.outputList);
		if(this.operationClass != null)
			operationClass = this.operationClass.clone();
		
		MappingSheetRow mappingSheetRow = new MappingSheetRow(inputFields, operationClass, outputList);
		
		return mappingSheetRow;
	}

	@Override
	public String toString() {
		return "MappingSheetRow [inputFields=" + inputFields
				+ ", operationClass=" + operationClass + ", outputList="
				+ outputList + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((CUSTOM == null) ? 0 : CUSTOM.hashCode());
		result = prime * result + ((inputFields == null) ? 0 : inputFields.hashCode());
		result = prime * result + ((operationClass == null) ? 0 : operationClass.hashCode());
		result = prime * result + ((outputList == null) ? 0 : outputList.hashCode());
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
		MappingSheetRow other = (MappingSheetRow) obj;
		if (CUSTOM == null) {
			if (other.CUSTOM != null)
				return false;
		} else if (!CUSTOM.equals(other.CUSTOM))
			return false;
		if (inputFields == null) {
			if (other.inputFields != null)
				return false;
		} else if (!inputFields.equals(other.inputFields))
			return false;
		if (operationClass == null) {
			if (other.operationClass != null)
				return false;
		} else if (!operationClass.equals(other.operationClass))
			return false;
		if (outputList == null) {
			if (other.outputList != null)
				return false;
		} else if (!outputList.equals(other.outputList))
			return false;
		return true;
	}
}
