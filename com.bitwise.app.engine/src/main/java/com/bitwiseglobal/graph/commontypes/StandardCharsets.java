//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.21 at 07:24:07 PM IST 
//


package com.bitwiseglobal.graph.commontypes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for standard-charsets.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="standard-charsets">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="UTF-8"/>
 *     &lt;enumeration value="US-ASCII"/>
 *     &lt;enumeration value="ISO-8859-1"/>
 *     &lt;enumeration value="UTF-16BE"/>
 *     &lt;enumeration value="UTF-16LE"/>
 *     &lt;enumeration value="UTF-16"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "standard-charsets")
@XmlEnum
public enum StandardCharsets {

    @XmlEnumValue("UTF-8")
    UTF_8("UTF-8"),
    @XmlEnumValue("US-ASCII")
    US_ASCII("US-ASCII"),
    @XmlEnumValue("ISO-8859-1")
    ISO_8859_1("ISO-8859-1"),
    @XmlEnumValue("UTF-16BE")
    UTF_16_BE("UTF-16BE"),
    @XmlEnumValue("UTF-16LE")
    UTF_16_LE("UTF-16LE"),
    @XmlEnumValue("UTF-16")
    UTF_16("UTF-16");
    private final String value;

    StandardCharsets(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StandardCharsets fromValue(String v) {
        for (StandardCharsets c: StandardCharsets.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}