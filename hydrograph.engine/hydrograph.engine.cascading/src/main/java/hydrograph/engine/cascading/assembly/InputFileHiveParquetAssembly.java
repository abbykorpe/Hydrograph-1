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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.tap.hive.HivePartitionTap;
import cascading.tap.hive.HiveTap;
import hydrograph.engine.assembly.entity.InputFileHiveParquetEntity;
import hydrograph.engine.assembly.entity.base.HiveEntityBase;
import hydrograph.engine.cascading.assembly.base.InputFileHiveBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.scheme.hive.parquet.HiveParquetScheme;
import hydrograph.engine.cascading.scheme.hive.parquet.HiveParquetTableDescriptor;

public class InputFileHiveParquetAssembly extends InputFileHiveBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2775585858902811182L;
	private static Logger LOG = LoggerFactory
			.getLogger(InputFileHiveParquetAssembly.class);
	private HiveParquetTableDescriptor tableDesc;
	private InputFileHiveParquetEntity inputFileHiveParquetEntity;

	public InputFileHiveParquetAssembly(InputFileHiveParquetEntity baseComponentEntity,
			ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	protected void prepareScheme() {
		LOG.debug("Applying HiveParquetScheme to read data from Hive");

		// HiveParquetTableDescriptor is developed specifically for handling
		// Parquet File format with Hive. Hence, the object of table descriptor
		// is created in its respective assembly and not in its base class.
		tableDesc = new HiveParquetTableDescriptor(
				inputFileHiveParquetEntity.getDatabaseName(),
				inputFileHiveParquetEntity.getTableName(),
				fieldsCreator.getFieldNames(),
				fieldsCreator.hiveParquetDataTypeMapping(inputFileHiveParquetEntity
						.getFieldsList()),
				inputFileHiveParquetEntity.getPartitionKeys(),
				getHiveExternalTableLocationPath());
		scheme = new HiveParquetScheme(tableDesc);
		scheme.setSourceFields(tableDesc.toFields());
		scheme.setSinkFields(tableDesc.toFields());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hydrograph.engine.cascading.assembly.base.InputFileHiveBase#initializeHiveTap
	 * ()
	 */
	@Override
	protected void initializeHiveTap() {
		LOG.debug("Initializing Hive Tap using HiveParquetTableDescriptor");
		hiveTap = new HiveTap(tableDesc, scheme);
		if (inputFileHiveParquetEntity.getPartitionKeys() != null
				&& inputFileHiveParquetEntity.getPartitionKeys().length > 0) {
			hiveTap = new HivePartitionTap((HiveTap) hiveTap);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hydrograph.engine.cascading.assembly.base.InputFileHiveBase#
	 * castHiveEntityFromBase
	 * (hydrograph.engine.assembly.entity.base.HiveEntityBase) /* cast the
	 * hiveEntityBase to InputFileHiveParquetEntity
	 */

	@Override
	public void castHiveEntityFromBase(HiveEntityBase hiveEntityBase) {
		inputFileHiveParquetEntity = (InputFileHiveParquetEntity) hiveEntityBase;

	}
}