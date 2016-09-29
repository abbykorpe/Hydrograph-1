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
import cascading.jdbc.db.DBOutputFormat;
import cascading.pipe.Pipe;
import cascading.tap.SinkMode;
import cascading.tuple.Fields;
import hydrograph.engine.assembly.entity.OutputRDBMSEntity;
import hydrograph.engine.assembly.entity.elements.SchemaField;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.InputOutputFieldsAndTypesCreator;
import hydrograph.engine.cascading.assembly.utils.JavaToSQLTypeMapping;
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
	protected String[] outputFields;
	protected String[] columnDefs = {};
	protected String[] primaryKeys = null;
	protected String[] fieldsDataType;
	protected Fields fields;
	protected String[] columnNames;

	protected JDBCTap rdbmsTap;

	protected final static String TIME_STAMP = "hh:mm:ss";

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

		if (fieldsCreator.getColDef()[0] == null)
			columnDefs = JavaToSQLTypeMapping.createTypeMapping(outputRDBMSEntity.getDatabaseType(), fieldsDataType);
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
			// String[] orderByColumns= {"lwr"};
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

	protected void initializeRDBMSTap() {
		LOG.debug("Initializing RDBMS Tap");
		SinkMode sinkMode;
		if (outputRDBMSEntity.getLoadType().equals("truncateLoad")
				|| outputRDBMSEntity.getLoadType().equals("newTable"))
			sinkMode = SinkMode.REPLACE;
		else
			sinkMode = SinkMode.UPDATE;

		rdbmsTap = new JDBCTap(
				outputRDBMSEntity.getJdbcurl() + (outputRDBMSEntity.getDatabaseName() == null ? ""
						: "/" + outputRDBMSEntity.getDatabaseName()),
				outputRDBMSEntity.getUsername(), outputRDBMSEntity.getPassword(), driverName, tableDesc, scheme,
				sinkMode);
		((JDBCTap) rdbmsTap).setBatchSize(outputRDBMSEntity.getBatchSize());
	}

	protected void prepareAssembly() {
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

		initializeRDBMSTap();
	}

	@Override
	protected void createAssembly() {

		fieldsCreator = new InputOutputFieldsAndTypesCreator<OutputRDBMSEntity>(outputRDBMSEntity);
		intializeRdbmsSpecificDrivers();
		prepareAssembly(); // exception handled separately within

		try {
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