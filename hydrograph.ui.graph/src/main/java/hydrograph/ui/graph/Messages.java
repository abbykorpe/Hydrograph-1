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

 
package hydrograph.ui.graph;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "resources.messages"; //$NON-NLS-1$
	public static String GRADLE_RUN;
	public static String XMLPATH;
	public static String PARAM_FILE;
	public static String PARAM;
	public static String CONSOLE_NAME;
	public static String JOBEXTENSION;
	public static String XMLEXTENSION;
	public static String CMD;
	public static String SHELL;
	public static String CLUSTER_PASSWORD;
	public static String KILL_JOB;
	public static String HOST;
	public static String USERNAME;
	public static String PROCESS_ID;
	 
	public static String OPEN_GRAPH_TO_RUN;
	public static String KILL_JOB_MESSAGEBOX_TITLE;
	public static String KILL_JOB_MESSAGE;

	public static String GRADLE_TASK_FAILED;
	public static String CURRENT_JOB_ID;
	
	public static String MESSAGES_BEFORE_CLOSE_WINDOW;
	public static String JOB_ID;
	public static String BASE_PATH;
	public static String DEBUG_ALERT_MESSAGE;
	public static String NO_RECORD_FETCHED;
	
	public static String REMOTE_MODE_TEXT;
	public static String DEBUG_WIZARD_TEXT;
	public static String DEBUG_DEFAULT;
	public static String DEBUG_CUSTOM;
	public static String DEBUG_ALL;
	public static String ADD_WATCH_POINT_TEXT;
	public static String WATCH_RECORD_TEXT;
	public static String REMOVE_WATCH_POINT_TEXT;
	public static String RECORD_LIMIT;
	public static String LIMIT_VALUE;
	public static String DEFAULT_LIMIT;
	public static String MESSAGE_INFORMATION;
	public static String CONFIRM_TO_CREATE_SUBJOB_MESSAGE;
	public static String CONFIRM_TO_CREATE_SUBJOB_WINDOW_TITLE;
	public static String HELP;
	public static String PROPERTIES;
	
	 
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
