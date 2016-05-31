package hydrograph.ui.dataviewer.support;

import hydrograph.ui.dataviewer.datastructures.Schema;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringConverter {
	
	public static Object convert(String value,Schema schema){
		if(schema.getDataType().equalsIgnoreCase("java.lang.String")){
			return value;
		}else if(schema.getDataType().equalsIgnoreCase("java.lang.Integer")){
			return Integer.parseInt(value);
		}else if(schema.getDataType().equalsIgnoreCase("java.lang.Long")){
			return Long.parseLong(value);
		}else if(schema.getDataType().equalsIgnoreCase("java.lang.Double")){
			return Double.parseDouble(value);
		}else if(schema.getDataType().equalsIgnoreCase("java.lang.Float")){
			return Float.parseFloat(value);
		}else if(schema.getDataType().equalsIgnoreCase("java.lang.Short")){
			return Short.parseShort(value);
		}else if(schema.getDataType().equalsIgnoreCase("java.util.Date")){
		    DateFormat df = new SimpleDateFormat(schema.getDateFormat()); 
		    Date startDate;
		    try {
		        startDate = df.parse(value);
		        return startDate;
		        //String newDateString = df.format(startDate);
		        //System.out.println(newDateString);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		}else if(schema.getDataType().equalsIgnoreCase("java.math.BigDecimal")){
			return new BigDecimal(value);
		}
		return null;
	}
}
