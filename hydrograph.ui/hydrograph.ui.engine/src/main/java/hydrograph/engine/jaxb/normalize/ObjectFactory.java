
package hydrograph.engine.jaxb.normalize;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the hydrograph.engine.jaxb.normalize package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: hydrograph.engine.jaxb.normalize
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TypeInSocket }
     * 
     */
    public TypeInSocket createTypeInSocket() {
        return new TypeInSocket();
    }

    /**
     * Create an instance of {@link TypeOperationInputField }
     * 
     */
    public TypeOperationInputField createTypeOperationInputField() {
        return new TypeOperationInputField();
    }

    /**
     * Create an instance of {@link TypeOperationInputFields }
     * 
     */
    public TypeOperationInputFields createTypeOperationInputFields() {
        return new TypeOperationInputFields();
    }

    /**
     * Create an instance of {@link TypeOutSocket }
     * 
     */
    public TypeOutSocket createTypeOutSocket() {
        return new TypeOutSocket();
    }

    /**
     * Create an instance of {@link TypeOperation }
     * 
     */
    public TypeOperation createTypeOperation() {
        return new TypeOperation();
    }

}