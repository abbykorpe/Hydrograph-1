//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.01 at 06:42:06 PM IST 
//


package com.bitwise.app.common.component.config;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for shape.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="shape">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="InputFigure"/>
 *     &lt;enumeration value="OutputFigure"/>
 *     &lt;enumeration value="FilterFigure"/>
 *     &lt;enumeration value="GatherFigure"/>
 *     &lt;enumeration value="ReplicateFigure"/>
 *     &lt;enumeration value="OutputFixedWidthFigure"/>
 *     &lt;enumeration value="InputFWFigure"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "shape")
@XmlEnum
public enum Shape {

    @XmlEnumValue("InputFigure")
    INPUT_FIGURE("InputFigure"),
    @XmlEnumValue("OutputFigure")
    OUTPUT_FIGURE("OutputFigure"),
    @XmlEnumValue("FilterFigure")
    FILTER_FIGURE("FilterFigure"),
    @XmlEnumValue("GatherFigure")
    GATHER_FIGURE("GatherFigure"),
    @XmlEnumValue("ReplicateFigure")
    REPLICATE_FIGURE("ReplicateFigure"),
    @XmlEnumValue("OutputFixedWidthFigure")
    OUTPUT_FIXED_WIDTH_FIGURE("OutputFixedWidthFigure"),
    @XmlEnumValue("InputFWFigure")
    INPUT_FW_FIGURE("InputFWFigure");
    private final String value;

    Shape(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Shape fromValue(String v) {
        for (Shape c: Shape.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
