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

package com.bitwise.app.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to check if String is parameter.
 * 
 * @author Bitwise 
 * 
 */

public class ParameterUtil {
	
	private ParameterUtil(){}
	
	
	public static boolean isParameter(String input) {
		if (input != null) {
			String regex = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}";
			Matcher matchs = Pattern.compile(regex).matcher(input);
			if (matchs.matches()) {
				return true;
			}
		}
		return false;
	}
}
