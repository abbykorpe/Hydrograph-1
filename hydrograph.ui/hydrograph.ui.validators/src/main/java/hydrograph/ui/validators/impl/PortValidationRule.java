package hydrograph.ui.validators.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import hydrograph.ui.datastructure.property.FixedWidthGridRow;

public class PortValidationRule implements IValidator{
	
	private String errorMessage;

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
			Matcher matchs=Pattern.compile("^([\\@]{1}[\\{]{1}[\\s\\S]+[\\}]{1})|([\\d]{4})$").matcher(value);
			if(!matchs.matches()){
				errorMessage = propertyName + " Should be Numeric or Parameter E.g. 1234 or @{1234}";
				return false;
			}else{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}
