package com.bitwise.app.validators.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bitwise.app.common.datastructure.property.LookupConfigProperty;

public class LookupConfigValidationRule implements IValidator{

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
		LookupConfigProperty lookupConfigProperty = (LookupConfigProperty)object;
		if(lookupConfigProperty == null ){
			errorMessage = propertyName + " is mandatory";
			return false;
		}
		
		if(StringUtils.isBlank(lookupConfigProperty.getDriverKey())){
			errorMessage = "Driver key is mandatary";
			return false;
		}
		if(StringUtils.isBlank(lookupConfigProperty.getLookupKey())){
			errorMessage = "Lookup key is mandatary";
			return false;
		}
		if(StringUtils.isBlank(lookupConfigProperty.getLookupPort())){
			errorMessage = "Port is mandatary";
			return false;
		}
		return true;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
