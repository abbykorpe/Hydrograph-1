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

import cascading.flow.FlowDef;
import cascading.pipe.Pipe;
import cascading.scheme.hadoop.SequenceFile;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.InputOutputFieldsAndTypesCreator;
import hydrograph.engine.core.component.entity.OutputFileSequenceFormatEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputFileSequenceFormatAssembly extends BaseComponent<OutputFileSequenceFormatEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4184919036703029509L;
	private OutputFileSequenceFormatEntity outputFileSequenceFormatEntity;
	FlowDef flowDef;
	Pipe tailPipe;
	@SuppressWarnings("rawtypes")
	Tap outTap;
	SequenceFile scheme;
	private InputOutputFieldsAndTypesCreator<OutputFileSequenceFormatEntity> fieldsCreator;
	private static Logger LOG = LoggerFactory.getLogger(OutputFileSequenceFormatAssembly.class);

	public OutputFileSequenceFormatAssembly(OutputFileSequenceFormatEntity outputFileSequenceFormatEntity,
			ComponentParameters componentParameters) {
		super(outputFileSequenceFormatEntity, componentParameters);
	}

	@Override
	public void initializeEntity(OutputFileSequenceFormatEntity assemblyEntityBase) {
		this.outputFileSequenceFormatEntity = assemblyEntityBase;
	}
	
	@Override
	protected void createAssembly() {
		try {
			LOG.trace("Creating output file sequence format assembly for '"
					+ outputFileSequenceFormatEntity.getComponentId() + "'");
			fieldsCreator = new InputOutputFieldsAndTypesCreator<OutputFileSequenceFormatEntity>(outputFileSequenceFormatEntity);
			prepareAssembly();
			Pipe sinkPipe = new Pipe(outputFileSequenceFormatEntity.getComponentId()+"", tailPipe);
			setOutLink("output","NoSocketId",
					outputFileSequenceFormatEntity.getComponentId(), sinkPipe, componentParameters
					.getInputFieldsList().get(0));
			setHadoopProperties(outTap.getStepConfigDef());
			setHadoopProperties(sinkPipe.getStepConfigDef());
			flowDef = flowDef.addTailSink(sinkPipe, outTap);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	private void prepareAssembly() {
		flowDef = componentParameters.getFlowDef();
		String filePathToWrite = outputFileSequenceFormatEntity.getPath();
		tailPipe = componentParameters.getInputPipe();
		try {
			prepareScheme();
		} catch (Exception e) {
			LOG.error("Error in preparing scheme for component '" + outputFileSequenceFormatEntity.getComponentId()
					+ "': " + e.getMessage());
			throw new RuntimeException(e);
		}
		outTap = new Hfs(scheme, filePathToWrite, SinkMode.REPLACE);
	}

	public void prepareScheme() {
		Fields outputFields = fieldsCreator.makeFieldsWithTypes();
		scheme = new SequenceFile(outputFields);
	}
}