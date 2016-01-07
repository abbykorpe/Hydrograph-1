package com.bitwise.app.validators.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class NonEmptyStringValidatorRule implements IValidator{

	private String errorMessage;
	
	@Override
	public boolean validateMap(Object object, String propertyName) {
		Map<String, Object> propertyMap = (Map<String, Object>) object;
		if(propertyMap != null && !propertyMap.isEmpty()){ 
			return validate(propertyMap.get(propertyName), propertyName);
		}
		return false;
	}


	@Override
	public boolean validate(Object object, String propertyName) {
		String value = (String)object;
		if(StringUtils.isNotBlank(value)){
			return true;
		}
		errorMessage = propertyName + " can not be empty";
		return false;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
