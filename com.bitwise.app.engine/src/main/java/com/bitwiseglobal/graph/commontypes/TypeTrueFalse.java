//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.23 at 03:55:32 PM IST 
//


package com.bitwiseglobal.graph.commontypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for type-true-false complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-true-false">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="value" type="{http://www.bitwiseglobal.com/graph/commontypes}true_false" default="true" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-true-false")
public class TypeTrueFalse {

    @XmlAttribute(name = "value")
    protected TrueFalse value;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link TrueFalse }
     *     
     */
    public TrueFalse getValue() {
        if (value == null) {
            return TrueFalse.TRUE;
        } else {
            return value;
        }
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrueFalse }
     *     
     */
    public void setValue(TrueFalse value) {
        this.value = value;
    }

}