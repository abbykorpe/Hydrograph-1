package hydrograph.engine.helper;

import hydrograph.engine.execution.tracking.ComponentInfo;

import java.util.List;
import java.util.Map;

public class StatusHelper {

	List<ComponentInfo> status;
	
	public StatusHelper(List<ComponentInfo> statusList){
		status = statusList;
	}
	
	public String getComponentId(String compId){
		for (ComponentInfo componentInfo : status) {
			if(componentInfo.getComponentId().equals(compId))
			return componentInfo.getComponentId();
		}
		return null;
	}
	
	public String getCurrentStatus(String compId){
		for (ComponentInfo componentInfo : status) {
			if(componentInfo.getComponentId().equals(compId))
			{
			return componentInfo.getCurrentStatus();
			}
		}
		return null;
	}
	
	public Map<String, Long> getProcessedRecords(String compId){
		for (ComponentInfo componentInfo : status) {
			if(componentInfo.getComponentId().equals(compId))
			{
			return componentInfo.getProcessedRecords();
			}
		}
		return null;
	}
	
	public Map<String, String> getStatusPerSocketMap(String compId){
		for (ComponentInfo componentInfo : status) {
			if(componentInfo.getComponentId().equals(compId))
			{
			return componentInfo.getStatusPerSocketMap();
			}
		}
		return null;
	}

}
