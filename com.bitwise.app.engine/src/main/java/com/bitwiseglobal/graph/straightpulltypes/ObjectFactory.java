//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.19 at 05:36:19 PM IST 
//


package com.bitwiseglobal.graph.straightpulltypes;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bitwiseglobal.graph.straightpulltypes package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bitwiseglobal.graph.straightpulltypes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PartitionByExpression }
     * 
     */
    public PartitionByExpression createPartitionByExpression() {
        return new PartitionByExpression();
    }

    /**
     * Create an instance of {@link PartitionByExpression.Operation }
     * 
     */
    public PartitionByExpression.Operation createPartitionByExpressionOperation() {
        return new PartitionByExpression.Operation();
    }

    /**
     * Create an instance of {@link PartitionByExpression.Operation.InputFields }
     * 
     */
    public PartitionByExpression.Operation.InputFields createPartitionByExpressionOperationInputFields() {
        return new PartitionByExpression.Operation.InputFields();
    }

    /**
     * Create an instance of {@link RemoveDups }
     * 
     */
    public RemoveDups createRemoveDups() {
        return new RemoveDups();
    }

    /**
     * Create an instance of {@link Limit }
     * 
     */
    public Limit createLimit() {
        return new Limit();
    }

    /**
     * Create an instance of {@link UnionAll }
     * 
     */
    public UnionAll createUnionAll() {
        return new UnionAll();
    }

    /**
     * Create an instance of {@link Clone }
     * 
     */
    public Clone createClone() {
        return new Clone();
    }

    /**
     * Create an instance of {@link PartitionByExpression.NoOfPartitions }
     * 
     */
    public PartitionByExpression.NoOfPartitions createPartitionByExpressionNoOfPartitions() {
        return new PartitionByExpression.NoOfPartitions();
    }

    /**
     * Create an instance of {@link PartitionByExpression.Operation.InputFields.Field }
     * 
     */
    public PartitionByExpression.Operation.InputFields.Field createPartitionByExpressionOperationInputFieldsField() {
        return new PartitionByExpression.Operation.InputFields.Field();
    }

    /**
     * Create an instance of {@link RemoveDups.Keep }
     * 
     */
    public RemoveDups.Keep createRemoveDupsKeep() {
        return new RemoveDups.Keep();
    }

    /**
     * Create an instance of {@link Limit.MaxRecords }
     * 
     */
    public Limit.MaxRecords createLimitMaxRecords() {
        return new Limit.MaxRecords();
    }

}
