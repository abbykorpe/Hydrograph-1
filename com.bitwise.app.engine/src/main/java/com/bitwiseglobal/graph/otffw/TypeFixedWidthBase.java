//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.21 at 07:24:07 PM IST 
//


package com.bitwiseglobal.graph.otffw;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeOutputComponent;
import com.bitwiseglobal.graph.outputtypes.FileFixedWidth;


/**
 * <p>Java class for type-fixed-width-base complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-fixed-width-base">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-output-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{http://www.bitwiseglobal.com/graph/otffw}type-output-fixedwidth-in-socket"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-fixed-width-base")
@XmlSeeAlso({
    FileFixedWidth.class
})
public class TypeFixedWidthBase
    extends TypeOutputComponent
{


}