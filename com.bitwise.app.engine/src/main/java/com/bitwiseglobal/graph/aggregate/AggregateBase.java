//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.07 at 04:14:42 PM IST 
//


package com.bitwiseglobal.graph.aggregate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.operationstypes.Aggregate;


/**
 * <p>Java class for aggregate-base complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="aggregate-base">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-operations-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{http://www.bitwiseglobal.com/graph/aggregate}type-in-socket"/>
 *         &lt;element name="operation" type="{http://www.bitwiseglobal.com/graph/aggregate}type-operation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="outSocket" type="{http://www.bitwiseglobal.com/graph/aggregate}type-out-socket"/>
 *         &lt;element name="runtimeProperties" type="{http://www.bitwiseglobal.com/graph/commontypes}type-properties" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "aggregate-base")
@XmlSeeAlso({
    Aggregate.class
})
public class AggregateBase
    extends TypeOperationsComponent
{


}
