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

 
package com.bitwise.app.graph.model.helper;

import java.util.HashMap;

import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * @author Bitwise
 * 
 */
public class LoggerUtil {
	private static HashMap<Class<?>, Logger> loggerMap = new HashMap<>();

	/**
	 * Gets the loger.
	 * 
	 * @param clazz
	 *            the clazz
	 * @return the loger
	 */
	public static Logger getLoger(Class<?> clazz) {
		Logger tempLogger = loggerMap.get(clazz);
		if (tempLogger != null) {
			return tempLogger;
		} else {
			tempLogger = LogFactory.INSTANCE.getLogger(clazz);
			loggerMap.put(clazz, tempLogger);
			return tempLogger;
		}
	}
}
