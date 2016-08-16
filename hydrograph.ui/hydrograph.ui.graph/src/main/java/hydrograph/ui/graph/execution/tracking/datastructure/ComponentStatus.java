package hydrograph.ui.graph.execution.tracking.datastructure;



import java.util.Map;

public class ComponentStatus {
	String componentId;  
	String componentName;  
	String currentStatus;
	Map<String, Long> processedRecordCount;

	public ComponentStatus(String componentId, String componentName, String currentStatus,
			Map<String, Long> processedRecordCount) {
		super();
		this.componentId = componentId;
		this.componentName = componentName;
		this.currentStatus = currentStatus;
		this.processedRecordCount = processedRecordCount;
	}
	@Override
	public String toString() {
		return "ComponentStatus [componentId=" + componentId + ", componentName=" + componentName + ", currentStatus="
				+ currentStatus + ", processedRecordCount=" + processedRecordCount + "]";
	}

	public String getComponentId() {
		return componentId;
	}

	public String getComponentName() {
		return componentName;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public Map<String, Long> getProcessedRecordCount() {
		return processedRecordCount;
	}
	
	
	

}
