//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.22 at 08:00:25 PM IST 
//


package com.bitwise.app.common.component.config;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sub_group.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="sub_group">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DISPLAY"/>
 *     &lt;enumeration value="CONFIGURATION"/>
 *     &lt;enumeration value="RECORD_STRUCTURE_DETAILS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "sub_group", namespace = "http://www.bitwise.com/constant")
@XmlEnum
public enum SubGroup {

    DISPLAY,
    CONFIGURATION,
    RECORD_STRUCTURE_DETAILS;

    public String value() {
        return name();
    }

    public static SubGroup fromValue(String v) {
        return valueOf(v);
    }

}
