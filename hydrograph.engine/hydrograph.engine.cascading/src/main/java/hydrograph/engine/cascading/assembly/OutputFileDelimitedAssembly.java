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
import cascading.scheme.hadoop.TextDelimited;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.InputOutputFieldsAndTypesCreator;
import hydrograph.engine.cascading.scheme.HydrographDelimitedParser;
import hydrograph.engine.core.component.entity.OutputFileDelimitedEntity;
import hydrograph.engine.utilities.ComponentHelper;

public class OutputFileDelimitedAssembly extends BaseComponent<OutputFileDelimitedEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4184919036703029509L;
	private OutputFileDelimitedEntity outputFileDelimitedEntity;
	FlowDef flowDef;
	String filePathToWrite = outputFileDelimitedEntity.getPath();
	Pipe tailPipe;
	@SuppressWarnings("rawtypes")
	Tap outTap;
	@SuppressWarnings("rawtypes")
	Scheme scheme;
	private static Logger LOG = LoggerFactory.getLogger(OutputFileDelimitedAssembly.class);

	private InputOutputFieldsAndTypesCreator<OutputFileDelimitedEntity> fieldsCreator;

	public OutputFileDelimitedAssembly(OutputFileDelimitedEntity parameters, ComponentParameters componentParameters) {
		super(parameters, componentParameters);
	}

	@Override
	public void initializeEntity(OutputFileDelimitedEntity graphTypeImpl) {
		outputFileDelimitedEntity = graphTypeImpl;
	}

	@Override
	protected void createAssembly() {
		try {
		fieldsCreator = new InputOutputFieldsAndTypesCreator<OutputFileDelimitedEntity>(outputFileDelimitedEntity);
			LOG.debug("OutputFile Delimited Component: [ Fields List : "
					+ Arrays.toString(fieldsCreator.getFieldNames()) + ", Field Types : "
					+ Arrays.toString(fieldsCreator.getFieldDataTypes()) + ", Delimiter : '"
					+ outputFileDelimitedEntity.getDelimiter() + "' , Path : " + outputFileDelimitedEntity.getPath()
					+ ", Batch : " + outputFileDelimitedEntity.getBatch() + "]");
			if (LOG.isTraceEnabled()) {
				LOG.trace(outputFileDelimitedEntity.toString());
			}
			LOG.trace(
					"Creating output file delimited assembly for '" + outputFileDelimitedEntity.getComponentId() + "'");
			prepareAssembly();
			Pipe sinkPipe = new Pipe(outputFileDelimitedEntity.getComponentId()+"", tailPipe);
			setOutLink("output","NoSocketId",
					outputFileDelimitedEntity.getComponentId(), sinkPipe, componentParameters
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
	private void prepareAssembly() {
		flowDef = componentParameters.getFlowDef();
		filePathToWrite = outputFileDelimitedEntity.getPath();
		tailPipe = componentParameters.getInputPipe();
		// boolean isMultiplePartitions = tailPipes.length > 1 ? true : false;
		try {
			prepareScheme();
		} catch (Exception e) {
			LOG.error("Error in preparing scheme for component '" + outputFileDelimitedEntity.getComponentId() + "': "
					+ e.getMessage());
			throw new RuntimeException(e);
		}
		if (outputFileDelimitedEntity.isOverWrite())
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.REPLACE);
		else
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.KEEP);
	}

	public void prepareScheme() {

		Fields outputFields = fieldsCreator.makeFieldsWithTypes();
		HydrographDelimitedParser delimitedParser = new HydrographDelimitedParser(outputFileDelimitedEntity.getDelimiter(),
				outputFileDelimitedEntity.getQuote(), null, outputFileDelimitedEntity.isStrict(),
				outputFileDelimitedEntity.getSafe());
		scheme = new TextDelimited(outputFields, null, false, outputFileDelimitedEntity.getHasHeader(),
				outputFileDelimitedEntity.getCharset(), delimitedParser);
	}
}
