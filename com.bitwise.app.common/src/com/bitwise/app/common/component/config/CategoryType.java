//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.18 at 01:08:01 PM IST 
//


package com.bitwise.app.common.component.config;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for category_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="category_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INPUT"/>
 *     &lt;enumeration value="OUTPUT"/>
 *     &lt;enumeration value="TRANSFORM"/>
 *     &lt;enumeration value="STRAIGHTPULL"/>
 *     &lt;enumeration value="DUMMY"/>
 *     &lt;enumeration value="SUBGRAPH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "category_type", namespace = "http://www.bitwise.com/constant")
@XmlEnum
public enum CategoryType {

    INPUT,
    OUTPUT,
    TRANSFORM,
    STRAIGHTPULL,
    DUMMY,
    SUBGRAPH;

    public String value() {
        return name();
    }

    public static CategoryType fromValue(String v) {
        return valueOf(v);
    }

}
