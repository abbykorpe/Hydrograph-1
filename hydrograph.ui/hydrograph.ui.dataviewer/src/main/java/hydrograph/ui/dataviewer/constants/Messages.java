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

/**
 * 
 * Messages class holds constants for messages to show 
 * 
 * @author Bitwise
 *
 */
public class Messages extends NLS{
	private static final String BUNDLE_NAME = "resources.messages";

	
	
	/*view data preferences*/
	public static String MEMORY_FIELD_MESSAGE;
	public static String INTEGER_FIELD_VALIDATION;
	public static String FILE_INTEGER_FIELD_VALIDATION;
	public static String PAGE_INTEGER_FIELD_VALIDATION;
	
	public static String DELIMITER_SINGLE_CHARACTOR_ERROR_MESSAGE;
	public static String QUOTE_SINGLE_CHARACTOR_ERROR_MESSAGE;
	
	public static String File_FIELD_NUMERIC_VALUE_ACCPECTED;
	public static String PAGE_FIELD_NUMERIC_VALUE_ACCPECTED;
	
	public static String PAGE_SIZE_WARNING;
	public static String DELIMITER_WARNING;
	public static String DELIMITER_VALUE_MATCH_ERROR;
	public static String CHARACTER_LENGTH_ERROR;
	public static String QUOTE_WARNING;
	public static String QUOTE_VALUE_MATCH_ERROR;
	public static String UNABLE_TO_RELOAD_DEBUG_FILE;
	public static String UNABLE_TO_LOAD_DEBUG_FILE;
	public static String INVALID_DEBUG_FILE;
	public static String END_OF_FILE;
	public static String BEGINING_OF_FILE;
	public static String ERROR_WHILE_FETCHING_RECORDS;
	public static String JUMP_To_PAGE_OPERATION_NOT_ALLOWED;
	public static String JUMP_PAGE_TEXTBOX_CAN_NOTE_BE_EMPTY;
	public static String FETCHING_PAGE;
	public static String FETCHING_PREVIOUS_PAGE;
	public static String FETCHING_NEXT_PAGE;
	public static String SHOWING_RECORDS_FROM;
	public static String ROW_COUNT;
	public static String FETCHING_TOTAL_NUMBER_OF_RECORDS;

	public static String ERROR_MESSAGE;
	public static String NUMERIC_VALUE_ACCPECTED ;
	public static String FILE_SIZE_BLANK ;
	public static String PAGE_SIZE_BLANK ;
	public static String SINGLE_CHARACTOR_ERROR_MESSAGE ;
	public static String DELIMITER_BLANK;
	public static String QUOTE_CHARACTOR_BLANK;
	public static String WARNING_MESSAGE;
	public static String MEMORY_OVERFLOW_EXCEPTION;
	public static String DUPLICATE_ERROR_MESSAGE;
	public static String DELIMITER_HELP_TEXT;
	public static String QUOTE_CHARACTOR_HELP_TEXT;
	public static String INCLUDE_HEADER_HELP_TEXT;
	public static String FILE_SIZE_HELP_TEXT;
	public static String PAGE_SIZE_HELP_TEXT;

	public static String LOADING_DEBUG_FILE;
	public static String UNABLE_TO_FETCH_DEBUG_FILE;
	public static String UNABLE_TO_READ_DEBUG_FILE;
	public static String EMPTY_DEBUG_FILE;
		
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
