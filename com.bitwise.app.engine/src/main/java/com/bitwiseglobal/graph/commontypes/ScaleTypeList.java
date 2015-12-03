//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.02 at 04:38:30 PM IST 
//


package com.bitwiseglobal.graph.commontypes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for scale-type-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="scale-type-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="explicit"/>
 *     &lt;enumeration value="implicit"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "scale-type-list")
@XmlEnum
public enum ScaleTypeList {

    @XmlEnumValue("explicit")
    EXPLICIT("explicit"),
    @XmlEnumValue("implicit")
    IMPLICIT("implicit");
    private final String value;

    ScaleTypeList(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ScaleTypeList fromValue(String v) {
        for (ScaleTypeList c: ScaleTypeList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
