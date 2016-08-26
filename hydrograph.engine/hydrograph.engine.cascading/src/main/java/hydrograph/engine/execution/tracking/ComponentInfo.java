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
 * ComponentInfo class to fill the statistics of components such as record count,current status
 * @author Aniketmo
 *
 */
public class ComponentInfo {

	private String componentId;
	private String currentStatus;
	private Map<String, String> statusPerSocketMap = new HashMap<String, String>();
	private Map<String, Long> mapofStats = new LinkedHashMap<String, Long>();

	/**
	 * @return the status of socket
	 */
	public Map<String, String> getStatusPerSocketMap() {
		return statusPerSocketMap;
	}

	/**
	 * @param scoketId
	 * 				the socketId to set
	 * @param status
	 * 				the status to set
	 */
	public void setStatusPerSocketMap(String scoketId, Status status) {
		statusPerSocketMap.put(scoketId, status.name());
	}

	/**
	 * @return the record count
	 */
	public Map<String, Long> getProcessedRecords() {
		return mapofStats;
	}

	/**
	 * @param portid
	 * 				the portid to set
	 * @param recordCount
	 * 					the recordCount to set
	 */
	public void setProcessedRecordCount(String portid, long recordCount) {
		mapofStats.put(portid, recordCount);
	}

	/**
	 * @return the current status of component
	 */
	public String getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @param currentStatus
	 * 				the current status to set
	 */
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	/**
	 * @return the componentId
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * @param componentId
	 * 				the componentId to set
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
