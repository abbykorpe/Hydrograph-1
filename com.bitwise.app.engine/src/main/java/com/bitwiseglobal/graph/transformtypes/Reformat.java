//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.16 at 05:21:26 PM IST 
//


package com.bitwiseglobal.graph.transformtypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeTransformComponent;


/**
 * <p>Java class for reformat complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reformat">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-transform-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{http://www.bitwiseglobal.com/graph/reformat}type-reformat-in-socket"/>
 *         &lt;element name="operation" type="{http://www.bitwiseglobal.com/graph/reformat}type-reformat-operation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="outSocket" type="{http://www.bitwiseglobal.com/graph/reformat}type-reformat-out-socket"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reformat")
public class Reformat
    extends TypeTransformComponent
{


}
