
package hydrograph.engine.jaxb.commontypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.ifmixedscheme.TypeMixedBase;
import hydrograph.engine.jaxb.ifsubjob.TypeInputFileDelimitedSubjob;
import hydrograph.engine.jaxb.igr.TypeGenerateRecordBase;
import hydrograph.engine.jaxb.ihivetextfile.TypeInputHiveTextFileDelimitedBase;
import hydrograph.engine.jaxb.itffw.TypeFixedWidthBase;
import hydrograph.engine.jaxb.itfs.TypeInputFileSequenceBase;


/**
 * <p>Java class for type-input-component complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-input-component">
 *   &lt;complexContent>
 *     &lt;extension base="{hydrograph/engine/jaxb/commontypes}type-base-component">
 *       &lt;sequence>
 *         &lt;element name="outSocket" type="{hydrograph/engine/jaxb/commontypes}type-input-outSocket" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-input-component", namespace = "hydrograph/engine/jaxb/commontypes", propOrder = {
    "outSocket"
})
@XmlSeeAlso({
    TypeFixedWidthBase.class,
    hydrograph.engine.jaxb.itfd.TypeInputFileDelimitedBase.class,
    TypeInputFileSequenceBase.class,
    TypeGenerateRecordBase.class,
    hydrograph.engine.jaxb.ifparquet.TypeInputFileDelimitedBase.class,
    hydrograph.engine.jaxb.ihiveparquet.TypeInputFileDelimitedBase.class,
    TypeInputHiveTextFileDelimitedBase.class,
    TypeMixedBase.class,
    hydrograph.engine.jaxb.ifsubjob.TypeInputFileDelimitedBase.class,
    TypeInputFileDelimitedSubjob.class
})
public abstract class TypeInputComponent
    extends TypeBaseComponent
{

    @XmlElement(required = true)
    protected List<TypeInputOutSocket> outSocket;

    /**
     * Gets the value of the outSocket property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outSocket property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutSocket().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeInputOutSocket }
     * 
     * 
     */
    public List<TypeInputOutSocket> getOutSocket() {
        if (outSocket == null) {
            outSocket = new ArrayList<TypeInputOutSocket>();
        }
        return this.outSocket;
    }

}
