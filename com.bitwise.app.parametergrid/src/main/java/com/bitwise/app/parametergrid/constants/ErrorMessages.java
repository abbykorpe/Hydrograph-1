package com.bitwise.app.parametergrid.constants;

import org.eclipse.osgi.util.NLS;

public class ErrorMessages extends NLS{

	private static final String BUNDLE_NAME = "com.bitwise.app.parametergrid.constants.ErrorMessages"; //$NON-NLS-1$
	
	public static String UNABLE_TO_LOAD_PARAM_FILE1;
	public static String UNABLE_TO_LOAD_PARAM_FILE2;
	public static String UNABLE_TO_LOAD_PARAM_FILE3;
	public static String PARAMETER_NAME_CAN_NOT_BE_BLANK;
	public static String PARAMETER_FILE_NOT_LOADED;
	public static String UNABLE_TO_STORE_PARAMETERS;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ErrorMessages.class);
	}

	private ErrorMessages() {
	}
}
