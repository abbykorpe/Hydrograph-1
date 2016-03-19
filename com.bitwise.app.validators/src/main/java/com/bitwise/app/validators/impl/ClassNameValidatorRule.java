/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package com.bitwise.app.validators.impl;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;

public class ClassNameValidatorRule implements IValidator {
	private static final Pattern VALID_JAVA_IDENTIFIER = Pattern.compile(
			"(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");
	
	private String errorMessage;
	
	public boolean validateJavaIdentifier(String identifier) {
        return VALID_JAVA_IDENTIFIER.matcher(identifier).matches();
    }
	
	@Override
	public boolean validateMap(Object object, String propertyName) {
		Map<String, Object> propertyMap = (Map<String, Object>) object;
		if(propertyMap != null && !propertyMap.isEmpty()){ 
			return validate(propertyMap.get(propertyName), propertyName);
		}
		errorMessage = "Invalid parameter value";
		return false;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public boolean validate(Object object, String propertyName) {
		if(object == null || !OperationClassProperty.class.isAssignableFrom(object.getClass())){
			errorMessage = "Invalid parameter value";
			return false;
		}
		OperationClassProperty operationClassProperty = (OperationClassProperty) object; 
		String operationClassPath = operationClassProperty.getOperationClassPath();
		if(StringUtils.isBlank(operationClassPath)){
			errorMessage = "Field should not be empty";
			return false;
		}
		else if(operationClassProperty.isParameter()){
			if(!operationClassPath.startsWith("@{") || !operationClassPath.endsWith("}") || 
					operationClassPath.indexOf("}") != 2){
				return true;
			}
			else{
				errorMessage = "Invalid parameter value";
				return false;
			}
		}
		else{
			if(!validateJavaIdentifier(operationClassPath)){
				errorMessage = "Invalid value for property";
				return false;
			}
			else{
				return true;
			}
		}
	}
}
