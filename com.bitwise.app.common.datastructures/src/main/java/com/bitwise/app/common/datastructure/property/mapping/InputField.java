package com.bitwise.app.common.datastructure.property.mapping;

import com.bitwise.app.cloneableinterface.IDataStructure;

/**
 * 
 * The class stores input field names propagated in Aggregate/transform component
 * 
 * @author Bitwise
 *
 */
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

	/**
	 * returns field name
	 * 
	 * @return
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 
	 * set field name
	 * 
	 * @param fieldName
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * returns {@code ErrorObject} associated with the field.
	 * 
	 * @return {@link ErrorObject}
	 */
	public ErrorObject getErrorObject() {
		return errorObject;
	}

	/**
	 * 
	 * Set {@code ErrorObject}
	 * @param errorObject
	 */
	public void setErrorObject(ErrorObject errorObject) {
		this.errorObject = errorObject;
	}
	
	@Override
	public Object clone(){
		return new InputField(fieldName,(ErrorObject) errorObject.clone());
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorObject == null) ? 0 : errorObject.hashCode());
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
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
		InputField other = (InputField) obj;
		if (errorObject == null) {
			if (other.errorObject != null)
				return false;
		} else if (!errorObject.equals(other.errorObject))
			return false;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InputField [fieldName=" + fieldName + ", errorObject="
				+ errorObject + "]";
	}
}
