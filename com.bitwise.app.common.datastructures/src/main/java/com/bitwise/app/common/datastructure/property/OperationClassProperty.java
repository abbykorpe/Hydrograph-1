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



import com.bitwise.app.cloneableinterface.IDataStructure;


/**
 * The Class OperationClassProperty.
 * 
 * @author Bitwise
 */
public class OperationClassProperty implements IDataStructure{


	private String comboBoxValue;
	private String operationClassPath;
	private boolean isParameter;
	private String operationClassFullPath;
	
	/**
	 * Instantiates a new operation class property.
	 * 
	 * @param operationClassPath
	 *            the operation class path
	 * @param isParameter
	 *            the is parameter
	 */
	public OperationClassProperty(String comboBoxValue, String operationClassPath, boolean isParameter) {
		this.operationClassPath = operationClassPath;
		this.isParameter = isParameter;
		this.comboBoxValue = comboBoxValue;
	}
	
	/**
	 * Instantiates a new operation class property.
	 * 
	 * @param comboBoxValue
	 * @param operationClassPath
	 * @param isParameter
	 * @param operationClassFullPath
	 */
	public OperationClassProperty(String comboBoxValue,String operationClassPath, boolean isParameter,String operationClassFullPath) {
		this.comboBoxValue=comboBoxValue;
		this.operationClassPath = operationClassPath;
		this.isParameter = isParameter;
		this.operationClassFullPath=operationClassFullPath;
	}
	
	/**
	 * 
	 * returns combobox value from Opeartion class widget
	 * 
	 * @return
	 */
	public String getComboBoxValue() {
		return comboBoxValue;
	}
	
	/**
	 * 
	 * Returns operation class path
	 * 
	 * @return operation class path
	 */
	public String getOperationClassPath() {
		return operationClassPath;
	}
	
	/**
	 * 
	 * returns true if user added parameter instead of operation class
	 * 
	 * @return boolean
	 */
	public boolean isParameter() {
		return isParameter;
	}
	
	/**
	 * 
	 * returns full operation class path
	 * 	
	 * @return
	 */
	public String getOperationClassFullPath() {
		return operationClassFullPath;
	}
	
	/**
	 * 
	 * set full operation class path
	 * 
	 * @param operationClassFullPath
	 */
	public void setOperationClassFullPath(String operationClassFullPath) {
		this.operationClassFullPath = operationClassFullPath;
	}
	
	@Override
	public OperationClassProperty clone() {
		return new OperationClassProperty(comboBoxValue,operationClassPath,isParameter,operationClassFullPath);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comboBoxValue == null) ? 0 : comboBoxValue.hashCode());
		result = prime * result + (isParameter ? 1231 : 1237);
		result = prime * result + ((operationClassFullPath == null) ? 0 : operationClassFullPath.hashCode());
		result = prime * result + ((operationClassPath == null) ? 0 : operationClassPath.hashCode());
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
		OperationClassProperty other = (OperationClassProperty) obj;
		if (comboBoxValue == null) {
			if (other.comboBoxValue != null)
				return false;
		} else if (!comboBoxValue.equals(other.comboBoxValue))
			return false;
		if (isParameter != other.isParameter)
			return false;
		if (operationClassFullPath == null) {
			if (other.operationClassFullPath != null)
				return false;
		} else if (!operationClassFullPath.equals(other.operationClassFullPath))
			return false;
		if (operationClassPath == null) {
			if (other.operationClassPath != null)
				return false;
		} else if (!operationClassPath.equals(other.operationClassPath))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return operationClassPath;
	}
}
