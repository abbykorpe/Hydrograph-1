//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.28 at 05:29:11 PM IST 
//


package com.bitwiseglobal.graph.commontypes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for type-lookup-insocket.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="type-lookup-insocket">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="driver"/>
 *     &lt;enumeration value="lookup"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "type-lookup-insocket")
@XmlEnum
public enum TypeLookupInsocket {

    @XmlEnumValue("driver")
    DRIVER("driver"),
    @XmlEnumValue("lookup")
    LOOKUP("lookup");
    private final String value;

    TypeLookupInsocket(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TypeLookupInsocket fromValue(String v) {
        for (TypeLookupInsocket c: TypeLookupInsocket.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
