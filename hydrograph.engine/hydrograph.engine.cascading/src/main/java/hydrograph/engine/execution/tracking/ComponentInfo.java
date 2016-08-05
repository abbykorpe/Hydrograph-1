package hydrograph.engine.execution.tracking;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import cascading.stats.CascadingStats.Status;

public class ComponentInfo {

	private String componentId;
	private String currentStatus;
	private Map<String, String> statusPerSocketMap = new HashMap<String, String>();
	private Map<String, Long> mapofStats = new LinkedHashMap<String, Long>();

	public Map<String, String> getStatusPerSocketMap() {
		return statusPerSocketMap;
	}

	public void setStatusPerSocketMap(String scoketId, Status status) {
		statusPerSocketMap.put(scoketId, status.name());
	}

	public Map<String, Long> getProcessedRecords() {
		return mapofStats;
	}

	public void setProcessedRecordCount(String portid, long recordCount) {
		mapofStats.put(portid, recordCount);
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getComponentId() {
		return componentId;
	}

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
