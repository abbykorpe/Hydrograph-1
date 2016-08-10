package hydrograph.engine.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import cascading.stats.CascadingStats.Status;
import hydrograph.engine.commandline.utilities.HydrographService;
import hydrograph.engine.execution.tracking.ComponentInfo;

@RunWith(value = Parameterized.class)
public class HydrographServiceTest {
	HydrographService hydrographService;

	@Parameter(value = 0)
	public String argument0;

	@Parameter(value = 1)
	public String argument1;

	@Parameter(value = 2)
	public List<ComponentInfo> listOfComponentInfo;

	@Parameters(name = "{index}:testHydrographService({0},{1})")
	public static Collection<Object[]> testData() {
		String testFolderPath = "testData/XMLFiles";
		File folder = new File(testFolderPath);
		File[] listOfFiles = folder.listFiles();

		Collection<Object[]> parameters = new ArrayList<Object[]>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String filename = listOfFiles[i].getName();
				if (filename.equals("Clone.xml")) {
					parameters
							.add(new Object[] { "-xmlpath", testFolderPath + "/" + filename, getcomponentInfoList() });
				}
			}
		}
		return parameters;
	}

	private static List<ComponentInfo> getcomponentInfoList() {
		List<ComponentInfo> componentInfos = new ArrayList<ComponentInfo>();
		
		ComponentInfo component1 = new ComponentInfo();
		component1.setComponentId("input1");
		component1.setCurrentStatus("SUCCESSFUL");
		component1.setProcessedRecordCount("out0", 3);
		component1.setStatusPerSocketMap("out0", Status.SUCCESSFUL);
		componentInfos.add(component1);
		
		ComponentInfo component2 = new ComponentInfo();
		component2.setComponentId("clone");
		component2.setCurrentStatus("SUCCESSFUL");
		component2.setProcessedRecordCount("sdf", 3);
		component2.setProcessedRecordCount("sdf1", 3);
		component2.setStatusPerSocketMap("sdf", Status.SUCCESSFUL);
		component2.setStatusPerSocketMap("sdf1", Status.SUCCESSFUL);
		componentInfos.add(component2);
		
		ComponentInfo component3 = new ComponentInfo();
		component3.setComponentId("output2");
		component3.setCurrentStatus("SUCCESSFUL");
		component3.setProcessedRecordCount("in0", 0);
		component3.setStatusPerSocketMap("in0", Status.SUCCESSFUL);
		componentInfos.add(component3);
		
		return componentInfos;
	}

	@Test
	public void testHydrographService() {
		hydrographService = new HydrographService();
		try {
			//given
			hydrographService.executeGraph(new String[]{argument0,argument1});
			//when
			for (ComponentInfo expectedComponentInfo : hydrographService.getStatus()) {
				for (ComponentInfo actualComponentInfo : listOfComponentInfo) {
					if(expectedComponentInfo.getComponentId().equals(actualComponentInfo.getComponentId())){
						Assert.assertEquals(expectedComponentInfo.getComponentId(), actualComponentInfo.getComponentId());
						Assert.assertEquals(expectedComponentInfo.getCurrentStatus(), actualComponentInfo.getCurrentStatus());
						Assert.assertEquals(expectedComponentInfo.getProcessedRecords(),actualComponentInfo.getProcessedRecords());
						Assert.assertEquals(expectedComponentInfo.getStatusPerSocketMap(), actualComponentInfo.getStatusPerSocketMap());
					}
				}
			}
			//then
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

