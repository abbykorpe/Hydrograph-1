/*******************************************************************************
 *  Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
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
package hydrograph.server.debug.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.PrivilegedAction;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathNotFoundException;
import org.apache.hadoop.security.UserGroupInformation;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import cascading.lingual.type.SQLTimestampCoercibleType;
import hydrograph.server.debug.antlr.parser.QueryParserLexer;
import hydrograph.server.debug.antlr.parser.QueryParserParser;
import hydrograph.server.debug.lingual.LingualFilter;
import hydrograph.server.debug.lingual.json.RemoteFilterJson;
import hydrograph.server.debug.lingual.querygenerator.LingualQueryCreator;
import hydrograph.server.debug.utilities.Constants;
import hydrograph.server.debug.utilities.ServiceUtilities;
import hydrograph.server.metadata.exception.ParamsCannotBeNullOrEmpty;
import hydrograph.server.metadata.exception.TableOrQueryParamNotFound;
import hydrograph.server.metadata.helper.HiveMetadataHelper;
import hydrograph.server.metadata.helper.OracleMetadataHelper;
import hydrograph.server.metadata.helper.RedShiftMetadataHelper;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * @author Bitwise
 */
public class DebugService implements PrivilegedAction<Object> {
	private static final Logger LOG = LoggerFactory.getLogger(DebugService.class);

	public DebugService() {
	}

	private void start() {
		int portNumber = Constants.DEFAULT_PORT_NUMBER;
		try {
			portNumber = Integer
					.parseInt(ServiceUtilities.getServiceConfigResourceBundle().getString(Constants.PORT_ID));
			LOG.debug("Port number '" + portNumber + "' fetched from properties file");
		} catch (Exception e) {
			LOG.error("Error fetching port number. Defaulting to " + Constants.DEFAULT_PORT_NUMBER, e);
		}
		Spark.setPort(portNumber);

		Spark.post("readFromMetastore", new Route() {

			@Override
			public Object handle(Request request, Response response)
					throws ParamsCannotBeNullOrEmpty, ClassNotFoundException, IllegalAccessException, JSONException,
					JsonProcessingException, TableOrQueryParamNotFound, SQLException, InstantiationException {
				LOG.info("************************readFromMetastore endpoint - started************************");
				LOG.info("+++ Start: " + new Timestamp((new Date()).getTime()));
				String objectAsString = "", dbClassName = "", dbType = null, userId = null, password = null,
						host = null, port = null, sid = null, driverType = null, query = null, tableName = null,
						database = null;
				ObjectMapper objectMapper = new ObjectMapper();
				String requestParameters = request.queryParams(Constants.REQUEST_PARAMETERS);
				JSONObject requestParameterValues = new JSONObject(requestParameters);
				if (!requestParameterValues.isNull(Constants.dbType)) {
					dbType = requestParameterValues.getString(Constants.dbType);
				}
				if (!requestParameterValues.isNull(Constants.USERNAME)) {
					userId = requestParameterValues.getString(Constants.USERNAME);
				}
				if (!requestParameterValues.isNull(Constants.PASSWORD)) {
					password = requestParameterValues.getString(Constants.PASSWORD);
				}
				if (!requestParameterValues.isNull(Constants.HOST_NAME)) {
					host = requestParameterValues.getString(Constants.HOST_NAME);
				}
				if (!requestParameterValues.isNull(Constants.PORT_NUMBER)) {
					port = requestParameterValues.getString(Constants.PORT_NUMBER);
				}
				if (!requestParameterValues.isNull(Constants.SID)) {
					sid = requestParameterValues.getString(Constants.SID);
				}
				if (!requestParameterValues.isNull(Constants.DRIVER_TYPE)) {
					driverType = requestParameterValues.getString(Constants.DRIVER_TYPE);
				}
				if (!requestParameterValues.isNull(Constants.QUERY)) {
					query = requestParameterValues.getString(Constants.QUERY);
				}
				if (!requestParameterValues.isNull(Constants.TABLENAME)) {
					tableName = requestParameterValues.getString(Constants.TABLENAME);
				}
				if (!requestParameterValues.isNull(Constants.DATABASE_NAME)) {
					database = requestParameterValues.getString(Constants.DATABASE_NAME);
				}
				LOG.debug("Fetched request parameters are: " + Constants.dbType + " => " + dbType + " "
						+ Constants.USERNAME + " => " + userId + " " + Constants.HOST_NAME + " => " + host + " "
						+ Constants.PORT_NUMBER + " => " + port + " " + Constants.SID + " => " + sid + " "
						+ Constants.DRIVER_TYPE + " => " + driverType + " " + Constants.QUERY + " => " + query + " "
						+ Constants.TABLENAME + " => " + tableName + " " + Constants.DATABASE_NAME + " => " + database
						+ " ");

				if (dbType != null && !dbType.isEmpty()) {
					try {
						Object dbClass;
						switch (dbType.toLowerCase()) {
						case Constants.ORACLE:
							dbClassName = Constants.oracle;
							if ((dbClass = Class.forName(dbClassName).newInstance()) instanceof OracleMetadataHelper) {
								OracleMetadataHelper oracleMetadataHelper = (OracleMetadataHelper) dbClass;
								oracleMetadataHelper.setConnection(userId, password, host, port, sid, driverType);
								objectAsString = objectMapper
										.writeValueAsString(oracleMetadataHelper.fillComponentSchema(query, tableName));
								LOG.info("+++ Stop: " + new Timestamp((new Date()).getTime()));
							} else {
								LOG.debug("Invalid " + dbClass + " name supplied");
								response.status(400);
								return "Invalid " + dbClass + " name supplied";
							}
							break;
						case Constants.HIVE:
							dbClassName = Constants.hive;
							if ((dbClass = Class.forName(dbClassName).newInstance()) instanceof HiveMetadataHelper) {
								HiveMetadataHelper hiveMetadataHelper = (HiveMetadataHelper) dbClass;
								hiveMetadataHelper.setConnection(userId, password,database,tableName);
								objectAsString = objectMapper
										.writeValueAsString(hiveMetadataHelper.fillComponentSchema(tableName,database));
								LOG.info("+++ Stop: " + new Timestamp((new Date()).getTime()));
							} else {
								LOG.debug("Invalid " + dbClass + " name supplied");
								response.status(400);
								return "Invalid " + dbClass + " name supplied";
							}
							break;
						case Constants.REDSHIFT:
							dbClassName = Constants.redshift;
							if ((dbClass = Class.forName(dbClassName)
									.newInstance()) instanceof RedShiftMetadataHelper) {
								RedShiftMetadataHelper redShiftMetadataHelper = (RedShiftMetadataHelper) dbClass;
								redShiftMetadataHelper.setConnection(userId, password, host, port, database);
								objectAsString = objectMapper.writeValueAsString(
										redShiftMetadataHelper.fillComponentSchema(query, tableName));
								LOG.info("+++ Stop: " + new Timestamp((new Date()).getTime()));
							} else {
								LOG.debug("Invalid " + dbClass + " name supplied");
								response.status(400);
								return "Invalid " + dbClass + " name supplied";
							}
							break;
						}
					} catch (Exception e) {
						LOG.error("Metadata read for database helper: " + dbType + " not supported." + e);
						response.status(400);
						return "Metadata read for database helper: " + dbType + " not supported.";
					}
					LOG.info("Class Name used for " + dbType + " is : " + dbClassName);
				} else {
					throw new ParamsCannotBeNullOrEmpty("dbType cannot be null or empty!");
				}
				return objectAsString;
			}
		});

		Spark.post("/read", new Route() {
			@Override
			public Object handle(Request request, Response response) {
				LOG.info("************************read endpoint - started************************");
				LOG.info("+++ Start: " + new Timestamp((new Date()).getTime()));
				String jobId = request.queryParams(Constants.JOB_ID);
				String componentId = request.queryParams(Constants.COMPONENT_ID);
				String socketId = request.queryParams(Constants.SOCKET_ID);
				String basePath = request.queryParams(Constants.BASE_PATH);

				// String host = request.queryParams(Constants.HOST);
				String userID = request.queryParams(Constants.USER_ID);
				String password = request.queryParams(Constants.PASSWORD);

				double sizeOfData = Double.parseDouble(request.queryParams(Constants.FILE_SIZE)) * 1024 * 1024;
				LOG.info("Base Path: {}, Job Id: {}, Component Id: {}, Socket ID: {}, User ID:{}, DataSize:{}",
						new Object[] { basePath, jobId, componentId, socketId, userID, sizeOfData });

				String batchID = jobId + "_" + componentId + "_" + socketId;
				String tempLocationPath = ServiceUtilities.getServiceConfigResourceBundle()
						.getString(Constants.TEMP_LOCATION_PATH);
				String filePath = tempLocationPath + "/" + batchID + ".csv";
				try {
					readFileFromHDFS(basePath + "/debug/" + jobId + "/" + componentId + "_" + socketId, sizeOfData,
							filePath, userID, password);
					LOG.info("+++ Stop: " + new Timestamp((new Date()).getTime()));
				} catch (Exception e) {
					LOG.error("Error in reading debug files", e);
					return "error";
				}
				return filePath;
			}

			/**
			 * This method will read the HDFS file, fetch the records from it
			 * and write its records to a local file on edge node with size <=
			 * {@code sizeOfData} passed in parameter.
			 *
			 * @param hdfsFilePath
			 *            path of HDFS file from where records to be read
			 * @param sizeOfData
			 *            defines the size of data (in bytes) to be read from
			 *            HDFS file
			 * @param remoteFileName
			 *            after reading the data of {@code sizeOfData} bytes
			 *            from HDFS file, it will be written to local file on
			 *            edge node with file name {@code remoteFileName}
			 * @param userId
			 * @param password
			 *
			 */
			private void readFileFromHDFS(String hdfsFilePath, double sizeOfData, String remoteFileName, String userId,
					String password) {
				try {
					Path path = new Path(hdfsFilePath);
					LOG.debug("Reading Debug file:" + hdfsFilePath);
					Configuration conf = new Configuration();

					// load hdfs-site.xml and core-site.xml
					String hdfsConfigPath = ServiceUtilities.getServiceConfigResourceBundle()
							.getString(Constants.HDFS_SITE_CONFIG_PATH);
					String coreSiteConfigPath = ServiceUtilities.getServiceConfigResourceBundle()
							.getString(Constants.CORE_SITE_CONFIG_PATH);
					LOG.debug("Loading hdfs-site.xml:" + hdfsConfigPath);
					conf.addResource(new Path(hdfsConfigPath));
					LOG.debug("Loading hdfs-site.xml:" + coreSiteConfigPath);
					conf.addResource(new Path(coreSiteConfigPath));

					// apply kerberos token
					applyKerberosToken(userId, password, conf);

					listAndWriteFiles(remoteFileName, path, conf, sizeOfData);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			/**
			 * This method will list all files for {@code path}, read all files
			 * and writes its data to a local file on edge node with size <=
			 * {@code sizeOfData} passed in parameter.
			 *
			 * @param remoteFileName
			 * @param path
			 * @param conf
			 * @param sizeOfData
			 * @throws IOException
			 * @throws FileNotFoundException
			 */
			private void listAndWriteFiles(String remoteFileName, Path path, Configuration conf, double sizeOfData)
					throws IOException, FileNotFoundException {
				FileSystem fs = FileSystem.get(conf);
				FileStatus[] status = fs.listStatus(path);
				File remoteFile = new File(remoteFileName);

				OutputStream os = new FileOutputStream(remoteFileName);
				try {

					int numOfBytes = 0;
					for (int i = 0; i < status.length; i++) {
						BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(status[i].getPath())));
						String line = "";
						line = br.readLine();
						if (line != null) {
							// header will only get fetch from first part file
							// and it
							// will skip header from remaining files
							if (numOfBytes == 0) {
								os.write((line + "\n").toString().getBytes());
								numOfBytes += line.toString().length();
							}
							while ((line = br.readLine()) != null) {
								numOfBytes += line.toString().length();
								// line = br.readLine();
								if (numOfBytes <= sizeOfData) {
									os.write((line + "\n").toString().getBytes());
								} else {
									break;
								}
							}
						}
						br.close();
						remoteFile.setReadable(true, false);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					os.close();
					fs.close();
				}
			}

		});

		Spark.post("/delete", new Route() {
			@Override
			public Object handle(Request request, Response response) {
				LOG.info("************************delete endpoint - started************************");
				LOG.info("+++ Start: " + new Timestamp((new Date()).getTime()));
				response.type("text/json");
				String jobId = request.queryParams(Constants.JOB_ID);
				String basePath = request.queryParams(Constants.BASE_PATH);
				String componentId = request.queryParams(Constants.COMPONENT_ID);
				String socketId = request.queryParams(Constants.SOCKET_ID);
				String userID = request.queryParams(Constants.USER_ID);
				String password = request.queryParams(Constants.PASSWORD);

				LOG.info("Base Path: {}, Job Id: {}, Component Id: {}, Socket ID: {}, User ID:{}",
						new Object[] { basePath, jobId, componentId, socketId, userID });

				try {
					removeDebugFiles(basePath, jobId, componentId, socketId, userID, password);
					LOG.info("+++ Stop: " + new Timestamp((new Date()).getTime()));
				} catch (Exception e) {
					LOG.error("Error in deleting debug files", e);
				}
				return "error";
			}

			private void removeDebugFiles(String basePath, String jobId, String componentId, String socketId,
					String userID, String password) {
				try {
					// DebugFilesReader debugFilesReader = new
					// DebugFilesReader(basePath, jobId, componentId, socketId,
					// userID,
					// password);
					delete(basePath, jobId, componentId, socketId, userID, password);
				} catch (Exception e) {
					LOG.error("Error while deleting the debug file", e);
					throw new RuntimeException(e);
				}
			}

			/**
			 * Deletes the jobId directory
			 *
			 * @param password
			 * @param userID
			 * @param socketId
			 * @param componentId
			 * @param jobId
			 * @param basePath
			 *
			 * @throws IOException
			 */
			public void delete(String basePath, String jobId, String componentId, String socketId, String userID,
					String password) throws IOException {
				LOG.trace("Entering method delete()");
				String deletePath = basePath + "/debug/" + jobId;
				Configuration configuration = new Configuration();
				FileSystem fileSystem = FileSystem.get(configuration);
				Path deletingFilePath = new Path(deletePath);
				if (!fileSystem.exists(deletingFilePath)) {
					throw new PathNotFoundException(deletingFilePath.toString());
				} else {
					// Delete file
					fileSystem.delete(deletingFilePath, true);
					LOG.info("Deleted path : " + deletePath);
				}
				fileSystem.close();
			}
		});

		Spark.post("/deleteLocalDebugFile", new Route() {
			@Override
			public Object handle(Request request, Response response) {
				String error = "";
				LOG.info("+++ Start: " + new Timestamp((new Date()).getTime()));
				LOG.info("************************deleteLocalDebugFile endpoint - started************************");
				try {
					String jobId = request.queryParams(Constants.JOB_ID);
					String componentId = request.queryParams(Constants.COMPONENT_ID);
					String socketId = request.queryParams(Constants.SOCKET_ID);
					String batchID = jobId + "_" + componentId + "_" + socketId;
					String tempLocationPath = ServiceUtilities.getServiceConfigResourceBundle()
							.getString(Constants.TEMP_LOCATION_PATH);

					LOG.info("Job Id: {}, Component Id: {}, Socket ID: {}, TemporaryPath: {}",
							new Object[] { jobId, componentId, socketId, tempLocationPath });
					LOG.debug("File to be deleted: " + tempLocationPath + "/" + batchID + ".csv");
					File file = new File(tempLocationPath + "/" + batchID + ".csv");
					file.delete();
					LOG.trace("Local debug file deleted successfully.");
					return "Success";
				} catch (Exception e) {
					LOG.error("Error in deleting local debug file.", e);
					error = e.getMessage();
				}
				LOG.info("+++ Stop: " + new Timestamp((new Date()).getTime()));
				return "Local file delete failed. Error: " + error;
			}
		});

		// TODO : Keep this for test
		Spark.post("/post", new Route() {

			@Override
			public Object handle(Request request, Response response) {
				response.type("text/json");
				return "calling post...";
			}
		});

		// TODO : Keep this for test
		Spark.get("/test", new Route() {

			@Override
			public Object handle(Request request, Response response) {
				response.type("text/json");
				response.status(200);
				response.body("Test successful!");
				return "Test successful!";
			}
		});

		Spark.post("/filter", new Route() {
			@Override
			public Object handle(Request request, Response response) {

				LOG.info("************************filter - started************************");
				LOG.info("+++ Start: " + new Timestamp((new Date()).getTime()));

				Gson gson = new Gson();
				String json = request.queryParams(Constants.REQUEST_PARAMETERS);
				RemoteFilterJson remoteFilterJson = gson.fromJson(json, RemoteFilterJson.class);

				String jobId = remoteFilterJson.getJobDetails().getUniqueJobID();
				String componentId = remoteFilterJson.getJobDetails().getComponentID();
				String socketId = remoteFilterJson.getJobDetails().getComponentSocketID();
				String basePath = remoteFilterJson.getJobDetails().getBasepath();
				String username = remoteFilterJson.getJobDetails().getUsername();
				String password = remoteFilterJson.getJobDetails().getPassword();
				double outputFileSizeInMB = remoteFilterJson.getFileSize();
				double sizeOfDataInByte = outputFileSizeInMB * 1024 * 1024;

				String condition = parseSQLQueryToLingualQuery(remoteFilterJson);

				LOG.info("Base Path: {}, Job Id: {}, Component Id: {}, Socket ID: {}, User ID:{}, DataSize:{}",
						new Object[] { basePath, jobId, componentId, socketId, username, sizeOfDataInByte });

				String batchID = jobId + "_" + componentId + "_" + socketId;

				String tempLocationPath = ServiceUtilities.getServiceConfigResourceBundle()
						.getString(Constants.TEMP_LOCATION_PATH);

				String filePath = tempLocationPath + "/" + batchID + ".csv";
				String UUID = generateUUID();
				String uniqueId = batchID + "_" + UUID;
				String linugalMetaDataPath = basePath + "/filter/" + UUID;

				String fieldNames[] = getHeader(basePath + "/debug/" + jobId + "/" + componentId + "_" + socketId,
						username, password);
				try {
					HashMap<String, Type> fieldNameAndDatatype = getFieldNameAndType(remoteFilterJson);
					Type[] fieldTypes = getFieldTypeFromMap(fieldNames, fieldNameAndDatatype);
					Configuration conf = getConfiguration(username, password);

					new LingualFilter().filterData(linugalMetaDataPath, uniqueId,
							basePath + "/debug/" + jobId + "/" + componentId + "_" + socketId, sizeOfDataInByte,
							filePath, condition, fieldNames, fieldTypes, conf);

					LOG.info("debug output path : " + filePath);
					LOG.info("+++ Stop: " + new Timestamp((new Date()).getTime()));
				} catch (Exception e) {
					LOG.error("Error in reading debug files", e);
					return "error";
				} finally {
					try {
						System.gc();
						deleteLingualResult(linugalMetaDataPath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return filePath;
			}

			private Type[] getFieldTypeFromMap(String[] fieldNames, HashMap<String, Type> fieldNameAndDatatype) {
				Type[] type = new Type[fieldNameAndDatatype.size()];
				int i = 0;
				for (String eachFieldName : fieldNames) {
					type[i++] = fieldNameAndDatatype.get(eachFieldName);
				}
				return type;
			}

			private String[] getHeader(String path, String username, String password) {
				String[] header = readFile(path, username, password);
				return header;
			}

			private String[] readFile(String hdfsFilePath, String username, String password) {
				String[] header = null;
				try {
					Path path = new Path(hdfsFilePath);
					LOG.debug("Reading Debug file:" + hdfsFilePath);
					Configuration conf = new Configuration();

					// load hdfs-site.xml and core-site.xml
					String hdfsConfigPath = ServiceUtilities.getServiceConfigResourceBundle()
							.getString(Constants.HDFS_SITE_CONFIG_PATH);
					String coreSiteConfigPath = ServiceUtilities.getServiceConfigResourceBundle()
							.getString(Constants.CORE_SITE_CONFIG_PATH);
					LOG.debug("Loading hdfs-site.xml:" + hdfsConfigPath);
					conf.addResource(new Path(hdfsConfigPath));
					LOG.debug("Loading hdfs-site.xml:" + coreSiteConfigPath);
					conf.addResource(new Path(coreSiteConfigPath));

					// apply kerberos token
					applyKerberosToken(username, password, conf);

					header = getHeaderArray(path, conf);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return header;
			}

			private Path filterOutSuccessFile(FileStatus[] fileStatus) {
				for (FileStatus status : fileStatus) {
					if (status.getPath().getName().toUpperCase().contains("_SUCCESS"))
						continue;
					else
						return status.getPath();
				}
				return null;
			}

			private String[] getHeaderArray(Path path, Configuration conf) throws IOException {
				FileSystem fs = FileSystem.get(conf);
				FileStatus[] status = fs.listStatus(path);
				String line = "";
				try {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(fs.open(filterOutSuccessFile(status))));

					line = br.readLine();
					br.close();

				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					fs.close();
				}
				return line.split(",");
			}

			private Configuration getConfiguration(String userId, String password) throws LoginException, IOException {
				Configuration conf = new Configuration();

				// load hdfs-site.xml and core-site.xml
				String hdfsConfigPath = ServiceUtilities.getServiceConfigResourceBundle()
						.getString(Constants.HDFS_SITE_CONFIG_PATH);
				String coreSiteConfigPath = ServiceUtilities.getServiceConfigResourceBundle()
						.getString(Constants.CORE_SITE_CONFIG_PATH);
				LOG.debug("Loading hdfs-site.xml:" + hdfsConfigPath);
				conf.addResource(new Path(hdfsConfigPath));
				LOG.debug("Loading hdfs-site.xml:" + coreSiteConfigPath);
				conf.addResource(new Path(coreSiteConfigPath));

				// apply kerberos token
				applyKerberosToken(userId, password, conf);
				return conf;
			}

			private void deleteLingualResult(String deletePath) throws IOException {

				Configuration configuration = new Configuration();
				FileSystem fileSystem = FileSystem.get(configuration);
				Path deletingFilePath = new Path(deletePath);

				try {
					if (!fileSystem.exists(deletingFilePath)) {
						throw new PathNotFoundException(deletingFilePath.toString());
					} else {
						boolean isDeleted = fileSystem.delete(deletingFilePath, true);
						if (isDeleted) {
							fileSystem.deleteOnExit(deletingFilePath);
						}
						LOG.info("Deleted path : " + deletePath);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				fileSystem.close();
			}

			private String generateUUID() {
				return String.valueOf(UUID.randomUUID());
			}

			private String parseSQLQueryToLingualQuery(RemoteFilterJson remoteFilterJson) {
				ANTLRInputStream stream = new ANTLRInputStream(remoteFilterJson.getCondition());
				QueryParserLexer lexer = new QueryParserLexer(stream);
				CommonTokenStream tokenStream = new CommonTokenStream(lexer);
				QueryParserParser parser = new QueryParserParser(tokenStream);
				parser.removeErrorListeners();
				LingualQueryCreator customVisitor = new LingualQueryCreator(remoteFilterJson.getSchema());
				String condition = customVisitor.visit(parser.eval());
				return condition;
			}

			private HashMap<String, Type> getFieldNameAndType(RemoteFilterJson remoteFilterJson)
					throws ClassNotFoundException {
				HashMap<String, Type> fieldDataTypeMap = new HashMap<>();
				Type type;
				for (int i = 0; i < remoteFilterJson.getSchema().size(); i++) {
					Class clazz = Class.forName(remoteFilterJson.getSchema().get(i).getDataTypeValue());
					if (clazz.getSimpleName().toString().equalsIgnoreCase("Date")) {
						type = new SQLTimestampCoercibleType();
					} else {
						type = clazz;
					}
					fieldDataTypeMap.put(remoteFilterJson.getSchema().get(i).getFieldName(), type);
				}
				return fieldDataTypeMap;
			}

		});
	}

	public static void main(String[] args) {
		DebugService service = new DebugService();
		service.start();
	}

	/**
	 * @param userId
	 * @param password
	 * @param conf
	 * @throws LoginException
	 * @throws IOException
	 */
	private void applyKerberosToken(String userId, String password, Configuration conf)
			throws LoginException, IOException {
		String enableKerberos = ServiceUtilities.getServiceConfigResourceBundle().getString(Constants.ENABLE_KERBEROS);
		if (Boolean.parseBoolean(enableKerberos)) {
			LOG.debug("Kerberos is enabled. Kerberos ticket will be generated for user: " + userId);
			if (ServiceUtilities.getServiceConfigResourceBundle().containsKey(Constants.KERBEROS_DOMAIN_NAME)) {
				LOG.debug("Kerberos domain name is set in config. UserID will be updated with the domain name.");
				String kerberosDomainName = ServiceUtilities.getServiceConfigResourceBundle()
						.getString(Constants.KERBEROS_DOMAIN_NAME);
				kerberosDomainName = kerberosDomainName.equals("") ? "" : "@" + kerberosDomainName;
				userId = userId + kerberosDomainName;
				LOG.debug("Updated userId: " + userId);
			}
			getKerberosToken(userId, password.toCharArray(), conf);
		}
	}

	private void getKerberosToken(String user, char[] password, Configuration configuration)
			throws LoginException, IOException {
		LOG.trace("Entering method getKerberosToken() for user: " + user);
		URL url = DebugService.class.getClassLoader().getResource("jaas.conf");
		System.setProperty("java.security.auth.login.config", url.toExternalForm());

		LOG.info("Generating Kerberos ticket for user: " + user);
		UserGroupInformation.setConfiguration(configuration);

		LoginContext lc = new LoginContext("EntryName", new UserPassCallbackHandler(user, password));
		lc.login();

		Subject subject = lc.getSubject();
		UserGroupInformation.loginUserFromSubject(subject);
		Subject.doAs(subject, this);
		LOG.info("Kerberos ticket successfully generated for user: " + user);
	}

	@Override
	public Object run() {
		LOG.trace("Entering method run()");
		return null;
	}

	private class UnableToLoadPropertiesException extends RuntimeException {
		private static final long serialVersionUID = 4031525765978790699L;

		public UnableToLoadPropertiesException(Throwable e) {
			super(e);
		}
	}
}