
package hydrograph.engine.jaxb.itfs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.commontypes.TypeInputComponent;
import hydrograph.engine.jaxb.inputtypes.SequenceInputFile;


/**
 * <p>Java class for type-input-file-sequence-base complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-input-file-sequence-base">
 *   &lt;complexContent>
 *     &lt;restriction base="{hydrograph/engine/jaxb/commontypes}type-input-component">
 *       &lt;sequence>
 *         &lt;element name="outSocket" type="{hydrograph/engine/jaxb/itfs}type-input-sequence-out-socket"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-input-file-sequence-base", namespace = "hydrograph/engine/jaxb/itfs")
@XmlSeeAlso({
    SequenceInputFile.class
})
public class TypeInputFileSequenceBase
    extends TypeInputComponent
{


}