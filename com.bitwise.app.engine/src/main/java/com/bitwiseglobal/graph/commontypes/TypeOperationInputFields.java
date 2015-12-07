//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.07 at 04:14:42 PM IST 
//


package com.bitwiseglobal.graph.commontypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.filter.TypeFilterOperationInputFields;
import com.bitwiseglobal.graph.transform.TypeTransformOperationInputFields;


/**
 * <p>Java class for type-operation-input-fields complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-operation-input-fields">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="field" type="{http://www.bitwiseglobal.com/graph/commontypes}type-input-field" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-operation-input-fields", propOrder = {
    "field"
})
@XmlSeeAlso({
    TypeTransformOperationInputFields.class,
    com.bitwiseglobal.graph.aggregate.TypeOperationInputFields.class,
    TypeFilterOperationInputFields.class,
    com.bitwiseglobal.graph.cumulate.TypeOperationInputFields.class,
    com.bitwiseglobal.graph.normalize.TypeOperationInputFields.class
})
public class TypeOperationInputFields {

    @XmlElement(required = true)
    protected List<TypeInputField> field;

    /**
     * Gets the value of the field property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the field property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getField().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeInputField }
     * 
     * 
     */
    public List<TypeInputField> getField() {
        if (field == null) {
            field = new ArrayList<TypeInputField>();
        }
        return this.field;
    }

}
