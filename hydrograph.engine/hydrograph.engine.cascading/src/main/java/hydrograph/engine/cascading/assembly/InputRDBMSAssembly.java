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

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowDef;
import cascading.jdbc.JDBCScheme;
import cascading.jdbc.JDBCTap;
import cascading.jdbc.TableDesc;
import cascading.jdbc.db.DBInputFormat;
import cascading.pipe.Pipe;
import cascading.tap.SinkMode;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.InputOutputFieldsAndTypesCreator;
import hydrograph.engine.core.component.entity.InputRDBMSEntity;
import hydrograph.engine.core.component.entity.elements.OutSocket;
import hydrograph.engine.core.component.entity.elements.SchemaField;

public class InputRDBMSAssembly extends BaseComponent<InputRDBMSEntity> {

	/**
	 * RDBMS Input Component - read records from RDBMS Table.
	 * 
	 */
	private static final long serialVersionUID = -2946197683137950707L;
	protected FlowDef flowDef;
	protected Pipe pipe;
	protected List<SchemaField> schemaFieldList;
	protected InputRDBMSEntity inputRDBMSEntity;
	protected String[] fieldsDataType;
	@SuppressWarnings("rawtypes")
	Class<? extends DBInputFormat> inputFormatClass;
	protected JDBCScheme scheme;
	private TableDesc tableDesc;
	protected String driverName;
	protected String[] columnDefs = {};
	protected String[] primaryKeys = null;
	protected Fields fields;
	protected String[] columnNames;
	protected String condition;

	protected JDBCTap rdbmsTap;

	protected final static String TIME_STAMP = "hh:mm:ss";

	protected InputOutputFieldsAndTypesCreator<InputRDBMSEntity> fieldsCreator;

	private static Logger LOG = LoggerFactory.getLogger(InputRDBMSAssembly.class);

	public InputRDBMSAssembly(InputRDBMSEntity baseComponentEntity, ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	public void initializeEntity(InputRDBMSEntity assemblyEntityBase) {
		inputRDBMSEntity = assemblyEntityBase;

	}

	public void intializeRdbmsSpecificDrivers() {
		// Todo
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bitwiseglobal.cascading.assembly.base.BaseComponent#createAssembly()
	 * This method call the generate Taps and Pipes and setOutlinks
	 */

	protected void createAssembly() {

		fieldsCreator = new InputOutputFieldsAndTypesCreator<InputRDBMSEntity>(inputRDBMSEntity);
		intializeRdbmsSpecificDrivers();
		generateTapsAndPipes(); // exception handled separately within
		try {
			flowDef = flowDef.addSource(pipe, rdbmsTap);

			if (LOG.isTraceEnabled()) {
				LOG.trace(inputRDBMSEntity.toString());
			}
			for (OutSocket outSocket : inputRDBMSEntity.getOutSocketList()) {

				String[] fieldsArray = new String[inputRDBMSEntity.getFieldsList().size()];
				int i = 0;
				for (SchemaField Fields : inputRDBMSEntity.getFieldsList()) {
					fieldsArray[i++] = Fields.getFieldName();
				}

				LOG.trace("Creating input " + inputRDBMSEntity.getDatabaseType() + " assembly for '"
						+ inputRDBMSEntity.getComponentId() + "' for socket: '" + outSocket.getSocketId()
						+ "' of type: '" + outSocket.getSocketType() + "'");
				setOutLink(outSocket.getSocketType(), outSocket.getSocketId(), inputRDBMSEntity.getComponentId(), pipe,
						new Fields(fieldsArray));
			}
		} catch (Exception e) {
			LOG.error("Error in creating assembly for component '" + inputRDBMSEntity.getComponentId() + "', Error: "
					+ e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method will create the table descriptor and scheme to read the data
	 * from RDBMS Table. In this method, table descriptor and scheme will be
	 * created for specific file format like TextDelimited for Text file, and so
	 * on for other file format like parquet, etc.
	 */
	protected void prepareScheme() {

		fields = fieldsCreator.makeFieldsWithTypes();
		columnNames = fieldsCreator.getFieldNames();
		condition = inputRDBMSEntity.getCondition();
		LOG.debug("Applying " + inputRDBMSEntity.getDatabaseType() + "  schema to read data from RDBMS");

		createTableDescAndScheme();
	}

	protected void createTableDescAndScheme() {
		// For sql query
		if (inputRDBMSEntity.getQuery() != null && inputRDBMSEntity.getQuery() != "") {
			String sql = inputRDBMSEntity.getQuery();

			String countSql = "select count(*) from " + inputRDBMSEntity.getTableName();
			scheme = new JDBCScheme(inputFormatClass, fields, columnNames, sql, countSql, -1);

		} else {
			tableDesc = new TableDesc(inputRDBMSEntity.getTableName(), fieldsCreator.getFieldNames(), columnDefs,
					primaryKeys);

			// scheme = new JDBCScheme(inputFormatClass, fields, columnNames);
			// scheme = new JDBCScheme(inputFormatClass,null, fields,
			// columnNames, null, null,-1, null, null, true );
			scheme = new JDBCScheme(inputFormatClass, null, fields, columnNames, null, condition, -1, null, null, true);
		}
	}

	protected void initializeRdbmsTap() {
		LOG.debug("Initializing RDBMS Tap.");
		// //JDBCTap replaceTap = new JDBCTap(inputRDBMSEntity.getJdbcurl(),
		// inputRDBMSEntity.getDriverName(), tableDesc,scheme,
		// SinkMode.REPLACE);

		if (inputRDBMSEntity.getQuery() == null || inputRDBMSEntity.getQuery() == "") {
			rdbmsTap = new JDBCTap(
					inputRDBMSEntity.getJdbcurl() + (inputRDBMSEntity.getDatabaseName() == null ? ""
							: "/" + inputRDBMSEntity.getDatabaseName()),
					inputRDBMSEntity.getUsername(), inputRDBMSEntity.getPassword(), driverName, tableDesc, scheme,
					SinkMode.REPLACE);

		} else {
			rdbmsTap = new JDBCTap(
					inputRDBMSEntity.getJdbcurl() + (inputRDBMSEntity.getDatabaseName() == null ? ""
							: "/" + inputRDBMSEntity.getDatabaseName()),
					inputRDBMSEntity.getUsername(), inputRDBMSEntity.getPassword(), driverName, scheme);
		}
		((JDBCTap) rdbmsTap).setBatchSize(inputRDBMSEntity.getBatchSize());

	}

	public void generateTapsAndPipes() {

		// initializing each pipe and tap
		LOG.debug(inputRDBMSEntity.getDatabaseType() + " Input Component '" + inputRDBMSEntity.getComponentId()
				+ "': [ Database Name: " + inputRDBMSEntity.getDatabaseName() + ", Table Name: "
				+ inputRDBMSEntity.getTableName() + ", Column Names: " + Arrays.toString(fieldsCreator.getFieldNames())
				+ "]");

		// scheme and tap to be initialized in its specific assembly
		try {
			schemaFieldList = inputRDBMSEntity.getFieldsList();
			fieldsDataType = new String[schemaFieldList.size()];
			int i = 0;
			for (SchemaField eachSchemaField : schemaFieldList) {
				fieldsDataType[i++] = eachSchemaField.getFieldDataType();
			}

			prepareScheme();
		} catch (Exception e) {
			LOG.error("Error in preparing scheme for component '" + inputRDBMSEntity.getComponentId() + "': "
					+ e.getMessage());
			throw new RuntimeException(e);
		}

		flowDef = componentParameters.getFlowDef();
		initializeRdbmsTap();

		pipe = new Pipe(inputRDBMSEntity.getComponentId());
		setHadoopProperties(rdbmsTap.getStepConfigDef());
		setHadoopProperties(pipe.getStepConfigDef());
	}
}