//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.16 at 05:21:26 PM IST 
//


package com.bitwiseglobal.graph.straightpulltypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.KeepValue;
import com.bitwiseglobal.graph.commontypes.KeyfieldDescriptionType;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.dedup.TypeDedupBase;


/**
 * <p>Java class for dedup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dedup">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bitwiseglobal.com/graph/dedup}type-dedup-base">
 *       &lt;sequence>
 *         &lt;element name="keep" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="value" use="required" type="{http://www.bitwiseglobal.com/graph/commontypes}keep_value" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="key_description" type="{http://www.bitwiseglobal.com/graph/commontypes}keyfield_description_type"/>
 *         &lt;element name="runtime_properties" type="{http://www.bitwiseglobal.com/graph/commontypes}type-properties" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dedup", propOrder = {
    "keep",
    "keyDescription",
    "runtimeProperties"
})
public class Dedup
    extends TypeDedupBase
{

    protected Dedup.Keep keep;
    @XmlElement(name = "key_description", required = true)
    protected KeyfieldDescriptionType keyDescription;
    @XmlElement(name = "runtime_properties")
    protected TypeProperties runtimeProperties;

    /**
     * Gets the value of the keep property.
     * 
     * @return
     *     possible object is
     *     {@link Dedup.Keep }
     *     
     */
    public Dedup.Keep getKeep() {
        return keep;
    }

    /**
     * Sets the value of the keep property.
     * 
     * @param value
     *     allowed object is
     *     {@link Dedup.Keep }
     *     
     */
    public void setKeep(Dedup.Keep value) {
        this.keep = value;
    }

    /**
     * Gets the value of the keyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link KeyfieldDescriptionType }
     *     
     */
    public KeyfieldDescriptionType getKeyDescription() {
        return keyDescription;
    }

    /**
     * Sets the value of the keyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyfieldDescriptionType }
     *     
     */
    public void setKeyDescription(KeyfieldDescriptionType value) {
        this.keyDescription = value;
    }

    /**
     * Gets the value of the runtimeProperties property.
     * 
     * @return
     *     possible object is
     *     {@link TypeProperties }
     *     
     */
    public TypeProperties getRuntimeProperties() {
        return runtimeProperties;
    }

    /**
     * Sets the value of the runtimeProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeProperties }
     *     
     */
    public void setRuntimeProperties(TypeProperties value) {
        this.runtimeProperties = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="value" use="required" type="{http://www.bitwiseglobal.com/graph/commontypes}keep_value" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Keep {

        @XmlAttribute(name = "value", required = true)
        protected KeepValue value;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link KeepValue }
         *     
         */
        public KeepValue getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link KeepValue }
         *     
         */
        public void setValue(KeepValue value) {
            this.value = value;
        }

    }

}