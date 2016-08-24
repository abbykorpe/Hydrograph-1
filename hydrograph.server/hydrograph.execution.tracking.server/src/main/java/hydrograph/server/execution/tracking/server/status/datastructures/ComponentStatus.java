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


import java.util.Map;

/**
 * The Class ComponentStatus.
 */
public class ComponentStatus {
	
	/** The component id. */
	String componentId;  
	
	/** The current status. */
	String currentStatus;
	
	/** The processed record count. */
	Map<String, Long> processedRecordCount;

	/**
	 * Instantiates a new component status.
	 *
	 * @param componentId the component id
	 * @param currentStatus the current status
	 * @param processedRecordCount the processed record count
	 */
	public ComponentStatus(String componentId,String currentStatus,
			Map<String, Long> processedRecordCount) {
		super();
		this.componentId = componentId;
		this.currentStatus = currentStatus;
		this.processedRecordCount = processedRecordCount;
	}

	/**
	 * Gets the component id.
	 *
	 * @return the component id
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * Gets the current status.
	 *
	 * @return the current status
	 */
	public String getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * Gets the processed record count.
	 *
	 * @return the processed record count
	 */
	public Map<String, Long> getProcessedRecordCount() {
		return processedRecordCount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ComponentStatus [componentId=" + componentId + ", currentStatus="
				+ currentStatus + ", processedRecordCount=" + processedRecordCount + "]";
	}
	

}
