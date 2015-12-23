package com.bitwise.app.menus.messages;

import org.eclipse.osgi.util.NLS;

/**
 * The Class Messages.
 * 
 * @author Bitwise
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.bitwise.app.menus.messages.messages";
	public static String TITLE;
	public static String ALREADY_EXISTS_ERROR_MESSAGE;
	public static String SOURCE_EMPTY_ERROR_MESSAGE;
	public static String NOTE_MESSAGE_TEXT;
	public static String NOTE_LABEL_HEADER_TEXT;
	public static String NEW_FILE_LABEL_TEXT;
	public static String SELECT_FILE_LABEL_TEXT;
	public static String IMPORT_WINDOW_TITLE_TEXT;
	public static String INVALID_TARGET_FILE_ERROR;
	public static String EXCEPTION_OCCURED;	
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
