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

 
package com.bitwise.app.propertywindow.messages;

import org.eclipse.osgi.util.NLS;

/**
 * The Class Messages.
 * 
 * @author Bitwise
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "resources.messages";

	public static String COULD_NOT_SAVE_PROPERTY_SHEET;
	public static String INVALID_MAPPING;
	public static String OPERATION_CALSS_LABEL;
	public static String EDIT_BUTTON_LABEL;
	public static String OPEARTION_CLASS_OPEN_BUTTON_MESSAGE;
	public static String OPEN_BUTTON_LABEL;
	public static String CREATE_NEW_OPEARTION_CLASS_LABEL;
	public static String WARNING;
	public static String FILE_NOT_FOUND;
	
	public static String NOT_EXISTS;
	public static  String FILE_DOES_NOT_EXISTS ;

	public static  String SCHEMA_CONFIG_XSD_PATH;

	public static  String FIELDNAME_NOT_ALPHANUMERIC_ERROR;
	public static String EMPTY_XML_CONTENT;
	public static String RUNTIME_WINDOW_NAME;
	public static String RUNTIME_HEADER;
	public static String LENGTHERROR;
	public static String GENERIC_GRIDROW;
	public static String FIXEDWIDTH_GRIDROW;
	public static String GENERATE_RECORD_GRIDROW;
	public static String DATATYPELIST;
	public static String FIELDNAME;
	public static String DATATYPE;
	public static String DATEFORMAT;
	public static String PRECISION;
	public static String SCALE_TYPE;
	public static String SCALE_TYPE_NONE;
	public static String SCALETYPELIST;
	public static String FIELD_DESCRIPTION;
	public static String RANGE_FROM;
	public static String RANGE_TO;
	public static String DEFAULT_VALUE;
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
	public static String PRIMARY_COLUMN_KEY_WINDOW_NAME;
	public static String SECONDARY_COLUMN_KEY_WINDOW_NAME;
	public static String EmptyColumnNotification;
	public static String EmptySortOrderNotification;
	public static String EMPTY_FIELD;
	public static String INVALID_SORT_ORDER;
	public static String ALLOWED_CHARACTERS;
	public static String PROPERTY_NAME_ALLOWED_CHARACTERS;
	public static String INVALID_CHARACTERS;
	public static String PORT_VALUE;
	public static String CUSTOM;
	public static String PROPERTY_NAME;
	public static String PROPERTY_VALUE;
	public static String PROPERTY_DECORATOR;
	public static String PROPERTIES;
	public static String INNER_OPERATION_INPUT_FIELD;
	public static String INNER_OPERATION_OUTPUT_FIELD;
	public static String OPERATIONAL_OUTPUT_FIELD;
	public static String OPERATIONAL_SYSTEM_FIELD;
	public static String EXTERNAL_SCHEMA;
	public static String SCHEMA_TYPES;
	public static String INTERNAL_SCHEMA_TYPE;
	public static String EXTERNAL_SCHEMA_TYPE;
	public static String BROWSE_BUTTON;
	public static String ADD_SCHEMA_TOOLTIP;
	public static String DELETE_SCHEMA_TOOLTIP;
	public static String MOVE_SCHEMA_UP_TOOLTIP;
	public static String MOVE_SCHEMA_DOWN_TOOLTIP;
	public static String IMPORTED_SCHEMA;
	public static String EXPORTED_SCHEMA;
	public static String IMPORT_XML;
	public static String EXPORT_XML;
	public static String IMPORT_XML_ERROR;
	public static String IMPORT_XML_FORMAT_ERROR;
	public static String IMPORT_XML_DUPLICATE_FIELD_ERROR;
	public static String EXPORT_XML_ERROR;
	public static String EXPORT_XML_EMPTY_FILENAME;
	public static String OUTPUT_FIELD_EXISTS;
	public static String AVAILABLE_FIELDS_HEADER;
    public static String ADD_ICON;
    public static String DELETE_ICON;
    public static String UP_ICON;
    public static String DOWN_ICON;
    public static String INFORMATION;
	public static String SAVE_JOB_MESSAGE;
	public static String IS_PARAMETER;
	public static String CLASS_NOT_EXIST;
	public static String CHECKBOX_DISABLE_MESSAGE;
	public static String ERROR;
	public static String PARAMETER_ERROR;
	public static String ATTACH_LISTENER_ERROR;
	
	public static String FIELD_MAPPING;
	public static String OPERATION_CLASS;
	public static String OUTPUT_FIELD;
	public static String INVALID_INPUT_FIELDS_OPERATION_CLASS;
	public static String BLANK_OUTPUT_FIELD;
	public static String INVALID_INPUT_FIELD;
	public static String DUPLICATE_FIELDS;
	public static String DUPLICATE_OUTPUT;
	public static String LINE_SEPARATOR_KEY;
	public static String NUMBER_INPUT_AND_NUMBER_OF_OUTPUT_FIELD_VALIDATION_MESSAGE;
	public static String ALPHA_NUMRIC_REGULAR_EXPRESSION;
	public static String TEXT_FIELD_SHOULD_MATCH;
	public static String BROWSE_BUTTON_TEXT;

	public static String EmptyFieldNameNotification;
	public static String EmptySourceFieldNotification;
	public static String EmptyOutputFieldNotification;
	public static String SourceFieldAlreadyExists;
	public static String OutputFieldAlreadyExists;
	public static String FieldNameAlreadyExists;
	public static String ABSOLUTE_PATH_TEXT;


	public static String OPERATION_ID_PREFIX;
    public static String OPERATION_LIST_EMPTY;
    public static String PULL_BUTTON_LABEL;
    public static String OPERATION_CONTROL;
    public static String SOURCE;
    public static String TARGET;
    public static String MAP_FIELD;
    public static String OPERATION_ID;
    public static String OP_CLASS;
    public static String PARAMETER_LABEL;
    public static String ALL_DATA_WILL_BE_LOST_DO_YOU_WISH_TO_CONTINUE;
    public static String IS_PARAM;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
