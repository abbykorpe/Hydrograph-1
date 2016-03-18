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

 
package com.bitwise.app.engine.xpath;

public enum ComponentXpathConstants {
	COMPONENT_CHARSET_XPATH("/graph/*[@id='$id']/charset"),
	COMPONENT_JOIN_TYPE_XPATH("/graph/operations[@id='$id']/keys[@inSocketId='$inSocketId'] [not(@joinType)]"),
	COMPONENT_XPATH_BOOLEAN("/graph/*[@id='$id']/propertyName"),
	COMPONENT_XPATH_COUNT("/graph/*[@id='$id']/maxRecords"),
	FILTER_INPUT_FIELDS("/graph/operations[@id='$id']/operation[@id='filter_opt']/inputFields"),
	AGGREGATE_PRIMARY_KEYS("/graph/operations[@id='$id']/primaryKeys"),
	AGGREGATE_SECONDARY_KEYS("/graph/operations[@id='$id']/secondaryKeys"),
	
	LOOKUP_KEYS("/graph/operations[@id='$id']/keys[@inSocketId='$inSocketId']"),
	
	REMOVEDUPS_PRIMARY_KEYS("/graph/straightPulls[@id='$id']/primaryKeys"),
	REMOVEDUPS_SECONDARY_KEYS("/graph/straightPulls[@id='$id']/secondaryKeys"),
	
	SORT_PRIMARY_KEYS("/graph/straightPulls[@id='$id']/primaryKeys"),
	SORT_SECONDARY_KEYS("/graph/straightPulls[@id='$id']/secondaryKeys"),
	
	JOIN_KEYS("/graph/operations[@id='$id']/keys[@inSocketId='$inSocketId']"),;
	
	private final String value;

	ComponentXpathConstants(String value) {
		this.value = value;
	}

	public String value() { 
		return value;
	}

	public static ComponentXpathConstants fromValue(String value) {
		for (ComponentXpathConstants xpathConstants : ComponentXpathConstants.values()) {
			if (xpathConstants.value.equals(value)) {
				return xpathConstants;
			}
		}
		throw new IllegalArgumentException(value);
	}

}
