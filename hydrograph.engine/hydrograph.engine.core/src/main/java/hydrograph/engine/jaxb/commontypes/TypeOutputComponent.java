
package hydrograph.engine.jaxb.commontypes;

import hydrograph.engine.jaxb.ofmixedscheme.TypeMixedBase;
import hydrograph.engine.jaxb.ofsubjob.TypeOutputFileDelimitedSubjob;
import hydrograph.engine.jaxb.ohivetextfile.TypeOutputHiveTextFileDelimitedBase;
import hydrograph.engine.jaxb.omysql.TypeOutputMysqlBase;
import hydrograph.engine.jaxb.ooracle.TypeOutputOracleBase;
import hydrograph.engine.jaxb.oredshift.TypeOutputRedshiftBase;
import hydrograph.engine.jaxb.otffw.TypeFixedWidthBase;
import hydrograph.engine.jaxb.otfs.TypeOutputFileSequenceBase;
import hydrograph.engine.jaxb.outputtypes.Discard;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for type-output-component complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-output-component">
 *   &lt;complexContent>
 *     &lt;extension base="{hydrograph/engine/jaxb/commontypes}type-base-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{hydrograph/engine/jaxb/commontypes}type-output-inSocket" maxOccurs="unbounded"/>
 *         &lt;element name="overWrite" type="{hydrograph/engine/jaxb/commontypes}type-true-false" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-output-component", namespace = "hydrograph/engine/jaxb/commontypes", propOrder = {
    "inSocket",
    "overWrite"
})
@XmlSeeAlso({
    Discard.class,
    TypeFixedWidthBase.class,
    hydrograph.engine.jaxb.otfd.TypeOutputFileDelimitedBase.class,
    TypeOutputFileSequenceBase.class,
    hydrograph.engine.jaxb.ofparquet.TypeOutputFileDelimitedBase.class,
    hydrograph.engine.jaxb.ohiveparquet.TypeOutputFileDelimitedBase.class,
    TypeOutputHiveTextFileDelimitedBase.class,
    TypeMixedBase.class,
    TypeOutputFileDelimitedSubjob.class,
    hydrograph.engine.jaxb.ofsubjob.TypeOutputFileDelimitedBase.class,
    TypeOutputMysqlBase.class,
    TypeOutputRedshiftBase.class,
    TypeOutputOracleBase.class
})
public abstract class TypeOutputComponent
    extends TypeBaseComponent
{

    @XmlElement(required = true)
    protected List<TypeOutputInSocket> inSocket;
    protected TypeTrueFalse overWrite;

    /**
     * Gets the value of the inSocket property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inSocket property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInSocket().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeOutputInSocket }
     * 
     * 
     */
    public List<TypeOutputInSocket> getInSocket() {
        if (inSocket == null) {
            inSocket = new ArrayList<TypeOutputInSocket>();
        }
        return this.inSocket;
    }

    /**
     * Gets the value of the overWrite property.
     * 
     * @return
     *     possible object is
     *     {@link TypeTrueFalse }
     *     
     */
    public TypeTrueFalse getOverWrite() {
        return overWrite;
    }

    /**
     * Sets the value of the overWrite property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeTrueFalse }
     *     
     */
    public void setOverWrite(TypeTrueFalse value) {
        this.overWrite = value;
    }

}
