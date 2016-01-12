package com.bitwise.app.validators.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.JoinMappingGrid;
import com.bitwise.app.common.datastructure.property.LookupConfigProperty;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.datastructure.property.LookupMappingGrid;

public class LookupMappingValidationRule implements IValidator{

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
		LookupMappingGrid lookupMappingGrid = (LookupMappingGrid)object;
		if(lookupMappingGrid == null){
			errorMessage = propertyName + " is mandatory";
			return false;
		}
		List<List<FilterProperties>> lookupInputProperties = lookupMappingGrid.getLookupInputProperties();
		List<LookupMapProperty> lookupMapProperties = lookupMappingGrid.getLookupMapProperties();
		if(lookupInputProperties == null || 
				lookupInputProperties.isEmpty() || lookupInputProperties.size() < 2){
			errorMessage = "Invalid input for join component"; 
			return false;
		}
		if(lookupMapProperties == null || lookupMapProperties.isEmpty()){
			errorMessage = "Invalid output from join component"; 
			return false;
		}
		
		for (List<FilterProperties> input : lookupInputProperties) {
			if(input == null || input.size() == 0){
				errorMessage = "Input mapping is mandatory";
				return false;
			}
			for(FilterProperties properties  : input){
				if (StringUtils.isBlank(properties.getPropertyname())) {
					errorMessage = "Input mapping is mandatory";
					return false;
				}
			}
		}
		
		for (LookupMapProperty lookupMapProperty : lookupMapProperties) {
			if (StringUtils.isBlank(lookupMapProperty.getSource_Field()) || StringUtils.isBlank(lookupMapProperty.getOutput_Field())) {
				errorMessage = "Output names are mandatory";
				return false;
			}
		}
		return true;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}