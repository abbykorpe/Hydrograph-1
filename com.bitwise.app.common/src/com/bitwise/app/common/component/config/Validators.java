//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.20 at 02:25:42 PM IST 
//


package com.bitwise.app.common.component.config;

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
 *     &lt;enumeration value="SCHEMA_GRID_VALIDATOR"/>
 *     &lt;enumeration value="BOOLEAN_OR_PARAMETER_VALIDATOR"/>
 *     &lt;enumeration value="JOIN_CONFIG_VALIDATOR"/>
 *     &lt;enumeration value="JOIN_MAPPING_VALIDATOR"/>
 *     &lt;enumeration value="LOOKUP_CONFIG_VALIDATOR"/>
 *     &lt;enumeration value="LOOKUP_MAPPING_VALIDATOR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "validators", namespace = "http://www.bitwise.com/constant")
@XmlEnum
public enum Validators {

    @XmlEnumValue("CLASS_NAME_VALIDATOR")
    ClassNameValidatorRule("CLASS_NAME_VALIDATOR"),
    @XmlEnumValue("INTEGER_VALIDATOR")
    IntegerValidatorRule("INTEGER_VALIDATOR"),
    @XmlEnumValue("NON_EMPTY_STRING_VALIDATOR")
    NonEmptyStringValidatorRule("NON_EMPTY_STRING_VALIDATOR"),
    @XmlEnumValue("SCHEMA_GRID_VALIDATOR")
    SchemaGridValidationRule("SCHEMA_GRID_VALIDATOR"),
    @XmlEnumValue("BOOLEAN_OR_PARAMETER_VALIDATOR")
    BooleanOrParameterValidationRule("BOOLEAN_OR_PARAMETER_VALIDATOR"),
    @XmlEnumValue("JOIN_CONFIG_VALIDATOR")
    JoinConfigValidationRule("JOIN_CONFIG_VALIDATOR"),
    @XmlEnumValue("JOIN_MAPPING_VALIDATOR")
    JoinMappingValidationRule("JOIN_MAPPING_VALIDATOR"),
    @XmlEnumValue("LOOKUP_CONFIG_VALIDATOR")
    LookupConfigValidationRule("LOOKUP_CONFIG_VALIDATOR"),
    @XmlEnumValue("LOOKUP_MAPPING_VALIDATOR")
    LookupMappingValidationRule("LOOKUP_MAPPING_VALIDATOR");
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
