//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.16 at 05:21:26 PM IST 
//


package com.bitwiseglobal.graph.lookup;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bitwiseglobal.graph.lookup package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bitwiseglobal.graph.lookup
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LookupBase }
     * 
     */
    public LookupBase createLookupBase() {
        return new LookupBase();
    }

    /**
     * Create an instance of {@link TypeInSocket }
     * 
     */
    public TypeInSocket createTypeInSocket() {
        return new TypeInSocket();
    }

    /**
     * Create an instance of {@link TypeOutSocket }
     * 
     */
    public TypeOutSocket createTypeOutSocket() {
        return new TypeOutSocket();
    }

    /**
     * Create an instance of {@link TypeKeyFields }
     * 
     */
    public TypeKeyFields createTypeKeyFields() {
        return new TypeKeyFields();
    }

}
