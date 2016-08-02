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

package hydrograph.ui.expression.editor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "resources.messages"; //$NON-NLS-1$
	public static String CANNOT_OPEN_EDITOR;
	public static String MESSAGE_TO_EXIT_WITHOUT_SAVE;
	public static String JAVA_DOC_NOT_AVAILABLE;
	public static String OPERATOR_FILE_NOT_FOUND;
	public static String WARNING;
	public static String ERROR_TITLE;
	public static String JAR_FILE_COPY_ERROR;
	public static String DUPLICATE_JAR_FILE_COPY_ERROR;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
