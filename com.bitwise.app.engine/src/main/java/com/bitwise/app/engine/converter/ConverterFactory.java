package com.bitwise.app.engine.converter;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.exceptions.ConverterNotFoundException;
import com.bitwise.app.engine.exceptions.DummyComponentException;
import com.bitwise.app.graph.model.Component;

/**
 * Factory class for creating Converter instances for particular component
 * 
 */
public class ConverterFactory {
	public static final ConverterFactory INSTANCE = new ConverterFactory();
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ConverterFactory.class);

	private ConverterFactory() {
	}

	public Converter getConverter(Component component) throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		try {
			logger.debug("Getting converter for :{}", component.getProperties().get(Constants.PARAM_NAME));
			return (Converter) Class.forName(component.getConverter()).getDeclaredConstructor(Component.class)
					.newInstance(component);
		} catch (ClassNotFoundException exception) {
			logger.error("Exception Occured getting Converter for {}, {}:", new Object[] {
					component.getProperties().get(Constants.PARAM_NAME), exception });
			if (component.getComponentName().equalsIgnoreCase(Constants.DUMMY_COMPONENT))
				throw new DummyComponentException(component.getComponentName(), exception);
			else
				throw new ConverterNotFoundException(component.getPrefix(), exception);
		}
	}
}
