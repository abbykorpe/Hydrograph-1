package hydrograph.engine.execution.tracking;

import hydrograph.engine.commandline.utilities.HydrographService;
import hydrograph.engine.helper.StatusHelper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExecutionTrackingNormalizeXmlTest {

	static HydrographService hydrographService;
	static StatusHelper statusHelper;
	static int returnCode;

	@BeforeClass
	public static void hydrographService() throws Exception {
		String[] args = { "-xmlpath", "testData/XMLFiles/NormalizeExample.xml" };
		hydrographService = new HydrographService();
		returnCode = hydrographService.executeGraph(args);
		statusHelper = new StatusHelper(hydrographService.getStatus());
	}

	@Test
	public void isJobSuccessfulAndReturnCodeZero() {
		Assert.assertEquals(returnCode, 0);
	}

	@Test
	public void itShouldTestInput1Component() {
		Assert.assertEquals(statusHelper.getComponentId("input1"), "input1");
		Assert.assertEquals(statusHelper.getCurrentStatus("input1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("input1").get("out0"), new Long(9));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input1").get("out0"), "SUCCESSFUL");
	}

	@Test
	public void itShouldTestNormalizeComponent() {
		Assert.assertEquals(statusHelper.getComponentId("normalize"), "normalize");
		Assert.assertEquals(statusHelper.getCurrentStatus("normalize"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("normalize").get("out0"), new Long(11));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("normalize").get("out0"), "SUCCESSFUL");
	}

	@Test
	public void itShouldTestOutputComponent() {
		Assert.assertEquals(statusHelper.getComponentId("output1"), "output1");
		Assert.assertEquals(statusHelper.getCurrentStatus("output1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output1").get("NoSocketId"), new Long(11));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output1").get("NoSocketId"), "SUCCESSFUL");
	}

}
