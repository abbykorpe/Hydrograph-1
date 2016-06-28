package hydrograph.ui.dataviewer.filter;

import hydrograph.ui.logging.factory.LogFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public class FilterValidator {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterValidator.class);
	List<String> relationalList = Arrays.asList(new String[]{"AND", "OR"});
	String[] fields = new String[]{"a", "b"};
	List<String> fieldsList = Arrays.asList(fields);
	
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
				logger.debug("fields are blank");
				return false;
			}
			if(StringUtils.isBlank(fieldName) 
					|| StringUtils.isBlank(conditional) ||StringUtils.isBlank(value)){
				logger.debug("fields are blank");
				return false;
			}
			if(index != 0){
				if(!relationalList.contains(relationalOperator)){
					logger.debug("Relational Operator is wrong {}", relationalOperator);
					return false;
				}
			}
			if(!Arrays.asList(fieldNames).contains(fieldName)){
				logger.debug("Field Name is wrong {}", fieldName);
				return false;
			}
			String type = fieldsAndTypes.get(fieldName);
			List<String> operators = Arrays.asList(conditionalOperatorsMap.get(type));
			if(!operators.contains(condition.getConditionalOperator())){
				logger.debug("operators is wrong {}", operators);
				return false;
			}
		}
		return true;
	}
}
