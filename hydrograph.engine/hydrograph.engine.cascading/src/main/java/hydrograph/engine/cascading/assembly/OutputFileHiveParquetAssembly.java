/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 *******************************************************************************/
package hydrograph.engine.cascading.assembly;

import cascading.tap.SinkMode;
import cascading.tap.hive.HivePartitionTap;
import cascading.tap.hive.HiveTap;
import hydrograph.engine.cascading.assembly.base.OutputFileHiveBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.scheme.hive.parquet.HiveParquetScheme;
import hydrograph.engine.cascading.scheme.hive.parquet.HiveParquetTableDescriptor;
import hydrograph.engine.core.component.entity.OutputFileHiveParquetEntity;
import hydrograph.engine.core.component.entity.base.HiveEntityBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputFileHiveParquetAssembly extends
		OutputFileHiveBase<OutputFileHiveParquetEntity> {

	private static final long serialVersionUID = 6015895554773455760L;
	private static Logger LOG = LoggerFactory
			.getLogger(OutputFileHiveParquetAssembly.class);
	private HiveParquetTableDescriptor tableDesc;
	private OutputFileHiveParquetEntity outputFileHiveParquetEntity;

	public OutputFileHiveParquetAssembly(
			OutputFileHiveParquetEntity assemblyEntityBase,
			ComponentParameters componentParameters) {
		super(assemblyEntityBase, componentParameters);
	}

	@Override
	protected void prepareScheme() {
		LOG.debug("Applying HiveParquetScheme to write data to Hive");
		tableDesc = new HiveParquetTableDescriptor(
				outputFileHiveParquetEntity.getDatabaseName(),
				outputFileHiveParquetEntity.getTableName(), outputFields,
				fieldsCreator
						.hiveParquetDataTypeMapping(outputFileHiveParquetEntity
								.getFieldsList()),
				outputFileHiveParquetEntity.getPartitionKeys(),
				getHiveExternalTableLocationPath(outputFileHiveParquetEntity
						.getExternalTablePathUri()));

		scheme = new HiveParquetScheme(tableDesc);
		scheme.setSourceFields(tableDesc.toFields(true));
		scheme.setSinkFields(tableDesc.toFields(true));
	}

	@Override
	protected void initializeHiveTap() {
		LOG.debug("Initializing Hive Tap using HiveParquetTableDescriptor");
		if (outputFileHiveParquetEntity.getOverWrite()) {
			hiveTap = new HiveTap(tableDesc, scheme, SinkMode.REPLACE, false);
		} else {
			hiveTap = new HiveTap(tableDesc, scheme, SinkMode.KEEP, false);
		}

		if (outputFileHiveParquetEntity.getPartitionKeys() != null
				&& outputFileHiveParquetEntity.getPartitionKeys().length > 0) {
			hiveTap = new HivePartitionTap((HiveTap) hiveTap);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hydrograph.engine.cascading.assembly.base.OutputFileHiveBase#
	 * castHiveEntityFromBase
	 * (hydrograph.engine.assembly.entity.base.HiveEntityBase)
	 */
	@Override
	public void castHiveEntityFromBase(HiveEntityBase hiveEntityBase) {
		outputFileHiveParquetEntity = (OutputFileHiveParquetEntity) hiveEntityBase;

	}

}
