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

import hydrograph.engine.assembly.entity.OutputFileParquetEntity;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.IOFieldsAndTypesCreator;
import hydrograph.engine.cascading.parquet.scheme.ParquetTupleScheme;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowDef;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.tap.SinkMode;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class OutputFileParquetAssembly extends BaseComponent {

	private static final long serialVersionUID = 4184919036703029509L;
	private OutputFileParquetEntity outputFileParquetEntity;
	@SuppressWarnings("rawtypes")
	private Scheme scheme;
	private String filePathToWrite;
	private Pipe tailPipe;
	private FlowDef flowDef;
	private Hfs outTap;
	private static Logger LOG = LoggerFactory
			.getLogger(OutputFileParquetAssembly.class);

	private IOFieldsAndTypesCreator<OutputFileParquetEntity> fieldsCreator;

	public OutputFileParquetAssembly(AssemblyEntityBase assemblyEntityBase,
			ComponentParameters componentParameters) {
		super(assemblyEntityBase, componentParameters);
	}

	@Override
	public void castEntityFromBase(AssemblyEntityBase assemblyEntityBase) {
		outputFileParquetEntity = (OutputFileParquetEntity) assemblyEntityBase;
	}

	@Override
	protected void createAssembly() {
		try {
			fieldsCreator = new IOFieldsAndTypesCreator<OutputFileParquetEntity>(
					outputFileParquetEntity);
			LOG.debug("OutputFile Parquet Component: [ Fields List : "
					+ Arrays.toString(fieldsCreator.getFieldNames())
					+ ", Field Types : "
					+ Arrays.toString(fieldsCreator.getFieldDataTypes())
					+ " , Path : " + outputFileParquetEntity.getPath()
					+ ", Phase : " + outputFileParquetEntity.getPhase() + "]");
			if (LOG.isTraceEnabled()) {
				LOG.trace(outputFileParquetEntity.toString());
			}
			LOG.trace("Creating output file parquet assembly for '"
					+ outputFileParquetEntity.getComponentId() + "'");
			flowDef = componentParameters.getFlowDef();
			filePathToWrite = outputFileParquetEntity.getPath();
			tailPipe = componentParameters.getInputPipe();
			try {
				prepareScheme();
			} catch (Exception e) {
				LOG.error("Error in preparing scheme for component '"
						+ outputFileParquetEntity.getComponentId() + "': "
						+ e.getMessage());
				throw new RuntimeException(e);
			}
			Pipe sinkPipe = new Pipe(outputFileParquetEntity.getComponentId(),
					tailPipe);
			setHadoopProperties(outTap.getStepConfigDef());
			setHadoopProperties(sinkPipe.getStepConfigDef());
			flowDef = flowDef.addTailSink(sinkPipe, outTap);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void prepareScheme() {
		Fields fields = fieldsCreator.makeFields();
		scheme = new ParquetTupleScheme(fields,
				fieldsCreator
						.hiveParquetDataTypeMapping(outputFileParquetEntity
								.getFieldsList()));
		if (outputFileParquetEntity.isOverWrite())
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.REPLACE);
		else
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.KEEP);
	}
}
