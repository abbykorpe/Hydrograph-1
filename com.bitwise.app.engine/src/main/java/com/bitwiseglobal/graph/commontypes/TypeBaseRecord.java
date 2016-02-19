//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.19 at 02:03:47 PM IST 
//


package com.bitwiseglobal.graph.commontypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.igr.TypeGenerateRecordRecord;


/**
 * <p>Java class for type-base-record complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-base-record">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="field" type="{http://www.bitwiseglobal.com/graph/commontypes}type-base-field"/>
 *         &lt;element name="record" type="{http://www.bitwiseglobal.com/graph/commontypes}type-base-record"/>
 *         &lt;element name="includeExternalSchema" type="{http://www.bitwiseglobal.com/graph/commontypes}type-external-schema" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-base-record", propOrder = {
    "fieldOrRecordOrIncludeExternalSchema"
})
@XmlSeeAlso({
    com.bitwiseglobal.graph.itffw.TypeFixedwidthRecord.class,
    TypeGenerateRecordRecord.class,
    com.bitwiseglobal.graph.ifmixedscheme.TypeMixedRecord.class,
    com.bitwiseglobal.graph.otffw.TypeFixedwidthRecord.class,
    com.bitwiseglobal.graph.ofmixedscheme.TypeMixedRecord.class
})
public class TypeBaseRecord {

    @XmlElements({
        @XmlElement(name = "field", type = TypeBaseField.class),
        @XmlElement(name = "record", type = TypeBaseRecord.class),
        @XmlElement(name = "includeExternalSchema", type = TypeExternalSchema.class)
    })
    protected List<Object> fieldOrRecordOrIncludeExternalSchema;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the fieldOrRecordOrIncludeExternalSchema property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fieldOrRecordOrIncludeExternalSchema property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFieldOrRecordOrIncludeExternalSchema().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeBaseField }
     * {@link TypeBaseRecord }
     * {@link TypeExternalSchema }
     * 
     * 
     */
    public List<Object> getFieldOrRecordOrIncludeExternalSchema() {
        if (fieldOrRecordOrIncludeExternalSchema == null) {
            fieldOrRecordOrIncludeExternalSchema = new ArrayList<Object>();
        }
        return this.fieldOrRecordOrIncludeExternalSchema;
    }

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

}
