//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.03 at 12:05:43 PM IST 
//


package com.bitwiseglobal.graph.commontypes;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import com.bitwiseglobal.graph.clone.TypeCloneInSocket;
import com.bitwiseglobal.graph.filter.TypeFilterInSocket;
import com.bitwiseglobal.graph.limit.TypeLimitInSocket;
import com.bitwiseglobal.graph.partitionbyexpression.TypePbeInSocket;
import com.bitwiseglobal.graph.transform.TypeTransformInSocket;


/**
 * <p>Java class for type-base-inSocket complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-base-inSocket">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://www.bitwiseglobal.com/graph/commontypes}grp-attr-base-inSocket"/>
 *       &lt;anyAttribute/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-base-inSocket")
@XmlSeeAlso({
    TypeBaseInSocketFixedIn0 .class,
    TypeOutputInSocket.class,
    TypeLimitInSocket.class,
    TypeCloneInSocket.class,
    TypeTransformInSocket.class,
    com.bitwiseglobal.graph.aggregate.TypeInSocket.class,
    com.bitwiseglobal.graph.join.TypeInSocket.class,
    TypeFilterInSocket.class,
    com.bitwiseglobal.graph.cumulate.TypeInSocket.class,
    com.bitwiseglobal.graph.lookup.TypeInSocket.class,
    com.bitwiseglobal.graph.normalize.TypeInSocket.class,
    com.bitwiseglobal.graph.subgraph.TypeInSocket.class,
    com.bitwiseglobal.graph.hashjoin.TypeInSocket.class,
    TypePbeInSocket.class
})
public class TypeBaseInSocket {

    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "fromComponentId", required = true)
    protected String fromComponentId;
    @XmlAttribute(name = "fromSocketId", required = true)
    protected String fromSocketId;
    @XmlAttribute(name = "fromSocketType")
    protected String fromSocketType;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the fromComponentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromComponentId() {
        return fromComponentId;
    }

    /**
     * Sets the value of the fromComponentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromComponentId(String value) {
        this.fromComponentId = value;
    }

    /**
     * Gets the value of the fromSocketId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromSocketId() {
        return fromSocketId;
    }

    /**
     * Sets the value of the fromSocketId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromSocketId(String value) {
        this.fromSocketId = value;
    }

    /**
     * Gets the value of the fromSocketType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromSocketType() {
        return fromSocketType;
    }

    /**
     * Sets the value of the fromSocketType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromSocketType(String value) {
        this.fromSocketType = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
