package hydrograph.engine.execution.tracking;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.commandline.utilities.HydrographService;
import hydrograph.engine.helper.StatusHelper;

public class ExecutionTrackingOutputFileHiveParquetExampleExternalTableXmlTest {

	static HydrographService hydrographService;
	static StatusHelper statusHelper;
	private static Logger LOG = LoggerFactory.getLogger(ExecutionTrackingFilterXmltest.class);
	
	@BeforeClass
	public static void hydrographService() {
		String[] args = { "-xmlpath", "testData/XMLFiles/OutputFileHiveParquetExampleExternalTable.xml" };
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
		Assert.assertEquals(statusHelper.getComponentId("IFDelimited_01"), "IFDelimited_01");
		Assert.assertEquals(statusHelper.getCurrentStatus("IFDelimited_01"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("IFDelimited_01").get("out0"), new Long(3));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("IFDelimited_01").get("out0"), "SUCCESSFUL");
	}
	
	@Test
	public void itShouldTestHiveParquetOutputComponent() {
		Assert.assertEquals(statusHelper.getComponentId("hiveparquetoutput"), "hiveparquetoutput");
		Assert.assertEquals(statusHelper.getCurrentStatus("hiveparquetoutput"), "SUCCESSFUL");
		Assert.assertEquals(statusHelper.getProcessedRecords("hiveparquetoutput").get("NoSocketId"), new Long(3));
		Assert.assertEquals(statusHelper.getStatusPerSocketMap("hiveparquetoutput").get("NoSocketId"), "SUCCESSFUL");
	}
	
}
