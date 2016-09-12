
package hydrograph.engine.jaxb.ordbms;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the hydrograph.engine.jaxb.ordbms package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: hydrograph.engine.jaxb.ordbms
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TypeRdbmsField }
     * 
     */
    public TypeRdbmsField createTypeRdbmsField() {
        return new TypeRdbmsField();
    }

    /**
     * Create an instance of {@link DatabaseType }
     * 
     */
    public DatabaseType createDatabaseType() {
        return new DatabaseType();
    }

    /**
     * Create an instance of {@link TypeOutputRdbmsBase }
     * 
     */
    public TypeOutputRdbmsBase createTypeOutputRdbmsBase() {
        return new TypeOutputRdbmsBase();
    }

    /**
     * Create an instance of {@link TypePriamryKeys }
     * 
     */
    public TypePriamryKeys createTypePriamryKeys() {
        return new TypePriamryKeys();
    }

    /**
     * Create an instance of {@link TypeRdbmsRecord }
     * 
     */
    public TypeRdbmsRecord createTypeRdbmsRecord() {
        return new TypeRdbmsRecord();
    }

    /**
     * Create an instance of {@link TypeLoadChoice }
     * 
     */
    public TypeLoadChoice createTypeLoadChoice() {
        return new TypeLoadChoice();
    }

    /**
     * Create an instance of {@link TypeOutputRdbmsOutSocket }
     * 
     */
    public TypeOutputRdbmsOutSocket createTypeOutputRdbmsOutSocket() {
        return new TypeOutputRdbmsOutSocket();
    }

    /**
     * Create an instance of {@link TypeUpdateKeys }
     * 
     */
    public TypeUpdateKeys createTypeUpdateKeys() {
        return new TypeUpdateKeys();
    }

}
