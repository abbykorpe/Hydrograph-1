//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.25 at 05:30:11 PM IST 
//


package hydrograph.ui.common.component.config;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for group.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="group">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GENERAL"/>
 *     &lt;enumeration value="SCHEMA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "group", namespace = "hydrograph/ui/constant")
@XmlEnum
public enum Group {

    GENERAL,
    SCHEMA;

    public String value() {
        return name();
    }

    public static Group fromValue(String v) {
        return valueOf(v);
    }

}
