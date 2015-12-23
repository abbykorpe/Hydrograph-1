package com.bitwise.app.joblogger.utils;

import java.sql.Timestamp;

import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
/**
 * 
 * Job logger utility class
 * 
 * @author Bitwise
 *
 */
public class JobLoggerUtils {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(JobLoggerUtils.class);
	/**
	 * get timestamp
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getTimeStamp() {
		java.util.Date date= new java.util.Date();
		logger.debug("Created date object");
		Timestamp timestamp = new Timestamp(date.getTime());
		logger.debug("Created timestamp - {}" , timestamp );
		return timestamp;
	}
}
