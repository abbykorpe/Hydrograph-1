//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.19 at 02:03:47 PM IST 
//


package com.bitwiseglobal.graph.operationstypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.join.JoinBase;
import com.bitwiseglobal.graph.join.TypeKeyFields;


/**
 * <p>Java class for join complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="join">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bitwiseglobal.com/graph/join}join-base">
 *       &lt;sequence>
 *         &lt;element name="keys" type="{http://www.bitwiseglobal.com/graph/join}type-key-fields" maxOccurs="unbounded" minOccurs="2"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "join", propOrder = {
    "keys"
})
public class Join
    extends JoinBase
{

    @XmlElement(required = true)
    protected List<TypeKeyFields> keys;

    /**
     * Gets the value of the keys property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keys property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeys().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeKeyFields }
     * 
     * 
     */
    public List<TypeKeyFields> getKeys() {
        if (keys == null) {
            keys = new ArrayList<TypeKeyFields>();
        }
        return this.keys;
    }

}
