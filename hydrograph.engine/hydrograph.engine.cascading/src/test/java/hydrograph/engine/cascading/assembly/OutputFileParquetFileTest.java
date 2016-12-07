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

import static data.InputData.outputFileParquet;

import hydrograph.engine.cascading.assembly.OutputFileParquetAssembly;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.component.entity.OutputFileParquetEntity;
import hydrograph.engine.core.component.entity.elements.SchemaField;
import hydrograph.engine.utilites.AssemblyBuildHelper;
import hydrograph.engine.utilites.CascadingTestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop2.Hadoop2MR1FlowConnector;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;

@SuppressWarnings("rawtypes")
public class OutputFileParquetFileTest {

	@Test
	public void itShouldTestParquetOutputFileAssemblyWithOptionalParameters() throws IOException {

		String outPath = "testData/component/output/parquetOPFile";

		Properties runtimeProp = new Properties();
		runtimeProp.setProperty("prop", "propValue");

		Fields fields = new Fields("name", "surname", "city", "val", "date");
		FlowDef flowDef = FlowDef.flowDef();
		Pipe pipes = AssemblyBuildHelper.generateInputPipes(fields, ",", true, flowDef, outputFileParquet);

		OutputFileParquetEntity entity = new OutputFileParquetEntity();
		entity.setPath(outPath);
		entity.setOverWrite(true);
		List<SchemaField> fieldList = new ArrayList<SchemaField>();
		SchemaField sf1 = new SchemaField("name", "java.lang.String");
		SchemaField sf2 = new SchemaField("surname", "java.lang.String");
		SchemaField sf3 = new SchemaField("city", "java.lang.String");
		SchemaField sf4 = new SchemaField("val", "java.lang.String");
		SchemaField sf5 = new SchemaField("date", "java.lang.String");

		fieldList.add(sf1);
		fieldList.add(sf2);
		fieldList.add(sf3);
		fieldList.add(sf4);
		fieldList.add(sf5);

		entity.setFieldsList(fieldList);
		entity.setRuntimeProperties(runtimeProp);

		ComponentParameters cpOutput = new ComponentParameters();
		cpOutput.addInputPipe(pipes);
		cpOutput.setFlowDef(flowDef);
		cpOutput.addInputFields(fields);

		new OutputFileParquetAssembly(entity, cpOutput);

		Flow flow = new Hadoop2MR1FlowConnector().connect(flowDef);
		flow.complete();

		CascadingTestCase.validateFileLength(flow.openSink(), 3);

	}
}
