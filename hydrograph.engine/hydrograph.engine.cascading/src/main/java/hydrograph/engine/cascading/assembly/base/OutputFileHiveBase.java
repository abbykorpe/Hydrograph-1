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

import hydrograph.engine.assembly.entity.base.HiveEntityBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.IOFieldsAndTypesCreator;

import java.util.Arrays;

import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowDef;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.tap.Tap;

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

	private static Logger LOG = LoggerFactory
			.getLogger(OutputFileHiveBase.class);

	protected IOFieldsAndTypesCreator<HiveEntityBase> fieldsCreator;

	public OutputFileHiveBase(HiveEntityBase baseComponentEntity,
			ComponentParameters componentParameters) {
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
		LOG.debug("Hive Output Component: [ Database Name: "
				+ hiveEntityBase.getDatabaseName() + ", Table Name: "
				+ hiveEntityBase.getTableName() + ", Column Names: "
				+ Arrays.toString(outputFields) + ", Partition Column: "
				+ Arrays.toString(hiveEntityBase.getPartitionKeys()) + "]");

		if (LOG.isTraceEnabled()) {
			LOG.trace(hiveEntityBase.toString());
		}
		LOG.trace("Creating output file Hive assembly for '"
				+ hiveEntityBase.getComponentId() + "'");
		flowDef = componentParameters.getFlowDef();
		tailPipe = componentParameters.getInputPipe();

		try {
			prepareScheme();
		} catch (Exception e) {
			LOG.error("Error in preparing scheme for component '"
					+ hiveEntityBase.getComponentId() + "': " + e.getMessage());
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
		fieldsCreator = new IOFieldsAndTypesCreator<HiveEntityBase>(
				hiveEntityBase);
		prepareAssembly(); // exception handled separately within

		try {
			Pipe sinkPipe = new Pipe(hiveEntityBase.getComponentId(), tailPipe);
			setHadoopProperties(hiveTap.getStepConfigDef());
			setHadoopProperties(sinkPipe.getStepConfigDef());
			flowDef = flowDef.addTailSink(sinkPipe, hiveTap);
		} catch (Exception e) {
			LOG.error(
					"Error in creating assembly for component '"
							+ hiveEntityBase.getComponentId() + "', Error: "
							+ e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public Path getHiveExternalTableLocationPath(String externalPath) {
		return externalPath == null ? null : new Path(externalPath);
	}
}