//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.03 at 12:05:43 PM IST 
//


package com.bitwiseglobal.graph.cumulate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeOperationsOutSocket;


/**
 * <p>Java class for type-out-socket complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-out-socket">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-operations-out-socket">
 *       &lt;choice>
 *         &lt;element name="copyOfInsocket" type="{http://www.bitwiseglobal.com/graph/commontypes}type-outSocket-as-inSocket"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="passThroughField" type="{http://www.bitwiseglobal.com/graph/cumulate}type-operation-input-field"/>
 *           &lt;element name="operationField" type="{http://www.bitwiseglobal.com/graph/commontypes}type-operation-field"/>
 *           &lt;element name="mapField" type="{http://www.bitwiseglobal.com/graph/commontypes}type-map-field"/>
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
@XmlType(name = "type-out-socket")
public class TypeOutSocket
    extends TypeOperationsOutSocket
{


}
