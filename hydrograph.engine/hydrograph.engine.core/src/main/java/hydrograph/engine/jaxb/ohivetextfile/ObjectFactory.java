
package hydrograph.engine.jaxb.ohivetextfile;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the hydrograph.engine.jaxb.ohivetextfile package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: hydrograph.engine.jaxb.ohivetextfile
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HivePathType }
     * 
     */
    public HivePathType createHivePathType() {
        return new HivePathType();
    }

    /**
     * Create an instance of {@link TypeOutputHiveTextFileDelimitedInSocket }
     * 
     */
    public TypeOutputHiveTextFileDelimitedInSocket createTypeOutputHiveTextFileDelimitedInSocket() {
        return new TypeOutputHiveTextFileDelimitedInSocket();
    }

    /**
     * Create an instance of {@link HiveType }
     * 
     */
    public HiveType createHiveType() {
        return new HiveType();
    }

    /**
     * Create an instance of {@link HivePartitionFieldsType }
     * 
     */
    public HivePartitionFieldsType createHivePartitionFieldsType() {
        return new HivePartitionFieldsType();
    }

    /**
     * Create an instance of {@link FieldBasicType }
     * 
     */
    public FieldBasicType createFieldBasicType() {
        return new FieldBasicType();
    }

    /**
     * Create an instance of {@link PartitionFieldBasicType }
     * 
     */
    public PartitionFieldBasicType createPartitionFieldBasicType() {
        return new PartitionFieldBasicType();
    }

    /**
     * Create an instance of {@link TypeOutputHiveTextFileDelimitedBase }
     * 
     */
    public TypeOutputHiveTextFileDelimitedBase createTypeOutputHiveTextFileDelimitedBase() {
        return new TypeOutputHiveTextFileDelimitedBase();
    }

}
