package com.bitwise.app.validators.impl;

public interface IValidator {
		boolean validateMap(Object object, String propertyName);
		boolean validate(Object object, String propertyName);
		String getErrorMessage();
}
