
package hydrograph.engine.jaxb.commontypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import hydrograph.engine.jaxb.aggregate.AggregateBase;
import hydrograph.engine.jaxb.cumulate.CumulateBase;
import hydrograph.engine.jaxb.hashjoin.HashjoinBase;
import hydrograph.engine.jaxb.join.JoinBase;
import hydrograph.engine.jaxb.lookup.LookupBase;
import hydrograph.engine.jaxb.operationstypes.Filter;
import hydrograph.engine.jaxb.operationstypes.GenerateSequence;
import hydrograph.engine.jaxb.operationstypes.Normalize;
import hydrograph.engine.jaxb.operationstypes.Transform;
import hydrograph.engine.jaxb.partitionbyexpression.PartitionByExpressionBase;
import hydrograph.engine.jaxb.subgraph.SubgraphBase;


/**
 * <p>Java class for type-operations-component complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-operations-component">
 *   &lt;complexContent>
 *     &lt;extension base="{hydrograph/engine/jaxb/commontypes}type-base-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{hydrograph/engine/jaxb/commontypes}type-base-inSocket" maxOccurs="unbounded"/>
 *         &lt;element name="operation" type="{hydrograph/engine/jaxb/commontypes}type-transform-operation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="outSocket" type="{hydrograph/engine/jaxb/commontypes}type-operations-out-socket" maxOccurs="unbounded"/>
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
@XmlType(name = "type-operations-component", namespace = "hydrograph/engine/jaxb/commontypes", propOrder = {
    "inSocket",
    "operation",
    "outSocket",
    "runtimeProperties"
})
@XmlSeeAlso({
    Normalize.class,
    GenerateSequence.class,
    Transform.class,
    Filter.class,
    AggregateBase.class,
    JoinBase.class,
    CumulateBase.class,
    LookupBase.class,
    SubgraphBase.class,
    HashjoinBase.class,
    PartitionByExpressionBase.class
})
public abstract class TypeOperationsComponent
    extends TypeBaseComponent
{

    @XmlElement(required = true)
    protected List<TypeBaseInSocket> inSocket;
    protected List<TypeTransformOperation> operation;
    @XmlElement(required = true)
    protected List<TypeOperationsOutSocket> outSocket;
    protected TypeProperties runtimeProperties;

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
     * {@link TypeBaseInSocket }
     * 
     * 
     */
    public List<TypeBaseInSocket> getInSocket() {
        if (inSocket == null) {
            inSocket = new ArrayList<TypeBaseInSocket>();
        }
        return this.inSocket;
    }

    /**
     * Gets the value of the operation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeTransformOperation }
     * 
     * 
     */
    public List<TypeTransformOperation> getOperation() {
        if (operation == null) {
            operation = new ArrayList<TypeTransformOperation>();
        }
        return this.operation;
    }

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
     * {@link TypeOperationsOutSocket }
     * 
     * 
     */
    public List<TypeOperationsOutSocket> getOutSocket() {
        if (outSocket == null) {
            outSocket = new ArrayList<TypeOperationsOutSocket>();
        }
        return this.outSocket;
    }

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
