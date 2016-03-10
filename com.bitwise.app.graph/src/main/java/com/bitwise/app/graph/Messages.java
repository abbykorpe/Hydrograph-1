package com.bitwise.app.graph;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "resources.messages"; //$NON-NLS-1$
	public static String GRADLE_RUN;
	public static String XMLPATH;
	public static String DEBUGXMLPATH;
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
	public static String MESSAGE_INFORMATION;
	
	public static final String DEBUG_DEFAULT = "Default";
	public static final String DEBUG_CUSTOM = "Custom";
	public static final String DEBUG_ALL = "All";
	public static final String ADD_WATCH_POINT_TEXT = "Add Watch Point";
	public static final String WATCH_RECORD_TEXT = "Watch Records";
	public static final String REMOVE_WATCH_POINT_TEXT = "Remove Watch Point";
	public static final String RECORD_LIMIT = "Record Limit";
	public static final String LIMIT_VALUE = "Limit Value";
	public static final String DEFAULT_LIMIT = "Default Limit : 100";
	 
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
