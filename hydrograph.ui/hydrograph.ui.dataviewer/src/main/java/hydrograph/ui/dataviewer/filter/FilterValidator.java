package hydrograph.ui.dataviewer.filter;

import hydrograph.ui.logging.factory.LogFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public class FilterValidator {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterValidator.class);
	List<String> relationalList = Arrays.asList(new String[]{"AND", "OR"});
	
	public static FilterValidator INSTANCE = new FilterValidator();
	
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
				logger.trace("operator at {} is incorrect", operators);
				return false;
			}
			
			if(StringUtils.isNotBlank(value)){
				if(!validateDataBasedOnTypes(type, value)){
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
	
	public boolean validateDataBasedOnTypes(String type, String value){
		try{
			if(FilterHelper.TYPE_BOOLEAN.equals(type)){
				Boolean convertedBoolean = Boolean.valueOf(value);
				if(!StringUtils.equalsIgnoreCase(convertedBoolean.toString(), value)){
					return false;
				}
			}
			else if(FilterHelper.TYPE_DOUBLE.equals(type)){
				Double.valueOf(value);
			}
			else if(FilterHelper.TYPE_FLOAT.equals(type)){
				Float.valueOf(value);
			}
			else if(FilterHelper.TYPE_INTEGER.equals(type)){
				Integer.valueOf(value);
			}
			else if(FilterHelper.TYPE_LONG.equals(type)){
				Long.valueOf(value);
			}
			else if(FilterHelper.TYPE_SHORT.equals(type)){
				Short.valueOf(value);
			}
			else if(FilterHelper.TYPE_STRING.equals(type)){
				String.valueOf(value);
			}
			else if(FilterHelper.TYPE_BIGDECIMAL.equals(type)){
				new BigDecimal(value);
			}
			else if(FilterHelper.TYPE_DATE.equals(type)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
				Date comp_date= sdf.parse(value);
			}
		}
		catch(Exception exception){
			logger.trace("value can not be converted to {}", new Object[]{type});
			return false;
		}
		return true;
	}
}
