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

 
package com.bitwise.app.common.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bitwise.app.common.component.config.Component;
import com.bitwise.app.common.component.config.Property;
import com.bitwise.app.common.component.config.Validators;

public class ComponentCacheUtil {
	public static final ComponentCacheUtil INSTANCE = new ComponentCacheUtil();
	
	private ComponentCacheUtil(){}
	
	private final Map<String, Map<String, Object>> propertyCache = new LinkedHashMap<>();
	private final Map<String, Map<String, List<String>>> validatorCache = new LinkedHashMap<>();
	
	
	//TODO : refine for nested properties
	public Map<String, Object> getProperties(String componentName) {
		Map<String, Object> propertyMap = null;
		Map<String, List<String>> propertyValidatorMap = null;
		
		if (propertyCache.containsKey(componentName)) {
			propertyMap = propertyCache.get(componentName);
		} else {
			Component component = XMLConfigUtil.INSTANCE.getComponent(componentName);
			propertyMap = new LinkedHashMap<>();
			propertyValidatorMap = new LinkedHashMap<>();
			for (Property property : component.getProperty()) {
				propertyMap.put(property.getName(), property.getValue());
				fillValidators(property, propertyValidatorMap, property.getName());
			}
			propertyCache.put(componentName, propertyMap);
			validatorCache.put(componentName, propertyValidatorMap);
		}
		return cloneTheMap(propertyMap);
	}
	
	public List<String> getValidatorsForProperty(String componentName, String propertyName){
		if(validatorCache.containsKey(componentName)){
			Map<String, List<String>> propertyValidatorMap = validatorCache.get(componentName);
			if(propertyValidatorMap.containsKey(propertyName)){
				return propertyValidatorMap.get(propertyName);
			}
		}
		else{
			return new ArrayList<>();
		}
		throw new RuntimeException("Incorrect Component or Property Name");
	}
	
	private void fillValidators(Property property, Map<String, List<String>> propertyValidatorMap, String propertyName) {
		List<String> validatorsList = new ArrayList<>();
		
		for(Validators validators : property.getValidator()){
			validatorsList.add(validators.name());
		}
		propertyValidatorMap.put(propertyName, validatorsList);
	}

	private Map<String, Object> cloneTheMap(Map<String, Object> propertyMap) {
		Map<String, Object> clonedMap = new LinkedHashMap<>();
		for (Map.Entry<String, Object> mapEntry : propertyMap.entrySet()) {
			clonedMap.put(mapEntry.getKey(), mapEntry.getValue());
		}
		return clonedMap;
	}
}