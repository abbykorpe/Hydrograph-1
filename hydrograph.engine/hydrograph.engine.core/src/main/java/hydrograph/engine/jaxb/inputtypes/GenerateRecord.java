
package hydrograph.engine.jaxb.inputtypes;

import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.igr.TypeGenerateRecordBase;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for generateRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="generateRecord">
 *   &lt;complexContent>
 *     &lt;extension base="{hydrograph/engine/jaxb/igr}type-generate-record-base">
 *       &lt;sequence>
 *         &lt;element name="runtimeProperties" type="{hydrograph/engine/jaxb/commontypes}type-properties" minOccurs="0"/>
 *         &lt;element name="recordCount">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}long" />
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
@XmlType(name = "generateRecord", namespace = "hydrograph/engine/jaxb/inputtypes", propOrder = {
    "runtimeProperties",
    "recordCount"
})
public class GenerateRecord
    extends TypeGenerateRecordBase
{

    protected TypeProperties runtimeProperties;
    @XmlElement(required = true)
    protected GenerateRecord.RecordCount recordCount;

    /**
     * Gets the value of the runtimeProperties property.
     * 
     * @return
     *     possible object is
     *     {@link TypeProperties }
     *     
     */
    public TypeProperties getRuntimeProperties() {
        return runtimeProperties;
    }

    /**
     * Sets the value of the runtimeProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeProperties }
     *     
     */
    public void setRuntimeProperties(TypeProperties value) {
        this.runtimeProperties = value;
    }

    /**
     * Gets the value of the recordCount property.
     * 
     * @return
     *     possible object is
     *     {@link GenerateRecord.RecordCount }
     *     
     */
    public GenerateRecord.RecordCount getRecordCount() {
        return recordCount;
    }

    /**
     * Sets the value of the recordCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link GenerateRecord.RecordCount }
     *     
     */
    public void setRecordCount(GenerateRecord.RecordCount value) {
        this.recordCount = value;
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
     *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}long" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class RecordCount {

        @XmlAttribute(name = "value")
        protected Long value;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link Long }
         *     
         */
        public Long getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link Long }
         *     
         */
        public void setValue(Long value) {
            this.value = value;
        }

    }

}
