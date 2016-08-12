package hydrograph.engine.test;

import java.util.List;
import java.util.Map;

import hydrograph.engine.execution.tracking.ComponentInfo;

public class StatusHelper {

	List<ComponentInfo> status;
//	static ComponentInfo compInfo;
//	static String componentID;
//	static String componentStatus;
	
	public StatusHelper(List<ComponentInfo> statusList){
		status = statusList;
	}
	
	/*public static void generateComponentStatus(List<ComponentInfo> status) {
		for (ComponentInfo componentInfo : status) {
			compInfo = componentInfo;
			componentID = compInfo.getComponentId();
			componentStatus = compInfo.getCurrentStatus();
		}
	}*/
	
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
