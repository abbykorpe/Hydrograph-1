/*******************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.server.metadata.type;

import java.io.IOException;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.NoSuchObjectException;
import org.apache.hadoop.hive.metastore.api.SerDeInfo;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.thrift.TException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.server.debug.service.UserPassCallbackHandler;
import hydrograph.server.debug.utilities.Constants;
import hydrograph.server.debug.utilities.ServiceUtilities;
import hydrograph.server.metadata.exception.ParamsCannotBeNullOrEmpty;
import hydrograph.server.metadata.schema.TableSchema;
import hydrograph.server.metadata.schema.TableSchemaField;
import hydrograph.server.metadata.type.base.DataBaseType;

/**
 * 
 * <p>
 * Concrete implementation for to retrieve hive meta store details.
 * </p>
 * 
 * <p>
 * This class required kerberos token for security purpose authentication.
 * </p>
 * 
 * 
 *
 */
public class HiveDatabaseType implements DataBaseType, PrivilegedAction<Object> {
		
	private enum InputOutputFormat {
		PARQUET("parquet"), TEXTDELIMITED("textdelimited"), SEQUENCE("sequence");

		private String name;

		InputOutputFormat(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	};

	private static final Logger LOG = LoggerFactory.getLogger(HiveDatabaseType.class);
	HiveConf hiveConf = null;
	TableSchema hiveTableSchema = null;
	StorageDescriptor storageDescriptor = null;
	Table table;
	boolean isTableExternal;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setConnection(String userId,String password,String host,String port,String sid,String driverType,String database,String tableName) throws ParamsCannotBeNullOrEmpty, JSONException {

		try {
			getKerberosToken(userId, password);
		} catch (LoginException e1) {
			throw new RuntimeException("Unable to login " + e1.getMessage());
		} catch (IOException e1) {
			throw new RuntimeException("Login Fails : " + e1.getMessage());
		}
		this.hiveConf = new HiveConf();
		String pathToHiveSiteXml = ServiceUtilities.getServiceConfigResourceBundle()
				.getString(Constants.HIVE_SITE_CONFIG_PATH);

		if (pathToHiveSiteXml == null || pathToHiveSiteXml.equals("")) {
			LOG.error("Error loading hive-site.xml: Path to hive-site.xml should not be null or empty.");
			throw new RuntimeException(
					"Error loading hive-site.xml: Path to hive-site.xml should not be null or empty.");
		}

		LOG.debug("Loading hive-site.xml: " + pathToHiveSiteXml);
		hiveConf.addResource(new Path(pathToHiveSiteXml));

		HiveMetaStoreClient client = null;
		try {
			client = new HiveMetaStoreClient(hiveConf);
			this.table = client.getTable(database, tableName);
			this.storageDescriptor = table.getSd();
		} catch (MetaException e) {
			throw new RuntimeException(e.getMessage());
		} catch (NoSuchObjectException e) {
			throw new RuntimeException(e.getMessage());
		} catch (TException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	private void getKerberosToken(String userId, String password) throws LoginException, IOException {
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

	}

	/**
	 * Apply the kerberos Token for specified user.
	 * 
	 * @param userId
	 *            - of type String
	 * @param password
	 *            - password of the user
	 * @param conf
	 *            - of type {@link Configuration}.
	 * @throws LoginException
	 *             - Creates while getting Kerberos Token.
	 * @throws IOException
	 *             - throws when login fails.
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
		URL url = HiveDatabaseType.class.getClassLoader().getResource("jaas.conf");
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

	/**
	 * {@inheritDoc}
	 */

	@Override
	public TableSchema fillComponentSchema(String query,String tableName,String database) {

		// hiveTableSchema = new HiveTableSchema(database, tableName);
		hiveTableSchema = new TableSchema();
		hiveTableSchema.setDatabaseName(database);
		hiveTableSchema.setTableName(tableName);
		fillHiveTableSchema();
		hiveTableSchema = getHiveTableSchema();
		return hiveTableSchema;
	}

	private void fillHiveTableSchema() {
		setTableLocation();
		setExternalTable();
		setInputOutputFormat();
		setPartitionKeys();
		setFieldDelimiter();
		setOwner();
		fillFieldSchema();
	}

	private void setExternalTable() {

		if (checkIfhiveTableIsExternal())
			this.hiveTableSchema.setExternalTableLocation(storageDescriptor.getLocation());

	}

	private boolean checkIfhiveTableIsExternal() {

		String hiveWarehouseDir = hiveConf.get("hive.metastore.warehouse.dir");
		if (!storageDescriptor.getLocation().contains(hiveWarehouseDir))
			isTableExternal = true;

		return isTableExternal;

	}

	private void fillFieldSchema() {

		List<FieldSchema> columns = storageDescriptor.getCols();
		List<FieldSchema> partitionKeys = table.getPartitionKeys();
		List<TableSchemaField> listOfHiveTableSchemaField = new ArrayList<TableSchemaField>();
		fillHiveTableSchemaFields(columns, listOfHiveTableSchemaField);
		fillHiveTableSchemaFields(partitionKeys, listOfHiveTableSchemaField);
		this.hiveTableSchema.setSchemaFields(listOfHiveTableSchemaField);
	}

	private void fillHiveTableSchemaFields(List<FieldSchema> columns,
			List<TableSchemaField> listOfHiveTableSchemaField) {
		for (FieldSchema fieldSchema : columns) {
			TableSchemaField hiveSchemaField = new TableSchemaField();
			hiveSchemaField = fillHiveTableSchemaField(fieldSchema);
			listOfHiveTableSchemaField.add(hiveSchemaField);
		}
	}

	private TableSchemaField fillHiveTableSchemaField(FieldSchema fieldSchema) {

		TableSchemaField hiveSchemaField = new TableSchemaField();
		hiveSchemaField.setFieldName(fieldSchema.getName());
		if (fieldSchema.getType().equals("string")) {
			hiveSchemaField.setFieldType("java.lang.String");
		} else if (fieldSchema.getType().equals("int")) {
			hiveSchemaField.setFieldType("java.lang.Integer");
		} else if (fieldSchema.getType().equals("bigint")) {
			hiveSchemaField.setFieldType("java.lang.Long");
		} else if (fieldSchema.getType().equals("smallint")) {
			hiveSchemaField.setFieldType("java.lang.Short");
		} else if (fieldSchema.getType().equals("date")) {
			hiveSchemaField.setFieldType("java.util.Date");
		} else if (fieldSchema.getType().equals("timestamp")) {
			hiveSchemaField.setFieldType("java.util.Date");
		} else if (fieldSchema.getType().equals("double")) {
			hiveSchemaField.setFieldType("java.lang.Double");
		} else if (fieldSchema.getType().equals("boolean")) {
			hiveSchemaField.setFieldType("java.lang.Boolean");
		} else if (fieldSchema.getType().equals("float")) {
			hiveSchemaField.setFieldType("java.lang.Float");
		} else if (fieldSchema.getType().contains("decimal")) {
			hiveSchemaField.setFieldType("java.math.BigDecimal");
			hiveSchemaField.setScale(getScale(fieldSchema.getType()));
			hiveSchemaField.setPrecision(getPrecision(fieldSchema.getType()));
		}
		return hiveSchemaField;
	}

	private String getScalePrecision(String typeWithScale, int index) {

		String pattern = "decimal\\((\\d+),(\\d+)\\)";
		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(typeWithScale);
		if (m.find()) {
			return m.group(index);
		} else {
			return "-999";
		}
	}

	private String getScale(String typeWithScale) {
		return getScalePrecision(typeWithScale, 2);
	}

	private String getPrecision(String typeWithPrecision) {
		return getScalePrecision(typeWithPrecision, 1);
	}

	private void setPartitionKeys() {

		List<String> listOfPartitionKeys = new ArrayList<String>();
		for (FieldSchema fieldSchema : table.getPartitionKeys()) {
			listOfPartitionKeys.add(fieldSchema.getName());
		}
		this.hiveTableSchema.setPartitionKeys(listOfPartitionKeys.toString().replace("[", "").replace("]", ""));
	}

	private void setInputOutputFormat() {

		if (storageDescriptor.getInputFormat().contains("parquet")) {
			this.hiveTableSchema.setInputOutputFormat(InputOutputFormat.PARQUET.getName());
		} else if (storageDescriptor.getInputFormat().contains("sequence")) {
			this.hiveTableSchema.setInputOutputFormat(InputOutputFormat.SEQUENCE.getName());
		} else {
			this.hiveTableSchema.setInputOutputFormat(InputOutputFormat.TEXTDELIMITED.getName());
		}
	}

	private void setTableLocation() {

		this.hiveTableSchema.setLocation(storageDescriptor.getLocation());
	}

	private void setFieldDelimiter() {
		SerDeInfo serDeInfo = storageDescriptor.getSerdeInfo();
		this.hiveTableSchema.setFieldDelimiter(serDeInfo.getParameters().get("field.delim"));

	}

	private void setOwner() {

		this.hiveTableSchema.setOwner(table.getOwner());
	}

	public TableSchema getHiveTableSchema() {
		return hiveTableSchema;
	}

	@Override
	public Object run() {
		return null;
	}
}
