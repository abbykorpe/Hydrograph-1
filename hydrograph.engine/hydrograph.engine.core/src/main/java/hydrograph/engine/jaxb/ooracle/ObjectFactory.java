
package hydrograph.engine.jaxb.ooracle;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the hydrograph.engine.jaxb.ooracle package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: hydrograph.engine.jaxb.ooracle
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TypeOracleRecord }
     * 
     */
    public TypeOracleRecord createTypeOracleRecord() {
        return new TypeOracleRecord();
    }

    /**
     * Create an instance of {@link TypeUpdateKeys }
     * 
     */
    public TypeUpdateKeys createTypeUpdateKeys() {
        return new TypeUpdateKeys();
    }

    /**
     * Create an instance of {@link DatabaseType }
     * 
     */
    public DatabaseType createDatabaseType() {
        return new DatabaseType();
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Create an instance of {@link TypeOutputOracleBase }
=======
     * Create an instance of {@link TypeOracleField }
>>>>>>> Added teradata component with changed loadTypeUtils and exportUtils
=======
     * Create an instance of {@link TypeOracleField }
>>>>>>> partition by Expression component upgraded for expression support
     * 
     */
    public TypeOracleField createTypeOracleField() {
        return new TypeOracleField();
    }

    /**
     * Create an instance of {@link TypeLoadChoice }
     * 
     */
    public TypeLoadChoice createTypeLoadChoice() {
        return new TypeLoadChoice();
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Create an instance of {@link TypeOracleField }
=======
     * Create an instance of {@link TypeUpdateKeys }
     * 
     */
    public TypeUpdateKeys createTypeUpdateKeys() {
        return new TypeUpdateKeys();
    }

    /**
     * Create an instance of {@link TypeOracleRecord }
>>>>>>> Added teradata component with changed loadTypeUtils and exportUtils
=======
     * Create an instance of {@link TypePrimaryKeys }
>>>>>>> partition by Expression component upgraded for expression support
     * 
     */
    public TypePrimaryKeys createTypePrimaryKeys() {
        return new TypePrimaryKeys();
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
     * Create an instance of {@link TypeUpdateKeys }
=======
     * Create an instance of {@link TypeOutputOracleBase }
>>>>>>> Added teradata component with changed loadTypeUtils and exportUtils
     * 
     */

=======
     * Create an instance of {@link TypeOutputOracleBase }
     * 
     */
    public TypeOutputOracleBase createTypeOutputOracleBase() {
        return new TypeOutputOracleBase();
    }

    /**
     * Create an instance of {@link TypeOutputOracleInSocket }
     * 
     */
    public TypeOutputOracleInSocket createTypeOutputOracleInSocket() {
        return new TypeOutputOracleInSocket();
    }
>>>>>>> partition by Expression component upgraded for expression support

}
