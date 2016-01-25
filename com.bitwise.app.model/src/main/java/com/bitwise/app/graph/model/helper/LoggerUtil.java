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
