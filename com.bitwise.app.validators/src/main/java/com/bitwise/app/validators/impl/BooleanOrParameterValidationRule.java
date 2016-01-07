package com.bitwise.app.validators.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class BooleanOrParameterValidationRule implements IValidator {
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
		String value = (String) object;
		if(StringUtils.isNotBlank(value)){
			if((Boolean.TRUE.toString().equalsIgnoreCase(value) || Boolean.FALSE.toString().equalsIgnoreCase(value))|| 
				((StringUtils.startsWith(value, "@{") && StringUtils.endsWith(value, "}")) &&
						!StringUtils.contains(value, "@{}"))){
				return true;
			}
			errorMessage = propertyName + " is mandatory";
		}
		errorMessage = propertyName + " is not boolean value or valid parameter";
		return false;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
