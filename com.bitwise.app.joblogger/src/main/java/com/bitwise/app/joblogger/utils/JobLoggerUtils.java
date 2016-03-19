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

 
package com.bitwise.app.joblogger.utils;

import java.sql.Timestamp;

import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
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
