//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.28 at 05:29:11 PM IST 
//


package com.bitwiseglobal.graph.otfd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeOutputComponent;
import com.bitwiseglobal.graph.outputtypes.TextFileDelimited;


/**
 * <p>Java class for type-output-file-delimited-base complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-output-file-delimited-base">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-output-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{http://www.bitwiseglobal.com/graph/otfd}type-output-delimited-in-socket"/>
 *         &lt;element name="overWrite" type="{http://www.bitwiseglobal.com/graph/commontypes}type-true-false" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-output-file-delimited-base")
@XmlSeeAlso({
    TextFileDelimited.class
})
public class TypeOutputFileDelimitedBase
    extends TypeOutputComponent
{


}
