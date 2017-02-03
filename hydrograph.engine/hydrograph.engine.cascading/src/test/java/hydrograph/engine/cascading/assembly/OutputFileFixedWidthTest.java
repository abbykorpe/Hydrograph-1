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

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop2.Hadoop2MR1FlowConnector;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.OutputFileFixedWidthAssembly;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.component.entity.OutputFileFixedWidthEntity;
import hydrograph.engine.core.component.entity.elements.SchemaField;
import hydrograph.engine.utilites.AssemblyBuildHelper;
import hydrograph.engine.utilites.CascadingTestCase;
import org.apache.hadoop.conf.Configuration;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static data.InputData.outputFileFixedWidth;

@SuppressWarnings({ "rawtypes", "unused" })
public class OutputFileFixedWidthTest {

	@Test
	public void itShouldCheckFileLenghtOfDelimitedOutputFile()
			throws IOException {

		String outPath = "testData/component/input/output/fix_out";

		Fields fields = new Fields("fname", "lname", "doj", "d", "e");
		FlowDef flowDef = FlowDef.flowDef();

		Pipe pipes = AssemblyBuildHelper.generateInputPipes(fields, flowDef,
				outputFileFixedWidth, false);

		OutputFileFixedWidthEntity entity = new OutputFileFixedWidthEntity();
		List<SchemaField> fieldList = new ArrayList<SchemaField>();
		SchemaField sf1 = new SchemaField("fname", "java.lang.String");
		SchemaField sf2 = new SchemaField("lname", "java.lang.String");
		SchemaField sf3 = new SchemaField("doj", "java.lang.String");
		SchemaField sf4 = new SchemaField("d", "java.lang.String");
		SchemaField sf5 = new SchemaField("e", "java.lang.String");

		sf1.setFieldLength(7);
		sf2.setFieldLength(6);
		sf3.setFieldLength(10);
		sf4.setFieldLength(10);
		sf5.setFieldLength(10);

		fieldList.add(sf1);
		fieldList.add(sf2);
		fieldList.add(sf3);
		fieldList.add(sf4);
		fieldList.add(sf5);

		entity.setFieldsList(fieldList);
		entity.setOverWrite(true);
		entity.setPath(outPath);
		entity.setStrict(false);
		entity.setCharset("UTF-8");

		Properties runtimeProperties = new Properties();
		runtimeProperties.put("mapreduce.output.fileoutputformat.compress",
				"true");
		runtimeProperties.put(
				"mapreduce.output.fileoutputformat.compress.codec",
				"org.apache.hadoop.io.compress.GzipCodec");
		entity.setRuntimeProperties(runtimeProperties);

		ComponentParameters cpOutput = new ComponentParameters();
		cpOutput.addInputPipe(pipes);
		cpOutput.setFlowDef(flowDef);
		cpOutput.addInputFields(fields);

		new OutputFileFixedWidthAssembly(entity, cpOutput);

		Flow flow = new Hadoop2MR1FlowConnector().connect(flowDef);
		flow.complete();

		CascadingTestCase.validateFileLength(flow.openSink(), 4);
		Configuration conf = new Configuration();
		System.out.println(conf
				.get("mapreduce.output.fileoutputformat.compress.codec"));

	}
}
