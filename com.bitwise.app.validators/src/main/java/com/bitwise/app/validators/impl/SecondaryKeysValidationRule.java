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

import java.util.LinkedHashMap;
import java.util.Map;

public class SecondaryKeysValidationRule implements IValidator{
	private String errorMessage;
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
