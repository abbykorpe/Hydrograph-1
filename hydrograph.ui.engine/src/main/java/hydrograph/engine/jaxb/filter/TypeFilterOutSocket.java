
package hydrograph.engine.jaxb.filter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;


/**
 * <p>Java class for type-filter-out-socket complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-filter-out-socket">
 *   &lt;complexContent>
 *     &lt;restriction base="{hydrograph/engine/jaxb/commontypes}type-operations-out-socket">
 *       &lt;choice>
 *         &lt;element name="copyOfInsocket" type="{hydrograph/engine/jaxb/commontypes}type-outSocket-as-inSocket"/>
 *       &lt;/choice>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" use="required" type="{hydrograph/engine/jaxb/filter}types-outSocket-types" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-filter-out-socket", namespace = "hydrograph/engine/jaxb/filter")
public class TypeFilterOutSocket
    extends TypeOperationsOutSocket
{


}
