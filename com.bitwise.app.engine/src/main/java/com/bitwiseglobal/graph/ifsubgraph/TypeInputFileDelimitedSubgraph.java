//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.07 at 03:05:21 PM IST 


package com.bitwiseglobal.graph.ifsubgraph;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeInputComponent;
import com.bitwiseglobal.graph.inputtypes.SubgraphInput;


/**
 * <p>Java class for type-input-file-delimited-subgraph complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-input-file-delimited-subgraph">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-input-component">
 *       &lt;sequence>
 *         &lt;element name="outSocket" type="{http://www.bitwiseglobal.com/graph/ifsubgraph}type-input-delimited-out-socket" maxOccurs="unbounded"/>
 *         &lt;element name="dependsOn" type="{http://www.bitwiseglobal.com/graph/commontypes}type-depends-on" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-input-file-delimited-subgraph")
@XmlSeeAlso({
    SubgraphInput.class
})
public class TypeInputFileDelimitedSubgraph
    extends TypeInputComponent
{


}
