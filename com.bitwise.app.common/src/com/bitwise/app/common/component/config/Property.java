//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.05 at 12:58:41 PM IST 
//


package com.bitwise.app.common.component.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Property complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Property">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="validation" type="{http://www.bitwise.com/ComponentConfig}Validation" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" use="required" type="{http://www.bitwise.com/ComponentConfig}property_type" />
 *       &lt;attribute name="renderer" use="required" type="{http://www.bitwise.com/ComponentConfig}property_renderer" />
 *       &lt;attribute name="dataType" use="required" type="{http://www.bitwise.com/ComponentConfig}data_type" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="group" use="required" type="{http://www.bitwise.com/ComponentConfig}group" />
 *       &lt;attribute name="subGroup" use="required" type="{http://www.bitwise.com/ComponentConfig}sub_group" />
 *       &lt;attribute name="showAsTooltip" use="required" type="{http://www.bitwise.com/ComponentConfig}showAsTooltip" />
 *       &lt;attribute name="tooltipDataType" use="required" type="{http://www.bitwise.com/ComponentConfig}tooltipDataType" />
 *       &lt;attribute name="usage" type="{http://www.bitwise.com/ComponentConfig}usage" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Property", propOrder = {
    "validation"
})
public class Property {

    protected List<Validation> validation;
    @XmlAttribute(name = "type", required = true)
    protected PropertyType type;
    @XmlAttribute(name = "renderer", required = true)
    protected PropertyRenderer renderer;
    @XmlAttribute(name = "dataType", required = true)
    protected DataType dataType;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "value")
    protected String value;
    @XmlAttribute(name = "group", required = true)
    protected Group group;
    @XmlAttribute(name = "subGroup", required = true)
    protected SubGroup subGroup;
    @XmlAttribute(name = "showAsTooltip", required = true)
    protected ShowAsTooltip showAsTooltip;
    @XmlAttribute(name = "tooltipDataType", required = true)
    protected TooltipDataType tooltipDataType;
    @XmlAttribute(name = "usage")
    protected Usage usage;

    /**
     * Gets the value of the validation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the validation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValidation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Validation }
     * 
     * 
     */
    public List<Validation> getValidation() {
        if (validation == null) {
            validation = new ArrayList<Validation>();
        }
        return this.validation;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link PropertyType }
     *     
     */
    public PropertyType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertyType }
     *     
     */
    public void setType(PropertyType value) {
        this.type = value;
    }

    /**
     * Gets the value of the renderer property.
     * 
     * @return
     *     possible object is
     *     {@link PropertyRenderer }
     *     
     */
    public PropertyRenderer getRenderer() {
        return renderer;
    }

    /**
     * Sets the value of the renderer property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertyRenderer }
     *     
     */
    public void setRenderer(PropertyRenderer value) {
        this.renderer = value;
    }

    /**
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link DataType }
     *     
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataType }
     *     
     */
    public void setDataType(DataType value) {
        this.dataType = value;
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
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link Group }
     *     
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Sets the value of the group property.
     * 
     * @param value
     *     allowed object is
     *     {@link Group }
     *     
     */
    public void setGroup(Group value) {
        this.group = value;
    }

    /**
     * Gets the value of the subGroup property.
     * 
     * @return
     *     possible object is
     *     {@link SubGroup }
     *     
     */
    public SubGroup getSubGroup() {
        return subGroup;
    }

    /**
     * Sets the value of the subGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubGroup }
     *     
     */
    public void setSubGroup(SubGroup value) {
        this.subGroup = value;
    }

    /**
     * Gets the value of the showAsTooltip property.
     * 
     * @return
     *     possible object is
     *     {@link ShowAsTooltip }
     *     
     */
    public ShowAsTooltip getShowAsTooltip() {
        return showAsTooltip;
    }

    /**
     * Sets the value of the showAsTooltip property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowAsTooltip }
     *     
     */
    public void setShowAsTooltip(ShowAsTooltip value) {
        this.showAsTooltip = value;
    }

    /**
     * Gets the value of the tooltipDataType property.
     * 
     * @return
     *     possible object is
     *     {@link TooltipDataType }
     *     
     */
    public TooltipDataType getTooltipDataType() {
        return tooltipDataType;
    }

    /**
     * Sets the value of the tooltipDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TooltipDataType }
     *     
     */
    public void setTooltipDataType(TooltipDataType value) {
        this.tooltipDataType = value;
    }

    /**
     * Gets the value of the usage property.
     * 
     * @return
     *     possible object is
     *     {@link Usage }
     *     
     */
    public Usage getUsage() {
        return usage;
    }

    /**
     * Sets the value of the usage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Usage }
     *     
     */
    public void setUsage(Usage value) {
        this.usage = value;
    }

}
