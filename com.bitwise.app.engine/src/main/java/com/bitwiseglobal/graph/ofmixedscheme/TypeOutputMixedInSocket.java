//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.03 at 06:22:33 PM IST 
//


package com.bitwiseglobal.graph.ofmixedscheme;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeOutputInSocket;


/**
 * <p>Java class for type-output-mixed-in-socket complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-output-mixed-in-socket">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-output-inSocket">
 *       &lt;sequence>
 *         &lt;element name="schema" type="{http://www.bitwiseglobal.com/graph/ofmixedscheme}type-mixed-record"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="in0" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" fixed="in" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-output-mixed-in-socket")
public class TypeOutputMixedInSocket
    extends TypeOutputInSocket
{


}
