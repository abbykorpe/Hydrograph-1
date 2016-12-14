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
package hydrograph.engine.cascading.assembly;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import cascading.jdbc.JDBCUtil;
import cascading.tap.TapException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowDef;
import cascading.jdbc.JDBCScheme;
import cascading.jdbc.JDBCTap;
import cascading.jdbc.TableDesc;
import cascading.jdbc.db.DBInputFormat;
import cascading.jdbc.db.DBOutputFormat;
import cascading.pipe.Pipe;
import cascading.tap.SinkMode;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.InputOutputFieldsAndTypesCreator;
import hydrograph.engine.cascading.assembly.utils.JavaToSQLTypeMapping;
import hydrograph.engine.core.component.entity.OutputRDBMSEntity;
import hydrograph.engine.core.component.entity.elements.SchemaField;
import hydrograph.engine.jaxb.commontypes.TypeFieldName;


public abstract class OutputRDBMSAssembly extends BaseComponent<OutputRDBMSEntity> {

	/**
	 * RDBMS Input Component - read records as input from RDBMS Table.
	 *
	 */
	private static final long serialVersionUID = -2946197683137950707L;
	protected FlowDef flowDef;
	protected Pipe tailPipe;
	protected List<SchemaField> schemaFieldList;
	protected OutputRDBMSEntity outputRDBMSEntity;
	@SuppressWarnings("rawtypes")
	Class<? extends DBInputFormat> inputFormatClass;
	protected JDBCScheme scheme;
	protected TableDesc tableDesc;
	protected String driverName;
	protected String jdbcURL;
	protected String[] outputFields;
	protected String[] columnDefs = {};
	protected String[] primaryKeys = null;
	protected String[] fieldsDataType;
	protected int[] fieldsScale;
	protected int[] fieldsPrecision;
	protected Fields fields;
	protected String[] columnNames;
	private Connection connection;

	protected JDBCTap rdbmsTap;

	protected final static String TIME_STAMP = "HH:mm:ss";

	protected InputOutputFieldsAndTypesCreator<OutputRDBMSEntity> fieldsCreator;

	protected static Logger LOG = LoggerFactory.getLogger(OutputRDBMSAssembly.class);

	public OutputRDBMSAssembly(OutputRDBMSEntity baseComponentEntity, ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	public void initializeEntity(OutputRDBMSEntity assemblyEntityBase) {
		outputRDBMSEntity = assemblyEntityBase;
	}

	public abstract void intializeRdbmsSpecificDrivers();

	protected void prepareScheme() {

		fieldsDataType = fieldsCreator.getFieldDataTypes();
		fieldsScale = fieldsCreator.getFieldScale();
		fieldsPrecision = fieldsCreator.getFieldPrecision();

		if (fieldsCreator.getColDef()[0] == null)
			columnDefs = JavaToSQLTypeMapping.createTypeMapping(outputRDBMSEntity.getDatabaseType(), fieldsDataType,fieldsScale,fieldsPrecision);
		else
			columnDefs = fieldsCreator.getColDef();

		if (outputRDBMSEntity.getPrimaryKeys() != null) {
			primaryKeys = new String[outputRDBMSEntity.getPrimaryKeys().size()];
			int i = 0;
			for (TypeFieldName typeFieldName : outputRDBMSEntity.getPrimaryKeys()) {
				primaryKeys[i] = typeFieldName.getName();
				i++;
			}
		}
		fields = fieldsCreator.makeFieldsWithTypes();
		columnNames = fieldsCreator.getFieldNames();
		LOG.debug("Applying RDBMS schema to read data from RDBMS");

		createTableDescAndScheme();
	}

	protected void createTableDescAndScheme() {
		tableDesc = new TableDesc(outputRDBMSEntity.getTableName(), columnNames, columnDefs, primaryKeys);
		if (!outputRDBMSEntity.getLoadType().equals("update"))
			scheme = new JDBCScheme(inputFormatClass, fields, columnNames);
		else {
			String[] updateByColumns = new String[outputRDBMSEntity.getUpdateByKeys().size()];
			int i = 0;
			for (TypeFieldName typeFieldName : outputRDBMSEntity.getUpdateByKeys()) {
				updateByColumns[i] = typeFieldName.getName();
				i++;
			}
			scheme = new JDBCScheme(inputFormatClass, DBOutputFormat.class, fields, columnNames, null,
					new Fields(updateByColumns), updateByColumns);
		}
	}

	protected void initializeRDBMSTap() throws IOException, SQLException, Exception {
		LOG.debug("Initializing RDBMS Tap");
		SinkMode sinkMode = null;
		connection = createConnection(jdbcURL, driverName, outputRDBMSEntity.getUsername(),
				outputRDBMSEntity.getPassword());
		sinkMode = getSinkMode(connection);

		rdbmsTap = new JDBCTap(jdbcURL, outputRDBMSEntity.getUsername(), outputRDBMSEntity.getPassword(), driverName,
				tableDesc, scheme, sinkMode);
		((JDBCTap) rdbmsTap).setBatchSize(outputRDBMSEntity.getChunkSize());
	}

	protected SinkMode getSinkMode(Connection connection) throws SQLException, IOException {
		if (outputRDBMSEntity.getLoadType().equals("truncateLoad")) {
			if (!JDBCUtil.tableExists(connection, tableDesc)) {
				throw new IOException("Table '" + outputRDBMSEntity.getDatabaseName() + "."
						+ outputRDBMSEntity.getTableName() + "' doesn't exist for truncate");
			} else {
				JDBCUtil.executeUpdate(connection, "truncate table " + outputRDBMSEntity.getTableName());
				return SinkMode.UPDATE;
			}
		} else if (outputRDBMSEntity.getLoadType().equals("newTable")) {
			if (JDBCUtil.tableExists(connection, tableDesc)) {
				throw new SQLException("Table '" + outputRDBMSEntity.getDatabaseName() + "."
						+ outputRDBMSEntity.getTableName() + "' already exist");
			} else
				return SinkMode.REPLACE;
		} else { // for update and insert
			if (!JDBCUtil.tableExists(connection, tableDesc)) {
				throw new IOException("Table '" + outputRDBMSEntity.getDatabaseName() + "."
						+ outputRDBMSEntity.getTableName() + "' doesn't exist for " + outputRDBMSEntity.getLoadType());
			} else
				return SinkMode.UPDATE;

		}
	}

	private Connection createConnection(String connectionUrl, String driverClassName, String username,
										String password) {
		try {
			LOG.info("creating connection: {}", connectionUrl);
			Class.forName(driverClassName);

			Connection connection = null;

			if (username == null)
				connection = DriverManager.getConnection(connectionUrl);
			else
				connection = DriverManager.getConnection(connectionUrl, username, password);

			connection.setAutoCommit(false);

			return connection;
		} catch (SQLException exception) {
			if (exception.getMessage().startsWith("No suitable driver found for")) {
				List<String> availableDrivers = new ArrayList<String>();
				Enumeration<Driver> drivers = DriverManager.getDrivers();

				while (drivers.hasMoreElements())
					availableDrivers.add(drivers.nextElement().getClass().getName());

				LOG.error("Driver not found: {} because {}. Available drivers are: {}", driverClassName,
						exception.getMessage(), availableDrivers);
			}
			throw new TapException(exception.getMessage() + " (SQL error code: " + exception.getErrorCode()
					+ ") opening connection: " + connectionUrl, exception);
		} catch (Exception exception) {
			LOG.error("unable to load driver class: " + driverClassName + " because: " + exception.getMessage() + " not found");
			throw new TapException(
					"unable to load driver class: " + driverClassName + " because: " + exception.getMessage(),
					exception);
		}
	}

	protected void prepareAssembly() throws SQLException, IOException {
		outputFields = fieldsCreator.getFieldNames();
		LOG.debug(outputRDBMSEntity.getDatabaseType() + " Output Component: [ Database Name: "
				+ outputRDBMSEntity.getDatabaseName() + ", Table Name: " + outputRDBMSEntity.getTableName()
				+ ", Column Names: " + Arrays.toString(outputFields) + "]");

		if (LOG.isTraceEnabled()) {
			LOG.trace(outputRDBMSEntity.toString());
		}
		LOG.trace("Creating output assembly for " + outputRDBMSEntity.getDatabaseType() + "'"
				+ outputRDBMSEntity.getComponentId() + "'");
		flowDef = componentParameters.getFlowDef();
		tailPipe = componentParameters.getInputPipe();

		try {
			prepareScheme();
		} catch (Exception e) {
			LOG.error("Error in preparing scheme for component '" + outputRDBMSEntity.getComponentId() + "': "
					+ e.getMessage());
			throw new RuntimeException(e);
		}

		try {
			initializeRDBMSTap();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			connection.close();
		}
	}

	@Override
	protected void createAssembly() {

		fieldsCreator = new InputOutputFieldsAndTypesCreator<OutputRDBMSEntity>(outputRDBMSEntity);
		intializeRdbmsSpecificDrivers();

		try {
			prepareAssembly();

			Pipe sinkPipe = new Pipe(outputRDBMSEntity.getComponentId(), tailPipe);
			setHadoopProperties(rdbmsTap.getStepConfigDef());
			setHadoopProperties(sinkPipe.getStepConfigDef());
			flowDef = flowDef.addTailSink(sinkPipe, rdbmsTap);
			setOutLink("output", "NoSocketId", outputRDBMSEntity.getComponentId(), sinkPipe,
					componentParameters.getInputFieldsList().get(0));
		} catch (Exception e) {
			LOG.error("Error in creating assembly for component '" + outputRDBMSEntity.getComponentId() + "', Error: "
					+ e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}