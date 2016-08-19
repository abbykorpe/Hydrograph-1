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



import java.util.Map;

/**
 * The Class ComponentStatus.
 * @author Butwise
 */
public class ComponentStatus {
	
	/** The component id. */
	private String componentId;  
	
	/** The component name. */
	private String componentName;  
	
	/** The current status. */
	private String currentStatus;
	
	/** The processed record count. */
	private Map<String, Long> processedRecordCount;

	/**
	 * Instantiates a new component status.
	 *
	 * @param componentId the component id
	 * @param componentName the component name
	 * @param currentStatus the current status
	 * @param processedRecordCount the processed record count
	 */
	public ComponentStatus(String componentId, String componentName, String currentStatus,
			Map<String, Long> processedRecordCount) {
		super();
		this.componentId = componentId;
		this.componentName = componentName;
		this.currentStatus = currentStatus;
		this.processedRecordCount = processedRecordCount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ComponentStatus [componentId=" + componentId + ", componentName=" + componentName + ", currentStatus="
				+ currentStatus + ", processedRecordCount=" + processedRecordCount + "]";
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
	 * Gets the component name.
	 *
	 * @return the component name
	 */
	public String getComponentName() {
		return componentName;
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
	
	
	

}
