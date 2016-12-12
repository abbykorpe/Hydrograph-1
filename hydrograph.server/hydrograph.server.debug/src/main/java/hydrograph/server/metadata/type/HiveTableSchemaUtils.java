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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.NoSuchObjectException;
import org.apache.hadoop.hive.metastore.api.SerDeInfo;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.server.debug.utilities.Constants;
import hydrograph.server.debug.utilities.ServiceUtilities;
import hydrograph.server.metadata.schema.HiveTableSchema;
import hydrograph.server.metadata.schema.HiveTableSchemaField;

public class HiveTableSchemaUtils {

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

	private static final Logger LOG = LoggerFactory.getLogger(HiveTableSchemaUtils.class);
	HiveConf hiveConf = null;
	HiveTableSchema hiveTableSchema;
	StorageDescriptor storageDescriptor = null;
	Table table;
	boolean isTableExternal;

	public HiveTableSchemaUtils(String databaseName, String tableName) {

		connectToMetastore(databaseName, tableName);

		hiveTableSchema = new HiveTableSchema(databaseName, tableName);
		fillHiveTableSchema();
	}

	private void connectToMetastore(String databaseName, String tableName) {

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
			this.table = client.getTable(databaseName, tableName);
			this.storageDescriptor = table.getSd();
		} catch (MetaException e) {
			throw new RuntimeException(e.getMessage());
		} catch (NoSuchObjectException e) {
			throw new RuntimeException(e.getMessage());
		} catch (TException e) {
			throw new RuntimeException(e.getMessage());
		}

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
		List<HiveTableSchemaField> listOfHiveTableSchemaField = new ArrayList<HiveTableSchemaField>();
		fillHiveTableSchemaFields(columns, listOfHiveTableSchemaField);
		fillHiveTableSchemaFields(partitionKeys, listOfHiveTableSchemaField);
		this.hiveTableSchema.setSchemaFields(listOfHiveTableSchemaField);
	}

	private void fillHiveTableSchemaFields(List<FieldSchema> columns,
			List<HiveTableSchemaField> listOfHiveTableSchemaField) {
		for (FieldSchema fieldSchema : columns) {
			HiveTableSchemaField hiveSchemaField = new HiveTableSchemaField();
			hiveSchemaField = fillHiveTableSchemaField(fieldSchema);
			listOfHiveTableSchemaField.add(hiveSchemaField);
		}
	}

	private HiveTableSchemaField fillHiveTableSchemaField(FieldSchema fieldSchema) {

		HiveTableSchemaField hiveSchemaField = new HiveTableSchemaField();
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

	public HiveTableSchema getHiveTableSchema() {
		return hiveTableSchema;
	}
}
