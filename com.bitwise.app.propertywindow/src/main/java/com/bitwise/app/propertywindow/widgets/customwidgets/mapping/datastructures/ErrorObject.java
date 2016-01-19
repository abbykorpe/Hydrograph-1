package com.bitwise.app.propertywindow.widgets.customwidgets.mapping.datastructures;


public class ErrorObject {
	boolean hasError;
	String errorMessage;
	
	public ErrorObject(boolean hasError, String errorMessage) {
		super();
		this.hasError = hasError;
		this.errorMessage = errorMessage;
	}

	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
