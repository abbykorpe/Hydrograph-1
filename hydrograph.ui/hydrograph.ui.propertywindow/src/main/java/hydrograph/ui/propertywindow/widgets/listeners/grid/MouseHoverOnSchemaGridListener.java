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

package hydrograph.ui.propertywindow.widgets.listeners.grid;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.GenerateRecordSchemaGridRow;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.MixedSchemeGridRow;
import hydrograph.ui.propertywindow.factory.ListenerFactory;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import hydrograph.ui.propertywindow.widgets.listeners.MouseActionListener;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

public class MouseHoverOnSchemaGridListener extends MouseActionListener{
	
	Table table=null;
	private Shell tip=null;
	private Label label=null;
	
	private static final String JAVA_MATH_BIG_DECIMAL = "java.math.BigDecimal";
	private static final String JAVA_UTIL_DATE = "java.util.Date";
	private static final String JAVA_LANG_INTEGER = "java.lang.Integer";
	private static final String JAVA_LANG_DOUBLE = "java.lang.Double";
	private static final String JAVA_LANG_FLOAT = "java.lang.Float";
	private static final String JAVA_LANG_SHORT = "java.lang.Short";
	private static final String JAVA_LANG_LONG = "java.lang.Long";
	
	@Override
	public int getListenerType() {
		return SWT.MouseHover;
	}
	
	@Override
	public void mouseAction(
			PropertyDialogButtonBar propertyDialogButtonBar,
			ListenerHelper helpers,Event event,Widget... widgets) {
		
		  table=(Table)widgets[0];
	      TableItem item = table.getItem(new Point(event.x, event.y));
         if (item != null && item.getForeground().getRed()==255) {
           if (tip != null && !tip.isDisposed()){
        	   tip.dispose();
           }
           tip = new Shell(table.getShell(), SWT.ON_TOP | SWT.TOOL);
           tip.setLayout(new FormLayout());
           label = new Label(tip, SWT.NONE);
           label.setForeground(table.getParent().getShell().getDisplay()
               .getSystemColor(SWT.COLOR_INFO_FOREGROUND));
           label.setBackground(table.getParent().getShell().getDisplay()
               .getSystemColor(SWT.COLOR_INFO_BACKGROUND));
           label.setData("_TABLEITEM", item);
           label.setText(setAppropriateToolTipMessage(item,(String)helpers.get(HelperType.COMPONENT_TYPE)));
           label.addListener(SWT.MouseExit,ListenerFactory.Listners.DISPOSE_LISTENER
					.getListener().getListener(propertyDialogButtonBar, helpers, widgets) );
           label.addListener(SWT.MouseDown,ListenerFactory.Listners.DISPOSE_LISTENER
					.getListener().getListener(propertyDialogButtonBar, helpers, widgets));
           Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
           Point pt = table.toDisplay(event.x, event.y);
           tip.setBounds(pt.x, pt.y-20, size.x, size.y);
           tip.setVisible(true);
           table.setData("tip",tip);
           table.setData("label", label);
	}
}
	private String setAppropriateToolTipMessage(TableItem item,String componentType)
	{
		GridRow basicSchemaGridRow=(GridRow)item.getData();
		if(StringUtils.equalsIgnoreCase(basicSchemaGridRow.getDataTypeValue(), JAVA_UTIL_DATE) && (StringUtils.isBlank(basicSchemaGridRow.getDateFormat())))
			return Messages.DATE_FORMAT_MUST_NOT_BE_BLANK;
		if((StringUtils.equalsIgnoreCase(basicSchemaGridRow.getDataTypeValue(), JAVA_MATH_BIG_DECIMAL)))
		{			 
			if(StringUtils.isBlank(basicSchemaGridRow.getPrecision())&& (StringUtils.containsIgnoreCase(componentType, "hive")||StringUtils.containsIgnoreCase(componentType, "parquet")))
			{
		    return Messages.PRECISION_MUST_NOT_BE_BLANK;
		    }	
			else if(!(basicSchemaGridRow.getPrecision().matches("\\d+")) &&StringUtils.isNotBlank(basicSchemaGridRow.getPrecision()))
			{
				return Messages.PRECISION_MUST_CONTAINS_NUMBER_ONLY_0_9;
			}	
			else if((StringUtils.isBlank(basicSchemaGridRow.getScale())))
			{
				 return Messages.SCALE_MUST_NOT_BE_BLANK;
			}
			else if(!(basicSchemaGridRow.getScale().matches("\\d+")))
			{
				return Messages.SCALE_MUST_CONTAINS_NUMBER_ONLY_0_9;
			}	
			else if(StringUtils.equalsIgnoreCase(basicSchemaGridRow.getScaleTypeValue(),"none"))
			{
				return Messages.SCALETYPE_MUST_NOT_BE_NONE;
			}	
		}
		
		if(basicSchemaGridRow instanceof GenerateRecordSchemaGridRow){
			
			GenerateRecordSchemaGridRow generateRecordSchemaGridRow = (GenerateRecordSchemaGridRow) basicSchemaGridRow;

			if (StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeFrom())
					|| StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeTo())) {

				if (StringUtils.equalsIgnoreCase(generateRecordSchemaGridRow.getDataTypeValue(), JAVA_UTIL_DATE)) {
					
					Date rangeFromDate = null, rangeToDate = null;
					SimpleDateFormat formatter = new SimpleDateFormat(generateRecordSchemaGridRow.getDateFormat());

					if (StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeFrom())) {
						try {
							rangeFromDate = formatter.parse(generateRecordSchemaGridRow.getRangeFrom());
						} catch (ParseException e) {
							return Messages.RANGE_FROM_DATE_INCORRECT_PATTERN;
						}
					}

					if (StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeTo())) {
						try {
							rangeToDate = formatter.parse(generateRecordSchemaGridRow.getRangeTo());
						} catch (ParseException e) {
							return Messages.RANGE_TO_DATE_INCORRECT_PATTERN;
						}
					}

					if (rangeFromDate != null && rangeToDate != null && rangeFromDate.after(rangeToDate))
						return Messages.RANGE_FROM_GREATER_THAN_RANGE_TO;

				} else if (StringUtils.equalsIgnoreCase(generateRecordSchemaGridRow.getDataTypeValue(), JAVA_MATH_BIG_DECIMAL)) {

					BigDecimal rangeFrom = null, rangeTo = null;

					if (StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeFrom()))
						rangeFrom = new BigDecimal(generateRecordSchemaGridRow.getRangeFrom());
					if (StringUtils.isNotBlank(generateRecordSchemaGridRow.getRangeTo()))
						rangeTo = new BigDecimal(generateRecordSchemaGridRow.getRangeTo());

					if (rangeFrom != null && rangeTo != null) {
						if (rangeFrom.compareTo(rangeTo) > 0)
							return Messages.RANGE_FROM_GREATER_THAN_RANGE_TO;
					}

					int fieldLength = 0, scaleLength = 0;

					if (!generateRecordSchemaGridRow.getLength().isEmpty())
						fieldLength = Integer.parseInt(generateRecordSchemaGridRow.getLength());

					if (!generateRecordSchemaGridRow.getScale().isEmpty())
						scaleLength = Integer.parseInt(generateRecordSchemaGridRow.getScale());

					if (fieldLength < 0)
						return Messages.FIELD_LENGTH_LESS_THAN_ZERO;
					else if (scaleLength < 0)
						return Messages.FIELD_SCALE_LESS_THAN_ZERO;
					else if (scaleLength >= fieldLength)
						return Messages.FIELD_SCALE_NOT_LESS_THAN_FIELD_LENGTH;
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
						
						if(fieldLength == 0){
							minPermissibleRangeValue="0";
							maxPermissibleRangeValue="0";
						}
						
						BigDecimal minRangeValue = new BigDecimal(minPermissibleRangeValue);
						BigDecimal maxRangeValue = new BigDecimal(maxPermissibleRangeValue);

						if (rangeFrom != null && fieldLength > 0 && rangeTo != null) {
							if (rangeFrom.compareTo(minRangeValue) < 0)
								return Messages.RANGE_FROM_LESS_THAN_MIN_PERMISSIBLE_VALUE;
						} else if(rangeFrom != null && fieldLength > 0 && rangeTo == null){
							if (rangeFrom.compareTo(maxRangeValue) > 0)
								return Messages.RANGE_FROM_GREATER_THAN_MAX_PERMISSIBLE_VALUE;
						}

						if (rangeTo != null && fieldLength > 0 && rangeFrom != null) {
							if (rangeTo.compareTo(maxRangeValue) > 0)
								return Messages.RANGE_TO_GREATER_THAN_MAX_PERMISSIBLE_VALUE;
						} else if(rangeTo != null && fieldLength > 0 && rangeFrom == null){
							if (rangeTo.compareTo(minRangeValue) < 0)
								return Messages.RANGE_TO_LESS_THAN_MIN_PERMISSIBLE_VALUE;
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

					if (rangeFrom != null && rangeTo != null) {
						if (rangeFrom.compareTo(rangeTo) > 0)
							return Messages.RANGE_FROM_GREATER_THAN_RANGE_TO;
					}
					
					int fieldLength = 0;

					if (!generateRecordSchemaGridRow.getLength().isEmpty())
						fieldLength = Integer.parseInt(generateRecordSchemaGridRow.getLength());

					if (fieldLength < 0) {
						return Messages.FIELD_LENGTH_LESS_THAN_ZERO;
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
						
						if(fieldLength == 0){
							minPermissibleRangeValue="0";
							maxPermissibleRangeValue="0";
						}
							
						
						BigDecimal minRangeValue = new BigDecimal(minPermissibleRangeValue);
						BigDecimal maxRangeValue = new BigDecimal(maxPermissibleRangeValue);
					
						
						if (rangeFrom != null && fieldLength > 0 && rangeTo != null) {
							if (rangeFrom.compareTo(minRangeValue) < 0)
								return Messages.RANGE_FROM_LESS_THAN_MIN_PERMISSIBLE_VALUE;
							else if (rangeTo.compareTo(maxRangeValue) > 0)
								return Messages.RANGE_TO_GREATER_THAN_MAX_PERMISSIBLE_VALUE;
						} else if(rangeFrom != null && fieldLength > 0 && rangeTo == null){
							if (rangeFrom.compareTo(maxRangeValue) > 0)
								return Messages.RANGE_FROM_GREATER_THAN_MAX_PERMISSIBLE_VALUE;
						} else if(rangeTo != null && fieldLength > 0 && rangeFrom == null){
							if (rangeTo.compareTo(minRangeValue) < 0)
								return Messages.RANGE_TO_LESS_THAN_MIN_PERMISSIBLE_VALUE;
						}
					}
				}
			}
		}
		
		if(basicSchemaGridRow instanceof FixedWidthGridRow)
		{
			FixedWidthGridRow fixedWidthGridRow=(FixedWidthGridRow)basicSchemaGridRow;
			
			if(fixedWidthGridRow instanceof MixedSchemeGridRow)
			{
				if(StringUtils.isBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isBlank(fixedWidthGridRow.getLength()))
				{
					return Messages.LENGTH_OR_DELIMITER_MUST_NOT_BE_BLANK;
				}	
				else if(!(fixedWidthGridRow.getLength().matches("\\d+")))
				{
					return Messages.LENGTH_MUST_BE_AN_INTEGER_VALUE;
				}
				
				else if(StringUtils.isNotBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isNotBlank(fixedWidthGridRow.getLength()))
				{
					return Messages.ONLY_SPECIFY_LENGTH_OR_DELIMITER;
				}
				
				
			}
			else
			{
			if(StringUtils.isBlank(fixedWidthGridRow.getLength()))
			{
				return Messages.LENGTH_MUST_NOT_BE_BLANK;
			}	
			else if(!(fixedWidthGridRow.getLength().matches("\\d+")))
			{
				return Messages.LENGTH_MUST_BE_AN_INTEGER_VALUE;
			}
			
			}
		}	
			return "";
	}
}
