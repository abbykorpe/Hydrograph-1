//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.27 at 03:28:57 PM IST 
//


package hydrograph.ui.common.component.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PortInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PortInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="sequenceOfPort" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="typeOfPort" use="required" type="{hydrograph/ui/constant}PortType" />
 *       &lt;attribute name="nameOfPort" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="labelOfPort" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortInfo")
public class PortInfo {

    @XmlAttribute(name = "sequenceOfPort", required = true)
    protected int sequenceOfPort;
    @XmlAttribute(name = "typeOfPort", required = true)
    protected PortType typeOfPort;
    @XmlAttribute(name = "nameOfPort", required = true)
    protected String nameOfPort;
    @XmlAttribute(name = "labelOfPort", required = true)
    protected String labelOfPort;

    /**
     * Gets the value of the sequenceOfPort property.
     * 
     */
    public int getSequenceOfPort() {
        return sequenceOfPort;
    }

    /**
     * Sets the value of the sequenceOfPort property.
     * 
     */
    public void setSequenceOfPort(int value) {
        this.sequenceOfPort = value;
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
     * Gets the value of the nameOfPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOfPort() {
        return nameOfPort;
    }

    /**
     * Sets the value of the nameOfPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOfPort(String value) {
        this.nameOfPort = value;
    }

    /**
     * Gets the value of the labelOfPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelOfPort() {
        return labelOfPort;
    }

    /**
     * Sets the value of the labelOfPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelOfPort(String value) {
        this.labelOfPort = value;
    }

}
