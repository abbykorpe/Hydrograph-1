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

import cascading.flow.FlowDef;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.tap.SinkMode;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.InputOutputFieldsAndTypesCreator;
import hydrograph.engine.cascading.scheme.parquet.ParquetTupleScheme;
import hydrograph.engine.core.component.entity.OutputFileParquetEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class OutputFileParquetAssembly extends BaseComponent<OutputFileParquetEntity> {

	private static final long serialVersionUID = 4184919036703029509L;
	private OutputFileParquetEntity outputFileParquetEntity;
	@SuppressWarnings("rawtypes")
	private Scheme scheme;
	private String filePathToWrite;
	private Pipe tailPipe;
	private FlowDef flowDef;
	private Hfs outTap;
	private static Logger LOG = LoggerFactory.getLogger(OutputFileParquetAssembly.class);

	private InputOutputFieldsAndTypesCreator<OutputFileParquetEntity> fieldsCreator;

	public OutputFileParquetAssembly(OutputFileParquetEntity assemblyEntityBase,
			ComponentParameters componentParameters) {
		super(assemblyEntityBase, componentParameters);
	}

	@Override
	protected void createAssembly() {
		try {
			fieldsCreator = new InputOutputFieldsAndTypesCreator<OutputFileParquetEntity>(outputFileParquetEntity);
			LOG.debug("OutputFile Parquet Component: [ Fields List : " + Arrays.toString(fieldsCreator.getFieldNames())
					+ ", Field Types : " + Arrays.toString(fieldsCreator.getFieldDataTypes()) + " , Path : "
					+ outputFileParquetEntity.getPath() + ", Batch : " + outputFileParquetEntity.getBatch() + "]");
			if (LOG.isTraceEnabled()) {
				LOG.trace(outputFileParquetEntity.toString());
			}
			LOG.trace("Creating output file parquet assembly for '" + outputFileParquetEntity.getComponentId() + "'");
			flowDef = componentParameters.getFlowDef();
			filePathToWrite = outputFileParquetEntity.getPath();
			tailPipe = componentParameters.getInputPipe();
			try {
				prepareScheme();
			} catch (Exception e) {
				LOG.error("Error in preparing scheme for component '" + outputFileParquetEntity.getComponentId() + "': "
						+ e.getMessage());
				throw new RuntimeException(e);
			}
			Pipe sinkPipe = new Pipe(outputFileParquetEntity.getComponentId()+"",
					tailPipe);
			setOutLink("output","NoSocketId",
					outputFileParquetEntity.getComponentId(), sinkPipe, componentParameters
					.getInputFieldsList().get(0));
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
				fieldsCreator.hiveParquetDataTypeMapping(outputFileParquetEntity.getFieldsList()));
		if (outputFileParquetEntity.isOverWrite())
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.REPLACE);
		else
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.KEEP);
	}

	@Override
	public void initializeEntity(OutputFileParquetEntity assemblyEntityBase) {
		this.outputFileParquetEntity = assemblyEntityBase;
	}
}
