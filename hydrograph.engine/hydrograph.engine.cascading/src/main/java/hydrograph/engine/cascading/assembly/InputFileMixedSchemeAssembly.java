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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowDef;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;
import hydrograph.engine.assembly.entity.InputFileMixedSchemeEntity;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.assembly.entity.elements.OutSocket;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.assembly.utils.IOFieldsAndTypesCreator;
import hydrograph.engine.cascading.scheme.TextDelimitedAndFixedWidth;

public class InputFileMixedSchemeAssembly extends BaseComponent {

	private static final long serialVersionUID = 7857965970250755857L;
	private static Logger LOG = LoggerFactory.getLogger(InputFileMixedSchemeAssembly.class);

	Pipe pipe;
	@SuppressWarnings("rawtypes")
	Tap tap;
	@SuppressWarnings("rawtypes")
	Scheme scheme;
	FlowDef flowDef;

	private InputFileMixedSchemeEntity inputFileMixedSchemeEntity;

	private IOFieldsAndTypesCreator<InputFileMixedSchemeEntity> fieldsCreator;

	public InputFileMixedSchemeAssembly(AssemblyEntityBase baseComponentEntity,
			ComponentParameters componentParameters) {
		super(baseComponentEntity, componentParameters);
	}

	@Override
	public void castEntityFromBase(AssemblyEntityBase assemblyEntityBase) {
		inputFileMixedSchemeEntity = (InputFileMixedSchemeEntity) assemblyEntityBase;
	}

	@Override
	protected void createAssembly() {
		fieldsCreator = new IOFieldsAndTypesCreator<InputFileMixedSchemeEntity>(inputFileMixedSchemeEntity);
		try {
			generateTapsAndPipes();
			flowDef = flowDef.addSource(pipe, tap);

			if (LOG.isTraceEnabled()) {
				LOG.trace(inputFileMixedSchemeEntity.toString());
			}
			for (OutSocket outSocket : inputFileMixedSchemeEntity.getOutSocketList()) {
				LOG.trace("Creating input file mixed scheme assembly for '"
						+ inputFileMixedSchemeEntity.getComponentId() + "' for socket: '" + outSocket.getSocketId()
						+ "' of type: '" + outSocket.getSocketType() + "'");
				setOutLink(outSocket.getSocketType(), outSocket.getSocketId(),
						inputFileMixedSchemeEntity.getComponentId(), pipe, scheme.getSourceFields());
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
	}

	@SuppressWarnings("unchecked")
	public void generateTapsAndPipes() throws IOException {
		try {
			prepareScheme();
		} catch (Exception e) {
			LOG.error("Error in preparing scheme for component '" + inputFileMixedSchemeEntity.getComponentId() + "': "
					+ e.getMessage());
			throw new RuntimeException(e);
		}
		flowDef = componentParameters.getFlowDef();

		// initializing each pipe and tap
		tap = new Hfs(scheme, inputFileMixedSchemeEntity.getPath());
		pipe = new Pipe(inputFileMixedSchemeEntity.getComponentId());

		setHadoopProperties(tap.getStepConfigDef());
		setHadoopProperties(pipe.getStepConfigDef());
	}

	public void prepareScheme() {

		Fields inputFields = fieldsCreator.makeFieldsWithTypes();
		scheme = new TextDelimitedAndFixedWidth(inputFields, fieldsCreator.getFieldLengthOrDelimiter(),
				fieldsCreator.getTypeFieldLengthDelimiter(), inputFields.getTypes(),
				inputFileMixedSchemeEntity.getStrict(), inputFileMixedSchemeEntity.getSafe(),
				inputFileMixedSchemeEntity.getCharset());

	}

}
