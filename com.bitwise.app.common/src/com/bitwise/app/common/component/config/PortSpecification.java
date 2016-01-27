//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
<<<<<<< HEAD
// Generated on: 2016.01.25 at 01:58:51 PM IST 
=======
// Generated on: 2016.01.20 at 02:25:42 PM IST 
>>>>>>> branch 'feature/subgraph' of git@gitlab:bhs-ui/bhs-ui.git
//


package com.bitwise.app.common.component.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PortSpecification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PortSpecification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="port" type="{http://www.bitwise.com/ComponentConfig}PortInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="typeOfPort" use="required" type="{http://www.bitwise.com/constant}PortType" />
 *       &lt;attribute name="numberOfPorts" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="changePortCountDynamically" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortSpecification", propOrder = {
    "port"
})
public class PortSpecification {

    protected List<PortInfo> port;
    @XmlAttribute(name = "typeOfPort", required = true)
    protected PortType typeOfPort;
    @XmlAttribute(name = "numberOfPorts", required = true)
    protected int numberOfPorts;
    @XmlAttribute(name = "changePortCountDynamically")
    protected Boolean changePortCountDynamically;

    /**
     * Gets the value of the port property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the port property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPort().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PortInfo }
     * 
     * 
     */
    public List<PortInfo> getPort() {
        if (port == null) {
            port = new ArrayList<PortInfo>();
        }
        return this.port;
    }

    /**
     * Gets the value of the typeOfPort property.
     * 
     * @return
     *     possible object is
     *     {@link PortType }
     *     
     */
    public PortType getTypeOfPort() {
        return typeOfPort;
    }

    /**
     * Sets the value of the typeOfPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link PortType }
     *     
     */
    public void setTypeOfPort(PortType value) {
        this.typeOfPort = value;
    }

    /**
     * Gets the value of the numberOfPorts property.
     * 
     */
    public int getNumberOfPorts() {
        return numberOfPorts;
    }

    /**
     * Sets the value of the numberOfPorts property.
     * 
     */
    public void setNumberOfPorts(int value) {
        this.numberOfPorts = value;
    }

    /**
     * Gets the value of the changePortCountDynamically property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isChangePortCountDynamically() {
        if (changePortCountDynamically == null) {
            return false;
        } else {
            return changePortCountDynamically;
        }
    }

    /**
     * Sets the value of the changePortCountDynamically property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setChangePortCountDynamically(Boolean value) {
        this.changePortCountDynamically = value;
    }

}
