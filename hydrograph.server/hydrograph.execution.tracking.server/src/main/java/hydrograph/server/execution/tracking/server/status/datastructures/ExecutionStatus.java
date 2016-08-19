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
package hydrograph.server.execution.tracking.server.status.datastructures;

import java.util.List;

/**
 * The Class ExecutionStatus.
 */
public class ExecutionStatus {
	
	/** The component status. */
	private List<ComponentStatus> componentStatus;
	
	/** The job id. */
	private String jobId;
	
	/** The type. */
	private String type;
	
	/** The job status. */
	private String jobStatus;

	/**
	 * Instantiates a new execution status.
	 *
	 * @param componentStatus the component status
	 */
	public ExecutionStatus(List<ComponentStatus> componentStatus) {
		super();
		this.componentStatus = componentStatus;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the component status.
	 *
	 * @return the component status
	 */
	public List<ComponentStatus> getComponentStatus() {
		return componentStatus;
	}

	/**
	 * Sets the component status.
	 *
	 * @param componentStatus the new component status
	 */
	public void setComponentStatus(List<ComponentStatus> componentStatus) {
		this.componentStatus = componentStatus;
	}

	/**
	 * Gets the job status.
	 *
	 * @return the job status
	 */
	public String getJobStatus() {
		return jobStatus;
	}

	/**
	 * Sets the job status.
	 *
	 * @param jobStatus the new job status
	 */
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	/**
	 * Gets the job id.
	 *
	 * @return the job id
	 */
	public String getJobId() {
		return jobId;
	}

	/**
	 * Sets the job id.
	 *
	 * @param jobId the new job id
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExecutionStatus [componentStatus=" + componentStatus + ", jobId=" + jobId + ", type=" + type
				+ ", jobStatus=" + jobStatus + "]";
	}
	

}
