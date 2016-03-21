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

import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;

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
