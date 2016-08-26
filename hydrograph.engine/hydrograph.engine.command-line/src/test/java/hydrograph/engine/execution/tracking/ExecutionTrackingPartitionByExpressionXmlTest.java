package hydrograph.engine.execution.tracking;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.commandline.utilities.HydrographService;
import hydrograph.engine.helper.StatusHelper;

public class ExecutionTrackingPartitionByExpressionXmlTest {

	static HydrographService hydrographService;
	static StatusHelper statusHelper;
	private static Logger LOG = LoggerFactory.getLogger(ExecutionTrackingFilterXmltest.class);
	
	@BeforeClass
	public static void hydrographService() {
		String[] args = { "-xmlpath", "testData/XMLFiles/PartitionByExpressionExample.xml" };
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
		Assert.assertEquals(statusHelper.getProcessedRecords("input1").get("out0"), new Long(13));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("input1").get("out0"), "SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestPartitionExpressionComponent() {
		Assert.assertEquals(statusHelper.getComponentId("partitionByExpression"), "partitionByExpression");
		Assert.assertEquals(statusHelper.getCurrentStatus("partitionByExpression"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("partitionByExpression").get("out_credit"), new Long(6));
		Assert.assertEquals(statusHelper.getProcessedRecords("partitionByExpression").get("out_debit"), new Long(5));
		Assert.assertEquals(statusHelper.getProcessedRecords("partitionByExpression").get("out_mix"), new Long(2));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("partitionByExpression").get("out_credit"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("partitionByExpression").get("out_debit"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("partitionByExpression").get("out_mix"), "SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestOutput1Component() {
		Assert.assertEquals(statusHelper.getComponentId("output1"), "output1");
		Assert.assertEquals(statusHelper.getCurrentStatus("output1"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output1").get("NoSocketId"), new Long(6));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output1").get("NoSocketId"), "SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestOutput2Component() {
		Assert.assertEquals(statusHelper.getComponentId("output2"), "output2");
		Assert.assertEquals(statusHelper.getCurrentStatus("output2"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output2").get("NoSocketId"), new Long(5));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output2").get("NoSocketId"), "SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestOutput3Component() {
		Assert.assertEquals(statusHelper.getComponentId("output3"), "output3");
		Assert.assertEquals(statusHelper.getCurrentStatus("output3"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("output3").get("NoSocketId"), new Long(2));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("output3").get("NoSocketId"), "SUCCESSFUL");
	}
}
