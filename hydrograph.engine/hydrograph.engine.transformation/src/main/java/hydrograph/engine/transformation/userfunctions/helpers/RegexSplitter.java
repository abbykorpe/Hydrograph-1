/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 *******************************************************************************/
package hydrograph.engine.transformation.userfunctions.helpers;

import java.util.regex.Pattern;

public class RegexSplitter {

	Pattern pattern;

	public RegexSplitter(String regex) {
		this.pattern = getPattern(regex);
	}

	private static Pattern getPattern(String regex) {
		Pattern pattern;
		pattern = Pattern.compile(regex);
		return pattern;

	}

	public String[] split(String value) {
		if (value == null){
			return null;
		}
		return pattern.split(value);
	}

}
