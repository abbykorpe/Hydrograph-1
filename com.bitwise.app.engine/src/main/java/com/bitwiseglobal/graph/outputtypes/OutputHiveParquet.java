//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.16 at 05:21:26 PM IST 
//


package com.bitwiseglobal.graph.outputtypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.bitwiseglobal.graph.commontypes.TypeProperties;
import com.bitwiseglobal.graph.ohiveparquet.HivePartitionFieldsType;
import com.bitwiseglobal.graph.ohiveparquet.HiveType;
import com.bitwiseglobal.graph.ohiveparquet.TypeOutputFileDelimitedBase;


/**
 * <p>Java class for output-hive-parquet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="output-hive-parquet">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bitwiseglobal.com/graph/ohiveparquet}type-output-file-delimited-base">
 *       &lt;sequence>
 *         &lt;element name="partition_keys" type="{http://www.bitwiseglobal.com/graph/ohiveparquet}hive_partition_fields_type" minOccurs="0"/>
 *         &lt;element name="database_name" type="{http://www.bitwiseglobal.com/graph/ohiveparquet}hive_type"/>
 *         &lt;element name="table_name" type="{http://www.bitwiseglobal.com/graph/ohiveparquet}hive_type"/>
 *         &lt;element name="runtime_properties" type="{http://www.bitwiseglobal.com/graph/commontypes}type-properties" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "output-hive-parquet", propOrder = {
    "partitionKeys",
    "databaseName",
    "tableName",
    "runtimeProperties"
})
public class OutputHiveParquet
    extends TypeOutputFileDelimitedBase
{

    @XmlElement(name = "partition_keys")
    protected HivePartitionFieldsType partitionKeys;
    @XmlElement(name = "database_name", required = true)
    protected HiveType databaseName;
    @XmlElement(name = "table_name", required = true)
    protected HiveType tableName;
    @XmlElement(name = "runtime_properties")
    protected TypeProperties runtimeProperties;

    /**
     * Gets the value of the partitionKeys property.
     * 
     * @return
     *     possible object is
     *     {@link HivePartitionFieldsType }
     *     
     */
    public HivePartitionFieldsType getPartitionKeys() {
        return partitionKeys;
    }

    /**
     * Sets the value of the partitionKeys property.
     * 
     * @param value
     *     allowed object is
     *     {@link HivePartitionFieldsType }
     *     
     */
    public void setPartitionKeys(HivePartitionFieldsType value) {
        this.partitionKeys = value;
    }

    /**
     * Gets the value of the databaseName property.
     * 
     * @return
     *     possible object is
     *     {@link HiveType }
     *     
     */
    public HiveType getDatabaseName() {
        return databaseName;
    }

    /**
     * Sets the value of the databaseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link HiveType }
     *     
     */
    public void setDatabaseName(HiveType value) {
        this.databaseName = value;
    }

    /**
     * Gets the value of the tableName property.
     * 
     * @return
     *     possible object is
     *     {@link HiveType }
     *     
     */
    public HiveType getTableName() {
        return tableName;
    }

    /**
     * Sets the value of the tableName property.
     * 
     * @param value
     *     allowed object is
     *     {@link HiveType }
     *     
     */
    public void setTableName(HiveType value) {
        this.tableName = value;
    }

    /**
     * Gets the value of the runtimeProperties property.
     * 
     * @return
     *     possible object is
     *     {@link TypeProperties }
     *     
     */
    public TypeProperties getRuntimeProperties() {
        return runtimeProperties;
    }

    /**
     * Sets the value of the runtimeProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeProperties }
     *     
     */
    public void setRuntimeProperties(TypeProperties value) {
        this.runtimeProperties = value;
    }

}
