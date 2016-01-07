package com.bitwise.app.validators.impl;

import java.util.Map;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;

public class IntegerValidatorRule implements IValidator{

	private static final Logger logger = LogFactory.INSTANCE.getLogger(IntegerValidatorRule.class);
	private String errorMessage;
	
	@Override
	public boolean validateMap(Object object, String propertyName) {
		Map<String, Object> propertyMap = (Map<String, Object>) object;
		
		if(propertyMap != null && !propertyMap.isEmpty()){ 
			return validate((String)propertyMap.get(propertyName), propertyName);
		}
		return false;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public boolean validate(Object object, String propertyName) {
		try{
			String value = (String)object;
			Integer.parseInt(value);
		}
		catch(NumberFormatException exception){
			logger.trace("Failed to parse value from {}, {}", propertyName, exception);
			errorMessage = "Invalid integer value";
			return false;
		}
		return true;
	}
}
