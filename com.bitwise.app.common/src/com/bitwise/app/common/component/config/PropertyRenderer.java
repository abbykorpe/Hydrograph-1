//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.09 at 01:07:50 PM IST 
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
 *     &lt;enumeration value="FILE"/>
 *     &lt;enumeration value="TEXT"/>
 *     &lt;enumeration value="GRID"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "property_renderer")
@XmlEnum
public enum PropertyRenderer {

    FILE,
    TEXT,
    GRID;

    public String value() {
        return name();
    }

    public static PropertyRenderer fromValue(String v) {
        return valueOf(v);
    }

}
