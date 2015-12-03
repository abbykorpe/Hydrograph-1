package com.bitwise.app.propertywindow.messages;

import org.eclipse.osgi.util.NLS;

/**
 * The Class Messages.
 * 
 * @author Bitwise
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.bitwise.app.propertywindow.messages.messages";
	public static String RUNTIME_WINDOW_NAME;
	public static String RUNTIME_HEADER;
	public static String LENGTHERROR;
	public static String DATATYPELIST;
	public static String FIELDNAME;
	public static String DATATYPE;
	public static String DATEFORMAT;
	public static String FIELDNAMEERROR;
	public static String FIELDPHASE;
	public static String CHARACTERSET;
	public static String SCALE;
	public static String SCALEERROR;
	public static String RuntimePropertAlreadyExists;
	public static String MessageBeforeClosingWindow;
	public static String SelectRowToDelete;
	public static String ConfirmToDeleteAllProperties;
	public static String CustomWindowOnButtonWidget_EDIT;
	public static String PropertyAppliedNotification;
	public static String EmptyFiledNotification;
	public static String EmptyNameNotification;
	public static String EmptyValueNotification;
	public static String OperationClassBlank;
	public static String FIELD_LABEL_ERROR;
	public static String path;
	public static String INVALID_FILE;
	public static String EMPTYFIELDMESSAGE;
	public static String LENGTH;
	public static String SECONDARY_COLUMN_KEY_WINDOW_NAME;
	public static String SECONDARY_COLUMN_KEY_WINDOW_HEADER;
	public static String EmptyColumnNotification;
	public static String EmptySortOrderNotification;
	public static String EMPTY_FIELD;
	public static String INVALID_SORT_ORDER;
	public static String ALLOWED_CHARACTERS;
	public static String INVALID_CHARACTERS;
	public static String PORT_VALUE;
	public static String FILTER_TEMPLATE;
	public static String TRANSFORM_TEMPLATE;
	public static String AGGREGATE_TEMPLATE;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
