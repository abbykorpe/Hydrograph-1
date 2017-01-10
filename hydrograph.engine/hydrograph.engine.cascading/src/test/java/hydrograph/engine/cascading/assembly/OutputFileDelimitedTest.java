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

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop2.Hadoop2MR1FlowConnector;
import cascading.pipe.Pipe;
import cascading.tuple.Fields;
import hydrograph.engine.cascading.assembly.OutputFileDelimitedAssembly;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.coercetype.StrictDateType;
import hydrograph.engine.core.component.entity.OutputFileDelimitedEntity;
import hydrograph.engine.core.component.entity.elements.SchemaField;
import hydrograph.engine.utilites.AssemblyBuildHelper;
import hydrograph.engine.utilites.CascadingTestCase;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static data.InputData.outputFileDelimited;

@SuppressWarnings({ "rawtypes", "unused" })
public class OutputFileDelimitedTest {

	@Test
	public void itShouldTestDelimitedOutputFileAssemblyWithOptionalParameters()
			throws IOException {

		String outPath = "testData/component/output/delimitedOPFile";
		Properties runtimeProp = new Properties();
		runtimeProp.setProperty("prop", "propValue");
		StrictDateType strictDateType = new StrictDateType("dd-MM-yyyy");
		Fields fields = new Fields("name", "surname", "city", "val", "date")
				.applyTypes(String.class, String.class, String.class,
						BigDecimal.class, strictDateType);
		FlowDef flowDef = FlowDef.flowDef();

		Pipe pipes = AssemblyBuildHelper.generateInputPipes(fields, flowDef,
				outputFileDelimited);

		OutputFileDelimitedEntity entity = new OutputFileDelimitedEntity();
		entity.setPath(outPath);

		List<SchemaField> fieldList = new ArrayList<SchemaField>();
		SchemaField sf1 = new SchemaField("name", "java.lang.String");
		SchemaField sf2 = new SchemaField("surname", "java.lang.String");
		SchemaField sf3 = new SchemaField("city", "java.lang.String");
		SchemaField sf4 = new SchemaField("val", "java.math.BigDecimal");
		sf4.setFieldScale(3);
		sf4.setFieldScaleType("explicit");
		SchemaField sf5 = new SchemaField("date", "java.util.Date");
		sf5.setFieldFormat("yyyy-MM-dd");

		fieldList.add(sf1);
		fieldList.add(sf2);
		fieldList.add(sf3);
		fieldList.add(sf4);
		fieldList.add(sf5);

		entity.setFieldsList(fieldList);
		entity.setOverWrite(true);
		entity.setDelimiter(",");
		entity.setHasHeader(false);
		entity.setCharset("UTF-8");
		entity.setRuntimeProperties(runtimeProp);

		ComponentParameters cpOutput = new ComponentParameters();
		cpOutput.addInputPipe(pipes);
		cpOutput.setFlowDef(flowDef);
		cpOutput.addInputFields(fields);

		new OutputFileDelimitedAssembly(entity, cpOutput);

		Flow flow = new Hadoop2MR1FlowConnector().connect(flowDef);
		flow.complete();

		CascadingTestCase.validateFileLength(flow.openSink(), 3);

	}

	@Test
	public void itShouldTestDelimitedOutputFileAssemblyWithoutOptionalParameters()
			throws IOException {

		String outPath = "testData/component/output/delimitedOPFile1";

		StrictDateType strictDateType = new StrictDateType("dd-MM-yyyy");
		Fields fields = new Fields("name", "surname", "city", "val", "date")
				.applyTypes(String.class, String.class, String.class,
						BigDecimal.class, strictDateType);
		FlowDef flowDef = FlowDef.flowDef();

		Pipe pipes = AssemblyBuildHelper.generateInputPipes(fields, flowDef,
				outputFileDelimited);

		OutputFileDelimitedEntity entity = new OutputFileDelimitedEntity();
		entity.setPath(outPath);
		entity.setOverWrite(true);
		entity.setDelimiter(",");
		List<SchemaField> fieldList = new ArrayList<SchemaField>();
		SchemaField sf1 = new SchemaField("name", "java.lang.String");
		SchemaField sf2 = new SchemaField("surname", "java.lang.String");
		SchemaField sf3 = new SchemaField("city", "java.lang.String");
		SchemaField sf4 = new SchemaField("val", "java.math.BigDecimal");
		sf4.setFieldScale(3);
		sf4.setFieldScaleType("explicit");
		SchemaField sf5 = new SchemaField("date", "java.util.Date");
		sf5.setFieldFormat("yyyy-MM-dd");

		fieldList.add(sf1);
		fieldList.add(sf2);
		fieldList.add(sf3);
		fieldList.add(sf4);
		fieldList.add(sf5);

		entity.setFieldsList(fieldList);
		ComponentParameters cpOutput = new ComponentParameters();
		cpOutput.addInputPipe(pipes);
		cpOutput.setFlowDef(flowDef);
		cpOutput.addInputFields(fields);

		new OutputFileDelimitedAssembly(entity, cpOutput);

		Flow flow = new Hadoop2MR1FlowConnector().connect(flowDef);
		flow.complete();

		CascadingTestCase.validateFileLength(flow.openSink(), 3);
	}
}
