//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.02 at 04:38:30 PM IST 
//


package com.bitwiseglobal.graph.transform;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for type-transform-operation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-transform-operation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-transform-operation">
 *       &lt;sequence>
 *         &lt;element name="inputFields" type="{http://www.bitwiseglobal.com/graph/transform}type-transform-operation-input-fields" minOccurs="0"/>
 *         &lt;element name="outputFields" type="{http://www.bitwiseglobal.com/graph/commontypes}type-operation-output-fields"/>
 *         &lt;element name="properties" type="{http://www.bitwiseglobal.com/graph/commontypes}type-properties" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="class" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-transform-operation")
public class TypeTransformOperation
    extends com.bitwiseglobal.graph.commontypes.TypeTransformOperation
{


}
