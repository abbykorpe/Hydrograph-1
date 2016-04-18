
package hydrograph.engine.jaxb.aggregate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.commontypes.TypeOperationsOutSocket;


/**
 * <p>Java class for type-out-socket complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-out-socket">
 *   &lt;complexContent>
 *     &lt;restriction base="{hydrograph/engine/jaxb/commontypes}type-operations-out-socket">
 *       &lt;choice>
 *         &lt;element name="copyOfInsocket" type="{hydrograph/engine/jaxb/commontypes}type-outSocket-as-inSocket"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="passThroughField" type="{hydrograph/engine/jaxb/aggregate}type-operation-input-field"/>
 *           &lt;element name="operationField" type="{hydrograph/engine/jaxb/commontypes}type-operation-field"/>
 *           &lt;element name="mapField" type="{hydrograph/engine/jaxb/commontypes}type-map-field"/>
 *         &lt;/choice>
 *       &lt;/choice>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="out0" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" fixed="out" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-out-socket", namespace = "hydrograph/engine/jaxb/aggregate")
public class TypeOutSocket
    extends TypeOperationsOutSocket
{


}