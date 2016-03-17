package com.bitwise.app.validators.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SecondaryKeysValidationRule implements IValidator{
	String errorMessage;
	@Override
	public boolean validateMap(Object object, String propertyName) {
		Map<String, Object> propertyMap = (Map<String, Object>) object;
		if (propertyMap != null && !propertyMap.isEmpty()) {
			return validate(propertyMap.get(propertyName), propertyName);
		}
		return false;
	}

	@Override
	public boolean validate(Object object, String propertyName) {
		if (object != null) {
			LinkedHashMap<String, Object> 	secondaryKeysList = (LinkedHashMap<String, Object>) object;
			if (secondaryKeysList.size() != 0) {
				return true;
			}
		}
		errorMessage = propertyName.replace("_"," ") + " are mandatory";
		return false;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
