package hydrograph.ui.propertywindow.widgets.utility;

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
	private static final String JAVA_UTIL_DATE = "java.util.Date";
	private static final String PARQUET = "parquet";
	private static final String HIVE = "hive";
	private static final String JAVA_MATH_BIG_DECIMAL = "java.math.BigDecimal";
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
		    if(StringUtils.equalsIgnoreCase(gridRow.getDataTypeValue(), JAVA_MATH_BIG_DECIMAL)){
				executeIfDataTypeIsBigDecimal(gridRow, componentType, tableItem);	
			}
		 
			else if(StringUtils.equalsIgnoreCase(gridRow.getDataTypeValue(),JAVA_UTIL_DATE)){
				executeIfDataTypeIsDate(gridRow, tableItem);	
			}
			else if(gridRow instanceof FixedWidthGridRow && !(gridRow instanceof GenerateRecordSchemaGridRow))
			{
				executeIfObjectIsFixedWidthRow(gridRow, tableItem);	
			}
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
	private void setBlackColor(TableItem tableItem) {
		tableItem.setForeground(new Color(Display.getDefault(), 0, 0, 0));
	}
	private void setRedColor(TableItem tableItem) {
		tableItem.setForeground(new Color(Display.getDefault(), 255, 0, 0));
	}
	private void executeIfDataTypeIsDate(GridRow gridRow, TableItem tableItem) {
		if((StringUtils.isBlank(gridRow.getDateFormat()))){
			setRedColor(tableItem);
		}
		else if(gridRow instanceof FixedWidthGridRow && !(gridRow instanceof GenerateRecordSchemaGridRow))
		{
			executeIfObjectIsFixedWidthRow(gridRow, tableItem);	
		}	
		else
		{
			setBlackColor(tableItem);
		}
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
				){
			setRedColor(tableItem);
		}	
		else if(gridRow instanceof FixedWidthGridRow && !(gridRow instanceof GenerateRecordSchemaGridRow)){
			executeIfObjectIsFixedWidthRow(gridRow, tableItem);	
		}	
		else
		{
			setBlackColor(tableItem);
		}
	}
}
