package hydrograph.engine.cascading.assembly;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop2.Hadoop2MR1FlowConnector;
import hydrograph.engine.cascading.assembly.infra.ComponentParameters;
import hydrograph.engine.core.component.entity.InputRDBMSEntity;
import hydrograph.engine.core.component.entity.elements.OutSocket;
import hydrograph.engine.core.component.entity.elements.SchemaField;
import hydrograph.engine.utilites.AssemblyBuildHelper;
import hydrograph.engine.utilites.CascadingTestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InputMysqlAssemblyTest {

	@Test
	public void itShouldCountNumberOfRecordReadFromMySqlDatabaseUsingTableName() throws IOException{
		
		String outPath = "testData/component/input/output/MySqlInput_UsingTable_output";
		
		InputRDBMSEntity inputRDBMSEntity=new InputRDBMSEntity();
		inputRDBMSEntity.setComponentId("inputMySql");
		inputRDBMSEntity.setDatabaseName("test");
		inputRDBMSEntity.setHostName("10.130.248.53");
		inputRDBMSEntity.setJdbcDriver("Connector/J");
		inputRDBMSEntity.setUsername("root");
		inputRDBMSEntity.setPassword("root");
		inputRDBMSEntity.setPort(3306);
		inputRDBMSEntity.setTableName("employee");
		
		
		List<SchemaField> fieldList = new ArrayList<SchemaField>();
		SchemaField sf1 = new SchemaField("eid", "java.lang.Integer");
		SchemaField sf2 = new SchemaField("ename", "java.lang.String");
		SchemaField sf3 = new SchemaField("salary", "java.lang.String");
		SchemaField sf4 = new SchemaField("city", "java.lang.String");
		SchemaField sf5 = new SchemaField("deptno", "java.lang.String");
		SchemaField sf6 = new SchemaField("dob", "java.util.Date");
	
		sf6.setFieldFormat("yyyy-MM-dd");
		
		fieldList.add(sf1);
		fieldList.add(sf2);
		fieldList.add(sf3);
		fieldList.add(sf4);
		fieldList.add(sf5);
		fieldList.add(sf6);

		inputRDBMSEntity.setFieldsList(fieldList);
		
		Properties runtimeProp = new Properties();
		runtimeProp.setProperty("prop", "propValue");

		inputRDBMSEntity.setRuntimeProperties(runtimeProp);
	
		List<OutSocket> outSockets = new ArrayList<OutSocket>();
		outSockets.add(new OutSocket("outSocket"));
		inputRDBMSEntity.setOutSocketList(outSockets);
		
		FlowDef flowDef = FlowDef.flowDef();

		ComponentParameters cpInput = new ComponentParameters();
		cpInput.setFlowDef(flowDef);
		
		InputMysqlAssembly inputMysqlAssembly = new InputMysqlAssembly(inputRDBMSEntity, cpInput);
		
		AssemblyBuildHelper.generateOutputPipes(
				inputMysqlAssembly.getOutLink("out", "outSocket", inputRDBMSEntity.getComponentId()), outPath, flowDef);

		Flow<?> flow = new Hadoop2MR1FlowConnector().connect(flowDef);
		flow.complete();
		
		CascadingTestCase.validateFileLength(flow.openSink(), 5);
	}
	
	@Test
	public void itShouldCountNumberOfRecordReadFromMySqlDatabaseUsingQuery() throws IOException{
		
		String outPath = "testData/component/input/output/MySqlInput_UsingQuery_output";
		
		InputRDBMSEntity inputRDBMSEntity=new InputRDBMSEntity(); 
		inputRDBMSEntity.setComponentId("inputMySql");
		inputRDBMSEntity.setDatabaseName("test");
		inputRDBMSEntity.setHostName("10.130.248.53");
		inputRDBMSEntity.setJdbcDriver("Connector/J");
		inputRDBMSEntity.setUsername("root");
		inputRDBMSEntity.setPassword("root");
		inputRDBMSEntity.setPort(3306);
		inputRDBMSEntity.setSelectQuery("select * from employee where salary=123.03 and city='Mumbai'");
		inputRDBMSEntity.setCountQuery("select count(*) from employee"); 
		
		
		List<SchemaField> fieldList = new ArrayList<SchemaField>();
		SchemaField sf1 = new SchemaField("eid", "java.lang.Integer");
		SchemaField sf2 = new SchemaField("ename", "java.lang.String");
		SchemaField sf3 = new SchemaField("salary", "java.lang.String");
		SchemaField sf4 = new SchemaField("city", "java.lang.String");
		SchemaField sf5 = new SchemaField("deptno", "java.lang.String");
		SchemaField sf6 = new SchemaField("dob", "java.util.Date");
	
		sf6.setFieldFormat("yyyy-MM-dd");
		
		fieldList.add(sf1);
		fieldList.add(sf2);
		fieldList.add(sf3);
		fieldList.add(sf4);
		fieldList.add(sf5);
		fieldList.add(sf6);

		inputRDBMSEntity.setFieldsList(fieldList);
		
		Properties runtimeProp = new Properties();
		runtimeProp.setProperty("prop", "propValue");

		inputRDBMSEntity.setRuntimeProperties(runtimeProp);
	
		List<OutSocket> outSockets = new ArrayList<OutSocket>();
		outSockets.add(new OutSocket("outSocket"));
		inputRDBMSEntity.setOutSocketList(outSockets);
		
		FlowDef flowDef = FlowDef.flowDef();

		ComponentParameters cpInput = new ComponentParameters();
		cpInput.setFlowDef(flowDef);
		
		InputMysqlAssembly inputMysqlAssembly = new InputMysqlAssembly(inputRDBMSEntity, cpInput);
		
		AssemblyBuildHelper.generateOutputPipes(
				inputMysqlAssembly.getOutLink("out", "outSocket", inputRDBMSEntity.getComponentId()), outPath, flowDef);

		Flow<?> flow = new Hadoop2MR1FlowConnector().connect(flowDef);
		flow.complete();
		
		CascadingTestCase.validateFileLength(flow.openSink(), 1);
	}
}
