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

import hydrograph.engine.assembly.entity.InputFileParquetEntity;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.IOFieldsAndTypesCreator;
import hydrograph.engine.cascading.scheme.parquet.ParquetTupleScheme;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowDef;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class InputFileParquetAssembly extends
		BaseComponent<InputFileParquetEntity> {

	private static final long serialVersionUID = -2946197683137950707L;
	private InputFileParquetEntity inputFileParquetEntity;
	@SuppressWarnings("rawtypes")
	private Scheme scheme;
	private Pipe pipe;
	@SuppressWarnings("rawtypes")
	private Tap tap;
	private FlowDef flowDef;
	private Fields inputFields;

	private IOFieldsAndTypesCreator<InputFileParquetEntity> fieldsCreator;

	private static final Logger LOG = LoggerFactory
			.getLogger(InputFileParquetAssembly.class);

	public InputFileParquetAssembly(InputFileParquetEntity baseComponentEntity,
			ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	protected void createAssembly() {
		try {
			fieldsCreator = new IOFieldsAndTypesCreator<InputFileParquetEntity>(
					inputFileParquetEntity);
			LOG.debug("InputFile Parquet Component: [ Fields List : "
					+ Arrays.toString(fieldsCreator.getFieldNames())
					+ ", Field Types : "
					+ Arrays.toString(fieldsCreator.getFieldDataTypes())
					+ " , Path : " + inputFileParquetEntity.getPath()
					+ ", Phase : " + inputFileParquetEntity.getPhase() + "]");
			inputFields = fieldsCreator.makeFields();

			generateTapsAndPipes();
			flowDef = flowDef.addSource(pipe, tap);

			if (LOG.isTraceEnabled()) {
				LOG.trace(inputFileParquetEntity.toString());
			}
			for (OutSocket outSocket : inputFileParquetEntity
					.getOutSocketList()) {

				LOG.trace("Creating input file parquet assembly for '"
						+ inputFileParquetEntity.getComponentId()
						+ "' for socket: '" + outSocket.getSocketId()
						+ "' of type: '" + outSocket.getSocketType() + "'");
				setOutLink(outSocket.getSocketType(), outSocket.getSocketId(),
						inputFileParquetEntity.getComponentId(), pipe,
						scheme.getSourceFields());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void generateTapsAndPipes() throws IOException {
		try {
			prepareScheme();
		} catch (Exception e) {
			LOG.error("Error in preparing scheme for component '"
					+ inputFileParquetEntity.getComponentId() + "': "
					+ e.getMessage());
			throw new RuntimeException(e);
		}
		flowDef = componentParameters.getFlowDef();
		tap = new Hfs(scheme, inputFileParquetEntity.getPath());
		pipe = new Pipe(inputFileParquetEntity.getComponentId());

		setHadoopProperties(tap.getStepConfigDef());
		setHadoopProperties(pipe.getStepConfigDef());
	}

	protected void prepareScheme() {
		scheme = new ParquetTupleScheme(inputFields,
				fieldsCreator.hiveParquetDataTypeMapping(inputFileParquetEntity
						.getFieldsList()));
	}

	@Override
	public void initializeEntity(InputFileParquetEntity assemblyEntityBase) {
		this.inputFileParquetEntity = assemblyEntityBase;
	}

}