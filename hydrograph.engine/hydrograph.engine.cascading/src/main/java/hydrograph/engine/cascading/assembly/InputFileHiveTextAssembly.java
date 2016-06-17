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
/**
 * 
 */
package hydrograph.engine.cascading.assembly;

import hydrograph.engine.assembly.entity.InputFileHiveTextEntity;
import hydrograph.engine.assembly.entity.base.HiveEntityBase;
import hydrograph.engine.cascading.assembly.base.InputFileHiveBase;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.scheme.HydrographDelimitedParser;
import hydrograph.engine.cascading.scheme.hive.text.HiveTextTableDescriptor;
import hydrograph.engine.cascading.utilities.DataTypeCoerce;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextDelimited;
import cascading.tap.SinkMode;
import cascading.tap.hive.HivePartitionTap;
import cascading.tap.hive.HiveTap;
import cascading.tuple.Fields;

public class InputFileHiveTextAssembly extends InputFileHiveBase {

	/**
	 * Hive Input File Component - read records as input from Hive Table stored
	 * in Text format.
	 * 
	 */
	private static final long serialVersionUID = -2946197683137950707L;

	protected InputFileHiveTextEntity inputHiveFileEntity;
	@SuppressWarnings("rawtypes")
	protected Scheme scheme;
	private HiveTextTableDescriptor tableDesc;

	private static Logger LOG = LoggerFactory
			.getLogger(InputFileHiveTextAssembly.class);

	public InputFileHiveTextAssembly(InputFileHiveTextEntity baseComponentEntity,
			ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	public void validateParameters() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hydrograph.engine.cascading.assembly.base.InputFileHiveBase#prepareScheme
	 * ()
	 */
	@Override
	protected void prepareScheme() {
		LOG.debug("Applying HiveTextScheme to read data from Hive");

		// HiveTextTableDescriptor is developed specifically for handling
		// Text File format with Hive. Hence, the object of table descriptor
		// is created in its respective assembly and not in its base class.
		tableDesc = new HiveTextTableDescriptor(
				inputHiveFileEntity.getDatabaseName(),

				inputHiveFileEntity.getTableName(),
				fieldsCreator.getFieldNames(),
				fieldsCreator.hiveParquetDataTypeMapping(inputHiveFileEntity
						.getFieldsList()),
				inputHiveFileEntity.getPartitionKeys(),
				inputHiveFileEntity.getDelimiter(), "",
				getHiveExternalTableLocationPath(), false);

		Fields fields = getFieldsToWrite();
		HydrographDelimitedParser delimitedParser = new HydrographDelimitedParser(
				inputHiveFileEntity.getDelimiter(),

				inputHiveFileEntity.getQuote(), null,
				inputHiveFileEntity.isStrict(), inputHiveFileEntity.isSafe());

		scheme = new TextDelimited(fields, null, false, false, "UTF-8",
				delimitedParser);

		// scheme = new
		// TextDelimited(fields,inputHiveFileEntity.getDelimiter());
		scheme.setSourceFields(fields);
		scheme.setSinkFields(fields);
	}

	protected void initializeHiveTap() {
		LOG.debug("Initializing Hive Tap using HiveTextTableDescriptor");
		hiveTap = new HiveTap(tableDesc, scheme, SinkMode.KEEP, true);
		if (inputHiveFileEntity.getPartitionKeys() != null
				&& inputHiveFileEntity.getPartitionKeys().length > 0) {
			hiveTap = new HivePartitionTap((HiveTap) hiveTap);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bitwiseglobal.cascading.assembly.base.InputFileHiveBase#
	 * castHiveEntityFromBase
	 * (com.bitwiseglobal.assembly.entity.base.HiveEntityBase)
	 * 
	 * 
	 * 
	 * cast the hiveEntityBase to InputFileHiveTextEntity
	 */
	@Override
	public void castHiveEntityFromBase(HiveEntityBase hiveEntityBase) {
		inputHiveFileEntity = (InputFileHiveTextEntity) hiveEntityBase;

	}

	/**
	 * @return Fields.
	 */
	private Fields getFieldsToWrite() {
		String[] testField = new String[fieldsCreator.getFieldNames().length
				- inputHiveFileEntity.getPartitionKeys().length];
		int i = 0;
		for (String inputfield : fieldsCreator.getFieldNames()) {
			if (!Arrays.asList(inputHiveFileEntity.getPartitionKeys())
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
		Type[] typeArr = new Type[fieldsCreator.getFieldDataTypes().length -inputHiveFileEntity.getPartitionKeys().length];
		int i = 0;
		int j = 0;
		for (String dataTypes : fieldsCreator.getFieldDataTypes()) {
			if(!Arrays.asList(inputHiveFileEntity.getPartitionKeys()).contains(fieldsCreator.getFieldNames()[i])){
				try {
					typeArr[j++] = DataTypeCoerce.convertClassToCoercibleType(
							Class.forName(dataTypes), fieldsCreator.getFieldFormat()[i], fieldsCreator.getFieldScale()[i],
							fieldsCreator.getFieldScaleType()[i]);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(
							"'"
									+ dataTypes
									+ "' class not found while applying cascading datatypes for component '"
									+ inputHiveFileEntity.getComponentId()
									+ "' ", e);
				}

			}	
			i++;
		}
		return typeArr;
	}

}