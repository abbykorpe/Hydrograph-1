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
package hydrograph.engine.cascading.integration;

import hydrograph.engine.utilities.GeneralUtilities;

public class CommandLineOptionsProcessor {

	public static final String OPTION_DOT_PATH = "dotpath";
	public static final String OPTION_NO_EXECUTION = "noexecution";

	public static String getDotPath(String[] args) {
		String[] paths;

		paths = GeneralUtilities.getArgsOption(args, OPTION_DOT_PATH);

		if (paths != null) {
			// only the first path
			return paths[0];
		} else {
			return null;
		}
	}

}
