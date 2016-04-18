
package hydrograph.engine.jaxb.commontypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.commandtypes.FtpIn;
import hydrograph.engine.jaxb.commandtypes.Hplsql;
import hydrograph.engine.jaxb.commandtypes.RunProgram;
import hydrograph.engine.jaxb.commandtypes.Subgraph;


/**
 * <p>Java class for type-command-component complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-command-component">
 *   &lt;complexContent>
 *     &lt;extension base="{hydrograph/engine/jaxb/commontypes}type-base-component">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-command-component", namespace = "hydrograph/engine/jaxb/commontypes")
@XmlSeeAlso({
    RunProgram.class,
    Subgraph.class,
    FtpIn.class,
    Hplsql.class
})
public abstract class TypeCommandComponent
    extends TypeBaseComponent
{


}