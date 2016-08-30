package hydrograph.engine.execution.tracking;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.commandline.utilities.HydrographService;
import hydrograph.engine.helper.StatusHelper;

public class ExecutionTrackingUnionAllXmlTest {

	static HydrographService hydrographService;
	static StatusHelper statusHelper;

	@BeforeClass
	public static void hydrographService() throws Exception {
		String[] args = { "-xmlpath", "testData/XMLFiles/UnionAllExample.xml" };
		hydrographService = new HydrographService();
		hydrographService.executeGraph(args);
		statusHelper = new StatusHelper(hydrographService.getStatus());
	}

	@Test
	public void itShouldTestInput1Component() {
		Assert.assertEquals(statusHelper.getComponentId("input1"), "input1");
		Assert.assertEquals(statusHelper.getCurrentStatus("input1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("input1").get("out0"), new Long(3));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input1").get("out0"), "SUCCESSFUL");
	}

	@Test
	public void itShouldTestInput2Component() {
		Assert.assertEquals(statusHelper.getComponentId("input2"), "input2");
		Assert.assertEquals(statusHelper.getCurrentStatus("input2"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("input2").get("out0"), new Long(5));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input2").get("out0"), "SUCCESSFUL");
	}

	@Test
	public void itShouldTestGatherComponent() {
		Assert.assertEquals(statusHelper.getComponentId("gather"), "gather");
		Assert.assertEquals(statusHelper.getCurrentStatus("gather"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("gather").get("sdf"), new Long(8));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("gather").get("sdf"), "SUCCESSFUL");
	}

	@Test
	public void itShouldTestOutputComponent() {
		Assert.assertEquals(statusHelper.getComponentId("output1"), "output1");
		Assert.assertEquals(statusHelper.getCurrentStatus("output1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output1").get("NoSocketId"), new Long(8));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output1").get("NoSocketId"), "SUCCESSFUL");
	}

}
