package com.bitwise.app.validators.impl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.bitwise.app.common.util.Constants;

public class FileExistsValidatorRule implements IValidator{

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
		String value = (String) object;
		if (StringUtils.isNotBlank(value)) {
			if (isFileExistsOnLocalFileSystem(value))
				return true;
			else{
				errorMessage = propertyName + " Invalid file path";
				return false;
			}
		}
		errorMessage= propertyName + " is mandatory";
		return false;
	}

	private boolean isFileExistsOnLocalFileSystem(String value) {
		Matcher matchs=Pattern.compile(Constants.PARAMETER_REGEX).matcher(value);
		if(matchs.find())
			return true;
		IPath jobFilePath = new Path(value);
		try {
			if (jobFilePath.isValidPath(value)) {
				if (ResourcesPlugin.getWorkspace().getRoot().getFile(jobFilePath).exists())
					return true;
				else if (jobFilePath.toFile().exists())
					return true;
			}
		} catch (Exception e) {

		}
		return false;
	}

	
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
