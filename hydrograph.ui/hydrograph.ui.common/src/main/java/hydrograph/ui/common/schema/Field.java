/*******************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.03 at 11:14:26 AM IST 
//


package hydrograph.ui.common.schema;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Field complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Field">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" use="required" type="{hydrograph/ui/graph/schema}field_data_types" />
 *       &lt;attribute name="type" use="required" type="{hydrograph/ui/graph/schema}string" />
 *       &lt;attribute name="format" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="precision" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="scale" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="scaleType" type="{hydrograph/ui/graph/schema}scale_types" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="length" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="delimiter" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rangeTo" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rangeFrom" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="default" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Field")
public class Field {

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "type", required = true)
    protected FieldDataTypes type;
    @XmlAttribute(name = "absoluteOrRelativeXpath")
    protected String absoluteOrRelativeXpath;
    @XmlAttribute(name = "format")
    protected String format;
    @XmlAttribute(name = "precision")
    protected Integer precision;
    @XmlAttribute(name = "scale")
    protected Integer scale;
    @XmlAttribute(name = "scaleType")
    protected ScaleTypes scaleType;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "length")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger length;
    @XmlAttribute(name = "delimiter")
    protected String delimiter;
    @XmlAttribute(name = "rangeTo")
    protected String rangeTo;
    @XmlAttribute(name = "rangeFrom")
    protected String rangeFrom;
    @XmlAttribute(name = "default")
    protected String _default;

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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbsoluteOrRelativeXpath() {
		return absoluteOrRelativeXpath;
	}
    
    /**
     * Sets the value of the type property.
     * 
     * @param absoluteOrRelativeXpath
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbsoluteOrRelativeXpath(String absoluteOrRelativeXpath) {
		this.absoluteOrRelativeXpath = absoluteOrRelativeXpath;
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
     * Gets the value of the precision property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPrecision() {
        return precision;
    }

    /**
     * Sets the value of the precision property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPrecision(Integer value) {
        this.precision = value;
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
     *     {@link ScaleTypes }
     *     
     */
    public ScaleTypes getScaleType() {
        return scaleType;
    }

    /**
     * Sets the value of the scaleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScaleTypes }
     *     
     */
    public void setScaleType(ScaleTypes value) {
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
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLength(BigInteger value) {
        this.length = value;
    }

    /**
     * Gets the value of the delimiter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Sets the value of the delimiter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelimiter(String value) {
        this.delimiter = value;
    }

    /**
     * Gets the value of the rangeTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRangeTo() {
        return rangeTo;
    }

    /**
     * Sets the value of the rangeTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRangeTo(String value) {
        this.rangeTo = value;
    }

    /**
     * Gets the value of the rangeFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRangeFrom() {
        return rangeFrom;
    }

    /**
     * Sets the value of the rangeFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRangeFrom(String value) {
        this.rangeFrom = value;
    }

    /**
     * Gets the value of the default property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefault(String value) {
        this._default = value;
    }

}
