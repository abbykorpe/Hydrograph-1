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
import hydrograph.engine.core.entity.LinkInfo;
import hydrograph.engine.core.helper.JAXBTraversal;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeBaseOutSocket;

public class ComponentPipeMapping {

	private static List<String> actualComponents = new ArrayList<String>();
	private static Map<String, List<String>> componentSocketMap = new HashMap<String, List<String>>();
	private static Map<String, String> comopnentsAndFilterMap = new HashMap<String, String>();
	private static Map<String, Pipe> componentToPipeMapping = new HashMap<String, Pipe>();
	private static List<String> filterList = new ArrayList<String>();

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
				generateComponentFilterMapForInputSocket(jaxbTraversal, eachComponentId, outSockets, inSockets);
				generateComponentFilterMapForOutputSocket(jaxbTraversal, eachComponentId, outSockets);

			}
		}
	}

	private static void generateComponentFilterMapForOutputSocket(JAXBTraversal jaxbTraversal, String eachComponentId,
			List<? extends TypeBaseOutSocket> outSockets) {
		for (TypeBaseOutSocket typeBaseOutSocket : outSockets) {
			addComponentAndSocketInMap(eachComponentId, typeBaseOutSocket.getId());

			LinkInfo linkInfo = jaxbTraversal.getPhaseLinkFromOutputSocket(eachComponentId, typeBaseOutSocket);
			if (!filterList.contains(eachComponentId)) {
				filterList.add(linkInfo.getComponentId());
				actualComponents.add(linkInfo.getSourceComponentId() + "_" + linkInfo.getOutSocketId());
				comopnentsAndFilterMap.put(linkInfo.getSourceComponentId() + "_" + linkInfo.getOutSocketId(),
						linkInfo.getComponentId() + "_out0");
			}
		}
	}

	private static void generateComponentFilterMapForInputSocket(JAXBTraversal jaxbTraversal, String eachComponentId,
			List<? extends TypeBaseOutSocket> outSockets, List<? extends TypeBaseInSocket> inSockets) {
		if (outSockets.size() == 0) {
			for (TypeBaseInSocket typeBaseInSocket : inSockets) {
				addComponentAndSocketInMap(eachComponentId, typeBaseInSocket.getId());

				LinkInfo linkInfo = jaxbTraversal.getPhaseLinkFromInputSocket(typeBaseInSocket);
				if (filterList.contains(linkInfo.getSourceComponentId())) {
					actualComponents.add(linkInfo.getComponentId() + "_" + linkInfo.getInSocketId());
					comopnentsAndFilterMap.put(linkInfo.getComponentId() + "_" + linkInfo.getInSocketId(),
							linkInfo.getSourceComponentId() + "_out0");
				}
			}
		}
	}

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

	public static List<String> getActualComponents() {
		return actualComponents;
	}

	public static List<String> getHydrographFilterComponents() {
		return filterList;
	}

	public static Map<String, Pipe> getComponentToPipeMapping() {
		return componentToPipeMapping;
	}

	public static Map<String, List<String>> getComponentSocketMap() {
		return componentSocketMap;
	}

	public static Map<String, String> getComopnentsAndFilterMap() {
		return comopnentsAndFilterMap;
	}
}
