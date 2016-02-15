package com.bitwise.app.validators.impl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Validation rule to check that value is either integer or parameter <br>
 * ex. @{some_parameter_name}.
 */
public class IntegerOrParameterValidationRule implements IValidator {
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
			Matcher matcher=Pattern.compile("[\\d]*").matcher(value);
			if((matcher.matches())|| 
				((StringUtils.startsWith(value, "@{") && StringUtils.endsWith(value, "}")) &&
						!StringUtils.contains(value, "@{}"))){
				return true;
			}
			errorMessage = propertyName + " is mandatory";
		}
		errorMessage = propertyName + " is not integer value or valid parameter";
		return false;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}