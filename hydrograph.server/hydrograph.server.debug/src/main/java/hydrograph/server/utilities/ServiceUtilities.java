/*******************************************************************************
 *  Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package hydrograph.server.utilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.server.service.HydrographService;

/**
 * The utility class for the service. This class is meant to be stateless and
 * provide just static utility methods. There should not be any instance
 * variables of this class. The constructor has deliberately been made private
 * in order to discourage users from creating instance of this class
 *
 * @author Prabodh
 *
 */
public class ServiceUtilities {

	private static String SERVICE_CONFIG = "ServiceConfig";

	private static final Logger LOG = LoggerFactory.getLogger(ServiceUtilities.class);

	/**
	 *
	 */
	private ServiceUtilities() {

	}

	/**
	 * Returns the {@link ResourceBundle} object for the service config file
	 *
	 * @return the {@link ResourceBundle} object for the service config file
	 */
	public static ResourceBundle getServiceConfigResourceBundle() {
		return getResourceBundle(SERVICE_CONFIG);
	}

	private static ResourceBundle getResourceBundle(String resource) {
		ResourceBundle resBundl = ResourceBundle.getBundle(SERVICE_CONFIG);

		// Try to get ServiceConfig.properties file from the config folder of
		// the project
		// Can also use the default one provided in the package.
		String basePath = HydrographService.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		basePath = basePath.substring(0, basePath.lastIndexOf("/"));
		basePath = basePath.substring(0, basePath.lastIndexOf("/")) + "/config/";

		LOG.info("Base Path:" + basePath);

		File file = new File(basePath);

		LOG.info("FILE PATH: " + basePath);

		if (file.exists()) {
			LOG.info("Using ServiceConfig from: " + basePath);

			try {
				URL[] resourceURLs = { file.toURI().toURL() };
				URLClassLoader urlLoader = new URLClassLoader(resourceURLs);
				resBundl = ResourceBundle.getBundle("ServiceConfigOverride", Locale.getDefault(), urlLoader);
				LOG.info("PORT:" + resBundl.getString(Constants.PORT_ID));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}

		return resBundl;
	}	

}
