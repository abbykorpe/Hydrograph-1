//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.07 at 03:05:21 PM IST 
//


package com.bitwiseglobal.graph.join;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for join_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="join_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="inner"/>
 *     &lt;enumeration value="outer"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "join_type")
@XmlEnum
public enum JoinType {

    @XmlEnumValue("inner")
    INNER("inner"),
    @XmlEnumValue("outer")
    OUTER("outer");
    private final String value;

    JoinType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static JoinType fromValue(String v) {
        for (JoinType c: JoinType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
