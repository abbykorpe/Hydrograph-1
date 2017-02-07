package hydrograph.ui.validators.impl;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;

public class DatabaseKeyValidation implements IValidator{
	
	private String errorMessage;

	@Override
	public boolean validateMap(Object object, String propertyName,
			Map<String, List<FixedWidthGridRow>> inputSchemaMap) {
		Map<String, Object> propertyMap = (Map<String, Object>) object;
		if (propertyMap != null && !propertyMap.isEmpty()) {
			return validate(propertyMap.get(propertyName), propertyName,inputSchemaMap,false);
		}
		return false;
	}

	@Override
	public boolean validate(Object object, String propertyName, Map<String, List<FixedWidthGridRow>> inputSchemaMap,
			boolean isJobFileImported) {
		if (object != null) {
			List<String> tmpList = new LinkedList<>();
			Map<String, Object> keyFieldsList = (LinkedHashMap<String, Object>) object;
			
			if (keyFieldsList != null && !keyFieldsList.isEmpty()&& keyFieldsList.containsKey(Constants.LOAD_TYPE_NEW_TABLE_KEY)) {
				if(inputSchemaMap != null){
					for(java.util.Map.Entry<String, List<FixedWidthGridRow>> entry : inputSchemaMap.entrySet()){
						List<FixedWidthGridRow> gridRowList = entry.getValue();
						gridRowList.forEach(gridRow -> tmpList.add(gridRow.getFieldName()));
					}
				}
				for(Entry<String, Object> grid: keyFieldsList.entrySet()){
				    String[] keyValues = StringUtils.split((String) grid.getValue(), ",");
				    for(String values : keyValues){
				    	if(!tmpList.contains(values)){
				    		errorMessage = "Target Fields Should be present in Available Fields";
							return false;
				    	}
				    }
				}
				return true;
			}else if(keyFieldsList != null && !keyFieldsList.isEmpty() && keyFieldsList.containsKey(Constants.LOAD_TYPE_INSERT_KEY)){
				return true;
			}else if(keyFieldsList != null && !keyFieldsList.isEmpty() && keyFieldsList.containsKey(Constants.LOAD_TYPE_REPLACE_KEY)){
				return true;
			}
		}
		return true;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
