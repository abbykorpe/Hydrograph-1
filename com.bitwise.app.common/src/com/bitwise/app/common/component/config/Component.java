//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.17 at 01:41:18 PM IST 
//


package com.bitwise.app.common.component.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Component complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Component">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="category" type="{http://www.bitwise.com/constant}category_type"/>
 *         &lt;element name="port" type="{http://www.bitwise.com/ComponentConfig}IOPort"/>
 *         &lt;element name="property" type="{http://www.bitwise.com/ComponentConfig}Property" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="policy" type="{http://www.bitwise.com/ComponentConfig}Policy" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="operations" type="{http://www.bitwise.com/ComponentConfig}Operations" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nameInPalette" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="defaultNamePrefix" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="paletteIconPath" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="canvasIconPath" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="helpFilePath" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Component", propOrder = {
    "category",
    "port",
    "property",
    "policy",
    "operations"
})
public class Component {

    @XmlElement(required = true)
    protected CategoryType category;
    @XmlElement(required = true)
    protected IOPort port;
    protected List<Property> property;
    protected List<Policy> policy;
    protected Operations operations;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "nameInPalette", required = true)
    protected String nameInPalette;
    @XmlAttribute(name = "defaultNamePrefix", required = true)
    protected String defaultNamePrefix;
    @XmlAttribute(name = "paletteIconPath", required = true)
    protected String paletteIconPath;
    @XmlAttribute(name = "canvasIconPath", required = true)
    protected String canvasIconPath;
    @XmlAttribute(name = "description", required = true)
    protected String description;
    @XmlAttribute(name = "helpFilePath", required = true)
    protected String helpFilePath;

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link CategoryType }
     *     
     */
    public CategoryType getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link CategoryType }
     *     
     */
    public void setCategory(CategoryType value) {
        this.category = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link IOPort }
     *     
     */
    public IOPort getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link IOPort }
     *     
     */
    public void setPort(IOPort value) {
        this.port = value;
    }

    /**
     * Gets the value of the property property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the property property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Property }
     * 
     * 
     */
    public List<Property> getProperty() {
        if (property == null) {
            property = new ArrayList<Property>();
        }
        return this.property;
    }

    /**
     * Gets the value of the policy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Policy }
     * 
     * 
     */
    public List<Policy> getPolicy() {
        if (policy == null) {
            policy = new ArrayList<Policy>();
        }
        return this.policy;
    }

    /**
     * Gets the value of the operations property.
     * 
     * @return
     *     possible object is
     *     {@link Operations }
     *     
     */
    public Operations getOperations() {
        return operations;
    }

    /**
     * Sets the value of the operations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Operations }
     *     
     */
    public void setOperations(Operations value) {
        this.operations = value;
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

    /**
     * Gets the value of the nameInPalette property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameInPalette() {
        return nameInPalette;
    }

    /**
     * Sets the value of the nameInPalette property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameInPalette(String value) {
        this.nameInPalette = value;
    }

    /**
     * Gets the value of the defaultNamePrefix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultNamePrefix() {
        return defaultNamePrefix;
    }

    /**
     * Sets the value of the defaultNamePrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultNamePrefix(String value) {
        this.defaultNamePrefix = value;
    }

    /**
     * Gets the value of the paletteIconPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaletteIconPath() {
        return paletteIconPath;
    }

    /**
     * Sets the value of the paletteIconPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaletteIconPath(String value) {
        this.paletteIconPath = value;
    }

    /**
     * Gets the value of the canvasIconPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCanvasIconPath() {
        return canvasIconPath;
    }

    /**
     * Sets the value of the canvasIconPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCanvasIconPath(String value) {
        this.canvasIconPath = value;
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
     * Gets the value of the helpFilePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHelpFilePath() {
        return helpFilePath;
    }

    /**
     * Sets the value of the helpFilePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHelpFilePath(String value) {
        this.helpFilePath = value;
    }

}
