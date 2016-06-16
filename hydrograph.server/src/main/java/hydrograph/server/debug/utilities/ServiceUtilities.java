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
package hydrograph.server.debug.utilities;

import java.util.ResourceBundle;

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

	/**
	 * 
	 */
	private ServiceUtilities() {

	}

	/**
	 * Returns the {@link ResourceBundle} object for the service config file
	 * @return the {@link ResourceBundle} object for the service config file
	 */
	public static ResourceBundle getServiceConfigResourceBundle() {
		return getResourceBundle(SERVICE_CONFIG);
	}

	private static ResourceBundle getResourceBundle(String resource) {
		return ResourceBundle.getBundle(SERVICE_CONFIG);
	}

}
