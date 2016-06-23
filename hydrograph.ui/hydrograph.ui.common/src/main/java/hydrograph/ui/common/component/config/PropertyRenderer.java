/*******************************************************************************
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
 *******************************************************************************/
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.17 at 01:02:05 PM IST 
//


package hydrograph.ui.common.component.config;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for property_renderer.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="property_renderer">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GENERATE_RECORDS_SCHEMA_WIDGET"/>
 *     &lt;enumeration value="TRANSFORM_SCHEMA_WIDGET"/>
 *     &lt;enumeration value="SCHEMA_WIDGET"/>
 *     &lt;enumeration value="FIELD_SEQUENCE_WIDGET"/>
 *     &lt;enumeration value="FIXED_WIDGET"/>
 *     &lt;enumeration value="RUNTIME_PROPERTIES_WIDGET"/>
 *     &lt;enumeration value="FILE_PATH_WIDGET"/>
 *     &lt;enumeration value="BROWSE_WORKSPACE_WIDGET"/>
 *     &lt;enumeration value="CHARACTER_SET_WIDGET"/>
 *     &lt;enumeration value="DELIMETER_WIDGET"/>
 *     &lt;enumeration value="PHASE_WIDGET"/>
 *     &lt;enumeration value="NO_OF_RECORDS_WIDGET"/>
 *     &lt;enumeration value="HAS_HEADER_WIDGET"/>
 *     &lt;enumeration value="SAFE_PROPERTY_WIDGET"/>
 *     &lt;enumeration value="COMPONENT_NAME_WIDGET"/>
 *     &lt;enumeration value="FILTER_PROPERTY_WIDGET"/>
 *     &lt;enumeration value="OPERATIONAL_CLASS_WIDGET"/>
 *     &lt;enumeration value="STRICT_CLASS_WIDGET"/>
 *     &lt;enumeration value="RETENTION_LOGIC_WIDGET"/>
 *     &lt;enumeration value="COLUMN_NAME_WIDGET"/>
 *     &lt;enumeration value="SECONDARY_COLUMN_KEYS_WIDGET"/>
 *     &lt;enumeration value="SECONDARY_KEYS_WIDGET"/>
 *     &lt;enumeration value="TRANSFORM_WIDGET"/>
 *     &lt;enumeration value="NORMALIZE_WIDGET"/>
 *     &lt;enumeration value="AGGREGATE_WIDGET"/>
 *     &lt;enumeration value="CUMULATE_WIDGET"/>
 *     &lt;enumeration value="JOIN_INPUT_COUNT_WIDGET"/>
 *     &lt;enumeration value="INPUT_COUNT_WIDGET"/>
 *     &lt;enumeration value="OUTPUT_COUNT_WIDGET"/>
 *     &lt;enumeration value="JOIN_TYPE_WIDGET"/>
 *     &lt;enumeration value="JOIN_MAPPING_WIDGET"/>
 *     &lt;enumeration value="HASH_JOIN_WIDGET"/>
 *     &lt;enumeration value="HASH_JOIN_MAPPING_WIDGET"/>
 *     &lt;enumeration value="XML_CONTENT_WIDGET"/>
 *     &lt;enumeration value="SUBJOB_PROPERTIES_WIDGET"/>
 *     &lt;enumeration value="MATCH_PROPERTY_WIDGET"/>
 *     &lt;enumeration value="TEXTBOX_WITH_IS_PARAMETER_CHECKBOX_WIDGET"/>
 *     &lt;enumeration value="COUNT_WIDGET"/>
 *     &lt;enumeration value="PRIMARY_COLUMN_KEYS_WIDGET"/>
 *     &lt;enumeration value="PRIMARY_COLUMN_KEYS_WIDGET"/>
 *     &lt;enumeration value="DATABASE_NAME_WIDGET"/>
 *     &lt;enumeration value="TABLE_NAME_WIDGET"/>
 *     &lt;enumeration value="PARTITION_KEYS_WIDGET"/>
 *     &lt;enumeration value="QUOTE_WIDGET"/>
 *     &lt;enumeration value="EXTERNAL_TABLE_PATH_WIDGET"/>
 *     &lt;enumeration value="MIXED_SCHEME"/>
 *     &lt;enumeration value="OVERWRITE_WIDGET"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "property_renderer", namespace = "hydrograph/ui/constant")
@XmlEnum
public enum PropertyRenderer {

    GENERATE_RECORDS_SCHEMA_WIDGET,
    TRANSFORM_SCHEMA_WIDGET,
    SCHEMA_WIDGET,
    HIVE_PARQUET_WIDGET,
    FIELD_SEQUENCE_WIDGET,
    FIXED_WIDGET,
    RUNTIME_PROPERTIES_WIDGET,
    FILE_PATH_WIDGET,
    BROWSE_WORKSPACE_WIDGET,
    CHARACTER_SET_WIDGET,
    DELIMETER_WIDGET,
    PHASE_WIDGET,
    NO_OF_RECORDS_WIDGET,
    HAS_HEADER_WIDGET,
    SAFE_PROPERTY_WIDGET,
    COMPONENT_NAME_WIDGET,
    FILTER_PROPERTY_WIDGET,
    OPERATIONAL_CLASS_WIDGET,
    STRICT_CLASS_WIDGET,
    RETENTION_LOGIC_WIDGET,
    COLUMN_NAME_WIDGET,
    SECONDARY_COLUMN_KEYS_WIDGET,
    SECONDARY_KEYS_WIDGET,
    TRANSFORM_WIDGET,
    NORMALIZE_WIDGET,
    AGGREGATE_WIDGET,
    CUMULATE_WIDGET,
    JOIN_INPUT_COUNT_WIDGET,
    INPUT_COUNT_WIDGET,
    OUTPUT_COUNT_WIDGET,
    JOIN_TYPE_WIDGET,
    JOIN_MAPPING_WIDGET,
    HASH_JOIN_WIDGET,
    HASH_JOIN_MAPPING_WIDGET,
    XML_CONTENT_WIDGET,
    SUBJOB_PROPERTIES_WIDGET,
    MATCH_PROPERTY_WIDGET,
    TEXTBOX_WITH_IS_PARAMETER_CHECKBOX_WIDGET,
    COUNT_WIDGET,
    PRIMARY_COLUMN_KEYS_WIDGET,
    DATABASE_NAME_WIDGET,
    TABLE_NAME_WIDGET,
    PARTITION_KEYS_WIDGET,
    QUOTE_WIDGET,
    EXTERNAL_TABLE_PATH_WIDGET,
    MIXED_SCHEME,
    OVERWRITE_WIDGET;

    public String value() {
        return name();
    }

    public static PropertyRenderer fromValue(String v) {
        return valueOf(v);
    }

}
