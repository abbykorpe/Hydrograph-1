//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.03 at 06:22:33 PM IST 
//


package com.bitwiseglobal.graph.join;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeOperationsComponent;
import com.bitwiseglobal.graph.operationstypes.Join;


/**
 * <p>Java class for join-base complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="join-base">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-operations-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{http://www.bitwiseglobal.com/graph/join}type-in-socket" maxOccurs="unbounded" minOccurs="2"/>
 *         &lt;element name="outSocket" type="{http://www.bitwiseglobal.com/graph/join}type-out-socket" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "join-base")
@XmlSeeAlso({
    Join.class
})
public class JoinBase
    extends TypeOperationsComponent
{


}
