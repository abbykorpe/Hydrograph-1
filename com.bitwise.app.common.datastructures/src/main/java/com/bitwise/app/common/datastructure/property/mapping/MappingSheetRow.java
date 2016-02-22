package com.bitwise.app.common.datastructure.property.mapping;

import java.util.LinkedList;
import java.util.List;

import com.bitwise.app.cloneableinterface.IDataStructure;
import com.bitwise.app.common.datastructure.property.OperationClassProperty;

public class MappingSheetRow implements IDataStructure {

	private List<String> inputFields;
	private OperationClassProperty operationClass;
	private List<String> outputList;
	private String CUSTOM="custom";
	
	public MappingSheetRow(List<String> imputFields, OperationClassProperty operationClass,
			List<String> outputList) {
		this.inputFields = imputFields;
		this.operationClass = operationClass;
		this.outputList = outputList;
	}
	
	public List<String> getImputFields() {
		return inputFields;
	}

	public void setImputFields(List<String> imputFields) {
		this.inputFields = imputFields;
	}
	
	public OperationClassProperty getOperationClassProperty() {
		return operationClass;
	}

	public void setOperationClassProperty(
			OperationClassProperty operationClass) {
		this.operationClass = operationClass;
	}

	public List<String> getOutputList() {
		return outputList;
	}

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
