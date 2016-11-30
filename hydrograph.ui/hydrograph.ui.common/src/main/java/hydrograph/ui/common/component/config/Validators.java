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
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for validators.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="validators">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CLASS_NAME_VALIDATOR"/>
 *     &lt;enumeration value="INTEGER_VALIDATOR"/>
 *     &lt;enumeration value="NON_EMPTY_STRING_VALIDATOR"/>
 *     &lt;enumeration value="NON_BLANK_STRING_VALIDATOR"/>
 *     &lt;enumeration value="SCHEMA_GRID_VALIDATOR"/>
 *     &lt;enumeration value="MIXED_SCHEME_GRID_VALIDATOR"/>
 *     &lt;enumeration value="BOOLEAN_OR_PARAMETER_VALIDATOR"/>
 *     &lt;enumeration value="JOIN_CONFIG_VALIDATOR"/>
 *     &lt;enumeration value="JOIN_MAPPING_VALIDATOR"/>
 *     &lt;enumeration value="LOOKUP_CONFIG_VALIDATOR"/>
 *     &lt;enumeration value="LOOKUP_MAPPING_VALIDATOR"/>
 *     &lt;enumeration value="INTEGER_OR_PARAMETER_VALIDATOR"/>
 *     &lt;enumeration value="TRANSFORM_SCHEMA_GRID_VALIDATOR"/>
 *     &lt;enumeration value="FILE_EXISTS_VALIDATOR"/>
 *     &lt;enumeration value="KEY_FIELDS_VALIDATOR"/>
 *     &lt;enumeration value="SORT_KEY_FIELDS_VALIDATOR"/>
 *     &lt;enumeration value="TRANSFORM_MAPPING_VALIDATOR"/>
 *     &lt;enumeration value="RUNTIME_PROPERTY_VALIDATOR"/>
 *     &lt;enumeration value="HIVE_PARQUET_VALIDATOR"/>
 *     &lt;enumeration value="LONG_VALIDATOR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "validators", namespace = "hydrograph/ui/constant")
@XmlEnum
public enum Validators {

    @XmlEnumValue("CLASS_NAME_VALIDATOR")
    ClassNameValidatorRule("CLASS_NAME_VALIDATOR"),
    @XmlEnumValue("INTEGER_VALIDATOR")
    IntegerValidatorRule("INTEGER_VALIDATOR"),
    @XmlEnumValue("NON_EMPTY_STRING_VALIDATOR")
    NonEmptyStringValidatorRule("NON_EMPTY_STRING_VALIDATOR"),
    @XmlEnumValue("NON_BLANK_STRING_VALIDATOR")
    NonBlankStringValidatorRule("NON_BLANK_STRING_VALIDATOR"),
    @XmlEnumValue("SCHEMA_GRID_VALIDATOR")
    SchemaGridValidationRule("SCHEMA_GRID_VALIDATOR"),
    @XmlEnumValue("MIXED_SCHEME_GRID_VALIDATOR")
    MixedSchemeGridValidationRule("MIXED_SCHEME_GRID_VALIDATOR"),
    @XmlEnumValue("BOOLEAN_OR_PARAMETER_VALIDATOR")
    BooleanOrParameterValidationRule("BOOLEAN_OR_PARAMETER_VALIDATOR"),
    @XmlEnumValue("JOIN_CONFIG_VALIDATOR")
    JoinConfigValidationRule("JOIN_CONFIG_VALIDATOR"),
    @XmlEnumValue("JOIN_MAPPING_VALIDATOR")
    JoinMappingValidationRule("JOIN_MAPPING_VALIDATOR"),
    @XmlEnumValue("LOOKUP_CONFIG_VALIDATOR")
    LookupConfigValidationRule("LOOKUP_CONFIG_VALIDATOR"),
    @XmlEnumValue("LOOKUP_MAPPING_VALIDATOR")
    LookupMappingValidationRule("LOOKUP_MAPPING_VALIDATOR"),
    @XmlEnumValue("INTEGER_OR_PARAMETER_VALIDATOR")
    IntegerOrParameterValidationRule("INTEGER_OR_PARAMETER_VALIDATOR"),
    @XmlEnumValue("TRANSFORM_SCHEMA_GRID_VALIDATOR")
    TransformSchemaGridValidationRule("TRANSFORM_SCHEMA_GRID_VALIDATOR"),
    @XmlEnumValue("FILE_EXISTS_VALIDATOR")
    FileExistsValidatorRule("FILE_EXISTS_VALIDATOR"),
    @XmlEnumValue("KEY_FIELDS_VALIDATOR")
    KeyFieldsValidationRule("KEY_FIELDS_VALIDATOR"),
    @XmlEnumValue("SORT_KEY_FIELDS_VALIDATOR")
    SortComponentKeysFieldsValidationRule("SORT_KEY_FIELDS_VALIDATOR"),
    @XmlEnumValue("TRANSFORM_MAPPING_VALIDATOR")
    TransformMappingValidationRule("TRANSFORM_MAPPING_VALIDATOR"),
    @XmlEnumValue("RUNTIME_PROPERTY_VALIDATOR")
    RuntimePropertyValueValidationRule("RUNTIME_PROPERTY_VALIDATOR"),
    @XmlEnumValue("LONG_VALIDATOR")
    LongValidatorRule("LONG_VALIDATOR"),
    @XmlEnumValue("TOGGlE_SELECTION_VALIDATOR")
    ToggleSelectionValidationRule("TOGGlE_SELECTION_VALIDATOR");

    
    private final String value;

    Validators(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Validators fromValue(String v) {
        for (Validators c: Validators.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
