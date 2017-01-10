
package hydrograph.engine.jaxb.operationstypes;

import hydrograph.engine.jaxb.partitionbyexpression.PartitionByExpressionBase;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for partitionByExpression complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="partitionByExpression">
 *   &lt;complexContent>
 *     &lt;extension base="{hydrograph/engine/jaxb/partitionByExpression}partitionByExpression-base">
 *       &lt;sequence>
 *         &lt;element name="noOfPartitions">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "partitionByExpression", namespace = "hydrograph/engine/jaxb/operationstypes", propOrder = {
    "noOfPartitions"
})
public class PartitionByExpression
    extends PartitionByExpressionBase
{

    @XmlElement(required = true)
    protected NoOfPartitions noOfPartitions;

    /**
     * Gets the value of the noOfPartitions property.
     * 
     * @return
     *     possible object is
     *     {@link NoOfPartitions }
     *     
     */
    public NoOfPartitions getNoOfPartitions() {
        return noOfPartitions;
    }

    /**
     * Sets the value of the noOfPartitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link NoOfPartitions }
     *     
     */
    public void setNoOfPartitions(NoOfPartitions value) {
        this.noOfPartitions = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class NoOfPartitions {

        @XmlAttribute(name = "value", required = true)
        protected long value;

        /**
         * Gets the value of the value property.
         * 
         */
        public long getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         */
        public void setValue(long value) {
            this.value = value;
        }

    }

}
