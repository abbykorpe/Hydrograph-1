//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.28 at 02:50:05 PM IST 
//


package com.bitwiseglobal.graph.ifmixedscheme;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeBaseRecord;


/**
 * <p>Java class for type-mixed-record complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-mixed-record">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-base-record">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="field" type="{http://www.bitwiseglobal.com/graph/ifmixedscheme}type-mixed-field"/>
 *         &lt;element name="record" type="{http://www.bitwiseglobal.com/graph/ifmixedscheme}type-mixed-record"/>
 *       &lt;/choice>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type-mixed-record")
public class TypeMixedRecord
    extends TypeBaseRecord
{


}
