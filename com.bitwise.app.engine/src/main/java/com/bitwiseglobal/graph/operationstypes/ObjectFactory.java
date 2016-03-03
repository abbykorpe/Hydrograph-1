//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.03 at 12:05:43 PM IST 
//


package com.bitwiseglobal.graph.operationstypes;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bitwiseglobal.graph.operationstypes package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bitwiseglobal.graph.operationstypes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Lookup }
     * 
     */
    public Lookup createLookup() {
        return new Lookup();
    }

    /**
     * Create an instance of {@link HashJoin }
     * 
     */
    public HashJoin createHashJoin() {
        return new HashJoin();
    }

    /**
     * Create an instance of {@link PartitionByExpression }
     * 
     */
    public PartitionByExpression createPartitionByExpression() {
        return new PartitionByExpression();
    }

    /**
     * Create an instance of {@link Subgraph }
     * 
     */
    public Subgraph createSubgraph() {
        return new Subgraph();
    }

    /**
     * Create an instance of {@link Normalize }
     * 
     */
    public Normalize createNormalize() {
        return new Normalize();
    }

    /**
     * Create an instance of {@link GenerateSequence }
     * 
     */
    public GenerateSequence createGenerateSequence() {
        return new GenerateSequence();
    }

    /**
     * Create an instance of {@link Transform }
     * 
     */
    public Transform createTransform() {
        return new Transform();
    }

    /**
     * Create an instance of {@link Aggregate }
     * 
     */
    public Aggregate createAggregate() {
        return new Aggregate();
    }

    /**
     * Create an instance of {@link Cumulate }
     * 
     */
    public Cumulate createCumulate() {
        return new Cumulate();
    }

    /**
     * Create an instance of {@link Join }
     * 
     */
    public Join createJoin() {
        return new Join();
    }

    /**
     * Create an instance of {@link Filter }
     * 
     */
    public Filter createFilter() {
        return new Filter();
    }

    /**
     * Create an instance of {@link Lookup.Match }
     * 
     */
    public Lookup.Match createLookupMatch() {
        return new Lookup.Match();
    }

    /**
     * Create an instance of {@link HashJoin.Match }
     * 
     */
    public HashJoin.Match createHashJoinMatch() {
        return new HashJoin.Match();
    }

    /**
     * Create an instance of {@link PartitionByExpression.NoOfPartitions }
     * 
     */
    public PartitionByExpression.NoOfPartitions createPartitionByExpressionNoOfPartitions() {
        return new PartitionByExpression.NoOfPartitions();
    }

    /**
     * Create an instance of {@link Subgraph.Path }
     * 
     */
    public Subgraph.Path createSubgraphPath() {
        return new Subgraph.Path();
    }

}
