//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.28 at 05:29:11 PM IST 
//


package com.bitwiseglobal.graph.igr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeBaseRecord;


/**
 * <p>Java class for type-generate-record-record complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type-generate-record-record">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.bitwiseglobal.com/graph/commontypes}type-base-record">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="field" type="{http://www.bitwiseglobal.com/graph/igr}type-generate-record-field"/>
 *         &lt;element name="record" type="{http://www.bitwiseglobal.com/graph/igr}type-generate-record-record"/>
 *         &lt;element name="includeExternalSchema" type="{http://www.bitwiseglobal.com/graph/commontypes}type-external-schema" minOccurs="0"/>
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
@XmlType(name = "type-generate-record-record")
public class TypeGenerateRecordRecord
    extends TypeBaseRecord
{


}
