package com.bitwise.app.graph.model.processor;

import java.util.Hashtable;

import org.slf4j.Logger;

import com.bitwise.app.common.component.config.Component;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;

/**
 * The Class DynamicClassProcessor.
 */
public class DynamicClassProcessor{
	public static final DynamicClassProcessor INSTANCE = new DynamicClassProcessor(); 

	private static final Logger logger = LogFactory.INSTANCE.getLogger(DynamicClassProcessor.class);
	private Hashtable<String, Class<?>> classMapStringToClass = new Hashtable<String, Class<?>>();
	private Hashtable<Class<?>, String> classMapClassToString = new Hashtable<Class<?>, String>();
	
	/**
	 * Contains.
	 * 
	 * @param clazz
	 *            the clazz
	 * @return true, if successful
	 */
	public boolean contains(Class<?> clazz) {
		return classMapStringToClass.containsValue(clazz);
	}
	
	/**
	 * Contains.
	 * 
	 * @param clazzName
	 *            the clazz name
	 * @return true, if successful
	 */
	public boolean contains(String clazzName) {
		return classMapClassToString.containsValue(clazzName);
	}
	
	private void put(String clazzName, Class<?> clazz){
		classMapStringToClass.put(clazzName, clazz);
		classMapClassToString.put(clazz, clazzName);
	}
	
	/**
	 * Gets the clazz name.
	 * 
	 * @param clazz
	 *            the clazz
	 * @return the clazz name
	 */
	public String getClazzName(Class<?> clazz){
		return classMapClassToString.get(clazz);
	}
	
	/**
	 * Gets the clazz.
	 * 
	 * @param className
	 *            the class name
	 * @return the clazz
	 */
	public Class<?> getClazz(String className){
		return classMapStringToClass.get(className);
	}
	
	/**
	 * Creates the class.
	 * 
	 * @param componentConfig
	 *            the component config
	 * @return the class
	 */
	public Class<?> createClass(Component componentConfig){
		if(contains(componentConfig.getName())){
			return getClazz(componentConfig.getName());
		}
		else{
			Class<?> clazz;
			try {
				clazz = this.getClass().getClassLoader().loadClass(Constants.COMPONENT_PACKAGE_PREFIX + componentConfig.getName());
			} catch (ClassNotFoundException exception) {
				logger.error("Failed to load component {} due to {}", componentConfig.getName(), exception);
				throw new RuntimeException();
			}
			INSTANCE.put(componentConfig.getName(), clazz);
			return clazz;
		}
	}
}