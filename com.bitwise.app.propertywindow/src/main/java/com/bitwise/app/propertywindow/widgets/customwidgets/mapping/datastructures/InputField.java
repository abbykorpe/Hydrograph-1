package com.bitwise.app.propertywindow.widgets.customwidgets.mapping.datastructures;


public class InputField {
	private String fieldName;
	private ErrorObject errorObject;
	
	public InputField(String fieldName) {
		super();
		this.fieldName = fieldName;
		errorObject = new ErrorObject(false, "");
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
}
