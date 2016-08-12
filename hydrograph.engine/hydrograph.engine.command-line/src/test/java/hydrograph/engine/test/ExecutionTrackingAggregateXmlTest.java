package hydrograph.engine.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hydrograph.engine.commandline.utilities.HydrographService;

public class ExecutionTrackingAggregateXmlTest {

	static HydrographService hydrographService;
	static StatusHelper statusHelper;

	@BeforeClass
	public static void hydrographService() {
		String[] args = { "-xmlpath", "testData/XMLFiles/AggregateExample.xml" };
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
	public void itShouldTestInputComponent() {
		Assert.assertEquals(statusHelper.getComponentId("input1"),"input1");
		Assert.assertEquals(statusHelper.getCurrentStatus("input1"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("input1").get("out0"),new Long(7));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input1").get("out0"),"SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestAggregateComponent() {
		Assert.assertEquals(statusHelper.getComponentId("reformat"),"reformat");
		Assert.assertEquals(statusHelper.getCurrentStatus("reformat"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("reformat").get("out0"),new Long(4));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("reformat").get("out0"),"SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestOutputComponent() {
		Assert.assertEquals(statusHelper.getComponentId("output1"),"output1");
		Assert.assertEquals(statusHelper.getCurrentStatus("output1"),"SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output1").get("in0"),new Long(0));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output1").get("in0"),"SUCCESSFUL");
	}
}
