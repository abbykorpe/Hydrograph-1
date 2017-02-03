/*******************************************************************************
 *  Copyright 2016, 2017 Capital One Services, LLC and Bitwise, Inc.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package hydrograph.server.utilities;

import hydrograph.server.metadata.exception.ParamsCannotBeNullOrEmpty;
import hydrograph.server.service.HydrographService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The utility class for the service. This class is meant to be stateless and
 * provide just static utility methods. There should not be any instance
 * variables of this class. The constructor has deliberately been made private
 * in order to discourage users from creating instance of this class
 *
 * @author Prabodh
 */
public class ServiceUtilities {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceUtilities.class);
    private static String SERVICE_CONFIG = "ServiceConfig";

    /**
     *
     */
    private ServiceUtilities() {

    }

    /**
     * Returns the {@link ResourceBundle} object for the service config file
     *
     * @return the {@link ResourceBundle} object for the service config file
     */
    public static ResourceBundle getServiceConfigResourceBundle() {
        return getResourceBundle(SERVICE_CONFIG);
    }

    private static ResourceBundle getResourceBundle(String resource) {
        ResourceBundle resBundl = ResourceBundle.getBundle(SERVICE_CONFIG);

        // Try to get ServiceConfig.properties file from the config folder of
        // the project
        // Can also use the default one provided in the package.
        String basePath = HydrographService.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        basePath = basePath.substring(0, basePath.lastIndexOf("/"));
        basePath = basePath.substring(0, basePath.lastIndexOf("/")) + "/config/";

        LOG.info("Base Path:" + basePath);

        File file = new File(basePath);

        LOG.info("FILE PATH: " + basePath);

        if (file.exists()) {
            LOG.info("Using ServiceConfig from: " + basePath);

            try {
                URL[] resourceURLs = {file.toURI().toURL()};
                URLClassLoader urlLoader = new URLClassLoader(resourceURLs);
                resBundl = ResourceBundle.getBundle("ServiceConfigOverride", Locale.getDefault(), urlLoader);
                LOG.info("PORT:" + resBundl.getString(Constants.PORT_ID));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return resBundl;
    }

    /**
     * Used to set the connection for
     * <p>
     * //@param connectionProperties - contain request params details
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public static boolean getConnectionStatus(Map metadataOperationProperteies, String jdbcClassName,
                                              String queryToTest) throws Exception {
        String jdbcUrl = null;
        String dbType = metadataOperationProperteies
                .getOrDefault(Constants.dbType,
                        new ParamsCannotBeNullOrEmpty(Constants.DRIVER_TYPE + " not found in request parameter"))
                .toString();
        String driverType = metadataOperationProperteies
                .getOrDefault(Constants.DRIVER_TYPE,
                        new ParamsCannotBeNullOrEmpty(Constants.DRIVER_TYPE + " not found in request parameter"))
                .toString();
        String host = metadataOperationProperteies
                .getOrDefault(Constants.HOST_NAME,
                        new ParamsCannotBeNullOrEmpty(Constants.HOST_NAME + " not found in request parameter"))
                .toString();
        String port = metadataOperationProperteies
                .getOrDefault(Constants.PORT_NUMBER,
                        new ParamsCannotBeNullOrEmpty(Constants.PORT_NUMBER + " not found in request parameter"))
                .toString();
        String userId = metadataOperationProperteies
                .getOrDefault(Constants.USERNAME,
                        new ParamsCannotBeNullOrEmpty(Constants.USERNAME + " not found in request parameter"))
                .toString();
        String password = metadataOperationProperteies
                .getOrDefault(Constants.PASSWORD,
                        new ParamsCannotBeNullOrEmpty(Constants.PASSWORD + " not found in request parameter"))
                .toString();
        switch (dbType.toLowerCase()) {
            case Constants.ORACLE:
                String sid = metadataOperationProperteies
                        .getOrDefault(Constants.SID,
                                new ParamsCannotBeNullOrEmpty(Constants.SID + " Not found in request parameter"))
                        .toString();
                jdbcUrl = "jdbc:oracle:" + driverType + "://@" + host + ":" + port + ":" + sid;
                break;
            case Constants.MYSQL:
                String database = metadataOperationProperteies
                        .getOrDefault(Constants.DATABASE_NAME,
                                new ParamsCannotBeNullOrEmpty(Constants.DATABASE_NAME + " not found in request parameter"))
                        .toString();
                jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
                break;
            case Constants.REDSHIFT:
                String databasename = metadataOperationProperteies
                        .getOrDefault(Constants.DATABASE_NAME,
                                new ParamsCannotBeNullOrEmpty(Constants.DATABASE_NAME + " not found in request parameter"))
                        .toString();
                jdbcUrl = "jdbc:redshift://" + host + ":" + port + "/" + databasename;
                break;
            case Constants.TERADATA:
                String database_teradata = metadataOperationProperteies
                        .getOrDefault(Constants.DATABASE_NAME,
                                new ParamsCannotBeNullOrEmpty(Constants.DATABASE_NAME + " not found in request parameter"))
                        .toString();

                jdbcUrl = "jdbc:teradata://" + host + "/" + "DBS_PORT" + "=" + port + "," + "DATABASE" + "=" + database_teradata;
                break;
        }
        Class.forName(jdbcClassName);
        Connection connectionObject;
        try {
            connectionObject = DriverManager.getConnection(jdbcUrl, userId, password);
        } catch (SQLException e) {
            LOG.error("Error while connecting to database ", e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }
        return testConnection(connectionObject, queryToTest);
    }

    /**
     * Test Validity of a Jdbc Connection
     * <p>
     * //@param  JDBC connection object
     *
     * @return true if a given connection object is a valid one; otherwise
     * return false.
     * @throws SQLException
     */
    private static boolean testConnection(Connection connectionObject, String queryToTest) throws SQLException {

        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = connectionObject.createStatement();
            if (stmt == null) {
                return false;
            }

            rs = stmt.executeQuery(queryToTest);
            if (rs == null) {
                return false;
            }

            // connection object is valid: you were able to
            // connect to the database and return something useful.
            if (rs.next()) {
                return true;
            }

            // there is no hope any more for the validity
            // of the Connection object
            return false;
        } catch (Exception e) {
            // Something went wrong: connection is bad
            LOG.error("Unable to make Connection to Oracle Database" + e.getMessage());
            return false;
        } finally {
            // close database resources
            rs.close();
            stmt.close();
        }
    }

}
