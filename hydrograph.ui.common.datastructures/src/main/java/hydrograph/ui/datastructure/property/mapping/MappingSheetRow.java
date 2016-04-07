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

	private List<FilterProperties> inputFields;
	private String comboBoxValue;
	private String operationClassPath;
	private boolean isWholeOperationParameter;
	private List<FilterProperties> outputList;
	private boolean isClassParameter;
	private String operationId;
	private List<NameValueProperty> nameValueProperty;
    private String wholeOperationParameterValue;
    private String operationClassFullPath;
	
	

	public String getOperationClassFullPath() {
		return operationClassFullPath;
	}

	public void setOperationClassFullPath(String operationClassFullPath) {
		this.operationClassFullPath = operationClassFullPath;
	}

	public MappingSheetRow(List<FilterProperties> input,
			OperationClassProperty   operationClass,
			List<FilterProperties> outputList) {
		this.inputFields = input;
		
		this.outputList = outputList;

	}

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
		this.inputFields = input;
		this.outputList = outputList;
		this.comboBoxValue = comBoxValue;
		this.operationClassPath = operationClassPath;
		this.operationId=operationId;
		this.setClassParameter(isClassParameter);
		this.nameValueProperty=nameValueProperty;
		this.wholeOperationParameterValue=wholeOperationParameterValue;
		this.isWholeOperationParameter=isWholeOperationParameter;
		this.operationClassFullPath=operationClassFullPath;
		
    }
	
	public MappingSheetRow(List<FilterProperties> input, List<FilterProperties> outputList, String comBoxValue,String operationClassPath,boolean isClassParameter,String operationId,
			 List<NameValueProperty> nameValueProperty) 
	{
		this.inputFields = input;
		this.outputList = outputList;
		this.comboBoxValue = comBoxValue;
		this.operationClassPath = operationClassPath;
		this.operationId=operationId;
		this.nameValueProperty=nameValueProperty;
		this.setClassParameter(isClassParameter);
	}
	
	

   public boolean isWholeOperationParameter() {
		return isWholeOperationParameter;
	}

	public void setWholeOperationParameter(boolean isWholeOperationParameter) {
		this.isWholeOperationParameter = isWholeOperationParameter;
	}

	public String getWholeOperationParameterValue() {
		return wholeOperationParameterValue;
	}

	public void setWholeOperationParameterValue(String wholeOperationParameterValue) {
		this.wholeOperationParameterValue = wholeOperationParameterValue;
	}

	public List<NameValueProperty> getNameValueProperty() {
		return nameValueProperty;
	}

	public void setNameValueProperty(List<NameValueProperty> nameValueProperty) {
		this.nameValueProperty = nameValueProperty;
	}

	public String getOperationID() {
		return operationId;
	}

	public void setOperationID(String operaionId) {
		this.operationId = operaionId;
	}

	
	
	public String getComboBoxValue() {
		return comboBoxValue;
	}
	public void setComboBoxValue(String comboBoxValue) {
		this.comboBoxValue = comboBoxValue;
	}
	public String getOperationClassPath() {
		return operationClassPath;
	}
	public void setOperationClassPath(String operationClassPath) {
		this.operationClassPath = operationClassPath;
	}
	public boolean isParameter() {
		return isWholeOperationParameter;
	}
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
		if(this.inputFields==null)
			return new ArrayList<FilterProperties>();
		return inputFields;
	}

	/**
	 * 
	 * set list of input fields
	 * 
	 * @param inputFields
	 */
	public void setInputFields(List<FilterProperties> inputFields) {
		this.inputFields = inputFields;
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
	
	public boolean isClassParameter() {
		return isClassParameter;
	}

	public void setClassParameter(boolean isClassParameter) {
		this.isClassParameter = isClassParameter;
	}
	
	

	@Override
	public Object clone(){
		List<FilterProperties> inputFields = new LinkedList<>();
		List<FilterProperties> outputList = new LinkedList<>();		
		List<NameValueProperty> nameValueProperty=new ArrayList<>();
		
		boolean isWholeOperationParameter=this.isWholeOperationParameter;
		String wholeOperationParameterValue=this.wholeOperationParameterValue;
		String comboBoxvalue=this.comboBoxValue;
		String operationClasspath=this.operationClassPath;
		boolean isClassParamter=this.isClassParameter;
		String operationId=this.operationId;
		String operationClassFullPath=this.operationClassFullPath;
		inputFields.addAll(this.inputFields);
		outputList.addAll(this.outputList);
		if(this.nameValueProperty!=null)
		{
		for(NameValueProperty nameValueProperty2:this.nameValueProperty)
		{
			NameValueProperty clonedNameValueProperty=new NameValueProperty();
			clonedNameValueProperty=nameValueProperty2.clone();
			nameValueProperty.add(clonedNameValueProperty);
		}
		}
		MappingSheetRow mappingSheetRow = new MappingSheetRow(inputFields, outputList,operationId,comboBoxvalue,operationClasspath,nameValueProperty,isClassParamter,wholeOperationParameterValue,isWholeOperationParameter,operationClassFullPath);
		
		return mappingSheetRow;
	}



	@Override
	public String toString() {
		return "MappingSheetRow [inputFields=" + inputFields + ", comboBoxValue=" + comboBoxValue
				+ ", operationClassPath=" + operationClassPath + ", isWholeOperationParameter="
				+ isWholeOperationParameter + ", outputList=" + outputList + ", isClassParameter=" + isClassParameter
				+ ", operationId=" + operationId + ", nameValueProperty=" + nameValueProperty
				+ ", wholeOperationParameterValue=" + wholeOperationParameterValue + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comboBoxValue == null) ? 0 : comboBoxValue.hashCode());
		result = prime * result + ((inputFields == null) ? 0 : inputFields.hashCode());
		result = prime * result + (isClassParameter ? 1231 : 1237);
		result = prime * result + (isWholeOperationParameter ? 1231 : 1237);
		result = prime * result + ((nameValueProperty == null) ? 0 : nameValueProperty.hashCode());
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
		if (inputFields == null) {
			if (other.inputFields != null)
				return false;
		} else if (!inputFields.equals(other.inputFields))
			return false;
		if (isClassParameter != other.isClassParameter)
			return false;
		if (isWholeOperationParameter != other.isWholeOperationParameter)
			return false;
		if (nameValueProperty == null) {
			if (other.nameValueProperty != null)
				return false;
		} else if (!nameValueProperty.equals(other.nameValueProperty))
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
