package com.bitwise.app.tooltip.utils;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;

/**
 * 
 * ToolTipUtils - Tooltip utils class
 * 
 * @author Bitwise
 *
 */
public class ToolTipUtils {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ToolTipUtils.class);
	
	/**
	 * returns length of longest string in string array
	 * 
	 * @param lines - Array of strings
	 * @return maxlength
	 */
	public static int getMaxLength(String[] lines) {
		
		int maxLength=0;		
		for(int i=0;i<lines.length;i++){
			logger.debug("ToolTipUtils.getMaxLength: lines["+ i+"]=" + lines[i]);
			if(lines[i].length() > maxLength){
				maxLength = lines[i].length();
			}
		}
		logger.debug("ToolTipUtils.getMaxLength: max length=" + maxLength);
		return maxLength;
	}
	
	
	public static String[] getLines(String text) {
		String[] lines = text.split("\\n");
		return lines;
	}
}
