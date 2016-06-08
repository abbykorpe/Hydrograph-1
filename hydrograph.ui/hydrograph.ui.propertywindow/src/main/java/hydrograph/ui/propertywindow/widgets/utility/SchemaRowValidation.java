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
	
	public static void highlightInvalidRowWithRedColor(GridRow gridRow,Table table,String componentType )
	{ 
		for(TableItem item:table.getItems())
		{
			if(gridRow==null)
		    gridRow=(GridRow)item.getData();	
		    if(StringUtils.equalsIgnoreCase(item.getText(),gridRow.getFieldName()))
			{
			
		    	
		    	if(StringUtils.equalsIgnoreCase(gridRow.getDataTypeValue(), "java.math.BigDecimal"))
		    	{
		    		if(StringUtils.containsIgnoreCase(componentType, "hive")||StringUtils.containsIgnoreCase(componentType, "parquet"))
		    		{
		    			if(StringUtils.isBlank(gridRow.getPrecision())|| StringUtils.isBlank(gridRow.getScale()) ||
			    				StringUtils.equalsIgnoreCase(gridRow.getScaleTypeValue(), "none")||
								!(gridRow.getScale().matches("\\d+"))||!(gridRow.getPrecision().matches("\\d+"))
								)
		    			{
		    				item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
		    			}
		    			else
						{
							item.setForeground(new Color(Display.getDefault(), 0, 0, 0));
						}	
		    		}	
		    		else if(StringUtils.isBlank(gridRow.getScale()) ||
		    				StringUtils.equalsIgnoreCase(gridRow.getScaleTypeValue(), "none")||
							!(gridRow.getScale().matches("\\d+"))||(!(gridRow.getPrecision().matches("\\d+"))&& StringUtils.isNotBlank(gridRow.getPrecision()))
							)
		    		{
		    			item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
		    		}	
		    		else if(gridRow instanceof FixedWidthGridRow && !(gridRow instanceof GenerateRecordSchemaGridRow))
					{
					
						FixedWidthGridRow fixedWidthGridRow=(FixedWidthGridRow)gridRow;
						if(fixedWidthGridRow instanceof MixedSchemeGridRow)
						{
							if((StringUtils.isBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isBlank(fixedWidthGridRow.getLength())))
							{
								item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
							}	
							else if(StringUtils.isNotBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isNotBlank(fixedWidthGridRow.getLength()))	
							{
								item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
							}
							else if(StringUtils.isNotBlank(fixedWidthGridRow.getLength())&& !(fixedWidthGridRow.getLength().matches("\\d+")))	
							{
								item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
							}
							else
							{
								item.setForeground(new Color(Display.getDefault(), 0, 0, 0));
							}	
							
						}
						else
						{
						if(StringUtils.isBlank(fixedWidthGridRow.getLength())||!(fixedWidthGridRow.getLength().matches("\\d+")))
						{
							item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
						}	
						else
						{
							item.setForeground(new Color(Display.getDefault(), 0, 0, 0));
						}	
						}	
					}	
		    		else
					{
						item.setForeground(new Color(Display.getDefault(), 0, 0, 0));
					}	
		    	}	
		    	
		    	
				
				else if(StringUtils.equalsIgnoreCase(gridRow.getDataTypeValue(),"java.util.Date"))
				{
					if((StringUtils.isBlank(gridRow.getDateFormat())))
					item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
					else if(gridRow instanceof FixedWidthGridRow && !(gridRow instanceof GenerateRecordSchemaGridRow))
					{
					
						FixedWidthGridRow fixedWidthGridRow=(FixedWidthGridRow)gridRow;
						if(fixedWidthGridRow instanceof MixedSchemeGridRow)
						{
							if((StringUtils.isBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isBlank(fixedWidthGridRow.getLength())))
							{
								item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
							}	
							else if(StringUtils.isNotBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isNotBlank(fixedWidthGridRow.getLength()))	
							{
								item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
							}
							else if(StringUtils.isNotBlank(fixedWidthGridRow.getLength())&& !(fixedWidthGridRow.getLength().matches("\\d+")))	
							{
								item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
							}
							else
							{
								item.setForeground(new Color(Display.getDefault(), 0, 0, 0));
							}	
							
						}
						else
						{
						if(StringUtils.isBlank(fixedWidthGridRow.getLength())||!(fixedWidthGridRow.getLength().matches("\\d+")))
						{
							item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
						}	
						else
						{
							item.setForeground(new Color(Display.getDefault(), 0, 0, 0));
						}	
						}	
					}	
					else
					{
						item.setForeground(new Color(Display.getDefault(), 0, 0, 0));
					}	
					
					
				}
				else if(gridRow instanceof FixedWidthGridRow && !(gridRow instanceof GenerateRecordSchemaGridRow))
				{
				
					FixedWidthGridRow fixedWidthGridRow=(FixedWidthGridRow)gridRow;
					if(fixedWidthGridRow instanceof MixedSchemeGridRow)
					{
						if((StringUtils.isBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isBlank(fixedWidthGridRow.getLength())))
						{
							item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
						}	
						else if(StringUtils.isNotBlank(fixedWidthGridRow.getDelimiter()) && StringUtils.isNotBlank(fixedWidthGridRow.getLength()))	
						{
							item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
						}
						else if(StringUtils.isNotBlank(fixedWidthGridRow.getLength())&& !(fixedWidthGridRow.getLength().matches("\\d+")))	
						{
							item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
						}
						else
						{
							item.setForeground(new Color(Display.getDefault(), 0, 0, 0));
						}	
						
					}
					else
					{
					if(StringUtils.isBlank(fixedWidthGridRow.getLength())||!(fixedWidthGridRow.getLength().matches("\\d+")))
					{
						item.setForeground(new Color(Display.getDefault(), 255, 0, 0));
					}	
					else
					{
						item.setForeground(new Color(Display.getDefault(), 0, 0, 0));
					}	
					}	
				}	
				
				gridRow=null;
			}	
		    
		}	
	}

}
