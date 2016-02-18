package com.bitwise.app.common.util;

/**
 * NOTE : Do not change/modify values for below constants(not even space) until you know 
 * where it is affecting the behavior 
 * @author Bitwise
 */
public class Constants {
	public static final String VALIDATOR_PACKAGE_PREFIX = "com.bitwise.app.validators.impl.";
	public static final String COMPONENT_PACKAGE_PREFIX = "com.bitwise.app.graph.model.components.";
	
	public static final String DELIMITER = "Delimiter";
	public static final String CHARACTER_SET = "Character Set";
	public static final String PHASE = "Phase";
	public static final String NO_OF_RECORDS = "No of Records";
	public static final String PARAM_NO_OF_RECORDS = "no_of_records";
	public static final String STRICT = "Strict";
	public static final String HAS_HEADER = "Has Header";
	public static final String SAFE_PROPERTY = "Safe Property ";	
	public static final String INPUT_COUNT = "Input Count";
	
	public static final String PARAM_OPERATION = "operation";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_PHASE = "phase";
	public static final String PARAM_DEPENDS_ON = "dependsOn";
	public static final String PARAM_PROPERTY_NAME = "propertyName";

	public static final String PARAMETER = "Parameter";
	public static final String UTF_16 = "UTF-16";
	public static final String UTF_16LE = "UTF-16LE";
	public static final String IUTF_16BE = "IUTF-16BE";
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String US_ASCII = "US-ASCII";
	public static final String UTF_8 = "UTF-8";
	
	public static final String TRUE = "True";
	public static final String FALSE = "False";
	public static final String OPERATION_FIELD = "Operation Field ";
	public static final String OPERATION_FIELDS = "Operation\nFields";
	public static final String COLUMN_NAME2 = "Key Field ";
	public static final String COLUMN_NAME = "Key\nFields";
	public static final String SECONDARY_KEYS2 = "Secondary Keys ";
	public static final String SECONDARY_KEYS = "Secondary\n Keys";
	
	 // Used for validating AlphaNumeric or Parameter E.g Aplha_123 or @{Param_123}
	public static final String REGEX = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}||[\\w]*";
	// Used for validating only Parameters E.g  @{Param_123}
	public static final String PARAMETER_REGEX = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}"; 
	
	public static final String PROPERTY_COLUMN_NAME = "column_name";
	public static final String PROPERTY_SECONDARY_COLUMN_KEYS = "secondary_column_keys";
	public static final String INNER = "Inner";
	public static final String OUTER = "Outer";
	
	public static final String MATCH_PROPERTY_WIDGET = "match_value";
	public static final String MATCH = "Match";
	public static final String FIRST = "First";
	public static final String LAST = "Last";
	public static final String ALL = "All";
	
	public static final String FILTER = "FILTER";
	public static final String NORMALIZE = "NORMALIZE";
	public static final String TRANSFORM = "TRANSFORM";
	public static final String AGGREGATE = "AGGREGATE";
	public static final String AGGREGATE_DISPLAYNAME = "Aggregate";
	public static final String TRANSFORM_DISPLAYNAME = "Transform";
	public static final String NORMALIZE_DISPLAYNAME = "Normalize";
	public static final String INPUT_SOCKET_TYPE = "in";
	public static final String OUTPUT_SOCKET_TYPE = "out";
	public static final String UNUSED_SOCKET_TYPE = "unused";
	public static final String DUMMY_COMPONENT_CATEGORY = "DUMMY";
	public static final String DUMMY_COMPONENT = "DummyComponent";
	
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
	public static final String SCHEMA_TO_PROPAGATE="output_schema";

	public static final String ASCENDING_SORT_ORDER = "Asc";
	public static final String DESCENDING_SORT_ORDER="Desc";

	//help related constants
	public static final String ABOUT_TEXT="Accelero for ETL Developers\n\n Version: Accelero Service Release 1\n\n (c) Copyright Accelero contributors.  All rights reserved.\n" +
			"Visit http://Accelero.org/";
	public static final String ABOUT_DIALOG_IMAGE_PATH="icons/alt_about.gif";
	public static final String ABOUT_DIALOG_FEATURE_IMAGE_PATH="icons/app_icon.png";
	public static final String ABOUT_DIALOG_IMAGE_BUNDLE_NAME="com.bitwise.app.help";
	public static final String ABOUT_DIALOG_FEATURE_IMAGE_BUNDLE_NAME="com.bitwise.app.perspective";
	public static final String IFDELIMITED="IFDelimited";
	public static final String IFIXEDWIDTH="IFixedWidth";
	/*
	 * Sub graph required constants 
	 */
	public static final String SUBGRAPH_COMPONENT_CATEGORY = "SUBGRAPH";
	public static final String SUBGRAPH_COMPONENT = "SubgraphComponent";
	public static final String PATH = "path";
	public static final String TYPE = "type";
	public static final String OPERATION = "operation";
	public static final String OUTPUT = "output";
	public static final String INPUT = "input";
	public static final String INPUTSUBGRAPH = "inputsubgraph";
	public static final String OUTPUTSUBGRAPH = "outputsubgraph";
	public static final String NAME = "name";
	public static final String SUBGRAPH_NAME = "subgraph.job";
	
	public static final String LENGTH_QNAME = "length";
	public static final String RANGE_FROM_QNAME = "rangeFrom";
	public static final String RANGE_TO_QNAME = "rangeTo";
	public static final String DEFAULT_VALUE_QNAME = "default";
	
	
	public static final String GENERATE_RECORDS_COMPONENT_TYPE = "Generate Records";
	public static final String FIXED_INSOCKET_ID = "in0";
	public static final String SEQUENCE_FIELD = "Sequence Field";
	public static final String IS_PARAMETER = "Is Parameter";
	public static final String SCHEMA_DEFAULT_FIELD_NAME_SUFFIX="DefaultValue";
	public static final String DEFAULT_DATATYPE_INDEX_VALUE="0";
	public static final String PARAMETER_SUFFIX = "@{";
	public static final String UNIQUE_SEQUENCE = "UniqueSequence";
	
	public static final String PARAMETER_PREFIX = "}";
	public static final String UNIQUE_SEQUENCE_PROPERTY_NAME = "sequence_field";
	public static final String ADD_ALL_FIELDS_SYMBOL = "*";
		
}
