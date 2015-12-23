//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.23 at 03:55:32 PM IST 
//


package com.bitwiseglobal.graph.commontypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.clone.TypeCloneOutSocket;
import com.bitwiseglobal.graph.limit.TypeLimitOutSocket;


/**
 * <p>Java class for type-straight-pull-out-socket complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-straight-pull-out-socket">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bitwiseglobal.com/graph/commontypes}type-base-outSocket">
 *       &lt;sequence>
 *         &lt;element name="copyOfInsocket" type="{http://www.bitwiseglobal.com/graph/commontypes}type-outSocket-as-inSocket"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-straight-pull-out-socket", propOrder = {
    "copyOfInsocket"
})
@XmlSeeAlso({
    TypeLimitOutSocket.class,
    com.bitwiseglobal.graph.limit.TypeOutSocket.class,
    TypeCloneOutSocket.class,
    com.bitwiseglobal.graph.removedups.TypeOutSocket.class,
    com.bitwiseglobal.graph.sort.TypeOutSocket.class
})
public class TypeStraightPullOutSocket
    extends TypeBaseOutSocket
{

    @XmlElement(required = true)
    protected TypeOutSocketAsInSocket copyOfInsocket;

    /**
     * Gets the value of the copyOfInsocket property.
     * 
     * @return
     *     possible object is
     *     {@link TypeOutSocketAsInSocket }
     *     
     */
    public TypeOutSocketAsInSocket getCopyOfInsocket() {
        return copyOfInsocket;
    }

    /**
     * Sets the value of the copyOfInsocket property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeOutSocketAsInSocket }
     *     
     */
    public void setCopyOfInsocket(TypeOutSocketAsInSocket value) {
        this.copyOfInsocket = value;
    }

}
