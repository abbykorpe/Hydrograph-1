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
import java.util.SortedSet;

import cascading.pipe.Pipe;
import hydrograph.engine.assembly.entity.base.AssemblyEntityBase;
import hydrograph.engine.cascading.assembly.base.BaseComponent;
import hydrograph.engine.cascading.integration.FlowContext;
import hydrograph.engine.cascading.integration.RuntimeContext;
import hydrograph.engine.core.core.HydrographJob;
import hydrograph.engine.core.helper.JAXBTraversal;
import hydrograph.engine.core.utilities.SocketUtilities;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeBaseOutSocket;
import hydrograph.engine.jaxb.operationstypes.Filter;

public class ComponentPipeMapping {

	private static Map<String, List<String>> componentSocketMap = new HashMap<String, List<String>>();
	private static Map<String, Pipe> componentToPipeMapping = new HashMap<String, Pipe>();
	private static Map<String, List<String>> componentAndPreviousMap = new HashMap<String, List<String>>();
	private static List<String> listOfFilterComponent = new ArrayList<String>();

	/**
	 * Generates  map of component and pipe
	 * @param flowContextMap
	 * 					- Map of String and {@link FlowContext} to set
	 */
	public static void generateComponentToPipeMap(Map<String, FlowContext> flowContextMap) {
		for (FlowContext flowContext : flowContextMap.values()) {
			Map<String, BaseComponent<AssemblyEntityBase>> Assemblies = flowContext.getAssemblies();
			for (BaseComponent<AssemblyEntityBase> baseComponent : Assemblies.values()) {
				Collection<HashMap<String, Pipe>> setOfOutLinks = baseComponent.getAllOutLinkForAssembly();
				for (HashMap<String, Pipe> outLinkMap : setOfOutLinks) {
					componentToPipeMapping.putAll(outLinkMap);
				}
			}
		}
	}

	/**
	 * @param runtimeContext
	 * 					- {@link RuntimeContext} to set
	 */
	public static void generateComponentToFilterMap(RuntimeContext runtimeContext) {
		JAXBTraversal jaxbTraversal = runtimeContext.getTraversal();
		SortedSet<String> phases = jaxbTraversal.getFlowsNumber();

		for (String eachPhaseNumber : phases) {
			List<String> orderedComponentList = jaxbTraversal.getOrderedComponentsList(eachPhaseNumber);
			for (String eachComponentId : orderedComponentList) {
				List<? extends TypeBaseOutSocket> outSockets = jaxbTraversal
						.getOutputSocketFromComponentId(eachComponentId);
				List<? extends TypeBaseInSocket> inSockets = jaxbTraversal
						.getInputSocketFromComponentId(eachComponentId);
				generateComponentAndPreviousMap(runtimeContext.getHydrographJob(), eachComponentId, outSockets,
						inSockets);

			}
		}
	}

	/**
	 * Generates map of component and their previous components
	 * @param hydrographJob
	 * 					- {@link HydrographJob} to set
	 * @param eachComponentId
	 * 					- ComponentId to set
	 * @param outSockets
	 * 					- List of {@link TypeBaseOutSocket} to set
	 * @param inSockets
	 * 					- List of {@link TypeBaseInSocket} to set
	 */
	private static void generateComponentAndPreviousMap(HydrographJob hydrographJob, String eachComponentId,
			List<? extends TypeBaseOutSocket> outSockets, List<? extends TypeBaseInSocket> inSockets) {
		List<String> PreviousComponents = new ArrayList<String>();
		if (outSockets.size() == 0) {
			for (TypeBaseInSocket inSocket : inSockets) {
				addComponentAndSocketInMap(eachComponentId, "NoSocketId");
			}
		}
		for (TypeBaseOutSocket outSocket : outSockets) {
			addComponentAndSocketInMap(eachComponentId, outSocket.getId());
		}

		if (inSockets.size() == 0) {
			componentAndPreviousMap.put(eachComponentId, null);
		}
		componentAndPreviousMap.put(eachComponentId, PreviousComponents);
		for (TypeBaseInSocket currentComponentInSocket : inSockets) {
			List<TypeBaseComponent> allComponents = hydrographJob.getJAXBObject().getInputsOrOutputsOrStraightPulls();
			for (TypeBaseComponent previousComponent : allComponents) {
				List<? extends TypeBaseOutSocket> previousOutSockets = SocketUtilities
						.getOutSocketList(previousComponent);
				for (TypeBaseOutSocket previousOutSocket : previousOutSockets) {
					if (previousComponent.getId().equals(currentComponentInSocket.getFromComponentId())
							&& currentComponentInSocket.getFromSocketId().equals(previousOutSocket.getId())) {
						PreviousComponents.add(previousComponent.getId() + "_" + previousOutSocket.getId());
					}
				}
			}

		}
	}


	/**
	 * Create a map of component and socket
	 * @param componentId
	 * 				- componentId to set	
	 * @param socketId
	 * 				- socketId to set	
	 */
	private static void addComponentAndSocketInMap(String componentId, String socketId) {
		if (componentSocketMap.containsKey(componentId)) {
			List<String> sockets = componentSocketMap.get(componentId);
			sockets.add(socketId);
		} else {
			List<String> sockets = new ArrayList<String>();
			sockets.add(socketId);
			componentSocketMap.put(componentId, sockets);
		}
	}

	/**
	 * Generates a map of generated filter components
	 * @param generatedFilter
	 * 					- {@link} Filter to set
	 */
	public static void generateFilterList(Filter generatedFilter) {
		listOfFilterComponent.add(generatedFilter.getId());
	}


	/**
	 * @return the componentToPipeMapping map 
	 */
	public static Map<String, Pipe> getComponentToPipeMapping() {
		return componentToPipeMapping;
	}

	/**
	 * @return the componentSocketMap
	 */
	public static Map<String, List<String>> getComponentSocketMap() {
		return componentSocketMap;
	}

	/**
	 * @return the componentAndPreviousMap
	 */
	public static Map<String, List<String>> getComponentAndPreviousMap() {
		return componentAndPreviousMap;
	}
	
	/**
	 * @return the listOfFilterComponent map
	 */
	public static List<String> getListOfFilterComponent() {
		return listOfFilterComponent;
	}
}