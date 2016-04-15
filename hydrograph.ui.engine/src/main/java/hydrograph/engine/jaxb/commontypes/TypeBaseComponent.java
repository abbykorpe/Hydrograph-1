
package hydrograph.engine.jaxb.commontypes;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for type-base-component complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-base-component">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="phase" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-base-component", namespace = "hydrograph/engine/jaxb/commontypes")
@XmlSeeAlso({
    TypeInputComponent.class,
    TypeOutputComponent.class,
    TypeStraightPullComponent.class,
    TypeOperationsComponent.class,
    TypeCommandComponent.class
})
public class TypeBaseComponent {

    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "phase")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger phase;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the phase property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPhase() {
        if (phase == null) {
            return new BigInteger("0");
        } else {
            return phase;
        }
    }

    /**
     * Sets the value of the phase property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPhase(BigInteger value) {
        this.phase = value;
    }

}
