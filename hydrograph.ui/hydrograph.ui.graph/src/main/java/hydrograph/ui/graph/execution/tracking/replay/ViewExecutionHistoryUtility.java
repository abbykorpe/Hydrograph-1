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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.execution.tracking.datastructure.ComponentStatus;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
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
	 * Static 'instance' method
	 *
	 */
	public static ViewExecutionHistoryUtility getInstance( ) {
      return INSTANCE;
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
	
	
	/**
	 * The Function will add componentId and componentLabel
	 * @param componentId
	 * @param componentLabel
	 */
	public void addUnusedCompLabel(String componentId, String componentLabel){
		if(!unusedCompOnCanvas.containsKey(componentId)){
			unusedCompOnCanvas.put(componentId, componentLabel);
		}
	}
	
	/**
	 * The Function will return component details map
	 * @return Components Details map 
	 */
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
	
	/**
	 * The Function will return extra component list which exist on Job Canvas
	 * @param ExecutionStatus
	 * @return Component List
	 */
	public void getExtraComponentList(ExecutionStatus executionStatus){
		for(ComponentStatus componentStatus: executionStatus.getComponentStatus()){
			
			if(unusedCompOnCanvas.get(componentStatus.getComponentId()) != null){
				unusedCompOnCanvas.remove(componentStatus.getComponentId());
			}
		}
	}
	
	/**
	 * The Function will return missed component list
	 * @param ExecutionStatus
	 * @return Component List
	 */
	public List<String> getMissedComponents(ExecutionStatus executionStatus){
		List<String> compList = new ArrayList<>(); 
		executionStatus.getComponentStatus().forEach(componentStatus ->{
			if(!unusedCompOnCanvas.containsKey(componentStatus.getComponentId()) && componentStatus.getComponentName() != null 
					&& StringUtils.isNotBlank(componentStatus.getComponentName())){
				compList.add(componentStatus.getComponentId());
			}
		});
		return compList;
	}
	
	/**
	 * Return job vs execution tracking status map.
	 * @return
	 */
	public Map<String, ExecutionStatus> getTrackingStatus(){
		return trackingMap;
	}
	
	/*
	 * The function will use to check componentId and componentName in subjob.
	 */
	public void subjobParams(Map<String, String> componentNameAndLink, Component subjobComponent, StringBuilder subjobPrefix, boolean isParent){
		Container container = (Container) subjobComponent.getProperties().get(Constants.SUBJOB_CONTAINER);
		for(Object object:container.getChildren()){
			Component component=(Component) object;
			if( !(component.getComponentName().equals(Messages.INPUT_SUBJOB_COMPONENT)) && 
					!(component.getComponentName().equals(Messages.OUTPUT_SUBJOB_COMPONENT))){
				
				if(Constants.SUBJOB_COMPONENT.equals(component.getComponentName())){
					subjobPrefix.append(subjobComponent.getComponentId()+".");
					subjobParams(componentNameAndLink, component, subjobPrefix, false);
				}
				
				if(!Constants.SUBJOB_COMPONENT.equals(component.getComponentName())){
				if(isParent){
					componentNameAndLink.put(subjobComponent.getComponentId()+"."+component.getComponentId(), 
							subjobComponent.getComponentId()+"."+component.getComponentId());
				}else{
					componentNameAndLink.put(subjobPrefix+subjobComponent.getComponentId()+"."+component.getComponentId(), 
							subjobPrefix+subjobComponent.getComponentId()+"."+component.getComponentId());
				}
			   }
			}
		}
		
	}
	
}
