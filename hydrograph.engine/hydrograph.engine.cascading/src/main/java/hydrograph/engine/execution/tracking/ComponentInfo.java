/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.engine.execution.tracking;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import cascading.stats.CascadingStats.Status;

/**
 * ComponentInfo class stores the current statistics of component such as
 * component id, current status of component, record count per output
 * socket,etc.
 * 
 * @author bitwise
 *
 */
public class ComponentInfo {

	private String componentId;
	private String currentStatus;
	private Map<String, String> statusPerSocketMap = new HashMap<String, String>();
	private Map<String, Long> mapofStats = new LinkedHashMap<String, Long>();

	/**
	 * @return Status per outSocket of component.
	 */
	public Map<String, String> getStatusPerSocketMap() {
		return statusPerSocketMap;
	}

	/**
	 * Method setStatusPerSocketMap sets the status of outSocket of a
	 * component.
	 * 
	 * @param scoketId
	 *            - outSocketId of component.
	 * @param status
	 *            - Status of the pipe connected to the outSocket.
	 */
	public void setStatusPerSocketMap(String scoketId, Status status) {
		statusPerSocketMap.put(scoketId, status.name());
	}

	/**
	 * @return Record count per outSocketId of a component.
	 */
	public Map<String, Long> getProcessedRecords() {
		return mapofStats;
	}

	/**
	 * @param socketId
	 *            - outSocketId of component.
	 * @param recordCount
	 *            - Record count of the outsocket.
	 */
	public void setProcessedRecordCount(String socketId, long recordCount) {
		mapofStats.put(socketId, recordCount);
	}

	/**
	 * @return Current status of component can be "Pending", "Running", "Failed"
	 *         or "Successful".
	 */
	public String getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @param currentStatus
	 *            Current status of component can be "Pending", "Running",
	 *            "Failed" or "Successful".
	 */
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	/**
	 * @return Component id of component
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * @param componentId
	 *            - Component Id of component
	 */
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@Override
	public String toString() {
		String message = "";
		message = "ComponentInfo : \n" + "componentId :" + componentId + "\ncurrentStatus :" + currentStatus;
		if (statusPerSocketMap != null) {
			for (Entry<String, String> entry : statusPerSocketMap.entrySet()) {
				message += "\n Socketid:" + entry.getKey() + "\n record processed :" + mapofStats.get(entry.getKey())
						+ "\n statusofport:" + entry.getValue();
			}
		}
		message += "\n";
		return message;
	}
}
