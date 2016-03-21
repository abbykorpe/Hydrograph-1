/********************************************************************************
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
 ******************************************************************************/

 
package com.bitwise.app.graph.schema.propagation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.ComponentsOutputSchema;
import com.bitwise.app.common.datastructure.property.FixedWidthGridRow;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.logging.factory.LogFactory;

/**
 * This class is used to propagate schema.
 * 
 * @author Bitwise
 * 
 */
public class SchemaPropagation {
	public static final SchemaPropagation INSTANCE = new SchemaPropagation();
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(SchemaPropagation.class);
	private ComponentsOutputSchema componentsOutputSchema;
	private List<Link> componentsLinkList = new ArrayList<>();
	private List<Link> mainLinkList = new ArrayList<>();

	private SchemaPropagation() {
	}

	/**
	 * This method propagates component's schema-map to its successor components.
	 * 
	 * @param component
	 * @param schemaMap
	 */
	public void continuousSchemaPropagation(Component component, Map<String, ComponentsOutputSchema> schemaMap) {
		LOGGER.debug("Initiating recursive schema propagation");

		if (component != null && schemaMap != null)
			if (StringUtils.equals(Constants.SUBGRAPH_COMPONENT_CATEGORY, component.getCategory()))
				appplySchemaToTargetComponentsFromSchemaMap(component, schemaMap, Constants.FIXED_OUTSOCKET_ID);
			else
				appplySchemaToTargetComponentsFromSchemaMap(component, schemaMap, null);

		flushLinkLists();
	}

	private void flushLinkLists() {
		mainLinkList.clear();
		componentsLinkList.clear();
	}

	private void appplySchemaToTargetComponentsFromSchemaMap(Component destinationComponent,
			Map<String, ComponentsOutputSchema> schemaMap, String targetTerminal) {

		if (StringUtils.isNotEmpty(targetTerminal)) {
			applySchemaToTargetComponents(destinationComponent, targetTerminal, schemaMap.get(targetTerminal));

		} else {
			applySchemaToTargetComponents(destinationComponent, null, schemaMap.get(Constants.FIXED_OUTSOCKET_ID));
		}
	}

	private void applySchemaToTargetComponents(Component destinationComponent, String targetTerminal,
			ComponentsOutputSchema componentsOutputSchema) {
		LOGGER.debug("Applying Schema to :" + destinationComponent.getComponentLabel());
		if (!isInputSubGraphComponent(destinationComponent)
				&& StringUtils.equals(Constants.SUBGRAPH_COMPONENT_CATEGORY, destinationComponent.getCategory())
				&& targetTerminal != null) {
			propagateSchemaFromSubgraph(destinationComponent, targetTerminal, componentsOutputSchema);
		}
		setSchemaMapOfComponent(destinationComponent, componentsOutputSchema);
		if (destinationComponent != null && destinationComponent.getSourceConnections().isEmpty()) {
//			mainLinkList.clear();
			return;
		}
		if (!StringUtils.equals(Constants.SUBGRAPH_COMPONENT_CATEGORY, destinationComponent.getCategory())
				|| isInputSubGraphComponent(destinationComponent)) {
			for (Link link : destinationComponent.getSourceConnections()) {
				applySchemaToLinkedComponents(link,componentsOutputSchema);
			}
		}
	}

	
	private void applySchemaToLinkedComponents(Link link, ComponentsOutputSchema componentsOutputSchema) {
		if ((!(Constants.TRANSFORM.equals(link.getTarget().getCategory()) & !Constants.FILTER.equalsIgnoreCase(link
				.getTarget().getComponentName())) && !link.getTarget().getProperties()
				.containsValue(componentsOutputSchema))) {
			if (!checkUnusedSocketAsSourceTerminal(link))
				applySchemaToTargetComponents(link.getTarget(), link.getTargetTerminal(), componentsOutputSchema);
			else {
				getComponentsOutputSchema(link);
				applySchemaToTargetComponents(link.getTarget(), link.getTargetTerminal(), this.componentsOutputSchema);
			}
		} else if (Constants.UNIQUE_SEQUENCE.equals(link.getTarget().getComponentName())) {
			propagateSchemForUniqueSequenceComponent(link.getTarget(), componentsOutputSchema);
		} else {
			for (Link link2 : link.getTarget().getSourceConnections()) {
				if (!isMainLinkChecked(link2)) {
					if (checkUnusedSocketAsSourceTerminal(link2) && getComponentsOutputSchema(link2) != null) {
						applySchemaToLinkedComponents(link2,this.componentsOutputSchema);
					} else
						propagatePassThroughAndMapFields(link);
				} else
					break;
			}
		}
	}
	
	
	private ComponentsOutputSchema getComponentsOutputSchemaFromMap(Component destinationComponent,
			Link link, ComponentsOutputSchema componentsOutputSchema) {
		Map<String , ComponentsOutputSchema> schemaMap=(Map<String, ComponentsOutputSchema>) destinationComponent.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
		if(isInputSubGraphComponent(destinationComponent) && schemaMap!=null){
			componentsOutputSchema = schemaMap.get(link.getSourceTerminal());	
		}
		return componentsOutputSchema ;
	}

	private boolean isInputSubGraphComponent(Component component) {
		if (StringUtils.equals(Constants.INPUT_SUBGRAPH, component.getComponentName()))
			return true;
		return false;
	}

	private void setSchemaMapOfComponent(Component component, ComponentsOutputSchema componentsOutputSchema) {
		if(StringUtils.equals(Constants.OFIXED_WIDTH, component.getComponentName()))
		{
			updateComponentStatus(component,componentsOutputSchema);
		}
		if (!StringUtils.equals(Constants.SUBGRAPH_COMPONENT_CATEGORY, component.getCategory())) {
			Map<String, ComponentsOutputSchema> tempSchemaMap = new LinkedHashMap<String, ComponentsOutputSchema>();
			tempSchemaMap.put(Constants.FIXED_OUTSOCKET_ID, componentsOutputSchema);
			component.getProperties().put(Constants.SCHEMA_TO_PROPAGATE, tempSchemaMap);
		} 
	}

	private void updateComponentStatus(Component component, ComponentsOutputSchema componentsOutputSchema) {
		for (FixedWidthGridRow fixedWidthGridRow : componentsOutputSchema.getFixedWidthGridRowsOutputFields()) {
			if (StringUtils.isBlank(fixedWidthGridRow.getLength())) {
				component.getProperties().put(Constants.VALIDITY_STATUS, Constants.ERROR);
			}
		}
	}

	private void propagateSchemaFromSubgraph(Component subGraphComponent, String targetTerminal,
			ComponentsOutputSchema componentsOutputSchema) {
		String outPutTargetTerminal = getTagetTerminalForSubgraph(targetTerminal);

		if (subGraphComponent.getProperties().get(Constants.INPUT_SUBGRAPH) != null) {
			Component inputSubgraphComponent = (Component) subGraphComponent.getProperties().get(
					Constants.INPUT_SUBGRAPH);
			Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) inputSubgraphComponent
					.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
			schemaMap.put(outPutTargetTerminal, componentsOutputSchema);
			for (Link link : inputSubgraphComponent.getSourceConnections()) {
				if (StringUtils.equals(link.getSourceTerminal(), outPutTargetTerminal))
					 applySchemaToLinkedComponents(link,componentsOutputSchema);
			}
		}

		else if (StringUtils.equals(Constants.OUTPUT_SUBGRAPH, subGraphComponent.getComponentName())) {

			// appplySchemaToTargetComponentsFromSchemaMap((Component)
			// subGraphComponent.getProperties().get(Constants.SUBGRAPH_COMPONENT), schemaMap,
			// getTagetTerminalForSubgraph(targetTerminal));
			propagateSchemaFromOutputSubgraphComponent(subGraphComponent, outPutTargetTerminal, componentsOutputSchema);

		}
	}

	private boolean isOutputSubgraphComponent(Component subGraphComponent) {
		if (StringUtils.equals(Constants.OUTPUT_SUBGRAPH, subGraphComponent.getComponentName()))
			return true;
		return false;
	}

	private void propagateSchemForUniqueSequenceComponent(Component component,
			ComponentsOutputSchema previousComponentOutputSchema) {
		FixedWidthGridRow fixedWidthGridRow = null;
		Map<String, ComponentsOutputSchema> tempSchemaMap = (Map<String, ComponentsOutputSchema>) component
				.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
		if (tempSchemaMap == null)
			tempSchemaMap = new LinkedHashMap<>();
		ComponentsOutputSchema uniqeSequenceOutputSchema = tempSchemaMap.get(Constants.FIXED_OUTSOCKET_ID);
		if (uniqeSequenceOutputSchema != null
				&& !uniqeSequenceOutputSchema.getFixedWidthGridRowsOutputFields().isEmpty()) {
			fixedWidthGridRow = uniqeSequenceOutputSchema.getFixedWidthGridRowsOutputFields().get(
					uniqeSequenceOutputSchema.getFixedWidthGridRowsOutputFields().size() - 1);
			uniqeSequenceOutputSchema.copySchemaFromOther(previousComponentOutputSchema);
			if (!uniqeSequenceOutputSchema.getFixedWidthGridRowsOutputFields().contains(fixedWidthGridRow))
				uniqeSequenceOutputSchema.getFixedWidthGridRowsOutputFields().add(fixedWidthGridRow);
			applySchemaToTargetComponents(component, null, uniqeSequenceOutputSchema);
		} else
			for (Link linkFromCurrentComponent : component.getTargetConnections()) {
				applySchemaToTargetComponents(linkFromCurrentComponent.getTarget(), null, previousComponentOutputSchema);
			}
	}

	private void propagatePassThroughAndMapFields(Link link) {
		boolean toPropagate = false;
		
		ComponentsOutputSchema sourceOutputSchema = getComponentsOutputSchema(link);
		ComponentsOutputSchema targetOutputSchema = getTargetComponentsOutputSchemaFromMap(link);
		if (targetOutputSchema != null && !targetOutputSchema.getPassthroughFields().isEmpty()) {
			targetOutputSchema.updatePassthroughFieldsSchema(sourceOutputSchema, link.getTargetTerminal());
			toPropagate = true;
		}
		if (targetOutputSchema != null && !targetOutputSchema.getMapFields().isEmpty()) {
			targetOutputSchema.updateMapFieldsSchema(sourceOutputSchema, link.getTargetTerminal());
			toPropagate = true;
		}
		if (toPropagate)
			applySchemaToTargetComponents(link.getTarget(), null, targetOutputSchema);
	}

	/**
	 * This method retrieves schema from source component
	 * 
	 * @param link
	 * @return ComponentsOutputSchema, the componentsOutputSchema is output schema of component.
	 */
	public ComponentsOutputSchema getComponentsOutputSchema(Link link) {
		LOGGER.debug("Getting Source Output Schema for component.");
		this.componentsOutputSchema = null;
		getSourceSchemaForUnusedPorts(link);
		componentsLinkList.clear();
		return this.componentsOutputSchema;
	}

	private void getSourceSchemaForUnusedPorts(Link link) {
		LOGGER.debug("Reverse propagation for fetching source schema for component.");
		String socketId = link.getSourceTerminal();
		if (isLinkChecked(link))
			return;
		if (!checkUnusedSocketAsSourceTerminal(link)) {
			this.componentsOutputSchema = getSourceComponentsOutputSchemaFromMap(link);
			return;
		}
		for (Link link2 : link.getSource().getTargetConnections()) {
			if (link2.getTargetTerminal().equals(getInSocketForUnusedSocket(socketId))) {
				getSourceSchemaForUnusedPorts(link2);
			}
		}

	}

	private boolean isLinkChecked(Link link) {
		if (componentsLinkList.contains(link)) {
			componentsLinkList.clear();
			return true;
		}
		componentsLinkList.add(link);
		return false;
	}

	private boolean isMainLinkChecked(Link link) {
		if (mainLinkList.contains(link)) {
			return true;
		}
		mainLinkList.add(link);
		return false;
	}

	private String getInSocketForUnusedSocket(String unusedSocketId) {
		String unusedPortNo = unusedSocketId.substring(6);
		String inSocket = Constants.INPUT_SOCKET_TYPE + unusedPortNo;
		return inSocket;
	}

	private boolean checkUnusedSocketAsSourceTerminal(Link link) {
		LOGGER.debug("Checking whether link is connected to unused port");
		if (link.getSource().getPort(link.getSourceTerminal()) != null
				&& link.getSource().getPort(link.getSourceTerminal()).getPortType()
						.equals(Constants.UNUSED_SOCKET_TYPE))
			return true;
		return false;
	}

	private ComponentsOutputSchema getSourceComponentsOutputSchemaFromMap(Link link) {
		ComponentsOutputSchema componentsOutputSchema = null;
		if (link != null && link.getSource() != null) {
			Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) link.getSource()
					.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
			if (schemaMap != null && schemaMap.get(link.getSourceTerminal()) != null)
				componentsOutputSchema = schemaMap.get(link.getSourceTerminal());
		}
		return componentsOutputSchema;
	}

	private ComponentsOutputSchema getTargetComponentsOutputSchemaFromMap(Link link) {
		ComponentsOutputSchema componentsOutputSchema = null;
		if (link != null && link.getTarget() != null) {
			Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) link.getTarget()
					.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
			if (schemaMap != null && schemaMap.get(Constants.FIXED_OUTSOCKET_ID) != null)
				componentsOutputSchema = schemaMap.get(Constants.FIXED_OUTSOCKET_ID);
		}
		return componentsOutputSchema;
	}

	private String getTagetTerminalForSubgraph(String targetTerminal) {
		String targetTerminalForSubgraph = Constants.FIXED_OUTSOCKET_ID;
		if (StringUtils.isNotEmpty(targetTerminal))
			targetTerminalForSubgraph = targetTerminal.replace(Constants.INPUT_SOCKET_TYPE,
					Constants.OUTPUT_SOCKET_TYPE);
		return targetTerminalForSubgraph;

	}

	private void propagateSchemaFromOutputSubgraphComponent(Component outputSubraphComponent, String targetTerminal,
			ComponentsOutputSchema componentsOutputSchema) {

		Component parentSubgraph = (Component) outputSubraphComponent.getProperties().get(Constants.SUBGRAPH_COMPONENT);
		Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) outputSubraphComponent
				.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
		if (schemaMap != null)
			schemaMap.put(targetTerminal, componentsOutputSchema);
		if (parentSubgraph != null) {
			for (Link link : parentSubgraph.getSourceConnections()) {
				if (StringUtils.equals(link.getSourceTerminal(), targetTerminal))
					 applySchemaToLinkedComponents(link,componentsOutputSchema);
			}
		}
	}
}
