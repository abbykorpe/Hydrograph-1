package com.bitwise.app.graph;

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
	
	 
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
