
package hydrograph.engine.jaxb.ohiveparquet;

import hydrograph.engine.jaxb.commontypes.TypeOutputComponent;
import hydrograph.engine.jaxb.outputtypes.ParquetHiveFile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for type-output-file-delimited-base complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-output-file-delimited-base">
 *   &lt;complexContent>
 *     &lt;restriction base="{hydrograph/engine/jaxb/commontypes}type-output-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{hydrograph/engine/jaxb/ohiveparquet}type-output-delimited-in-socket"/>
 *         &lt;element name="overWrite" type="{hydrograph/engine/jaxb/commontypes}type-true-false" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-output-file-delimited-base", namespace = "hydrograph/engine/jaxb/ohiveparquet")
@XmlSeeAlso({
    ParquetHiveFile.class
})
public class TypeOutputFileDelimitedBase
    extends TypeOutputComponent
{


}
