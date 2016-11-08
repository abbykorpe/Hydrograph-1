/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package hydrograph.ui.graph.execution.tracking.replay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import hydrograph.ui.graph.execution.tracking.datastructure.ComponentStatus;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.logging.factory.LogFactory;

/**
 * The Class ViewExecutionHistoryUtility use to create collection of job and their status.
 * 
 * @author Bitwise
 */

public class ViewExecutionHistoryUtility {

	private Map<String, ExecutionStatus> trackingMap;
	private Map<String, List<Job>> trackingJobMap;
	private Map<String, String> unusedCompOnCanvas;
	
	/** The logger. */
	private static Logger logger = LogFactory.INSTANCE.getLogger(ViewExecutionHistoryUtility.class);

	public static ViewExecutionHistoryUtility INSTANCE = new ViewExecutionHistoryUtility();
	
	
	private ViewExecutionHistoryUtility() {
		trackingMap = new HashMap<String, ExecutionStatus>();
		trackingJobMap = new HashMap<String, List<Job>>();
		unusedCompOnCanvas = new LinkedHashMap<>();
	}
	
	/**
	 * Add JobId and Status in map
	 * @param uniqueRunJobId
	 * @param executionStatus
	 */
	public void addTrackingStatus(String uniqueRunJobId, ExecutionStatus executionStatus){
		if(uniqueRunJobId != null){
			trackingMap.put(uniqueRunJobId, executionStatus);
		}
	}
	
	
	public void addUnusedCompLabel(String compId, String componentLabel){
		if(!unusedCompOnCanvas.containsKey(compId)){
			unusedCompOnCanvas.put(compId, componentLabel);
		}
	}
	
	public Map<String, String> getUnusedCompsOnCanvas(){
		return unusedCompOnCanvas;
	}
	
	/**
	 * Add Job name and its details.
	 * @param jobName
	 * @param jobDetails
	 */
	public void addTrackingJobs(String jobName, Job jobDetails){
		Job cloneJob = null;
		try {
			cloneJob = (Job) jobDetails.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("Failed to clone job: ",e);
		}
		if (trackingJobMap.get(jobName) == null) {
			List<Job> jobs = new ArrayList<>();
			jobs.add(cloneJob);
			trackingJobMap.put(jobName, jobs);
		} else {
			trackingJobMap.get(jobName).add(cloneJob);
		}
	}
	
	/**
	 * Return job list for tracking.
	 * @return Job details 
	 */
	public Map<String, List<Job>> getTrackingJobs(){
		return trackingJobMap;
	}
	
	
	public void getExtraComponentList(ExecutionStatus executionStatus){
		for(ComponentStatus componentStatus: executionStatus.getComponentStatus()){
			if(unusedCompOnCanvas.get(componentStatus.getComponentId()) != null){
				unusedCompOnCanvas.remove(componentStatus.getComponentId());
			}
		}
	}
	
	public List<String> getReplayMissedComponents(ExecutionStatus executionStatus){
		List<String> compList = new ArrayList<>(); 
		for(ComponentStatus componentStatus: executionStatus.getComponentStatus()){
			if(!unusedCompOnCanvas.containsKey(componentStatus.getComponentId()) && !(componentStatus.getComponentId().startsWith("viewData"))){
				compList.add(componentStatus.getComponentId());
			}
		}
		return compList;
	}
	
	/**
	 * Return job vs execution tracking status map.
	 * @return
	 */
	public Map<String, ExecutionStatus> getTrackingStatus(){
		return trackingMap;
	}
}
