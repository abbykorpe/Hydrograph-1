//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.05 at 05:51:44 PM IST 
//


package com.bitwiseglobal.graph.removedups;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.straightpulltypes.RemoveDups;


/**
 * <p>Java class for type-removedups-base complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-removedups-base">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-straight-pull-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{http://www.bitwiseglobal.com/graph/commontypes}type-base-inSocket-fixed-in0"/>
 *         &lt;element name="outSocket" type="{http://www.bitwiseglobal.com/graph/removedups}type-out-socket" maxOccurs="2"/>
 *         &lt;element name="runtime_properties" type="{http://www.bitwiseglobal.com/graph/commontypes}type-properties" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-removedups-base")
@XmlSeeAlso({
    RemoveDups.class
})
public class TypeRemovedupsBase
    extends TypeStraightPullComponent
{


}
