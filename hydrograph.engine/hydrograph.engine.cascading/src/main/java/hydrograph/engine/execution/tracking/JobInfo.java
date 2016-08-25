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
	private Map<String, Pipe> componentPipeMap;
	private Map<String, String> pipeComponentMap;
	private Map<String, List<String>> componentSocketMap;
	private Map<String, List<String>> componentAndPreviousMap;
	private List<String> AllPipes;
	private static List<String> listOfFilterComponent;

	/**
	 * Processes the CascadingStats to generate the component Statistics
	 * 
	 * @param cascadingStats
	 * @throws ElementGraphNotFoundException
	 */
	public synchronized void storeComponentStats(CascadingStats<?> cascadingStats)
			throws ElementGraphNotFoundException {
		checkAndCreateMaps();
		generateStats(cascadingStats);
	}

	private void checkAndCreateMaps() {
		if (componentPipeMap == null) {
			componentPipeMap = ComponentPipeMapping.getComponentToPipeMapping();
			pipeComponentMap = createReverseMap(componentPipeMap);
			componentSocketMap = ComponentPipeMapping.getComponentSocketMap();
			componentAndPreviousMap = ComponentPipeMapping.getComponentAndPreviousMap();
			AllPipes = new ArrayList<String>();
			listOfFilterComponent = ComponentPipeMapping.getListOfFilterComponent();
		}
	}

	private void generateStats(CascadingStats<?> cascadingStats) throws ElementGraphNotFoundException {
		ElementGraph elementGraph = extractElementGraphFromCascadeStats(cascadingStats);
		for (Scope scope : elementGraph.edgeSet()) {
			if (!AllPipes.contains(scope.getName())) {
				AllPipes.add(scope.getName());
			}
		}
		for (Scope scope : elementGraph.edgeSet()) {
			String currentComponent_SocketId = getComponentFromPipe(scope.getName());
			if (currentComponent_SocketId != null) {
				String currentComponentId = getComponentIdFromComponentSocketID(currentComponent_SocketId);
				if (!isComponentGeneratedFilter(currentComponentId)) {
					getPreviousComponentInfoIfScopeIsNotPresent(cascadingStats, currentComponentId);
					createComponentInfoForComponent(currentComponent_SocketId, cascadingStats);
					generateStatsForOutputComponent(currentComponent_SocketId, cascadingStats);
				}
			}
		}
	}

	private void generateStatsForOutputComponent(String currentComponent_SocketId, CascadingStats<?> cascadingStats) {
		String currentComponentId = getComponentIdFromComponentSocketID(currentComponent_SocketId);
		ComponentInfo currentComponentInfo = componentInfoMap.get(currentComponentId);
		List<String> previousComponents = new ArrayList<String>();
		Collection<List<String>> previousComponentLists = componentAndPreviousMap.values();
		for (List<String> previousComponentList : previousComponentLists) {
			previousComponents.addAll(previousComponentList);
		}
		if (!previousComponents.contains(currentComponent_SocketId)) {
			for (String previousGeneratedfilter : componentAndPreviousMap.get(currentComponentId)) {
				for (String previousComponent_SocketID : componentAndPreviousMap
						.get(getComponentIdFromComponentSocketID(previousGeneratedfilter))) {
					long recordCount = 0;
					String previousPipeName = componentPipeMap.get(previousComponent_SocketID).getName();
					recordCount = cascadingStats.getCounterValue(COUNTER_GROUP, previousPipeName);
					currentComponentInfo.setProcessedRecordCount("NoSocketId", recordCount);
				}
			}
		}

	}

	private boolean isComponentGeneratedFilter(String currentComponentId) {
		return listOfFilterComponent.contains(currentComponentId);
	}

	private void getPreviousComponentInfoIfScopeIsNotPresent(CascadingStats<?> cascadingStats,
			String currentComponentId) {
		if (componentAndPreviousMap.containsKey(currentComponentId)) {
			List<String> previousComponentId_SocketIds = componentAndPreviousMap.get(currentComponentId);
			for (String previousComponentId_SocketId : previousComponentId_SocketIds) {
				if (isComponentGeneratedFilter(getComponentIdFromComponentSocketID(
						getComponentFromPipe(componentPipeMap.get(previousComponentId_SocketId).getName())))) {
					List<String> prePreviousComponentId_SocketIds = componentAndPreviousMap
							.get(getComponentIdFromComponentSocketID(getComponentFromPipe(
									componentPipeMap.get(previousComponentId_SocketId).getName())));
					for (String prePreviousComponentId_SocketId : prePreviousComponentId_SocketIds) {
						String previousPipeName = componentPipeMap.get(prePreviousComponentId_SocketId).getName();
						if (!AllPipes.contains(previousPipeName) && previousPipeName != null) {
							createComponentInfoForComponent(prePreviousComponentId_SocketId, cascadingStats);
							getPreviousComponentInfoIfScopeIsNotPresent(cascadingStats,
									getComponentIdFromComponentSocketID(prePreviousComponentId_SocketId));
						}
					}
				}
			}
		}
	}

	private void createComponentInfoForComponent(String component_SocketId, CascadingStats<?> cascadingStats) {
		ComponentInfo componentInfo = null;
		String currentComponentId = getComponentIdFromComponentSocketID(component_SocketId);
		if (currentComponentId != null) {
			if (componentInfoMap.containsKey(currentComponentId)) {
				componentInfo = componentInfoMap.get(currentComponentId);
				componentInfo.setStatusPerSocketMap(getSocketIdFromComponentSocketID(component_SocketId),
						cascadingStats.getStatus());
			} else {
				componentInfo = new ComponentInfo();
				componentInfo.setComponentId(currentComponentId);
				for (String socketId : componentSocketMap.get(currentComponentId)) {
					componentInfo.setStatusPerSocketMap(socketId, cascadingStats.getStatus());
					componentInfo.setProcessedRecordCount(socketId, 0);
				}
			}
		}
		generateAndUpdateComponentRecordCount(cascadingStats, componentInfo, currentComponentId);
		componentInfoMap.put(currentComponentId, componentInfo);
		setStatus(componentInfo, cascadingStats);
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

	private String getComponentIdFromComponentSocketID(String componentId_SocketId) {
		for (Entry<String, List<String>> component_Socketid : componentSocketMap.entrySet()) {
			String componentId = component_Socketid.getKey();
			for (String socketId : component_Socketid.getValue()) {
				if (componentId_SocketId.equals(componentId + "_" + socketId)) {
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

	private void generateAndUpdateComponentRecordCount(CascadingStats<?> cascadingStats, ComponentInfo componentInfo,
			String ComponentId) {
		for (String socketId : componentSocketMap.get(ComponentId)) {
			long recordCount = 0;
			for (String counter : cascadingStats.getCountersFor(COUNTER_GROUP)) {
				String componentIdANdSocketId = pipeComponentMap.get(counter);
				if ((getComponentIdFromComponentSocketID(componentIdANdSocketId) != null
						&& getComponentIdFromComponentSocketID(componentIdANdSocketId).equals(ComponentId))
						&& socketId.equals(getSocketIdFromComponentSocketID(componentIdANdSocketId))) {
					recordCount = cascadingStats.getCounterValue(COUNTER_GROUP, counter);
					componentInfo.setProcessedRecordCount(socketId, recordCount);
				}
			}
		}
	}

	private Map<String, String> createReverseMap(Map<String, Pipe> allMapOfPipes) {
		Map<String, String> pipeComponent = new HashMap<>();
		for (Map.Entry<String, Pipe> entry : allMapOfPipes.entrySet()) {
			pipeComponent.put(entry.getValue().getName(), entry.getKey());
		}
		return pipeComponent;
	}

	private String getComponentFromPipe(String pipeName) {
		for (Entry<String, Pipe> componentPipeSet : componentPipeMap.entrySet()) {
			if (componentPipeSet.getValue().getName().equals(pipeName)) {
				return componentPipeSet.getKey();
			}
		}
		return null;
	}

	private void setStatus(ComponentInfo componentInfo, CascadingStats<?> flowNodeStats) {
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

	/**
	 * Method returns the current status of all components
	 * 
	 * @return List of ComponentInfo
	 */
	public List<ComponentInfo> getstatus() {
		return new ArrayList<>(componentInfoMap.values());
	}

	/**
	 * ElementGraphNotFoundException
	 */
	public class ElementGraphNotFoundException extends Exception {

		private static final long serialVersionUID = 4691438024426481804L;

		public ElementGraphNotFoundException(String message) {
			super(message);
		}

	}
}