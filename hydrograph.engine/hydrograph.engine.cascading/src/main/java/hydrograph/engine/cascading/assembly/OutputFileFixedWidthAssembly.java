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

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowDef;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.InputOutputFieldsAndTypesCreator;
import hydrograph.engine.cascading.scheme.TextFixedWidth;
import hydrograph.engine.core.component.entity.OutputFileFixedWidthEntity;
import hydrograph.engine.utilities.ComponentHelper;

public class OutputFileFixedWidthAssembly extends BaseComponent<OutputFileFixedWidthEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8758826605729474551L;
	private Pipe tailPipe;
	@SuppressWarnings("rawtypes")
	Tap outTap;
	@SuppressWarnings("rawtypes")
	Scheme scheme;
	FlowDef flowDef;
	String filePathToWrite;
	private OutputFileFixedWidthEntity outputFileFixedWidthEntity;
	private static Logger LOG = LoggerFactory.getLogger(OutputFileFixedWidthAssembly.class);

	private InputOutputFieldsAndTypesCreator<OutputFileFixedWidthEntity> fieldsCreator;

	public OutputFileFixedWidthAssembly(OutputFileFixedWidthEntity outputFileFixedWidthEntity,
			ComponentParameters componentParameters) {
		super(outputFileFixedWidthEntity, componentParameters);
	}

	@Override
	protected void createAssembly() {
		try {
			fieldsCreator = new InputOutputFieldsAndTypesCreator<OutputFileFixedWidthEntity>(
					outputFileFixedWidthEntity);
			LOG.debug("OutputFile Fixed Width Component: [ Fields List : "
					+ Arrays.toString(fieldsCreator.getFieldNames()) + ", Field Types : "
					+ Arrays.toString(fieldsCreator.getFieldDataTypes()) + ", Field Length : "
					+ Arrays.toString(fieldsCreator.getFieldLength()) + " , Path : "
					+ outputFileFixedWidthEntity.getPath() + ", Batch : " + outputFileFixedWidthEntity.getBatch()
					+ "]");
			if (LOG.isTraceEnabled()) {
				LOG.trace(outputFileFixedWidthEntity.toString());
			}
			LOG.trace("Creating output file fixed width assembly for '" + outputFileFixedWidthEntity.getComponentId()
					+ "'");
			prepareAssembly();
			Pipe sinkPipe = new Pipe(outputFileFixedWidthEntity.getComponentId()+"", tailPipe);
			setOutLink("output","NoSocketId",
					outputFileFixedWidthEntity.getComponentId(), sinkPipe, componentParameters
					.getInputFieldsList().get(0));
			setHadoopProperties(outTap.getStepConfigDef());
			setHadoopProperties(sinkPipe.getStepConfigDef());
			flowDef = flowDef.addTailSink(sinkPipe, outTap);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	public void prepareScheme() {
		int[] fieldLength = new int[outputFileFixedWidthEntity.getFieldsList().size()];

		for (int i = 0; i < outputFileFixedWidthEntity.getFieldsList().size(); i++) {
			fieldLength[i] = outputFileFixedWidthEntity.getFieldsList().get(i).getFieldLength();
		}

		Fields outputFields = fieldsCreator.makeFieldsWithTypes();
		scheme = new TextFixedWidth(outputFields, fieldLength, outputFields.getTypes(),
				outputFileFixedWidthEntity.isStrict(), outputFileFixedWidthEntity.isSafe(),
				outputFileFixedWidthEntity.getCharset());
	}

	@SuppressWarnings("unchecked")
	private void prepareAssembly() {
		flowDef = componentParameters.getFlowDef();
		filePathToWrite = outputFileFixedWidthEntity.getPath();
		tailPipe = componentParameters.getInputPipe();
		try {
			prepareScheme();
		} catch (Exception e) {
			LOG.error("Error in preparing scheme for component '" + outputFileFixedWidthEntity.getComponentId() + "': "
					+ e.getMessage());
			throw new RuntimeException(e);
		}
		if (outputFileFixedWidthEntity.isOverWrite())
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.REPLACE);
		else
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.KEEP);
	}

	@Override
	public void initializeEntity(OutputFileFixedWidthEntity assemblyEntityBase) {
		this.outputFileFixedWidthEntity = assemblyEntityBase;
	}
}