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

package hydrograph.ui.dataviewer.constants;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS{
	private static final String BUNDLE_NAME = "resources.messages";
	
	/*view data preferences*/
	public static String MEMORY_FIELD_MESSAGE;
	public static String INTEGER_FIELD_VALIDATION;
	public static String PAGE_SIZE_WARNING;
	public static String DELIMITER_WARNING;
	public static String DELIMITER_VALUE_MATCH_ERROR;
	public static String CHARACTER_LENGTH_ERROR;
	public static String QUOTE_WARNING;
	public static String QUOTE_VALUE_MATCH_ERROR;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
