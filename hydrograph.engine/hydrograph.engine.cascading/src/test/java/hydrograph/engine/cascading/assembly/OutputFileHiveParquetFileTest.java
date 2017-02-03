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
import hydrograph.engine.cascading.assembly.OutputFileHiveParquetAssembly;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.cascading.coercetype.StrictDateType;
import hydrograph.engine.core.component.entity.OutputFileHiveParquetEntity;
import hydrograph.engine.core.component.entity.elements.SchemaField;
import hydrograph.engine.utilites.AssemblyBuildHelper;
import hydrograph.engine.utilites.CascadingTestCase;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("rawtypes")
public class OutputFileHiveParquetFileTest {

	@Test
	public void itShouldTestHiveParquetOutputFileAssembly() throws IOException {

		String inPath = "../elt-command-line/testData/Input/Source1-delimited.txt";
		String databaseName = "dSimple";
		String tableName = "tsimple";

		Properties runtimeProp = new Properties();
		runtimeProp.setProperty("prop", "propValue");

		StrictDateType strictDateType = new StrictDateType("yyyy-MM-dd");
		Fields fields = new Fields("id", "fname", "lname", "salary").applyTypes(Integer.class, String.class,
				strictDateType, BigDecimal.class);

		FlowDef flowDef = FlowDef.flowDef();
		Pipe pipes = AssemblyBuildHelper.generateInputPipes(fields, ",", false, flowDef, inPath);

		OutputFileHiveParquetEntity entity = new OutputFileHiveParquetEntity();

		List<SchemaField> fieldList = new ArrayList<SchemaField>();
		SchemaField sf1 = new SchemaField("id", "java.lang.Integer");
		SchemaField sf2 = new SchemaField("fname", "java.lang.String");
		SchemaField sf3 = new SchemaField("lname", "java.util.Date");
		SchemaField sf4 = new SchemaField("salary", "java.math.BigDecimal");
		sf3.setFieldFormat("yyyy-MM-dd");
		sf4.setFieldScale(2);
		sf4.setFieldPrecision(6);
		sf4.setFieldScaleType("explicit");
		fieldList.add(sf1);
		fieldList.add(sf2);
		fieldList.add(sf3);
		fieldList.add(sf4);

		entity.setDatabaseName(databaseName);
		entity.setTableName(tableName);
		entity.setOverWrite(true);
		entity.setFieldsList(fieldList);
		entity.setRuntimeProperties(runtimeProp);

		ComponentParameters cpOutput = new ComponentParameters();
		cpOutput.addInputPipe(pipes);
		cpOutput.setFlowDef(flowDef);

		new OutputFileHiveParquetAssembly(entity, cpOutput);

		Flow flow = new Hadoop2MR1FlowConnector().connect(flowDef);
		flow.complete();

		CascadingTestCase.validateFieldLength(flow.openSink(), 4);
		CascadingTestCase.validateFileLength(flow.openSink(), 6);

	}

	@Test
	public void itShouldTestHiveParquetOutputFileAssemblyWithPartition() throws IOException {

		String inPath = "../elt-command-line/testData/Input/Source1-delimited.txt";
		String databaseName = "dPartition";
		String tableName = "tPartition";

		Properties runtimeProp = new Properties();
		runtimeProp.setProperty("prop", "propValue");

		StrictDateType strictDateType = new StrictDateType("yyyy-MM-dd");
		Fields fields = new Fields("id", "fname", "lname", "salary").applyTypes(Integer.class, String.class,
				strictDateType, BigDecimal.class);

		FlowDef flowDef = FlowDef.flowDef();
		Pipe pipes = AssemblyBuildHelper.generateInputPipes(fields, ",", false, flowDef, inPath);

		OutputFileHiveParquetEntity entity = new OutputFileHiveParquetEntity();

		List<SchemaField> fieldList = new ArrayList<SchemaField>();
		SchemaField sf1 = new SchemaField("id", "java.lang.Integer");
		SchemaField sf2 = new SchemaField("fname", "java.lang.String");
		SchemaField sf3 = new SchemaField("lname", "java.util.Date");
		SchemaField sf4 = new SchemaField("salary", "java.math.BigDecimal");
		sf3.setFieldFormat("yyyy-MM-dd");
		sf4.setFieldScale(2);
		sf4.setFieldPrecision(6);
		sf4.setFieldScaleType("explicit");
		fieldList.add(sf1);
		fieldList.add(sf2);
		fieldList.add(sf3);
		fieldList.add(sf4);

		String[] partitionField = new String[1];
		partitionField[0] = "id";

		entity.setDatabaseName(databaseName);
		entity.setTableName(tableName);
		entity.setOverWrite(true);
		entity.setFieldsList(fieldList);
		entity.setPartitionKeys(partitionField);
		entity.setRuntimeProperties(runtimeProp);

		ComponentParameters cpOutput = new ComponentParameters();
		cpOutput.addInputPipe(pipes);
		cpOutput.setFlowDef(flowDef);

		new OutputFileHiveParquetAssembly(entity, cpOutput);

		Flow flow = new Hadoop2MR1FlowConnector().connect(flowDef);
		flow.complete();
		// Field length should be 3 as one column will be partition column.
		// Currently ParquetHive is generating all columns in output file even
		// with partition.
		CascadingTestCase.validateFieldLength(flow.openSink(), 4);
		CascadingTestCase.validateFileLength(flow.openSink(), 6);
	}

	@Test
	public void itShouldTestHiveParquetOutputFileAssemblyWithExternalTable() throws IOException {

		String inPath = "../elt-command-line/testData/Input/Source1-delimited.txt";
		String databaseName = "dExternal";
		String tableName = "tExternalTable";
		// Full path is necessary for external Table.
		String externalTablePathUri = "C:/Users/kishorb/git/elt/elt-command-line/testData/Output/HiveParquetOutputExternalTable";

		Properties runtimeProp = new Properties();
		runtimeProp.setProperty("prop", "propValue");

		StrictDateType strictDateType = new StrictDateType("yyyy-MM-dd");
		Fields fields = new Fields("id", "fname", "lname", "salary").applyTypes(Integer.class, String.class,
				strictDateType, BigDecimal.class);

		FlowDef flowDef = FlowDef.flowDef();
		Pipe pipes = AssemblyBuildHelper.generateInputPipes(fields, ",", false, flowDef, inPath);

		OutputFileHiveParquetEntity entity = new OutputFileHiveParquetEntity();

		List<SchemaField> fieldList = new ArrayList<SchemaField>();
		SchemaField sf1 = new SchemaField("id", "java.lang.Integer");
		SchemaField sf2 = new SchemaField("fname", "java.lang.String");
		SchemaField sf3 = new SchemaField("lname", "java.util.Date");
		SchemaField sf4 = new SchemaField("salary", "java.math.BigDecimal");
		sf3.setFieldFormat("yyyy-MM-dd");
		sf4.setFieldScale(2);
		sf4.setFieldPrecision(6);
		sf4.setFieldScaleType("explicit");
		fieldList.add(sf1);
		fieldList.add(sf2);
		fieldList.add(sf3);
		fieldList.add(sf4);

		entity.setDatabaseName(databaseName);
		entity.setTableName(tableName);
		entity.setOverWrite(true);
		entity.setFieldsList(fieldList);
		entity.setExternalTablePathUri(externalTablePathUri);
		entity.setRuntimeProperties(runtimeProp);

		ComponentParameters cpOutput = new ComponentParameters();
		cpOutput.addInputPipe(pipes);
		cpOutput.setFlowDef(flowDef);

		new OutputFileHiveParquetAssembly(entity, cpOutput);

		Flow flow = new Hadoop2MR1FlowConnector().connect(flowDef);
		flow.complete();

		CascadingTestCase.validateFieldLength(flow.openSink(), 4);
		CascadingTestCase.validateFileLength(flow.openSink(), 6);

	}

	@Test
	public void itShouldTestHiveParquetOutputFileAssemblyWithPartitionAndExternalTable() throws IOException {

		String inPath = "../elt-command-line/testData/Input/Source1-delimited.txt";
		String databaseName = "dPartitionExternal";
		String tableName = "tPartitionExternal";
		// Full path is necessary for external Table.
		String externalTablePathUri = "C:/Users/kishorb/git/elt/elt-command-line/testData/Output/HiveParquetOutputPartitionExternalTable";
		Properties runtimeProp = new Properties();
		runtimeProp.setProperty("prop", "propValue");

		StrictDateType strictDateType = new StrictDateType("yyyy-MM-dd");
		Fields fields = new Fields("id", "fname", "lname", "salary").applyTypes(Integer.class, String.class,
				strictDateType, BigDecimal.class);

		FlowDef flowDef = FlowDef.flowDef();
		Pipe pipes = AssemblyBuildHelper.generateInputPipes(fields, ",", false, flowDef, inPath);

		OutputFileHiveParquetEntity entity = new OutputFileHiveParquetEntity();

		List<SchemaField> fieldList = new ArrayList<SchemaField>();
		SchemaField sf1 = new SchemaField("id", "java.lang.Integer");
		SchemaField sf2 = new SchemaField("fname", "java.lang.String");
		SchemaField sf3 = new SchemaField("lname", "java.util.Date");
		SchemaField sf4 = new SchemaField("salary", "java.math.BigDecimal");
		sf3.setFieldFormat("yyyy-MM-dd");
		sf4.setFieldScale(2);
		sf4.setFieldPrecision(6);
		sf4.setFieldScaleType("explicit");
		fieldList.add(sf1);
		fieldList.add(sf2);
		fieldList.add(sf3);
		fieldList.add(sf4);

		String[] partitionField = new String[1];
		partitionField[0] = "id";

		entity.setDatabaseName(databaseName);
		entity.setTableName(tableName);
		entity.setOverWrite(true);
		entity.setFieldsList(fieldList);
		entity.setExternalTablePathUri(externalTablePathUri);
		entity.setRuntimeProperties(runtimeProp);
		entity.setPartitionKeys(partitionField);

		ComponentParameters cpOutput = new ComponentParameters();
		cpOutput.addInputPipe(pipes);
		cpOutput.setFlowDef(flowDef);

		new OutputFileHiveParquetAssembly(entity, cpOutput);

		Flow flow = new Hadoop2MR1FlowConnector().connect(flowDef);
		flow.complete();

		CascadingTestCase.validateFieldLength(flow.openSink(), 4);
		CascadingTestCase.validateFileLength(flow.openSink(), 6);

	}
}
