//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.27 at 08:32:57 PM IST 
//


package com.bitwise.app.common.component.config;

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
 *     &lt;enumeration value="SCHEMA_WIDGET"/>
 *     &lt;enumeration value="FIELD_SEQUENCE_WIDGET"/>
 *     &lt;enumeration value="FIXED_WIDGET"/>
 *     &lt;enumeration value="RUNTIME_PROPERTIES_WIDGET"/>
 *     &lt;enumeration value="FILE_PATH_WIDGET"/>
 *     &lt;enumeration value="CHARACTER_SET_WIDGET"/>
 *     &lt;enumeration value="DELIMETER_WIDGET"/>
 *     &lt;enumeration value="PHASE_WIDGET"/>
 *     &lt;enumeration value="HAS_HEADER_WIDGET"/>
 *     &lt;enumeration value="SAFE_PROPERTY_WIDGET"/>
 *     &lt;enumeration value="COMPONENT_NAME_WIDGET"/>
 *     &lt;enumeration value="FILTER_PROPERTY_WIDGET"/>
 *     &lt;enumeration value="OPERATIONAL_CLASS_WIDGET"/>
 *     &lt;enumeration value="STRICT_CLASS_WIDGET"/>
 *     &lt;enumeration value="RETENTION_LOGIC_WIDGET"/>
 *     &lt;enumeration value="COLUMN_NAME_WIDGET"/>
 *     &lt;enumeration value="CUSTOM_WINDOW_ON_BUTTON_WIDGET"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "property_renderer")
@XmlEnum
public enum PropertyRenderer {

    SCHEMA_WIDGET,
    FIELD_SEQUENCE_WIDGET,
    FIXED_WIDGET,
    RUNTIME_PROPERTIES_WIDGET,
    FILE_PATH_WIDGET,
    CHARACTER_SET_WIDGET,
    DELIMETER_WIDGET,
    PHASE_WIDGET,
    HAS_HEADER_WIDGET,
    SAFE_PROPERTY_WIDGET,
    COMPONENT_NAME_WIDGET,
    FILTER_PROPERTY_WIDGET,
    OPERATIONAL_CLASS_WIDGET,
    STRICT_CLASS_WIDGET,
    RETENTION_LOGIC_WIDGET,
    COLUMN_NAME_WIDGET,
    CUSTOM_WINDOW_ON_BUTTON_WIDGET;

    public String value() {
        return name();
    }

    public static PropertyRenderer fromValue(String v) {
        return valueOf(v);
    }

}
