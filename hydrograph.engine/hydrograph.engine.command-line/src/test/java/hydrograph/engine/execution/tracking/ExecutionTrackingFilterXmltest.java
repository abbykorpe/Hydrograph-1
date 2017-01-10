package hydrograph.engine.execution.tracking;

import hydrograph.engine.commandline.utilities.HydrographService;
import hydrograph.engine.helper.StatusHelper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExecutionTrackingFilterXmltest {

	static HydrographService hydrographService;
	static StatusHelper statusHelper;
	static int returnCode;

	@BeforeClass
	public static void hydrographService() throws Exception {
		String[] args = { "-xmlpath", "testData/XMLFiles/FilterExample.xml" };
		hydrographService = new HydrographService();
		returnCode = hydrographService.executeGraph(args);
		statusHelper = new StatusHelper(hydrographService.getStatus());
	}

	@Test
	public void isJobSuccessfulAndReturnCodeZero() {
		Assert.assertEquals(returnCode, 0);
	}

	@Test
	public void itShouldTestInputComponent() {
		Assert.assertEquals(statusHelper.getComponentId("input1"), "input1");
		Assert.assertEquals(statusHelper.getCurrentStatus("input1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("input1").get("out0"), new Long(4));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input1").get("out0"), "SUCCESSFUL");
	}

	@Test
	public void itShouldTestFilterComponent() {
		Assert.assertEquals(statusHelper.getComponentId("trans"), "trans");
		Assert.assertEquals(statusHelper.getCurrentStatus("trans"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("trans").get("out1"), new Long(2));
		Assert.assertEquals(statusHelper.getProcessedRecords("trans").get("unused1"), new Long(2));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("trans").get("out1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("trans").get("unused1"), "SUCCESSFUL");
	}

	@Test
	public void itShouldTestOutputComponent() {
		Assert.assertEquals(statusHelper.getComponentId("output1"), "output1");
		Assert.assertEquals(statusHelper.getCurrentStatus("output1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output1").get("NoSocketId"), new Long(2));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output1").get("NoSocketId"), "SUCCESSFUL");
	}

	@Test
	public void itShouldTestUnusedOutputComponent() {
		Assert.assertEquals(statusHelper.getComponentId("unusedOutput"), "unusedOutput");
		Assert.assertEquals(statusHelper.getCurrentStatus("unusedOutput"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("unusedOutput").get("NoSocketId"), new Long(2));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("unusedOutput").get("NoSocketId"), "SUCCESSFUL");
	}

}
