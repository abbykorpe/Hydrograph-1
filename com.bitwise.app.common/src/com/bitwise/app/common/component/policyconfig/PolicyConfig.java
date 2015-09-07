//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.04 at 12:53:22 PM IST 
//


package com.bitwise.app.common.component.policyconfig;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="masterpolicies" type="{http://www.bitwise.com/ComponentConfig}MasterPolicies"/>
 *         &lt;element name="categorypolicies" type="{http://www.bitwise.com/ComponentConfig}CategoryPolicies" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "masterpolicies",
    "categorypolicies"
})
@XmlRootElement(name = "PolicyConfig")
public class PolicyConfig {

    @XmlElement(required = true)
    protected MasterPolicies masterpolicies;
    @XmlElement(required = true)
    protected List<CategoryPolicies> categorypolicies;

    /**
     * Gets the value of the masterpolicies property.
     * 
     * @return
     *     possible object is
     *     {@link MasterPolicies }
     *     
     */
    public MasterPolicies getMasterpolicies() {
        return masterpolicies;
    }

    /**
     * Sets the value of the masterpolicies property.
     * 
     * @param value
     *     allowed object is
     *     {@link MasterPolicies }
     *     
     */
    public void setMasterpolicies(MasterPolicies value) {
        this.masterpolicies = value;
    }

    /**
     * Gets the value of the categorypolicies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the categorypolicies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategorypolicies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CategoryPolicies }
     * 
     * 
     */
    public List<CategoryPolicies> getCategorypolicies() {
        if (categorypolicies == null) {
            categorypolicies = new ArrayList<CategoryPolicies>();
        }
        return this.categorypolicies;
    }

}
