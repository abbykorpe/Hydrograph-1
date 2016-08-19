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

package hydrograph.ui.graph.execution.tracking.datastructure;


import java.util.List;

/**
 * The Class ExecutionStatus, contains information about status of all components.
 * @author Bitwise
 */
public class ExecutionStatus {
	private List<ComponentStatus> componentStatus;
	private String jobId;
	private String type;
	private String jobStatus;

	

	public ExecutionStatus(List<ComponentStatus> componentStatus) {
		super();
		this.componentStatus = componentStatus;
	}
	/**
	 * @return type of the status
	 */
	public String getType() {
		return type;
	}

	
	/**
	 * set type fo the status
	 * @param type 
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return List of component status's in a graph
	 */
	public List<ComponentStatus> getComponentStatus() {
		return componentStatus;
	}

	/**
	 * set Component status
	 * @param componentStatus
	 */
	public void setComponentStatus(List<ComponentStatus> componentStatus) {
		this.componentStatus = componentStatus;
	}

	
	/**
	 * @return current status of the job
	 */
	public String getJobStatus() {
		return jobStatus;
	}

	/**
	 * set job status 
	 * @param jobStatus
	 */
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	
	/**
	 * @return Job ID
	 */
	public String getJobId() {
		return jobId;
	}

	
	/**
	 * set job ID
	 * @param jobId
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Override
	public String toString() {
		return "ExecutionStatus [componentStatus=" + componentStatus + ", jobId=" + jobId + ", type=" + type
				+ ", jobStatus=" + jobStatus + "]";
	}
	
	
	

}
