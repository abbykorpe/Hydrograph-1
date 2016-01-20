package com.bitwise.app.common.datastructure.property.mapping;

import com.bitwise.app.cloneableinterface.IDataStructure;


public class InputField implements IDataStructure{
	private String fieldName;
	private ErrorObject errorObject;
	
	public InputField(String fieldName) {
		super();
		this.fieldName = fieldName;
		errorObject = new ErrorObject(false, "");
	}
	
	public InputField(String fieldName, ErrorObject errorObject) {
		super();
		this.fieldName = fieldName;
		this.errorObject = errorObject;
	}



	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public ErrorObject getErrorObject() {
		return errorObject;
	}

	public void setErrorObject(ErrorObject errorObject) {
		this.errorObject = errorObject;
	}
	
	@Override
	public Object clone(){
		return new InputField(fieldName,(ErrorObject) errorObject.clone());
	}

	@Override
	public String toString() {
		return "InputField [fieldName=" + fieldName + ", errorObject="
				+ errorObject + "]";
	}
}
