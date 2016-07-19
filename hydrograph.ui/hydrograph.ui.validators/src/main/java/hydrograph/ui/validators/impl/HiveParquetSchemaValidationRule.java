package hydrograph.ui.validators.impl;

import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.logging.factory.LogFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public class HiveParquetSchemaValidationRule implements IValidator{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(SchemaGridValidationRule.class); 

	private static final String DATA_TYPE_DATE = "java.util.Date";
	private static final String DATA_TYPE_BIG_DECIMAL = "java.math.BigDecimal";
	private static final String SCALE_TYPE_NONE = "none";
	private static final String REGULAR_EXPRESSION_FOR_NUMBER = "\\d+";
	
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
			if(StringUtils.isBlank(schema.getExternalSchemaPath())){
				errorMessage = propertyName + " is mandatory";
				return false;
			}
		}
		return validateSchema(schema, propertyName);
	}


	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	private boolean validateSchema(Schema schema, String propertyName) {
		List<GridRow> gridRowList = (List<GridRow>) schema.getGridRow();
		
		/*this list is used for checking duplicate names in the grid*/
		List<String> uniqueNamesList = new ArrayList<>();
		if(gridRowList == null || gridRowList.isEmpty()){
			errorMessage = propertyName + " is mandatory";
			return false;
		}
		for (GridRow gridRow : gridRowList) {
			if(StringUtils.isBlank(gridRow.getFieldName())){
				errorMessage = "Field name can not be blank";
				return false;
			}
			
			if(DATA_TYPE_BIG_DECIMAL.equalsIgnoreCase(gridRow.getDataTypeValue())){
				if(StringUtils.isBlank(gridRow.getScale()) || StringUtils.equalsIgnoreCase(gridRow.getScale(), "0") 
						|| !(gridRow.getScale().matches(REGULAR_EXPRESSION_FOR_NUMBER))){
					errorMessage = "Scale should be positive integer.";
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
			
			if (StringUtils.equalsIgnoreCase(DATA_TYPE_BIG_DECIMAL, gridRow.getDataTypeValue())
					&& (StringUtils.isBlank(gridRow.getScaleTypeValue()) || StringUtils.equalsIgnoreCase(
							SCALE_TYPE_NONE, gridRow.getScaleTypeValue()))){
				errorMessage = "Scale type cannot be blank or none for Big Decimal data type";
				return false;
			}
			
			if(StringUtils.equalsIgnoreCase(DATA_TYPE_BIG_DECIMAL, gridRow.getDataTypeValue()) && (StringUtils.isBlank(gridRow.getPrecision()))){
				errorMessage = "Precision cannot be blank or none for Big Decimal data type";
				return false;
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
