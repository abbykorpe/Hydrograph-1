//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.07 at 03:05:21 PM IST 
//


package com.bitwiseglobal.graph.cumulate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeFieldName;
import com.bitwiseglobal.graph.commontypes.TypeSortOrder;


/**
 * <p>Java class for type-seconday-key-fields-attributes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-seconday-key-fields-attributes">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bitwiseglobal.com/graph/commontypes}type-field-name">
 *       &lt;attribute name="order" type="{http://www.bitwiseglobal.com/graph/commontypes}type-sort-order" default="asc" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-seconday-key-fields-attributes")
public class TypeSecondayKeyFieldsAttributes
    extends TypeFieldName
{

    @XmlAttribute(name = "order")
    protected TypeSortOrder order;

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link TypeSortOrder }
     *     
     */
    public TypeSortOrder getOrder() {
        if (order == null) {
            return TypeSortOrder.ASC;
        } else {
            return order;
        }
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeSortOrder }
     *     
     */
    public void setOrder(TypeSortOrder value) {
        this.order = value;
    }

}
