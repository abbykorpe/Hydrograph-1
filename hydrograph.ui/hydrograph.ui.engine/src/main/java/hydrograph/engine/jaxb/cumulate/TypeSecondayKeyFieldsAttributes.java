//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.25 at 04:54:03 PM IST 
//


package hydrograph.engine.jaxb.cumulate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;
import hydrograph.engine.jaxb.commontypes.TypeSortOrder;


/**
 * <p>Java class for type-seconday-key-fields-attributes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-seconday-key-fields-attributes">
 *   &lt;complexContent>
 *     &lt;extension base="{hydrograph/engine/jaxb/commontypes}type-field-name">
 *       &lt;attribute name="order" type="{hydrograph/engine/jaxb/commontypes}type-sort-order" default="asc" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-seconday-key-fields-attributes")
public class TypeSecondayKeyFieldsAttributes
    extends TypeFieldName
{

    @XmlAttribute(name = "order")
    protected TypeSortOrder order;

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link TypeSortOrder }
     *     
     */
    public TypeSortOrder getOrder() {
        if (order == null) {
            return TypeSortOrder.ASC;
        } else {
            return order;
        }
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeSortOrder }
     *     
     */
    public void setOrder(TypeSortOrder value) {
        this.order = value;
    }

}
