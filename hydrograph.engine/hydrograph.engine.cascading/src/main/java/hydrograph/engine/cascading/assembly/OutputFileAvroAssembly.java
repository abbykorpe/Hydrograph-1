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

import hydrograph.engine.assembly.entity.OutputFileAvroEntity;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.avro.scheme.AvroDescriptor;
import hydrograph.engine.cascading.avro.scheme.CustomAvroScheme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cascading.flow.FlowDef;
import cascading.pipe.Pipe;
import cascading.scheme.Scheme;
import cascading.tap.SinkMode;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class OutputFileAvroAssembly extends BaseComponent {

	private static final long serialVersionUID = 4184919036703029509L;
	private OutputFileAvroEntity outputFileAvroEntity;
	@SuppressWarnings("rawtypes")
	private Scheme scheme;
	private String filePathToWrite;
	private Pipe tailPipe;
	private FlowDef flowDef;
	private Hfs outTap;
	private static Logger LOG = LoggerFactory
			.getLogger(OutputFileAvroAssembly.class);

	public OutputFileAvroAssembly(AssemblyEntityBase assemblyEntityBase,
			ComponentParameters componentParameters) {
		super(assemblyEntityBase, componentParameters);
	}

	@Override
	public void castEntityFromBase(AssemblyEntityBase assemblyEntityBase) {
		outputFileAvroEntity = (OutputFileAvroEntity) assemblyEntityBase;
	}

	@Override
	protected void createAssembly() {

		if (LOG.isTraceEnabled()) {
			LOG.trace(outputFileAvroEntity.toString());
		}
		LOG.trace("Creating output file Avro assembly for '"
				+ outputFileAvroEntity.getComponentId() + "'");
		flowDef = componentParameters.getFlowDef();
		filePathToWrite = outputFileAvroEntity.getPath();
		tailPipe = componentParameters.getInputPipe();
		prepareScheme();
		Pipe sinkPipe = new Pipe(outputFileAvroEntity.getComponentId(),
				tailPipe);
		setHadoopProperties(outTap.getStepConfigDef());
		setHadoopProperties(sinkPipe.getStepConfigDef());
		flowDef = flowDef.addTailSink(sinkPipe, outTap);
	}

	@SuppressWarnings("unchecked")
	public void prepareScheme() {
		String[] outputFields = new String[outputFileAvroEntity
				.getSchemaFieldsList().size()];
		String[] fieldDataTypes = new String[outputFileAvroEntity
				.getSchemaFieldsList().size()];
		int[] fieldScale = new int[outputFileAvroEntity
				.getSchemaFieldsList().size()];
		int[] fieldPrecision = new int[outputFileAvroEntity
				.getSchemaFieldsList().size()];
		for (int i = 0; i < outputFileAvroEntity.getSchemaFieldsList()
				.size(); i++) {
			outputFields[i] = outputFileAvroEntity.getSchemaFieldsList()
					.get(i).getFieldName();
			fieldDataTypes[i] = outputFileAvroEntity.getSchemaFieldsList()
					.get(i).getFieldDataType();
			fieldScale[i] = outputFileAvroEntity.getSchemaFieldsList()
					.get(i).getFieldScale();
			fieldPrecision[i] = outputFileAvroEntity.getSchemaFieldsList()
					.get(i).getFieldPrecision();
		}
		Fields fields = new Fields(outputFields);
		AvroDescriptor avroDescriptor = new AvroDescriptor(fields,
				dataTypeMapping(fieldDataTypes), fieldPrecision, fieldScale);

		scheme = new CustomAvroScheme(avroDescriptor);
		if (outputFileAvroEntity.isOverWrite())
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.REPLACE);
		else
			outTap = new Hfs(scheme, filePathToWrite, SinkMode.KEEP);
	}

	private Class<?>[] dataTypeMapping(String[] fieldDataTypes) {
		Class<?>[] types = new Class<?>[fieldDataTypes.length];
		for (int i = 0; i < fieldDataTypes.length; i++) {
			try {
				types[i] = Class.forName(fieldDataTypes[i]);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return types;
	}
}
