package hydrograph.engine.execution.tracking;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.commandline.utilities.HydrographService;
import hydrograph.engine.helper.StatusHelper;

public class ExecutionTrackingCumulateXmlTest {

	static HydrographService hydrographService;
	static StatusHelper statusHelper;
	private static Logger LOG = LoggerFactory.getLogger(ExecutionTrackingFilterXmltest.class);
	
	@BeforeClass
	public static void hydrographService() {
		String[] args = { "-xmlpath", "testData/XMLFiles/CumulateExample.xml" };
		try {
			hydrographService = new HydrographService();
			hydrographService.executeGraph(args);
			statusHelper = new StatusHelper(hydrographService.getStatus());
		} catch (Exception e) {
			LOG.error("",e);
		}
	}
	
	@Test
	public void itShouldTestInput1Component() {
		Assert.assertEquals(statusHelper.getComponentId("input1"), "input1");
		Assert.assertEquals(statusHelper.getCurrentStatus("input1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("input1").get("out0"), new Long(6));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input1").get("out0"), "SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestCumulateComponent() {
		Assert.assertEquals(statusHelper.getComponentId("scan"), "scan");
		Assert.assertEquals(statusHelper.getCurrentStatus("scan"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("scan").get("out0"), new Long(6));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("scan").get("out0"), "SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestOutputComponent() {
		Assert.assertEquals(statusHelper.getComponentId("output1"), "output1");
		Assert.assertEquals(statusHelper.getCurrentStatus("output1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output1").get("NoSocketId"), new Long(6));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output1").get("NoSocketId"), "SUCCESSFUL");
	}
	
}
