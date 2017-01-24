package hydrograph.engine.spark.components;

import java.util.Properties;

/**
 * Created by sandeepv on 1/14/2017.
 */
public interface RunSQLComponentInt {

    public void testDatabaseConnection(Properties properties)throws Exception;


}