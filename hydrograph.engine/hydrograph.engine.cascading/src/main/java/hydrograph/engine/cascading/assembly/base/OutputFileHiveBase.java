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
package hydrograph.engine.cascading.assembly.base;

import java.util.Arrays;

import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowDef;
import cascading.pipe.Pipe;
import cascading.pipe.assembly.Rename;
import cascading.scheme.Scheme;
import cascading.tap.Tap;
import cascading.tuple.Fields;
import hydrograph.engine.assembly.entity.OutputFileHiveParquetEntity;
import hydrograph.engine.assembly.entity.OutputFileHiveTextEntity;
import hydrograph.engine.assembly.entity.base.HiveEntityBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.InputOutputFieldsAndTypesCreator;
import hydrograph.engine.utilities.ComponentHelper;

public abstract class OutputFileHiveBase<T extends HiveEntityBase> extends BaseComponent<HiveEntityBase> {

	/**
	 * Hive Output File Component - records written as output into Hive Table
	 */
	private static final long serialVersionUID = 4184919036703029509L;
	private Pipe tailPipe;
	private FlowDef flowDef;
	protected HiveEntityBase hiveEntityBase;
	@SuppressWarnings("rawtypes")
	protected Scheme scheme;
	@SuppressWarnings("rawtypes")
	protected Tap hiveTap;

	protected String[] outputFields;
	protected final static String TIME_STAMP = "hh:mm:ss";

	private static Logger LOG = LoggerFactory.getLogger(OutputFileHiveBase.class);

	protected InputOutputFieldsAndTypesCreator<HiveEntityBase> fieldsCreator;

	public OutputFileHiveBase(HiveEntityBase baseComponentEntity, ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	public void initializeEntity(HiveEntityBase assemblyEntityBase) {
		hiveEntityBase = assemblyEntityBase;
		castHiveEntityFromBase(hiveEntityBase);
	}

	public abstract void castHiveEntityFromBase(HiveEntityBase hiveEntityBase);

	/**
	 * This method will create the table descriptor and scheme to write the data
	 * to Hive Table. In this method, table descriptor and scheme will be
	 * created for specific file format like TextDelimited for Text file, and so
	 * on for other file format like hive, etc.
	 */
	protected abstract void prepareScheme();

	/**
	 * This method will initialize the Hive Tap with its file format specific
	 * table descriptor and scheme. This method will also initialize Hive
	 * Partition Tap if table is partitioned.
	 */
	protected abstract void initializeHiveTap();

	protected void prepareAssembly() {
		outputFields = fieldsCreator.getFieldNames();
		LOG.debug("Hive Output Component: [ Database Name: " + hiveEntityBase.getDatabaseName() + ", Table Name: "
				+ hiveEntityBase.getTableName() + ", Column Names: " + Arrays.toString(outputFields)
				+ ", Partition Column: " + Arrays.toString(hiveEntityBase.getPartitionKeys()) + "]");

		if (LOG.isTraceEnabled()) {
			LOG.trace(hiveEntityBase.toString());
		}
		LOG.trace("Creating output file Hive assembly for '" + hiveEntityBase.getComponentId() + "'");
		flowDef = componentParameters.getFlowDef();
		tailPipe = componentParameters.getInputPipe();

		try {
			prepareScheme();
		} catch (Exception e) {
			LOG.error("Error in preparing scheme for component '" + hiveEntityBase.getComponentId() + "': "
					+ e.getMessage());
			throw new RuntimeException(e);
		}

		initializeHiveTap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hydrograph.engine.cascading.assembly.base.BaseComponent#createAssembly()
	 */
	@Override
	protected void createAssembly() {
		fieldsCreator = new InputOutputFieldsAndTypesCreator<HiveEntityBase>(hiveEntityBase);
		validateDateFormatInHive(fieldsCreator.getFieldNames(), fieldsCreator.getFieldDataTypes(),
				fieldsCreator.getFieldFormat());
		prepareAssembly(); // exception handled separately within

		try {
			Pipe sinkPipe = new Pipe(ComponentHelper.getComponentName(getComponentType(hiveEntityBase),
					hiveEntityBase.getComponentId(), ""), tailPipe);
			sinkPipe = new Rename(sinkPipe, new Fields(outputFields), new Fields(convertLowerCase(outputFields)));
			setOutLink("output","NoSocketId",
					hiveEntityBase.getComponentId(), sinkPipe, componentParameters
					.getInputFieldsList().get(0));
			setHadoopProperties(hiveTap.getStepConfigDef());
			setHadoopProperties(sinkPipe.getStepConfigDef());
			flowDef = flowDef.addTailSink(sinkPipe, hiveTap);
		} catch (Exception e) {
			LOG.error("Error in creating assembly for component '" + hiveEntityBase.getComponentId() + "', Error: "
					+ e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method will validate date format for Hive component. It will throw
	 * an exception if unsupported date format is used.
	 * 
	 * @param fieldNames
	 * @param datatypes
	 * @param dateformat
	 */
	private void validateDateFormatInHive(String[] fieldNames, String[] datatypes, String[] dateformat) {
		for (int i = 0; i < datatypes.length; i++) {
			if ((datatypes[i].toLowerCase().contains("date") && !dateformat[i].contains("yyyy-MM-dd"))
					|| (datatypes[i].toLowerCase().contains("timestamp")
							&& !dateformat[i].contains("yyyy-MM-dd HH:mm:ss"))) {
				throw new RuntimeException("Component: \"" + hiveEntityBase.getComponentId() + "\" - Field \""
						+ fieldNames[i] + "\" has unsupported date format \"" + dateformat[i]
						+ "\". Hive supports only \"yyyy-MM-dd\" format for date datatype and \"yyyy-MM-dd HH:mm:ss\" format for timestamp datatype.");
			}
		}
	}

	protected String[] convertLowerCase(String[] fields) {
		String[] convertedfields = new String[fields.length];
		int i = 0;
		for (String field : fields) {
			convertedfields[i++] = field.toLowerCase();
		}
		return convertedfields;
	}

	private String getComponentType(HiveEntityBase hiveEntityBase2) {
		if (hiveEntityBase2 instanceof OutputFileHiveTextEntity)
			return "outputFileHiveText";
		else if (hiveEntityBase2 instanceof OutputFileHiveParquetEntity)
			return "outputFileHiveParquet";
		else
			return "outputFileHive";
	}

	public Path getHiveExternalTableLocationPath(String externalPath) {
		return externalPath == null ? null : new Path(externalPath);
	}

}
