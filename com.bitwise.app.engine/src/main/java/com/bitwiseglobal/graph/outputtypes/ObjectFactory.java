//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.28 at 02:50:05 PM IST 
//


package com.bitwiseglobal.graph.outputtypes;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bitwiseglobal.graph.outputtypes package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bitwiseglobal.graph.outputtypes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OutputFileParquet }
     * 
     */
    public OutputFileParquet createOutputFileParquet() {
        return new OutputFileParquet();
    }

    /**
     * Create an instance of {@link OutputFileMixedSchemeText }
     * 
     */
    public OutputFileMixedSchemeText createOutputFileMixedSchemeText() {
        return new OutputFileMixedSchemeText();
    }

    /**
     * Create an instance of {@link FileFixedWidth }
     * 
     */
    public FileFixedWidth createFileFixedWidth() {
        return new FileFixedWidth();
    }

    /**
     * Create an instance of {@link FileDelimited }
     * 
     */
    public FileDelimited createFileDelimited() {
        return new FileDelimited();
    }

    /**
     * Create an instance of {@link OutputHiveParquet }
     * 
     */
    public OutputHiveParquet createOutputHiveParquet() {
        return new OutputHiveParquet();
    }

    /**
     * Create an instance of {@link Trash }
     * 
     */
    public Trash createTrash() {
        return new Trash();
    }

    /**
     * Create an instance of {@link OutputFileParquet.Path }
     * 
     */
    public OutputFileParquet.Path createOutputFileParquetPath() {
        return new OutputFileParquet.Path();
    }

    /**
     * Create an instance of {@link OutputFileMixedSchemeText.Path }
     * 
     */
    public OutputFileMixedSchemeText.Path createOutputFileMixedSchemeTextPath() {
        return new OutputFileMixedSchemeText.Path();
    }

    /**
     * Create an instance of {@link OutputFileMixedSchemeText.Charset }
     * 
     */
    public OutputFileMixedSchemeText.Charset createOutputFileMixedSchemeTextCharset() {
        return new OutputFileMixedSchemeText.Charset();
    }

    /**
     * Create an instance of {@link FileFixedWidth.Path }
     * 
     */
    public FileFixedWidth.Path createFileFixedWidthPath() {
        return new FileFixedWidth.Path();
    }

    /**
     * Create an instance of {@link FileFixedWidth.Charset }
     * 
     */
    public FileFixedWidth.Charset createFileFixedWidthCharset() {
        return new FileFixedWidth.Charset();
    }

    /**
     * Create an instance of {@link FileDelimited.Path }
     * 
     */
    public FileDelimited.Path createFileDelimitedPath() {
        return new FileDelimited.Path();
    }

    /**
     * Create an instance of {@link FileDelimited.Delimiter }
     * 
     */
    public FileDelimited.Delimiter createFileDelimitedDelimiter() {
        return new FileDelimited.Delimiter();
    }

    /**
     * Create an instance of {@link FileDelimited.Charset }
     * 
     */
    public FileDelimited.Charset createFileDelimitedCharset() {
        return new FileDelimited.Charset();
    }

    /**
     * Create an instance of {@link FileDelimited.Quote }
     * 
     */
    public FileDelimited.Quote createFileDelimitedQuote() {
        return new FileDelimited.Quote();
    }

}
