
package hydrograph.engine.jaxb.partitionbyexpression;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.commontypes.TypeOperationInputFields;


/**
 * <p>Java class for type-pbe-input-fields complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-pbe-input-fields">
 *   &lt;complexContent>
 *     &lt;restriction base="{hydrograph/engine/jaxb/commontypes}type-operation-input-fields">
 *       &lt;sequence>
 *         &lt;element name="field" type="{hydrograph/engine/jaxb/partitionByExpression}type-pbe-operation-input-field" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-pbe-input-fields")
public class TypePbeInputFields
    extends TypeOperationInputFields
{


}
