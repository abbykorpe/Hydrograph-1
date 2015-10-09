//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.21 at 07:24:07 PM IST 
//


package com.bitwiseglobal.graph.commontypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.igr.TypeGenerateRecordBase;
import com.bitwiseglobal.graph.itfd.TypeInputFileDelimitedBase;
import com.bitwiseglobal.graph.itffw.TypeFixedWidthBase;


/**
 * <p>Java class for type-input-component complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-input-component">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bitwiseglobal.com/graph/commontypes}type-base-component">
 *       &lt;sequence>
 *         &lt;element name="outSocket" type="{http://www.bitwiseglobal.com/graph/commontypes}type-input-outSocket" maxOccurs="unbounded"/>
 *         &lt;element name="dependsOn" type="{http://www.bitwiseglobal.com/graph/commontypes}type-depends-on" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-input-component", propOrder = {
    "outSocket",
    "dependsOn"
})
@XmlSeeAlso({
    TypeFixedWidthBase.class,
    TypeInputFileDelimitedBase.class,
    TypeGenerateRecordBase.class
})
public abstract class TypeInputComponent
    extends TypeBaseComponent
{

    @XmlElement(required = true)
    protected List<TypeInputOutSocket> outSocket;
    protected List<TypeDependsOn> dependsOn;

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

    /**
     * Gets the value of the dependsOn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dependsOn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDependsOn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeDependsOn }
     * 
     * 
     */
    public List<TypeDependsOn> getDependsOn() {
        if (dependsOn == null) {
            dependsOn = new ArrayList<TypeDependsOn>();
        }
        return this.dependsOn;
    }

}