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
/**
 * 
 */
package hydrograph.engine.cascading.assembly;

import cascading.scheme.hadoop.TextDelimited;
import cascading.tap.SinkMode;
import cascading.tap.hive.HivePartitionTap;
import cascading.tap.hive.HiveTap;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.base.OutputFileHiveBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.scheme.HydrographDelimitedParser;
import hydrograph.engine.cascading.scheme.hive.text.HiveTextTableDescriptor;
import hydrograph.engine.cascading.utilities.DataTypeCoerce;
import hydrograph.engine.core.component.entity.OutputFileHiveTextEntity;
import hydrograph.engine.core.component.entity.base.HiveEntityBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Hive Output File Component - records written as output into Hive Table
 */
public class OutputFileHiveTextAssembly extends OutputFileHiveBase<OutputFileHiveTextEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4413173479628531160L;
	private HiveTextTableDescriptor tableDesc;
	protected OutputFileHiveTextEntity outputFileHiveTextEntity;

	private static Logger LOG = LoggerFactory
			.getLogger(OutputFileHiveTextAssembly.class);

	public OutputFileHiveTextAssembly(
			OutputFileHiveTextEntity baseComponentEntity,
			ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	public void validateParameters() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bitwiseglobal.cascading.assembly.base.OutputFileHiveBase#
	 * initializeHiveTap()
	 */
	@Override
	protected void initializeHiveTap() {

		LOG.debug("Initializing Hive Tap using HiveTableDescriptor");
		if (outputFileHiveTextEntity.getOverWrite()) {
			hiveTap = new HiveTap(tableDesc, scheme, SinkMode.REPLACE, true);
		} else {
			hiveTap = new HiveTap(tableDesc, scheme, SinkMode.UPDATE, true);
		}

		if (outputFileHiveTextEntity.getPartitionKeys() != null
				&& outputFileHiveTextEntity.getPartitionKeys().length > 0) {
			hiveTap = new HivePartitionTap((HiveTap) hiveTap);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bitwiseglobal.cascading.assembly.base.OutputFileHiveBase#prepareScheme
	 * ()
	 */
	@Override
	protected void prepareScheme() {

		LOG.debug("Applying HiveTextScheme to write data to Hive");

		tableDesc = new HiveTextTableDescriptor(
				outputFileHiveTextEntity.getDatabaseName(),
				outputFileHiveTextEntity.getTableName(), outputFields,
				fieldsCreator
						.hiveParquetDataTypeMapping(outputFileHiveTextEntity
								.getFieldsList()),
						outputFileHiveTextEntity.getPartitionKeys(),
				outputFileHiveTextEntity.getDelimiter(), "",
				getHiveExternalTableLocationPath(outputFileHiveTextEntity
						.getExternalTablePathUri()));

		HydrographDelimitedParser delimitedParser = new HydrographDelimitedParser(
				outputFileHiveTextEntity.getDelimiter() != null ? outputFileHiveTextEntity.getDelimiter() : "\1",
				outputFileHiveTextEntity.getQuote(), null,
				outputFileHiveTextEntity.isStrict(),
				outputFileHiveTextEntity.isSafe());

		// scheme = new TextDelimited(fields)

		Fields fields = getFieldsToWrite();
		scheme = new TextDelimited(fields, null, false, false, "UTF-8",
				delimitedParser);
		// scheme = new
		// TextDelimited(false,outputFileHiveTextEntity.getDelimiter());
		scheme.setSourceFields(fields);
		scheme.setSinkFields(fields);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bitwiseglobal.cascading.assembly.base.OutputFileHiveBase#
	 * castHiveEntityFromBase
	 * (com.bitwiseglobal.assembly.entity.base.HiveEntityBase)
	 */
	@Override
	public void castHiveEntityFromBase(HiveEntityBase hiveEntityBase) {
		outputFileHiveTextEntity = (OutputFileHiveTextEntity) hiveEntityBase;

	}

	/**
	 * @return Fields.
	 */
	private Fields getFieldsToWrite() {
		String[] testField = new String[outputFields.length
				- outputFileHiveTextEntity.getPartitionKeys().length];
		int i = 0;
		for (String inputfield : convertLowerCase(outputFields)) {
			if (!Arrays.asList(convertLowerCase(outputFileHiveTextEntity.getPartitionKeys()))
					.contains(inputfield)) {
				testField[i++] = inputfield;
			}
		}
		return new Fields(testField).applyTypes(getTypes());

	}

	/**
	 * The datatype support for text to skip the partition key write in file.
	 */
	private Type[] getTypes() {
		Type[] typeArr = new Type[fieldsCreator.getFieldDataTypes().length
				- outputFileHiveTextEntity.getPartitionKeys().length];
		int i = 0;
		int j = 0;
		for (String dataTypes : fieldsCreator.getFieldDataTypes()) {
			if (!Arrays.asList(outputFileHiveTextEntity.getPartitionKeys())
					.contains(fieldsCreator.getFieldNames()[i])) {
				try {
					typeArr[j++] = DataTypeCoerce.convertClassToCoercibleType(
							Class.forName(dataTypes),
							fieldsCreator.getFieldFormat()[i],
							fieldsCreator.getFieldScale()[i],
							fieldsCreator.getFieldScaleType()[i]);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(
							"'"
									+ dataTypes
									+ "' class not found while applying cascading datatypes for component '"
									+ outputFileHiveTextEntity.getComponentId()
									+ "' ", e);
				}

			}
			i++;
		}
		return typeArr;
	}
}
