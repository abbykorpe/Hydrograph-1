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

package hydrograph.ui.common.util;

/**
 * NOTE : Do not change/modify values for below constants(not even space) until you know 
 * where it is affecting the behavior 
 * @author Bitwise
 */
public class Constants {
	public static final String VALIDATOR_PACKAGE_PREFIX = "hydrograph.ui.validators.impl.";
	public static final String COMPONENT_PACKAGE_PREFIX = "hydrograph.ui.graph.model.components.";
	
	
	public static final String ERROR = "ERROR";
	
	public static final String BATCH = "Batch";
	public static final String PARAM_NO_OF_RECORDS = "no_of_records";
	
	public static final String INPUT_PORT_COUNT_PROPERTY="inPortCount";
 	public static final String OUTPUT_PORT_COUNT_PROPERTY="outPortCount";
	
	public static final String PARAM_OPERATION = "operation";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_BATCH = "batch";
	public static final String PARAM_DEPENDS_ON = "dependsOn";
	public static final String PARAM_PROPERTY_NAME = "propertyName";
	public static final String PARAM_COUNT = "count";
	
	
	public static final String PARAMETER = "Parameter";
	public static final String UTF_16 = "UTF-16";
	public static final String UTF_16LE = "UTF-16LE";
	public static final String IUTF_16BE = "IUTF-16BE";
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String US_ASCII = "US-ASCII";
	public static final String UTF_8 = "UTF-8";
	
	public static final String TRUE = "True";
	public static final String FALSE = "False";
	public static final String OPERATION_FIELDS_WINDOW_TITLE = "Operation Fields";
	public static final String KEY_FIELDS_WINDOW_TITLE = "Key Fields";
	
	public static final String PARTITION_KEYS_WINDOW_TITLE = "Partition Keys";
	
	public static final String COMPONENT_NAME = "Component Name";
	 // Used for validating AlphaNumeric or Parameter E.g Aplha_123 or @{Param_123}
	public static final String REGEX = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}||[\\w]*";
	// Used for validating only Parameters E.g  @{Param_123}
	public static final String PARAMETER_REGEX = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}"; 
	

	public static final String MATCH_PROPERTY_WIDGET = "match_value";
	public static final String MATCH = "Match";
	public static final String FIRST = "First";
	public static final String LAST = "Last";
	public static final String ALL = "All";
	
	public static final String OFIXED_WIDTH = "OFixedWidth";
	public static final String FILTER = "FILTER";
	public static final String NORMALIZE = "NORMALIZE";
	public static final String TRANSFORM = "TRANSFORM";
	public static final String AGGREGATE = "AGGREGATE";
	public static final String CUMULATE = "CUMULATE";
	public static final String LOOKUP = "LOOKUP";
	public static final String JOIN = "JOIN";
	public static final String AGGREGATE_DISPLAYNAME = "Aggregate";
	public static final String TRANSFORM_DISPLAYNAME = "Transform";
	public static final String CUMULATE_DISPLAYNAME = "Cumulate";
	public static final String NORMALIZE_DISPLAYNAME = "Normalize";

	public static final String AGGREGATE_WINDOW_TITLE = "Aggregate";
	public static final String TRANSFORM_WINDOW_TITLE = "Transform";
	public static final String CUMULATE_WINDOW_TITLE= "Cumulate";
	public static final String NORMALIZE_WINDOW_TITLE = "Normalize";
	
	public static final String INPUT_SOCKET_TYPE = "in";
	public static final String OUTPUT_SOCKET_TYPE = "out";
	public static final String UNUSED_SOCKET_TYPE = "unused";
	public static final String UNKNOWN_COMPONENT_CATEGORY = "UNKNOWN";
	public static final String UNKNOWN_COMPONENT = "UnknownComponent";
	
	public static final String LOOKUP_CONFIG_FIELD = "hash_join";
	public static final String LOOKUP_MAP_FIELD = "hash_join_map";
	
	public static final String JOIN_CONFIG_FIELD = "join_config";
	public static final String JOIN_MAP_FIELD = "join_mapping";
	public static final String UNKNOWN_COMPONENT_TYPE = "UNKNOWN";
	public static final String JOIN_TYPE_ATTRIBUTE_NAME="joinType";
	
	public static final String COMPONENT_ORIGINAL_NAME = "componentOriginalName";
	public static final String COMPONENT_TYPE = "componentType";
	public static final String COMPONENT_BASE_TYPE = "componentBaseType";
	public static final String COMPONENT_NAMES = "componentNames";
	public static final String SCHEMA_TO_PROPAGATE="output_schema_map";

	public static final String ASCENDING_SORT_ORDER = "Asc";
	public static final String DESCENDING_SORT_ORDER="Desc";
	public static final String NONE_SORT_ORDER="From Param";


	/*
	 * Sub Job required constants 
	 */
	public static final String SUBJOB_COMPONENT_CATEGORY = "SUBJOB";
	public static final String SUBJOB_COMPONENT = "SubjobComponent";
	public static final String PATH = "path";
	public static final String TYPE = "type";
	public static final String OPERATION = "operation";
	public static final String OUTPUT = "output";
	public static final String INPUT = "input";
	public static final String INPUT_SUBJOB = "InputSubjobComponent";
 	public static final String OUTPUT_SUBJOB = "OutputSubjobComponent";
	public static final String NAME = "name";
	public static final String SUBJOB_NAME = "subjob.job";
	
	public static final String LENGTH_QNAME = "length";
	public static final String DELIMITER_QNAME = "delimiter";	
	public static final String RANGE_FROM_QNAME = "rangeFrom";
	public static final String RANGE_TO_QNAME = "rangeTo";
	public static final String DEFAULT_VALUE_QNAME = "default";
	
	
	public static final String GENERATE_RECORDS_COMPONENT_TYPE = "Generate Records";
	public static final String FIXED_INSOCKET_ID = "in0";
	public static final String FIXED_OUTSOCKET_ID = "out0";
	public static final String SEQUENCE_FIELD = "Sequence Field";
	public static final String IS_PARAMETER = "Is Parameter";
	public static final String SCHEMA_DEFAULT_FIELD_NAME_SUFFIX="DefaultField";
	public static final String DEFAULT_INDEX_VALUE_FOR_COMBOBOX="0";
	public static final String PARAMETER_PREFIX = "@{";
	public static final String UNIQUE_SEQUENCE = "UniqueSequence";
	public static final String UNIQUE_SEQUENCE_TYPE = "Unique Sequence";
	
	public static final String PARAMETER_SUFFIX = "}";
	public static final String UNIQUE_SEQUENCE_PROPERTY_NAME = "sequence_field";
	public static final String ADD_ALL_FIELDS_SYMBOL = "*";
	public static final String INPUT_SUBJOB_COMPONENT_NAME = "InputSubjobComponent";
	
	public static final String EDIT = "Edit";
	public static final String RUNTIME_PROPERTIES_COLUMN_NAME = "Runtime\nProperties";
	public static final String RUNTIME_PROPERTIES_WINDOW_LABEL = "Runtime Properties";
	public static final String RUNTIME_PROPERTY_NAME = "runtime_properties";
	
	public static final String RUNTIME_PROPERTY_LABEL = "Runtime\nProperties";
	public static final String SUBJOB_PROPERTY_LABEL = "Subjob\nProperties";
	public static final String UNUSED_PORT_COUNT_PROPERTY = "unusedPortCount";
	public static final String UNUSED_AND_INPUT_PORT_COUNT_PROPERTY = "inPortCount|unusedPortCount";
	public static final String SUBJOB_ALREADY_PRESENT_IN_CANVAS = " - already present in canvas.";
	public static final String PATH_PROPERTY_NAME = "path";
	public static final String SUBJOB_CREATE = "Create";
	public static final String SUBJOB_OPEN = "Open"; 	
	public static final String SUBJOB_TRACKING = "View Tracking"; 
	public static final String SUBJOB_ACTION = "SubJob";
	public static final String SUBJOB_ACTION_ToolTip = "Path operations";
	public static final String STANDALONE_SUBJOB = "StandAlone_Subjob";
	public static final String SUBJOB_WINDOW_LABEL = "Subjob Parameters";
	public static final String WINDOW_TITLE="WINDOW TITLE";
	public static final String JOB_PATH="path";
	public static final String JOB_EXTENSION=".job";
	public static final String XML_EXTENSION=".xml";
	public static final String JOB_EXTENSION_FOR_IPATH="job";
	public static final String XML_EXTENSION_FOR_IPATH="xml";
	public static final String SUBJOB_UPDATE = "Refresh";
	public static final String JOIN_KEYS_WINDOW_TITLE = "Join Key(s)";
	public static final String LOOKUP_KEYS_WINDOW_TITLE = "Lookup Key(s)";
	
	public static final String INNER = "Inner";
	public static final String OUTER = "Outer";
	public static final String INPUT_SOCKET_FOR_SUBJOB="Input Socket for subjob";
	public static final String OUTPUT_SOCKET_FOR_SUBJOB="Output Socket for subjob";
	public static final String PROPERTIES="properties";
	public static final String SUBJOB_INPUT_COMPONENT_NAME="InSubjob_01";
	public static final String SUBJOB_OUTPUT_COMPONENT_NAME="OutSubjob_01";
	
	/*
	 * Debug required constants 
	 */
	
	public static final String CURRENT_VIEW_DATA_ID = "currentJobViewDataId";
	public static final String PRIOR_VIEW_DATA_ID = "priorViewDataId";
	
	
	
	public static final String WATCHER_ACTION = "Watch Point";
	public static final String WATCHER_ACTION_TEXT = "Add watch";
	public static final String ADD_WATCH_POINT_ID = "watchPointId";
	public static final String WATCH_RECORD_ID = "watchRecordId";
	public static final String REMOVE_WATCH_POINT_ID = "removeWatchPointId";
	public static final String SOCKET_ID = "socketId";
	public static final String JOB_ID = "jobId";
	public static final String PORT_NO = ":8004";
	public static final String ROUTE_TO_READ_DATA = "/read";
	public static final String ROUTE_TO_REMOVE_FILES = "/delete";
	
	public static final String HTTP_PROTOCOL = "http://";
	public static final String DEBUG_EXTENSION = "_debug.xml";
	
	
	public static final String UPDATE_AVAILABLE = "update_available";
	public static final String SUBJOB_VERSION = "subjobVersion";
	public static final String VALIDITY_STATUS = "validityStatus";

	public static final String HELP_ID="helpId";
	public static final String COMPONENT_PROPERTIES_ID="propertiesId";

	// Components Properties Keys
	public static final String PARAM_PRIMARY_COLUMN_KEYS = "Key_fields_sort";
	public static final String PARAM_SECONDARY_COLUMN_KEYS = "Secondary_keys";
	public static final String PROPERTY_COLUMN_NAME = "Key_fields";
	public static final String PROPERTY_SECONDARY_COLUMN_KEYS = "Secondary_keys";
	

	public static final String GRAPH_PROPERTY = "Graph Property";
	public static final String GRAPH_PROPERTY_COMMAND_ID = "hydrograph.ui.propertywindow.graphProperties";
	public static final String SCHEMA_PROPERTY_NAME = "schema";
	
	
	
	//Types of UI- Schema 
	public static final String FIXEDWIDTH_GRID_ROW = "FixedWidth";
	public static final String MIXEDSCHEMA_GRID_ROW = "MixedScheme";		
	public static final String GENERATE_RECORD_GRID_ROW="GenerateRecord";
	public static final String GENERIC_GRID_ROW="Generic";
	public static final String PACKAGE = "package";
	
	
	public static final String EXTERNAL_SCHEMA = "External";
	public static final String INTERNAL_SCHEMA = "Internal";

	public static String SCHEMA_NOT_SYNC_MESSAGE="Fields in schema and mapping are not in sync.";
	public static String SYNC_WARNING="Sync Warning";
	public static String SYNC_CONFIRM="Confirm";
	public static String SYNC_CONFIRM_MESSAGE="Do you want to sync schema? It will override existing .";
	public static String SYNC_OUTPUT_FIELDS_CONFIRM_MESSAGE="Do you want to pull output fields from schema? It will over write existing Output fields.";
	public static String CLICK_TO_FOCUS="Click to focus";

	// Temporary property names of component 
	public static String SCHEMA_FIELD_SEQUENCE = "schema_field_sequence";
	public static String COPY_FROM_INPUT_PORT_PROPERTY = "Copy of ";
	//Hive input partition key
	public static String PARTITION_KEYS="PartitionKeys";

	public static String INPUT_SUBJOB_TYPE="Input Socket for subjob";
	public static String OUTPUT_SUBJOB_TYPE="Output Socket for subjob";

	public static final String PROPERTY_TABLE = "PROPERTY_TABLE";
	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	public static final String PROPERTY_VALUE = "PROPERTY_VALUE";
	public static final Character KEY_D = 'd';
	public static final Character KEY_N = 'n';

	public static final String DATABASE_WIDGET_NAME="databaseName";
	public static final String DBTABLE_WIDGET_NAME="tableName";
	public static final String PARTITION_KEYS_WIDGET_NAME="partitionKeys";
	public static final String EXTERNAL_TABLE_PATH_WIDGET_NAME="externalTablePath";
	public static final String HOST = "host";

}
