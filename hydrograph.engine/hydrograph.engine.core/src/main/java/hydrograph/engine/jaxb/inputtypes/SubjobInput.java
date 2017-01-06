
package hydrograph.engine.jaxb.inputtypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.ifsubjob.TypeInputFileDelimitedSubjob;


/**
 * <p>Java class for subjobInput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="subjobInput">
 *   &lt;complexContent>
 *     &lt;extension base="{hydrograph/engine/jaxb/ifsubjob}type-input-file-delimited-subjob">
 *       &lt;sequence>
 *         &lt;element name="runtimeProperties" type="{hydrograph/engine/jaxb/commontypes}type-properties" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subjobInput", namespace = "hydrograph/engine/jaxb/inputtypes", propOrder = {
    "runtimeProperties"
})
public class SubjobInput
    extends TypeInputFileDelimitedSubjob
{

    protected TypeProperties runtimeProperties;

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

}
