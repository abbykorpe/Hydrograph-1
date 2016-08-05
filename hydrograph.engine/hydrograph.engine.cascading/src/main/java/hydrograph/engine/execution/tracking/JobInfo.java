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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cascading.flow.FlowNode;
import cascading.flow.FlowStep;
import cascading.flow.planner.Scope;
import cascading.flow.planner.graph.ElementGraph;
import cascading.pipe.Pipe;
import cascading.stats.CascadingStats;
import cascading.stats.FlowNodeStats;
import cascading.stats.FlowStepStats;

public class JobInfo {

	private final String COUNTER_GROUP = "com.hydrograph.customgroup";
	private Map<String, ComponentInfo> componentInfoMap = new HashMap<>();
	private final Map<String, Pipe> componentPipeMap = ComponentPipeMapping.getComponentToPipeMapping();
	private final Map<String, String> pipeComponentMap = createReverseMap(componentPipeMap);
	private final Map<String, String> componentFilterMap = ComponentPipeMapping.getComopnentsAndFilterMap();
	private List<String> filterList = new ArrayList<String>(componentFilterMap.values());
	private final Map<String, List<String>> componentSocketMap = ComponentPipeMapping.getComponentSocketMap();

	private static volatile JobInfo jobInfo;

	public static JobInfo getInstance() {
		if (jobInfo == null) {
			synchronized (JobInfo.class) {
				if (jobInfo == null)
					jobInfo = new JobInfo();
			}
		}
		return jobInfo;
	}

	/**Processes the CascadingStats to generate the component Statistics
	 * @author saketm
	 * @param cascadingStats
	 * @throws ElementGraphNotFoundException
	 */
	public synchronized void storeComponentStats(CascadingStats<?> cascadingStats)
			throws ElementGraphNotFoundException {
		generateStats(cascadingStats);
	}

	private void generateStats(CascadingStats<?> cascadingStats) throws ElementGraphNotFoundException {
		ElementGraph elementGraph = extractElementGraphFromCascadeStats(cascadingStats);
		for (Scope scope : elementGraph.edgeSet()) {
			ComponentInfo componentInfo = null;
			String filterComponent = getFilterComponentFromScope(scope);
			if (filterComponent != null) {
				List<String> connectedComponentId_Socketid = getFilterConnectedComponent(filterComponent);
				for (String componentId_Socketid : connectedComponentId_Socketid) {
					String eachConnectedComponentId = getComponentIdFromMap(componentId_Socketid);
					if (eachConnectedComponentId != null) {
						if (componentInfoMap.containsKey(eachConnectedComponentId)) {
							componentInfo = componentInfoMap.get(eachConnectedComponentId);
							componentInfo.setStatusPerSocketMap(getSocketIdFromComponentSocketID(componentId_Socketid),
									cascadingStats.getStatus());
						} else {
							componentInfo = new ComponentInfo();
							componentInfo.setComponentId(eachConnectedComponentId);
							for (String socketId : componentSocketMap.get(eachConnectedComponentId)) {
								componentInfo.setStatusPerSocketMap(socketId, cascadingStats.getStatus());
								componentInfo.setProcessedRecordCount(socketId, 0);
							}
						}
					}
					generateAndUpdateComponentRecordCount(scope, cascadingStats, componentInfo,
							eachConnectedComponentId);
					setStatus(componentInfo, cascadingStats);
					System.out.println("Component status is : "+ componentInfo);
				}
			}
		}
	}

	private String getFilterComponentFromScope(Scope scope) {
		String filterComponent = null;
		String componentAndSocketID = getComponentIdFromPipe(scope.getName());
		if (filterList.contains(componentAndSocketID)) {
			filterComponent = componentAndSocketID;
		}
		return filterComponent;
	}

	private ElementGraph extractElementGraphFromCascadeStats(CascadingStats<?> cascadingStats)
			throws ElementGraphNotFoundException {
		ElementGraph elementGraph = null;
		if (cascadingStats instanceof FlowNodeStats) {
			FlowNodeStats flowNodeStats = (FlowNodeStats) cascadingStats;
			FlowNode flowNode = flowNodeStats.getFlowNode();
			elementGraph = flowNode.getElementGraph();
		} else if (cascadingStats instanceof FlowStepStats) {
			FlowStepStats flowStepStats = (FlowStepStats) cascadingStats;
			FlowStep<?> flowStep = flowStepStats.getFlowStep();
			elementGraph = flowStep.getElementGraph();
		} else {
			throw new ElementGraphNotFoundException(
					"Element Graph not found from FlowNodeStats/FlowStepStats while fetching stats from cascading");
		}
		return elementGraph;
	}

	private String getComponentIdFromMap(String eachConnectedComponentIdAndSocketid) {
		for (Entry<String, List<String>> component_Socketid : componentSocketMap.entrySet()) {
			String componentId = component_Socketid.getKey();
			for (String socketId : component_Socketid.getValue()) {
				if (eachConnectedComponentIdAndSocketid.equals(componentId + "_" + socketId)) {
					return componentId;
				}
			}
		}
		return null;
	}

	private String getSocketIdFromComponentSocketID(String prevComponentId_socketid) {
		for (Entry<String, List<String>> component_Socketid : componentSocketMap.entrySet()) {
			String componentId = component_Socketid.getKey();
			for (String socketId : component_Socketid.getValue()) {
				if (prevComponentId_socketid.equals(componentId + "_" + socketId)) {
					return socketId;
				}
			}
		}
		return null;
	}

	private void generateAndUpdateComponentRecordCount(Scope scope, CascadingStats<?> flowNodeStats,
			ComponentInfo componentInfo, String prevComponentId) {
		for (String socketId : componentSocketMap.get(prevComponentId)) {
			long recordCount = 0;
			for (String counter : flowNodeStats.getCountersFor(COUNTER_GROUP)) {
				String componentIdANdSocketId = pipeComponentMap.get(counter);
				if ((getComponentIdFromMap(componentIdANdSocketId) != null
						&& getComponentIdFromMap(componentIdANdSocketId).equals(prevComponentId))
						&& socketId.equals(getSocketIdFromComponentSocketID(componentIdANdSocketId))) {
					recordCount = flowNodeStats.getCounterValue(COUNTER_GROUP, counter);
					componentInfo.setProcessedRecordCount(socketId, recordCount);
				}
			}
		}
		componentInfoMap.put(prevComponentId, componentInfo);
	}

	private Map<String, String> createReverseMap(Map<String, Pipe> allMapOfPipes) {
		Map<String, String> pipeComponent = new HashMap<>();
		for (Map.Entry<String, Pipe> entry : allMapOfPipes.entrySet()) {
			pipeComponent.put(entry.getValue().getName(), entry.getKey());
		}
		return pipeComponent;
	}

	private String getComponentIdFromPipe(String pipeName) {
		for (Entry<String, Pipe> componentPipeSet : componentPipeMap.entrySet()) {
			if (componentPipeSet.getValue().getName().equals(pipeName)) {
				return componentPipeSet.getKey();
			}
		}
		return null;
	}

	private List<String> getFilterConnectedComponent(String filterComponent) {
		List<String> filterConnectedComponents = new ArrayList<String>();
		for (Entry<String, String> componentFilterSet : componentFilterMap.entrySet()) {
			if (componentFilterSet.getValue().equals(filterComponent)) {
				filterConnectedComponents.add(componentFilterSet.getKey());
			}
		}
		return filterConnectedComponents;
	}

	private void setStatus(ComponentInfo componentInfo, CascadingStats<?> flowNodeStats) {
		// code changes
		Map<String, String> outSocketStats = componentInfo.getStatusPerSocketMap();
		List<String> listOfStatus = new ArrayList<String>();
		for (Entry<String, String> entry : outSocketStats.entrySet()) {
			listOfStatus.add(entry.getValue());
		}
		if (listOfStatus.contains("FAILED") || listOfStatus.contains("STOPPED"))
			componentInfo.setCurrentStatus("FAILED");
		else if (listOfStatus.contains("RUNNING"))
			componentInfo.setCurrentStatus("RUNNING");
		else if (listOfStatus.contains("PENDING") || listOfStatus.contains("STARTED")
				|| listOfStatus.contains("SUBMITTED")) {
			componentInfo.setCurrentStatus("PENDING");
		} else if (listOfStatus.contains("SUCCESSFUL")) {
			boolean isSuccessful = true;
			for (String status1 : listOfStatus) {
				if (!status1.equals("SUCCESSFUL"))
					isSuccessful = false;
			}
			if (isSuccessful)
				componentInfo.setCurrentStatus("SUCCESSFUL");
		}
	}

	/**Method returns the current status of all components
	 * 
	 * @author saketm
	 * @return List of ComponentInfo
	 */
	public Collection<ComponentInfo> getstatus() {
		return componentInfoMap.values();
	}

	/**ElementGraphNotFoundException 
	 * @author saketm
	 *
	 */
	public class ElementGraphNotFoundException extends Exception {

		private static final long serialVersionUID = 4691438024426481804L;

		public ElementGraphNotFoundException(String message) {
			super(message);
		}

	}
}