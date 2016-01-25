package com.bitwise.app.validators.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.common.datastructure.property.Schema;
import com.bitwise.app.logging.factory.LogFactory;

public class SchemaGridValidationRule implements IValidator {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(SchemaGridValidationRule.class); 

	private static final String DATA_TYPE_DOUBLE = "java.lang.Double";
	private static final String DATA_TYPE_FLOAT = "java.lang.Float"; 
	private static final String DATA_TYPE_DATE = "java.util.Date";
	private static final String DATA_TYPE_BIG_DECIMAL = "java.math.BigDecimal";

	String errorMessage;
	
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
		Schema schema = (Schema) object;
		if(schema == null){
			errorMessage = propertyName + " is mandatory";
			return false;
		}
		else if(schema.getIsExternal()){
			errorMessage = propertyName + " is mandatory";
			return StringUtils.isNotBlank(schema.getExternalSchemaPath());
		}
		else{
			return validateSchema(schema, propertyName);
		}
	}


	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	private boolean validateSchema(Schema schema, String propertyName) {
		List<GridRow> gridRowList = (List<GridRow>) schema.getGridRow();
		
		/*this list is used for checking duplicate names in the grid*/
		List<String> uniqueNamesList = new ArrayList<>();
		boolean fixedWidthGrid = false;
		if(gridRowList == null || gridRowList.isEmpty()){
			errorMessage = propertyName + " is mandatory";
			return false;
		}
		GridRow gridRowTest = gridRowList.iterator().next();
		if(FixedWidthGridRow.class.isAssignableFrom(gridRowTest.getClass())){
			fixedWidthGrid = true;
		}
		for (GridRow gridRow : gridRowList) {
			if(StringUtils.isBlank(gridRow.getFieldName())){
				errorMessage = "Field name can not be blank";
				return false;
			}
			
			if(DATA_TYPE_DOUBLE.equalsIgnoreCase(gridRow.getDataTypeValue()) ||
					DATA_TYPE_FLOAT.equalsIgnoreCase(gridRow.getDataTypeValue()) || 
					DATA_TYPE_BIG_DECIMAL.equalsIgnoreCase(gridRow.getDataTypeValue())){
				if(StringUtils.isBlank(gridRow.getScale())){
					errorMessage = "Scale can not be blank";
					return false;
				}
				try{
					Integer.parseInt(gridRow.getScale());
				}catch(NumberFormatException exception){
					logger.debug("Failed to parse the scale", exception);
					errorMessage = "Scale must be integer value";
					return false;
				}
			}
			else if(DATA_TYPE_DATE.equalsIgnoreCase(gridRow.getDataTypeValue()) && 
					StringUtils.isBlank(gridRow.getDateFormat())){
				errorMessage = "Date format is mandatory";
				return false;
			}
			
			if(fixedWidthGrid){
				FixedWidthGridRow fixedWidthGridRow = (FixedWidthGridRow) gridRow;
				if(StringUtils.isBlank(fixedWidthGridRow.getLength())){
					errorMessage = "Length is mandatory";
					return false;
				}
			}
			if(uniqueNamesList.isEmpty() || !uniqueNamesList.contains(gridRow.getFieldName())){
				uniqueNamesList.add(gridRow.getFieldName());
			}
			else{
				errorMessage = "Schema grid must have unique names";
				return false;
			}
		}
		return true;
	}
}
