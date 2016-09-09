package hydrograph.ui.graph.utility;

import hydrograph.ui.logging.factory.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

/**
 * 
 * @author Bitwise
 *
 */
public class ViewDataUtils {

	private Logger logger = LogFactory.INSTANCE.getLogger(ViewDataUtils.class);
	
	/** The jobUniqueId map. */
	private static Map<String,List<String>> viewDataUniqueIdMap = new HashMap<>();
	private static List<String> uniqueJobIdList = new ArrayList<>();
	
	
	public ViewDataUtils() {
	}
	
	
	/**
	 * Gets the uniqueJobId.
	 *
	 * @param jobName the job name
	 */
	public static List<String> getJob(String jobName) {
		return viewDataUniqueIdMap.get(jobName);
	}
	
	/**
	 * Adds the viewData uniqueJobId.
	 *
	 * @param jobName the job id
	 * @param uniqueJobId the viewData uniqueJobId
	 */
	public void addDebugJob(String jobName, String uniqueJobId){
		if(uniqueJobId.length() <=5){
			uniqueJobIdList.add(uniqueJobId);
		}else{
			uniqueJobIdList.clear();
			uniqueJobIdList.add(uniqueJobId);
		}
		viewDataUniqueIdMap.put(jobName, uniqueJobIdList);
	}
	
}
