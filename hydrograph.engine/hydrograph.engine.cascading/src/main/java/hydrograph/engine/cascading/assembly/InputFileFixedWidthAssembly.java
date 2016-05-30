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

import hydrograph.engine.assembly.entity.InputFileFixedWidthEntity;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.IOFieldsAndTypesCreator;
import hydrograph.engine.cascading.scheme.TextFixedWidth;

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

public class InputFileFixedWidthAssembly extends BaseComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8145806669418685707L;

	InputFileFixedWidthEntity inputFileFixedWidthEntity;
	Pipe pipe;
	@SuppressWarnings("rawtypes")
	Tap tap;
	@SuppressWarnings("rawtypes")
	Scheme scheme;
	FlowDef flowDef;
	private static Logger LOG = LoggerFactory
			.getLogger(InputFileFixedWidthAssembly.class);

	private IOFieldsAndTypesCreator<InputFileFixedWidthEntity> fieldsCreator;

	public InputFileFixedWidthAssembly(AssemblyEntityBase baseComponentEntity,
			ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	public void castEntityFromBase(AssemblyEntityBase assemblyEntityBase) {
		inputFileFixedWidthEntity = (InputFileFixedWidthEntity) assemblyEntityBase;
	}

	@Override
	protected void createAssembly() {
		try {
		fieldsCreator = new IOFieldsAndTypesCreator<InputFileFixedWidthEntity>(
				inputFileFixedWidthEntity);
		LOG.debug("InputFile Fixed Width Component: [ Fields List : " + Arrays.toString(fieldsCreator.getFieldNames()) + ", Field Types : "
				+ Arrays.toString(fieldsCreator.getFieldDataTypes()) + ", Field Length : " + Arrays.toString(fieldsCreator.getFieldLength())
				+ " , Path : " + inputFileFixedWidthEntity.getPath() + ", Phase : "
				+ inputFileFixedWidthEntity.getPhase() + "]");
			generateTapsAndPipes();
			flowDef = flowDef.addSource(pipe, tap);

			if (LOG.isTraceEnabled()) {
				LOG.trace(inputFileFixedWidthEntity.toString());
			}
			for (OutSocket outSocket : inputFileFixedWidthEntity
					.getOutSocketList()) {
				LOG.trace("Creating input file fixed width assembly for '"
						+ inputFileFixedWidthEntity.getComponentId()
						+ "' for socket: '" + outSocket.getSocketId()
						+ "' of type: '" + outSocket.getSocketType() + "'");
				setOutLink(outSocket.getSocketType(), outSocket.getSocketId(),
						inputFileFixedWidthEntity.getComponentId(), pipe,
						scheme.getSourceFields());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void generateTapsAndPipes() throws IOException {
		try{
			prepareScheme();
		}
		catch(Exception e) {
			LOG.error("Error in preparing scheme for component '"
					+ inputFileFixedWidthEntity.getComponentId() + "': " + e.getMessage());
			throw new RuntimeException(e);
		}
		flowDef = componentParameters.getFlowDef();

		// initializing each pipe and tap
		tap = new Hfs(scheme, inputFileFixedWidthEntity.getPath());
		pipe = new Pipe(inputFileFixedWidthEntity.getComponentId());
		setHadoopProperties(tap.getStepConfigDef());
		setHadoopProperties(pipe.getStepConfigDef());
	}

	protected void prepareScheme() {

		int[] fieldLength = new int[inputFileFixedWidthEntity.getFieldsList()
				.size()];
		for (int i = 0; i < inputFileFixedWidthEntity.getFieldsList().size(); i++) {
			fieldLength[i] = inputFileFixedWidthEntity.getFieldsList().get(i)
					.getFieldLength();
		}

		Fields inputFields = fieldsCreator.makeFieldsWithTypes();
		scheme = new TextFixedWidth(inputFields, fieldLength, null,
				inputFileFixedWidthEntity.isStrict(),
				inputFileFixedWidthEntity.isSafe(),
				inputFileFixedWidthEntity.getCharset());

	}

}