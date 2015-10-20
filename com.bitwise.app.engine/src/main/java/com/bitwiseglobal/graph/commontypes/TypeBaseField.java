//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 07:12:07 PM IST 
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
import com.bitwiseglobal.graph.igr.TypeGenerateRecordField;
import com.bitwiseglobal.graph.uniquesequence.TypeNameField;


/**
 * <p>Java class for type-base-field complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-base-field">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://www.bitwiseglobal.com/graph/commontypes}grp-attr-base-field"/>
 *       &lt;anyAttribute/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-base-field")
@XmlSeeAlso({
    com.bitwiseglobal.graph.itffw.TypeFixedwidthField.class,
    TypeGenerateRecordField.class,
    com.bitwiseglobal.graph.ifmixedscheme.TypeMixedField.class,
    com.bitwiseglobal.graph.otffw.TypeFixedwidthField.class,
    com.bitwiseglobal.graph.ofmixedscheme.TypeMixedField.class,
    TypeNameField.class
})
public class TypeBaseField {

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "type", required = true)
    protected FieldDataTypes type;
    @XmlAttribute(name = "format")
    protected String format;
    @XmlAttribute(name = "scale")
    protected Integer scale;
    @XmlAttribute(name = "scale_type")
    protected ScaleTypeList scaleType;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link FieldDataTypes }
     *     
     */
    public FieldDataTypes getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldDataTypes }
     *     
     */
    public void setType(FieldDataTypes value) {
        this.type = value;
    }

    /**
     * Gets the value of the format property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormat(String value) {
        this.format = value;
    }

    /**
     * Gets the value of the scale property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getScale() {
        return scale;
    }

    /**
     * Sets the value of the scale property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setScale(Integer value) {
        this.scale = value;
    }

    /**
     * Gets the value of the scaleType property.
     * 
     * @return
     *     possible object is
     *     {@link ScaleTypeList }
     *     
     */
    public ScaleTypeList getScaleType() {
        return scaleType;
    }

    /**
     * Sets the value of the scaleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScaleTypeList }
     *     
     */
    public void setScaleType(ScaleTypeList value) {
        this.scaleType = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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
