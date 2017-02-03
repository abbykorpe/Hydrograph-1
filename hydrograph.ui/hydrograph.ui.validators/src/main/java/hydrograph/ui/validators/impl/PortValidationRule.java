/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.ui.validators.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;

/***
 * This class validates port_no for all DB Components.
 * @author Bitwise
 *
 */
public class PortValidationRule implements IValidator{
	String errorMessage;
	@Override
	public boolean validateMap(Object object, String propertyName,
			Map<String, List<FixedWidthGridRow>> inputSchemaMap) {
		Map<String, Object> propertyMap = (Map<String, Object>) object;
		if (propertyMap != null && !propertyMap.isEmpty()) {
			return validate(propertyMap.get(propertyName), propertyName, inputSchemaMap,false);
		}
		return false;
	}

	@Override
	public boolean validate(Object object, String propertyName, Map<String, List<FixedWidthGridRow>> inputSchemaMap,
			boolean isJobFileImported) {
		String value = (String)object;
		if(StringUtils.isNotBlank(value)){
			Matcher matchs=Pattern.compile(Constants.REGEX_NUMERIC_AND_PARAMETER).matcher(value);
			if(!matchs.matches()){
				errorMessage = propertyName + Constants.PORT_WIDGET_ERROR;
				return false;
			}else{
				return true;
			}
		}
		errorMessage = propertyName + " can not be blank";
		return false;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
