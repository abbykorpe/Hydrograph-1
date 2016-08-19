package hydrograph.engine.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hydrograph.engine.commandline.utilities.HydrographService;
import hydrograph.engine.helper.StatusHelper;

public class ExecutionTrackingJoinXmlTest {

	static HydrographService hydrographService;
	static StatusHelper statusHelper;

	@BeforeClass
	public static void hydrographService() {
		String[] args = { "-xmlpath", "testData/XMLFiles/Joinexample.xml" };
		try {
			hydrographService = new HydrographService();
			hydrographService.executeGraph(args);
			statusHelper = new StatusHelper(hydrographService.getStatus());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void itShouldTestInput1Component() {
		Assert.assertEquals(statusHelper.getComponentId("input1"),"input1");
		Assert.assertEquals(statusHelper.getCurrentStatus("input1"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("input1").get("out0"),new Long(5));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input1").get("out0"),"SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestInput2Component() {
		Assert.assertEquals(statusHelper.getComponentId("input2"),"input2");
		Assert.assertEquals(statusHelper.getCurrentStatus("input2"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("input2").get("out0"),new Long(5));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input2").get("out0"),"SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestInput3Component() {
		Assert.assertEquals(statusHelper.getComponentId("input3"),"input3");
		Assert.assertEquals(statusHelper.getCurrentStatus("input3"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("input3").get("out0"),new Long(5));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input3").get("out0"),"SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestJoinComponent() {
		Assert.assertEquals(statusHelper.getComponentId("join"),"join");
		Assert.assertEquals(statusHelper.getCurrentStatus("join"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("join").get("out0"),new Long(5));
		Assert.assertEquals(statusHelper.getProcessedRecords("join").get("unused0"),new Long(1));
		Assert.assertEquals(statusHelper.getProcessedRecords("join").get("unused1"),new Long(0));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("join").get("out0"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("join").get("unused0"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("join").get("unused1"),"SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestOutput1Component() {
		Assert.assertEquals(statusHelper.getComponentId("output1"),"output1");
		Assert.assertEquals(statusHelper.getCurrentStatus("output1"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output1").get("NoSocketId"),new Long(5));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output1").get("NoSocketId"),"SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestOutput2Component() {
		Assert.assertEquals(statusHelper.getComponentId("output2"),"output2");
		Assert.assertEquals(statusHelper.getCurrentStatus("output2"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output2").get("NoSocketId"),new Long(1));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output2").get("NoSocketId"),"SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestOutput3Component() {
		Assert.assertEquals(statusHelper.getComponentId("output3"),"output3");
		Assert.assertEquals(statusHelper.getCurrentStatus("output3"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output3").get("NoSocketId"),new Long(0));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output3").get("NoSocketId"),"SUCCESSFUL");
	}
	
}
