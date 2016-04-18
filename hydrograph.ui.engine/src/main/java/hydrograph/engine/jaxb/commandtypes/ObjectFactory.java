
package hydrograph.engine.jaxb.commandtypes;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the hydrograph.engine.jaxb.commandtypes package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: hydrograph.engine.jaxb.commandtypes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Hplsql }
     * 
     */
    public Hplsql createHplsql() {
        return new Hplsql();
    }

    /**
     * Create an instance of {@link Hplsql.Execute }
     * 
     */
    public Hplsql.Execute createHplsqlExecute() {
        return new Hplsql.Execute();
    }

    /**
     * Create an instance of {@link FtpIn }
     * 
     */
    public FtpIn createFtpIn() {
        return new FtpIn();
    }

    /**
     * Create an instance of {@link Subgraph }
     * 
     */
    public Subgraph createSubgraph() {
        return new Subgraph();
    }

    /**
     * Create an instance of {@link RunProgram }
     * 
     */
    public RunProgram createRunProgram() {
        return new RunProgram();
    }

    /**
     * Create an instance of {@link Hplsql.Command }
     * 
     */
    public Hplsql.Command createHplsqlCommand() {
        return new Hplsql.Command();
    }

    /**
     * Create an instance of {@link Hplsql.Execute.Query }
     * 
     */
    public Hplsql.Execute.Query createHplsqlExecuteQuery() {
        return new Hplsql.Execute.Query();
    }

    /**
     * Create an instance of {@link Hplsql.Execute.Uri }
     * 
     */
    public Hplsql.Execute.Uri createHplsqlExecuteUri() {
        return new Hplsql.Execute.Uri();
    }

    /**
     * Create an instance of {@link FtpIn.Host }
     * 
     */
    public FtpIn.Host createFtpInHost() {
        return new FtpIn.Host();
    }

    /**
     * Create an instance of {@link Subgraph.Path }
     * 
     */
    public Subgraph.Path createSubgraphPath() {
        return new Subgraph.Path();
    }

    /**
     * Create an instance of {@link RunProgram.Command }
     * 
     */
    public RunProgram.Command createRunProgramCommand() {
        return new RunProgram.Command();
    }

}