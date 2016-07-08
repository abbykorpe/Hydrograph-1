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
package hydrograph.ui.dataviewer.filter;

import hydrograph.ui.logging.factory.LogFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

/**
 * Validator class for Filter Window
 * @author Bitwise
 *
 */
public class FilterValidator {

	public static FilterValidator INSTANCE = new FilterValidator();

	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterValidator.class);
	private List<String> relationalList = Arrays.asList(new String[]{FilterConstants.AND, FilterConstants.OR});
	
	
	public boolean isAllFilterConditionsValid(List<Condition> conditionList, Map<String, String> fieldsAndTypes, String[] fieldNames){
		Map<String, String[]> conditionalOperatorsMap = FilterHelper.INSTANCE.getTypeBasedOperatorMap();
		
		for (int index = 0; index < conditionList.size(); index++) {
			Condition condition = conditionList.get(index);
			String relationalOperator = condition.getRelationalOperator();
			String fieldName = condition.getFieldName();
			String conditional = condition.getConditionalOperator();
			String value = condition.getValue();
			if(index != 0 && StringUtils.isBlank(relationalOperator)){
				logger.trace("Relational Operator at {} is blank" + index);
				return false;
			}
			if(StringUtils.isBlank(fieldName) 
					|| StringUtils.isBlank(conditional) || StringUtils.isBlank(value)){
				logger.trace("Field name at {} is blank" + index);
				return false;
			}
			
			if(index != 0 && !relationalList.contains(relationalOperator)){
				logger.trace("Relational Operator at {} is incorrect", index);
				return false;
			}
			if(!Arrays.asList(fieldNames).contains(fieldName)){
				logger.trace("Field Name at {} is incorrect {}", index);
				return false;
			}
			String type = getType(fieldName, fieldsAndTypes);
			List<String> operators = Arrays.asList(conditionalOperatorsMap.get(type));
			if(!operators.contains(condition.getConditionalOperator())){
				logger.trace("operator at {} is incorrect", condition.getConditionalOperator());
				return false;
			}
			
			if(StringUtils.isNotBlank(value)){
				if(!validateDataBasedOnTypes(type, value, condition.getConditionalOperator())){
					return false;
				}
			}
		}
		return true;
	}
	
	public String getType(String fieldName, Map<String, String> fieldsAndTypes){
		String type = fieldsAndTypes.get(fieldName);
		return type;
	}
	
	public boolean validateDataBasedOnTypes(String type, String value, String conditionalOperator){
		try{
			if(FilterConstants.IN.equalsIgnoreCase(conditionalOperator) ||
					FilterConstants.NOT_IN.equalsIgnoreCase(conditionalOperator)){
				if(value.contains(FilterConstants.DELIM_COMMA)){
					StringTokenizer tokenizer = new StringTokenizer(value, FilterConstants.DELIM_COMMA);
					int numberOfTokens = tokenizer.countTokens();
					for (int index = 0; index < numberOfTokens; index++) {
						validate(type, tokenizer.nextToken());
					}
				}
			}
			
		}
		catch(Exception exception){
			logger.trace("value can not be converted to {}", new Object[]{type});
			return false;
		}
		return true;
	}

	private boolean validate(String type, String value) throws ParseException {
		if(FilterConstants.TYPE_BOOLEAN.equals(type)){
			Boolean convertedBoolean = Boolean.valueOf(value);
			if(!StringUtils.equalsIgnoreCase(convertedBoolean.toString(), value)){
				return false;
			}
		}
		else if(FilterConstants.TYPE_DOUBLE.equals(type)){
			Double.valueOf(value);
		}
		else if(FilterConstants.TYPE_FLOAT.equals(type)){
			Float.valueOf(value);
		}
		else if(FilterConstants.TYPE_INTEGER.equals(type)){
			Integer.valueOf(value);
		}
		else if(FilterConstants.TYPE_LONG.equals(type)){
			Long.valueOf(value);
		}
		else if(FilterConstants.TYPE_SHORT.equals(type)){
			Short.valueOf(value);
		}
		else if(FilterConstants.TYPE_STRING.equals(type)){
			String.valueOf(value);
		}
		else if(FilterConstants.TYPE_BIGDECIMAL.equals(type)){
			new BigDecimal(value);
		}
		else if(FilterConstants.TYPE_DATE.equals(type)){
			SimpleDateFormat sdf = new SimpleDateFormat(FilterConstants.YYYY_MM_DD); 
			sdf.parse(value);
		}
		return true;
		
	}
}
