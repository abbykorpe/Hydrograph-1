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
package hydrograph.engine.core.helper;

import hydrograph.engine.core.entity.Link;
import hydrograph.engine.core.entity.LinkInfo;
import hydrograph.engine.core.utilities.SocketUtilities;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeBaseOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeInputOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeOutputInSocket;
import hydrograph.engine.jaxb.inputtypes.SequenceInputFile;
import hydrograph.engine.jaxb.main.Graph;
import hydrograph.engine.jaxb.outputtypes.SequenceOutputFile;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JAXBTraversal {

	private Map<String, Link> linkMap;
	public static final String PHASE_INTERMEDIATE_INPUT_COMPONENT = "phase_intermediate_input";
	public static final String PHASE_INTERMEDIATE_OUTPUT_COMPONENT = "phase_intermediate_output";
	public static final String DEFAULT_OUT_SOCKET = "out0";
	public static final String DEFAULT_IN_SOCKET = "in0";
	private static Logger LOG = LoggerFactory.getLogger(JAXBTraversal.class);
	private SortedSet<BigInteger> flowCount = new TreeSet<BigInteger>();

	private List<TypeBaseComponent> jaxbGraph;
	private List<LinkInfo> phaseChangeOriginalLinks;
	private List<LinkInfo> phaseChangeLinks;
	private boolean isHiveComponentPresentInFlow = false;
	public JAXBTraversal(Graph graph) {
		
		this.phaseChangeOriginalLinks = new ArrayList<LinkInfo>();
		this.phaseChangeLinks = new ArrayList<LinkInfo>();
		
		jaxbGraph = graph.getInputsOrOutputsOrStraightPulls();
		identifyHiveComponentInFlow();
		populatePhase();
		populatePhaseChangeComponents();
		updateLinksAndComponents(graph);
	}


	private void identifyHiveComponentInFlow() {
		for (TypeBaseComponent component : jaxbGraph) {
			if(!isHiveComponentPresentInFlow && (component.getClass().getName().contains("Hive"))){
				isHiveComponentPresentInFlow = true;
				break;
			}
		}		
	}

	public Map<String, Link> getLinkMap() {
		return linkMap;
	}


	
	
	
	public List<? extends TypeBaseInSocket> getInputSocketFromComponentId(String componentId){
		for (TypeBaseComponent component : jaxbGraph) {
			if(component.getId().equals(componentId)) {
								List<? extends TypeBaseInSocket> inSocket = SocketUtilities.getInSocketList(component);
								return inSocket;
			}
		}
		
		throw new GraphTraversalException("InputSocet not present for the component with id: " + componentId);
	}
	
	public List<? extends TypeBaseOutSocket> getOutputSocketFromComponentId(String componentId){
		for (TypeBaseComponent component : jaxbGraph) {
			if(component.getId().equals(componentId)) {
								List<? extends TypeBaseOutSocket> outSocket = SocketUtilities.getOutSocketList(component);
								return outSocket;
			}
		}
		throw new GraphTraversalException("OutputSocet not present for the component with id: " + componentId);
	}
	
	public TypeBaseComponent getComponentFromComponentId(String componentId){
		for (TypeBaseComponent component : jaxbGraph) {
			if(component.getId().equals(componentId)) {
				return component;
			}
		}
		throw new GraphTraversalException("Component not present for the component id: " + componentId);
	}
	
	public List<String> getOrderedComponentsList(BigInteger phase) {
		HashMap<String, Integer> componentDependencies = new HashMap<String, Integer>();
		ArrayList<String> orderedComponents = new ArrayList<String>();
		Queue<String> resolvedComponents = new LinkedList<String>();
		Map<String, TypeBaseComponent> componentMap = new HashMap<String, TypeBaseComponent>();

		LOG.trace("Ordering components");
		for (TypeBaseComponent component : jaxbGraph) {
			
			if (!component.getPhase().equals(phase))
				continue;
			
			int intitialDependency = 0;

			intitialDependency = SocketUtilities.getInSocketList(component)
					.size();

			if (intitialDependency == 0) {
				resolvedComponents.add(component.getId());
			} else {
				componentDependencies
						.put(component.getId(), intitialDependency);
			}
			componentMap.put(component.getId(), component);
		}

		 if (resolvedComponents.isEmpty() && !componentDependencies.isEmpty()) {
			 throw new GraphTraversalException(
			 "Unable to find any source component to process for phase "
			 + phase + " in graph " + jaxbGraph);
		 }

		while (!resolvedComponents.isEmpty()) {
			// get resolved component
			String component = resolvedComponents.remove();

			// add to ordered list
			orderedComponents.add(component);
			LOG.trace("Added component: '" + component + "' to ordered list");
			// reduce the dependency of the components which are dependent on
			// this component
			List<? extends TypeBaseOutSocket> outSocketList = SocketUtilities
					.getOutSocketList(componentMap.get(component));

			for (TypeBaseOutSocket link : outSocketList) {
				// get the dependent component
				String targetComponent = getDependentComponent(link.getId(),
						component);
				
				if(targetComponent == null)
					throw new GraphTraversalException(
							"Unable to find Depenedent components in traversal for "
									+ component
									+ ". This may be due to circular dependecies or unlinked components. ");
				
				String dependentComponentID = targetComponent;

				// decrease the dependency by one
				Integer dependency = componentDependencies
						.get(dependentComponentID);
				dependency = dependency - 1;
				
				// if dependency is resolved then add it to resolved queue
				if (dependency == 0) {
					resolvedComponents.add(targetComponent);

					// also remove it from dependency pool
					componentDependencies.remove(dependentComponentID);
				} else {
					// else just update the dependency
					componentDependencies.put(dependentComponentID, dependency);
				}
			}
		}

		if (!componentDependencies.isEmpty()) {
			String components = "";
			for (String componentID : componentDependencies.keySet()) {
				components = components + ",  " + componentID;
			}

			throw new GraphTraversalException(
					"Unable to include following components in traversal"
							+ components
							+ ". This may be due to circular dependecies or unlinked components. Please inspect and remove circular dependencies.");
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Ordered component list: " + orderedComponents.toString());
		}
		return orderedComponents;
	}

	private String getDependentComponent(String socketId, String componentID) {
		for (TypeBaseComponent component : jaxbGraph) {
			List<? extends TypeBaseInSocket> inSocketList = SocketUtilities
					.getInSocketList(component);
			for (TypeBaseInSocket inSocket : inSocketList) {
				if (inSocket.getFromComponentId().equals(componentID)
						&& inSocket.getFromSocketId().equals(socketId))
					return component.getId();
			}
		}
		throw new GraphTraversalException("Dependent component not found for component with id '" + componentID + "' and socket id '" + socketId + "'");
	}

	private TypeBaseComponent getComponent(String componentID) {
		for (TypeBaseComponent component : jaxbGraph) {
				if (component.getId().equals(componentID))
					return component;
		}
		throw new GraphTraversalException("Component not found with id '" + componentID +"'");
	}
	
	public SortedSet<BigInteger> getFlowsNumber() {
		return flowCount;
	}

	private void populatePhase() {
		for (TypeBaseComponent component : jaxbGraph) {
			flowCount.add(component.getPhase());
		}
	}
	
	private void updateLinksAndComponents(Graph graph) {
		int counter = 0;
		
		for (LinkInfo link2 : phaseChangeLinks) {
			TypeBaseComponent targetComponent = this.getComponentFromComponentId(link2.getComponentId());
			TypeBaseInSocket inSocket = link2.getInSocket();
			
			String sequenceInputComponentId = targetComponent.getId() + "_" + counter + "_phase_" + targetComponent.getPhase();

			SequenceInputFile jaxbSequenceInputFile = new SequenceInputFile();
			jaxbSequenceInputFile.setId(sequenceInputComponentId);
			jaxbSequenceInputFile.setPhase(targetComponent.getPhase());

			TypeInputOutSocket outputSocket = new TypeInputOutSocket();
			outputSocket.setId(DEFAULT_OUT_SOCKET);
			
			jaxbSequenceInputFile.getOutSocket().add(outputSocket);
			
			graph.getInputsOrOutputsOrStraightPulls().add(jaxbSequenceInputFile);

			//TypeBaseInSocket newInSocket = getCopyOfInSocket(inSocket);
			
			inSocket.setFromComponentId(sequenceInputComponentId);
			inSocket.setFromSocketId(DEFAULT_OUT_SOCKET);
			
			//SocketUtilities.replaceInSocket(targetComponent, inSocket.getId(), newInSocket);
			counter++;
			
			TypeBaseComponent sourceComponent = this.getComponentFromComponentId(link2.getSourceComponentId());
			
			TypeBaseOutSocket outSocket = link2.getOutSocket();
			
			SequenceOutputFile jaxbSequenceOutputFile = new SequenceOutputFile();
			
			String sequenceOutputComponentId = sourceComponent.getId() + "_" + counter + "_phase_" + sourceComponent.getPhase();
			jaxbSequenceOutputFile.setId(sequenceOutputComponentId);
			
			jaxbSequenceOutputFile.setPhase(sourceComponent.getPhase());
			
			TypeOutputInSocket outputInSocket = new TypeOutputInSocket();
			
			outputInSocket.setFromComponentId(sourceComponent.getId());
			outputInSocket.setFromSocketId(outSocket.getId());
			outputInSocket.setId(DEFAULT_IN_SOCKET);
			
			jaxbSequenceOutputFile.getInSocket().add(outputInSocket);
			
			graph.getInputsOrOutputsOrStraightPulls().add(jaxbSequenceOutputFile);

			counter++;
			
			addInSocketToOriginalLinks(sourceComponent.getId(), outSocket.getId(), inSocket);
			addOutSocketToOriginalLinks(targetComponent.getId(), inSocket.getId(), outputSocket);
			
		}
	}
	
	private void addInSocketToOriginalLinks(String sourceComponentId, String outSocketId, TypeBaseInSocket inSocket) {
		for (LinkInfo link2 : phaseChangeOriginalLinks) {
			if(link2.getOutSocketId().equals(outSocketId) && link2.getSourceComponentId().equals(sourceComponentId)){
				link2.setInSocket(inSocket);
			}
		}
		
	}


	private void addOutSocketToOriginalLinks(String targetComponentId, String inSocketPortId, TypeInputOutSocket outputSocket) {
		for (LinkInfo link2 : phaseChangeOriginalLinks) {
			if(link2.getInSocketId().equals(inSocketPortId) && link2.getComponentId().equals(targetComponentId)){
				link2.setOutSocket(outputSocket);
			}
		}
		
	}


	private void populatePhaseChangeComponents() {
		
		for (TypeBaseComponent component : jaxbGraph) {
			List<? extends TypeBaseInSocket> inSocketList = SocketUtilities
					.getInSocketList(component);
			
			BigInteger phase = component.getPhase();

			for (TypeBaseInSocket inSocket : inSocketList) {
				// get the dependent component
				TypeBaseComponent sourceComponent = getComponent(inSocket.getFromComponentId());
				if(sourceComponent.getPhase().compareTo(phase) > 0){
					throw new GraphTraversalException(
							"Phase of source component cannot be greator then target component. Source component "
									+ sourceComponent.getId()
									+ " has phase "
									+  sourceComponent.getPhase()
									+ " and target component "
									+ component.getId()
									+ " has phase "
									+ phase);
				} 

				TypeBaseOutSocket outSocket = SocketUtilities
						.getOutSocket(sourceComponent, inSocket.getFromSocketId());
				
				LinkInfo link = new LinkInfo(component.getId(), inSocket.getId(), inSocket, sourceComponent.getId(), outSocket.getId(), outSocket);

				if(sourceComponent.getPhase().compareTo(phase) < 0){
					phaseChangeLinks.add(link);
				}
				phaseChangeOriginalLinks.add(link);
			}
		}
	}

	
	public class GraphTraversalException extends RuntimeException {

		private static final long serialVersionUID = -2396594973435552339L;

		public GraphTraversalException(String msg) {
			super(msg);
		}

		public GraphTraversalException(Throwable e) {
			super(e);
		}

		public GraphTraversalException(String msg, Throwable e) {
			super(msg, e);
		}
	}


	public LinkInfo getPhaseLinkFromInputSocket(TypeBaseInSocket typeBaseInSocket) {
		for (LinkInfo link2 : phaseChangeOriginalLinks) {
			if(link2.getSourceComponentId().equals(typeBaseInSocket.getFromComponentId()) &&
					link2.getOutSocketId().equals(typeBaseInSocket.getFromSocketId()))
				return link2;
		}
		throw new GraphTraversalException("Link not found for input socket: " + typeBaseInSocket.toString());
	}

	public LinkInfo getPhaseLinkFromOutputSocket(TypeBaseOutSocket typeBaseOutSocket) {
		for (LinkInfo link2 : phaseChangeOriginalLinks) {
			if(link2.getOutSocket().equals(typeBaseOutSocket))
				return link2;
		}
		
		throw new GraphTraversalException("Link not found for output socket: " + typeBaseOutSocket.toString());
	}
	
	public LinkInfo getPhaseLinkFromOutputSocket(String componentId, TypeBaseOutSocket typeBaseOutSocket) {
		for (LinkInfo link2 : phaseChangeOriginalLinks) {
			if(link2.getInSocket().getFromComponentId().equals(componentId) && link2.getOutSocket().getId().equals(typeBaseOutSocket.getId()))
				return link2;
		}
		throw new GraphTraversalException("Link not found for output socket: " + typeBaseOutSocket.toString());
	}

	public boolean isHiveComponentPresentInFlow() {
		return isHiveComponentPresentInFlow;
	}
}