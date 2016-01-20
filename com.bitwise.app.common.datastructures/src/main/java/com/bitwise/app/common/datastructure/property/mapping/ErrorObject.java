package com.bitwise.app.common.datastructure.property.mapping;

import com.bitwise.app.cloneableinterface.IDataStructure;


public class ErrorObject implements IDataStructure {
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
	
	@Override
	public Object clone() {
		return new ErrorObject(hasError, errorMessage);
	}

	@Override
	public String toString() {
		return "ErrorObject [hasError=" + hasError + ", errorMessage="
				+ errorMessage + "]";
	}
	
	
}
