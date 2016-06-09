package hydrograph.ui.propertywindow.widgets.utility;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.GenerateRecordSchemaGridRow;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.MixedSchemeGridRow;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class SchemaRowValidation {
	private static final String NONE = "none";
	private static final String REGULAR_EXPRESSION_FOR_NUMBER = "\\d+";
	private static final String PARQUET = "parquet";
	private static final String HIVE = "hive";
	private static final String JAVA_MATH_BIG_DECIMAL = "java.math.BigDecimal";
	private static final String JAVA_UTIL_DATE = "java.util.Date";
	private static final String JAVA_LANG_INTEGER = "java.lang.Integer";
	private static final String JAVA_LANG_DOUBLE = "java.lang.Double";
	private static final String JAVA_LANG_FLOAT = "java.lang.Float";
	private static final String JAVA_LANG_SHORT = "java.lang.Short";
	private static final String JAVA_LANG_LONG = "java.lang.Long";
	
	
	public static final SchemaRowValidation INSTANCE = new SchemaRowValidation();
	
	public void highlightInvalidRowWithRedColor(GridRow gridRow,TableItem item,Table table,String componentType )
	{ 
		if(item==null)
		{
			for(TableItem tableItem:table.getItems())
			{		
		     gridRow=(GridRow)tableItem.getData();
		     setRedColorOnTableRowBasedOnInvalidData(gridRow, componentType, tableItem);	
			 gridRow=null;
			}
			
		}	
		else{
			 setRedColorOnTableRowBasedOnInvalidData(gridRow, componentType, item);	
			 gridRow=null;
		}
	}
	private void setRedColorOnTableRowBasedOnInvalidData(GridRow gridRow,
			String componentType, TableItem tableItem) {
		if(StringUtils.equalsIgnoreCase(gridRow.getDataTypeValue(), JAVA_MATH_BIG_DECIMAL))
			executeIfDataTypeIsBigDecimal(gridRow, componentType, tableItem);	
		else if(StringUtils.equalsIgnoreCase(gridRow.getDataTypeValue(),JAVA_UTIL_DATE))
			executeIfDataTypeIsDate(gridRow, tableItem);	
		else if(gridRow instanceof FixedWidthGridRow && !(gridRow instanceof GenerateRecordSchemaGridRow))
			executeIfObjectIsFixedWidthRow(gridRow, tableItem);	
		else if(gridRow instanceof GenerateRecordSchemaGridRow)
			executeIfObjectIsGenerateRecordRow(gridRow, tableItem);
	}
	private void executeIfObjectIsFixedWidthRow(GridRow gridRow,
			TableItem tableItem) {
		FixedWidthGridRow fixedWidthGridRow=(FixedWidthGridRow)gridRow;
		if(fixedWidthGridRow instanceof MixedSchemeGridRow)
		{
			if((StringUtils.isBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isBlank(fixedWidthGridRow.getLength()))
			   ||(StringUtils.isNotBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isNotBlank(fixedWidthGridRow.getLength()))
			   ||(StringUtils.isNotBlank(fixedWidthGridRow.getLength())&& !(fixedWidthGridRow.getLength().matches(REGULAR_EXPRESSION_FOR_NUMBER)))
			   )
			{
				setRedColor(tableItem);
			}	
			else
			{
				setBlackColor(tableItem);
			}	
			
		}
		else{
		if(StringUtils.isBlank(fixedWidthGridRow.getLength())||!(fixedWidthGridRow.getLength().matches(REGULAR_EXPRESSION_FOR_NUMBER))){
			setRedColor(tableItem);
		}	
		else{
			setBlackColor(tableItem);
		}	
		}
	}
	
	private void executeIfObjectIsGenerateRecordRow(GridRow gridRow, TableItem tableItem) {

		GenerateRecordSchemaGridRow generateRecordSchemaGridRow = (GenerateRecordSchemaGridRow) gridRow;
		boolean isRedColor = false;
		
		if (StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeFrom())
				|| StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeTo())) {

			if (StringUtils.equalsIgnoreCase(generateRecordSchemaGridRow.getDataTypeValue(), JAVA_UTIL_DATE)) {
				
				Date rangeFromDate = null, rangeToDate = null;
				SimpleDateFormat formatter = new SimpleDateFormat(generateRecordSchemaGridRow.getDateFormat());

				if (!isRedColor && StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeFrom())) {
					try {
						rangeFromDate = formatter.parse(generateRecordSchemaGridRow.getRangeFrom());
					} catch (ParseException e) {
						isRedColor = true;
						e.printStackTrace();
					}
				}

				if (!isRedColor && StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeTo())) {
					try {
						rangeToDate = formatter.parse(generateRecordSchemaGridRow.getRangeTo());
					} catch (ParseException e) {
						isRedColor = true;
						e.printStackTrace();
					}
				}

				if (!isRedColor && rangeFromDate != null && rangeToDate != null && rangeFromDate.after(rangeToDate))
					isRedColor = true;

			} else if (StringUtils.equalsIgnoreCase(generateRecordSchemaGridRow.getDataTypeValue(), JAVA_MATH_BIG_DECIMAL)) {

				BigDecimal rangeFrom = null, rangeTo = null;

				if (StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeFrom()))
					rangeFrom = new BigDecimal(generateRecordSchemaGridRow.getRangeFrom());
				if (StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeTo()))
					rangeTo = new BigDecimal(generateRecordSchemaGridRow.getRangeTo());

				if (!isRedColor && rangeFrom != null && rangeTo != null) {
					if (rangeFrom.compareTo(rangeTo) > 0) {
						isRedColor = true;
					}
				}

				int fieldLength = 0, scaleLength = 0;

				if (!generateRecordSchemaGridRow.getLength().isEmpty())
					fieldLength = Integer.parseInt(generateRecordSchemaGridRow.getLength());

				if (!generateRecordSchemaGridRow.getScale().isEmpty())
					scaleLength = Integer.parseInt(generateRecordSchemaGridRow.getScale());

				if (!isRedColor && fieldLength < 0) {
					isRedColor = true;
				} else if (!isRedColor && scaleLength < 0) {
					isRedColor = true;
				} else if (!isRedColor && scaleLength >= fieldLength) {
					isRedColor = true;
				} else {

					String minPermissibleRangeValue = "", maxPermissibleRangeValue = "";

					for (int i = 1; i <= fieldLength; i++) {
						maxPermissibleRangeValue = maxPermissibleRangeValue.concat("9");
						if (minPermissibleRangeValue.trim().length() == 0)
							minPermissibleRangeValue = minPermissibleRangeValue.concat("-");
						else
							minPermissibleRangeValue = minPermissibleRangeValue.concat("9");
					}
					
					if(minPermissibleRangeValue.equals("-"))
						minPermissibleRangeValue = "0";

					if (scaleLength != 0) {
						int decimalPosition = fieldLength - scaleLength;

						if (decimalPosition == 1) {
							minPermissibleRangeValue = "0";
							maxPermissibleRangeValue = maxPermissibleRangeValue.replaceFirst("9", ".");
						} else
							minPermissibleRangeValue = minPermissibleRangeValue.substring(0, decimalPosition - 1)
									+ "." + minPermissibleRangeValue.substring(decimalPosition);
						
						maxPermissibleRangeValue = maxPermissibleRangeValue.substring(0, decimalPosition - 1)
								+ "." + maxPermissibleRangeValue.substring(decimalPosition);
					}
					
					
					BigDecimal minRangeValue = new BigDecimal(minPermissibleRangeValue);
					BigDecimal maxRangeValue = new BigDecimal(maxPermissibleRangeValue);

					if (!isRedColor && rangeFrom != null && fieldLength > 0 && rangeTo != null) {
						if (rangeFrom.compareTo(minRangeValue) < 0)
							isRedColor = true;
					} else if(!isRedColor && rangeFrom != null && fieldLength > 0 && rangeTo == null){
						if (rangeFrom.compareTo(maxRangeValue) > 0)
							isRedColor = true;									
					}

					if (!isRedColor && rangeTo != null && fieldLength > 0 && rangeFrom != null) {
						if (rangeTo.compareTo(maxRangeValue) > 0)
							isRedColor = true;
					} else if(!isRedColor && rangeTo != null && fieldLength > 0 && rangeFrom == null){
						if (rangeTo.compareTo(minRangeValue) < 0)
							isRedColor = true;
					}
				}

			} else if (StringUtils.equalsIgnoreCase(generateRecordSchemaGridRow.getDataTypeValue(), JAVA_LANG_DOUBLE)
					|| StringUtils.equalsIgnoreCase(generateRecordSchemaGridRow.getDataTypeValue(), JAVA_LANG_FLOAT)
					|| StringUtils.equalsIgnoreCase(generateRecordSchemaGridRow.getDataTypeValue(), JAVA_LANG_INTEGER)
					|| StringUtils.equalsIgnoreCase(generateRecordSchemaGridRow.getDataTypeValue(), JAVA_LANG_LONG)
					|| StringUtils.equalsIgnoreCase(generateRecordSchemaGridRow.getDataTypeValue(), JAVA_LANG_SHORT)) {

				BigDecimal rangeFrom = null, rangeTo = null;

				if (StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeFrom()))
					rangeFrom = new BigDecimal(generateRecordSchemaGridRow.getRangeFrom());
				if (StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeTo()))
					rangeTo = new BigDecimal(generateRecordSchemaGridRow.getRangeTo());

				if (!isRedColor && rangeFrom != null && rangeTo != null) {
					if (rangeFrom.compareTo(rangeTo) > 0)
						isRedColor = true;
				}
				
				int fieldLength = 0;

				if (!generateRecordSchemaGridRow.getLength().isEmpty())
					fieldLength = Integer.parseInt(generateRecordSchemaGridRow.getLength());

				if (!isRedColor && fieldLength < 0)
					isRedColor = true;
				else {

					String minPermissibleRangeValue = "", maxPermissibleRangeValue = "";

					for (int i = 1; i <= fieldLength; i++) {
						maxPermissibleRangeValue = maxPermissibleRangeValue.concat("9");
						if (minPermissibleRangeValue.trim().length() == 0)
							minPermissibleRangeValue = minPermissibleRangeValue.concat("-");
						else
							minPermissibleRangeValue = minPermissibleRangeValue.concat("9");
					}
					
					if(minPermissibleRangeValue.equals("-"))
						minPermissibleRangeValue = "0";
					
					BigDecimal minRangeValue = new BigDecimal(minPermissibleRangeValue);
					BigDecimal maxRangeValue = new BigDecimal(maxPermissibleRangeValue);
				
					
					if (!isRedColor && rangeFrom != null && fieldLength > 0 && rangeTo != null) {
						if (rangeFrom.compareTo(minRangeValue) < 0)
							isRedColor = true;
						else if (rangeTo.compareTo(maxRangeValue) > 0)
							isRedColor = true;
					} else if(!isRedColor && rangeFrom != null && fieldLength > 0 && rangeTo == null){
						if (rangeFrom.compareTo(maxRangeValue) > 0)
							isRedColor = true;									
					} else if(!isRedColor && rangeTo != null && fieldLength > 0 && rangeFrom == null){
						if (rangeTo.compareTo(minRangeValue) < 0)
							isRedColor = true;
					}
				}
			}
		}

		if (isRedColor)
			setRedColor(tableItem);
		else
			setBlackColor(tableItem);
			
	}
	
	
	private void setBlackColor(TableItem tableItem) {
		tableItem.setForeground(new Color(Display.getDefault(), 0, 0, 0));
	}

	
	private void setRedColor(TableItem tableItem) {
		tableItem.setForeground(new Color(Display.getDefault(), 255, 0, 0));
	}
	
	
	private void executeIfDataTypeIsDate(GridRow gridRow, TableItem tableItem) {
		if((StringUtils.isBlank(gridRow.getDateFormat())))
			setRedColor(tableItem);
		else if(gridRow instanceof FixedWidthGridRow && !(gridRow instanceof GenerateRecordSchemaGridRow))
			executeIfObjectIsFixedWidthRow(gridRow, tableItem);	
		else if(gridRow instanceof GenerateRecordSchemaGridRow)
			executeIfObjectIsGenerateRecordRow(gridRow, tableItem);
		else
			setBlackColor(tableItem);
	}
	
	
	private void executeIfDataTypeIsBigDecimal(GridRow gridRow,
			String componentType, TableItem tableItem) {
		if(StringUtils.containsIgnoreCase(componentType, HIVE)||StringUtils.containsIgnoreCase(componentType, PARQUET)){
			if(StringUtils.isBlank(gridRow.getPrecision())|| StringUtils.isBlank(gridRow.getScale()) ||
					StringUtils.equalsIgnoreCase(gridRow.getScaleTypeValue(), NONE)||
					!(gridRow.getScale().matches(REGULAR_EXPRESSION_FOR_NUMBER))||!(gridRow.getPrecision().matches(REGULAR_EXPRESSION_FOR_NUMBER))
					){
				setRedColor(tableItem);
			}
			else{
				setBlackColor(tableItem);
			}	
		}	
		else if(StringUtils.isBlank(gridRow.getScale()) ||
				StringUtils.equalsIgnoreCase(gridRow.getScaleTypeValue(), NONE)||
				!(gridRow.getScale().matches(REGULAR_EXPRESSION_FOR_NUMBER))||(!(gridRow.getPrecision().matches(REGULAR_EXPRESSION_FOR_NUMBER))&&
				 StringUtils.isNotBlank(gridRow.getPrecision()))
				)
			setRedColor(tableItem);
		else if(gridRow instanceof FixedWidthGridRow && !(gridRow instanceof GenerateRecordSchemaGridRow))
			executeIfObjectIsFixedWidthRow(gridRow, tableItem);
		else if(gridRow instanceof GenerateRecordSchemaGridRow)
			executeIfObjectIsGenerateRecordRow(gridRow, tableItem);
		else
			setBlackColor(tableItem);
	}
}
