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

 
package hydrograph.ui.datastructure.property.mapping;

import hydrograph.ui.common.cloneableinterface.IDataStructure;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.OperationClassProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 
 * This class stores rows in mapping sheet 
 * 
 * @author Bitwise
 *
 */
public class MappingSheetRow implements IDataStructure {

	private List<FilterProperties> inputFieldList;
	private String comboBoxValue;
	private String operationClassPath;
	private boolean isWholeOperationParameter;
	private List<FilterProperties> outputList;
	private boolean isClassParameter;
	private String operationId;
	private List<NameValueProperty> nameValuePropertyList;
    private String wholeOperationParameterValue;
    private String operationClassFullPath;
	
	

	/**
	 * Gets the operation class full path.
	 * 
	 * @return the operation class full path
	 */
	public String getOperationClassFullPath() {
		return operationClassFullPath;
	}

	/**
	 * Sets the operation class full path.
	 * 
	 * @param operationClassFullPath
	 *            the new operation class full path
	 */
	public void setOperationClassFullPath(String operationClassFullPath) {
		this.operationClassFullPath = operationClassFullPath;
	}

	/**
	 * Instantiates a new mapping sheet row.
	 * 
	 * @param input
	 *            the input
	 * @param operationClass
	 *            the operation class
	 * @param outputList
	 *            the output list
	 */
	public MappingSheetRow(List<FilterProperties> input,
			OperationClassProperty   operationClass,
			List<FilterProperties> outputList) {
		this.inputFieldList = input;
		
		this.outputList = outputList;

	}

	/**
	 * Instantiates a new mapping sheet row.
	 * 
	 * @param input
	 *            the input
	 * @param outputList
	 *            the output list
	 * @param operationId
	 *            the operation id
	 * @param comBoxValue
	 *            the com box value
	 * @param operationClassPath
	 *            the operation class path
	 * @param nameValueProperty
	 *            the name value property
	 * @param isClassParameter
	 *            the is class parameter
	 * @param wholeOperationParameterValue
	 *            the whole operation parameter value
	 * @param isWholeOperationParameter
	 *            the is whole operation parameter
	 * @param operationClassFullPath
	 *            the operation class full path
	 */
	public MappingSheetRow(List<FilterProperties> input,
			List<FilterProperties> outputList,
			String operationId,
			String comBoxValue,
			String operationClassPath,
			List<NameValueProperty> nameValueProperty,
			boolean isClassParameter,
			String wholeOperationParameterValue,
			boolean isWholeOperationParameter,
			String operationClassFullPath
			) {
		this.inputFieldList = input;
		this.outputList = outputList;
		this.comboBoxValue = comBoxValue;
		this.operationClassPath = operationClassPath;
		this.operationId=operationId;
		this.setClassParameter(isClassParameter);
		this.nameValuePropertyList=nameValueProperty;
		this.wholeOperationParameterValue=wholeOperationParameterValue;
		this.isWholeOperationParameter=isWholeOperationParameter;
		this.operationClassFullPath=operationClassFullPath;
		
    }
	
	/**
	 * Instantiates a new mapping sheet row.
	 * 
	 * @param input
	 *            the input
	 * @param outputList
	 *            the output list
	 * @param comBoxValue
	 *            the com box value
	 * @param operationClassPath
	 *            the operation class path
	 * @param isClassParameter
	 *            the is class parameter
	 * @param operationId
	 *            the operation id
	 * @param nameValueProperty
	 *            the name value property
	 */
	public MappingSheetRow(List<FilterProperties> input, List<FilterProperties> outputList, String comBoxValue,String operationClassPath,boolean isClassParameter,String operationId,
			 List<NameValueProperty> nameValueProperty) 
	{
		this.inputFieldList = input;
		this.outputList = outputList;
		this.comboBoxValue = comBoxValue;
		this.operationClassPath = operationClassPath;
		this.operationId=operationId;
		this.nameValuePropertyList=nameValueProperty;
		this.setClassParameter(isClassParameter);
	}
	
	

   /** Checks if is whole operation parameter.
	 * 
	 * @return true, if is whole operation parameter
	 */
   public boolean isWholeOperationParameter() {
		return isWholeOperationParameter;
	}

	/**
	 * Sets the whole operation parameter.
	 * 
	 * @param isWholeOperationParameter
	 *            the new whole operation parameter
	 */
	public void setWholeOperationParameter(boolean isWholeOperationParameter) {
		this.isWholeOperationParameter = isWholeOperationParameter;
	}

	/**
	 * Gets the whole operation parameter value.
	 * 
	 * @return the whole operation parameter value
	 */
	public String getWholeOperationParameterValue() {
		return wholeOperationParameterValue;
	}

	/**
	 * Sets the whole operation parameter value.
	 * 
	 * @param wholeOperationParameterValue
	 *            the new whole operation parameter value
	 */
	public void setWholeOperationParameterValue(String wholeOperationParameterValue) {
		this.wholeOperationParameterValue = wholeOperationParameterValue;
	}

	/**
	 * Gets the name value property.
	 * 
	 * @return the name value property
	 */
	public List<NameValueProperty> getNameValueProperty() {
		return nameValuePropertyList;
	}

	/**
	 * Sets the name value property.
	 * 
	 * @param nameValueProperty
	 *            the new name value property
	 */
	public void setNameValueProperty(List<NameValueProperty> nameValueProperty) {
		this.nameValuePropertyList = nameValueProperty;
	}

	/**
	 * Gets the operation ID.
	 * 
	 * @return the operation ID
	 */
	public String getOperationID() {
		return operationId;
	}

	/**
	 * Sets the operation ID.
	 * 
	 * @param operaionId
	 *            the new operation ID
	 */
	public void setOperationID(String operaionId) {
		this.operationId = operaionId;
	}

	
	
	/**
	 * Gets the combo box value.
	 * 
	 * @return the combo box value
	 */
	public String getComboBoxValue() {
		return comboBoxValue;
	}
	
	/**
	 * Sets the combo box value.
	 * 
	 * @param comboBoxValue
	 *            the new combo box value
	 */
	public void setComboBoxValue(String comboBoxValue) {
		this.comboBoxValue = comboBoxValue;
	}
	
	/**
	 * Gets the operation class path.
	 * 
	 * @return the operation class path
	 */
	public String getOperationClassPath() {
		return operationClassPath;
	}
	
	/**
	 * Sets the operation class path.
	 * 
	 * @param operationClassPath
	 *            the new operation class path
	 */
	public void setOperationClassPath(String operationClassPath) {
		this.operationClassPath = operationClassPath;
	}
	
	/**
	 * Checks if is parameter.
	 * 
	 * @return true, if is parameter
	 */
	public boolean isParameter() {
		return isWholeOperationParameter;
	}
	
	/**
	 * Sets the parameter.
	 * 
	 * @param isParameter
	 *            the new parameter
	 */
	public void setParameter(boolean isParameter) {
		this.isWholeOperationParameter = isParameter;
	}

	
	
	
	/**
	 * 
	 * @param input - list of input fields
	 * @param operationClass - operation class
	 * @param outputList - list of output fields
	 */
	
	
	/**
	 * 
	 * returns list of input fields
	 * 
	 * @return - List of input fields
	 */
	public List<FilterProperties> getInputFields() {
		if(this.inputFieldList==null)
			return new ArrayList<FilterProperties>();
		return inputFieldList;
	}

	/**
	 * 
	 * set list of input fields
	 * 
	 * @param inputFields
	 */
	public void setInputFields(List<FilterProperties> inputFields) {
		this.inputFieldList = inputFields;
	}
	
	/**
	 * 
	 * returns {@link OperationClassProperty} 
	 * 
	 * @return
	 */
	

	/**
	 * 
	 * returns list of output fields
	 * 
	 * @return - output field list 
	 */
	public List<FilterProperties> getOutputList() {
		return outputList;
	}

	/**
	 * 
	 * set list of output fields
	 * 
	 * @param outputList
	 */
	public void setOutputList(List<FilterProperties> outputList) {
		this.outputList = outputList;
	}
	
	/**
	 * Checks if is class parameter.
	 * 
	 * @return true, if is class parameter
	 */
	public boolean isClassParameter() {
		return isClassParameter;
	}

	/**
	 * Sets the class parameter.
	 * 
	 * @param isClassParameter
	 *            the new class parameter
	 */
	public void setClassParameter(boolean isClassParameter) {
		this.isClassParameter = isClassParameter;
	}
	
	

	@Override
	public Object clone(){
		List<FilterProperties> inputFieldList = new LinkedList<>();
		List<FilterProperties> outputList = new LinkedList<>();		
		List<NameValueProperty> nameValuePropertyList=new ArrayList<>();
		
		boolean isWholeOperationParameter=this.isWholeOperationParameter;
		String wholeOperationParameterValue=this.wholeOperationParameterValue;
		String comboBoxvalue=this.comboBoxValue;
		String operationClasspath=this.operationClassPath;
		boolean isClassParamter=this.isClassParameter;
		String operationId=this.operationId;
		String operationClassFullPath=this.operationClassFullPath;
		inputFieldList.addAll(this.inputFieldList);
		outputList.addAll(this.outputList);
		if(this.nameValuePropertyList!=null)
		{
		for(NameValueProperty nameValueProperty2:this.nameValuePropertyList)
		{
			NameValueProperty clonedNameValueProperty=new NameValueProperty();
			clonedNameValueProperty=nameValueProperty2.clone();
			nameValuePropertyList.add(clonedNameValueProperty);
		}
		}
		MappingSheetRow mappingSheetRow = new MappingSheetRow(inputFieldList, outputList,operationId,comboBoxvalue,operationClasspath,nameValuePropertyList,isClassParamter,wholeOperationParameterValue,isWholeOperationParameter,operationClassFullPath);
		
		return mappingSheetRow;
	}



	@Override
	public String toString() {
		return "MappingSheetRow [inputFields=" + inputFieldList + ", comboBoxValue=" + comboBoxValue
				+ ", operationClassPath=" + operationClassPath + ", isWholeOperationParameter="
				+ isWholeOperationParameter + ", outputList=" + outputList + ", isClassParameter=" + isClassParameter
				+ ", operationId=" + operationId + ", nameValueProperty=" + nameValuePropertyList
				+ ", wholeOperationParameterValue=" + wholeOperationParameterValue + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comboBoxValue == null) ? 0 : comboBoxValue.hashCode());
		result = prime * result + ((inputFieldList == null) ? 0 : inputFieldList.hashCode());
		result = prime * result + (isClassParameter ? 1231 : 1237);
		result = prime * result + (isWholeOperationParameter ? 1231 : 1237);
		result = prime * result + ((nameValuePropertyList == null) ? 0 : nameValuePropertyList.hashCode());
		result = prime * result + ((operationClassPath == null) ? 0 : operationClassPath.hashCode());
		result = prime * result + ((operationId == null) ? 0 : operationId.hashCode());
		result = prime * result + ((outputList == null) ? 0 : outputList.hashCode());
		result = prime * result
				+ ((wholeOperationParameterValue == null) ? 0 : wholeOperationParameterValue.hashCode());
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
		if (comboBoxValue == null) {
			if (other.comboBoxValue != null)
				return false;
		} else if (!comboBoxValue.equals(other.comboBoxValue))
			return false;
		if (inputFieldList == null) {
			if (other.inputFieldList != null)
				return false;
		} else if (!inputFieldList.equals(other.inputFieldList))
			return false;
		if (isClassParameter != other.isClassParameter)
			return false;
		if (isWholeOperationParameter != other.isWholeOperationParameter)
			return false;
		if (nameValuePropertyList == null) {
			if (other.nameValuePropertyList != null)
				return false;
		} else if (!nameValuePropertyList.equals(other.nameValuePropertyList))
			return false;
		if (operationClassPath == null) {
			if (other.operationClassPath != null)
				return false;
		} else if (!operationClassPath.equals(other.operationClassPath))
			return false;
		if (operationId == null) {
			if (other.operationId != null)
				return false;
		} else if (!operationId.equals(other.operationId))
			return false;
		if (outputList == null) {
			if (other.outputList != null)
				return false;
		} else if (!outputList.equals(other.outputList))
			return false;
		if (wholeOperationParameterValue == null) {
			if (other.wholeOperationParameterValue != null)
				return false;
		} else if (!wholeOperationParameterValue.equals(other.wholeOperationParameterValue))
			return false;
		return true;
	}

	
}
