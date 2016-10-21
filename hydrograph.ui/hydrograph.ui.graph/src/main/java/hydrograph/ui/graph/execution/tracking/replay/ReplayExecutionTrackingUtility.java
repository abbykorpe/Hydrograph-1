package hydrograph.ui.graph.execution.tracking.replay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.job.Job;

public class ReplayExecutionTrackingUtility {

	private Map<String, ExecutionStatus> trackingMap;
	private Map<String, List<Job>> trackingJobMap;
	
	
	public static ReplayExecutionTrackingUtility INSTANCE = new ReplayExecutionTrackingUtility();
	
	private ReplayExecutionTrackingUtility() {
		trackingMap = new HashMap<String, ExecutionStatus>();
		trackingJobMap = new HashMap<String, List<Job>>();
	}
	
	public void addTrackingStatus(String uniqueRunJobId, ExecutionStatus executionStatus){
		if(uniqueRunJobId != null){
			trackingMap.put(uniqueRunJobId, executionStatus);
		}
	}
	
	public void addTrackingJobs(String jobName, Job jobDetails){
		if(trackingJobMap.get(jobName)==null){
			List<Job> jobs = new ArrayList<>();
			jobs.add(jobDetails);
			trackingJobMap.put(jobName, jobs);
		}else{
			trackingJobMap.get(jobName).add(jobDetails);
		}
	}
	
	public Map<String, List<Job>> getTrackingJobs(){
		return trackingJobMap;
		
	}
	
	public Map<String, ExecutionStatus> getTrackingStatus(){
		return trackingMap;
	}
}
