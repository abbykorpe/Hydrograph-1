//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.05 at 05:51:44 PM IST 
//


package com.bitwiseglobal.graph.ofmixedscheme;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeOutputComponent;
import com.bitwiseglobal.graph.outputtypes.TextFileMixedScheme;


/**
 * <p>Java class for type-mixed-base complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-mixed-base">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-output-component">
 *       &lt;sequence>
 *         &lt;element name="inSocket" type="{http://www.bitwiseglobal.com/graph/ofmixedscheme}type-output-mixed-in-socket"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-mixed-base")
@XmlSeeAlso({
    TextFileMixedScheme.class
})
public class TypeMixedBase
    extends TypeOutputComponent
{


}
