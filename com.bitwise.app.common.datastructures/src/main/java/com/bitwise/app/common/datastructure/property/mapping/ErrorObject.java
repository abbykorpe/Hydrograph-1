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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
		result = prime * result + (hasError ? 1231 : 1237);
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
		ErrorObject other = (ErrorObject) obj;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		if (hasError != other.hasError)
			return false;
		return true;
	}
	
	
	
}
