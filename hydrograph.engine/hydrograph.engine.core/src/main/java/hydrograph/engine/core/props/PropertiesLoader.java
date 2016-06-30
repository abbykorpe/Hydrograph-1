/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.engine.core.props;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLoader {

	public static final String ENGINE_PROPERTY_FILE = "engine.properties";
	public static final String INPUT_SERVICE_PROPERTY_FILE = "input_service.properties";
	public static final String RUNTIME_SERVICE_PROPERTY_FILE = "runtime_service.properties";
	public static final String XPATH_PROPERTY_FILE = "xpath.properties";

	private Properties engineProps;
	private Properties inputServiceProps;
	private Properties runtimeServiceProps;
	private Properties xpathProps;

	private static final Logger LOG = LoggerFactory
			.getLogger(PropertiesLoader.class);

	private static PropertiesLoader loaderInstance = null;

	private PropertiesLoader() {
		loadAllProperties();

	}

	public static PropertiesLoader getInstance() {

		if (loaderInstance == null) {
			loaderInstance = new PropertiesLoader();
		}
		return loaderInstance;

	}

	public String getInputServiceClassName() {
		String name = engineProps.getProperty("inputService");
		if (name == null) {
			throw new PropertiesException(
					"Unable to find property inputService in engine properties");
		}

		return name;
	}

	public String getRuntimeServiceClassName() {
		String name = engineProps.getProperty("runtimeService");
		if (name == null) {
			throw new PropertiesException(
					"Unable to find property runtimeService in engine properties");
		}

		return name;

	}

	public Properties getEngineProperties() {
		return engineProps;
	}

	public Properties getInputServiceProperties() {
		return inputServiceProps;
	}

	public Properties getRuntimeServiceProperties() {
		return runtimeServiceProps;
	}

	public Properties getXpathProperties() {
		return xpathProps;
	}

	private void loadAllProperties() {

		engineProps = loadPropertiesFile(ENGINE_PROPERTY_FILE);

		inputServiceProps = loadPropertiesFile(INPUT_SERVICE_PROPERTY_FILE);
		runtimeServiceProps = loadPropertiesFile(RUNTIME_SERVICE_PROPERTY_FILE);
		xpathProps = loadPropertiesFile(XPATH_PROPERTY_FILE);
	}

	private Properties loadPropertiesFile(String fileName) {
		Properties properties = new Properties();

		String propFileName = null;

		propFileName = System.getProperty(fileName);
		if (propFileName == null) {
			propFileName = fileName;
			try {
				properties.load(ClassLoader
						.getSystemResourceAsStream(propFileName));
			} catch (Exception e) {
				throw new UnableToLoadPropertiesException(e);
			}
		} else {
			FileReader reader = null;
			try {
				reader = new FileReader(propFileName);
				properties.load(reader);
			} catch (Exception e) {
				throw new UnableToLoadPropertiesException(e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						LOG.error("Error closing the properties file: "
								+ propFileName, e);
						throw new RuntimeException(
								"Error closing the properties file: "
										+ propFileName, e);
					}
				}
			}
		}

		LOG.info("Loading property file " + propFileName);

		return properties;

	}

	private class UnableToLoadPropertiesException extends RuntimeException {
		private static final long serialVersionUID = 4031525765978790699L;

		public UnableToLoadPropertiesException(Throwable e) {
			super(e);
		}
	}

	private class PropertiesException extends RuntimeException {

		private static final long serialVersionUID = 6239127278298773996L;

		public PropertiesException(String msg) {
			super(msg);
		}

	}
}
