//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.21 at 07:24:07 PM IST 
//


package com.bitwiseglobal.graph.commontypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.igr.TypeGenerateRecordOutSocket;
import com.bitwiseglobal.graph.itfd.TypeInputDelimitedOutSocket;
import com.bitwiseglobal.graph.itffw.TypeInputFixedwidthOutSocket;


/**
 * <p>Java class for type-input-outSocket complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-input-outSocket">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bitwiseglobal.com/graph/commontypes}type-base-outSocket">
 *       &lt;sequence>
 *         &lt;element name="schema" type="{http://www.bitwiseglobal.com/graph/commontypes}type-base-record"/>
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
@XmlType(name = "type-input-outSocket", propOrder = {
    "schema"
})
@XmlSeeAlso({
    TypeInputFixedwidthOutSocket.class,
    TypeInputDelimitedOutSocket.class,
    TypeGenerateRecordOutSocket.class
})
public class TypeInputOutSocket
    extends TypeBaseOutSocket
{

    @XmlElement(required = true)
    protected TypeBaseRecord schema;

    /**
     * Gets the value of the schema property.
     * 
     * @return
     *     possible object is
     *     {@link TypeBaseRecord }
     *     
     */
    public TypeBaseRecord getSchema() {
        return schema;
    }

    /**
     * Sets the value of the schema property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeBaseRecord }
     *     
     */
    public void setSchema(TypeBaseRecord value) {
        this.schema = value;
    }

}