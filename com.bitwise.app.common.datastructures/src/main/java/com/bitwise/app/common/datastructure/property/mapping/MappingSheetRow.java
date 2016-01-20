package com.bitwise.app.common.datastructure.property.mapping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.bitwise.app.cloneableinterface.IDataStructure;

public class MappingSheetRow implements IDataStructure {

	private List<String> inputFields;
	private String operationClass;
	private List<String> outputList;
	
	public MappingSheetRow(List<String> imputFields, String operationClass,
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
	public String getOperationClass() {
		return operationClass;
	}
	public void setOperationClass(String operationClass) {
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
		String operationClass="" ;
		List<String> outputList = new LinkedList<>();		
		
		inputFields.addAll(this.inputFields);
		outputList.addAll(this.outputList);
		operationClass = this.operationClass;
		MappingSheetRow mappingSheetRow = new MappingSheetRow(inputFields, operationClass, outputList);
		
		return mappingSheetRow;
	}

	@Override
	public String toString() {
		return "MappingSheetRow [inputFields=" + inputFields
				+ ", operationClass=" + operationClass + ", outputList="
				+ outputList + "]";
	}
	
	
	
}
