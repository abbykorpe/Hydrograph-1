package com.bitwise.app.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class ParameterUtil
 * 
 * @author Bitwise
 * 
 */

public class ParameterUtil {

	public static ParameterUtil INSTANCE=new ParameterUtil();
	
	private ParameterUtil(){}
	
	
	public boolean isParameter(String input) {
		if (input != null) {
			String regex = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}";
			Matcher matchs = Pattern.compile(regex).matcher(input);
			if (matchs.matches()) {
				return true;
			}
		}
		return false;
	}
}
