package hydrograph.engine.execution.tracking;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.commandline.utilities.HydrographService;
import hydrograph.engine.helper.StatusHelper;

public class ExecutionTrackingDiscardXmlTest {

	static HydrographService hydrographService;
	static StatusHelper statusHelper;

	@BeforeClass
	public static void hydrographService() throws Exception {
		String[] args = { "-xmlpath", "testData/XMLFiles/DiscardExample.xml" };
		hydrographService = new HydrographService();
		hydrographService.executeGraph(args);
		statusHelper = new StatusHelper(hydrographService.getStatus());
	}

	@Test
	public void itShouldTestInput1Component() {
		Assert.assertEquals(statusHelper.getComponentId("input1"), "input1");
		Assert.assertEquals(statusHelper.getCurrentStatus("input1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("input1").get("out0"), new Long(7));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input1").get("out0"), "SUCCESSFUL");
	}

	@Test
	public void itShouldTestDiscardComponent() {
		Assert.assertEquals(statusHelper.getComponentId("trash"), "trash");
		Assert.assertEquals(statusHelper.getCurrentStatus("trash"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("trash").get("NoSocketId"), new Long(7));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("trash").get("NoSocketId"), "SUCCESSFUL");
	}

}
