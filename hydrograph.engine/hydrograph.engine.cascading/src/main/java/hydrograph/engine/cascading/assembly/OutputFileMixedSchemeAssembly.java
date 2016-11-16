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
import hydrograph.engine.cascading.scheme.TextDelimitedAndFixedWidth;
import hydrograph.engine.core.component.entity.OutputFileMixedSchemeEntity;
import hydrograph.engine.utilities.ComponentHelper;

public class OutputFileMixedSchemeAssembly extends BaseComponent<OutputFileMixedSchemeEntity> {

	private static final long serialVersionUID = -8758826605729474551L;

	private Pipe tailPipe;
	@SuppressWarnings("rawtypes")
	Tap outTap;
	@SuppressWarnings("rawtypes")
	Scheme scheme;
	FlowDef flowDef;
	String filePathToWrite;
	private OutputFileMixedSchemeEntity outputFileMixedSchemeEntity;
	private static Logger LOG = LoggerFactory.getLogger(OutputFileMixedSchemeAssembly.class);

	private InputOutputFieldsAndTypesCreator<OutputFileMixedSchemeEntity> fieldsCreator;

	public OutputFileMixedSchemeAssembly(OutputFileMixedSchemeEntity outputFileMixedSchemeEntity,
			ComponentParameters componentParameters) {
		super(outputFileMixedSchemeEntity, componentParameters);
	}

	@Override
	protected void createAssembly() {
		fieldsCreator = new InputOutputFieldsAndTypesCreator<OutputFileMixedSchemeEntity>(outputFileMixedSchemeEntity);
		if (LOG.isTraceEnabled()) {
			LOG.trace(outputFileMixedSchemeEntity.toString());
		}
		LOG.trace("Creating output file mixed scheme assembly for '" + outputFileMixedSchemeEntity.getComponentId()
				+ "'");
		prepareAssembly();
		Pipe sinkPipe = new Pipe(outputFileMixedSchemeEntity.getComponentId()+"", tailPipe);
		setOutLink("output","NoSocketId",
				outputFileMixedSchemeEntity.getComponentId(), sinkPipe, componentParameters
				.getInputFieldsList().get(0));
		setHadoopProperties(outTap.getStepConfigDef());
		setHadoopProperties(sinkPipe.getStepConfigDef());
		flowDef = componentParameters.getFlowDef().addTailSink(sinkPipe, outTap);
	}

	@SuppressWarnings("unchecked")
	private void prepareAssembly() {
		flowDef = componentParameters.getFlowDef();
		filePathToWrite = outputFileMixedSchemeEntity.getPath();
		tailPipe = componentParameters.getInputPipe();
		try {
			prepareScheme();
		} catch (Exception e) {
			LOG.error("Error in preparing scheme for component '" + outputFileMixedSchemeEntity.getComponentId() + "': "
					+ e.getMessage());
			throw new RuntimeException(e);
		}
		if (outputFileMixedSchemeEntity.isOverWrite())
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.REPLACE);
		else
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.KEEP);

	}

	public void prepareScheme() {

		Fields outputFields = fieldsCreator.makeFieldsWithTypes();
		scheme = new TextDelimitedAndFixedWidth(outputFields, fieldsCreator.getFieldLengthOrDelimiter(),
				fieldsCreator.getTypeFieldLengthDelimiter(), outputFields.getTypes(),
				outputFileMixedSchemeEntity.getStrict(), outputFileMixedSchemeEntity.getSafe(),
				outputFileMixedSchemeEntity.getCharset(), outputFileMixedSchemeEntity.getQuote());
	}

	@Override
	public void initializeEntity(OutputFileMixedSchemeEntity assemblyEntityBase) {
		this.outputFileMixedSchemeEntity = assemblyEntityBase;
	}
}
