package com.bitwise.app.validators.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bitwise.app.common.datastructure.property.JoinConfigProperty;

public class JoinConfigValidationRule implements IValidator{

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
		List<JoinConfigProperty> valueList = (List<JoinConfigProperty>)object;
		if(valueList == null || valueList.isEmpty() || valueList.size() < 2){
			errorMessage = propertyName + " must have atleast 2 port configuration";
			return false;
		}
		
		for (JoinConfigProperty joinConfigProperty : valueList) {
			if(StringUtils.isBlank(joinConfigProperty.getPortIndex())){
				errorMessage = "Port indexes are mandatary";
				return false;
			}
			if(StringUtils.isBlank(joinConfigProperty.getJoinKey())){
				errorMessage = "Join keys are mandatary";
				return false;
			}
			else if(joinConfigProperty.getJoinType() < 0 || joinConfigProperty.getJoinType() > 2){
				errorMessage = "Join Type should be either INNER OR OUTER OR paramter";
				return false;
			}
			else if(joinConfigProperty.getJoinType() == 2){
				if(StringUtils.isBlank(joinConfigProperty.getParamValue()) || 
						!(
							StringUtils.startsWith(joinConfigProperty.getParamValue(), "@{") && 
							StringUtils.endsWith(joinConfigProperty.getParamValue(), "}") &&
							!StringUtils.contains(joinConfigProperty.getParamValue(), "@{}")
						) 
					){
						errorMessage = "Invalid parameter value";
						return false;
				}
			}
		}
		return true;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
